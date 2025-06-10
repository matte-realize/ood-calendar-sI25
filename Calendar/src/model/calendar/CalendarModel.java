package model.calendar;

import java.time.ZoneId;

public class CalendarModel implements CalendarModelInterface {
  private String name;
  private ZoneId timeZone;

  public CalendarModel(String name, ZoneId timeZone) {
    this.name = name;
    this.timeZone = timeZone;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public ZoneId getTimeZone() {
    return timeZone;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setTimeZone(ZoneId timeZone) {
    this.timeZone = timeZone;
  }
}
