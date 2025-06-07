package calendarModelTests;

import org.junit.Before;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import model.Calendar;
import model.EventInterface;
import model.Location;
import model.Status;

/**
 * An abstract JUnit test class designed to set up and test all calendar
 * functionalities that implement the Calendar interface.
 */
public abstract class AbstractCalendarTest {
  protected Calendar calendar;
  protected LocalDateTime sampleStart;
  protected LocalDateTime sampleEnd;
  protected List<DayOfWeek> sampleRepeatDay;
  protected List<DayOfWeek> sampleRepeatDays;
  protected LocalDateTime allDayTimeStart;

  @Before
  public void setup() {
    calendar = new Calendar();
    sampleStart = LocalDateTime.of(2024, 12, 15, 10, 0);
    sampleEnd = LocalDateTime.of(2024, 12, 15, 11, 0);
    sampleRepeatDay = List.of(DayOfWeek.MONDAY);
    sampleRepeatDays = List.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY);
    allDayTimeStart = LocalDateTime.of(2025, 6, 10, 8, 0);

    EventInterface allDayEvent = calendar.createEvent(
            "Wedding",
            allDayTimeStart,
            null,
            null,
            null,
            null
    );

    EventInterface detailedEvent = calendar.createEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            LocalDateTime.of(2025, 7, 14, 10, 0),
            "Discussion about ethics.",
            Location.ONLINE,
            Status.PRIVATE
    );
  }
}
