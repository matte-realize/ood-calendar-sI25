package controller.eventCommands;

import java.util.regex.Pattern;

import controller.AbstractCommand;
import model.calendar.CalendarManagement;
import view.CalendarView;

/**
 * A command that extends the abstract command class which allows for the user
 * to be able to copy events through single events or a series through the
 * calendar controller.
 */
public class CopyEventCommand extends AbstractCommand {
  private static final Pattern CopySingleEvent = Pattern.compile(
          "^copy event \"([^\"]+)\" on "
                  + "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) --target "
                  + "([^\"]+) to "
                  + "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})$");

  private static final Pattern CopyDayEvents = Pattern.compile(
          "^copy events on "
                  + "(\\d{4}-\\d{2}-\\d{2}) --target "
                  + "([^\"]+) to "
                  + "(\\d{4}-\\d{2}-\\d{2})$");

  private static final Pattern CopyWindowEvents = Pattern.compile(
          "^copy events between "
                  + "(\\d{4}-\\d{2}-\\d{2}) and (\\d{4}-\\d{2}-\\d{2}) "
                  + "--target ([^\"]+) to "
                  + "(\\d{4}-\\d{2}-\\d{2})$");

  private final String tokensString;
  private final CalendarManagement calendarModel;
  private CalendarView calendarView;

  /**
   * A constructor for the copy command.
   *
   * @param tokensString  a string that determines the token.
   * @param calendarModel a calendar model.
   */
  public CopyEventCommand(String tokensString, CalendarManagement calendarModel, CalendarView calendarView) {
    this.tokensString = "create" + tokensString;
    this.calendarModel = calendarModel;
    this.calendarView = calendarView;
  }

  @Override
  public void execute() throws IllegalArgumentException {


  }
}
