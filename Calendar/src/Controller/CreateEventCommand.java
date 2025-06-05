package Controller;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateEventCommand extends AbstractCommand {

  private static final Pattern CreateSingleEvent = Pattern.compile(
          "^create event \"([^\"]+)\" from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})$");

  private static final Pattern CreateEventSeriesNum = Pattern.compile(
          "^create event \"([^\"]+)\" from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) repeats ([MTWRFSU]+) for (\\d+) times$");

  private static final Pattern CreateEventSeriesUntil = Pattern.compile(
          "^create event \"([^\"]+)\" from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) repeats ([MTWRFSU]+) until (\\d{4}-\\d{2}-\\d{2})$");

  private static final Pattern AllDayEvent = Pattern.compile(
          "^create event \"([^\"]+)\" on (\\d{4}-\\d{2}-\\d{2})$");

  private static final Pattern AllDayEventSeriesNum = Pattern.compile(
          "^create event \"([^\"]+)\" on (\\d{4}-\\d{2}-\\d{2}) repeats ([MTWRFSU]+) for (\\d+) times$");

  private static final Pattern AllDayEventSeriesUntil = Pattern.compile(
          "^create event \"([^\"]+)\" on (\\d{4}-\\d{2}-\\d{2}) repeats ([MTWRFSU]+) until (\\d{4}-\\d{2}-\\d{2})$");

  private final String tokensString;

  public CreateEventCommand(String tokensString) {
    this.tokensString = "create" + tokensString;

  }

  @Override
  public void execute() throws IllegalArgumentException {

    Matcher m;
    if ((m = CreateSingleEvent.matcher(tokensString)).matches()) {
      handleSingleEvent(m);
    } else if ((m = CreateEventSeriesNum.matcher(tokensString)).matches()) {
      handleRepeatingEventNTimes(m);
    } else if ((m = CreateEventSeriesUntil.matcher(tokensString)).matches()) {
      handleRepeatingEventUntil(m);
    } else if ((m = AllDayEvent.matcher(tokensString)).matches()) {
      handleAllDayEvent(m);
    } else if ((m = AllDayEventSeriesNum.matcher(tokensString)).matches()) {
      handleAllDayEventRepeatNTimes(m);
    } else if ((m = AllDayEventSeriesUntil.matcher(tokensString)).matches()) {
      handleAllDayEventRepeatUntil(m);
    } else {
      throw new IllegalArgumentException("Invalid command: \"" + tokensString + "\"");
    }
  }

  private void handleSingleEvent(Matcher m) {
    String subject = m.group(1);
    String start = m.group(2);
    String end = m.group(3);

    if (!isValidDateTime(start) || !isValidDateTime(end)) {
      throw new IllegalArgumentException("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
    }

    System.out.println("Created Event: \"" + subject + "\" from " + start + " to " + end);
  }

  private void handleRepeatingEventNTimes(Matcher m) {
    String subject = m.group(1);
    String start = m.group(2);
    String end = m.group(3);
    String weekdays = m.group(4);
    String repeatNum = m.group(5);

    if (!isValidDateTime(start) || !isValidDateTime(end)) {
      throw new IllegalArgumentException("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
    }

    if (!isValidWeekdayFormat(weekdays)) {
      throw new IllegalArgumentException("Invalid weekday format. Expected subset of MTWRFSU");
    }

    if (Integer.parseInt(repeatNum) < 1) {
      throw new IllegalArgumentException("Invalid repeat number. Must be greater than 0");
    }
    System.out.println("Created Event: \"" + subject + "\" from " + start + " to " + end);
  }

  private void handleRepeatingEventUntil(Matcher m) {
    String subject = m.group(1);
    String start = m.group(2);
    String end = m.group(3);
    String weekdays = m.group(4);
    String untilDate = m.group(5);

    if (!isValidDateTime(start) || !isValidDateTime(end)) {
      throw new IllegalArgumentException("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
    }

    if (!isValidWeekdayFormat(weekdays)) {
      throw new IllegalArgumentException("Invalid weekday format. Expected subset of MTWRFSU");
    }

    if (!isValidDate(untilDate)) {
      throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
    }
    System.out.println("Created Event: \"" + subject + "\" from " + start + " to " + end);
  }

  private void handleAllDayEvent(Matcher m) {
    String subject = m.group(1);
    String start = m.group(2)+ "T08:00";
    String end = m.group(2) + "T17:00";

    if (!isValidDateTime(start) || !isValidDateTime(end)) {
      throw new IllegalArgumentException("Invalid datet format. Expected format: yyyy-MM-dd");
    }

    System.out.println("Created Event: \"" + subject + "\" from " + start + " to " + end);
  }

  private void handleAllDayEventRepeatNTimes(Matcher m) {
    String subject = m.group(1);
    String start = m.group(2)+ "T08:00";
    String end = m.group(2) + "T17:00";
    String weekdays = m.group(3);
    String repeatNum = m.group(4);

    if (!isValidDateTime(start) || !isValidDateTime(end)) {
      throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
    }

    if (!isValidWeekdayFormat(weekdays)) {
      throw new IllegalArgumentException("Invalid weekday format. Expected subset of MTWRFSU");
    }

    if (Integer.parseInt(repeatNum) < 1) {
      throw new IllegalArgumentException("Invalid repeat number. Must be greater than 0");
    }
    System.out.println("Created Event: \"" + subject + "\" from " + start + " to " + end);
  }

  private void handleAllDayEventRepeatUntil(Matcher m) {
    String subject = m.group(1);
    String start = m.group(2)+ "T08:00";
    String end = m.group(2) + "T17:00";
    String weekdays = m.group(3);
    String untilDate = m.group(4);

    if (!isValidDateTime(start) || !isValidDateTime(end)) {
      throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
    }

    if (!isValidWeekdayFormat(weekdays)) {
      throw new IllegalArgumentException("Invalid weekday format. Expected subset of MTWRFSU");
    }

    if (!isValidDate(untilDate)) {
      throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
    }
    System.out.println("Created Event: \"" + subject + "\" from " + start + " to " + end);
  }

}
