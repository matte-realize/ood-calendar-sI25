package controller;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

import model.enums.Location;
import model.enums.Status;

/**
 * Abstract class that represents AbstractCommands that implement
 * the command interface. These methods within the class act as
 * helpers to check valid values.
 */
public abstract class AbstractCommand implements Command {
  @Override
  public boolean isValidDateTime(String input) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    try {
      LocalDateTime.parse(input, formatter);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  @Override
  public boolean isValidZoneId(String timezone) {
    try {
      ZoneId.of(timezone);
      return true;
    } catch (DateTimeException e) {
      return false;
    }
  }

  @Override
  public boolean isValidDate(String input) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    try {
      LocalDate.parse(input, formatter);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  @Override
  public boolean isValidWeekdayFormat(String input) {
    Set<Character> validDays = new HashSet<>();
    validDays.add('M');
    validDays.add('T');
    validDays.add('W');
    validDays.add('R');
    validDays.add('F');
    validDays.add('S');
    validDays.add('U');

    for (char ch : input.toCharArray()) {
      if (!validDays.contains(ch)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean isValidTimezone(String timezone) {
    try {
      ZoneId zoneId = ZoneId.of(timezone);
      return true;
    } catch (DateTimeException e) {
      return false;
    }
  }

  @Override
  public boolean isValidNewValue(String property, String newValue) {
    switch (property) {
      case "subject":
      case "description":
      case "name":
        return true;
      case "start":
      case "end":
        return isValidDateTime(newValue);
      case "status":
        try {
          Status.valueOf(newValue.toUpperCase());
          return true;
        } catch (IllegalArgumentException e) {
          return false;
        }
      case "location":
        try {
          Location.valueOf(newValue.toUpperCase());
          return true;
        } catch (IllegalArgumentException e) {
          return false;
        }
      case "timezone":
        break;
      default:
        break;
    }

    return true;
  }
}