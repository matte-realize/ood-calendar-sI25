package model;

import java.time.DayOfWeek;
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
   * Creates a single event within the calendar and is not part
   * of a series.
   *
   * @param subject     the subject provided that uniquely identifies an event.
   * @param start       the starting time for the event.
   * @param end         the end time for the event.
   * @param description the description provided to describe the event.
   * @param location    the location of the event.
   * @param status      the status of the event.
   * @return returns an event based on the arguments provided.
   * @throws IllegalArgumentException based on the conditions where the event could
   *                                  not be created.
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
   *
   * @param subject     the subject provided that uniquely identifies an event.
   * @param start       the starting time for the event series.
   * @param end         the ending time for the event series.
   * @param repeatDays  the days that the event should be repeating.
   * @param occurrences the amount of occurrences that should occur for the event.
   * @param description the description provided to describe the event.
   * @param location    the location of the event.
   * @param status      the status of the event.
   * @return returns an event series based on the arguments provided.
   * @throws IllegalArgumentException based on the conditions where the event series
   *                                  could not be created.
   */
  EventSeries createEventSeries(String subject,
                                LocalDateTime start,
                                LocalDateTime end,
                                List<DayOfWeek> repeatDays,
                                Integer occurrences,
                                String description,
                                Location location,
                                Status status
  ) throws IllegalArgumentException;

  /**
   * Edits a single event and applies modifications
   * whether it's an independent event or part of a series.
   *
   * @param subject      the subject provided that uniquely identifies an event.
   * @param start        the starting time for the event.
   * @param updatedEvent an updated event that is provided to modify the original event.
   * @param mode         the mode for which specific events should be changed.
   */
  void editEvent(String subject,
                 LocalDateTime start,
                 EventInterface updatedEvent,
                 EditMode mode
  );

  /**
   * Gets the events based on the input of a given date.
   *
   * @param subject the subject provided that uniquely identifies an event.
   * @param start   the start time for the event.
   * @param end     the end time for the event.
   * @return a list based on what events are occurring on that day.
   */
  EventInterface getEvent(String subject,
                          LocalDateTime start,
                          LocalDateTime end) throws IllegalArgumentException;
}
