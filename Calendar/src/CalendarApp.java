import Controller.CalendarController;
import Model.Calendar;
import View.CalendarView;

/**
 * The application used to run the Calendar Application.
 */
public class CalendarApp {
  public static void main(String[] args) {

    if (args.length < 2 || !args[0].equalsIgnoreCase("--mode")) {
      System.err.println("Usage: java CalendarApp --mode [interactive|headless] [commands.txt]");
      return;
    }

    if (args[1].equalsIgnoreCase("headless") && args.length != 3) {
      System.err.println("Missing file for headless mode");
      return;
    }

    Calendar model = new Calendar();
    CalendarView view = new CalendarView();
    CalendarController calendarController = new CalendarController(model, view);
    calendarController.go(args);
  }
}
