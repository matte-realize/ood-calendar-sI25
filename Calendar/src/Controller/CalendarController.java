//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Controller;

import Model.Calendar;
import View.CalendarView;

import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CalendarController implements CalendarControllerInterface{
  private final Calendar calendarModel;
  private final CalendarView calendarView;

  public CalendarController(Calendar calendarModel, CalendarView calendarView) {
    this.calendarModel = calendarModel;
    this.calendarView = calendarView;
  }

  public void go(String[] args) {
    switch (args[1].toLowerCase()) {
      case "interactive":
        this.runInteractiveMode();
        break;
      case "headless":
        this.runHeadlessMode(args[2]);
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
    } while(!this.processCommand(command, tokensString));

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

          CreateEventCommand createEvent = new CreateEventCommand(tokensString, calendarModel);
          createEvent.execute();

        break;
      case "edit":

        EditEventCommand editEvent = new EditEventCommand(tokensString, calendarModel);
        editEvent.execute();

        break;
      case "print":

        QueryEventCommand printEvent = new QueryEventCommand(tokensString, "print", calendarModel, calendarView);
        printEvent.execute();

          break;
      case "show":

        QueryEventCommand showStatus = new QueryEventCommand(tokensString, "show", calendarModel, calendarView);
        showStatus.execute();
        break;
      case "exit":
        return true;
      default:
        throw new IllegalArgumentException("Invalid command");
    }

    return false;
  }
}
