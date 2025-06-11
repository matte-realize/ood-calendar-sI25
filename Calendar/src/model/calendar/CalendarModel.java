package model.calendar;

import java.time.ZoneId;

/**
 * A class of a calendar model that represents calendar model interface
 *
 */
public class CalendarModel implements CalendarModelInterface {
  private String name;
  private ZoneId timeZone;
  private Calendar calendar;

  public CalendarModel(String name, ZoneId timeZone) {
    this.name = name;
    this.timeZone = timeZone;
    this.calendar = calendar;
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
  public Calendar getCalendar() {
    return calendar;
  }

  /**
   * A class representing a builder for creating events that extends
   * the event builder.
   */
  public static class CustomCalendarBuilder extends CalendarBuilder<CustomCalendarBuilder> {
    /**
     * Performs the build command to create an event.
     *
     * @return an event interface based on the details of the event.
     */
    public CalendarModel build() {
      CalendarModel calendar = new CalendarModel(this.calendarName, this.timezone);
      calendar.calendar = this.calendar;

      return calendar;
    }

    protected CustomCalendarBuilder returnBuilder() {
      return this;
    }
  }
}
