package view;

import java.util.List;

import model.event.Event;

/**
 * Calendar view class that implements the CalenderViewInterface
 * which is able to display to the user the application for the
 * calendar.
 */
public class CalendarView implements CalendarViewInterface {
  /**
   * Simple constructor for the calendar view.
   */
  /*public CalendarView() {
  }*/

  @Override
  public void printEvents(List<Event> events, String day) {
    if (events.isEmpty()) {
      System.out.println("No events found on " + day);
    } else {
      System.out.println("Events found on " + day + ":");
      for (Event event : events) {
        System.out.println("*  " + event.printEvent());
      }
    }
  }

  @Override
  public void printStatus(String status, String day) {
    System.out.println(status + " on " + day);
  }
}
