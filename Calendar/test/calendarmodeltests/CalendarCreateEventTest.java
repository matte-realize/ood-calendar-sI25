package calendarmodeltests;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Set;

import model.calendar.Calendar;
import model.event.Event;
import model.event.EventInterface;
import model.event.EventSeries;
import model.enums.Location;
import model.enums.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test that tests the creation methods within the Calendar class.
 */
public class CalendarCreateEventTest {
  private Calendar calendar;

  @Before
  public void setup() {
    calendar = new Calendar();
  }

  @Test
  public void testCreateEventReturnsCorrectArguments() {
    // overwrites the time since end time determines all day
    EventInterface allDayEvent = calendar.createEvent(
            "Wedding",
            LocalDateTime.of(2025, 6, 10, 0, 0),
            null,
            null,
            null,
            null
    );

    String expectedWeddingDateStartTime = LocalDateTime.of(2025, 6, 10,
            8, 0).toString();
    String expectedWeddingDateEndTime = LocalDateTime.of(2025, 6, 10,
            17, 0).toString();

    assertNotNull(allDayEvent);
    assertEquals("Wedding", allDayEvent.getSubject());
    assertEquals(expectedWeddingDateStartTime, allDayEvent.getStartDateTime().toString());
    assertEquals(expectedWeddingDateEndTime, allDayEvent.getEndDateTime().toString());

    EventInterface detailedEvent = calendar.createEvent(
            "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            LocalDateTime.of(2025, 7, 14, 10, 0),
            "Discussion about ethics.",
            Location.ONLINE,
            Status.PRIVATE
    );

    String expectedDetailedStartDateTime = LocalDateTime.of(2025, 7, 14,
            9, 0).toString();
    String expectedDetailedEndDateTime = LocalDateTime.of(2025, 7, 14,
            10, 0).toString();

    assertNotNull(detailedEvent);
    assertEquals("Wedding", allDayEvent.getSubject());
    assertEquals(expectedDetailedStartDateTime, detailedEvent.getStartDateTime().toString());
    assertEquals(expectedDetailedEndDateTime, detailedEvent.getEndDateTime().toString());
    assertEquals("Discussion about ethics.", detailedEvent.getDescription());
    assertEquals(Location.ONLINE, detailedEvent.getLocation());
    assertEquals(Status.PRIVATE, detailedEvent.getStatus());
  }

  @Test
  public void testCreateWeeklySeriesOnOneDay() {
    String subject = "Volleyball Practice";
    LocalDateTime start = LocalDateTime.of(2025, 8, 1, 22, 0);
    LocalDateTime end = LocalDateTime.of(2025, 8, 22, 23, 0);
    List<DayOfWeek> repeatDays = List.of(DayOfWeek.FRIDAY);
    EventSeries series = calendar.createEventSeries(
            subject,
            start,
            end,
            repeatDays,
            0,
            "Varsity practice",
            Location.PHYSICAL,
            Status.PUBLIC
    );

    Set<DayOfWeek> validDays = Set.of(DayOfWeek.FRIDAY);
    for (Event instance : series.getInstances()) {
      assertTrue(validDays.contains(instance.getStartDateTime().getDayOfWeek()));
      assertTrue(validDays.contains(instance.getEndDateTime().getDayOfWeek()));

      assertEquals(subject, instance.getSubject());
      assertEquals(
              instance.getStartDateTime().toLocalDate(),
              instance.getEndDateTime().toLocalDate()
      );
    }
  }

  @Test
  public void testCreateWeeklySeriesOnMultipleDaysWithWeekEnd() {
    String subject = "Team Meeting";
    LocalDateTime start = LocalDateTime.of(2025, 6, 2, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 12, 0);
    List<DayOfWeek> repeatDays = List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
    EventSeries series = calendar.createEventSeries(
            subject,
            start,
            end,
            repeatDays,
            0,
            "Meeting with team",
            Location.ONLINE,
            Status.PRIVATE
    );

    Set<DayOfWeek> validDays = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
    for (Event instance : series.getInstances()) {
      assertTrue(validDays.contains(instance.getStartDateTime().getDayOfWeek()));
      assertTrue(validDays.contains(instance.getEndDateTime().getDayOfWeek()));

      assertEquals(subject, instance.getSubject());
      assertEquals(
              instance.getStartDateTime().toLocalDate(),
              instance.getEndDateTime().toLocalDate()
      );
    }
  }

  @Test
  public void testCreateSeriesBasedOnInstances() {
    String subject = "Park Volunteer";
    LocalDateTime start = LocalDateTime.of(2025, 4, 1, 8, 0);
    List<DayOfWeek> repeatDays = List.of(DayOfWeek.TUESDAY);
    EventSeries series = calendar.createEventSeries(
            subject,
            start,
            null,
            repeatDays,
            10,
            "Volunteering at Fenway Park",
            Location.PHYSICAL,
            Status.PUBLIC
    );

    assertEquals(subject, series.getSubject());
    assertEquals(10, series.getInstances().size());

    Set<DayOfWeek> validDays = Set.of(DayOfWeek.TUESDAY);
    for (Event instance : series.getInstances()) {
      assertTrue(validDays.contains(instance.getStartDateTime().getDayOfWeek()));
      assertTrue(validDays.contains(instance.getEndDateTime().getDayOfWeek()));
      assertEquals(subject, instance.getSubject());

      assertEquals(
              instance.getStartDateTime().toLocalDate(),
              instance.getEndDateTime().toLocalDate()
      );
    }
  }
}
