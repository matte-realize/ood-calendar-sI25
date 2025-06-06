package Model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calendar model class that implements the CalenderInterface in order to provide functions
 * for the controller's interaction to create events or event series as well as edit events
 * or even series.
 */
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
      start = start.toLocalDate().atTime(8, 0);
      end = start.toLocalDate().atTime(17, 0);
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
                                       Integer occurrences,
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

    int count = 0;

    Duration eventDuration;
    LocalDateTime seriesEndDate;

    if (occurrences != null && occurrences > 0) {
      seriesEndDate = null;
      if (start.getHour() == 8 && start.getMinute() == 0) {
        eventDuration = Duration.ofHours(9);
      } else {
        eventDuration = Duration.ofHours(1);
      }
    } else {
      seriesEndDate = end;
      if (start.getHour() == 8 && start.getMinute() == 0) {
        eventDuration = Duration.ofHours(9);
      } else {
        eventDuration = Duration.ofHours(1);
      }
    }

    LocalDateTime currentCheck = start;

    while ((occurrences == null || count < occurrences) &&
            (seriesEndDate == null || !currentCheck.toLocalDate().isAfter(seriesEndDate.toLocalDate()))) {

      if (repeatDays.contains(currentCheck.getDayOfWeek())) {
        LocalDateTime instanceStart = currentCheck.toLocalDate().atTime(start.toLocalTime());
        LocalDateTime instanceEnd = instanceStart.plus(eventDuration);

        Event.CustomEventBuilder builder = new Event.CustomEventBuilder()
                .setSubject(subject)
                .setStartDateTime(instanceStart)
                .setEndDateTime(instanceEnd)
                .setDescription(description)
                .setLocation(location)
                .setStatus(status);

        Event instance = (Event) builder.build();
        series.addInstance(instance);
        eventsByDate.computeIfAbsent(instanceStart.toLocalDate(), d -> new ArrayList<>()).add(instance);

        count++;

        if (occurrences != null && count >= occurrences) {
          break;
        }
      }

      currentCheck = currentCheck.plusDays(1);
    }

    mapSeries.put(subject, series);
    return series;
  }

  private LocalDateTime nextValidDay(LocalDateTime from, List<DayOfWeek> repeatDays) {
    LocalDateTime candidate = from.plusDays(1);
    for (int i = 0; i < 7; i++) {
      if (repeatDays.contains(candidate.getDayOfWeek())) {
        return candidate;
      }
      candidate = candidate.plusDays(1);
    }
    return candidate;
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
        /*for (Event instance : series.getInstances()) {
          if (!instance.getStartDateTime().isBefore(start)) {
            Event newInstance = buildReplacementFrom(instance, updatedEvent);
            eventReplacement(instance, newInstance);
          }
        }*/
        break;

      case ALL:
        /*for (Event instance : series.getInstances()) {
          Event newInstance = buildReplacementFrom(instance, updatedEvent);
          eventReplacement(instance, newInstance);
        }*/
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
  public EventInterface getEvent(String subject, LocalDateTime start, LocalDateTime end) {
    LocalDate date = start.toLocalDate();
    List<Event> events = eventsByDate.getOrDefault(date, Collections.emptyList());

    for (Event e : events) {
      if (e.getSubject().equals(subject) &&
              e.getStartDateTime().equals(start) &&
              ((end == null && e.getEndDateTime() == null) || (e.getEndDateTime() != null && e.getEndDateTime().equals(end)))) {
        return e;
      }
    }
    return null;
  }

  public List<Event> getEventsSingleDay(LocalDate date) {
    List<Event> filteredEvents = new ArrayList<>();
    List<Event> allEvents = new ArrayList<>();

    for (List<Event> events : eventsByDate.values()) {
      allEvents.addAll(events);
    }

    for (Event e : allEvents) {
      LocalDateTime eventStart = e.getStartDateTime();
      LocalDateTime eventEnd = e.getEndDateTime();

      if (eventStart.toLocalDate().equals(date) || eventEnd.toLocalDate().equals(date)
              || (eventStart.toLocalDate().isBefore(date) && eventEnd.toLocalDate().isAfter(date))) {
        filteredEvents.add(e);
      }
    }

    return filteredEvents;
  }

  public List<Event> getEventsWindow(LocalDateTime start, LocalDateTime end) {
    List<Event> filteredEvents = new ArrayList<>();
    List<Event> allEvents = new ArrayList<>();

    for (List<Event> events : eventsByDate.values()) {
      allEvents.addAll(events);
    }

    for (Event e : allEvents) {
      LocalDateTime eventStart = e.getStartDateTime();
      LocalDateTime eventEnd = e.getEndDateTime();

      if ((eventStart.isAfter(start) && eventStart.isBefore(end)) ||
              (eventEnd.isAfter(start) && eventEnd.isBefore(end)) ||
              (eventStart.isBefore(start) && eventEnd.isAfter(end))) {
        filteredEvents.add(e);
      }
    }

    return filteredEvents;
  }

  private void baseExceptions(String subject, LocalDateTime start) throws IllegalArgumentException {
    if (subject == null || subject.isEmpty()) {
      throw new IllegalArgumentException("Subject must contain a string!");
    }
    if (start == null) {
      throw new IllegalArgumentException("Start time cannot be null!");
    }
  }
}