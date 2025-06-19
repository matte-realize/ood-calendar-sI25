package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import model.event.Event;
import model.calendar.CalendarManagement;

/**
 * The calendar view that utilizes the JFrame from Java Swing to create a
 * physical GUI that supports the user to perform actions with calendars
 * rather than being limited to only a terminal.
 */
public class JFrameCalendarView extends JFrame implements CalendarViewInterface {
  private JPanel mainPanel;
  private JPanel schedulePanel;
  private JPanel eventManagementPanel;
  private JPanel navigationPanel;
  private JPanel calendarManagementPanel;
  private JLabel statusLabel;

  private JComboBox<String> calendarSelector;
  private JButton newCalendarButton;
  private JButton editCalendarButton;
  private JTextField newCalendarNameField;

  private JTextField eventTitleField;
  private JSpinner startDateSpinner;
  private JSpinner startTimeSpinner;
  private JSpinner endDateSpinner;
  private JSpinner endTimeSpinner;
  private JButton createEventButton;
  private JButton editEventButton;
  private JButton cancelEditButton;

  private DefaultListModel<String> eventsListModel;
  private JList<String> eventsList;
  private JScrollPane eventsScrollPane;
  private JButton viewScheduleButton;

  private JSpinner dateSpinner;
  private JLabel currentDateLabel;
  private LocalDate currentViewDate;

  private CalendarManagement calendarModel;
  private CalendarView consoleView;
  private boolean isEditingMode = false;
  private Event currentEditingEvent = null;
  private List<Event> currentEvents;
  private List<String> availableCalendars = new ArrayList<>();
  private String currentSelectedCalendarName = null;

  private JCheckBox repeatCheckBox;
  private JPanel repeatOptionsPanel;
  private JCheckBox[] dayCheckboxes;
  private JRadioButton repeatForWeeksRadio;
  private JRadioButton repeatUntilDateRadio;
  private JSpinner weeksSpinner;
  private JSpinner untilDateSpinner;

  public JFrameCalendarView() {
    initializeGUI();
  }

  public void setModel(CalendarManagement calendarModel, CalendarView consoleView) {
    this.calendarModel = calendarModel;
    this.consoleView = consoleView;

    if (availableCalendars.isEmpty()) {
      createDefaultCalendar();
    }
    refreshCalendarList();
    refreshScheduleView();
  }

  private void createDefaultCalendar() {
    try {
      String defaultName = "My Calendar";
      calendarModel.createCalendar(defaultName, ZoneId.systemDefault());
      addCalendarToGUIList(defaultName);
      calendarModel.selectCalendar(defaultName);
      currentSelectedCalendarName = defaultName;
      setStatus("Created default calendar: " + defaultName);
    } catch (Exception e) {
      showError("Failed to create default calendar: " + e.getMessage());
    }
  }

