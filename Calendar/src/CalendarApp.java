//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import Controller.CalendarController;
import Model.CalendarModel;
import View.CalendarView;

public class CalendarApp {
  public static void main(String[] args) {
    CalendarModel model = new CalendarModel();
    CalendarView view = new CalendarView();
    CalendarController calendarController = new CalendarController(model, view);
    calendarController.go(args);
  }
}
