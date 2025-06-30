package cs3500.solored;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cs3500.solored.model.hw02.SoloRedCard;
import cs3500.solored.model.hw02.SoloRedGameModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests play to canvas and draw for hand for basic model.
 */
public class SoloRedGameModelTest {

  private SoloRedGameModel model;
  private List<SoloRedCard> fullDeck;

  @Before
  public void setUp() {
    model = new SoloRedGameModel();
    fullDeck = model.getAllCards();
  }

  @Test
  public void testPlayToCanvas() {
    model.startGame(fullDeck, false, 4, 7);
    SoloRedCard initialCanvas = model.getCanvas();
    int initialHandSize = model.getHand().size();

    model.playToCanvas(0);

    assertNotEquals(initialCanvas, model.getCanvas());
    assertEquals(initialHandSize - 1, model.getHand().size());

    assertThrows(IllegalStateException.class, () -> model.playToCanvas(0));
    assertThrows(IllegalArgumentException.class, () -> model.playToCanvas(-1));

    model.getHand().clear();
    model.getHand().add(new SoloRedCard(SoloRedCard.Color.BLUE, 3));

    assertThrows(IllegalStateException.class, () -> model.playToCanvas(0));

    model = new SoloRedGameModel();
    assertThrows(IllegalStateException.class, () -> model.playToCanvas(0));
  }

  @Test
  public void testDrawForHand() {
    model.startGame(fullDeck, false, 4, 7);
    model.playToPalette(1, 0);

    int handSizeBeforeDraw = model.getHand().size();

    model.drawForHand();

    assertEquals(7, model.getHand().size());
    assertTrue(model.getHand().size() > handSizeBeforeDraw);

    model = new SoloRedGameModel();
    assertThrows(IllegalStateException.class, () -> model.drawForHand());

    model.startGame(fullDeck, false, 4, 7);
    model.isGameOver();

    assertThrows(IllegalStateException.class, () -> model.drawForHand());
  }
}