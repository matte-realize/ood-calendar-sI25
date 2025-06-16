import javax.swing.*;
import java.time.ZoneId;
import java.util.List;

import model.calendar.CalendarManagement;
import model.event.Event;
import view.CalendarView;
import view.JFrameCalendarView;

public class CalendarGUIApp {
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      System.out.println("Could not set system look and feel: " + e.getMessage());
    }

    SwingUtilities.invokeLater(() -> {
      try {
        CalendarManagement calendarModel = new CalendarManagement();

        CalendarView consoleView = new CalendarView() {
          @Override
          public void printEvents(List<Event> events, String day) {
            System.out.println("Events for " + day + ":");
            for (Event event : events) {
              System.out.println("  " + event.printEvent());
            }
          }

          @Override
          public void printStatus(String status, String day) {
            System.out.println("Status for " + day + ": " + status);
          }

          @Override
          public void printError(String message) {
            System.err.println("Error: " + message);
          }
        };

        JFrameCalendarView gui = new JFrameCalendarView();

        gui.setModel(calendarModel, consoleView);

        try {
          calendarModel.createCalendar("Default Calendar", ZoneId.systemDefault());
          gui.addInitialCalendar("Default Calendar");
          
          calendarModel.selectCalendar("Default Calendar");

        } catch (Exception e) {
          System.out.println("Could not create default calendar: " + e.getMessage());
        }

        System.out.println("Calendar Application Started Successfully!");

      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(
                null,
                "Failed to start application: " + e.getMessage(),
                "Startup Error",
                JOptionPane.ERROR_MESSAGE
        );
        System.exit(1);
      }
    });
  }
}