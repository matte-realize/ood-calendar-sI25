package Controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryEventCommand extends AbstractCommand {

  private static final Pattern PrintEvents = Pattern.compile(
          "^print events on (\\d{4}-\\d{2}-\\d{2})$");

  private static final Pattern PrintEventsWindow = Pattern.compile(
          "^print events from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})$");

  private static final Pattern ShowStatus = Pattern.compile(
          "^show status on (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})$");

  private final String tokensString;

  public QueryEventCommand(String tokensString, String command) {
    this.tokensString = command + tokensString;
  }

  @Override
  public void execute() throws IllegalArgumentException {

    Matcher m;

    if ((m = PrintEvents.matcher(tokensString)).matches()) {
      handlePrintEventsOn(m);
    } else if ((m = PrintEventsWindow.matcher(tokensString)).matches()) {
      handlePrintEventsFromTo(m);
    } else if ((m = ShowStatus.matcher(tokensString)).matches()) {
      handleShowStatusOn(m);
    } else {
      throw new IllegalArgumentException("Invalid query command: \"" + tokensString + "\"\n");
    }
  }

  private void handlePrintEventsOn(Matcher m) {
    String date = m.group(1);

    if (!isValidDate(date)) {
      throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
    }

    System.out.println("Printing events on " + date);
  }

  private void handlePrintEventsFromTo(Matcher m) {
    String startDate = m.group(1);
    String endDate = m.group(2);

    if (!isValidDateTime(startDate) || !isValidDateTime(endDate)) {
      throw new IllegalArgumentException("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
    }

    System.out.println("Printing events from " + startDate + " to " + endDate);

  }

  private void handleShowStatusOn(Matcher m) {
    String dateTime = m.group(1);

    if (!isValidDateTime(dateTime)) {
      throw new IllegalArgumentException("Invalid datetime format. Expected format: yyyy-MM-ddTHH:mm");
    }

    System.out.println("Status on " + dateTime + " is ---") ;
  }
}
