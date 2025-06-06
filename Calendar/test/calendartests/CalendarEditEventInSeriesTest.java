package calendartests;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import Model.EditMode;
import Model.Event;
import Model.EventInterface;
import Model.EventSeries;
import Model.Location;
import Model.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test that tests the edit methods of events in a series
 * within the Calendar class.
 */
public class CalendarEditEventInSeriesTest extends AbstractCalendarTest {

  @Test
  public void testEditSingleEventInSeries() {
    // Create a series that runs Monday and Thursday from June 2-16, 2025
    EventSeries multiDaySeries = calendar.createEventSeries(
            "Workout",
            LocalDateTime.of(2025, 6, 2, 7, 0),  // Monday
            LocalDateTime.of(2025, 6, 16, 9, 0), // Monday (end boundary)
            List.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY),
            0,
            "Weekly workouts",
            Location.PHYSICAL,
            Status.PUBLIC
    );

    // Debug: Check if series was created and has instances
    assertNotNull("Series should be created", multiDaySeries);
    assertNotNull("Series should have instances", multiDaySeries.getInstances());
    assertTrue("Series should have at least 2 instances", multiDaySeries.getInstances().size() >= 2);

    // Get the second instance (should be Thursday June 5th)
    Event target = multiDaySeries.getInstances().get(1);
    String originalSubject = target.getSubject();
    LocalDateTime targetStart = target.getStartDateTime();
    LocalDateTime targetEnd = target.getEndDateTime();

    // Create the edit with new subject but same times
    EventInterface edit = new Event.CustomEventBuilder()
            .setSubject("Modified Workout")
            .setStartDateTime(targetStart)
            .setEndDateTime(targetEnd)
            .setDescription("Weekly workouts") // Keep original description
            .setLocation(Location.PHYSICAL)    // Keep original location
            .setStatus(Status.PUBLIC)          // Keep original status
            .build();

    // Edit only the single event
    calendar.editEvent("Workout", targetStart, edit, EditMode.SINGLE);

    // Verify the target event was updated
    EventInterface updated = calendar.getEvent("Modified Workout", targetStart, targetEnd);
    assertNotNull("Updated event should exist", updated);
    assertEquals("Modified Workout", updated.getSubject());

    // Verify original event no longer exists at that time
    EventInterface original = calendar.getEvent("Workout", targetStart, targetEnd);
    assertNull("Original event should no longer exist at the modified time", original);

