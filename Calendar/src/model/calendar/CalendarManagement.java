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

  public void selectCalendar(String calendarName) {
    CalendarModel calendarModel = calendarModels.get(calendarName);

    if (calendarModel == null) {
      throw new IllegalArgumentException("Calendar '" + calendarName + "' does not exist.");
    }

    this.selectedCalendar = calendarModel.getCalendar();
  }

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


// handle exceptions + name + ensure subjects != subject
// syntax