package Model;

import java.time.LocalTime;
import java.time.LocalDate;

public class Event implements EventInterface {
  private String subject;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private String description;
  private Location location;
  private Status status;

  public Event() throws IllegalArgumentException {
  }
}
