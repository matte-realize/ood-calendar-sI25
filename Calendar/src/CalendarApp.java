import controller.CalendarController;
import model.calendar.Calendar;
import model.calendar.CalendarManagement;
import view.CalendarView;

/**
 * The application used to run the Calendar Application.
 */
public class CalendarApp {
  /**
   * The main function for the calendar.
   * @param args takes in string arguments to execute commands.
   */
  public static void main(String[] args) {

    if (args.length < 2 || !args[0].equalsIgnoreCase("--mode")) {
      System.err.println("Usage: java CalendarApp --mode [interactive|headless] [commands.txt]");
      return;
    }

    if (args[1].equalsIgnoreCase("headless") && args.length != 3) {
      System.err.println("Missing file for headless mode");
      return;
    }

    CalendarManagement model = new CalendarManagement();
    CalendarView view = new CalendarView();
    CalendarController calendarController = new CalendarController(model, view);
    calendarController.play(args);
  }
}
