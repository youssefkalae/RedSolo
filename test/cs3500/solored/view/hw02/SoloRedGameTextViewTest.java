package cs3500.solored.view.hw02;

import cs3500.solored.model.hw02.SoloRedCard;
import cs3500.solored.model.hw02.SoloRedGameModel;
import cs3500.solored.view.hw02.SoloRedGameTextView;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


/**
 * Tests for the view implementation made int eh SoloRedGameModel.
 * Essentially captures what the user will see and tests if the implementation did it correctly.
 */
public class SoloRedGameTextViewTest {
  private SoloRedGameModel model;
  private SoloRedGameTextView view;

  @Before
  public void setUp() {
    model = new SoloRedGameModel();
    view = new SoloRedGameTextView(model);
  }

  @Test
  public void testToString() {
    model.startGame(model.getAllCards(), false, 4, 7);
    String viewString = view.toString();
    assertTrue(viewString.startsWith("Canvas: "));
    assertTrue(viewString.contains("P1: "));
    assertTrue(viewString.contains("P2: "));
    assertTrue(viewString.contains("P3: "));
    assertTrue(viewString.contains("P4: "));
    assertTrue(viewString.contains("Hand: "));
  }

  @Test
  public void testEmptyHand() {
    List<SoloRedCard> deck = model.getAllCards().subList(0, 8);
    model.startGame(deck, false, 4, 1);
    model.playToPalette(0, 0);
    model.drawForHand();
    String viewString = view.toString();
    assertTrue(viewString.contains("Hand:"));
    assertFalse(viewString.matches(".*Hand: .*[ROBIV][1-7].*"));
  }

  @Test
  public void testCanvasChange() {
    model.startGame(model.getAllCards(), false, 4, 7);
    SoloRedCard initialCanvas = model.getCanvas();
    model.playToCanvas(0);
    SoloRedCard updatedCanvas = model.getCanvas();
    assertNotEquals("Canvas card should change after playToCanvas",
            initialCanvas, updatedCanvas);
  }

  @Test
  public void testWinningPalette() {
    List<SoloRedCard> simpleDeck = model.getAllCards().subList(0, 10);
    model.startGame(simpleDeck, false, 3, 2);
    String viewString = view.toString();
    int winningIndex = model.winningPaletteIndex();
    for (int i = 0; i < model.numPalettes(); i++) {
      if (i == winningIndex) {
        assertTrue("winning Palette with >",
                viewString.contains("> P" + (i + 1) + ":"));
      } else {
        assertFalse("winning Palette with >",
                viewString.contains("> P" + (i + 1) + ":"));
      }
    }
    assertTrue("view should have word Canvas", viewString.contains("Canvas: "));
    assertTrue("view should have word Canvas", viewString.contains("Hand: "));
  }

  @Test
  public void testNullModelConstructor() {
    assertThrows(IllegalArgumentException.class, () -> {
      new SoloRedGameTextView(null);
    });
  }

  @Test
  public void testIsGameOverWhenDeckEmpty() {
    List<SoloRedCard> smallDeck = model.getAllCards().subList(0, 10);
    model.startGame(smallDeck, false, 4, 5);
    model.drawForHand();
    model.drawForHand();
    assertFalse("Game should not be over when deck is empty but hand is not", model.isGameOver());
  }
}