package controller;

/**
 * This interface serves as the basis for commands to be created
 * for execution for the controller.
 */
public interface Command {
  /**
   * Performs an execution operation of the action for the command.
   *
   * @throws IllegalArgumentException if the command consists of an invalid input.
   */
  void execute() throws IllegalArgumentException;

  /**
   * Determines a boolean whether the input is a valid date time.
   *
   * @param input the string of the date time to be checked.
   * @return a boolean based on the validity of the given input.
   */
  boolean isValidDateTime(String input);

  /**
   * Determines a boolean whether the timezone is a valid timezone.
   *
   * @param timezone the string of the timezone to be checked.
   * @return a boolena based on the validity of the given input.
   */
  boolean isValidZoneId(String timezone);

  /**
   * '
   * Determines a boolean whether the input is a valid date.
   *
   * @param input the string of the date to be checked.
   * @return a boolean based on the validity of the given input.
   */
  boolean isValidDate(String input);

  /**
   * Determines a boolean whether the input is in the correct weekday format.
   *
   * @param input the string of the weekday format to be checked.
   * @return a boolean based on the validity of the given input.
   */
  boolean isValidWeekdayFormat(String input);

  /**
   * Determines a boolean based on whether the property is a new value.
   *
   * @param property the string of the property to be changed.
   * @param newValue the string of the new value that will replace the property.
   * @return a boolean based on the validty of the given input.
   */
  boolean isValidNewValue(String property, String newValue);
}
