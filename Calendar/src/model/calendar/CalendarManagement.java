package model.calendar;

import java.util.HashMap;
import java.util.Map;

public class CalendarManagement {
  private final Map<String, CalendarModel> calendarModels;
  private final Map<String, Calendar> calendarInstances;
  private String selectedCalendar;

  public CalendarManagement() {
    this.calendarModels = new HashMap<>();
    this.calendarInstances = new HashMap<>();
    this.selectedCalendar = null;
  }

  public void createCalendar() {

  }

  public void selectCalendar() {

  }

  public void editCalendar() {

  }
}

// handle exceptions + name + ensure subjects != subject
// syntax