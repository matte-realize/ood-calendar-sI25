package Controller;


public abstract interface Command {

  void execute() throws IllegalArgumentException;

  boolean isValidDateTime(String input, String pattern);

  boolean isValidWeekdayFormat(String input);

}
