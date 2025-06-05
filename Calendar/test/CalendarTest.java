import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import Model.Calendar;
import Model.Event;
import Model.EventInterface;
import Model.EventProperty;
import Model.Location;
import Model.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CalendarTest {
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
  public void testEditEventReturnsCorrectArguments() {
    EventInterface allDayEvent = calendar.createEvent(
            "Wedding",
            LocalDateTime.of(2025, 6, 10, 0, 0),
            null,
            null,
            null,
            null
    );

    EventInterface allDayEventUpdate = new Event.CustomEventBuilder()
            .setSubject("Wedding")
            .setStartDateTime(LocalDateTime.of(2025, 6, 11, 0, 0))
            .build();

    calendar.editEvent(
            "Wedding",
            LocalDateTime.of(2025, 6, 10, 0, 0),
            allDayEventUpdate
    );

    String expectedWeddingStartDateTime = LocalDateTime.of(2025, 6, 11, 0, 0).toString();

    assertNotNull(allDayEvent);
    assertEquals("Wedding", allDayEventUpdate.getSubject());
    assertEquals(expectedWeddingStartDateTime, allDayEventUpdate.getStartDateTime().toString());
  }
}
