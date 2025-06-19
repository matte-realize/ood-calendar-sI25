package calendarcontrollertests;

import org.junit.Before;
import org.junit.Test;

import controller.CalendarController;
import model.calendar.CalendarManagement;
import view.CalendarView;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A JUnit test that tests for the operations performed within the controller.
 */
public class CalendarControllerTest {
  private CalendarController controller;

  @Before
  public void setup() {
    CalendarManagement management;
    CalendarView view;
    management = new CalendarManagement();
    view = new CalendarView();
    controller = new CalendarController(management, view);
  }

  @Test
  public void testCorrectParsing() {
    try {
      Path tempFile = Files.createTempFile("valid_script", ".txt");
      try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(tempFile))) {
        writer.println("create calendar --name EventCalendar --timezone America/New_York");
        writer.println("use calendar --name EventCalendar");
        writer.println("create event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00");
        writer.println("exit");
      }

      String[] args = {"program", "headless", tempFile.toString()};

      try {
        controller.play(args);
        // Should continue
      } catch (Exception e) {
        fail("Valid script should parse without exceptions: " + e.getMessage());
      }

      Files.deleteIfExists(tempFile);
    } catch (IOException e) {
      fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  public void testIncorrectParsing() {
    try {
      Path tempFile = Files.createTempFile("valid_script", ".txt");
      try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(tempFile))) {
        writer.println("create TestEvent 2024-01-15 10:00 2024-01-15 11:00");
        writer.println("show");
        writer.println("exit");
      }

      String[] args = {"program", "headless", tempFile.toString()};

      try {
        controller.play(args);
        // Should continue
      } catch (Exception e) {
        fail("Valid script should parse without exceptions: " + e.getMessage());
      }

      Files.deleteIfExists(tempFile);
    } catch (IOException e) {
      fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  public void testModelInteraction() {
    try {
      Path tempFile = Files.createTempFile("model_test", ".txt");
      try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(tempFile))) {
        writer.println("create calendar --name test --timezone Europe/Paris");
        writer.println("use calendar --name test");
        writer.println("create event \"Event One\" from 2025-08-10T09:00 to 2025-08-10T10:00");
        writer.println("exit");
      }

      String[] args = {"program", "headless", tempFile.toString()};

      try {
        controller.play(args);
        // Should continue
      } catch (Exception e) {
        fail("Valid script should parse without exceptions: " + e.getMessage());
      }

      Files.deleteIfExists(tempFile);
    } catch (IOException e) {
      fail("Test setup failed: " + e.getMessage());
    }
  }

  @Test
  public void testGUIStart() {
    try {
      String[] args = {"program", "gui"};
      controller.play(args);
    } catch (Exception e) {
      fail("GUI mode should start without exceptions: " + e.getMessage());
    }
  }
}
