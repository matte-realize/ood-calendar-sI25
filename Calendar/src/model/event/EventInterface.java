package model.event;

import java.time.LocalDateTime;

import model.enums.Location;
import model.enums.Status;

/**
 * This interface defines all the getters that an event
 * should be supported by, including that of: subject,
 * start time, date time, description, location, and
 * status.
 */
public interface EventInterface {
  /**
   * Gets the subject of the event.
   *
   * @return a string based on the subject of the event.
   */
  String getSubject();

  /**
   * Gets the start time of the event.
   *
   * @return a LocalDateTime based on the start time of the event.
   */
  LocalDateTime getStartDateTime();

  /**
   * Gets the end time of the event.
   *
   * @return a LocalDateTime based on the end time of the event.
   */
  LocalDateTime getEndDateTime();

  /**
   * Gets the description of the event.
   *
   * @return a string based on the description of the event.
   */
  String getDescription();

  /**
   * Gets the location of the event.
   *
   * @return an enum based on the location of the event.
   */
  Location getLocation();

  /**
   * Gets the status of the event.
   *
   * @return an enum based on the status of the event.
   */
  Status getStatus();
}
