# PeachyBbies
# pachyBbies
[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)
A tablet-based, internal use only time tracking application for our internal product packing process.
The goal is to track an employee’s time while packing our products and while pausing for various reasons.   Here’s how we want it to work. 
## Features
- A user (manager) with a special PIN number or password.  
- There should be a toggle button that allows the manager user to select tracking mode on/off.  
- If tracking mode is OFF, the manager user is prompted to enter the following information. Also, this mode means that the timer is a STOPWATCH (counting up): 
>Packer name b.	Product SKU
- If tracking mode is ON, the manager user is prompted to enter the following information. Also, this mode means that the timer is a COUNTDOWN.  
>Packer Name 
>Product SKU 
>Time allocated for the packing process 
>Quota  
- Another user (packer) would enter in their name and then tap “START” as they begin their packing process. This is essentially a stopwatch that tracks the time they are working.  
>This will also note the time the user tapped start.   
- If a user (packer) needs to stop working, they can tap “PAUSE,” pausing the timer. When they pause, they will be required to select a reason for the pause: 
>Assisting in another task 
>Break
>Bathroom 
>Shelving Product 
>The app will then need to track the time spent on this break period.  
- When the packer user returns, they tap “START” again to move back into the main work time tracking mode.
- When completed, the packer user taps “END,” which then stops the timer. It also: 
>Notes the time when the user taps “END” 
>Prompts user to enter in the COUNT of units packed.
- At the end of each day, the system will compile the tracked data for each person using the app (there will be between 8 and 15 users on individual tablets at any given time) into a simple .CSV file that shows: 
>Packer Name 
>Start Time 
>End Time 
>Total Actual Working Time 
>Target Working Time 
>Total Break Time for each pause reason
>Target number of products to be packed 
>Actual number of products packed 
>SKU of product packed (there will only be 1 at a time)
- This .CSV file will be stored in a Google Drive folder OR emailed to an admin user (TBD)  
- The manager user can then enter in their PIN/password and reset the tablet for use the next day. 
