package Model;

import java.time.DayOfWeek;
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
  public final Map<String, EventSeries> mapSeries = new HashMap<>();

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
  public EventSeries createEventSeries(String subject,
                                       LocalDateTime start,
                                       LocalDateTime end,
                                       List<DayOfWeek> repeatDays,
                                       int occurrences,
                                       String description,
                                       Location location,
                                       Status status) {
    baseExceptions(subject, start);

    if (repeatDays == null || repeatDays.isEmpty()) {
      throw new IllegalArgumentException("Repeat days cannot be null or empty.");
    }

    EventSeries series = new EventSeries(subject, start);
    series.setRepeatDays(repeatDays);
    series.setOccurrences(occurrences);
    series.setEndDateTimeOfSeries(end);

    LocalDateTime current = start;
    int count = 0;

    while (count < occurrences && (end == null || !current.isAfter(end))) {
      if (repeatDays.contains(current.getDayOfWeek())) {
        Event.CustomEventBuilder builder = new Event.CustomEventBuilder()
                .setSubject(subject)
                .setStartDateTime(current)
                .setEndDateTime(current.plusHours(1))
                .setDescription(description)
                .setLocation(location)
                .setStatus(status);

        Event instance = (Event) builder.build();
        series.addInstance(instance);
        eventsByDate.computeIfAbsent(current.toLocalDate(), d -> new ArrayList<>()).add(instance);
        count++;
      }
      current = current.plusDays(1);
    }

    mapSeries.put(subject, series);
    return series;
  }

  @Override
  public void editEvent(String subject,
                        LocalDateTime start,
                        EventInterface updatedEvent,
                        EditMode mode) {
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

    Event edittedEvent = candidates.get(0);

    if (!(edittedEvent instanceof EventSeries)) {
      eventReplacement(edittedEvent, updatedEvent);
      return;
    }

    EventSeries series = (EventSeries) edittedEvent;

    switch (mode) {
      case SINGLE:
        eventReplacement(edittedEvent, updatedEvent);
        break;

      case FUTURE:
        for (int i = 0; i < series.getInstances().size(); i++) {
          Event instance = series.getInstances().get(i);
          if (!instance.getStartDateTime().isBefore(start)) {
            eventReplacement(instance, updatedEvent);
            series.getInstances().set(i, (Event) updatedEvent);
          }
        }
        break;

      case ALL:
        for (int i = 0; i < series.getInstances().size(); i++) {
          Event instance = series.getInstances().get(i);
          eventReplacement(instance, updatedEvent);
          series.getInstances().set(i, (Event) updatedEvent);
        }
        break;
      default:
        throw new IllegalArgumentException("Unsupported edit mode: " + mode);
    }
  }

  private void eventReplacement(Event oldEvent, EventInterface updatedEvent) {
    LocalDate originalDate = oldEvent.getStartDateTime().toLocalDate();
    List<Event> list = eventsByDate.get(originalDate);
    if (list != null) {
      list.remove(oldEvent);
    }

    LocalDate newDate = updatedEvent.getStartDateTime().toLocalDate();
    eventsByDate.computeIfAbsent(newDate, d -> new ArrayList<>()).add((Event) updatedEvent);
  }

  @Override
  public EventInterface getEvent(String subject, LocalDate date) {
    List<Event> events = eventsByDate.getOrDefault(date, Collections.emptyList());
    for (Event e : events) {
      if (e.getSubject().equals(subject) && e.getStartDateTime().toLocalDate().equals(date)) {
        return e;
      }
    }
    return null;
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
