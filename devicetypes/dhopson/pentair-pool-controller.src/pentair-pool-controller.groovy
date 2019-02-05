/**
 *  Pentair Pool Controller
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 * Adapted from (name: "Pentair Controller", namespace: "michaelusner", author: "Michael Usner", oauth: true)
 */
 
 
metadata {
	definition (name: "Pentair Pool Controller", namespace: "dhopson", author: "David Hopson", oauth: true) {
        //capability
        capability "Switch"
    	capability "Polling"        
        capability "Refresh"
        capability "Temperature Measurement"
        capability "Thermostat"
       //commands
        command "poolToggle"
        command "spaToggle"
        command "spaModeToggle"
        command "poolModeToggle"
        command "poolLightToggle"
        command "spaLightToggle"
        command "allLightToggle"
		command "blowerToggle"
		command "cleanerToggle"
        command "allPumpsToggle"
        command "highspeedToggle"
        command "spillWayToggle"	   
        command "spatempUp"
		command "spatempDown"
        command "pooltempUp"
		command "pooltempDown"
        command "setdatetime"
        command "addSchedule"
        command "delSchedule"
        command "displayCntr"
        command "cleanWayToggle"
        command "Toggle" ,["string"]
        attribute "POOL LIGHT", "enum", ["on", "off"]
        attribute "SPA LIGHT", "enum", ["on", "off"]
	}
   
    preferences {
       	section("Select your controller") {
       		input "controllerIP", type:"text", title: "Controller IP address", required: true, displayDuringSetup: true
       		input "controllerPort", type:"number", title: "Controller port", required: true, defaultValue: "3000", displayDuringSetup: true
            input "username", type:"text", title:"Username", description: "username", required: true, displayDuringSetup: true
            input "password", type:"password", title:"Password", description: "Password", required: true, displayDuringSetup: true
            }
         section("schedules") {   
            input name: "controllerTime", type: "time", title: "System Time", descripiton: "Enter Time, then select time tile to update", required: false
            input name: "controllerDay", type: "number", title: "Select day", description: "Day", range: "1..31", required: false
            input name: "controllerMonth", type: "enum", title: "Select month", description: "Month", required: false,
               options: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
            input name: "controllerDoW", type: "enum", title: "Select Day of Week", decription: "Day of Week", required: false,
               options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" ]
            input name: "eggTimerORschedule", type: "enum", title: "Is this an egg timer or schedule?", description: "Egg Timer or Schedule?", required: false, 
               options:[ 
                    "Schedule", 
                    "Egg Timer"]
                    
            input name: "sch_circuit", type: "enum", title: "Circuit Name", description: "Which circuit should be used for this schedule", required: false, 
               options:[
                    "SPA",
        			"AIR BLOWER",
        			"POOL LIGHT",
        			"SPA LIGHT",
        			"CLEANER",
        			"POOL",
        			"HIGH SPEED",
        			"SPILLWAY"]
                    
            input name: "sch_id", type: "enum", title: "Schedule ID", description: "ID is used for both schedule/eggtimer addition and deletion", required: false, 
               options:[
                    "1",
        			"2",
        			"3",
        			"4",
        			"5",
        			"6",
        			"7",
        			"8",
                    "9",
        			"10",
        			"11",
        			"12"]   
                    
            input name: "egg_hour", type: "number", title: "Egg Timer hour", description: "Number of hours for egg timer", range: "0..12", required: false
            input name: "egg_min", type: "number", title: "Egg Timer minute", description: "Number of minutes for egg timer", range: "0..59", required: false        
            input name: "sch_start", type: "time", title: "Schedule Start Time", descripiton: "Start Time for schedule", required: false
            input name: "sch_end",   type: "time", title: "Schedule End Time",   descripiton: "End Time for schedule",   required: false             
            input name: "Monday", type: "bool", title: "Schedule Monday?", description: "Should the task be scheduled for this day?", required: false 
            input name: "Tuesday", type: "bool", title: "Schedule Tuesday?", description: "Should the task be scheduled for this day?", required: false
            input name: "Wednesday", type: "bool", title: "Schedule Wednesday?", description: "Should the task be scheduled for this day?", required: false
            input name: "Thursday", type: "bool", title: "Schedule Thursday?", description: "Should the task be scheduled for this day?", required: false
            input name: "Friday", type: "bool", title: "Schedule Friday?", description: "Should the task be scheduled for this day?", required: false
            input name: "Saturday", type: "bool", title: "Schedule Saturday?", description: "Should the task be scheduled for this day?", required: false
            input name: "Sunday", type: "bool", title: "Schedule Sunday?", description: "Should the task be scheduled for this day?", required: false 
            input name: "adebug", type: "enum", title: "Debug", description: "Which feature should be debugged", required: false, multiple:false, refreshAfterSelection:false, 
               options:[
                    "ALL",
                    "SPA",
        			"AIR BLOWER",
        			"POOL LIGHT",
        			"SPA LIGHT",
        			"CLEANER",
        			"POOL",
        			"HIGH SPEED",
        			"SPILLWAY",
                    "SCHEDULE",
                    "EGG TIMER",
                    "PARSE",
                    "TEMPATURE",
                    "PUMP",
                    "CIRCUIT"]
        }
       
      }  
    
	simulator {
		// TODO: define status and reply messages here
	}
    
	tiles(scale: 2) {

        standardTile("refresh", "device.refresh", width: 2, height: 2, canChangeBackground: true) {
        	state "Idle", label:'refresh', action:"refresh", icon:"st.secondary.refresh-icon", nextState: "Active", backgroundColor: "#ffffff", defaultState: true
            state "Active", label:'refresh', action:"refresh", icon:"st.secondary.refresh-icon", nextState: "Idle", backgroundColor: "#cccccc", defaultState: false
    	}      
        valueTile("timedate", "device.timedate", width: 3, height: 2, decoration: "flat", canChangeBackground: false) {
            state "val", label:'${currentValue}', action:"setdatetime", defaultState: true
		}                   
        valueTile("poolCntr", "device.poolCntr", width: 3, height: 2, decoration: "flat", canChangeBackground: false) {
            state "on", label:'${currentValue}', defaultState: true, nextState: "off", action:"displayCntr"
            state "off", label:'${currentValue}', defaultState: false, nextState: "on", action:"displayCntr"
        }   

        standardTile("freeze", "device.freeze", width:2, height: 2, canChangeBackground: true) {  //bc2323
        	state "on", label:'Freeze', defaultState: true, nextState: "on", backgroundColor: "#bc2323", icon: "st.Weather.weather7", action: "noAction"
            state "off", label:'Freeze', defaultState: false, nextState: "off", backgroundColor: "#ffffff", icon: "st.Weather.weather7", action: "noAction"
        }        
        standardTile("config", "device.config", width:2, height: 2, canChangeBackground: true) {
            state "unknown", label: 'Updating', defaultState: true, backgroundColor: "#F2F200", icon: "st.samsung.da.RC_ic_power", action: "noAction"
            state "on", label:'Ready', defaultState: false, nextState: "off", backgroundColor: "#79b821", icon: "st.samsung.da.RC_ic_power", action: "noAction"
            state "off", label:'Not Ready', defaultState: false, nextState: "on", backgroundColor: "#bc2323", icon: "st.samsung.da.RC_ic_power", action: "noAction"
        }
                    
        // Air, Pool and Spa Temperature
        //////////////////////////////////////////////////
        
        valueTile("airTemp",  "device.airTemp",  width: 2, height: 2, canChangeBackground: true) {
        	state("temperature", label:'${currentValue}°', icon: "st.Weather.weather2", 
            backgroundColors:[
                [value: 31, color: "#153591"],
                [value: 44, color: "#1e9cbb"],
                [value: 59, color: "#90d2a7"],
                [value: 74, color: "#44b621"],
                [value: 84, color: "#f1d801"],
                [value: 95, color: "#d04e00"],
                [value: 96, color: "#bc2323"]
            ])
    	}     
        valueTile("poolTemp", "device.poolTemp", width: 2, height: 2, canChangeBackground: true) { 
        	state("temperature", label:'${currentValue}°', icon: "st.Health & Wellness.health2", 
            backgroundColors:[
                [value: 31, color: "#153591"],
                [value: 44, color: "#1e9cbb"],
                [value: 59, color: "#90d2a7"],
                [value: 74, color: "#44b621"],
                [value: 84, color: "#f1d801"],
                [value: 95, color: "#d04e00"],
                [value: 96, color: "#bc2323"]
            ])
    	}
        valueTile("spaTemp",  "device.spaTemp",  width: 2, height: 2, canChangeBackground: true) {
        	state("temperature", label:'${currentValue}°',decoration: "flat", icon: "st.Bath.bath4", 
            backgroundColors:[
                [value: 31, color: "#153591"],
                [value: 44, color: "#1e9cbb"],
                [value: 59, color: "#90d2a7"],
                [value: 74, color: "#44b621"],
                [value: 84, color: "#f1d801"],
                [value: 95, color: "#d04e00"],
                [value: 96, color: "#bc2323"]
            ])
    	}
        
        // Pool controls
        //////////////////////////////////////////////////
        
        standardTile("POOL LIGHT", "device.POOL LIGHT", width: 2, height: 2, canChangeBackground: true) {
            state "off", label: 'POOL LIGHT', action: "poolLightToggle", icon: "st.Lighting.light13", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'POOL LIGHT', action: "poolLightToggle", icon: "st.Lighting.light11", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("SPA LIGHT", "device.SPA LIGHT",   width: 2, height: 2, canChangeBackground: true) {
			state "off", label: 'SPA LIGHT', action: "spaLightToggle", icon: "st.Lighting.light13", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'SPA LIGHT', action: "spaLightToggle", icon: "st.Lighting.light11", backgroundColor: "#79b821", nextState: "off"
		}        
        standardTile("ALL LIGHTS", "device.ALL LIGHTS",   width: 2, height: 2, canChangeBackground: true) {
        	state "half", label: 'ALL LIGHTS', action: "allLightToggle", icon: "st.Lighting.light11", backgroundColor: "#F2F200", nextState: "off"
			state "off", label: 'ALL LIGHTS', action: "allLightToggle", icon: "st.Lighting.light13", backgroundColor: "#ffffff", nextState: "on", defaultState: true
			state "on", label: 'ALL LIGHTS', action: "allLightToggle", icon: "st.Lighting.light11", backgroundColor: "#79b821", nextState: "off", defaultState: false
		}  
        standardTile("HIGH SPEED", "device.HIGH SPEED", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'HIGH SPEED', action: "highspeedUnknown", icon: "st.thermostat.thermostat-right", backgroundColor: "#F2F200"
			state "off", label: 'HIGH SPEED', action: "highspeedToggle", icon: "st.thermostat.thermostat-right", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'HIGH SPEED', action: "highspeedToggle", icon: "st.thermostat.thermostat-right", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("CLEANER", "device.CLEANER", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'CLEANER', action: "cleanerUnknown", icon: "st.Bath.bath13", backgroundColor: "#F2F200"
			state "off", label: 'CLEANER', action: "cleanerToggle", icon: "st.Bath.bath13", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'CLEANER', action: "cleanerToggle", icon: "st.Bath.bath13", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("ALLPUMPS", "device.ALLPUMPS", width: 2, height: 2, canChangeBackground: true) {
			state "off", label: 'ALL PUMPS', action: "allPumpsToggle", icon: "st.Appliances.appliances2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'ALL PUMPS', action: "allPumpsToggle", icon: "st.Appliances.appliances2", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("CLEANWAY", "device.CLEANWAY", width: 2, height: 2, canChangeBackground: true) {
			state "off", label: 'CLEANWAY', action: "cleanWayToggle", icon: "st.Appliances.appliances2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'CLEANWAY', action: "cleanWayToggle", icon: "st.Appliances.appliances2", backgroundColor: "#79b821", nextState: "off"
		} 
        standardTile("SPILLWAY", "device.SPILLWAY", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'SPILLWAY', action: "spillWayUnknown", icon: "st.Bath.bath6", backgroundColor: "#F2F200"
			state "off", label: 'SPILLWAY', action: "spillWayToggle", icon: "st.Bath.bath6", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'SPILLWAY', action: "spillWayToggle", icon: "st.Bath.bath6", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("AIR BLOWER", "device.AIR BLOWER", width: 2, height: 2, canChangeBackground: true) {
            state "unknown", label: 'AIR BLOWER', action: "blowerUnknown", icon: "st.vents.vent", backgroundColor: "#F2F200"
			state "off", label: 'AIR BLOWER', action: "blowerToggle", icon: "st.vents.vent", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'AIR BLOWER', action: "blowerToggle", icon: "st.vents.vent-open", backgroundColor: "#79b821", nextState: "off"
		}   
        // Runs Pool filter
        standardTile("POOL", "device.POOL", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'POOL', action: "poolUnknown", icon: "st.Health & Wellness.health2", backgroundColor: "#F2F200"
			state "off", label: 'POOL', action: "poolToggle", icon: "st.Health & Wellness.health2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'POOL', action: "poolToggle", icon: "st.Health & Wellness.health2", backgroundColor: "#79b821", nextState: "off"
		}
        //////////////////////////////////////////////////
        // HeatingSetpoint
 		// Turns Spa heat on/off
        // Can use thermostatFull to change heater set point
        
        standardTile("spaDown", "device.spaDown", width: 2, height: 2, canChangeBackground: true) {
			state "down", label: 'Down', action: "spatempDown",icon: "st.thermostat.thermostat-down",nextState: "push", backgroundColor: "#ffffff"
  			state "push", label: 'Down', action: "spatempDown",icon: "st.thermostat.thermostat-down",nextState: "down", backgroundColor: "#cccccc"           
		}  
        standardTile("SPA", "device.SPA", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'SPA HEAT', action: "spaUnknown", icon: "st.Bath.bath4", backgroundColor: "#F2F200"
			state "off", label: 'SPA HEAT', action: "spaToggle", backgroundColor: "#ffffff", nextState: "on", icon: "st.Bath.bath4" //,icon: "st.thermostat.heat"
			state "on", label: 'SPA HEAT', action: "spaToggle", backgroundColor: "#79b821", nextState: "off", icon: "st.Bath.bath4" //,icon: "st.thermostat.heating"
        }     
        standardTile("spaUp", "device.spaUp", width: 2, height: 2, canChangeBackground: true) {
			state "up", label: 'Up', action: "spatempUp",icon: "st.thermostat.thermostat-up",nextState: "push", backgroundColor: "#ffffff"
			state "push", label: 'Up', action: "spatempUp",icon: "st.thermostat.thermostat-up",nextState: "up", backgroundColor: "#cccccc"
    	}
        
        // Pool Heat
        
        standardTile("poolDown", "device.poolDown", width: 2, height: 2, canChangeBackground: true) {
			state "down", label: 'Down', action: "pooltempDown",icon: "st.thermostat.thermostat-down",nextState: "push", backgroundColor: "#ffffff"
  			state "push", label: 'Down', action: "pooltempDown",icon: "st.thermostat.thermostat-down",nextState: "down", backgroundColor: "#cccccc"           
		}  
        standardTile("POOLHEAT", "device.POOLHEAT", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'POOL HEAT', action: "poolUnknown", icon: "st.Health & Wellness.health2", backgroundColor: "#F2F200"
			state "off", label: 'POOL HEAT', action: "poolHeatToggle", backgroundColor: "#ffffff", nextState: "on", icon: "st.Health & Wellness.health2", defaultState: true 
			state "on", label: 'POOL HEAT', action: "poolHeatToggle", backgroundColor: "#79b821", nextState: "off", icon: "st.Health & Wellness.health2"
        }     
        standardTile("poolUp", "device.poolUp", width: 2, height: 2, canChangeBackground: true) {
			state "up", label: 'Up', action: "pooltempUp",icon: "st.thermostat.thermostat-up",nextState: "push", backgroundColor: "#ffffff"
			state "push", label: 'Up', action: "pooltempUp",icon: "st.thermostat.thermostat-up",nextState: "up", backgroundColor: "#cccccc"
    	} 
        
        standardTile("spaMode", "device.spaMode", width: 3, height: 2, canChangeBackground: true) {
			state "Off", label: '${name}', action: "spaModeToggle", backgroundColor: "#ffffff", nextState: "Heater"
			state "Heater", label: '${name}', action: "spaModeToggle", backgroundColor: "#d04e00", nextState: "Solar Pref", icon: "st.alarm.temperature.normal"
			state "Solar Pref", label: '${name}', action: "spaModeToggle", backgroundColor: "#90d2a7", nextState: "Solar Only", icon: "st.alarm.temperature.overheat"
			state "Solar Only", label: '${name}', action: "spaModeToggle", backgroundColor: "#153591", nextState: "Off", icon: "st.alarm.temperature.overheat"
		}            
        standardTile("poolMode", "device.poolMode", width: 2, height: 2, canChangeBackground: true) {
			state "Off", label: '${name}', action: "poolModeToggle", backgroundColor: "#ffffff", nextState: "Heater"
			state "Heater", label: '${name}', action: "poolModeToggle", backgroundColor: "#ffffff", nextState: "Solar Pref"
			state "Solar Pref", label: '${name}', action: "poolModeToggle", backgroundColor: "#ffffff", nextState: "Solar Only"
			state "Solar Only", label: '${name}', action: "poolModeToggle", backgroundColor: "#ffffff", nextState: "Off"
		}
        
        // spaHeatMode/poolHeatMode
        // spaheat/mode/# (0=off, 1=heater, 2=solar pref, 3=solar only)        
        valueTile("poolHeatMode", "device.poolHeatMode", width:3, height: 2, decoration: "flat", canChangeBackground: true) {
            state "Off", label: '${currentValue}', action: "poolModeToggle", backgroundColor: "#ffffff", nextState: "Heater"
			state "Heater", label: '${currentValue}', action: "poolModeToggle", backgroundColor: "#d04e00", nextState: "Solar Pref", icon: "st.alarm.temperature.normal"
			state "Solar Pref", label: '${currentValue}', action: "poolModeToggle", backgroundColor: "#90d2a7", nextState: "Solar Only", icon: "st.alarm.temperature.overheat"
			state "Solar Only", label: '${currentValue}', action: "poolModeToggle", backgroundColor: "#153591", nextState: "Off", icon: "st.alarm.temperature.overheat"
  
        }
        valueTile("spaHeatMode", "device.spaHeatMode", width:3, height: 2, decoration: "flat", canChangeBackground: true) {
        	//state "val", label:'${currentValue}', defaultState: true, action: "noAction"
            state "Off", label: '${currentValue}', action: "spaModeToggle", backgroundColor: "#ffffff", nextState: "Heater"
			state "Heater", label: '${currentValue}', action: "spaModeToggle", backgroundColor: "#d04e00", nextState: "Solar Pref", icon: "st.alarm.temperature.normal"
			state "Solar Pref", label: '${currentValue}', action: "spaModeToggle", backgroundColor: "#90d2a7", nextState: "Solar Only", icon: "st.alarm.temperature.overheat"
			state "Solar Only", label: '${currentValue}', action: "spaModeToggle", backgroundColor: "#153591", nextState: "Off", icon: "st.alarm.temperature.overheat"
        }

        /*
        standardTile("heatingSetpoint", "device.heatingSetpoint", width: 2, height: 2, canChangeBackground: true, key: "HEATING_SETPOINT") {
			state "off", label: '${name}: ${currentValue}', action: "spaToggle", unit: "dF", icon: "st.Bath.bath4", backgroundColor: "#ffffff", nextState: "on"
			state "on" , label: '${name}: ${currentValue}', action: "spaToggle", unit: "dF", icon: "st.Bath.bath4", backgroundColor: "#79b821", nextState: "off"
		} 
        */
        
        //////////////////////////////////////////////////
        
        valueTile("spablank", "device.spablank", width: 2, height: 1) {
			state "blank", label:'Spa Controls', defaultState: true
		}           
        valueTile("poolblank", "device.poolblank", width: 2, height: 1) {
			state "blank", label:'Pool Controls', defaultState: true
		}        
        valueTile("blank3", "device.blank3", width: 3, height: 1) {
			state "blank3", label:'', defaultState: true
		} 
        
        //////////////////////////////////////////////////
        
        valueTile("Pump 1", "device.Pump 1", width: 3, height: 4, decoration: "flat", canChangeBackground: false) {
			state "val", label:'${currentValue}', defaultState: true
		}     
        valueTile("Pump 2", "device.Pump 2", width: 3, height: 4, decoration: "flat", canChangeBackground: false) {
			state "val", label:'${currentValue}', defaultState: true
		}
        
        //////////////////////////////////////////////////        
 		multiAttributeTile(name:"thermostatFullspa", type:"thermostat", width:6, height:4) {
        	// center - display the current temperature
            tileAttribute("device.spaTemp", key: "PRIMARY_CONTROL", canChangeBackground: true) {
                attributeState("spatemp", label:'${currentValue}°', unit:"dF", defaultState: true)
    		}
            // right up/down - controls for increasing or decreasing the temperature
   			tileAttribute("device.spatemperature", key: "VALUE_CONTROL", unit: "dF") {
        		attributeState("VALUE_UP", action: "spatempUp", unit: "dF")
        		attributeState("VALUE_DOWN", action: "spatempDown", unit: "dF")
    		}
            // lower left corner - displays textual data about the thermostat, like humidity
            tileAttribute("device.spatemperature", key: "SECONDARY_CONTROL") {
                attributeState("spatemperature", label:'Heat to ${currentValue}°', unit:"dF", defaultState: true)
    		}      
            // changes background color - what the termostat is doing
    		tileAttribute("device.spathermostatOperatingState", key: "OPERATING_STATE") {         
                attributeState("heating", label: '${currentValue}',
                  backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                  ]
                )  
                //attributeState("default", backgroundColor:"#00A0DC")
                
                attributeState("idle", backgroundColor:"#00A0DC")
                attributeState("heater", backgroundColor:"#00A0DC")
                attributeState("solar pref", backgroundColor:"#00A0DC")
                attributeState("solar only", backgroundColor:"#00A0DC")
                attributeState("OFF", backgroundColor:"#00A0DC")
                
    		}
            // bottom center - mode (i.e. Heat, Cool, or Auto)
    		tileAttribute("device.spaHeatMode", key: "THERMOSTAT_MODE") {
                attributeState("off", label:'${name}')
        		attributeState("heat", label: '${name}')
                attributeState("solar pref", label:'${name}')
                attributeState("solar only", label:'${name}')
    		}
		}   
        multiAttributeTile(name:"thermostatFullpool", type:"thermostat", width:6, height:4) {
        	// center
    		tileAttribute("device.poolTemp", key: "PRIMARY_CONTROL") {
        		attributeState("pooltemp", label:'${currentValue}°', unit:"dF", defaultState: true)
    		}
            // right up/down
   			tileAttribute("device.pooltemperature", key: "VALUE_CONTROL", unit: "dF") {
        		attributeState("VALUE_UP", action: "pooltempUp", unit: "dF")
        		attributeState("VALUE_DOWN", action: "pooltempDown", unit: "dF")
    		}
            // lower left corner
            tileAttribute("device.pooltemperature", key: "SECONDARY_CONTROL") {
        		attributeState("pooltemperature", label:'Heat to ${currentValue}°', unit:"dF", defaultState: true)
    		}
            // changes background color
    		tileAttribute("device.poolthermostatOperatingState", key: "OPERATING_STATE") {                 
       	 		attributeState("heating", label: '${currentValue}',
                  backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                  ]
                )  
                //attributeState("default", backgroundColor:"#00A0DC")
                
                attributeState("idle", backgroundColor:"#00A0DC") 
                attributeState("OFF", backgroundColor:"#00A0DC")
                attributeState("Heater", backgroundColor:"#00A0DC")
                attributeState("Solar Pref", backgroundColor:"#00A0DC")
                attributeState("Solar Only", backgroundColor:"#00A0DC")
                
    		}
            // bottom center
    		tileAttribute("device.poolHeatMode", key: "THERMOSTAT_MODE", label:'${name}') {
                attributeState("off", label:'${name}')
        		attributeState("heat", label:'${name}')
                attributeState("Solar Pref", label:'${name}')
                attributeState("Solar Only", label:'${name}')
    		}
            // Not sure what this does
		} 
        //////////////////////////////////////////////////         
        
        valueTile("scheduleTile", "device.scheduleTile", width: 6, height: 8, decoration: "flat", canChangeBackground: false) {
			state "schedule", label:'${currentValue}', defaultState: true
		}        
        valueTile("eggTimerTile", "device.eggTimerTile", width: 6, height: 8, decoration: "flat", canChangeBackground: false) {
			state "eggTimerTile", label:'${currentValue}', defaultState: true
		}  
        
        standardTile("addscheduleTile", "device.addschedule", width: 2, height:2) {
			state "add", label: '${currentValue}', action: "addSchedule", backgroundColor: "#ffffff",icon: "st.custom.buttons.add-icon", nextState: "activated", defaultState: true
            state "activated", label: '${currentValue}', action: "addSchedule", backgroundColor: "#cccccc",icon: "st.custom.buttons.add-icon", nextState: "add", defaultState: false          
 		}           
        valueTile("eggTimerSchedule", "device.eggTimerSchedule", width:4, height: 4, canChangeBackground: false) {
        	state "on", label: '${currentValue}', nextState: "off"
            state "off", label: '${currentValue}', nextState: "on"
        }        
        standardTile("delscheduleTile", "device.delschedule", width: 2, height: 2) {
			state "delete", label: '${currentValue}', action: "delSchedule", backgroundColor: "#ffffff",icon: "st.custom.buttons.subtract-icon", nextState: "activated", defaultState: true
		    state "activated", label: '${currentValue}', action: "delSchedule", backgroundColor: "#cccccc",icon: "st.custom.buttons.subtract-icon", nextState: "delete", defaultState: false
        }   
        
        main(["CLEANWAY"])

        details([
        "poolCntr", "timedate",
        "refresh", "config", "freeze", 
        "airTemp", "poolTemp", "spaTemp",
        
        "POOL LIGHT", "SPA LIGHT", "ALL LIGHTS", 
        "ALLPUMPS", "HIGH SPEED", "CLEANER", 
        "SPILLWAY", "AIR BLOWER", "POOL", 
        
        "thermostatFullspa",       
        "spaDown", "SPA", "spaUp",
        
        "thermostatFullpool",
        "poolDown", "POOLHEAT", "poolUp",

        "spaHeatMode","poolHeatMode",
        "Pump 1","Pump 2",        
        "eggTimerSchedule","addscheduleTile","delscheduleTile",
        "eggTimerTile",
        "scheduleTile"
 		])
	}
}

// parse

def parse(String description) {
	log.debug "------------- Parse -------------"
          
    def msg = parseLanMessage(description)
    if (!msg) {
       def evt = createEvent(name: "refresh", isStateChange: "true", value: "Idle")
       log.error "parsed lan message was nil"
       return evt
    }
    
    def xmlerror = msg.xmlError
    if (xmlerror) {
       log.error "Error: ${xmlerror}"
       log.error "error in xml"
       return null
    }   
    
    // process xml response from /device - stop processing when done via return null
    
    if (msg.xml) {
        def body = new XmlSlurper().parseText(msg.body)
        if (!body) return null
        def verMajor = body.specVersion.major
        def verMinor = body.specVersion.minor
        def verPatch = body.specVersion.patch
        def manufacturer = body.device.manufacturer
        def modelDescription = body.device.modelDescription
        def name = body.device.friendlyName
        
        if ((!name) || (!verMajor) || (!verMinor) || (!verPatch) || (!manufacturer) || (!modelDescription)) return null
        
        state.version = "${name} Version\n${verMajor}.${verMinor}.${verPatch}"
        state.manufacturer = "By ${manufacturer}\n${modelDescription}"
        //log.info "${name} - ${modelDescription} - ${manufacturer} - ${verMajor}.${verMinor}.${verPatch}"
        def evt = createEvent(name: "refresh", isStateChange: "true", value: "Idle")

        return evt
    }    
   
    // process json messages
    def json = msg.data
    
    // return if no json, return
    if (!json) {
       log.error "json failed"
       sendEvent(name: "refresh", isStateChange: "true", value: "Idle")
       return
    }   
     
    // process each json key 
    json.keySet().each() {
      def key = it

      switch (key) {
        case "value":
              log.info "processing value"
              log.warn "      value = ${json.value}"
              break
        case "status":
              log.info "processing status"
              log.warn "      status = ${json.status}"
              break
        case "text":
              log.info "processing text"
              //log.warn "parse: text response: json = ${json}"
              log.warn "text: ${json.text}"
              log.warn "status: ${json.status}"
              log.warn "value: ${json.value}"
              
              if (json.text.contains("POOL LIGHT")) { 
                  log.info "status = POOL LIGHT ${json.status}"
                  sendEvent(name: "POOL LIGHT", value: "${json.status}")
                  adjustAll("POOL LIGHT", "ALL LIGHTS", json.status, "SPA LIGHT") 
                  
              } else if (json.text.contains("SPA LIGHT")) {
                  log.info "status = SPA LIGHT ${json.status}"
                  sendEvent(name: "SPA LIGHT", value: "${json.status}")
                  adjustAll("SPA LIGHT", "ALL LIGHTS", json.status, "POOL LIGHT") 
                  
              } else if (json.text.contains("CLEANER")) {
                  log.info "status = CLEANER ${json.status}"
                  sendEvent(name: "CLEANER", value: "${json.status}")
                  adjustAll("CLEANER", "ALLPUMPS", json.status, "HIGH SPEED")
                  adjustAll("CLEANER", "CLEANWAY", json.status, "SPILLWAY")
                  
              } else if (json.text.contains("HIGH SPEED")) {
                  log.info "status = HIGH SPEED ${json.status}"
                  adjustAll("HIGH SPEED", "ALLPUMPS", json.status, "CLEANER")
                  sendEvent(name: "HIGH SPEED", value: "${json.status}")
                                   
              } else if (json.text.contains("SPILLWAY")) {
                  log.info "status = SPILLWAY ${json.status}"
                  adjustAll("SPILLWAY", "CLEANWAY", json.status, "CLEANER")
                  sendEvent(name: "SPILLWAY", value: "${json.status}")
                  
              } else if ((json.text.contains("set spa heat mode to") || json.text.contains("update spa heat set point"))) { 
                  log.info "spa heat modified"
                  sendEvent(name: "spathermostatOperatingState" , value: "${json.status}") 
                  sendEvent(name: "spaHeatMode", value: "Spa Heater Mode: ${json.status}\nSpa Set\nPoint: ${json.value}°")
                         
              } else if ((json.text.contains("set pool heat mode to") || json.text.contains("update pool heat set point"))) {
                  log.info "pool heat modified"
                  sendEvent(name: "poolthermostatOperatingState" , value: "${json.status}") 
                  sendEvent(name: "poolHeatMode", value: "Pool Heater Mode: ${json.status}\nPool Set\nPoint: ${json.value}°")
                  
              } else if (json.text.contains("set spa heat setpoint")) {
                  log.info "spa heat setpoing"
                  sendEvent(name: "device.spatemperature", value: "${json.value}")
                
              } else if (json.text.contains("set pool heat setpoint")) {
                  log.info "pool heat setpoing"
                  sendEvent(name: "device.pooltemperature", value: "${json.value}")   
                  
              } else if (json.text.contains("toggle SPA to")) {
                  log.info "SPA toggle"
                  def spaheatvalue = json.value?.is(0) ? "idle" : "heating"
                      
                  log.info "SPA toggle - spaheatvalue = ${spaheatvalue}"
                  log.info "SPA toggle - spaTemp  = ${state.spaTemp}"

                  if (spaheatvalue == "idle") {
                       sendEvent(name: "spathermostatOperatingState", value: "${spaheatvalue}")     
                  } else {  
                       sendEvent(name: "spathermostatOperatingState", value: "${state.spaTemp}° ${spaheatvalue}") 
                  }
              } else if (json.text.contains("toggle POOL to")) {
                  log.info "POOL toggle"
                  sendEvent(name: "POOL", value: "${json.status}")  
              } else if (json.text.contains("BLOWER")) {
                  log.info "status = BLOWER ${json.status}"
                  sendEvent(name: "AIR BLOWER", value: "${json.status}")
              }
              break
        case "time":
              log.info "processing time"
              //log.debug "json.time = ${json.time}"
              def time = json.time
              //log.info "time = ${time}"
              def controllerTime = time.controllerTime
              def controllerDate = time.controllerDateStr
              def controllerDoW = time.controllerDayOfWeekStr
              sendEvent(name: "timedate", value: "${controllerDoW}\n${controllerDate}\n${controllerTime}\n("+state.uom+")")
              break
        case "heat": 
              if (state.debug_parse) log.info "### parse : heat - merged with temperature - no break ###"
        case "temperature":
              log.info "processing temperature"
              def temperatures = json.temperature
              def poolTemp = temperatures.poolTemp
              def spaTemp = temperatures.spaTemp
              state.spaTemp = spaTemp
              state.poolTemp = poolTemp
              def airTemp = temperatures.airTemp
              def freeze = temperatures.freeze?.is(1) ? "on" : "off"
              
              def poolSetPoint = temperatures.poolSetPoint
              def poolHeatMode = temperatures.poolHeatMode
              def poolHeatModeStr = temperatures.poolHeatModeStr   
              
              def spaSetPoint = temperatures.spaSetPoint
              def spaHeatMode = temperatures.spaHeatMode
              def spaHeatModeStr = temperatures.spaHeatModeStr
              
              def heaterActive = temperatures.heaterActive

              sendEvent(name: "poolTemp", value: "${poolTemp}")
              sendEvent(name: "spaTemp", value: "${spaTemp}")
              sendEvent(name: "airTemp", value: "${airTemp}")
              sendEvent(name: "freeze", value: "${freeze}")
              

              sendEvent(name: "spaHeatMode", value: "Spa Heater Mode: ${spaHeatModeStr}\nSpa Set\nPoint: ${spaSetPoint}°")
              sendEvent(name: "spaMode", value: "${spaHeatModeStr}")
              sendEvent(name: "spatemperature", value: "${spaSetPoint}")
          
              sendEvent(name: "poolHeatMode", value: "Pool Heater Mode: ${poolHeatModeStr}\nPool Set\nPoint: ${poolSetPoint}°")
              sendEvent(name: "poolMode", value: "${poolHeatModeStr}")
              sendEvent(name: "pooltemperature", value: "${poolSetPoint}")
                            
              break
        case "circuit":
              log.info "processing circuit"
              def circuits = json.circuit
              if (state.debug_circuit) log.info "state.circuit = ${state.circuit}"           
              
              circuits.keySet().each {
                 def c = circuits[it]
                 def String cname = c.name
                 if (!cname.contains('FEATURE') && !cname.contains("NOT USED") && !cname.contains("AUX")) {
                     //log.info "processing circuit number ${c.number} - ${cname}"
                     state.circuit[cname] = c.number
                 }    
              }
              
              state.circuit.remove('TEST')
              
              state.circuit.keySet().each {
                  def num = state.circuit[it]
                  def status = circuits."${num}".status

                  sendEvent(name: "${it}", value: status?.is(0) ? "off" : "on")
                  if (it == "SPA") {
					  def poolheatvalue = circuits."${state.circuit["POOL"]}".status?.is(0) ? "idle" : "heating"
                      def spaheatvalue = circuits."${state.circuit["SPA"]}".status?.is(0) ? "idle" : "heating"
                      
                      if (spaheatvalue == "idle") {
                          sendEvent(name: "spathermostatOperatingState", value: "${spaheatvalue}")     
                      } else {  
                          sendEvent(name: "spathermostatOperatingState", value: "${state.spaTemp}° ${spaheatvalue}") 
                      }
                      if (poolheatvalue == "idle")
                    	  sendEvent(name: "poolthermostatOperatingState", value: "${poolheatvalue}")
                      else
                    	  sendEvent(name: "poolthermostatOperatingState", value: "${state.poolTemp}° ${poolheatvalue}") 
                  }
              }
                                  
        	  break
        case "pump":      
              log.info "processing pump"
              if (state.debug_pump) log.info "### parse : pumps ###"
              def pumps = json.pump
              pumps.keySet().each {      
                  def pump = pumps[it]
                  if (state.debug_pump) log.debug "pump ${it} = ${pump}"           
                  if (state.debug_pump) log.info "Pump name = " + pump.name
                  if (state.debug_pump) log.info "friendlyName = " + pump.friendlyName
                  sendEvent(name: "${pump.name}", value: "${pump.friendlyName}\n---Pump---\nWatts :${pump.watts} \nRPM :${pump.rpm} \nError :${pump.err}\nState :${pump.drivestate}\nMode :${pump.run}")
              } 
              break
        case "chlorinator":
              log.info "processing chlorinator"
              def chlor = json.chlorinator
              def output = chlor.currentOutput
              def outputSpaPercent = chlor.outputSpaPercent
              def outputPoolPercent = chlor.outputPoolPercent
              def installed = chlor.installed
              def name = chlor.name
              def superChlorinate = chlor.superChlorinate
              def saltPPM = chlor.saltPPM
              break
        case "config":
             log.info "processing config"
              def config = json.config
              //log.debug "config = ${config}"
              sendEvent(name: "config", value: config.systemReady?.is(1) ? "on" : "off", descriptionText: "System Status is ${config.systemReady?.is(1) ? 'on' : 'off'}")
              break
        case "UOM":
              log.info "processing UOM"
              state.uom = json.UOM.UOMStr
              log.info "uom = ${state.uom}"
              break
        case "valve":
              log.info "processing valve"
              def valve = json.valve
              log.info "valve = ${valve}"
              break
        case "intellichem":
              log.info "processing intellichem"
              def intellichem = json.intellichem
              //log.info "intell = ${intellichem}"
              break
        case "schedule":
              log.info "processing schedule"
              def schedule = json.schedule
              
              def fullSchedule = "----- SCHEDULE -----\n"
              fullSchedule = fullSchedule + "#      Circuit     StartTime     EndTime\n"      
              fullSchedule = fullSchedule + "____________________________________\n"
              
              def eggSchedule = "----- EGG TIMER -----\n"
              eggSchedule = eggSchedule + "   #         Circuit        Duration\n"
              eggSchedule = eggSchedule +   "____________________________________\n"  
            
              def int circuitSize = 0
              def space = ""
              def int i
              def active = 0
              def ison = ""
              schedule.keySet().each {
                  space = ""
                  def event = schedule[it]
                  def ID = event.ID
                  def CIRCUIT = event.CIRCUIT
                  def CN = event.CIRCUITNUM
                  def MODE = event.MODE
                  def bytes = event.BYTES
                  if (state.debug_schedule) log.debug "it:${it}-ID:${ID} CIRCUIT:${CN}-${CIRCUIT} MODE:${MODE} BYTES:${bytes}"
                 
                  if (MODE == "Egg Timer") {
                      def DURATION = event.DURATION
                      circuitSize = 16 - CIRCUIT.size()
                      for (i = 0; i <circuitSize; i++) {
                     	 space = space + " "
                      }
                      eggSchedule = eggSchedule + "${ID}${space}${CIRCUIT}${space}${DURATION}\n"
                  } else if (MODE == "Schedule") {
                      if (CIRCUIT != "NOT USED") {
                         circuitSize = 16 - CIRCUIT.size()
                         for (i = 0; i <circuitSize; i++) {
                      	    space = space + " "
                         }
                      
                	     def START_TIME = event.START_TIME
                	     def END_TIME = event.END_TIME
                         ison = ""
                         def between = timeOfDayIsBetween(START_TIME, END_TIME, new Date(), location.timeZone) 
                         def circuit_status = device.currentState(CIRCUIT)?.value
                         if (state.debug_schedule) log.debug "circuit_status = ${circuit_status}"
    				     if (between && circuit_status == "on") {
                            if (state.debug_schedule) log.debug "current time is between ${START_TIME} and ${END_TIME} for ID ${ID} circuit ${CIRCUIT}"
                               ison = "*"
                            active = active + 1
                         }
                      
                	     def DAYS = event.DAYS
                         def day_list = DAYS.split(" ")
                         def days = []
                         day_list.each {
                             days << it.substring(0,3)
                         }    
                         fullSchedule = fullSchedule + "${ison}${ID}${space}${CIRCUIT}${space}${START_TIME}${space}${END_TIME}\nDAYS:${days}\n\n"
                     }
                  }
  			 }	
             fullSchedule = fullSchedule + "* ${active} active schedule(s)"
             sendEvent(name: "scheduleTile", value: "${fullSchedule}")
             sendEvent(name: "eggTimerTile", value: "${eggSchedule}")
           	 break
		default:
            log.info "### parse : default ###"
            log.info "key = ${key}"
		}  
        
     }   
}  

// Init routines

def initialize() {
	runEvery1Minute(refresh)
    /*
    state.circuit = [
    "SPA":1,
    "AIR BLOWER":2,
    "POOL LIGHT":3,
    "SPA LIGHT":4,
    "CLEANER":5,
    "POOL":6,
    "HIGH SPEED":7,
    "SPILLWAY":8]
    */
    state.circuit = ["TEST":99]

    sendHubCommand(setFeature("/circuit"))
    
    state.dayValueMap = [Sunday:1,Monday:2,Tuesday:4,Wednesday:8,Thursday:16,Friday:32,Saturday:64,
                          Sun:1,Mon:2,Tue:4,Wed:8,Thu:16,Fri:32,Sat:64] 
                          
    state.Month = ["Jan":1,"Feb":2,"Mar":3,"Apr":4,"May":5,"Jun":6,"Jul":7,"Aug":8,"Sep":9,"Oct":10 ,"Nov":11,"Dec":12]                     
                          
    state.dayMap = ["Sunday":Sunday, 
                     "Monday":Monday, 
                     "Tuesday":Tuesday, 
                     "Wednesday":Wednesday, 
                     "Thursday":Thursday, 
                     "Friday":Friday, 
                     "Saturday":Saturday]                    
}

def refresh() {
    if (state.debug_all) log.warn "Requested a refresh"
    //sendEvent(name: "refresh", isStateChange: "true", value: "Active")
    sendHubCommand(setFeature("/all"))
    sendEvent(name: "config", value:"unknown", descriptionText: "System Status is unknown");
    sendHubCommand(setFeature("/device"))
    updateSchedule()
}

def poll() {
	// poll gets /all & /device status messages from pool controller (raspberry Pi)
    // this runs every minute
    sendHubCommand(setFeature("/all"))
    sendHubCommand(setFeature("/device"))
}

def setDebugFlags(value) {
    state.debug_all = value
    state.debug_spa = value
    state.debug_airblower = value
    state.debug_poollight = value
    state.debug_spalight = value
    state.debug_cleaner = value
    state.debug_pool = value
    state.debug_highspeed = value
    state.debug_spillway = value
    state.debug_schedule = value
    state.debug_eggtimer = value
    state.debug_parse = value
    state.debug_temp = value
    state.debug_pump = value
    state.debug_circuit = value
}

def updated() {
    //if (state.debug_all) 
    log.info "######### UPDATED #########" 
    initialize()
    state.eggTimerORschedule = eggTimerORschedule
    
    setDebugFlags(false)
    switch (adebug) {
       case "ALL":
          setDebugFlags(true)
          break
       case "TEMPATURE":
          state.debug_temp = true
          break
       case "SCHEDULE":
          state.debug_schedule = true
          state.debug_eggtimer = true
          break
       case "PARSE":
          state.debug_parse = true
          break    
       case "PUMP":
          state.debug_pump = true
          break      
       case "EGG TIMER":
          state.debug_eggtimer = true
          state.debug_schedule = true
          break   
       case "SPA":
          state.debug_spa = true
          state.debug_parse = true
          break
       case "AIR BLOWER":   
          state.debug_airblower = true
          state.debug_parse = true
          break
       case "POOL LIGHT":
          state.debug_poollight = true
          state.debug_parse = true
          break
       case "SPA LIGHT":  
          state.debug_spalight = true
          state.debug_parse = true
          break
       case "CLEANER":
          state.debug_cleaner = true
          state.debug_parse = true
          break
       case "POOL":
          state.debug_pool = true
          state.debug_parse = true
          break
       case "CIRCUIT":
          state.debug_circuit = true
          state.debug_spa = true
          state.debug_parse = true
          state.debug_pool = true
          state.debug_cleaner = true
          state.debug_spalight = true
          state.debug_airblower = true
          state.debug_highspeed = true
          state.debug_spillway = true
          state.debug_pump = true
          break          
       case "HIGH SPEED":
          state.debug_highspeed = true
          state.debug_parse = true
          break
       case "SPILLWAY":
          state.debug_spillway = true
          state.debug_parse = true
          break
       default:
          setDebugFlags(false)
    }      
          
    refresh()
}
     
def installed() {
	//if (state.debug_all) 
    log.info "########## installed ###########"
    initialize()
    setDeviceNetworkId("${controllerIP}","${controllerPort}")  
}

// Schedule 

// API calls in server.js file on https://github.com/tagyoureit/nodejs-poolController/blob/b3d31a94daf486058d9a8d8043d9039d3a459487/src/lib/comms/server.js
// curl -X GET http://user:pass@pi-pool:3000/schedule | jq '.schedule | ."7"'
// curl -X GET http://user:pass@pi-pool:3000/schedule/set/7/3/21/00/22/00/127
// curl -X GET http://user:pass@pi-pool:3000/schedule/set/id/7/circuit/3

def get_hour(time) {
   return time.format('HH')
}

def get_minute(time) {
   return time.format('mm')
}

def build_dow() {
    if (state.debug_schedule || state.debug_eggtimer) log.debug "state.dayMap = ${state.dayMap}"
       
    def dow = []
    state.dayMap.keySet().each {
       if (state.dayMap[it]) dow << it
    }   
    return (dow)
}

def calc_dow() {      
    def calc = 0
    state.dayMap.keySet().each {
       if (state.debug_schedule || state.debug_eggtimer) log.warn "it = ${it}"
       if (state.dayMap[it]) {        
          calc = calc + state.dayValueMap[it]
       }   
    }   
    if (state.debug_schedule || state.debug_eggtimer) log.debug "calc = ${calc}"    
    return (calc)
}

def updateSchedule() {
    def ID = sch_id? sch_id : 0
    def mode = state.eggTimerORschedule
    def dow = build_dow()
    if (dow == []) dow = ""
    def times
    if (mode == "Egg Timer") {
        //eggTimer
        sendEvent(name: "eggTimerSchedule", value: "Egg Timer\nID:${sch_id} Name:${sch_circuit}\nDuration:${egg_hour}:${egg_min}", isStateChange: "true")
    } else if (mode == "Schedule") {
        //schedule
        if (!sch_start || !sch_end) {
            times = "Start: 0:0 End: 0:0"
        } else {
            Date startTime = Date.parse("yyyy-MM-dd'T'HH:mm",sch_start)  
            Date endTime = Date.parse("yyyy-MM-dd'T'HH:mm",sch_end)   
            String sch_starthh = get_hour(startTime)
            String sch_startmm = get_minute(startTime)
            String sch_endhh = get_hour(endTime)
            String sch_endmm = get_minute(endTime)
            times = "Start: ${sch_starthh}:${sch_startmm} End: ${sch_endhh}:${sch_endmm}"
        }   
        sendEvent(name: "eggTimerSchedule", value: "Schedule\nID:${sch_id} Name:${sch_circuit}\n${times}\n\n${dow}", isStateChange: "true")
    }
    else 
        sendEvent(name: "eggTimerSchedule", value: "Schedule or Egg Timer not set\nID:${sch_id} Name:${sch_circuit}")
}

def addSchedule() {
    
    if (!sch_circuit || !sch_id) {
       sendEvent(name: "addschedule", isStateChange: "true", value: "add", descriptionText: "addschedule was updated")
       return
    }   
    def sch_circuit_num = state.circuit[sch_circuit]
    if (state.eggTimerORschedule == "Egg Timer") {
       def addeggtimer = setFeature("/eggtimer/set/id/${sch_id}/circuit/${sch_circuit_num}/hour/${egg_hour}/min/${egg_min}")
       sendHubCommand(addeggtimer)
    } else (state.eggTimerORschedule == "Schedule") {
       if (!sch_start || !sch_end) {
          sendEvent(name: "addschedule", isStateChange: "true", value: "add", descriptionText: "addschedule was updated")
          return
       }
       Date startTime = Date.parse("yyyy-MM-dd'T'HH:mm",sch_start)  
       Date endTime = Date.parse("yyyy-MM-dd'T'HH:mm",sch_end)  

       String sch_starthh = get_hour(startTime)
       String sch_startmm = get_minute(startTime)
       String sch_endhh = get_hour(endTime)
       String sch_endmm = get_minute(endTime)
       if (state.debug_schedule) log.info "start time: ${sch_starthh}, ${sch_startmm}"
       if (state.debug_schedule) log.info "end time: ${sch_endhh}, ${sch_endmm}"
       def sch_dow_num = calc_dow()
        
       // ----- setFeature : query = /schedule/set/7/3/21/00/22/00/127 -------
       // Response: REST API received request to set schedule 7 with values (start) 21:0 (end) 22:0 with days value 127
       // Response: REST API received request to set circuit on schedule with ID (7) to POOL LIGHT
 
       def scheduleCircuit = setFeature("/schedule/set/id/${sch_id}/circuit/${sch_circuit_num}")
       if (state.debug_schedule) log.info "scheduleCircuit = ${scheduleCircuit}"
       if (state.debug_schedule) log.info "schedule set id:${sch_id} to circuit:${sch_circuit_num}"    
       sendHubCommand(scheduleCircuit)
             
       def addsch = setFeature("/schedule/set/${sch_id}/${sch_circuit_num}/${sch_starthh}/${sch_startmm}/${sch_endhh}/${sch_endmm}/${sch_dow_num}")
       if (state.debug_schedule) log.info "schedule set id:${sch_id} to circuit:${sch_circuit_num} for time: start hour:${sch_starthh} start min:${sch_startmm} end hour:${sch_endhh} end min:${sch_endmm} day of the week number:${sch_dow_num}"
       if (state.debug_schedule) log.info "addsch = ${addsch}"
       sendHubCommand(addsch)
    }
    sendEvent(name: "addschedule", isStateChange: "true", value: "add", descriptionText: "addsechedule was updated")
    def action = setFeature("/schedule")
    sendHubCommand(action)
}

def delSchedule() {
   // app.get('/schedule/delete/id/:id'
   // 'REST API received request to delete schedule or egg timer with ID:' + id;
   if (!sch_id) {
       sendEvent(name: "delschedule", isStateChange: "true", value: "delete", descriptionText: "delschedule was updated")
       return
   }    
   def action = setFeature("/schedule/delete/id/${sch_id}") 
   sendHubCommand(action)
   sendEvent(name: "delschedule", isStateChange: "true", value: "delete", descriptionText: "delschedule was updated")
   action = setFeature("/schedule")
   sendHubCommand(action)
}

def setFeature(query) {
    def userpass = encodeCredentials(username, password)
    def headers = getHeader(userpass)
    def dni = setDeviceNetworkId("${controllerIP}","${controllerPort}")
     
    def poolAction = new physicalgraph.device.HubAction(
		method: "GET",
		path: query,
        headers: headers,
        dni
	)
    
    if (state.debug_parse) log.info "setFeature query: ${query}\npoolAction =\n${poolAction}"
	return poolAction
}

// Temp routines

def getHeatMode(mode) {
    def num = 0
    switch (mode) {
       case "Off":
          num = 0
          break
       case "Heater":
          num = 1
          break
       case "Solar Pref":
          num = 2
          break
       case "Solar Only":
          num = 3 
          break
       default: 
          num = null
    }
    return num
}   

def spaalterSetpoint(targetValue) {
    setSpaHeatPoint(targetValue)  
}

def poolalterSetpoint(targetValue) {
    setPoolHeatPoint(targetValue)  
}

def setPoolHeatPoint(value) {
    sendHubCommand(setFeature("/poolheat/setpoint/${value}"))
}

def setSpaHeatPoint(value) {
    sendHubCommand(setFeature("/spaheat/setpoint/${value}"))
}

def setPoint(num) {
    sendHubCommand(setFeature("/spaheat/setpoint/${num}"))
}

def changeSpaTemp(direction) {
    def ts = device.currentState("spatemperature")
    def value = direction?.is("Up") ? (ts ? ts.integerValue + 1 : 72) : (ts ? ts.integerValue - 1 : 72)
    spaalterSetpoint(value)
    //sendEvent(name: "spaheatingSetpoint", value: "${value}°")
    sendEvent(name: "spatemperature", value: "${value}")
    sendEvent(name: "spa${direction}", isStateChange: "true")
}

def changePoolTemp(direction) {
    def ts = device.currentState("pooltemperature")
    def value = direction?.is("Up") ? (ts ? ts.integerValue + 1 : 72) : (ts ? ts.integerValue - 1 : 72)
    poolalterSetpoint(value)
    //sendEvent(name: "poolheatingSetpoint", value: "${value}°")
    sendEvent(name: "pooltemperature", value: "${value}")
    sendEvent(name: "pool${direction}", isStateChange: "true")    
}

def spatempUp() {
    changeSpaTemp("Up")
}

def spatempDown() {
    changeSpaTemp("Down")
}

def pooltempUp() {
    changePoolTemp("Up")
}

def pooltempDown() {
    changePoolTemp("Down")
}

//Toggle routines

def setdatetime() {
// datetime/set/time/{hour}/{min}/{dow}/{day}/{mon}/{year}/{dst}
// set the schedule on the controller for the particular schedule ID. 
// dow= day of week as expressed as [0=Sunday, 1=Monday, 2=Tuesday, 4=Wednesday, 8=Thursday, 16=Friday, 32=Saturday] 
// or a combination thereof [3=Monday+Tuesday]. To set a schedule set a valid start and end time (hh:mm). 
// To set an egg timer, set the start time to 25:00 and the endtime to the duration (hh:mm) you want the egg timer to run.
//"text": "FAIL: SOCKET API - hour (NaN) should be 0-23 and minute (NaN) should be 0-59. 
// Received: NaN:NaNDay (NaN) should be 0-31, month (NaN) should be 0-12 and year (NaN) should be 0-99.
// Day of week (NaN) should be one of: [1,2,4,8,16,32,64] [Sunday->Saturday]dst (0) should be 0 or 1" }
    log.info "In setdatetime"
    if ((!controllerTime) || (!controllerDay) || (!controllerMonth) || (!controllerDow)) return
    
    log.info "controllerTime = ${controllerTime}"
    Date newDate = Date.parse("yyyy-MM-dd'T'HH:mm",controllerTime)  
    log.info "Date = ${newDate}"
    
    String year = newDate.format('yy')
    
    //String month = newDate.format('MM')
    String month = state.Month[controllerMonth]
    //month = 11
    log.info "month = ${month}"
    
    String day = controllerDay
    //String day = newDate.format('dd')
    log.info "day = ${day}"
    //day = 8
    
    String hour = newDate.format('HH')
    String minute = newDate.format('mm')
    String DayOfWeek = controllerDoW
    //String DayOfWeek = newDate.format('EE')
    log.info "DayOfWeek = ${DayOfWeek}"
    //DayOfWeek = "Sat"
    

    def downum = state.dayValueMap[DayOfWeek]
    def action = setFeature("/datetime/set/time/${hour}/${minute}/date/${downum}/${day}/${month}/${year}/0")
    sendHubCommand(action)
}

def adjustAll(feature, Allfeature, status, otherFeature) {
    sendEvent(name: Allfeature, value: "off")
    def current = device.currentState(otherFeature)?.value                 
    if (state.debug_parse) log.info "current = ${current}"
    if (status == "on" && current == "on")
         sendEvent(name: Allfeature, value: "on")
    else if (status == "off" || slcurrent == "off") 
         sendEvent(name: Allfeature, value: "off")
}

def spaModeToggle() {
    ModeToggle("spa")
}

def poolModeToggle() {
    ModeToggle("pool")
}

def ModeToggle(type) {
	log.info "--------- ${type} Toggle ----------"
    def mode = device.currentValue("${type}Mode")
    def num = ""
    def value = ""
    log.info "mode = ${mode}"    
    switch (mode) {
    	case "Off":
        	num = "1"
            value = "Heater"
        	break
    	case "Heater":
        	num = "2"
            value = "Solar Pref"
        	break
        case "Solar Pref":
        	num = "3"
            value = "Solar Only"
        	break
        case "Solar Only":
        	num = "0"
            value = "Off"
        	break
        default:
        	num = "1"
            value = "Heater"
    }        
        
    sendHubCommand(setFeature("/${type}heat/mode/${num}"))
    //sendEvent(name: "${type}Mode", value: "${value}")

}

def displayCntr() {
    if (state.debug_all) log.info "state.Cntr = ${state.Cntr}"
    if (state.Cntr) {
        sendEvent(name:"poolCntr", value: state.version, isStateChange: "true")
        state.Cntr = true
    } else {  
        sendEvent(name:"poolCntr", value: state.manufacturer, isStateChange: "true")  
        state.Cntr = false
    }    
    state.Cntr = !state.Cntr     
}

def Toggle(device) {
    def num = state.circuit[device]
    sendHubCommand(setFeature("/circuit/${num}/toggle/"))
    //refresh()
}

def spaToggle() {
    // turns spa heater on/off
	if (state.debug_spa) log.warn "Executing 'spaToggle'"
    Toggle('SPA')
}

def blowerToggle() {
	if (state.debug_airblower) log.warn "Executing 'blowerToggle'"  
    Toggle('AIR BLOWER')
}

def poolLightToggle() {
	if (state.debug_poollight) log.warn "Executing 'poolLightToggle'"
    Toggle('POOL LIGHT')
}

def allLightToggle() {
	if (state.debug_poollight) log.warn "Executing 'allLightToggle'"
    //def poollightstate = device.currentValue("POOL LIGHT")
    //def spalightstate = device.currentValue("SPA LIGHT")
    def allLightstate = device.currentValue("ALL LIGHTS")
    //log.debug "poollightstate = ${poollightstate}"
    //log.debug "spalightstate = ${spalightstate}"
    //log.debug "allLightstate = ${allLightstate}"

    def value 
    
    if (allLightstate == "on")
        value = 0
    else
        value = 1
    //log.debug "value = ${value}"
    
    def poolnum = state.circuit["POOL LIGHT"]
    def spanum = state.circuit["SPA LIGHT"]    
    sendHubCommand(setFeature("/circuit/${poolnum}/set/${value}"))
    sendHubCommand(setFeature("/circuit/${spanum}/set/${value}"))
}

def spaLightToggle() {
	if (state.debug_spalight) log.warn "Executing 'spaLightToogle'"
    Toggle('SPA LIGHT')
}

def cleanerToggle() {
	if (state.debug_cleaner) log.warn "Executing 'cleanerToogle'"
    Toggle('CLEANER')
}

def allPumpsToggle() {
	if (state.debug_cleaner) log.warn "Executing 'allPumpsToogle'"
    Toggle('CLEANER')
    Toggle('HIGH SPEED')
}

def cleanWayToggle() {
	if (state.debug_cleaner) log.warn "Executing 'cleanWayToogle'"
    Toggle('CLEANER')
    Toggle('SPILLWAY')
}

def poolToggle() {
	if (state.debug_pool) log.warn "Executing 'poolToogle'"
    Toggle('POOL')
}

def highspeedToggle() {
	if (state.debug_highspeed) log.warn "Executing 'highspeedToggle'"
    Toggle('HIGH SPEED')
}

def spillWayToggle() {
	if (state.debug_spillway) log.warn "Executing 'spillWayToogle'"
    Toggle('SPILLWAY')
}

/*
def poolHeatToggle() {
    Toggle
} 
*/

// private functions

private delayAction(long time) {
    new physicalgraph.device.HubAction("delay $time")
}

private setDeviceNetworkId(ip,port){
      def iphex = convertIPtoHex(ip)
      def porthex = convertPortToHex(port)
      device.deviceNetworkId = "$iphex:$porthex"
      return (device.deviceNetworkId)
}

private getHostAddress() {
    return "${controllerIP}:${controllerPort}"
}

private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02x', it.toInteger() ) }.join()
    return hex
}

private String convertPortToHex(port) {
    String hexport = port.toString().format( '%04x', port.toInteger() )
    return hexport
}

private encodeCredentials(username, password){
    def userpassascii = "${username}:${password}"
    def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    return userpass
}

private getHeader(userpass){
    def headers = [:]
    headers.put("HOST", "${controllerIP}:${controllerPort}")
    headers.put("Authorization", userpass)
    return headers
}