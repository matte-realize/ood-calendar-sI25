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
import model.event.Event;
import model.event.EventInterface;
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
  private final Calendar selectedCalendar;
  private CalendarView calendarView;

  /**
   * A constructor for the copy command.
   *
   * @param tokensString  a string that determines the token.
   * @param calendarModel a calendar model.
   * @param calendarView  a calendarView
   */
  public CopyEventCommand(String tokensString, CalendarManagement calendarModel, CalendarView calendarView) {
    this.tokensString = "copy" + tokensString;
    this.calendarModel = calendarModel;
    this.selectedCalendar = calendarModel.getSelectedCalendar();
    this.calendarView = calendarView;
  }

  @Override
  public void execute() throws IllegalArgumentException {
    Matcher m;
    if ((m = CopySingleEvent.matcher(tokensString)).matches()) {
      handleCopyEvent(m);
    } else if ((m = CopyDayEvents.matcher(tokensString)).matches()) {
      handleCopyEventsDay(m);
    } else if ((m = CopyWindowEvents.matcher(tokensString)).matches()) {
      handleCopyEventsWindow(m);
    } else {
      calendarView.printError("Invalid command: \"" + tokensString + "\"");
    }
  }

  private void handleCopyEvent(Matcher m) {
    String eventName = m.group(1);
    String eventDate = m.group(2);
    String targetCalendar = m.group(3);
    String targetDate = m.group(4);

    if (!isValidDateTime(eventDate) || !isValidDateTime(targetDate)) {
      calendarView.printError("Invalid date format");
      return;
    }

    calendarModel.copyEvent(selectedCalendar.getEvent(eventName, LocalDateTime.parse(eventDate), null),
            targetCalendar,
            LocalDateTime.parse(targetDate));
  }

  private void handleCopyEventsDay(Matcher m) {
    String eventsDate = m.group(1);
    String targetCalendar = m.group(2);
    String targetDate = m.group(3);

    if (!isValidDate(eventsDate) || !isValidDate(targetDate)) {
      calendarView.printError("Invalid date format");
      return;
    }

    calendarModel.copyEvents(convertEvents(selectedCalendar.getEventsSingleDay(LocalDate.parse(eventsDate)),
                    LocalDate.parse(targetDate),
                    calendarModel.getCalendarTimezone(null),
                    calendarModel.getCalendarTimezone(targetCalendar)),
            targetCalendar);
  }

  private void handleCopyEventsWindow(Matcher m) {
    String eventsStartDate = m.group(1);
    String eventsEndDate = m.group(2);
    String targetCalendar = m.group(3);
    String targetDate = m.group(4);

    if (!isValidDate(eventsStartDate) || !isValidDate(eventsEndDate) || !isValidDate(targetDate)) {
      calendarView.printError("Invalid date format");
      return;
    }

    calendarModel.copyEvents(convertEvents(selectedCalendar.getEventsWindow(LocalDateTime.parse(eventsStartDate + "T00:00"),
                            LocalDateTime.parse(eventsEndDate + "T23:59")),
                    LocalDate.parse(targetDate),
                    calendarModel.getCalendarTimezone(null),
                    calendarModel.getCalendarTimezone(targetCalendar)),
            targetCalendar);
  }

  private List<EventInterface> convertEvents(List<Event> events, LocalDate targetDate, ZoneId oldZoneId, ZoneId newZoneId) {

    List<EventInterface> convertedEventList = new ArrayList<>(events.size());

    for (Event event : events) {

      LocalDateTime test = event.getEndDateTime();

      LocalDateTime tempTargetDate = LocalDateTime.of(targetDate, event.getStartDateTime().toLocalTime());
      ZonedDateTime sourceDateTime = tempTargetDate.atZone(oldZoneId);
      ZonedDateTime targetDateTime = sourceDateTime.withZoneSameInstant(newZoneId);
      LocalDateTime targetLocalDateTime = targetDateTime.toLocalDateTime();

      LocalDateTime tempTargetEndDate = LocalDateTime.of(targetDate, event.getEndDateTime().toLocalTime());
      ZonedDateTime sourceEndDateTime = tempTargetEndDate.atZone(oldZoneId);
      ZonedDateTime targetEndDateTime = sourceEndDateTime.withZoneSameInstant(newZoneId);
      LocalDateTime targetEndLocalDateTime = targetEndDateTime.toLocalDateTime();

      EventInterface updatedEvent = new Event.CustomEventBuilder().setSubject(event.getSubject())
              .setStartDateTime(targetLocalDateTime)
              .setEndDateTime(targetEndLocalDateTime)
              .setDescription(event.getDescription())
              .setLocation(event.getLocation())
              .setStatus(event.getStatus())
              .build();

      convertedEventList.add(updatedEvent);
    }

    return convertedEventList;
  }

}