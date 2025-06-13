package controller.eventCommands;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.AbstractCommand;
import model.calendar.Calendar;
import model.calendar.CalendarManagement;
import model.enums.EditMode;
import model.event.Event;
import model.event.EventInterface;
import view.CalendarView;

/**
 * A command that extends the abstract command class which allows for the user
 * to be able to edit events through single events or a series through the
 * calendar controller.
 */
public class EditEventCommand extends AbstractCommand {
  private static final Pattern EditSingleEvent = Pattern.compile(
          "^edit event (subject|start|end|description|location|status) "
                  + "\"([^\"]+)\" from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to "
                  + "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) with (.+)$");

  private static final Pattern EditEvents = Pattern.compile(
          "^edit events (subject|start|end|description|location|status) "
                  + "\"([^\"]+)\" from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) with (.+)$");

  private static final Pattern EditEventSeries = Pattern.compile(
          "^edit series (subject|start|end|description|location|status) "
                  + "\"([^\"]+)\" from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) with (.+)$");

  private static final Pattern EditCalendar = Pattern.compile(
          "^edit calendar --name ([^\"]+) --property "
                  + "(name|timezone) \"([^\"]+)\"$");

  private final String tokensString;
  private final CalendarManagement calendarModel;
  private final Calendar selectedCalendar;
  private CalendarView calendarView;

  /**
   * Constructor for the edit command.
   *
   * @param tokensString  a string that determines the token.
   * @param calendarModel a calendar model.
   * @param calendarView  a calendar view.
   */
  public EditEventCommand(String tokensString, CalendarManagement calendarModel, CalendarView calendarView) {
    this.tokensString = "edit" + tokensString;
    this.calendarModel = calendarModel;
    this.selectedCalendar = calendarModel.getSelectedCalendar();
    this.calendarView = calendarView;
  }

  @Override
  public void execute() throws IllegalArgumentException {
    Matcher m;

    if ((m = EditSingleEvent.matcher(tokensString)).matches()) {
      handleEditSingleEvent(m);
    } else if ((m = EditEvents.matcher(tokensString)).matches()) {
      handleEditEventsFrom(m);
    } else if ((m = EditEventSeries.matcher(tokensString)).matches()) {
      handleEditSeries(m);
    } else if ((m = EditCalendar.matcher(tokensString)).matches()) {
      handleEditCalendar(m);
    } else {
      calendarView.printError("Invalid edit command: \"" + tokensString + "\"\n");
    }
  }

  private void handleEditSingleEvent(Matcher m) {
    String property = m.group(1);
    String subject = m.group(2);
    String from = m.group(3);
    String to = m.group(4);
    String newValue = m.group(5);

    if (!isValidDateTime(from) || !isValidDateTime(to)) {
      calendarView.printError("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
      return;
    }

    if (!isValidNewValue(property, newValue)) {
      calendarView.printError("Invalid new value format. Make sure the new "
              + "value is of the same type as you are trying to edit");
      return;
    }

    selectedCalendar.editEvent(subject, LocalDateTime.parse(from), editEventHelper(subject, from, to, property, newValue), EditMode.SINGLE);
  }

  private void handleEditEventsFrom(Matcher m) {
    String property = m.group(1);
    String subject = m.group(2);
    String from = m.group(3);
    String newValue = m.group(4);

    if (!isValidDateTime(from)) {
      calendarView.printError("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
      return;
    }

    if (!isValidNewValue(property, newValue)) {
      calendarView.printError("Invalid new value format. Make sure the new value is of "
              + "the same type as you are trying to edit");
      return;
    }

    selectedCalendar.editEvent(subject, LocalDateTime.parse(from), editEventHelper(subject, from, null, property, newValue), EditMode.FUTURE);
  }

  private void handleEditSeries(Matcher m) {
    String property = m.group(1);
    String subject = m.group(2);
    String from = m.group(3);
    String newValue = m.group(4);

    if (!isValidDateTime(from)) {
      calendarView.printError("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
      return;
    }

    if (!isValidNewValue(property, newValue)) {
      calendarView.printError("Invalid new value format. Make sure the new value "
              + "is of the same type as you are trying to edit");
      return;
    }

    selectedCalendar.editEvent(subject, LocalDateTime.parse(from), editEventHelper(subject, from, null, property, newValue), EditMode.ALL);
  }

  private void handleEditCalendar(Matcher m) {
    String name = m.group(1);
    String property = m.group(2);
    String newValue = m.group(3);

    if (!isValidNewValue(property, newValue)) {
      calendarView.printError("Invalid new value format. Make sure the new value is "
              + "of the same type as you are trying to edit");
      return;
    }

    calendarModel.editCalendar(name, property, newValue);
  }

  private EventInterface editEventHelper(String subject, String from, String to, String property, String newValue) {
    LocalDateTime startDateTime = LocalDateTime.parse(from);
    LocalDateTime endDateTime = null;
    if (to != null) {
      endDateTime = LocalDateTime.parse(to);
    } else {
      List<Event> eventsOnDate = selectedCalendar.getEventsSingleDay(startDateTime.toLocalDate());
      for (Event e : eventsOnDate) {
        if (e.getSubject().equals(subject) && e.getStartDateTime().equals(startDateTime)) {
          endDateTime = e.getEndDateTime();
          break;
        }
      }
    }

    try {
      EventInterface oldEvent = selectedCalendar.getEvent(subject, LocalDateTime.parse(from), endDateTime);
      if (oldEvent == null) {
        return null;
      }
      Event.CustomEventBuilder newEventBuilder = new Event.CustomEventBuilder().setSubject(oldEvent.getSubject())
              .setDescription(oldEvent.getDescription())
              .setLocation(oldEvent.getLocation())
              .setEndDateTime(oldEvent.getEndDateTime())
              .setStartDateTime(oldEvent.getStartDateTime())
              .setStatus(oldEvent.getStatus());

      switch (property) {
        case "subject":
          newEventBuilder.setSubject(newValue);
          break;
        case "start":
          newEventBuilder.setStartDateTime(LocalDateTime.parse(newValue));
          break;
        case "end":
          newEventBuilder.setEndDateTime(LocalDateTime.parse(newValue));
          break;
        case "description":
          newEventBuilder.setDescription(newValue);
          break;
        case "location":
          break;
        case "status":
          break;
        default:
          break;
      }

      return newEventBuilder.build();

    } catch (IllegalArgumentException e) {
      return null;
    }

  }
}
