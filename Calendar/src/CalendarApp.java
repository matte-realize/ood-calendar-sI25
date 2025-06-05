//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import Controller.CalendarController;
import Model.Calendar;
import View.CalendarView;

public class CalendarApp {
  public static void main(String[] args) {
    Calendar model = new Calendar();
    CalendarView view = new CalendarView();
    CalendarController calendarController = new CalendarController(model, view);
    calendarController.go(args);
  }
}
