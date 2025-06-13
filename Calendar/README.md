## Instructions 

The program runs through Intellij using the CalendarApp class. After the
prompt "Type commands to view/edit calendar (type 'exit' to quit):" comes
up, users will be able to create calendars using the following prompt:

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

## Features



## Work Distribution: 

Paul - Created the controller/Command Parsing and started the view

Matthew - Created the model and tests for the Calendar
