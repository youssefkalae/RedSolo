package cs3500.solored.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.view.hw02.RedGameView;
import cs3500.solored.view.hw02.SoloRedGameTextView;

/**
 * Class that implements the controller interface for the RedGame.
 * It contains a readable and an appendable for the user input (and output that comes out).
 */
public class SoloRedTextController implements RedGameController {

  private final Readable readable;
  private final Appendable appendable;

  /**
   * Initiates the fields for readable and appendable and tells whether game is allowed to play.
   *
   * @param readable   input detected.
   * @param appendable output given based on the input.
   */
  public SoloRedTextController(Readable readable, Appendable appendable) {
    if (readable == null || appendable == null) {
      throw new IllegalArgumentException("Input and output cannot be null");
    }
    this.readable = readable;
    this.appendable = appendable;
  }

  @Override
  public <C extends Card> void playGame(RedGameModel<C> model, List<C> deck,
                                        boolean shuffle, int numPalettes, int handSize) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }

    try {
      model.startGame(deck, shuffle, numPalettes, handSize);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Could not start game: " + e.getMessage());
    }

    Scanner scanner = new Scanner(this.readable);

    while (!model.isGameOver()) {
      String command = validCommand(scanner);
      if (command == null) {
        quit(model);
        return;
      }

      try {
        if (command.equalsIgnoreCase("palette")) {
          List<Integer> args = validInput(scanner, command);
          if (args == null) {
            quit(model);
            return;
          }
          int paletteIndex = args.get(0) - 1;
          int cardIndex = args.get(1) - 1;
          model.playToPalette(paletteIndex, cardIndex);
          model.drawForHand();
        } else if (command.equalsIgnoreCase("canvas")) {
          List<Integer> args = validInput(scanner, command);
          if (args == null) {
            quit(model);
            return;
          }
          int cardIndex = args.get(0) - 1;
          model.playToCanvas(cardIndex);
          model.drawForHand();
        }
      } catch (IllegalArgumentException e) {
        safeAppend("Invalid move. Try again. " + e.getMessage() + "\n");
        continue;
      }

      displayState(model);
    }

    if (model.isGameOver()) {
      endGame(model);
    }
  }

  private void safeAppend(String message) {
    try {
      appendable.append(message);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to transmit output", e);
    }
  }

  private <C extends Card> void displayState(RedGameModel<C> model) {
    try {
      RedGameView view = new SoloRedGameTextView(model, appendable);
      view.render();
      String deckMessage = "Number of cards in deck: " + model.numOfCardsInDeck() + "\n";
      safeAppend(deckMessage);
    } catch (IOException e) {
      throw new IllegalStateException("Error, unable to render!", e);
    }
  }

  private <C extends Card> void quit(RedGameModel<C> model) {
    safeAppend("Game quit!\n");
    safeAppend("State of game when quit:\n");
    displayState(model);
  }

  private String validCommand(Scanner scanner) {
    while (scanner.hasNext()) {
      String input = scanner.next().trim();
      if (input.equalsIgnoreCase("q")) {
        return null;
      } else if (input.equalsIgnoreCase("palette") || input.equalsIgnoreCase("canvas")) {
        return input;
      } else {
        safeAppend("Invalid command. Try again.\n");
      }
    }
    throw new IllegalStateException("Readable object unable to provide inputs");
  }

  private List<Integer> validInput(Scanner scanner, String command) {
    int numRequiredArgs;
    String[] indexNames;

    if (command.equalsIgnoreCase("palette")) {
      numRequiredArgs = 2;
      indexNames = new String[]{"palette index", "card index"};
    } else {
      numRequiredArgs = 1;
      indexNames = new String[]{"card index"};
    }

    List<Integer> args = new ArrayList<>();

    for (int i = 0; i < numRequiredArgs; i++) {
      while (true) {
        if (!scanner.hasNext()) {
          throw new IllegalStateException("Readable object unable to provide inputs");
        }
        String input = scanner.next().trim();
        if (input.equalsIgnoreCase("q")) {
          return null;
        }
        try {
          int num = Integer.parseInt(input);
          if (num >= 1) {
            args.add(num);
            break;
          } else {
            safeAppend("Invalid " + indexNames[i] + ". Must be a positive integer. Try again.\n");
          }
        } catch (NumberFormatException e) {
          safeAppend("Invalid " + indexNames[i] + ". Not a number. Try again.\n");
        }
      }
    }
    return args;
  }

  private <C extends Card> void endGame(RedGameModel<C> model) {
    if (!model.isGameOver()) {
      throw new IllegalStateException("Cannot end the game because it is not over.");
    }
    if (model.isGameWon()) {
      safeAppend("Game won.\n");
    } else {
      safeAppend("Game lost.\n");
    }
    displayState(model);
  }
}