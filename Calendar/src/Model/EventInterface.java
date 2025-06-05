package Model;

import java.time.LocalDateTime;

public interface EventInterface {
  String getSubject();
  LocalDateTime getStartDateTime();
  LocalDateTime getEndDateTime();
  String getDescription();
  Location getLocation();
  Status getStatus();
}
