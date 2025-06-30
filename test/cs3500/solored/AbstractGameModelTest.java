package cs3500.solored;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.solored.model.hw02.SoloRedCard;
import cs3500.solored.model.hw02.SoloRedGameModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Abstract test class testing for common implement methods from basic and advanced models .
 */
public class AbstractGameModelTest {
  private SoloRedGameModel model;
  private List<SoloRedCard> fullDeck;

  @Before
  public void setUp() {
    model = new SoloRedGameModel();
    fullDeck = model.getAllCards();
  }

  @Test
  public void testStartGame() {
    model.startGame(fullDeck, false, 4, 7);
    assertEquals(4, model.numPalettes());
    assertEquals(7, model.getHand().size());
    assertEquals(fullDeck.size() - 4 - 7, model.numOfCardsInDeck());
    assertThrows(IllegalStateException.class, () -> {
      model.startGame(fullDeck, false, 4, 7);
    });
    assertThrows(IllegalStateException.class, () -> {
      model.startGame(fullDeck, false, 4, 7);
    });
    model = new SoloRedGameModel();
    assertThrows(IllegalArgumentException.class, () -> {
      model.startGame(null, false, 4, 7);
    });
    model = new SoloRedGameModel();
    assertThrows(IllegalArgumentException.class, () -> {
      model.startGame(fullDeck, false, 4, 0);
    });
    model = new SoloRedGameModel();
    assertThrows(IllegalArgumentException.class, () -> {
      model.startGame(fullDeck, false, 1, 7);
    });
    model = new SoloRedGameModel();
    assertThrows(IllegalArgumentException.class, () -> {
      model.startGame(new ArrayList<>(), false, 4, 7);
    });
    List<SoloRedCard> smallDeck = fullDeck.subList(0, 3);
    model = new SoloRedGameModel();
    assertThrows(IllegalArgumentException.class, () -> {
      model.startGame(smallDeck, false, 4, 7);
    });
    List<SoloRedCard> duplicateDeck = new ArrayList<>(fullDeck);
    duplicateDeck.add(duplicateDeck.get(0));
    model = new SoloRedGameModel();
    assertThrows(IllegalArgumentException.class, () -> {
      model.startGame(duplicateDeck, false, 4, 7);
    });
    List<SoloRedCard> deckWithNull = new ArrayList<>(fullDeck);
    deckWithNull.set(0, null);
    model = new SoloRedGameModel();
    assertThrows(IllegalArgumentException.class, () -> {
      model.startGame(deckWithNull, false, 4, 7);
    });
  }

  @Test
  public void testPlayToPalette() {
    model.startGame(fullDeck, false, 4, 7);
    int initialHandSize = model.getHand().size();
    int initialPaletteSize = model.getPalette(1).size();
    model.playToPalette(1, 0);
    assertEquals(initialHandSize - 1, model.getHand().size());
    assertEquals(initialPaletteSize + 1, model.getPalette(1).size());
    assertThrows(IllegalArgumentException.class, () -> {
      model.playToPalette(1, -1);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      model.playToPalette(-1, 0);
    });
    model.isGameOver();
    assertThrows(IllegalStateException.class, () -> {
      model.playToPalette(1, 0);
    });
  }

  @Test
  public void testNumOfCardsInDeck() {
    assertThrows(IllegalStateException.class, () -> {
      model.numOfCardsInDeck();
      model.startGame(fullDeck, false, 4, 7);
      int expectedDeckSize = fullDeck.size() - (4 + 7);
      assertEquals(expectedDeckSize, model.numOfCardsInDeck());
    });
  }

  @Test
  public void testNumOfPalettes() {
    assertThrows(IllegalStateException.class, () -> {
      model.numPalettes();
    });
  }

  @Test
  public void testWinningPaletteIndex() {
    assertThrows(IllegalStateException.class, () -> {
      model.winningPaletteIndex();
      model.startGame(fullDeck, false, 4, 7);
      int winningIndex = model.winningPaletteIndex();
      assertTrue(winningIndex >= 0 && winningIndex < 4);
    });
  }

  @Test
  public void testGetAllCards() {
    List<SoloRedCard> allCards = model.getAllCards();
    assertEquals(35, allCards.size());
    for (int i = 0; i < allCards.size(); i++) {
      for (int j = i + 1; j < allCards.size(); j++) {
        assertFalse("Duplicate card found",
                allCards.get(i).toString().equals(allCards.get(j).toString()));
      }
      List<SoloRedCard> modifiedCards = new ArrayList<>(model.getAllCards());
      modifiedCards.remove(0);
      assertEquals(35, model.getAllCards().size());
    }
  }

  @Test
  public void testIsGameWon() {
    assertThrows(IllegalStateException.class, () -> {
      model.isGameWon();
    });
    model.startGame(fullDeck, false, 4, 7);
    assertThrows(IllegalStateException.class, () -> {
      model.isGameWon();
    });
  }

  @Test
  public void testGetHand() {
    assertThrows(IllegalStateException.class, () -> {
      model.getHand();
    });
    model.startGame(fullDeck, false, 4, 7);
    List<SoloRedCard> hand = model.getHand();
    assertEquals(7, hand.size());
    for (SoloRedCard card : hand) {
      assertNotNull(card);
      List<SoloRedCard> modifiedHand = new ArrayList<>(model.getHand());
      modifiedHand.remove(0);
      assertEquals(7, model.getHand().size());
    }
  }

  @Test
  public void testGetPalette() {
    assertThrows(IllegalStateException.class, () -> {
      model.getPalette(0);
    });
    model.startGame(fullDeck, false, 4, 7);
    for (int i = 0; i < 4; i++) {
      List<SoloRedCard> palette = model.getPalette(i);
      assertFalse(palette.isEmpty());
    }
  }

  @Test
  public void testGetCanvas() {
    assertThrows(IllegalStateException.class, () -> {
      model.getCanvas();
    });
    model.startGame(fullDeck, false, 4, 7);
    SoloRedCard canvas = model.getCanvas();
    assertNotNull(canvas);
    assertEquals("R1", canvas.toString());
  }
}
