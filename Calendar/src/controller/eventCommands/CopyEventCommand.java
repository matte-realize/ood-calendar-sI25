package controller.eventCommands;

import java.util.regex.Pattern;

import controller.AbstractCommand;
import model.calendar.CalendarManagement;

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

  public CopyEventCommand(String tokensString, CalendarManagement calendarModel) {
    this.tokensString = "create" + tokensString;
    this.calendarModel = calendarModel;
  }

  @Override
  public void execute() throws IllegalArgumentException {


  }
}
