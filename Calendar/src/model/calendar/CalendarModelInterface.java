package model.calendar;

import java.time.ZoneId;

public interface CalendarModelInterface {
  String getName();

  ZoneId getTimeZone();

  void setName(String name);

  void setTimeZone(ZoneId timeZone);
}
