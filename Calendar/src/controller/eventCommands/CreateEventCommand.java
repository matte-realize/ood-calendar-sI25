package controller.eventCommands;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.AbstractCommand;
import model.calendar.Calendar;
import model.calendar.CalendarManagement;

/**
 * A command that extends the abstract command class which allows for the user
 * to be able to create events through single events or a series through the
 * calendar controller.
 */
public class CreateEventCommand extends AbstractCommand {
  private static final Pattern CreateSingleEvent = Pattern.compile(
          "^create event \"([^\"]+)\" from "
                  + "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to "
                  + "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})$");

  private static final Pattern CreateEventSeriesNum = Pattern.compile(
          "^create event \"([^\"]+)\" from "
                  + "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to "
                  + "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) repeats "
                  + "([MTWRFSU]+) for (\\d+) times$");

  private static final Pattern CreateEventSeriesUntil = Pattern.compile(
          "^create event \"([^\"]+)\" from "
                  + "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to "
                  + "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) repeats "
                  + "([MTWRFSU]+) until (\\d{4}-\\d{2}-\\d{2})$");

  private static final Pattern AllDayEvent = Pattern.compile(
          "^create event \"([^\"]+)\" on (\\d{4}-\\d{2}-\\d{2})$");

  private static final Pattern AllDayEventSeriesNum = Pattern.compile(
          "^create event \"([^\"]+)\" on (\\d{4}-\\d{2}-\\d{2}) "
                  + "repeats ([MTWRFSU]+) for (\\d+) times$");

  private static final Pattern AllDayEventSeriesUntil = Pattern.compile(
          "^create event \"([^\"]+)\" on (\\d{4}-\\d{2}-\\d{2}) "
                  + "repeats ([MTWRFSU]+) until (\\d{4}-\\d{2}-\\d{2})$");

  private static final Pattern CreateCalendar = Pattern.compile(
          "^create calendar --name ([^\"]+) --timezone "
                  + "([A-Za-z_]+/[A-Za-z_]+)");

  private final String tokensString;
  private final CalendarManagement calendarModel;
  private final Calendar selectedCalendar;

  public CreateEventCommand(String tokensString, CalendarManagement calendarModel) {
    this.tokensString = "create" + tokensString;
    this.calendarModel = calendarModel;
    this.selectedCalendar = calendarModel.getSelectedCalendar();
  }

  @Override
  public void execute() throws IllegalArgumentException {

    Matcher m;
    if ((m = CreateSingleEvent.matcher(tokensString)).matches()) {
      handleSingleEvent(m, false);
    } else if ((m = CreateEventSeriesNum.matcher(tokensString)).matches()) {
      handleRepeatingEventNTimes(m, false);
    } else if ((m = CreateEventSeriesUntil.matcher(tokensString)).matches()) {
      handleRepeatingEventUntil(m, false);
    } else if ((m = AllDayEvent.matcher(tokensString)).matches()) {
      handleSingleEvent(m, true);
    } else if ((m = AllDayEventSeriesNum.matcher(tokensString)).matches()) {
      handleRepeatingEventNTimes(m, true);
    } else if ((m = AllDayEventSeriesUntil.matcher(tokensString)).matches()) {
      handleRepeatingEventUntil(m, true);
    } else if ((m= CreateCalendar.matcher(tokensString)).matches()) {
      handleCreateCalendar(m);
    } else {
      throw new IllegalArgumentException("Invalid command: \"" + tokensString + "\"");
    }
  }

  private void handleSingleEvent(Matcher m, boolean isALlDayEvent) {
    String subject = m.group(1);
    String start;
    String end;
    if (isALlDayEvent) {
      start = m.group(2) + "T08:00";
      end = m.group(2) + "T17:00";
    } else {
      start = m.group(2);
      end = m.group(3);
    }

    if (!isValidDateTime(start) || !isValidDateTime(end)) {
      throw new IllegalArgumentException(
              "Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm"
      );
    }

    selectedCalendar.createEvent(
            subject,
            LocalDateTime.parse(start),
            LocalDateTime.parse(end),
            "",
            null,
            null
    );
  }

