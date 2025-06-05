package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calendar implements CalendarInterface {
  private final Map<LocalDate, List<Event>> independentEvents = new HashMap<>();
  private final Map<LocalDate, List<Event>> eventsByDate = new HashMap<>();
  private final Map<String, EventSeries> mapSeries = new HashMap<>();

  @Override
  public Event createEvent(String subject,
                                    LocalDateTime start,
                                    LocalDateTime end,
                                    String description,
                                    Location location,
                                    Status status)
                                    throws IllegalArgumentException {
    baseExceptions(subject, start);

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

    EventInterface eventBuilt = builder.build();

    LocalDate dateKey = start.toLocalDate();
    eventsByDate.computeIfAbsent(dateKey, d -> new ArrayList<>()).add((Event) eventBuilt);

    return (Event) eventBuilt;
  }

  @Override
  public void createEventSeries() {

  }

  @Override
  public void editEvent(String subject,
                        LocalDateTime start,
                        EventInterface updatedEvent) {
    baseExceptions(subject, start);
    if (updatedEvent == null) {
      throw new IllegalArgumentException("Updated event cannot be null!");
    }

    LocalDate originalDate = start.toLocalDate();
    List<Event> candidates = new ArrayList<>();
    for (Event e : eventsByDate.getOrDefault(originalDate, Collections.emptyList())) {
      if (e.getSubject().equals(subject) && e.getStartDateTime().equals(start)) {
        candidates.add(e);
      }
    }

    Event toReplace = candidates.getFirst();

    eventsByDate.get(originalDate).remove(toReplace);

    LocalDate newDate = updatedEvent.getStartDateTime().toLocalDate();
    eventsByDate.computeIfAbsent(newDate, d -> new ArrayList<>())
            .add((Event) updatedEvent);

  }

  @Override
  public void editEventSeries() {

  }

  @Override
  public List<Event> getEvent(LocalDate date) {
    return eventsByDate.getOrDefault(date, Collections.emptyList());
  }

  @Override
  public List<Event> getEventSeries() {
    return List.of();
  }

  public void baseExceptions(String subject, LocalDateTime start) throws IllegalArgumentException {
    if (subject == null || subject.isEmpty()) {
      throw new IllegalArgumentException("Subject must contain a string!");
    }
    if (start == null) {
      throw new IllegalArgumentException("Start time cannot be null!");
    }
  }
}
