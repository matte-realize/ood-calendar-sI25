package calendarmodeltests;

import org.junit.Test;

import java.time.LocalDateTime;

import model.enums.Location;
import model.enums.Status;

import static org.junit.Assert.assertThrows;

/**
 * JUnit test for testing exceptions when creating events independently
 * or within a series.
 */
public class CalendarExceptionCreationTest extends AbstractCalendarTest {
  @Test
  public void testNullSubject() {
    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              null,
              sampleStart,
              sampleEnd,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              null,
              sampleStart,
              sampleEnd,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });
  }

  @Test
  public void testEmptySubject() {
    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              "",
              sampleStart,
              sampleEnd,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              "",
              sampleStart,
              sampleEnd,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });
  }

  @Test
  public void testNullStart() {
    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              "Workout",
              null,
              sampleEnd,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              "Workout",
              null,
              sampleEnd,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });
  }

  @Test
  public void testEndIsBeforeStart() {
    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              "Workout",
              sampleEnd,
              sampleStart,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              "Workout",
              sampleEnd,
              sampleStart,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });
  }

  @Test
  public void testSameTime() {
    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              "Workout",
              sampleStart,
              sampleStart,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              "Workout",
              sampleEnd,
              sampleEnd,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              "Workout",
              sampleStart,
              sampleStart,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              "Workout",
              sampleEnd,
              sampleEnd,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });
  }

  @Test
  public void testNullSubjectSeries() {
    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              null,
              sampleStart,
              sampleEnd,
              sampleRepeatDay,
              0,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              null,
              sampleStart,
              sampleEnd,
              sampleRepeatDays,
              0,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              null,
              sampleStart,
              sampleEnd,
              sampleRepeatDay,
              0,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              null,
              sampleStart,
              sampleEnd,
              sampleRepeatDays,
              0,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });
  }

  @Test
  public void testEmptySubjectSeries() {
    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "",
              sampleStart,
              sampleEnd,
              sampleRepeatDay,
              0,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "",
              sampleStart,
              sampleEnd,
              sampleRepeatDays,
              0,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "",
              sampleStart,
              sampleEnd,
              sampleRepeatDay,
              0,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "",
              sampleStart,
              sampleEnd,
              sampleRepeatDays,
              0,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });
  }

  @Test
  public void testNullStartSeries() {
    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              null,
              sampleEnd,
              sampleRepeatDay,
              0,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              null,
              sampleEnd,
              sampleRepeatDays,
              0,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              null,
              sampleEnd,
              sampleRepeatDay,
              0,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              null,
              sampleEnd,
              sampleRepeatDays,
              0,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });
  }

  @Test
  public void testEndIsBeforeStartSeries() {
    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              sampleEnd,
              sampleStart,
              sampleRepeatDay,
              0,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              sampleEnd,
              sampleStart,
              sampleRepeatDays,
              0,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              sampleEnd,
              sampleStart,
              sampleRepeatDay,
              0,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              sampleEnd,
              sampleStart,
              sampleRepeatDays,
              0,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });
  }


  @Test
  public void testSameTimeSeries() {
    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              sampleStart,
              sampleStart,
              sampleRepeatDay,
              0,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              sampleEnd,
              sampleEnd,
              sampleRepeatDay,
              0,
              null,
              null,
              null
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              sampleStart,
              sampleStart,
              sampleRepeatDay,
              0,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });

    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              sampleEnd,
              sampleEnd,
              sampleRepeatDay,
              0,
              "Fitness",
              Location.PHYSICAL,
              Status.PRIVATE
      );
    });
  }

  @Test
  public void testNullDaysInSeries() {
    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEventSeries(
              "Workout",
              sampleStart,
              sampleEnd,
              null,
              0,
              null,
              null,
              null
      );
    });
  }

  @Test
  public void testSameIdentifiers() {
    // event was already created within abstract class for setup
    assertThrows(IllegalArgumentException.class, () -> {
      calendar.createEvent(
              "Wedding",
              LocalDateTime.of(2025, 6, 10, 0, 0),
              null,
              null,
              null,
              null
      );
    });
  }
}
