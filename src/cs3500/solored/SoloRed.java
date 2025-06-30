package cs3500.solored;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import cs3500.solored.controller.RedGameController;
import cs3500.solored.controller.SoloRedTextController;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedCard;
import cs3500.solored.model.hw04.RedGameCreator;

/**
 * This class is for taking in the inputs and command lines from the user.
 * It detects whether the input is invalid and will set a default setting if so.
 */
public final class SoloRed {

  /**
   * Constructor for the string arguments inputted by the user.
   * Will throw and exception if nothing is inputted.
   * @param args input arguments from user.
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalStateException("input basic or advanced");
    }

    String typeGame = args[0];
    RedGameCreator.GameType gameType;
    if (typeGame.equalsIgnoreCase("advanced")) {
      gameType = RedGameCreator.GameType.ADVANCED;
    } else if (typeGame.equalsIgnoreCase("basic")) {
      gameType = RedGameCreator.GameType.BASIC;
    } else {
      throw new IllegalArgumentException("game type not specified");
    }

    int numPalette = 4;

    if (args.length > 1) {
      try {
        numPalette = Integer.parseInt(args[1]);
        if (numPalette < 2) {
          numPalette = 4;
        }
      } catch (NumberFormatException ignored) {
      }
    }

    int handSize = 7;

    if (args.length > 2) {
      try {
        handSize = Integer.parseInt(args[2]);
        if (handSize <= 0) {
          handSize = 7;
        }
      } catch (NumberFormatException ignored) {
      }
    }

    RedGameModel<SoloRedCard> model = RedGameCreator.createGame(gameType);
    Reader readable = new InputStreamReader(System.in);
    try {
      RedGameController controller = new SoloRedTextController(readable, System.out);
      List<SoloRedCard> deck = model.getAllCards();
      controller.playGame(model, deck, true, numPalette, handSize);
    } catch (IllegalArgumentException io) {
      System.out.println("problem with starting the game");
    } catch (IllegalStateException exception) {
      System.out.println("problem with inputs or outputs");
    }
  }
}