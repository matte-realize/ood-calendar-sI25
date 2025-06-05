package calendartests;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import Model.Calendar;
import Model.EditMode;
import Model.Event;
import Model.EventInterface;
import Model.EventSeries;
import Model.Location;
import Model.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
  public void testCreateWeeklySeriesWithOccurrences() {
    String subject = "Team Meeting";
    LocalDateTime start = LocalDateTime.of(2025, 6, 2, 10, 0);
    List<DayOfWeek> repeatDays = List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
    int occurrences = 4;
    EventSeries series = calendar.createEventSeries(
            subject,
            start,
            null,
            repeatDays,
            occurrences,
            null,
            Location.ONLINE,
            Status.PRIVATE
    );

    assertEquals(subject, series.getSubject());
    assertEquals(4, series.getInstances().size());

    Set<DayOfWeek> validDays = Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
    for (Event instance : series.getInstances()) {
      assertTrue(validDays.contains(instance.getStartDateTime().getDayOfWeek()));
      assertEquals(subject, instance.getSubject());
    }
  }
}
