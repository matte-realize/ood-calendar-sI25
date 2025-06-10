package model.calendar;

import java.time.ZoneId;

public interface CalendarModelInterface {
  String getName();

  ZoneId getTimeZone();
}
