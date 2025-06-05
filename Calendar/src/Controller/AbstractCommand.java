package Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCommand implements Command {

  @Override
  public boolean isValidDateTime(String input, String pattern) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    try {
      LocalDateTime.parse(input, formatter);
      return true;
    } catch (DateTimeParseException e) {
      System.out.println("Invalid date/time format: " + input);
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
}
