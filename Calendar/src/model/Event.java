package model;

import java.time.LocalDateTime;

/**
 * An event class that implements the event interface
 * that acts as an event for the calendar provided with
 * a subject, start date time, end date time, description,
 * location, and status.
 */
public class Event implements EventInterface {
  private final String subject;
  private final LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private String description;
  private Location location;
  private Status status;

  /**
   * Constructor that takes in the subject and start date time
   * arguments as the minimum necessary arguments for an event.
   *
   * @param subject       the subject that is the unique identifier for the event.
   * @param startDateTime the start time for the event.
   * @throws IllegalArgumentException if any of the parameters given are invalid.
   */
  public Event(String subject, LocalDateTime startDateTime) throws IllegalArgumentException {
    if (subject == null || subject.isEmpty()) {
      throw new IllegalArgumentException("Subject cannot be null or empty");
    }
    this.subject = subject;
    if (startDateTime == null) {
      throw new IllegalArgumentException("StartDateTime cannot be null");
    }
    this.startDateTime = startDateTime;
  }

  @Override
  public String getSubject() {
    return subject;
  }

  @Override
  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  @Override
  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Location getLocation() {
    return location;
  }

  @Override
  public Status getStatus() {
    return status;
  }

  /**
   * Returns a string over the event based on the minimum
   * details provided.
   *
   * @return a string describing the details of the event.
   */
  public String printEvent() {
    String eventString = this.subject
            + " from "
            + this.startDateTime.toString()
            + " to "
            + this.endDateTime.toString();

    if (this.location != null) {
      eventString += " " + this.location;
    }
    return eventString;
  }

  /**
   * A class representing a builder for creating events that extends
   * the event builder.
   */
  public static class CustomEventBuilder extends EventBuilder<CustomEventBuilder> {
    /**
     * Performs the build command to create an event.
     *
     * @return an event interface based on the details of the event.
     */
    public EventInterface build() {
      Event event = new Event(this.subject, this.startDateTime);
      event.description = this.description;
      event.location = this.location;
      event.status = this.status;
      event.endDateTime = this.endDateTime;
      return event;
    }

    protected CustomEventBuilder returnBuilder() {
      return this;
    }
  }
}
