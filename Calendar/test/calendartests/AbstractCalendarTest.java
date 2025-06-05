package calendartests;

import org.junit.Before;

import java.time.LocalDateTime;

import Model.Calendar;
import Model.EventInterface;
import Model.Location;
import Model.Status;

/**
 * An abstract JUnit test class designed to set up and test all calendar
 * functionalities that implement the Calendar interface.
 */
public abstract class AbstractCalendarTest {
  protected Calendar calendar;

  @Before
  public void setup() {
    calendar = new Calendar();

    EventInterface allDayEvent = calendar.createEvent(
            "Wedding",
            LocalDateTime.of(2025, 6, 10, 0, 0),
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
