package model.calendar;

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

  /**
   * A setter for the name of the calendar.
   *
   * @param calendarName the string representing the calendar name.
   * @return a T type based on the name of the calendar.
   */
  public T setName(String calendarName)
  {
    this.calendarName = calendarName;
    return this.returnBuilder();
  }

  /**
   * A setter for the specific zone id given for the calendar.
   *
   * @param timeZone the zone id representing the calendar's time zone.
   * @return a T type based on the zone id of the calendar.
   */
  public T setTimeZone(ZoneId timeZone) {
    this.timezone = timeZone;
    return this.returnBuilder();
  }

  /**
   * A setter for the calendar based on the calendar given.
   *
   * @param calendar the calendar representing the calendar to be set.
   * @return a T type based on the calendar given in the setter.
   */
  public T setCalendar(Calendar calendar) {
    this.calendar = calendar;
    return this.returnBuilder();
  }

  /**
   * The abstract method that allows for the building of an calendar.
   *
   * @return an CalendarModel taht represents the calendar created.
   */
  public abstract CalendarModel build();

  protected abstract T returnBuilder();
}
