package model.calendar;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that represents calendar management that will be able to perform
 * operations based on a collection of calendars. The management allows for
 * creation, selection, and editing of calendars.
 */
public class CalendarManagement {
  private final Map<String, CalendarModel> calendarModels;
  private Calendar selectedCalendar;

  /**
   * A constructor that uses an empty map that store calendar models and sets
   * the currently selected calendar to null, ready for selection.
   */
  public CalendarManagement() {
    this.calendarModels = new HashMap<>();
    this.selectedCalendar = null;
  }

  /**
   * A getter for the selected calendar.
   *
   * @return a calendar based on the calendar being selected.
   */
  public Calendar getSelectedCalendar() {
    return selectedCalendar;
  }

  /**
   * A method allowing for the creation of a calendar based on the name of the
   * calendar and the time zone that represents the zone id of the calendar.
   *
   * @param calendarName the name for the respective calendar.
   * @param timeZone     the time zone for the respective calendar.
   */
  public void createCalendar(String calendarName, ZoneId timeZone) {
    for (CalendarModel c : this.calendarModels.values()) {
      if (c.getName().equals(calendarName)) {
        throw new IllegalArgumentException("Calendar already exists with that name!");
      }
    }

    CalendarModel newCalendar = new CalendarModel.CustomCalendarBuilder()
            .setName(calendarName)
            .setTimeZone(timeZone)
            .setCalendar(new Calendar())
            .build();

    calendarModels.put(calendarName, newCalendar);
  }

  /**
   * A method that selects the calendar, given the string of the name of the calendar
   * that is to be selected.
   *
   * @param calendarName the name of the calendar to be selected.
   */
  public void selectCalendar(String calendarName) {
    CalendarModel calendarModel = calendarModels.get(calendarName);

    if (calendarModel == null) {
      throw new IllegalArgumentException("Calendar '" + calendarName + "' does not exist.");
    }

    this.selectedCalendar = calendarModel.getCalendar();
  }

  /**
   * A method that utilizes the builder method to create a new calendar that will change the
   * calendar with the name being given.
   *
   * @param calendarName        the name of the calendar to be edited.
   * @param updatedCalendarName the new calendar name the calendar will change into.
   * @param updatedZoneId       the new zone id the calendar will cahnge into.
   */
  public void editCalendar(String calendarName,
                           String updatedCalendarName,
                           ZoneId updatedZoneId) {
    CalendarModel calendarToEdit = calendarModels.get(calendarName);

    CalendarModel.CustomCalendarBuilder builder = new CalendarModel.CustomCalendarBuilder()
            .setCalendar(calendarToEdit.getCalendar());

    builder.setName(updatedCalendarName);
    builder.setTimeZone(updatedZoneId);

    CalendarModel updatedCalendar = builder.build();

    calendarModels.remove(calendarName);
    calendarModels.put(updatedCalendarName, updatedCalendar);
  }
}


// handle exceptions + name
// syntax