package view;

import java.util.List;

import model.Event;

/**
 * An interface that is able to define as the calendar view
 * which is able to print the events and status for the calendar.
 */
public interface CalendarViewInterface {
  /**
   * Displays the events on a specific day.
   *
   * @param events the list of events for displaying.
   * @param day    the day which the events are being displayed.
   */
  void printEvents(List<Event> events, String day);

  /**
   * Displays a status message for a specific day.
   *
   * @param status the status message to display.
   * @param day    the day which the event is associated with.
   */
  void printStatus(String status, String day);
}
