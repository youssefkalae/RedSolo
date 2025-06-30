package cs3500.solored.model.hw04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedCard;

/**
 * Abstract class that implements the original Red Game Model interface to keep code efficiency.
 */
public abstract class AbstractGameModel implements RedGameModel<SoloRedCard> {


  protected List<SoloRedCard> deck;
  protected List<List<SoloRedCard>> palettes;
  protected SoloRedCard canvas;
  protected List<SoloRedCard> hand;
  private int numPalettes;
  protected int maxHandSize;
  protected Random random;
  protected boolean gameStarted;
  protected boolean canvasPlayedThisTurn;

  protected int lastPlayedPalette;
  protected boolean gameEnd;

  /**
   * This method calls a different constructor taking a random object.
   * As the game has not yet started, random values are generated for the RedGame.
   */
  public AbstractGameModel() {
    this(new Random());
    this.gameStarted = false;
    this.random = new Random();
    gameEnd = false;

  }

  /**
   * This class initializes a SoloRedGameModel by starting with a random object for shuffles.
   * Throws an exception if the random object is null.
   */
  public AbstractGameModel(Random random) {
    if (random == null) {
      throw new IllegalArgumentException("Random object cannot be null");
    }
    this.random = random;
    this.gameStarted = false;
    gameEnd = false;
  }

  protected void checkCanvasExceptions(int cardIdxInHand) {
    if (!gameStarted || gameEnd) {
      throw new IllegalStateException("Game has not started or is over");
    }
    if (cardIdxInHand < 0 || cardIdxInHand >= hand.size()) {
      throw new IllegalArgumentException("Invalid card index in hand");
    }
    if (canvasPlayedThisTurn) {
      throw new IllegalStateException("Canvas already played this turn");
    }
    if (hand.size() == 1) {
      throw new IllegalStateException("Cannot play last card to canvas");
    }
  }


  @Override
  public void playToPalette(int paletteIdx, int cardIdxInHand) {
    if (!gameStarted || gameEnd) {
      throw new IllegalStateException("Game has not started or is over");
    }
    if (paletteIdx < 0 || paletteIdx >= numPalettes) {
      throw new IllegalArgumentException("Invalid palette index");
    }
    if (cardIdxInHand < 0 || cardIdxInHand >= hand.size()) {
      throw new IllegalArgumentException("Invalid card index in hand");
    }
    if (paletteIdx == winningPaletteIndex()) {
      throw new IllegalStateException("Cannot play to winning palette");
    }

    // Remove card from hand to add to palette
    SoloRedCard card = hand.remove(cardIdxInHand);
    palettes.get(paletteIdx).add(card);

    lastPlayedPalette = paletteIdx;
    gameEnd = isGameOver();
  }

  private List<Card> getPaletteScore(List<SoloRedCard> palette, SoloRedCard.Color rule) {
    switch (rule) {
      case RED:
        return Collections.singletonList(getHighestCard(palette));
      case ORANGE:
        return getMostOfSameNumber(palette);
      case BLUE:
        return getUniqueColors(palette);
      case INDIGO:
        return getLongestRun(palette);
      case VIOLET:
        return getCardsBelowFour(palette);
      default:
        throw new IllegalStateException("Unknown color rule");
    }
  }

  private SoloRedCard getHighestCard(List<SoloRedCard> palette) {
    return palette.stream()
            .max(Comparator.comparing(SoloRedCard::getNumber)
                    // Finds the cards with the highest value
                    .thenComparing(card -> card.getColor().getRedScore()))
            // Only happens if there are multiple cards with the same value
            .get(); // Can ignore warning bc palette will never be empty
  }


  private List<Card> getMostOfSameNumber(List<SoloRedCard> palette) {
    // R1 O5 I3 O3
    List<Integer> instances = palette.stream()
            .map(SoloRedCard::getNumber)
            .collect(Collectors.toList());
    // 1 5 3 3

    Map<Integer, Long> numberCount = instances.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    // 1, 1
    // 5, 1
    // 3, 2

    int key = Collections.max(numberCount.entrySet(), Map.Entry.comparingByValue()).getKey();

    List<Integer> cardIndexes = new ArrayList<>();
    for (int i = 0; i < instances.size(); i++) {
      if (instances.get(i) == key) {
        cardIndexes.add(i);
      }
    }
    List<Card> sameNumCards = new ArrayList<>();
    for (Integer idx : cardIndexes) {
      sameNumCards.add(palette.get(idx));
    }
    return sameNumCards;
  }


