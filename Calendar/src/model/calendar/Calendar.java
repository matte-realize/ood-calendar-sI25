package model.calendar;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.enums.EditMode;
import model.event.Event;
import model.event.EventInterface;
import model.event.EventSeries;
import model.enums.Location;
import model.enums.Status;

/**
 * Calendar model class that implements the CalenderInterface in order to provide functions
 * for the controller's interaction to create events or event series as well as edit events
 * or even series.
 */
public class Calendar implements CalendarInterface {
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

    if (end.isBefore(start) || end.isEqual(start)) {
      throw new IllegalArgumentException("End time can not be before or on start time.");
    }

    LocalDate dateKey = start.toLocalDate();
    List<Event> eventsOnDate = eventsByDate.getOrDefault(dateKey, Collections.emptyList());

    for (Event e : eventsOnDate) {
      if (e.getSubject().equals(subject)
              && e.getStartDateTime().equals(start)
              && e.getEndDateTime().equals(end)) {
        throw new IllegalArgumentException("An event with the same subject, start, and end time already exists.");
      }
    }

    Event.CustomEventBuilder builder = new Event.CustomEventBuilder()
            .setSubject(subject)
            .setStartDateTime(start)
            .setEndDateTime(end)
            .setDescription(description)
            .setLocation(location)
            .setStatus(status);

    EventInterface eventBuilt = builder.build();

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

    for (EventSeries events : mapSeries.values()) {
      if (events.getSubject().equals(subject) || events.getStartDateTime().equals(start)) {
        throw new IllegalArgumentException("Subject and start time cannot be the same as an existing series!");
      }
    }

    if (end == null) {
      end = start.toLocalDate().atTime(17, 0);
    }

    if (repeatDays == null || repeatDays.isEmpty()) {
      throw new IllegalArgumentException("Repeat days cannot be null or empty.");
    }

