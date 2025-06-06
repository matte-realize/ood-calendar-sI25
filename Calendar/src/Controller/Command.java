package Controller;


import Model.Calendar;

public abstract interface Command {

  void execute() throws IllegalArgumentException;

  boolean isValidDateTime(String input);

  boolean isValidDate(String input);

  boolean isValidWeekdayFormat(String input);

  boolean isValidNewValue(String property, String newValue);

}
