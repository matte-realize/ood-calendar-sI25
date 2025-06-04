package Model;

import java.time.LocalDateTime;
import java.util.Optional;

public class Event implements EventInterface {
  private String subject;
  private LocalDateTime startDateTime;
  private Optional<LocalDateTime> endDateTime;
  private Optional<String> description;
  private Location location;
  private Status status;

  public Event() throws IllegalArgumentException {
  }
}
