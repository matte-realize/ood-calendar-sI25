//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Controller;

import Model.Calendar;
import View.CalendarView;

import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

public class CalendarController implements CalendarControllerInterface{
  private Calendar calendarModel;
  private CalendarView calendarView;

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

  public void runInteractiveMode() throws IllegalArgumentException {
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

  public void runHeadlessMode(String filename) throws IllegalArgumentException {
  }

  private boolean processCommand(String command, String tokensString) throws IllegalArgumentException {

    String[] tokens;

    switch (command) {
      case "create":
        tokens = tokensString.split("\"");

        if (tokens.length != 3) {
          throw new IllegalArgumentException("Create event must contain a subject enclosed by \" and no other \"");
        }

        if (tokens[0].equals(" event ")) {
          CreateEventCommand createEvent = new CreateEventCommand(tokens[2].stripLeading(), tokens[1]);
          createEvent.execute();
        } else {
          throw new IllegalArgumentException("create command must specify \"event\" followed by the subject");
        }

        break;
      case "edit":

        /*if (tokens[1].equals("event")) {

        } else if (tokens[1].equals("events")) {

        } else if (tokens[1].equals("series")) {

        } else {
          throw new IllegalArgumentException("Invalid command");
        }*/
      case "print":
          break;
      case "show":
        break;
      case "exit":
        return true;
      default:
        throw new IllegalArgumentException("Invalid command");
    }

    return false;
  }
}
