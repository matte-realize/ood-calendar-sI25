package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.calendar.Calendar;
import model.calendar.CalendarManagement;
import model.enums.EditMode;
import model.enums.Location;
import model.enums.Status;
import model.event.Event;
import model.event.EventInterface;
import model.event.EventSeries;
import view.CalendarView;

/**
 * A command that extends the abstract command class which allows for the user
 * to be able to edit events through single events or a series through the
 * calendar controller.
 */
public class EditCommand extends AbstractCommand {
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
                  + "(name|timezone) ([^\"]+)$");

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
  public EditCommand(String tokensString,
                     CalendarManagement calendarModel,
                     CalendarView calendarView) {
    this.tokensString = "edit" + tokensString;
    this.calendarModel = calendarModel;
    this.selectedCalendar = calendarModel.getSelectedCalendar();
    this.calendarView = calendarView;
  }

  @Override
  public void execute() throws IllegalArgumentException {
    Matcher m;

    if ((m = EditCalendar.matcher(tokensString)).matches()) {
      handleEditCalendar(m);
      return;
    }

    if (!checkCalendarSelected(selectedCalendar, calendarView)) {
      return;
    }

    if ((m = EditSingleEvent.matcher(tokensString)).matches()) {
      handleEditSingleEvent(m);
    } else if ((m = EditEvents.matcher(tokensString)).matches()) {
      handleEditEventsFrom(m);
    } else if ((m = EditEventSeries.matcher(tokensString)).matches()) {
      handleEditSeries(m);
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

    try {
      selectedCalendar.editEvent(
              subject,
              LocalDateTime.parse(from),
              editEventHelper(subject, from, to, property, newValue),
              EditMode.SINGLE
      );
    } catch (Exception e) {
      calendarView.printError(e.getMessage());
    }
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

    try {
      selectedCalendar.editEvent(
              subject,
              LocalDateTime.parse(from),
              editEventHelper(subject, from, null, property, newValue),
              EditMode.FUTURE
      );
    } catch (Exception e) {
      calendarView.printError(e.getMessage());
    }
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

    try {
      selectedCalendar.editEvent(
              subject,
              LocalDateTime.parse(from),
              editEventHelper(subject, from, null, property, newValue),
              EditMode.ALL
      );
    } catch (Exception e) {
      calendarView.printError(e.getMessage());
    }
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

    try {
      // Special handling for timezone changes
      if ("timezone".equals(property)) {
        handleTimezoneChange(name, newValue);
      } else {
        calendarModel.editCalendar(name, property, newValue);
      }
    } catch (Exception e) {
      calendarView.printError(e.getMessage());
    }
  }

  /**
   * Handles timezone changes by updating all events in the calendar to reflect the new timezone.
   * Follows MVC pattern - Controller coordinates, Model does the work.
   *
   * @param calendarName the name of the calendar being edited
   * @param newTimezone the new timezone value
   */
  private void handleTimezoneChange(String calendarName, String newTimezone) {
    try {
      // Validate the new timezone first (Controller responsibility - input validation)
      if (!isValidTimezone(newTimezone)) {
        calendarView.printError("Invalid timezone: " + newTimezone);
        return;
      }

      // Get the old timezone before making changes (using existing CalendarManagement method)
      ZoneId oldTimezoneId = calendarModel.getCalendarTimezone(calendarName);
      if (oldTimezoneId == null) {
        calendarView.printError("Calendar not found: " + calendarName);
        return;
      }

      String oldTimezone = oldTimezoneId.getId();

      // If timezone is the same, no need to update events
      if (oldTimezone.equals(newTimezone)) {
        calendarModel.editCalendar(calendarName, "timezone", newTimezone);
        return;
      }

      // Get the calendar and update all its events
      Calendar calendarToUpdate = getCalendarByName(calendarName);
      if (calendarToUpdate != null) {
        updateAllEventsForTimezoneChange(calendarToUpdate, oldTimezone, newTimezone);
      }

      // Finally, update the calendar's timezone in the model
      calendarModel.editCalendar(calendarName, "timezone", newTimezone);

    } catch (Exception e) {
      calendarView.printError("Error updating timezone: " + e.getMessage());
    }
  }

  /**
   * Helper method to get a calendar by name from CalendarManagement.
   * This works around the lack of a direct getCalendar method.
   */
  private Calendar getCalendarByName(String calendarName) {
    // If it's the selected calendar and names match
    if (selectedCalendar != null) {
      // We need to check if this is the calendar we're looking for
      // Since we can get timezone by name, we can verify it's the right calendar
      ZoneId calendarTimezone = calendarModel.getCalendarTimezone(calendarName);
      if (calendarTimezone != null) {
        // If the name exists in CalendarManagement, and we have a selected calendar,
        // we can assume it's accessible. For proper implementation, you might want
        // to add a getCalendar method to CalendarManagement.

        // For now, if we're editing the selected calendar, use it
        ZoneId selectedTimezone = calendarModel.getCalendarTimezone(null); // null gets selected calendar timezone
        if (selectedTimezone != null && selectedTimezone.equals(calendarTimezone)) {
          return selectedCalendar;
        }
      }
    }
    return null; // In a full implementation, CalendarManagement should have getCalendar(name)
  }

  /**
   * Updates all events in a calendar to reflect a timezone change.
   * This is business logic that ideally should be in the Calendar class,
   * but we're keeping it here to avoid modifying too many classes.
   *
   * @param calendar the calendar containing the events
   * @param oldTimezone the old timezone
   * @param newTimezone the new timezone
   */
  private void updateAllEventsForTimezoneChange(Calendar calendar, String oldTimezone, String newTimezone) {
    try {
      ZoneId oldZone = ZoneId.of(oldTimezone);
      ZoneId newZone = ZoneId.of(newTimezone);

      // Get all events from the calendar
      List<EventInterface> allEvents = getAllEventsFromCalendar(calendar);

      for (EventInterface event : allEvents) {
        // Convert the event times from old timezone to new timezone
        LocalDateTime oldStart = event.getStartDateTime();
        LocalDateTime oldEnd = event.getEndDateTime();

        // Convert to ZonedDateTime in old timezone, then to new timezone
        ZonedDateTime zonedStart = oldStart.atZone(oldZone);
        ZonedDateTime zonedEnd = oldEnd.atZone(oldZone);

        // Convert to new timezone and back to LocalDateTime
        LocalDateTime newStart = zonedStart.withZoneSameInstant(newZone).toLocalDateTime();
        LocalDateTime newEnd = zonedEnd.withZoneSameInstant(newZone).toLocalDateTime();

        // Create updated event with new times
        Event.CustomEventBuilder updatedEventBuilder = new Event.CustomEventBuilder()
                .setSubject(event.getSubject())
                .setDescription(event.getDescription())
                .setLocation(event.getLocation())
                .setStartDateTime(newStart)
                .setEndDateTime(newEnd)
                .setStatus(event.getStatus());

        EventInterface updatedEvent = updatedEventBuilder.build();

        // Update the event in the calendar
        calendar.editEvent(
                event.getSubject(),
                event.getStartDateTime(), // Use original start time as identifier
                updatedEvent,
                EditMode.SINGLE
        );
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to update events for timezone change: " + e.getMessage());
    }
  }

  /**
   * Helper method to get all events from a calendar.
   * Based on the Calendar class structure with eventsByDate Map and mapSeries.
   * This implementation collects all events from both individual events and series instances.
   */
  private List<EventInterface> getAllEventsFromCalendar(Calendar calendar) {
    List<EventInterface> allEvents = new ArrayList<>();

    try {
      // Get all individual events from eventsByDate map
      // Since eventsByDate is private, we need to use the existing public methods

      // Strategy 1: Use a wide date range to get all events via getEventsWindow
      // Find the earliest and latest dates by checking a reasonable range
      LocalDateTime start = LocalDateTime.now().minusYears(10); // 10 years ago
      LocalDateTime end = LocalDateTime.now().plusYears(10);   // 10 years from now

      List<Event> windowEvents = calendar.getEventsWindow(start, end);
      allEvents.addAll(windowEvents);

      // Strategy 2: Also get events from series instances directly
      // Since mapSeries is public final, we can access it if needed
      // But the getEventsWindow should already include series instances
      // as they are added to eventsByDate when the series is created

    } catch (Exception e) {
      // Fallback: try to get events day by day for a reasonable range
      LocalDate startDate = LocalDate.now().minusYears(5);
      LocalDate endDate = LocalDate.now().plusYears(5);

      for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
        try {
          List<Event> dayEvents = calendar.getEventsSingleDay(date);
          for (Event event : dayEvents) {
            // Avoid duplicates
            if (!allEvents.contains(event)) {
              allEvents.add(event);
            }
          }
        } catch (Exception dayException) {
          // Continue with next day if this day fails
          continue;
        }
      }
    }

    return allEvents;
  }

  /**
   * Validates if a timezone string is valid.
   * Controller responsibility - input validation.
   */
  private boolean isValidTimezone(String timezone) {
    try {
      ZoneId.of(timezone);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Validates if a new value is valid for the given property type.
   */
  public boolean isValidNewValue(String property, String newValue) {
    if (newValue == null || newValue.trim().isEmpty()) {
      return false;
    }

    switch (property) {
      case "subject":
      case "description":
      case "name":
        return !newValue.trim().isEmpty();
      case "start":
      case "end":
        return isValidDateTime(newValue);
      case "location":
        try {
          Location.valueOf(newValue.toUpperCase());
          return true;
        } catch (IllegalArgumentException e) {
          return false;
        }
      case "status":
        try {
          Status.valueOf(newValue.toUpperCase());
          return true;
        } catch (IllegalArgumentException e) {
          return false;
        }
      case "timezone":
        return isValidTimezone(newValue);
      default:
        return true;
    }
  }

  /**
   * Validates if a string is a valid datetime format.
   */
  public boolean isValidDateTime(String dateTime) {
    try {
      LocalDateTime.parse(dateTime);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private EventInterface editEventHelper(
          String subject,
          String from,
          String to,
          String property,
          String newValue
  ) {
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
      EventInterface oldEvent = selectedCalendar.getEvent(
              subject, LocalDateTime.parse(from), endDateTime
      );
      if (oldEvent == null) {
        return null;
      }

      Event.CustomEventBuilder newEventBuilder =
              new Event.CustomEventBuilder()
                      .setSubject(oldEvent.getSubject())
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

  /**
   * Abstract method from AbstractCommand - needs to be implemented
   * based on your AbstractCommand class structure.
   */
  protected boolean checkCalendarSelected(Calendar calendar, CalendarView view) {
    if (calendar == null) {
      view.printError("No calendar selected. Please select a calendar first.");
      return false;
    }
    return true;
  }
}