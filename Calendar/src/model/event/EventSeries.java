package model.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * An event series that extends the event class and serves as representing
 * the same events within the same category.
 */
public class EventSeries extends Event {
  private List<Event> instances = new ArrayList<>();
  public List<java.time.DayOfWeek> repeatDays;
  public Integer occurrences;
  public LocalDateTime endDateTimeOfSeries;

  /**
   * Constructor for an event series.
   *
   * @param subject       the subject that uniquely identifies an event to
   *                      be created for the series.
   * @param startDateTime the start time for the event series.
   */
  public EventSeries(String subject, LocalDateTime startDateTime) {
    super(subject, startDateTime);
  }

  /**
   * Sets the days of the week which the event should repeat.
   *
   * @param repeatDays a list of the DayOfWeek values indicated
   *                   the event should repeat on.
   */
  public void setRepeatDays(List<java.time.DayOfWeek> repeatDays) {
    this.repeatDays = repeatDays;
  }

  /**
   * Sets the total number of occurrences which the event should repeat.
   *
   * @param occurrences a integer for the amount of times an event
   *                    should be repeating.
   */
  public void setOccurrences(Integer occurrences) {
    this.occurrences = occurrences;
  }

  /**
   * Sets the end date time for the series.
   *
   * @param endDateTimeOfSeries a local date time for when the event
   *                            should end.
   */
  public void setEndDateTimeOfSeries(LocalDateTime endDateTimeOfSeries) {
    this.endDateTimeOfSeries = endDateTimeOfSeries;
  }

  /**
   * Gets the amount of instances of the event.
   *
   * @return a list for every instance of the event occurring.
   */
  public List<Event> getInstances() {
    return instances;
  }

  /**
   * Adds an instance of the event to the series.
   *
   * @param event the event that will be added to the series.
   */
  public void addInstance(Event event) {
    this.instances.add(event);
  }
}
