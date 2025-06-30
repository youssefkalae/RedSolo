package cs3500.solored.view.hw02;

import java.io.IOException;
import java.util.List;

import cs3500.solored.model.hw02.RedGameModel;

/**
 * SoloRedGmeTextView implements the RedGameView interface.
 * It basically takes the different behaviors of the model for the view.
 */
public class SoloRedGameTextView implements RedGameView {
  private final RedGameModel<?> model;
  private final Appendable appendable;

  /**
   * Constructor that essentially initializes the SoloRedGameView texts within the game model.
   *
   * @param model If the model is null nothing will show or proceed.
   */
  public SoloRedGameTextView(RedGameModel<?> model, Appendable appendable) {
    if (model == null || appendable == null) {
      throw new IllegalArgumentException("Model or appendable cannot be null");
    }
    this.model = model;
    this.appendable = appendable;
  }

  public SoloRedGameTextView(RedGameModel<?> model) {
    this(model, new StringBuilder());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Canvas: ").append(model.getCanvas().toString().
            replaceAll("[^A-Z]", "")).append("\n");

    // Iterating over all palettes and printing state
    for (int i = 0; i < model.numPalettes(); i++) {
      // Winning palette?
      if (i == model.winningPaletteIndex()) {
        sb.append("> "); // Marking the winning palette
      }
      sb.append("P").append(i + 1).append(": ");
      // Current palette and iterating over the palette's cards
      List<?> currentPalette = model.getPalette(i);
      for (int l = 0; l < currentPalette.size(); l++) {
        sb.append(currentPalette.get(l).toString());
        // If it's not last card in palette, add space after it
        if (currentPalette.get(l) != currentPalette.get(currentPalette.size() - 1)) {
          sb.append(" ");
        }
        sb.append("\n"); // New line
      }
    }
    // Player's hand
    List<?> hand = model.getHand();
    sb.append("Hand: ");
    for (int i = 0; i < hand.size(); i++) {
      sb.append(hand.get(i).toString());
      // If not last card in the hand we got from the player, add space over it
      if (hand.get(i) != hand.get(hand.size() - 1)) {
        sb.append(" ");
      }
    }
    return sb.toString();
  }

  @Override
  public void render() throws IOException {
    if (appendable != null) {
      appendable.append(this.toString());
    } else {
      throw new IOException("no appendable provided");
    }
  }
}

