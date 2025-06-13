package calendarcontrollertests.eventtesting;

import org.junit.Test;

import java.time.LocalDateTime;

import controller.CreateCommand;
import controller.EditCommand;
import model.event.EventInterface;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test that tests for the controller being able to edit events.
 */
public class CalendarControllerEditEventTest extends AbstractControllerEventTest {
  @Test
  public void testEditSingleEvent() {
    LocalDateTime start = LocalDateTime.of(2025, 8, 10, 9, 0);
    LocalDateTime end = LocalDateTime.of(2025, 8, 10, 10, 0);

    String createEvent = " event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00";
    CreateCommand createCommand = new CreateCommand(createEvent,
            calendarManagement, calendarView);
    createCommand.execute();

    EventInterface event = calendarManagement.getSelectedCalendar()
            .getEvent("Event One", start, end);

    assertEquals("Event One", event.getSubject());

    String editEvent = " event subject \"Event One\" from 2025-08-10T09:00 to "
            + "2025-08-10T10:00 with Edited Event One";
    EditCommand editCommand = new EditCommand(editEvent,
            calendarManagement, calendarView);
    editCommand.execute();

    EventInterface editedEvent = calendarManagement.getSelectedCalendar()
            .getEvent("Edited Event One", start, end);

    assertEquals("Edited Event One", editedEvent.getSubject());
  }

  @Test
  public void testEditFuture() {
    LocalDateTime start1 = LocalDateTime.of(2025, 6, 9, 7, 0);
    LocalDateTime end1 = LocalDateTime.of(2025, 6, 9, 8, 0);

    LocalDateTime start2 = LocalDateTime.of(2025, 6, 11, 7, 0);
    LocalDateTime end2 = LocalDateTime.of(2025, 6, 11, 8, 0);

    LocalDateTime start3 = LocalDateTime.of(2025, 6, 16, 7, 0);
    LocalDateTime end3 = LocalDateTime.of(2025, 6, 16, 8, 0);

    LocalDateTime start4 = LocalDateTime.of(2025, 6, 18, 7, 0);
    LocalDateTime end4 = LocalDateTime.of(2025, 6, 18, 8, 0);

    LocalDateTime start5 = LocalDateTime.of(2025, 6, 23, 7, 0);
    LocalDateTime end5 = LocalDateTime.of(2025, 6, 23, 8, 0);

    LocalDateTime start6 = LocalDateTime.of(2025, 6, 25, 7, 0);
    LocalDateTime end6 = LocalDateTime.of(2025, 6, 25, 8, 0);

    String createEventSeries = " event \"Event Five\" from 2025-06-09T07:00 to 2025-06-09T08:00 "
            + "repeats MW for 6 times";
    CreateCommand createCommand = new CreateCommand(createEventSeries,
            calendarManagement, calendarView);
    createCommand.execute();

    EventInterface event = calendarManagement.getSelectedCalendar()
            .getEvent("Event Five", start1, end1);

    assertEquals("Event Five", event.getSubject());
    assertEquals("2025-06-09T07:00", event.getStartDateTime().toString());
    assertEquals("2025-06-09T08:00", event.getEndDateTime().toString());

    String editEventSeriesAll = " events subject \"Event Five\" from 2025-06-16T07:00 with edit";
    EditCommand editCommand = new EditCommand(editEventSeriesAll,
            calendarManagement, calendarView);
    editCommand.execute();

    EventInterface eventSeriesEvent1 = calendarManagement.getSelectedCalendar()
            .getEvent("Event Five", start1, end1);
    EventInterface eventSeriesEvent2 = calendarManagement.getSelectedCalendar()
            .getEvent("Event Five", start2, end2);
    EventInterface eventSeriesEvent3 = calendarManagement.getSelectedCalendar()
            .getEvent("edit", start3, end3);
    EventInterface eventSeriesEvent4 = calendarManagement.getSelectedCalendar()
            .getEvent("edit", start4, end4);
    EventInterface eventSeriesEvent5 = calendarManagement.getSelectedCalendar()
            .getEvent("edit", start5, end5);
    EventInterface eventSeriesEvent6 = calendarManagement.getSelectedCalendar()
            .getEvent("edit", start6, end6);

    assertEquals("Event Five", eventSeriesEvent1.getSubject());
    assertEquals("2025-06-09T07:00", eventSeriesEvent1.getStartDateTime().toString());
    assertEquals("2025-06-09T08:00", eventSeriesEvent1.getEndDateTime().toString());

    assertEquals("Event Five", eventSeriesEvent2.getSubject());
    assertEquals("2025-06-11T07:00", eventSeriesEvent2.getStartDateTime().toString());
    assertEquals("2025-06-11T08:00", eventSeriesEvent2.getEndDateTime().toString());

    assertEquals("edit", eventSeriesEvent3.getSubject());
    assertEquals("2025-06-16T07:00", eventSeriesEvent3.getStartDateTime().toString());
    assertEquals("2025-06-16T08:00", eventSeriesEvent3.getEndDateTime().toString());

    assertEquals("edit", eventSeriesEvent4.getSubject());
    assertEquals("2025-06-18T07:00", eventSeriesEvent4.getStartDateTime().toString());
    assertEquals("2025-06-18T08:00", eventSeriesEvent4.getEndDateTime().toString());

    assertEquals("edit", eventSeriesEvent5.getSubject());
    assertEquals("2025-06-23T07:00", eventSeriesEvent5.getStartDateTime().toString());
    assertEquals("2025-06-23T08:00", eventSeriesEvent5.getEndDateTime().toString());

    assertEquals("edit", eventSeriesEvent6.getSubject());
    assertEquals("2025-06-25T07:00", eventSeriesEvent6.getStartDateTime().toString());
    assertEquals("2025-06-25T08:00", eventSeriesEvent6.getEndDateTime().toString());
  }

