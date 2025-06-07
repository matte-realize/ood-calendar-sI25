package controller;

/**
 * This interface acts as the basis for a controller with the
 * go function as the entry point for the calendar.
 */
public interface CalendarControllerInterface {
  /**
   * Starts the calendar application with the provided
   * string arguments.
   *
   * @param args the command line arguments passed to
   *             the application.
   */
  void play(String[] args);
}
