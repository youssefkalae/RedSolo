package cs3500.solored.model.hw02;

import java.util.Random;

import cs3500.solored.model.hw04.AbstractGameModel;

/**
 * Defines SoloRedGameModel which implements the RedGameModel interface for the SoloRedCards.
 * It will regulate and manage the games state at each moment of the game.
 * The fields track deck, palettes, hand, and canvas.
 */
public class SoloRedGameModel extends AbstractGameModel {

  /**
   * First constructor to handle random.
   * @param random Random object used for the model.
   */
  public SoloRedGameModel(Random random) {
    super(random);
  }

  public SoloRedGameModel() {
    super();
  }


  @Override
  public void playToCanvas(int cardIdxInHand) {
    checkCanvasExceptions(cardIdxInHand);
    canvas = hand.remove(cardIdxInHand);
    canvasPlayedThisTurn = true;
  }

  // Draws cards from deck to fill the hand to max hand size
  @Override
  public void drawForHand() {
    if (!gameStarted || gameEnd) {
      throw new IllegalStateException("Game has not started or is over");
    }

    while (hand.size() < maxHandSize && !deck.isEmpty()) {
      hand.add(deck.remove(0));
    }
    canvasPlayedThisTurn = false;
  }
}