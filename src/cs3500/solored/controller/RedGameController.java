package cs3500.solored.controller;

import java.util.List;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;

/**
 * Interface for the controller of the RedGame. This allows you to play the game.
 */
public interface RedGameController {

  /**
   * This method plays a new game of Solo Red using the provided model,
   * Using the startGame method on the model.
   * @param model game model for the game that is being played.
   * @param deck the deck to use for game.
   * @param shuffle decision before game or not.
   * @param numPalettes number of palettes for the game.
   * @param handSize size of the players hand.
   * @param <C> type of card used in game.
   * @throws IllegalArgumentException if provided model is null, or game cannot be started.
   * @throws IllegalStateException if controller is unable to receive input or transmit output.
   */
  <C extends Card> void playGame(RedGameModel<C> model, List<C> deck,
                                  boolean shuffle, int numPalettes, int handSize);
}
