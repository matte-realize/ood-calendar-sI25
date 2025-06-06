package Model;

import java.time.LocalDateTime;

public class Event implements EventInterface {
  private final String subject;
  private final LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private String description;
  private Location location;
  private Status status;

  public Event(String subject, LocalDateTime startDateTime) throws IllegalArgumentException {
    this.subject = subject;
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

  public String printEvent() {
    String eventString = this.subject + " from " + this.startDateTime.toString() + " to " + this.endDateTime.toString();

    if (this.location != null) {
      eventString += " " + this.location.toString();
    }
    return eventString;
  }

  public static class CustomEventBuilder extends EventBuilder<CustomEventBuilder> {
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
