package calendartests;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import Model.Calendar;
import Model.Event;
import Model.EventInterface;
import Model.EventSeries;
import Model.Location;
import Model.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test that tests the creation methods within the Calendar class.
 */
public class CalendarCreationTest {
  private Calendar calendar;

  @Before
  public void setup() {
    calendar = new Calendar();
  }

  @Test
  public void testCreateEventReturnsCorrectArguments() {
    EventInterface allDayEvent = calendar.createEvent(
            "Wedding",
            LocalDateTime.of(2025, 6, 10, 0, 0),
            null,
            null,
            null,
            null
    );

    String expectedWeddingStartDateTime = LocalDateTime.of(2025, 6, 10, 0, 0).toString();

    assertNotNull(allDayEvent);
    assertEquals("Wedding", allDayEvent.getSubject());
    assertEquals(expectedWeddingStartDateTime, allDayEvent.getStartDateTime().toString());

    EventInterface detailedEvent = calendar.createEvent(
      "Meeting",
            LocalDateTime.of(2025, 7, 14, 9, 0),
            LocalDateTime.of(2025, 7, 14, 10, 0),
            "Discussion about ethics.",
            Location.ONLINE,
            Status.PRIVATE
    );

    String expectedDetailedStartDateTime = LocalDateTime.of(2025, 7, 14, 9, 0).toString();
    String expectedDetailedEndDateTime = LocalDateTime.of(2025, 7, 14, 10, 0).toString();

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
    LocalDateTime end = LocalDateTime.of(2025, 8, 22, 22, 0);
    List<DayOfWeek> repeatDays = List.of(DayOfWeek.FRIDAY);
    EventSeries series = calendar.createEventSeries(
            subject,
            start,
            end,
            repeatDays,
            "Varsity practice",
            Location.PHYSICAL,
            Status.PUBLIC
    );

    assertEquals(subject, series.getSubject());
    assertEquals(4, series.getInstances().size());

    Set<DayOfWeek> validDays = Set.of(DayOfWeek.FRIDAY);
    for (Event instance : series.getInstances()) {
      assertTrue(validDays.contains(instance.getStartDateTime().getDayOfWeek()));
      assertEquals(subject, instance.getSubject());
    }
  }

  @Test
  public void testCreateWeeklySeriesOnMultipleDays() {
    String subject = "Team Meeting";
    LocalDateTime start = LocalDateTime.of(2025, 6, 2, 10, 0);
    LocalDateTime end = LocalDateTime.of(2025, 6, 16, 10, 0);
    List<DayOfWeek> repeatDays = List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
    EventSeries series = calendar.createEventSeries(
            subject,
            start,
            end,
            repeatDays,
            "Meeting with team",
            Location.ONLINE,
            Status.PRIVATE
    );

    assertEquals(subject, series.getSubject());
    assertEquals(5, series.getInstances().size());

    Set<DayOfWeek> validDays = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
    for (Event instance : series.getInstances()) {
      assertTrue(validDays.contains(instance.getStartDateTime().getDayOfWeek()));
      assertEquals(subject, instance.getSubject());
    }
  }
}
