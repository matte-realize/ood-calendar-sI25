package model.calendar;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class CalendarManagement {
  private final Map<String, CalendarModel> calendarModels;
  private Calendar selectedCalendar;

  public CalendarManagement() {
    this.calendarModels = new HashMap<>();
    this.selectedCalendar = null;
  }

  public Calendar getSelectedCalendar() {
    return selectedCalendar;
  }

  public void createCalendar(String calendarName, ZoneId timeZone) {

    for (CalendarModel c : this.calendarModels.values()) {
      if (c.getName().equals(calendarName)) {
        throw new IllegalArgumentException("There already exists a calendar with the name " + calendarName);
      }
    }

    CalendarModel newCalendar = new CalendarModel.CustomCalendarBuilder()
            .setName(calendarName)
            .setTimeZone(timeZone)
            .setCalendar(new Calendar())
            .build();

    calendarModels.put(calendarName, newCalendar);
  }

  public void selectCalendar() {

  }

  public void editCalendar() {

  }
}


// handle exceptions + name + ensure subjects != subject
// syntax