  private void handleRepeatingEventNTimes(Matcher m, boolean isAllDayEvent) {
    String subject = m.group(1);
    String start;
    String end;
    String weekdays;
    String repeatNum;
    if (isAllDayEvent) {
      start = m.group(2) + "T08:00";
      end = m.group(2) + "T17:00";
      weekdays = m.group(3);
      repeatNum = m.group(4);
    } else {
      start = m.group(2);
      end = m.group(3);
      weekdays = m.group(4);
      repeatNum = m.group(5);
    }

    if (!isValidDateTime(start) || !isValidDateTime(end)) {
      throw new IllegalArgumentException(
              "Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm"
      );
    }

    if (!isValidWeekdayFormat(weekdays)) {
      throw new IllegalArgumentException(
              "Invalid weekday format. Expected subset of MTWRFSU"
      );
    }

    if (Integer.parseInt(repeatNum) < 1) {
      throw new IllegalArgumentException(
              "Invalid repeat number. Must be greater than 0"
      );
    }


    selectedCalendar.createEventSeries(
            subject,
            LocalDateTime.parse(start),
            LocalDateTime.parse(end),
            parseDays(weekdays),
            Integer.parseInt(repeatNum),
            null,
            null,
            null
    );
  }

  private void handleRepeatingEventUntil(Matcher m, boolean isALlDayEvent) {
    String subject = m.group(1);
    String start;
    String end;
    String weekdays;
    String untilDate;
    if (isALlDayEvent) {
      start = m.group(2) + "T08:00";
      end = m.group(2) + "T17:00";
      weekdays = m.group(3);
      untilDate = m.group(4) + "T23:59";

    } else {
      start = m.group(2);
      end = m.group(3);
      weekdays = m.group(4);
      untilDate = m.group(5) + "T23:59";
    }

    if (!isValidDateTime(start) || !isValidDateTime(end)) {
      throw new IllegalArgumentException(
              "Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm"
      );
    }

    if (!isValidWeekdayFormat(weekdays)) {
      throw new IllegalArgumentException(
              "Invalid weekday format. Expected subset of MTWRFSU"
      );
    }

    selectedCalendar.createEventSeries(
            subject,
            LocalDateTime.parse(start),
            LocalDateTime.parse(end),
            parseDays(weekdays),
            calculateWeeksNeeded(
                    LocalDateTime.parse(start),
                    LocalDateTime.parse(untilDate),
                    parseDays(weekdays)
            ),
            null,
            null,
            null
    );
  }

  private void handleCreateCalendar (Matcher m) {
    String name = m.group(1);
    String timezone = m.group(2);

    if (!isValidZoneId(timezone)) {
      throw new IllegalArgumentException("Timezone is of invalid format");
    } else {
      calendarModel.createCalendar(name, ZoneId.of(timezone));
    }
  }

  private static final Map<Character, DayOfWeek> dayMap;

  static {
    dayMap = new HashMap<>();
    dayMap.put('M', DayOfWeek.MONDAY);
    dayMap.put('T', DayOfWeek.TUESDAY);
    dayMap.put('W', DayOfWeek.WEDNESDAY);
    dayMap.put('R', DayOfWeek.THURSDAY);
    dayMap.put('F', DayOfWeek.FRIDAY);
    dayMap.put('S', DayOfWeek.SATURDAY);
    dayMap.put('U', DayOfWeek.SUNDAY);
  }

  private static List<DayOfWeek> parseDays(String input) {
    List<DayOfWeek> days = new ArrayList<>();
    for (char c : input.toUpperCase().toCharArray()) {
      DayOfWeek day = dayMap.get(c);
      if (day != null) {
        days.add(day);
      } else {
        throw new IllegalArgumentException("Invalid day character: " + c);
      }
    }
    return days;
  }

  private static int calculateWeeksNeeded(LocalDateTime startDate,
                                          LocalDateTime repeatUntilDate,
                                          List<DayOfWeek> repeatDays) {
    if (startDate.isAfter(repeatUntilDate)) {
      return 0;
    }

    int weeks = 0;
    LocalDateTime currentWeekStart = startDate;

    while (true) {
      boolean hasEventThisWeek = false;

      for (DayOfWeek dayOfWeek : repeatDays) {
        LocalDateTime eventDate = currentWeekStart.with(TemporalAdjusters.nextOrSame(dayOfWeek));
        if (!eventDate.isBefore(startDate) && !eventDate.isAfter(repeatUntilDate)) {
          hasEventThisWeek = true;
          break;
        }
      }

      if (hasEventThisWeek) {
        weeks++;
        currentWeekStart = currentWeekStart.plusWeeks(1);
      } else {
        break;
      }
    }

    return weeks;
  }
}
