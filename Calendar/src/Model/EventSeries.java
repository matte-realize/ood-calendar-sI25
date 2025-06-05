package Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class EventSeries extends Event {
  private List<Event> instances = new ArrayList<>();
  private List<java.time.DayOfWeek> repeatDays;
  private Integer occurrences;
  private LocalDateTime endDateTimeOfSeries;

  public EventSeries(String subject, LocalDateTime startDateTime) {
    super(subject, startDateTime);
  }

  public List<java.time.DayOfWeek> getRepeatDays() {
    return repeatDays;
  }

  public void setRepeatDays(List<java.time.DayOfWeek> repeatDays) {
    this.repeatDays = repeatDays;
  }

  public Integer getOccurrences() {
    return occurrences;
  }

  public void setOccurrences(Integer occurrences) {
    this.occurrences = occurrences;
  }

  public LocalDateTime getEndDateTimeOfSeries() {
    return endDateTimeOfSeries;
  }

  public void setEndDateTimeOfSeries(LocalDateTime endDateTimeOfSeries) {
    this.endDateTimeOfSeries = endDateTimeOfSeries;
  }

  public List<Event> getInstances() {
    return instances;
  }

  public void addInstance(Event event) {
    this.instances.add(event);
  }
}
