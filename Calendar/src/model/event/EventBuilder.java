package model.event;

import java.time.LocalDateTime;

import model.enums.Location;
import model.enums.Status;

/**
 * An abstract class that is capable of creating event objects through
 * the builder pattern.
 *
 * @param <T> the type of the builder subclass.
 */
public abstract class EventBuilder<T extends EventBuilder<T>> {
  protected String subject;
  protected LocalDateTime startDateTime;
  protected LocalDateTime endDateTime;
  protected String description;
  protected Location location;
  protected Status status;

  /**
   * A setter for the end date time for the event.
   *
   * @param endDateTime the local date time representing the end date
   *                    time of the event.
   * @return a T type instance of the builder for the end date time.
   */
  public T setEndDateTime(LocalDateTime endDateTime) {
    this.endDateTime = endDateTime;
    return (T) this.returnBuilder();
  }

  /**
   * A setter for the start date time for the event.
   *
   * @param startDateTime the local date time representing the start date
   *                      time of the event.
   * @return a T type instance of the builder for the start date time.
   */
  public T setStartDateTime(LocalDateTime startDateTime) {
    this.startDateTime = startDateTime;
    return (T) this.returnBuilder();
  }

  /**
   * A setter for the description for the event.
   *
   * @param description the string that represents the description of the event.
   * @return a T type instance of the builder for the description.
   */
  public T setDescription(String description) {
    this.description = description;
    return (T) this.returnBuilder();
  }

  /**
   * A setter for the subject for the event.
   *
   * @param subject the string that represents the subject of the event.
   * @return a T type instance of the builder for the subject.
   */
  public T setSubject(String subject) {
    this.subject = subject;
    return (T) this.returnBuilder();
  }

  /**
   * A setter for the location for the event.
   *
   * @param location the location given for the event.
   * @return a T type instance of the builder for the location.
   */
  public T setLocation(Location location) {
    this.location = location;
    return (T) this.returnBuilder();
  }

  /**
   * A setter for the status for the event.
   *
   * @param status the status given for the event.
   * @return a T type instance of the builder for the status.
   */
  public T setStatus(Status status) {
    this.status = status;
    return (T) this.returnBuilder();
  }

  /**
   * The abstract method that allows for the building of an event.
   *
   * @return an EventInterface that represents the event created.
   */
  public abstract EventInterface build();

  protected abstract T returnBuilder();
}
