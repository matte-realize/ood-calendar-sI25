package Model;

import java.time.LocalDate;
import java.util.List;

public class Calendar implements CalendarInterface {

  @Override
  public void createEvent() {

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
