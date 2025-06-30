package cs3500.solored;

import org.junit.Before;
import org.junit.Test;

import cs3500.solored.model.hw02.SoloRedCard;
import cs3500.solored.model.hw04.AdvancedSoloRedGameModel;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Tests advanced impl for draw for hand and playing to canvas for advanced model.
 */
public class AdvancedSoloRedGameModelTest {

  private AdvancedSoloRedGameModel advancedModel;
  private List<SoloRedCard> fullDeck;

  @Before
  public void setUp() {
    advancedModel = new AdvancedSoloRedGameModel(new Random());
    fullDeck = advancedModel.getAllCards();
  }

  @Test
  public void testPlayToCanvas() {
    advancedModel.startGame(fullDeck, false, 4, 7);

    int initialHandSize = advancedModel.getHand().size();
    advancedModel.playToCanvas(0);

    assertNotNull(advancedModel.getCanvas());
    assertEquals(initialHandSize - 1, advancedModel.getHand().size());

    advancedModel.getHand().add(new SoloRedCard(SoloRedCard.Color.RED, 10));
    advancedModel.playToCanvas(0);

    int handSizeBeforeDraw = advancedModel.getHand().size();
    advancedModel.drawForHand();

    assertEquals(handSizeBeforeDraw + 2, advancedModel.getHand().size());
  }

  @Test
  public void testDrawForHand() {
    advancedModel.startGame(fullDeck, false, 4, 7);

    int initialHandSize = advancedModel.getHand().size();
    advancedModel.drawForHand();
    assertTrue(advancedModel.getHand().size() > initialHandSize);

    advancedModel.playToCanvas(0);

    int handSizeBeforeDraw = advancedModel.getHand().size();
    advancedModel.drawForHand();

    assertEquals(handSizeBeforeDraw + 2, advancedModel.getHand().size());
  }
}