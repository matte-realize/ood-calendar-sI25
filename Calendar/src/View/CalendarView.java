package View;

import java.util.List;

import Model.Event;

public class CalendarView implements CalendarViewInterface {

  public CalendarView() {}

  public void PrintEvents(List<Event> events, String day) {

    if (events.isEmpty()) {
      System.out.println("No events found on " + day);
    } else {
      System.out.println("Events found on " + day + ":");
      for (Event event : events) {
        System.out.println("*  " + event.printEvent());
      }
    }
  }

  public void PrintStatus(String status, String day) {

    System.out.println(status + " on " + day);
  }
}
