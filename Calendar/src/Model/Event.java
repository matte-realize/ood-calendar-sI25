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
