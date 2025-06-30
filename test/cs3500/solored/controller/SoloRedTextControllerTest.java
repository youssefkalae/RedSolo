package cs3500.solored.controller;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import cs3500.solored.model.hw02.SoloRedCard;
import cs3500.solored.model.hw02.SoloRedGameModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * All the tests for the controller during a SoloRedGame.
 * Checks if the different behaviors are operating correctly.
 */
public class SoloRedTextControllerTest {


  @Test
  public void testGameStartAndQuit() throws IOException {
    StringReader input = new StringReader("q");
    StringWriter output = new StringWriter();

    SoloRedGameModel model = new SoloRedGameModel();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    List<SoloRedCard> deck = model.getAllCards();

    controller.playGame(model, deck, true, 4, 5);

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Game quit!"));
    assertTrue(outputStr.contains("State of game when quit:"));
  }

  @Test
  public void testPlayToPalette() throws IOException {
    StringReader input = new StringReader("palette 1 1\nq\n");
    StringWriter output = new StringWriter();

    SoloRedGameModel model = new SoloRedGameModel();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    List<SoloRedCard> deck = model.getAllCards();

    controller.playGame(model, deck, true, 4, 5);
    String expectedOutput =
            "Initial game state here\n"
                    + "Number of cards in deck: X\n"
                    + "Updated game state here\n"
                    + "Number of cards in deck: X-1\n"
                    + "Game quit!\n"
                    + "State of game when quit:\n"
                    + "Final game state here\n"
                    + "Number of cards in deck: \n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testPlayToCanvas() throws IOException {
    StringReader input = new StringReader("canvas 1\nq\n");
    StringWriter output = new StringWriter();

    SoloRedGameModel model = new SoloRedGameModel();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    List<SoloRedCard> deck = model.getAllCards();

    controller.playGame(model, deck, true, 4, 5);
    String expectedOutput =
            "Initial game state here\n"
                    + "Number of cards in deck: X\n"
                    + "Updated game state here\n"
                    + "Number of cards in deck: X-1\n"
                    + "Game quit!\n"
                    + "State of game when quit:\n"
                    + "Final game state here\n"
                    + "Number of cards in deck: \n";

    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testInvalidCommand() throws IOException {
    StringReader input = new StringReader("invalid_command\nq\n");
    StringWriter output = new StringWriter();

    SoloRedGameModel model = new SoloRedGameModel();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    List<SoloRedCard> deck = model.getAllCards();

    controller.playGame(model, deck, true, 4, 5);

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Invalid move. Try again: Unknown command:"));
    assertTrue(outputStr.contains("Game quit!"));
  }


  @Test
  public void testInvalidPaletteIndex() throws IOException {
    StringReader input = new StringReader("palette -1 1\nq\n");
    StringWriter output = new StringWriter();

    SoloRedGameModel model = new SoloRedGameModel();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    List<SoloRedCard> deck = model.getAllCards();

    controller.playGame(model, deck, true, 4, 5);

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Invalid move. Try again: Indices must be positive numbers"));
  }

  @Test
  public void testGameWin() throws IOException {
    StringReader input = new StringReader("palette 1 1\npalette 2 2\nq\n");
    StringWriter output = new StringWriter();

    SoloRedGameModel model = new SoloRedGameModel();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    List<SoloRedCard> deck = model.getAllCards();

    controller.playGame(model, deck, true, 4, 5);

    String outputStr = output.toString();
    assertTrue(outputStr.contains("Game won."));
  }
}