  @Test
  public void testEditAll() {
    LocalDateTime start1 = LocalDateTime.of(2025, 6, 9, 7, 0);
    LocalDateTime end1 = LocalDateTime.of(2025, 6, 9, 8, 0);

    LocalDateTime start2 = LocalDateTime.of(2025, 6, 11, 7, 0);
    LocalDateTime end2 = LocalDateTime.of(2025, 6, 11, 8, 0);

    LocalDateTime start3 = LocalDateTime.of(2025, 6, 16, 7, 0);
    LocalDateTime end3 = LocalDateTime.of(2025, 6, 16, 8, 0);

    LocalDateTime start4 = LocalDateTime.of(2025, 6, 18, 7, 0);
    LocalDateTime end4 = LocalDateTime.of(2025, 6, 18, 8, 0);

    LocalDateTime start5 = LocalDateTime.of(2025, 6, 23, 7, 0);
    LocalDateTime end5 = LocalDateTime.of(2025, 6, 23, 8, 0);

    LocalDateTime start6 = LocalDateTime.of(2025, 6, 25, 7, 0);
    LocalDateTime end6 = LocalDateTime.of(2025, 6, 25, 8, 0);

    String createEventSeries = " event \"Event Five\" from 2025-06-09T07:00 to 2025-06-09T08:00 "
            + "repeats MW for 6 times";
    CreateCommand createCommand = new CreateCommand(createEventSeries,
            calendarManagement, calendarView);
    createCommand.execute();

    EventInterface event = calendarManagement.getSelectedCalendar()
            .getEvent("Event Five", start1, end1);

    assertEquals("Event Five", event.getSubject());
    assertEquals("2025-06-09T07:00", event.getStartDateTime().toString());
    assertEquals("2025-06-09T08:00", event.getEndDateTime().toString());

    String editEventSeriesIncludingAndFuture = " series subject \"Event Five\" "
            + "from 2025-06-09T07:00 with edit";
    EditCommand editCommand = new EditCommand(editEventSeriesIncludingAndFuture,
            calendarManagement, calendarView);
    editCommand.execute();

    EventInterface eventSeriesEvent1 = calendarManagement.getSelectedCalendar()
            .getEvent("edit", start1, end1);
    EventInterface eventSeriesEvent2 = calendarManagement.getSelectedCalendar()
            .getEvent("edit", start2, end2);
    EventInterface eventSeriesEvent3 = calendarManagement.getSelectedCalendar()
            .getEvent("edit", start3, end3);
    EventInterface eventSeriesEvent4 = calendarManagement.getSelectedCalendar()
            .getEvent("edit", start4, end4);
    EventInterface eventSeriesEvent5 = calendarManagement.getSelectedCalendar()
            .getEvent("edit", start5, end5);
    EventInterface eventSeriesEvent6 = calendarManagement.getSelectedCalendar()
            .getEvent("edit", start6, end6);

    assertEquals("edit", eventSeriesEvent1.getSubject());
    assertEquals("2025-06-09T07:00", eventSeriesEvent1.getStartDateTime().toString());
    assertEquals("2025-06-09T08:00", eventSeriesEvent1.getEndDateTime().toString());

    assertEquals("edit", eventSeriesEvent2.getSubject());
    assertEquals("2025-06-11T07:00", eventSeriesEvent2.getStartDateTime().toString());
    assertEquals("2025-06-11T08:00", eventSeriesEvent2.getEndDateTime().toString());

    assertEquals("edit", eventSeriesEvent3.getSubject());
    assertEquals("2025-06-16T07:00", eventSeriesEvent3.getStartDateTime().toString());
    assertEquals("2025-06-16T08:00", eventSeriesEvent3.getEndDateTime().toString());

    assertEquals("edit", eventSeriesEvent4.getSubject());
    assertEquals("2025-06-18T07:00", eventSeriesEvent4.getStartDateTime().toString());
    assertEquals("2025-06-18T08:00", eventSeriesEvent4.getEndDateTime().toString());

    assertEquals("edit", eventSeriesEvent5.getSubject());
    assertEquals("2025-06-23T07:00", eventSeriesEvent5.getStartDateTime().toString());
    assertEquals("2025-06-23T08:00", eventSeriesEvent5.getEndDateTime().toString());

    assertEquals("edit", eventSeriesEvent6.getSubject());
    assertEquals("2025-06-25T07:00", eventSeriesEvent6.getStartDateTime().toString());
    assertEquals("2025-06-25T08:00", eventSeriesEvent6.getEndDateTime().toString());
  }
}
