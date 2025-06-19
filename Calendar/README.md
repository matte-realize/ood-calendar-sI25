## Instructions 

The program runs through Intellij using the CalendarApp class.
It can also be run using the jar file by double-clicking it (without command line arguments)
or through the terminal. After the prompt "Type commands to view/edit calendar
(type 'exit' to quit):" comes up, users will be able to create calendars using the following prompt:

- create calendar --name (name of the calendar) --timezone (valid time zone format)

*Valid time zone formats are written in IANA Time Zone database format (eg. America/Chicago).*

After creating a calendar, users can use the following prompt during any state of the program
to access a different calendar.

- use calendar --name (name of the calendar)

Calendars can also be edited using the following prompt.

- edit calendar --name (name of calendar) --property (name/timezone) "(edited prompt)"

After entering a calendar, users can create events or a series of events using the following prompts:

- create event "(name of the event)" from (XXXX-XX-XX)
- create event "(name of the event)" from (XXXX-XX-XX)T(XX:XX) to (XXXX-XX-XX)T(XX:XX)
- create event "(name of the event)" from (XXXX-XX-XX) repeats (D) for (R) times
- create event "(name of the event)" from (XXXX-XX-XX) repeats (D) until (XXXX-XX-XX)
- create event "(name of the event)" from (XXXX-XX-XX)T(XX) to (XXXX-XX-XX)T(XX:XX) repeats (D) for (R) times
- create event "(name of the event)" from (XXXX-XX-XX)T(XX) to (XXXX-XX-XX)T(XX:XX) repeats (D) until (XXXX-XX-XX)

*Parentheses are not necessary*

*X is represented by a number within the (YEAR-MONTH-DAY)T(HOUR:MINUTE) format*

*D represents the one of the characters that is used to represent the day of the week 
with MTWRFSU being the only valid characters (can be used consecutively, eg. WR)*

*R represents any number of times for repetition*

After events are created, users are able to edit events, independently, inclusive with the future, or all within a series, 
using the following prompts:

- edit event (choice selection) "(name of the event)" from (XXXX-XX-XX)T(XX:XX) to (XXXX-XX-XX)T(XX:XX) with (edited prompt)
- edit events (choice selection) "(name of the event)" from (XXXX-XX-XX)T(XX:XX) with (edited prompt)
- edit series (choice selection) "(name of the event)" from (XXXX-XX-XX)T(XX:XX) with (edited prompt)

*Choice selection range from subject, start, end, description, location, status*

*Ensure the name and time stamp matches exactly as the one that is wanted to be edited.*

*Edited prompt must also follow the correct guideline for edit (eg. editing a timezone still must be in (XXXX-XX-XX)T(XX:XX) format, 
status can only switch between public and private)*

Users are also able to copy events using the following prompts:

- copy event "(name of the event)" on (XXXX-XX-XX)T(XX:XX) --target (name of the event) to (XXXX-XX-XX)T(XX:XX)
- copy events on (XXXX-XX-XX)T(XX:XX) --target (name of the event) to (XXXX-XX-XX)T(XX:XX)
- copy events between (XXXX-XX-XX)T(XX:XX) and (XXXX-XX-XX)T(XX:XX) --target (name of the event) to (XXXX-XX-XX)T(XX:XX)

To use query commands, the following commands are valid:

- print events on (XXXX-XX-XX)
- print events from (XXXX-XX-XX)T(XX:XX) to (XXXX-XX-XX)T(XX:XX)
- show status on (XXXX-XX-XX)T(XX:XX)

To exit the program, the input is "exit."

The User can also run the program through the GUI by not specifying a run mode. Here they can work
with the default calendar or create a new one to work with. They can create single events or series and also
view all the events they have on each day. They can also edit events as well as the calendar itself.

## Features

The calendar is able to create and edit a calendar before being able to access the calendar to individually create event
or event series. These events or event series can be edited or copied to make other events that were modified and then be
able to be displayed on screen. 

## Work Distribution: 

Paul - Created the controller/Command Parsing, GUI, and started the view

Matthew - Created the model, GUI, and tests for the Calendar

## Changes:

- Added the GUI to allow for working with a calendar
- User can add single and series events
- They can also edit single events and series events, however editing the times for a series doesn't currently work
- One change to the existing design was the controller, all we added was for it to be able to handle running
  - the program in the GUI mode. It was implemented by adding a single method.

## Additional:

Invalid commands within our commandwithinvaildcommands.txt are to be followed in order to prompt invalid commands or prompts. Some such reasoning are:
- the command was not a valid command due to invalid syntax
- attempts to make duplicate events or series
- attempting to create an event before using a calendar
- editing with an invalid property
- editing to nonexistent events
- copying nonexistent events
- creating events with the same start and end time