  private void initializeGUI() {
    setTitle("Advanced Calendar Application");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    currentViewDate = LocalDate.now();

    createMainPanel();
    createMenuBar();

    add(mainPanel, BorderLayout.CENTER);

    setSize(1200, 800);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void createMainPanel() {
    mainPanel = new JPanel(new BorderLayout());

    createCalendarManagementPanel();
    createNavigationPanel();
    createSchedulePanel();
    createEventManagementPanel();
    createStatusPanel();

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.add(calendarManagementPanel, BorderLayout.NORTH);
    topPanel.add(navigationPanel, BorderLayout.CENTER);

    JTabbedPane tabbedPane = new JTabbedPane();

    schedulePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    eventManagementPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    tabbedPane.addTab("Schedule View", schedulePanel);
    tabbedPane.addTab("Create/Edit Events", eventManagementPanel);

    tabbedPane.setTabPlacement(JTabbedPane.TOP);
    tabbedPane.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

    tabbedPane.addChangeListener(e -> {
      int selectedIndex = tabbedPane.getSelectedIndex();
      if (selectedIndex == 0) {
        setStatus("Viewing schedule for " + formatDate(currentViewDate));
      } else if (selectedIndex == 1) {
        setStatus("Event management - Create or edit events");
      }
    });

    mainPanel.add(topPanel, BorderLayout.NORTH);
    mainPanel.add(tabbedPane, BorderLayout.CENTER);
  }

  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    JMenuItem newCalendarItem = new JMenuItem("New Calendar...");
    newCalendarItem.addActionListener(e -> showNewCalendarDialog());
    JMenuItem editCalendarItem = new JMenuItem("Edit Calendar...");
    editCalendarItem.addActionListener(e -> showEditCalendarDialog());
    JMenuItem exitItem = new JMenuItem("Exit");
    exitItem.addActionListener(e -> System.exit(0));
    fileMenu.add(newCalendarItem);
    fileMenu.add(editCalendarItem);
    fileMenu.addSeparator();
    fileMenu.add(exitItem);

    JMenu viewMenu = new JMenu("View");
    JMenuItem todayItem = new JMenuItem("Go to Today");
    todayItem.addActionListener(e -> goToToday());
    JMenuItem refreshItem = new JMenuItem("Refresh");
    refreshItem.addActionListener(e -> refreshScheduleView());
    viewMenu.add(todayItem);
    viewMenu.add(refreshItem);

    JMenu editMenu = new JMenu("Edit");
    JMenuItem editEventItem = new JMenuItem("Edit Selected Event");
    editEventItem.addActionListener(e -> editSelectedEvent());
    editMenu.add(editEventItem);

    menuBar.add(fileMenu);
    menuBar.add(viewMenu);
    menuBar.add(editMenu);

    setJMenuBar(menuBar);
  }

  private void createCalendarManagementPanel() {
    calendarManagementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    calendarManagementPanel.setBorder(new TitledBorder("Calendar Management"));

    calendarSelector = new JComboBox<>();
    calendarSelector.addActionListener(e -> onCalendarChanged());

    newCalendarNameField = new JTextField(15);
    newCalendarButton = new JButton("Create New Calendar");
    newCalendarButton.addActionListener(e -> createNewCalendar());

    editCalendarButton = new JButton("Edit Current Calendar");
    editCalendarButton.addActionListener(e -> showEditCalendarDialog());

    calendarManagementPanel.add(new JLabel("Current Calendar:"));
    calendarManagementPanel.add(calendarSelector);
    calendarManagementPanel.add(Box.createHorizontalStrut(20));
    calendarManagementPanel.add(new JLabel("New Calendar Name:"));
    calendarManagementPanel.add(newCalendarNameField);
    calendarManagementPanel.add(newCalendarButton);
    calendarManagementPanel.add(Box.createHorizontalStrut(10));
    calendarManagementPanel.add(editCalendarButton);
  }

  private void createNavigationPanel() {
    navigationPanel = new JPanel(new FlowLayout());
    navigationPanel.setBorder(new TitledBorder("Navigation"));

    dateSpinner = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
    dateSpinner.setEditor(dateEditor);
    dateSpinner.setValue(java.sql.Date.valueOf(currentViewDate));

    viewScheduleButton = new JButton("View Schedule");
    viewScheduleButton.addActionListener(new ViewScheduleActionListener());

    JButton prevDayButton = new JButton("◀ Previous Day");
    JButton nextDayButton = new JButton("Next Day ▶");

    prevDayButton.addActionListener(e -> navigateDay(-1));
    nextDayButton.addActionListener(e -> navigateDay(1));

    currentDateLabel = new JLabel("Current View: " + formatDate(currentViewDate));
    currentDateLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

    navigationPanel.add(new JLabel("Select Date:"));
    navigationPanel.add(dateSpinner);
    navigationPanel.add(viewScheduleButton);
    navigationPanel.add(Box.createHorizontalStrut(20));
    navigationPanel.add(prevDayButton);
    navigationPanel.add(nextDayButton);
    navigationPanel.add(Box.createHorizontalStrut(20));
    navigationPanel.add(currentDateLabel);
  }