    // Verify other instances in the series still have original subject
    for (Event e : multiDaySeries.getInstances()) {
      if (!e.getStartDateTime().equals(targetStart)) {
        // These should still exist with original subject
        EventInterface unchanged = calendar.getEvent(originalSubject,
                e.getStartDateTime(),
                e.getEndDateTime());
        assertNotNull("Other instances should still exist", unchanged);
        assertEquals(originalSubject, unchanged.getSubject());
      }
    }
  }

  @Test
  public void testEditEventAndFutureInSeries() {
    // Create a series that runs every Tuesday from June 3-24, 2025
    EventSeries series = calendar.createEventSeries(
            "Team Meeting",
            LocalDateTime.of(2025, 6, 3, 10, 0),  // Tuesday June 3
            LocalDateTime.of(2025, 6, 24, 11, 0), // Tuesday June 24 (end boundary)
            List.of(DayOfWeek.TUESDAY),
            0,
            "Weekly team sync",
            Location.ONLINE,
            Status.PUBLIC
    );

    // Debug: Check series creation
    assertNotNull("Series should be created", series);
    assertNotNull("Series should have instances", series.getInstances());
    assertTrue("Series should have at least 2 instances", series.getInstances().size() >= 2);

    // Target the second instance (June 10) to edit it and all future events
    Event target = series.getInstances().get(1); // June 10
    LocalDateTime targetStart = target.getStartDateTime();
    LocalDateTime targetEnd = target.getEndDateTime();

    // Create edit to change subject and location
    EventInterface edit = new Event.CustomEventBuilder()
            .setSubject("Strategic Planning")
            .setStartDateTime(targetStart)
            .setEndDateTime(targetEnd)
            .setDescription("Weekly team sync")  // Keep original description
            .setLocation(Location.PHYSICAL)      // Change to physical
            .setStatus(Status.PUBLIC)            // Keep original status
            .build();

    // Edit this event and all future events
    calendar.editEvent("Team Meeting", targetStart, edit, EditMode.FUTURE);

    // Verify the first instance (June 3) still has original properties
    Event firstInstance = series.getInstances().get(0);
    EventInterface firstUnchanged = calendar.getEvent("Team Meeting",
            firstInstance.getStartDateTime(),
            firstInstance.getEndDateTime());
    assertNotNull("First instance should still exist with original subject", firstUnchanged);
    assertEquals("Team Meeting", firstUnchanged.getSubject());
    assertEquals(Location.ONLINE, firstUnchanged.getLocation());

    // Verify target instance and future instances were updated
    for (int i = 1; i < series.getInstances().size(); i++) {
      Event instance = series.getInstances().get(i);
      EventInterface updated = calendar.getEvent("Strategic Planning",
              instance.getStartDateTime(),
              instance.getEndDateTime());
      assertNotNull("Future instances should be updated", updated);
      assertEquals("Strategic Planning", updated.getSubject());
      assertEquals(Location.PHYSICAL, updated.getLocation());

      // Verify original no longer exists
      EventInterface original = calendar.getEvent("Team Meeting",
              instance.getStartDateTime(),
              instance.getEndDateTime());
      assertNull("Original future events should no longer exist", original);
    }
  }

  @Test
  public void testEditAllEventsInSeries() {
    LocalDateTime start = LocalDateTime.of(2025, 6, 5, 16, 0);  // Thursday
    LocalDateTime end = LocalDateTime.of(2025, 6, 26, 20, 0);   // Thursday (end boundary)

    // Create series running every Thursday from June 5-26
    EventSeries series = calendar.createEventSeries(
            "Therapy",
            start,
            end,
            List.of(DayOfWeek.THURSDAY),
            0,
            "Session with Dr.Shine",
            Location.ONLINE,
            Status.PRIVATE
    );

    // Debug: Check series creation
    assertNotNull("Series should be created", series);
    assertNotNull("Series should have instances", series.getInstances());
    assertTrue("Series should have at least 1 instance", series.getInstances().size() >= 1);

    // Print debug info
    System.out.println("Series has " + series.getInstances().size() + " instances:");
    for (int i = 0; i < series.getInstances().size(); i++) {
      Event instance = series.getInstances().get(i);
      System.out.println("Instance " + i + ": " + instance.getStartDateTime() + " - " + instance.getEndDateTime());
    }

    // Create edit to change subject
    EventInterface edit = new Event.CustomEventBuilder()
            .setSubject("Therapy Session")
            .setStartDateTime(series.getStartDateTime()) // Keep original times
            .setEndDateTime(LocalDateTime.of(2025, 6, 5, 20, 0)) // Keep original end time
            .setDescription("Session with Dr.Shine")     // Keep original description
            .setLocation(Location.ONLINE)                // Keep original location
            .setStatus(Status.PRIVATE)                   // Keep original status
            .build();

    // Edit all events in the series
    calendar.editEvent("Therapy", series.getStartDateTime(), edit, EditMode.ALL);

    // Verify all instances were updated
    for (Event e : series.getInstances()) {
      EventInterface updated = calendar.getEvent("Therapy Session",
              e.getStartDateTime(),
              e.getEndDateTime());
      assertNotNull("All instances should be updated", updated);
      assertEquals("Therapy Session", updated.getSubject());
      assertEquals("Session with Dr.Shine", updated.getDescription());
      assertEquals(Location.ONLINE, updated.getLocation());
      assertEquals(Status.PRIVATE, updated.getStatus());

      // Verify original events no longer exist
      EventInterface original = calendar.getEvent("Therapy",
              e.getStartDateTime(),
              e.getEndDateTime());
      assertNull("Original events should no longer exist", original);
    }

    // Test the specific instances mentioned in the original test
    assertNotNull("First instance should exist",
            calendar.getEvent("Therapy Session",
                    start,
                    LocalDateTime.of(2025, 6, 5, 20, 0)));

    assertNotNull("Second instance should exist",
            calendar.getEvent("Therapy Session",
                    LocalDateTime.of(2025, 6, 12, 16, 0),
                    LocalDateTime.of(2025, 6, 12, 20, 0)));

    assertNotNull("Third instance should exist",
            calendar.getEvent("Therapy Session",
                    LocalDateTime.of(2025, 6, 19, 16, 0),
                    LocalDateTime.of(2025, 6, 19, 20, 0)));

    assertNotNull("Fourth instance should exist",
            calendar.getEvent("Therapy Session",
                    LocalDateTime.of(2025, 6, 26, 16, 0),
                    end));
  }
}