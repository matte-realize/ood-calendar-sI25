package controller;

import controller.eventCommands.CreateEventCommand;
import controller.eventCommands.EditEventCommand;
import controller.eventCommands.QueryEventCommand;
import model.calendar.Calendar;
import model.calendar.CalendarManagement;
import view.CalendarView;

import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Controller class that implements the CalendarControllerInterface and
 * acts as the controller for the Calendar in order to be able to process
 * commands as well as determine between the interactive and headless
 * mode to run commands.
 */
public class CalendarController implements CalendarControllerInterface {
  private final CalendarManagement calendarModel;
  private final CalendarView calendarView;

  public CalendarController(CalendarManagement calendarModel, CalendarView calendarView) {
    this.calendarModel = calendarModel;
    this.calendarView = calendarView;
  }

  @Override
  public void play(String[] args) {
    switch (args[1].toLowerCase()) {
      case "interactive":
        this.runInteractiveMode();
        break;
      case "headless":
        this.runHeadlessMode(args[2]);
        break;
      default:
        System.err.println("Invalid mode. Use 'interactive' or 'headless'.");
    }
  }

  private void runInteractiveMode() throws IllegalArgumentException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Type commands to view/edit calendar (type 'exit' to quit):");

    String command;
    String tokensString;
    do {
      System.out.print("> ");
      command = scanner.next();
      tokensString = scanner.nextLine();
    }
    while (!this.processCommand(command, tokensString));
  }

  private void runHeadlessMode(String filename) throws IllegalArgumentException {
    try (Scanner scanner = new Scanner(new FileReader(filename))) {
      String command;
      String tokensString;
      boolean exitFound = false;

      while (!exitFound) {
        try {
          command = scanner.next();
          if (command.equalsIgnoreCase("exit")) {
            System.out.println("Exiting...");
            exitFound = true;
            break;
          } else {
            tokensString = scanner.nextLine();
            processCommand(command, tokensString);
          }
        } catch (NoSuchElementException e) {
          System.err.println("Error: headless command file must end with an 'exit' command.");
          break;
        }
      }
    } catch (IOException e) {
      System.err.println("Failed to read command file: " + e.getMessage());
    }
  }

  private boolean processCommand(String command, String tokensString) throws IllegalArgumentException {
    switch (command) {
      case "create":
        CreateEventCommand createEvent =
                new CreateEventCommand(tokensString, calendarModel);
        createEvent.execute();
        break;
      case "edit":
        EditEventCommand editEvent =
                new EditEventCommand(tokensString, calendarModel);
        editEvent.execute();
        break;
      case "print":
        QueryEventCommand printEvent =
                new QueryEventCommand(tokensString, "print", calendarModel, calendarView);
        printEvent.execute();
        break;
      case "show":
        QueryEventCommand showStatus =
                new QueryEventCommand(tokensString, "show", calendarModel, calendarView);
        showStatus.execute();
        break;
      case "copy":

      case "exit":
        return true;
      default:
        throw new IllegalArgumentException("Invalid command");
    }

    return false;
  }
}