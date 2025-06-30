package cs3500.solored.model.hw04;

import java.util.Random;

/**
 * Advanced game model class to handle the play to canvas and drawForHand in respect to rules.
 */
public class AdvancedSoloRedGameModel extends AbstractGameModel {

  private boolean drawTwoCards;

  /**
   * First constructor to handle random.
   * @param random Random object used for the model.
   */
  public AdvancedSoloRedGameModel(Random random) {
    super(random);
    drawTwoCards = false;
  }

  /**
   * Second Constructor to call super from parent.
   */
  public AdvancedSoloRedGameModel() {
    super();
    drawTwoCards = false;
  }

  @Override
  public void playToCanvas(int cardIdxInHand) {
    checkCanvasExceptions(cardIdxInHand);
    canvas = hand.remove(cardIdxInHand);
    canvasPlayedThisTurn = true;
    // if the number of the card played to canvas is greater than the size of the winningPalette,
    // you draw 2 cards if true, else draw 1
    if (this.getCanvas().getNumber() > palettes.get(winningPaletteIndex()).size()) {
      drawTwoCards = true;
    }
  }

  @Override
  public void drawForHand() {
    drawCard(); // automatically 1 card must be drawn
    if (drawTwoCards) { // if two cards can be drawn meeting the condition
      drawCard(); // draw second card
    }
    drawTwoCards = false; // otherwise stick to drawing one card only
  }

  private void drawCard() {
    if (!deck.isEmpty() && hand.size() < maxHandSize) { // condition for when to draw card
      hand.add(deck.remove(0)); // add new card
    }
  }
}