    if (end.isBefore(start) || end.isEqual(start)) {
      throw new IllegalArgumentException("End time can not be before or on start time.");
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

    while ((occurrences == null || count < occurrences)
            && (seriesEndDate == null || !currentCheck.toLocalDate().isAfter(seriesEndDate.toLocalDate()))) {

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
        eventsByDate.computeIfAbsent(
                instanceStart.toLocalDate(),
                d -> new ArrayList<>()
        ).add(instance);

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

  @Override
  public void editEvent(String subject,
                        LocalDateTime start,
                        EventInterface updatedEvent,
                        EditMode mode) {
    baseExceptions(subject, start);
    validateUpdatedEvent(updatedEvent);

    Event targetEvent = findTargetEvent(subject, start);
    EventSeries parentSeries = findParentSeries(subject, start, targetEvent);

    if (parentSeries == null) {
      eventReplacement(targetEvent, updatedEvent);
      return;
    }

    editEventInSeries(mode, parentSeries, targetEvent, updatedEvent);
  }

  private void validateUpdatedEvent(EventInterface updatedEvent) {
    if (updatedEvent == null) {
      throw new IllegalArgumentException("Updated event cannot be null!");
    }
  }

  private Event findTargetEvent(String subject, LocalDateTime start) {
    Event targetEvent = findEventInSeries(subject, start);
    if (targetEvent == null) {
      targetEvent = findEventInDate(subject, start);
    }
    if (targetEvent == null) {
      throw new IllegalArgumentException("Event not found: " + subject + " at " + start);
    }
    return targetEvent;
  }

  private Event findEventInSeries(String subject, LocalDateTime start) {
    EventSeries candidateSeries = mapSeries.get(subject);
    if (candidateSeries != null) {
      for (Event instance : candidateSeries.getInstances()) {
        if (instance.getSubject().equals(subject) && instance.getStartDateTime().equals(start)) {
          return instance;
        }
      }
    }
    return null;
  }

  private Event findEventInDate(String subject, LocalDateTime start) {
    LocalDate originalDate = start.toLocalDate();
    List<Event> candidates = eventsByDate.getOrDefault(originalDate, Collections.emptyList());
    for (Event e : candidates) {
      if (e.getSubject().equals(subject) && e.getStartDateTime().equals(start)) {
        return e;
      }
    }
    return null;
  }

  private EventSeries findParentSeries(String subject, LocalDateTime start, Event targetEvent) {
    EventSeries candidateSeries = mapSeries.get(subject);
    if (candidateSeries != null) {
      for (Event instance : candidateSeries.getInstances()) {
        if (instance.equals(targetEvent)) {
          return candidateSeries;
        }
      }
    }
    return null;
  }

  private void editEventInSeries(EditMode mode, EventSeries parentSeries, Event targetEvent, EventInterface updatedEvent) {
    switch (mode) {
      case SINGLE:
        editSingleEventInSeries(parentSeries, targetEvent, updatedEvent);
        break;
      case FUTURE:
        editFutureEventsInSeries(parentSeries, targetEvent, updatedEvent);
        break;
      case ALL:
        editAllEventsInSeries(parentSeries, updatedEvent);
        break;
      default:
        throw new IllegalArgumentException("Unsupported edit mode: " + mode);
    }
  }

  private void editSingleEventInSeries(EventSeries series, Event targetEvent, EventInterface updatedEvent) {
    eventReplacement(targetEvent, updatedEvent);

    List<Event> instances = series.getInstances();
    for (int i = 0; i < instances.size(); i++) {
      if (instances.get(i).equals(targetEvent)) {
        instances.set(i, (Event) updatedEvent);
        break;
      }
    }
  }

  private void editFutureEventsInSeries(EventSeries series, Event targetEvent, EventInterface updatedEvent) {
    LocalDateTime targetStart = targetEvent.getStartDateTime();
    List<Event> instances = series.getInstances();

    for (Event instance : instances) {
      if (!instance.getStartDateTime().isBefore(targetStart)) {
        Event newInstance = buildReplacementFrom(instance, updatedEvent);
        eventReplacement(instance, newInstance);

        for (int i = 0; i < instances.size(); i++) {
          if (instances.get(i).equals(instance)) {
            instances.set(i, newInstance);
            break;
          }
        }
      }
    }
  }

  private void editAllEventsInSeries(EventSeries series, EventInterface updatedEvent) {
    List<Event> instances = new ArrayList<>(series.getInstances());

    for (Event instance : instances) {
      Event newInstance = buildReplacementFrom(instance, updatedEvent);
      eventReplacement(instance, newInstance);

      List<Event> seriesInstances = series.getInstances();
      for (int i = 0; i < seriesInstances.size(); i++) {
        if (seriesInstances.get(i).equals(instance)) {
          seriesInstances.set(i, newInstance);
          break;
        }
      }
    }
  }

  private Event buildReplacementFrom(Event originalInstance, EventInterface template) {
    Duration originalDuration = Duration.between(
            originalInstance.getStartDateTime(),
            originalInstance.getEndDateTime()
    );
    Duration templateDuration = Duration.between(
            template.getStartDateTime(),
            template.getEndDateTime()
    );

    Duration newDuration = templateDuration.isZero() ? originalDuration : templateDuration;
    LocalDateTime newEnd = originalInstance.getStartDateTime().plus(newDuration);

    Event.CustomEventBuilder builder = new Event.CustomEventBuilder()
            .setSubject(template.getSubject())
            .setStartDateTime(originalInstance.getStartDateTime())
            .setEndDateTime(newEnd)
            .setDescription(template.getDescription())
            .setLocation(template.getLocation())
            .setStatus(template.getStatus());

    return (Event) builder.build();
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
  public EventInterface getEvent(String subject,
                                 LocalDateTime start,
                                 LocalDateTime end) throws IllegalArgumentException {
    LocalDate date = start.toLocalDate();
    List<Event> events = eventsByDate.getOrDefault(date, Collections.emptyList());
    boolean found = false;
    EventInterface returnEvent = null;

    for (Event e : events) {
      if (e.getSubject().equals(subject)
              && e.getStartDateTime().equals(start)
              && ((end == null) ||
              (e.getEndDateTime() != null && e.getEndDateTime().equals(end)))) {
        if (found) {
          throw new IllegalArgumentException("Multiple Events with the same Start and Subject");
        } else {
          found = true;
          returnEvent = e;
        }
      }
    }
    return returnEvent;
  }

  @Override
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
              || (eventStart.toLocalDate().isBefore(date)
              && eventEnd.toLocalDate().isAfter(date))) {
        filteredEvents.add(e);
      }
    }

    return filteredEvents;
  }

  @Override
  public List<Event> getEventsWindow(LocalDateTime start, LocalDateTime end) {
    List<Event> filteredEvents = new ArrayList<>();
    List<Event> allEvents = new ArrayList<>();

    for (List<Event> events : eventsByDate.values()) {
      allEvents.addAll(events);
    }

    for (Event e : allEvents) {
      LocalDateTime eventStart = e.getStartDateTime();
      LocalDateTime eventEnd = e.getEndDateTime();

      if (((eventStart.isAfter(start) || eventStart.isEqual(start)) && (eventStart.isBefore(end) || eventStart.isEqual(end)))
              || ((eventEnd.isAfter(start) || eventEnd.isEqual(start)) && (eventEnd.isBefore(end) || eventEnd.isEqual(end)))
              || ((eventStart.isBefore(start) || eventStart.isEqual(start)) && (eventEnd.isAfter(end) || eventEnd.isEqual(end)))) {
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