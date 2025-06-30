package cs3500.solored.model.hw04;

import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedCard;
import cs3500.solored.model.hw02.SoloRedGameModel;

/**
 * Factory class to handle the two game types user can choose.
 * This will take the relevant game and apply the rules accordingly.
 */
public class RedGameCreator {

  /**
   * Enum class for the two type of games player can input.
   */
  public enum GameType {
    BASIC, // basic/same model from hw2
    ADVANCED; // advanced enum for model
  }

  /**
   * Making a new game for what the user chooses. Basic or Advanced.
   */
  public static RedGameModel<SoloRedCard> createGame(GameType gameType) {
    switch (gameType) {
      case BASIC:
        return new SoloRedGameModel();
      case ADVANCED:
        return new AdvancedSoloRedGameModel();
      default:
        throw new IllegalArgumentException("Unknown GameType: " + gameType);
    }
  }
}
