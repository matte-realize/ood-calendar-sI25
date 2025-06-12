package model.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.event.EventInterface;

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

  public ZoneId getCalendarTimezone(String calendarName) {

    for (CalendarModel calendarModel : calendarModels.values()) {
      if (calendarName == null && this.selectedCalendar.equals(calendarModel.getCalendar())) {
        return calendarModel.getTimeZone();
      } else if (calendarModel.getName().equals(calendarName)) {
        return calendarModel.getTimeZone();
      }
    }
    return null;
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


  /**
   * A method that selects the calendar, given the string of the name of the calendar
   * that is to be selected.
   *
   * @param calendarName the name of the calendar to be selected.
   */
  public void selectCalendar(String calendarName) throws IllegalArgumentException {

    boolean found = false;

    for (CalendarModel c : this.calendarModels.values()) {
      if (c.getName().equals(calendarName)) {
        this.selectedCalendar = c.getCalendar();
        found = true;
        break;
      }
    }

    if (!found) {
      throw new IllegalArgumentException("There does not exist a calendar with the name " + calendarName);
    }
  }

  /**
   * A method that utilizes the builder method to create a new calendar that will change the
   * calendar with the name being given.
   *
   * @param calendarName        the name of the calendar to be edited.
   * @param property the new calendar name the calendar will change into.
   * @param newValue       the new zone id the calendar will change into.
   */
  public void editCalendar(String calendarName, String property, String newValue) throws IllegalArgumentException {

    CalendarModel calenderToUpdate = null;
    boolean found = false;
    CalendarModel newCalendar;

    switch (property) {
      case "name":
        for (CalendarModel c : this.calendarModels.values()) {
          if (c.getName().equals(newValue)) {
            throw new IllegalArgumentException("There already exists a calendar with the name " + newValue);
          } else if (c.getName().equals(calendarName)) {
            found = true;
            calenderToUpdate = c;
          }
        }

        if (!found) {
          throw new IllegalArgumentException("There does not exist a calendar with the name " + calendarName);
        }

        newCalendar = new CalendarModel.CustomCalendarBuilder()
                .setName(newValue)
                .setTimeZone(calenderToUpdate.getTimeZone())
                .setCalendar(calenderToUpdate.getCalendar())
                .build();

        calendarModels.remove(calendarName);
        calendarModels.put(newValue, newCalendar);
        break;
      case "timezone":

        for (CalendarModel c : this.calendarModels.values()) {
          if (c.getName().equals(calendarName)) {
            found = true;
            calenderToUpdate = c;
          }

          if (!found) {
            throw new IllegalArgumentException("There does not exist a calendar with the name " + calendarName);
          }

          calendarModels.remove(calendarName);

          newCalendar = new CalendarModel.CustomCalendarBuilder()
                  .setName(calendarName)
                  .setTimeZone(ZoneId.of(newValue))
                  .setCalendar(calenderToUpdate.getCalendar())
                  .build();

          calendarModels.put(calendarName, newCalendar);
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid property " + property);
    }

  }

  public void copyEvent(EventInterface event, String targetCalendar, LocalDateTime targetDate) {

    for (CalendarModel c : this.calendarModels.values()) {

      if (c.getName().equals(targetCalendar)) {
        c.getCalendar().createEvent(event.getSubject(),
                targetDate,
                null,
                event.getDescription(),
                event.getLocation(),
                event.getStatus());
      }
    }
  }

  public void copyEvents(List<EventInterface> events, String targetCalendar) {

    for (CalendarModel c : this.calendarModels.values()) {

      if (c.getName().equals(targetCalendar)) {
        for (EventInterface e : events) {
          c.getCalendar().createEvent(e.getSubject(),
                  e.getStartDateTime(),
                  e.getEndDateTime(),
                  e.getDescription(),
                  e.getLocation(),
                  e.getStatus());
        }
      }
    }

  }
}


// handle exceptions + name + ensure subjects != subject
// syntax