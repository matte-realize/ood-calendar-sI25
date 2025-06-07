## Instructions 

The program runs through Intellij using the CalendarApp class. After the
prompt "Type commands to view/edit calendar (type 'exit' to quit):" comes
up, users will be able to create events using the follow prompts:

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

To use query commands, the following commands are valid:

- print events on (XXXX-XX-XX)
- print events from (XXXX-XX-XX)T(XX:XX) to (XXXX-XX-XX)T(XX:XX)
- show status on (XXXX-XX-XX)T(XX:XX)

To exit the program, the input is "exit."

## Features

The creation and query features for the calendar works. The edit feature does not work
as it has not been integrated within the project yet, however, it has been proven possible
through the tests.

## Work Distribution: 

Paul - Created the Controller/Command Parsing and started the View

Matthew - Created the Model and Tests for the Calendar