  private void createSchedulePanel() {
    schedulePanel = new JPanel(new BorderLayout());
    schedulePanel.setBorder(new TitledBorder("Schedule View (Next 10 Events)"));

    eventsListModel = new DefaultListModel<>();
    eventsList = new JList<>(eventsListModel);
    eventsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    eventsList.addListSelectionListener(new EventSelectionListener());
    eventsList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

    eventsScrollPane = new JScrollPane(eventsList);
    eventsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    eventsScrollPane.setPreferredSize(new Dimension(0, 200));

    JPanel eventButtonsPanel = new JPanel(new FlowLayout());
    editEventButton = new JButton("Edit Selected Event");

    editEventButton.addActionListener(e -> editSelectedEvent());
    editEventButton.setEnabled(false);

    eventButtonsPanel.add(editEventButton);

    schedulePanel.add(eventsScrollPane, BorderLayout.CENTER);
    schedulePanel.add(eventButtonsPanel, BorderLayout.SOUTH);
  }

  private void createEventManagementPanel() {
    eventManagementPanel = new JPanel();
    eventManagementPanel.setLayout(new BoxLayout(eventManagementPanel, BoxLayout.Y_AXIS));
    eventManagementPanel.setBorder(new TitledBorder("Event Management"));

    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    titlePanel.add(new JLabel("Event Title:"));
    eventTitleField = new JTextField(30);
    titlePanel.add(eventTitleField);

    JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    startPanel.add(new JLabel("Start:"));

    startDateSpinner = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
    startDateSpinner.setEditor(startDateEditor);
    startDateSpinner.setValue(java.sql.Date.valueOf(LocalDate.now()));

    startTimeSpinner = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm");
    startTimeSpinner.setEditor(startTimeEditor);
    startTimeSpinner.setValue(new java.util.Date());

    startPanel.add(startDateSpinner);
    startPanel.add(new JLabel("at"));
    startPanel.add(startTimeSpinner);

    JPanel endPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    endPanel.add(new JLabel("End:"));

    endDateSpinner = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
    endDateSpinner.setEditor(endDateEditor);
    endDateSpinner.setValue(java.sql.Date.valueOf(LocalDate.now()));

    endTimeSpinner = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm");
    endTimeSpinner.setEditor(endTimeEditor);
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.add(java.util.Calendar.HOUR_OF_DAY, 1);
    endTimeSpinner.setValue(cal.getTime());

    endPanel.add(endDateSpinner);
    endPanel.add(new JLabel("at"));
    endPanel.add(endTimeSpinner);

    JPanel buttonPanel = new JPanel(new FlowLayout());
    createEventButton = new JButton("Create Event");
    createEventButton.addActionListener(new CreateEventActionListener());

    cancelEditButton = new JButton("Cancel Edit");
    cancelEditButton.addActionListener(e -> cancelEdit());
    cancelEditButton.setVisible(false);

    buttonPanel.add(createEventButton);
    buttonPanel.add(cancelEditButton);


    repeatCheckBox = new JCheckBox("Repeat Event");
    repeatCheckBox.addActionListener(e -> repeatOptionsPanel.setVisible(repeatCheckBox.isSelected()));
    eventManagementPanel.add(repeatCheckBox);

    repeatOptionsPanel = new JPanel();
    repeatOptionsPanel.setLayout(new BoxLayout(repeatOptionsPanel, BoxLayout.Y_AXIS));
    repeatOptionsPanel.setBorder(BorderFactory.createTitledBorder("Repeat Options"));
    repeatOptionsPanel.setVisible(false);

    String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    dayCheckboxes = new JCheckBox[days.length];
    JPanel daysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    daysPanel.add(new JLabel("Repeat on:"));
    for (int i = 0; i < days.length; i++) {
      dayCheckboxes[i] = new JCheckBox(days[i]);
      daysPanel.add(dayCheckboxes[i]);
    }
    repeatOptionsPanel.add(daysPanel);

    repeatForWeeksRadio = new JRadioButton("Repeat for");
    repeatUntilDateRadio = new JRadioButton("Repeat until");
    ButtonGroup repeatGroup = new ButtonGroup();
    repeatGroup.add(repeatForWeeksRadio);
    repeatGroup.add(repeatUntilDateRadio);

    weeksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 52, 1));
    JPanel weeksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    weeksPanel.add(repeatForWeeksRadio);
    weeksPanel.add(weeksSpinner);
    weeksPanel.add(new JLabel("times"));

    untilDateSpinner = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor untilDateEditor = new JSpinner.DateEditor(untilDateSpinner, "yyyy-MM-dd");
    untilDateSpinner.setEditor(untilDateEditor);
    untilDateSpinner.setValue(java.sql.Date.valueOf(LocalDate.now().plusWeeks(1)));


    JPanel untilDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    untilDatePanel.add(repeatUntilDateRadio);
    untilDatePanel.add(untilDateSpinner);

    repeatOptionsPanel.add(weeksPanel);
    repeatOptionsPanel.add(untilDatePanel);

    eventManagementPanel.add(titlePanel);
    eventManagementPanel.add(startPanel);
    eventManagementPanel.add(endPanel);
    eventManagementPanel.add(repeatOptionsPanel);
    eventManagementPanel.add(buttonPanel);

  }

  private void createStatusPanel() {
    statusLabel = new JLabel("Ready - Advanced Calendar Application");
    statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
    statusLabel.setPreferredSize(new Dimension(0, 25));
    mainPanel.add(statusLabel, BorderLayout.PAGE_END);
  }

  private class EventSelectionListener implements ListSelectionListener {
    @Override
    public void valueChanged(ListSelectionEvent e) {
      if (!e.getValueIsAdjusting()) {
        boolean hasSelection = eventsList.getSelectedIndex() != -1;
        editEventButton.setEnabled(hasSelection);
      }
    }
  }

  private class ViewScheduleActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
        currentViewDate = selectedDate.toInstant()
                .atZone(TimeZone.getDefault().toZoneId())
                .toLocalDate();
        refreshScheduleView();
        updateCurrentDateLabel();
      } catch (Exception ex) {
        showError("Invalid date selected: " + ex.getMessage());
      }
    }
  }

  private class CreateEventActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        String title = eventTitleField.getText().trim();
        if (title.isEmpty()) {
          showError("Event title cannot be empty");
          return;
        }

        java.util.Date startDate = (java.util.Date) startDateSpinner.getValue();
        java.util.Date startTime = (java.util.Date) startTimeSpinner.getValue();
        java.util.Date endDate = (java.util.Date) endDateSpinner.getValue();
        java.util.Date endTime = (java.util.Date) endTimeSpinner.getValue();

        LocalDateTime startDateTime = combineDateTime(startDate, startTime);
        LocalDateTime endDateTime = combineDateTime(endDate, endTime);

        if (endDateTime.isBefore(startDateTime) || endDateTime.equals(startDateTime)) {
          showError("End time must be after start time");
          return;
        }

        boolean isRepeating = repeatCheckBox.isSelected();
        StringBuilder repeatDays = new StringBuilder();
        int weeks = 0;
        java.util.Date until;
        LocalDate untilDate = null;
        if (isRepeating) {
          for (int i = 0; i < dayCheckboxes.length; i++) {
            if (dayCheckboxes[i].isSelected()) {
              switch (i + 1) {
                case 1:
                  repeatDays.append("M");
                  break;
                case 2:
                  repeatDays.append("T");
                  break;
                case 3:
                  repeatDays.append("W");
                  break;
                case 4:
                  repeatDays.append("R");
                  break;
                case 5:
                  repeatDays.append("F");
                  break;
                case 6:
                  repeatDays.append("S");
                  break;
                case 7:
                  repeatDays.append("U");
                  break;
              }
            }
          }


          if (repeatForWeeksRadio.isSelected()) {
            weeks = (Integer) weeksSpinner.getValue();
          } else if (repeatUntilDateRadio.isSelected()) {
            until = (java.util.Date) untilDateSpinner.getValue();
            untilDate = until.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
          }
        }

        if (calendarModel != null) {
          if (isEditingMode && currentEditingEvent != null) {
            String editSubjectCommandString = "";
            String editStartDateCommandString = "";
            String editEndDateCommandString = "";

            editSubjectCommandString =
                    createCommand(" events subject \"%s\" from %sT%s with %s",
                            currentEditingEvent.getSubject(),
                            currentEditingEvent.getStartDateTime(),
                            title);
            editStartDateCommandString =
                    createCommand(" events start \"%s\" from %sT%s with %s",
                            title,
                            currentEditingEvent.getStartDateTime(),
                            startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            editEndDateCommandString =
                    createCommand(" events end \"%s\" from %sT%s with %s",
                            title,
                            startDateTime,
                            endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));

            controller.EditCommand editSubjectCommand =
                    new controller.EditCommand(editSubjectCommandString,
                            calendarModel, consoleView);
            editSubjectCommand.execute();
            controller.EditCommand editStartCommand =
                    new controller.EditCommand(editStartDateCommandString,
                            calendarModel, consoleView);
            editStartCommand.execute();
            controller.EditCommand editEndCommand =
                    new controller.EditCommand(editEndDateCommandString,
                            calendarModel, consoleView);
            editEndCommand.execute();
            setStatus("Event updated successfully: " + title);
            cancelEdit();
          } else if (!isRepeating) {

            String commandString = String.format(" event \"%s\" from %sT%s to %sT%s",
                    title,
                    startDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    startDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    endDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    endDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
            );

            controller.CreateCommand createCommand = new controller.CreateCommand(commandString, calendarModel, consoleView);
            createCommand.execute();
            setStatus("Event created successfully: " + title);
            clearEventForm();
          } else {

            String commandString = "";
            if (repeatForWeeksRadio.isSelected()) {

              commandString = String.format(" event \"%s\" from %sT%s to %sT%s repeats %s for %s times",
                      title,
                      startDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                      startDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                      endDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                      endDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                      repeatDays,
                      weeks

              );
            } else {
              commandString = String.format(" event \"%s\" from %sT%s to %sT%s repeats %s until %s",
                      title,
                      startDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                      startDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                      endDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                      endDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                      repeatDays,
                      untilDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
              );
            }

            controller.CreateCommand createCommand = new controller.CreateCommand(commandString, calendarModel, consoleView);
            createCommand.execute();
            setStatus("Event created successfully: " + title);
            clearEventForm();
          }
          refreshScheduleView();
        } else {
          showError("Calendar model not initialized");
        }

      } catch (Exception ex) {
        showError("Error " + (isEditingMode ? "updating" : "creating") + " event: " + ex.getMessage());
      }
    }
  }

  private void onCalendarChanged() {
    if (calendarModel != null && calendarSelector.getSelectedItem() != null) {
      String selectedCalendar = (String) calendarSelector.getSelectedItem();
      try {
        calendarModel.selectCalendar(selectedCalendar);
        currentSelectedCalendarName = selectedCalendar;
        refreshScheduleView();
        setStatus("Switched to calendar: " + selectedCalendar);
      } catch (Exception e) {
        showError("Failed to switch calendar: " + e.getMessage());
      }
    }
  }

  private void createNewCalendar() {
    String calendarName = newCalendarNameField.getText().trim();
    if (calendarName.isEmpty()) {
      showError("Calendar name cannot be empty");
      return;
    }

    if (calendarModel != null) {
      try {
        calendarModel.createCalendar(calendarName, ZoneId.systemDefault());
        newCalendarNameField.setText("");

        addCalendarToGUIList(calendarName);

        currentSelectedCalendarName = calendarName;
        calendarModel.selectCalendar(calendarName);

        setStatus("Calendar created: " + calendarName);
      } catch (Exception e) {
        showError("Failed to create calendar: " + e.getMessage());
      }
    }
  }


  private void showNewCalendarDialog() {
    String calendarName = JOptionPane.showInputDialog(
            this,
            "Enter name for new calendar:",
            "Create New Calendar",
            JOptionPane.PLAIN_MESSAGE
    );

    if (calendarName != null && !calendarName.trim().isEmpty()) {
      newCalendarNameField.setText(calendarName.trim());
      createNewCalendar();
    }
  }

  private void showEditCalendarDialog() {
    String currentCalendar = currentSelectedCalendarName;
    if (currentCalendar == null) {
      showError("No calendar selected");
      return;
    }

    JDialog editDialog = new JDialog(this, "Edit Calendar: " + currentCalendar, true);
    editDialog.setLayout(new BorderLayout());

    JPanel formPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    formPanel.add(new JLabel("Calendar Name:"), gbc);
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JTextField nameField = new JTextField(currentCalendar, 20);
    formPanel.add(nameField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.NONE;
    formPanel.add(new JLabel("Timezone:"), gbc);
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JComboBox<String> timezoneCombo = new JComboBox<>(TimeZone.getAvailableIDs());

    ZoneId currentTimezone = calendarModel.getCalendarTimezone(currentCalendar);
    if (currentTimezone != null) {
      timezoneCombo.setSelectedItem(currentTimezone.getId());
    } else {
      timezoneCombo.setSelectedItem(TimeZone.getDefault().getID());
    }
    formPanel.add(timezoneCombo, gbc);

    JPanel buttonPanel = new JPanel(new FlowLayout());
    JButton saveButton = new JButton("Save Changes");
    JButton cancelButton = new JButton("Cancel");

    saveButton.addActionListener(e -> {
      try {
        String newName = nameField.getText().trim();
        String newTimezone = (String) timezoneCombo.getSelectedItem();

        if (newName.isEmpty()) {
          showError("Calendar name cannot be empty");
          return;
        }

        boolean nameChanged = false;
        boolean timezoneChanged = false;

        if (!newName.equals(currentCalendar)) {
          calendarModel.editCalendar(currentCalendar, "name", newName);
          removeCalendarFromGUIList(currentCalendar);
          addCalendarToGUIList(newName);
          currentSelectedCalendarName = newName;
        }

        ZoneId oldTimezone = calendarModel.getCalendarTimezone(currentSelectedCalendarName);
        if (oldTimezone == null || !oldTimezone.getId().equals(newTimezone)) {
          calendarModel.editCalendar(currentSelectedCalendarName, "timezone", newTimezone);
          timezoneChanged = true;
        }

        if (timezoneChanged) {
          refreshScheduleView();
          setStatus("Calendar updated and events refreshed for new timezone: " + currentSelectedCalendarName);
        } else if (nameChanged) {
          setStatus("Calendar updated: " + currentSelectedCalendarName);
        } else {
          setStatus("No changes made to calendar: " + currentSelectedCalendarName);
        }

        editDialog.dispose();
      } catch (Exception ex) {
        showError("Failed to update calendar: " + ex.getMessage());
      }
    });

    cancelButton.addActionListener(e -> editDialog.dispose());

    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

    editDialog.add(formPanel, BorderLayout.CENTER);
    editDialog.add(buttonPanel, BorderLayout.SOUTH);

    editDialog.pack();
    editDialog.setLocationRelativeTo(this);
    editDialog.setVisible(true);
  }

  private void editSelectedEvent() {
    int selectedIndex = eventsList.getSelectedIndex();
    if (selectedIndex == -1 || currentEvents == null || selectedIndex >= currentEvents.size()) {
      showError("No event selected");
      return;
    }

    Event event = currentEvents.get(selectedIndex);
    currentEditingEvent = event;
    isEditingMode = true;

    eventTitleField.setText(event.getSubject());

    LocalDateTime startTime = event.getStartDateTime();
    startDateSpinner.setValue(java.sql.Date.valueOf(startTime.toLocalDate()));

    java.util.Calendar startCal = java.util.Calendar.getInstance();
    startCal.set(java.util.Calendar.HOUR_OF_DAY, startTime.getHour());
    startCal.set(java.util.Calendar.MINUTE, startTime.getMinute());
    startTimeSpinner.setValue(startCal.getTime());

    LocalDateTime endTime = event.getEndDateTime();
    endDateSpinner.setValue(java.sql.Date.valueOf(endTime.toLocalDate()));

    java.util.Calendar endCal = java.util.Calendar.getInstance();
    endCal.set(java.util.Calendar.HOUR_OF_DAY, endTime.getHour());
    endCal.set(java.util.Calendar.MINUTE, endTime.getMinute());
    endTimeSpinner.setValue(endCal.getTime());

    createEventButton.setText("Update Event");
    cancelEditButton.setVisible(true);
    eventManagementPanel.setBorder(new TitledBorder("Edit Event: " + event.getSubject()));

    setStatus("Editing event: " + event.getSubject());
  }

  private void cancelEdit() {
    isEditingMode = false;
    currentEditingEvent = null;
    createEventButton.setText("Create Event");
    cancelEditButton.setVisible(false);
    eventManagementPanel.setBorder(new TitledBorder("Event Management"));
    clearEventForm();
    setStatus("Edit cancelled");
  }

  private LocalDateTime combineDateTime(java.util.Date date, java.util.Date time) {
    LocalDate localDate = date.toInstant()
            .atZone(TimeZone.getDefault().toZoneId())
            .toLocalDate();
    LocalTime localTime = time.toInstant()
            .atZone(TimeZone.getDefault().toZoneId())
            .toLocalTime();
    return LocalDateTime.of(localDate, localTime);
  }

  private void navigateDay(int days) {
    currentViewDate = currentViewDate.plusDays(days);
    dateSpinner.setValue(java.sql.Date.valueOf(currentViewDate));
    refreshScheduleView();
    updateCurrentDateLabel();
  }

  private void goToToday() {
    currentViewDate = LocalDate.now();
    dateSpinner.setValue(java.sql.Date.valueOf(currentViewDate));
    refreshScheduleView();
    updateCurrentDateLabel();
  }

  private void refreshScheduleView() {
    if (calendarModel != null) {
      try {
        String commandString = " events on " + currentViewDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        CalendarView captureView = new CalendarView() {
          @Override
          public void printEvents(List<Event> events, String day) {
            currentEvents = events;
            SwingUtilities.invokeLater(() -> {
              eventsListModel.clear();

              ZoneId tz = calendarModel.getCalendarTimezone(currentSelectedCalendarName);
              DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

              if (events.isEmpty()) {
                eventsListModel.addElement("No events found for " + day);
              } else {
                int count = 0;
                for (Event event : events) {
                  if (count >= 10) break;
                  ZonedDateTime startZ = event.getStartDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(tz);
                  ZonedDateTime endZ = event.getEndDateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(tz);

                  String display = String.format("\"%s\" from %s to %s",
                          event.getSubject(),
                          startZ.format(fmt),
                          endZ.format(fmt));

                  eventsListModel.addElement(display);
                  count++;
                }
              }
            });
          }

          @Override
          public void printStatus(String status, String day) {
            SwingUtilities.invokeLater(() -> {
              setStatus(status + " on " + day);
            });
          }

          @Override
          public void printError(String message) {
            SwingUtilities.invokeLater(() -> {
              showError(message);
            });
          }
        };

        controller.QueryCommand printCommand = new controller.QueryCommand(commandString, "print", calendarModel, captureView);
        printCommand.execute();
      } catch (Exception e) {
        showError("Failed to refresh schedule: " + e.getMessage());
      }
    } else {
      eventsListModel.clear();
      eventsListModel.addElement("Calendar model not initialized");
    }
  }

  private void refreshCalendarList() {
    if (calendarModel != null) {
      try {
        calendarSelector.removeAllItems();

        for (String calendarName : availableCalendars) {
          calendarSelector.addItem(calendarName);
        }

        if (currentSelectedCalendarName != null && availableCalendars.contains(currentSelectedCalendarName)) {
          calendarSelector.setSelectedItem(currentSelectedCalendarName);
        } else if (!availableCalendars.isEmpty()) {
          calendarSelector.setSelectedIndex(0);
          currentSelectedCalendarName = availableCalendars.get(0);
        }

        setStatus("Calendar list refreshed - " + availableCalendars.size() + " calendars available");

      } catch (Exception e) {
        showError("Failed to refresh calendar list: " + e.getMessage());
      }
    } else {
      calendarSelector.removeAllItems();
      setStatus("No calendar model available");
    }
  }

  private void addCalendarToGUIList(String calendarName) {
    if (!availableCalendars.contains(calendarName)) {
      availableCalendars.add(calendarName);
      refreshCalendarList();
    }
  }

  private void removeCalendarFromGUIList(String calendarName) {
    if (availableCalendars.remove(calendarName)) {
      if (calendarName.equals(currentSelectedCalendarName)) {
        currentSelectedCalendarName = availableCalendars.isEmpty() ? null : availableCalendars.get(0);
      }
      refreshCalendarList();
    }
  }

  private void updateCurrentDateLabel() {
    currentDateLabel.setText("Current View: " + formatDate(currentViewDate));
  }

  private String formatDate(LocalDate date) {
    return date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
  }

  private void clearEventForm() {
    eventTitleField.setText("");
    startDateSpinner.setValue(java.sql.Date.valueOf(LocalDate.now()));
    endDateSpinner.setValue(java.sql.Date.valueOf(LocalDate.now()));

    java.util.Calendar cal = java.util.Calendar.getInstance();
    startTimeSpinner.setValue(cal.getTime());
    cal.add(java.util.Calendar.HOUR_OF_DAY, 1);
    endTimeSpinner.setValue(cal.getTime());
  }

  private void setStatus(String message) {
    statusLabel.setText(message);
  }

  private void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    setStatus("Error: " + message);
  }

  @Override
  public void printEvents(List<Event> events, String day) {
    currentEvents = events;
    SwingUtilities.invokeLater(() -> {
      eventsListModel.clear();

      if (events.isEmpty()) {
        eventsListModel.addElement("No events found for " + day);
        setStatus("No events found for " + day);
      } else {
        int count = 0;
        for (Event event : events) {
          if (count >= 10) break;
          eventsListModel.addElement(event.printEvent());
          count++;
        }
        setStatus("Showing " + Math.min(events.size(), 10) + " events for " + day);
      }
    });
  }

  @Override
  public void printStatus(String status, String day) {
    SwingUtilities.invokeLater(() -> {
      updateStatus(status + " on " + day, false);
    });
  }

  @Override
  public void printError(String message) {
    SwingUtilities.invokeLater(() -> {
      updateStatus("Error: " + message, true);
    });
  }

  private void updateStatus(String message, boolean isError) {
    statusLabel.setText(message);
    if (isError) {
      JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private String createCommand(String format, String title, LocalDateTime startDate, String newValue) {
    return String.format(format,
            title,
            startDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            startDate.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
            newValue);
  }
}