  private List<Card> getUniqueColors(List<SoloRedCard> palette) {
    // R1 R2. Map to track highest # card for each color
    Map<SoloRedCard.Color, SoloRedCard> mapCard = new HashMap<>();
    for (SoloRedCard card : palette) { // Iterate through each card in palette
      SoloRedCard.Color currentColor = card.getColor();
      if (!mapCard.containsKey(currentColor) // If the color is not in map, or card number not
              // Highest one in map, update map with new card
              || card.getNumber() > mapCard.get(currentColor).getNumber()) {
        mapCard.put(currentColor, card);
      }
    }
    return new ArrayList<>(mapCard.values()); // List of highest numbered card for each color
  }

  private List<Card> getLongestRun(List<SoloRedCard> palette) {
    // Sort cards in the palette by number (in order)
    List<SoloRedCard> sortedPalette = new ArrayList<>(palette);
    sortedPalette.sort(Comparator.comparingInt(SoloRedCard::getNumber));

    // Two lists initialized for maximum run and current run
    List<Card> maxRun = new ArrayList<>();
    List<Card> currentRun = new ArrayList<>();
    currentRun.add(sortedPalette.get(0)); // Add first card to the current run to begin

    // Iterating over the palette starting from second card
    for (int i = 1; i < sortedPalette.size(); i++) {
      // If card number is exactly one more than other, it's still in same run
      if (sortedPalette.get(i).getNumber() == sortedPalette.get(i - 1).getNumber() + 1) {
        currentRun.add(sortedPalette.get(i));
      } else {
        // If current run is longer than the max one, update max with current
        if (currentRun.size() > maxRun.size() || currentRun.size() == maxRun.size()) {
          maxRun = new ArrayList<>(currentRun);
        }
        // Resetting current run with new card
        currentRun.clear();
        currentRun.add(sortedPalette.get(i));
      }
    }
    // compare final run to the max run after looping if applicable
    if (currentRun.size() > maxRun.size() || currentRun.size() == maxRun.size()) {
      maxRun = new ArrayList<>(currentRun);
    }

    return maxRun;
  }

  private List<Card> getCardsBelowFour(List<SoloRedCard> palette) {
    return palette.stream().filter(card -> card.getNumber() < 4).collect(Collectors.toList());
  }


  // Starts new game with provided deck. Exceptions are made as stated in the interface
  @Override
  public void startGame(List<SoloRedCard> deck, boolean shuffle, int numPalettes, int handSize) {
    if (gameStarted) {
      throw new IllegalStateException("Game has already started");
    } else if (deck == null) {
      throw new IllegalArgumentException("Invalid game parameter");
    } else if (handSize <= 0) {
      throw new IllegalArgumentException("Invalid game parameter");
    } else if (numPalettes < 2) {
      throw new IllegalArgumentException("Invalid game parameter");
    } else if (deck.isEmpty()) {
      throw new IllegalArgumentException("Invalid game parameter");
    } else if (deck.size() < numPalettes + handSize) {
      throw new IllegalArgumentException("Deck size is not large enough to setup the game");
    } else if (hasDuplicatesOrNulls(deck)) {
      throw new IllegalArgumentException("Deck has non-unique cards or null cards");
    }

    this.deck = new ArrayList<>(deck);
    if (shuffle) {
      Collections.shuffle(this.deck, random);
    }

    this.numPalettes = numPalettes;
    this.maxHandSize = handSize;

    // ets up palettes and initial hand
    palettes = new ArrayList<>();
    for (int i = 0; i < numPalettes; i++) {
      palettes.add(new ArrayList<>());
      palettes.get(i).add(this.deck.remove(0)); // First card to each palette
    }
    canvas = new SoloRedCard(SoloRedCard.Color.RED, 1); // Default canvas card
    hand = new ArrayList<>();
    for (int i = 0; i < handSize; i++) {
      hand.add(this.deck.remove(0));
    }
    canvasPlayedThisTurn = false;
    this.gameStarted = true;
    lastPlayedPalette = winningPaletteIndex();
  }

