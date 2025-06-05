package Controller;

import java.util.HashSet;
import java.util.Set;

public class CreateEventCommand extends AbstractCommand {

  private String tokensString;
  private String[] tokens;
  private String subject;

  public CreateEventCommand(String tokensString, String subject) {
    this.tokensString = tokensString;
    this.tokens = tokensString.split(" ");
    this.subject = subject;
  }

  @Override
  public void execute() throws IllegalArgumentException {

    if (tokens[0].equals("from")) {
      if (tokens.length >= 4 && isValidDateTime(tokens[1], "yyyy-MM-dd'T'HH:mm") && tokens[2].equals("to") && isValidDateTime(tokens[3], "yyyy-MM-dd'T'HH:mm")) {
        if (tokens.length == 4) {
          //model.createEvent(this.subject, tokens[1], tokens[3]);
          System.out.println("Created Event!");
        } else {
          if (tokens[4].equals("repeats")) {
            if (tokens.length < 8) {
              throw new IllegalArgumentException("Missing part of the command");
            } else {
              if (isValidWeekdayFormat(tokens[5])) {
                Set<Character> weekDays = new HashSet<>();

                for (char c : tokens[5].toCharArray()) {
                  weekDays.add(c);
                }

                if (tokens[6].equals("for")) {
                  int numberOfWeeks;
                  try {
                    numberOfWeeks = Integer.parseInt(tokens[7]);
                  } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Must specify times to repeat as an integer");
                  }

                  if (tokens.length < 9 || !tokens[8].equals("times")) {
                    throw new IllegalArgumentException("Typo in the command: make sure \"times\" is included");
                  } else {
                    //model.createEventSeries(this.subject, tokens[1], tokens[2], weekDays, numberOfWeeks);
                    System.out.println("Created Event Series!");
                  }
                } else if (tokens[6].equals("until") && isValidDateTime(tokens[7], "yyyy-MM-dd")) {
                  //model.createEventSeries(this.subject, tokens[1], tokens[2], weekdays, tokens[7]
                  System.out.println("Created Event Series!");
                } else {
                  throw new IllegalArgumentException("Typo in the command");
                }
              } else {
                throw new IllegalArgumentException("Weekday format must of M T W R F S U");
              }
            }
          } else {
            throw new IllegalArgumentException("You must specify series using \"repeats\"");
          }
        }
      } else {
        throw new IllegalArgumentException("Invalid date time format, must be: \"yyyy-MM-ddTHH:mm to yyyy-MM-ddTHH:mm\"");
      }
    } else if (tokens[0].equals("on")) {

    } else {
      throw new IllegalArgumentException("Unknown command: create an event using \"from\" or \"on\"");
    }
  }
}
