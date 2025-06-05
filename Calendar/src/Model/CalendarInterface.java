package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface defines all the model operations that
 * a calendar should be able to support, including that of:
 * creating an event or event series, editing events or event
 * series, querying for events on a certain date or date-time,
 * and check for availability status on a respective date-time.
 */
public interface CalendarInterface {
  /**
   * Creates a single event within the date for the calendar.
   */
  Event createEvent(String subject,
                             LocalDateTime start,
                             LocalDateTime end,
                             String description,
                             Location location,
                             Status status
  ) throws IllegalArgumentException;

  /**
   * Creates a recurring event series where each event will
   * repeatedly start and end on the same day and share the
   * same start and end time.
   */
  void createEventSeries();

  /**
   * Edits a single event whether it's an independent event or
   * part of a series.
   */
  void editEvent(String subject,
                 LocalDateTime start,
                 EventInterface updatedEvent
  );

  /**
   * Edits the entire event series.
   */
  void editEventSeries();

  /**
   * Gets the events based on the input of a given date.
   * @param date the date that is input to display all the events.
   * @return a list based on what events are occurring on that day.
   */
  List<Event> getEvent(LocalDate date);

  /**
   * Gets the event series based on the input of a given timestamp.
   * @return
   */
  List<Event> getEventSeries();
}
