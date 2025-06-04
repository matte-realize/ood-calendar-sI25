//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Controller;

import Model.CalendarModel;
import View.CalendarView;
import java.util.Scanner;

public class CalendarController {
  private CalendarModel calendarModel;
  private CalendarView calendarView;

  public CalendarController(CalendarModel calendarModel, CalendarView calendarView) {
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
    do {
      System.out.print("> ");
      command = scanner.nextLine().trim();
    } while(!this.processCommand(command));

  }

  public void runHeadlessMode(String filename) throws IllegalArgumentException {
  }

  private boolean processCommand(String command) throws IllegalArgumentException {
    if (command.equals("exit")) {
      return true;
    }
    return false;
  }
}
