package model.calendar;

import java.time.ZoneId;


public class CalendarModel implements CalendarModelInterface {
  private String calendarName;
  private ZoneId timeZone;
  private Calendar calendar;

  public CalendarModel(String calendarName, ZoneId timeZone) {
    this.calendarName = calendarName;
    this.timeZone = timeZone;
  }

  @Override
  public String getName() {
    return calendarName;
  }

  @Override
  public ZoneId getTimeZone() {
    return timeZone;
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
