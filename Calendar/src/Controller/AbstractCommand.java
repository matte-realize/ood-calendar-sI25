package Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

import Model.Location;
import Model.Status;

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
    Set<Character> validDays = Set.of('M', 'T', 'W', 'R', 'F', 'S', 'U');

    for (char ch : input.toCharArray()) {
      if (!validDays.contains(ch)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean isValidNewValue(String property, String newValue) {

    switch (property) {
      case "subject":
        case "description":
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
    }

    return true;
  }
}
