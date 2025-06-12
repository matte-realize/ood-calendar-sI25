package model.calendar;

import java.time.ZoneId;

/**
 * An interface that describes the model for a calendar that is able to
 * represent the basic details for the individual calendars, seperated
 * distinctly from each other.
 */
public interface CalendarModelInterface {
  /**
   * Gets the name of the calendar.
   *
   * @return a string based on the name given to the calendar.
   */
  String getName();

  /**
   * Gets the zone id for the calendar.
   *
   * @return a zone id based on the given time zone established to the
   *         calendar.
   */
  ZoneId getTimeZone();

  /**
   * Gets the calendar for the current instance.
   *
   * @return a calendar based on the instance given.
   */
  Calendar getCalendar();
}