  // To check whether deck has duplicates or null cards
  private boolean hasDuplicatesOrNulls(List<SoloRedCard> deck) {
    Set<SoloRedCard> uniqueCards = new HashSet<>();
    for (SoloRedCard card : deck) {
      if (card == null) { // Check card is null?
        return true;
      }
      if (!uniqueCards.add(card)) {
        return true;
      }
    }
    return false; // When no duplicates or nulls found
  }

  @Override
  public int numOfCardsInDeck() {
    if (!gameStarted) {
      throw new IllegalStateException("numCards Game has not started");
    }
    return deck.size();
  }

  @Override
  public int numPalettes() {
    if (!gameStarted) {
      throw new IllegalStateException("numPalettes Game has not started");
    }
    return numPalettes;
  }

  @Override
  public int winningPaletteIndex() {
    if (!gameStarted) {
      throw new IllegalStateException("winningPalette Game has not started");
    }
    return determineWinningPalette();
  }

  private int determineWinningPalette() {

    SoloRedCard.Color canvasColor = canvas.getColor(); // Gets color for rule

    List<Integer> paletteScores = new ArrayList<>(); // New list to detect score

    for (List<SoloRedCard> p : palettes) {
      paletteScores.add(getPaletteScore(p, canvasColor).size());
    }

    int maxScore = Collections.max(paletteScores); // Max score from list of palette

    int winningPaletteIndex = paletteScores.indexOf(maxScore); // Index of palette with max score

    if (Collections.frequency(paletteScores, maxScore) > 1) {
      List<SoloRedCard> highestCardInPalette = new ArrayList<>();
      SoloRedCard trueHighestCard = getHighestCard(palettes.get(winningPaletteIndex)); // Tie?
      for (int i = 0; i < paletteScores.size(); i++) {
        if (paletteScores.get(i) == maxScore) { // Highest card found?
          highestCardInPalette.add(getHighestCard(palettes.get(i)));
          trueHighestCard = getHighestCard(highestCardInPalette);
        }
      }
      for (List<SoloRedCard> p : palettes) {
        if (p.contains(trueHighestCard)) {
          winningPaletteIndex = palettes.indexOf(p);
        } // Identifies the palette that contains highest card and assigns it as winning palette
      }
    }
    return winningPaletteIndex; // Returns index of the palette winning

  }

  @Override
  public boolean isGameOver() {
    if (!gameStarted) {
      throw new IllegalStateException("isGameOver Game has not started");
    }

    return (lastPlayedPalette != winningPaletteIndex() || deck.isEmpty() && hand.isEmpty());
  }

  @Override
  public boolean isGameWon() {
    if (!gameStarted) {
      throw new IllegalStateException("isGameWon Game has not started");
    }
    if (!gameEnd) {
      throw new IllegalStateException("Game is not over");
    }
    return (lastPlayedPalette == winningPaletteIndex());
  }

  @Override
  public List<SoloRedCard> getHand() {
    if (!gameStarted) {
      throw new IllegalStateException("getHand Game has not started");
    }
    return new ArrayList<>(hand);
  }

  @Override
  public List<SoloRedCard> getPalette(int paletteNum) {
    if (!gameStarted) {
      throw new IllegalStateException("getPalette Game has not started");
    }
    if (paletteNum < 0 || paletteNum >= numPalettes) {
      throw new IllegalArgumentException("Invalid palette index");
    }
    return new ArrayList<>(palettes.get(paletteNum));
  }

  @Override
  public SoloRedCard getCanvas() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return new SoloRedCard(canvas.getColor(), canvas.getNumber());
  } // Creates new card object but no modification made

  @Override
  public List<SoloRedCard> getAllCards() {
    List<SoloRedCard> allCards = new ArrayList<>(); // Creates list that's empty to hold cards
    for (SoloRedCard.Color color : SoloRedCard.Color.values()) { // Iterator for colors
      for (int number = 1; number <= 7; number++) { // Loops through numbers in iterator of colors
        allCards.add(new SoloRedCard(color, number)); // Creates each color and number combo
      }
    }
    return new ArrayList<>(allCards); // Returns copy of allCards list that we made/generated
  }
}
