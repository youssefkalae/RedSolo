package cs3500.solored.model.hw02;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

/**
 * Test class that makes sure the correct implementation of the game rules for SoloRedGame.
 * For example the red rule and more.
 */
public class SoloRedGameModelTest {
  private SoloRedGameModel model;
  private List<SoloRedCard> testDeck;

  @Before
  public void setUp() {
    model = new SoloRedGameModel();
    testDeck = Arrays.asList(
            new SoloRedCard(SoloRedCard.Color.RED, 1),
            new SoloRedCard(SoloRedCard.Color.BLUE, 2),
            new SoloRedCard(SoloRedCard.Color.ORANGE, 3),
            new SoloRedCard(SoloRedCard.Color.VIOLET, 4),
            new SoloRedCard(SoloRedCard.Color.INDIGO, 5),
            new SoloRedCard(SoloRedCard.Color.RED, 6),
            new SoloRedCard(SoloRedCard.Color.BLUE, 7)
    );
  }

  @Test
  public void testRedRule() {
    model.startGame(testDeck, false, 2, 3);
    assertEquals(1, model.winningPaletteIndex());
  }

  @Test
  public void testOrangeRule() {
    model.startGame(testDeck, false, 2, 3);
    model.playToCanvas(0);
    model.playToPalette(0, 0);
    assertEquals(0, model.winningPaletteIndex());
  }

  @Test
  public void testBlueRule() {
    model.startGame(testDeck, false, 3, 2);
    model.playToCanvas(0);
    assertEquals(2, model.winningPaletteIndex());
  }

  @Test
  public void testIndigoRule() {
    List<SoloRedCard> deck = Arrays.asList(
            new SoloRedCard(SoloRedCard.Color.RED, 1),
            new SoloRedCard(SoloRedCard.Color.BLUE, 2),
            new SoloRedCard(SoloRedCard.Color.ORANGE, 3),
            new SoloRedCard(SoloRedCard.Color.VIOLET, 5),
            new SoloRedCard(SoloRedCard.Color.INDIGO, 7)
    );
    model.startGame(deck, false, 2, 3);
    model.playToPalette(0, 0);
    model.playToPalette(0, 0);
    model.playToPalette(0, 0);
    model.playToPalette(1, 0);
    model.playToPalette(1, 0);
    assertEquals(0, model.winningPaletteIndex());
  }

  @Test
  public void testVioletRule() {
    model.startGame(testDeck, false, 3, 2);
    model.playToCanvas(1);
    model.playToPalette(0, 0);
    assertEquals(0, model.winningPaletteIndex());
  }

  @Test
  public void testTieBreaker() {
    List<SoloRedCard> tieBreakerDeck = Arrays.asList(
            new SoloRedCard(SoloRedCard.Color.RED, 1),
            new SoloRedCard(SoloRedCard.Color.BLUE, 1),
            new SoloRedCard(SoloRedCard.Color.ORANGE, 2),
            new SoloRedCard(SoloRedCard.Color.VIOLET, 3),
            new SoloRedCard(SoloRedCard.Color.INDIGO, 4),
            new SoloRedCard(SoloRedCard.Color.RED, 5),
            new SoloRedCard(SoloRedCard.Color.BLUE, 6)
    );
    model.startGame(tieBreakerDeck, false, 2, 3);
    model.playToCanvas(0);
    model.playToPalette(0, 0);
    model.playToPalette(1, 0);
    assertEquals(1, model.winningPaletteIndex());
  }
}