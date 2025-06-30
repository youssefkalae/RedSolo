package cs3500.solored.model.hw02;

import java.util.Objects;

/**
 * SoloRedCard implements the cards values like their number and color they are associated with.
 * Has methods to help the implement methods.
 */
public class SoloRedCard implements Card {
  private final Color color;
  private final int number;

  /**
   * This enum class will provide the only colors the SoloRedCards can be in the RedGame.
   * All other colors will not pass through.
   */
  public enum Color {
    RED(4), ORANGE(3), BLUE(2), INDIGO(1), VIOLET(0);

    private final int redScore;

    Color(int closeToRed) {
      this.redScore = closeToRed;
    }

    public int getRedScore() {
      return redScore;
    }

  }

  /**
   * Enum class for number of card. Makes sure the user is only inputting numbers 1-7.
   */
  public enum Number {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7);
    private int value;

    Number(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  /**
   * This class provides the SoloRedCard properties like color and number.
   *
   * @param color  For the color of card (red, orange etc...).
   * @param number For the number associated with the card.
   */
  public SoloRedCard(Color color, int number) {
    this.color = color;
    this.number = number;
  }

  @Override
  public String toString() {
    return color.toString().charAt(0) + String.valueOf(number);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SoloRedCard that = (SoloRedCard) o;
    return number == that.number && color == that.color;
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, number);
  }

  public Color getColor() {
    return color;
  }

  public int getNumber() {
    return number;
  }
}