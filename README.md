# Java Traffic Simulation
Developers: Calum Thow, Claudia Garcia Mendoza, Hamish Hartley, Mario Fuente Rodriguez.

When the program is first launched, the user is presented with the interface. This tab serves as the primary interface 
for data input and reading.

Within the interface, the user can view a table containing all of the vehicles in the intersection. To facilitate easier 
data exploration, the table can be sorted by clicking on each of the column headers.

Additionally, the user can review a list of phases and their corresponding durations, as well as statistics for each 
intersection segment. However, for the initial stage of development (Stage 1), the CO2 index and Air quality index are not 
calculated and will read as 0.

To add a new vehicle, the user can use the "New" button. Alternatively, the user can modify phases or remove vehicles using 
the "Edit" buttons. These buttons will trigger pop-up windows.

When using the add, edit and remove, the user needs to make sure to press Enter after typing the data in the text field. 
This will ensure the correct functioning of the program.

The Advanced Features tab is used to view information about the intersection in real-time in a graphical format. The tab 
shows a live graph of CO2 in the air and a visual representation of the junction showing vehicles and phases. As these 
features require the Stage 2 simulation functionality, they currently contain placeholder images.

The Export Settings tab is used to generate the report. The user can use the provided checkboxes to select the data they 
would like included in the report. Once the user is happy with their selection they can click  Generate report file  to 
output the data into a file. At this point the Exit button will be ungreyed out indicating that the user can exit the program 
if they wish. 

In order for the user to be able to see the saved files in Eclipse IDE, it will need to refresh the project explorer. This 
will make a file, inside the directory folder called  statistics_files , appear. The name of this file will contain the 
timestamp in which it was generated, in the case the user exports the data several times across the simulation.


