package Model;

import java.time.LocalDateTime;

public class EventSeries extends Event {

  public EventSeries(String subject, LocalDateTime startDateTime) throws IllegalArgumentException {
    super(subject, startDateTime);
  }
}
