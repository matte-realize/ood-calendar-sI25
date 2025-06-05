package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calendar implements CalendarInterface {
  private final Map<LocalDate, List<Event>> independentEvents = new HashMap<>();
  private final Map<LocalDate, List<Event>> allEvents = new HashMap<>();
  private final Map<String, EventSeries> mapSeries = new HashMap<>();

  @Override
  public EventInterface createEvent(String subject,
                                    LocalDateTime start,
                                    LocalDateTime end,
                                    String description,
                                    Location location,
                                    Status status)
                                    throws IllegalArgumentException {
    if (subject == null || subject.isEmpty()) {
      throw new IllegalArgumentException("Subject must contain a string!");
    }
    if (start == null) {
      throw new IllegalArgumentException("Start time cannot be null!");
    }

    if (end == null) {
      start = start.toLocalDate().atStartOfDay();
      end = start.toLocalDate().atTime(23, 59);
    }

    if (end.isBefore(start)) {
      throw new IllegalArgumentException("End time can not be before start time.");
    }

    Event.CustomEventBuilder builder = new Event.CustomEventBuilder()
            .setSubject(subject)
            .setStartDateTime(start)
            .setEndDateTime(end)
            .setDescription(description)
            .setLocation(location)
            .setStatus(status);

    EventInterface eventBuilded = builder.build();
    Event newEvent = (Event) eventBuilded;

    // add eventsto list

    return newEvent;
  }

  @Override
  public void createEventSeries() {

  }

  @Override
  public void editEvent() {

  }

  @Override
  public void editEventSeries() {

  }

  @Override
  public List<Event> getEvent(LocalDate date) {
    return List.of();
  }

  @Override
  public List<Event> getEventSeries() {
    return List.of();
  }
}
