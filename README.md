# pentair-pool-controller
Smarthings UI for use with the nodejs-PoolController

# License

An application to control pool equipment from within Smartthings.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Adapted from Michael Usner's smartthings app controller from [here](https://github.com/michaelusner/pentair-pool-controler)

Requries Russell Goldin (tagyoureit) nodejs-poolController be running on your network.  For details please visit [here](https://github.com/tagyoureit/nodejs-poolController)

Readme borrowed from [bsileo](https://github.com/bsileo/SmartThings_Pentair)

## What is the Smartthing Pentair Pool Controller?
A Smartthings device designed to interface with a nodejs-poolControlller instance which is talking on the RS-485 bus to allow viewing and setting pool control options. Single smartthings app to manage the Pool pumps, lights and heater, the spa pump and heater, the chlorinator, and any installed additional "Features". 

iPhone layout:

<img src="https://github.com/dhop90/pentair-pool-controller/blob/master/images/IMG_6354.PNG" height="400"> <img src="https://github.com/dhop90/pentair-pool-controller/blob/master/images/IMG_6355.PNG" height="400" ><img src="https://github.com/dhop90/pentair-pool-controller/blob/master/images/IMG_6356.PNG" height="400"> <img src="https://github.com/dhop90/pentair-pool-controller/blob/master/images/IMG_6357.PNG" height="400">


iPad layout:

<img src="https://github.com/dhop90/pentair-pool-controller/blob/master/images/IMG_1088.PNG" height="300"> <img src="https://github.com/dhop90/pentair-pool-controller/blob/master/images/IMG_1089.PNG" height="300">

***

## Installation Instructions

1. Install and configure Nodejs-Poolcontroller (version 4.x+)
          https://github.com/tagyoureit/nodejs-poolController
2. Update your Nodejs-Poolcontroller installation with the Smartthings interface

3. Install the new Device Handler into the Smartthings IDE (http://graph.api.smartthings.com/)
   - Pentair Pool Controller

   1. Go to [https://graph.api.smartthings.com/ide/devices]
   2. Hit the "+New Device Type" at the top right corner
   3. Hit the "From Code" tab on the left corner
   4. Copy and paste the code from https://github.com/dhop90/pentair-pool-controller
   5. Hit the create button at the bottom
   6. Hit the "publish/for me" button at the top right corner (in the code window)

4. Install a new device of type Pentair Pool Controller and configure it.
    - Go to https://graph.api.smartthings.com/device/list, click New Device, complete the form selecting "Pentair Pool Controller" for the type. 
    - Be sure to fill in the correct Device Network ID.   
    - In the IDE (or Preferences in the SmartThings App) you can update all preferences for the Device:
    	- Controller IP and Port - Set these to match the device where you have nodejs-PoolController running
        - username/password if you have `"expressAuth": 1` configured in config.sjon

## Issues
Currently this is very specific to my setup.  Manually change the Device Handler to fit your environment.

Change circuit definition in initialize function as needed
```
circuit = [  
        spa:'1',  
        blower:'2',  
        poolLight:'3',  
        spaLight:'4',  
        cleaner:'5',  
        pool:'6',  
        highSpeed:'7',  
        spillway:'8'  
        ]
```	

Able to modify eggtimers and schedules by first going into preferences and setting the desired values and then going back to main tile and executing either + schedule or - schedule.  The time can also be changed the same way by activating the time panel.
