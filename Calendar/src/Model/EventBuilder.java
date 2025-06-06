package Model;

import java.time.LocalDateTime;

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

  public T setEndDateTime(LocalDateTime endDateTime) {
    this.endDateTime = endDateTime;
    return (T) this.returnBuilder();
  }

  public T setStartDateTime(LocalDateTime startDateTime) {
    this.startDateTime = startDateTime;
    return (T) this.returnBuilder();
  }

  public T setDescription(String description) {
    this.description = description;
    return (T) this.returnBuilder();
  }

  public T setSubject(String subject) {
    this.subject = subject;
    return (T) this.returnBuilder();
  }

  public T setLocation(Location location) {
    this.location = location;
    return (T) this.returnBuilder();
  }

  public T setStatus(Status status) {
    this.status = status;
    return (T) this.returnBuilder();
  }

  public abstract EventInterface build();

  protected abstract T returnBuilder();
}
