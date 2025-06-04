//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Model;

import java.time.LocalDateTime;

public abstract class EventBuilder<T extends EventBuilder<T>> {
  protected String subject;
  protected LocalDateTime startDateTime
  protected LocalTime startTime;
  protected LocalTime endTime;
  protected String description;
  protected Location location;
  protected Status status;

  public T setEndDateTime(LocalDateTIme endDateTime) {
    this.endDateTime = endDateTime;
    return (T)this.returnBuilder();
  }

  public T setStartDateTime(LocalDateTime startDateTime) {
    this.startDateTime = startDateTime;
    return (T)this.returnBuilder();
  }


  public T setDescription(String description) {
    this.description = description;
    return (T)this.returnBuilder();
  }

  public T setSubject(String subject) {
    this.subject = subject;
    return (T)this.returnBuilder();
  }

  public T setLocation(Location location) {
    this.location = location;
    return (T)this.returnBuilder();
  }

  public T setStatus(Status status) {
    this.status = status;
    return (T)this.returnBuilder();
  }

  public abstract EventInterface build();

  protected abstract T returnBuilder();
}
