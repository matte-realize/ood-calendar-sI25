package model.calendar;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * An abstract class that is capable of creating Calendar objects through
 * the builder pattern.
 *
 * @param <T> the type of the builder subclass.
 */
public abstract class CalendarBuilder<T extends CalendarBuilder<T>> {
  protected String calendarName;
  protected ZoneId timezone;
  protected Calendar calendar;

  public T setName(String calendarName)
  {
    this.calendarName = calendarName;
    return this.returnBuilder();
  }

  public T setTimeZone(ZoneId timeZone) {
    this.timezone = timeZone;
    return this.returnBuilder();
  }

  public T setCalendar(Calendar calendar) {
    this.calendar = calendar;
    return this.returnBuilder();
  }

  public abstract CalendarModel build();

  protected abstract T returnBuilder();
}
