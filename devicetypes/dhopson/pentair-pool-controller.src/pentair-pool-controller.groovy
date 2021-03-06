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
 
// API calls in server.js file on https://github.com/tagyoureit/nodejs-poolController/blob/b3d31a94daf486058d9a8d8043d9039d3a459487/src/lib/comms/server.js
// curl -X GET http://user:pass@pi-pool:3000/schedule | jq '.schedule | ."7"'
// curl -X GET http://user:pass@pi-pool:3000/schedule/set/7/3/21/00/22/00/127
// curl -X GET http://user:pass@pi-pool:3000/schedule/set/id/7/circuit/3
 

metadata {
	definition (name: "Pentair Pool Controller", namespace: "dhopson", author: "David Hopson", oauth: true) {
        //capability
        capability "Switch"
    	capability "Polling"        
        capability "Refresh"
        capability "Temperature Measurement"
        capability "Thermostat"
        capability "Switch Level"
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
        command "highSpeedToggle"
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
        command "poolHeatToggle"
        command "Toggle" ,["string"]
        command "set_debug"
        command "spaSetRangedLevel", ["number"]
        command "poolSetRangedLevel", ["number"]
        //attribute
        attribute "POOL LIGHT", "enum", ["on", "off"]
        attribute "SPA LIGHT", "enum", ["on", "off"]
	}
   
    preferences {
       	section("Select your controller") {
       		input "controllerIP", type:"text", title: "Controller IP address", required: true, displayDuringSetup: true
       		input "controllerPort", type:"number", title: "Controller port", required: true, defaultValue: "3000", displayDuringSetup: true
            input "username", type:"text", title:"Username", description: "username", required: true, displayDuringSetup: true
            input "password", type:"password", title:"Password", description: "Password", required: true, displayDuringSetup: true, hidden: true
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
            input name: "DBG", type: "enum", title: "Debug", description: "Debug", required: false, multiple: false, submitOnChange : true,
            	options:[
                	"PUMPS",
                    "TEMPERATURES",
                    "CIRCUITS",
                    "TIME",
                    "SCHEDULE",
                    "CONFIG",
                    "ALL",
                    "ACTION"]
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
        standardTile("debug", "device.debug", width: 2, height: 2, canChangeBackground: true) {
        	state "on", label:'Debug ${currentValue}', action:"set_debug", icon:"st.samsung.da.RC_ic_power", nextState: "off", backgroundColor: "#79b821", defaultState: false
            state "off", label:'Debug ${currentValue}', action:"set_debug", icon:"st.samsung.da.RC_ic_power", nextState: "on", backgroundColor: "#ffffff", defaultState: true
    	}  
        valueTile("poolspace", "device.poolspace", width: 4, height: 2, decoration: "flat", canChangeBackground: false) {
            state "val", label:'POOL MODE ->', action:"", defaultState: true, icon: "st.Health & Wellness.health2" //, backgroundColor: "#00a0dc"
		} 
        valueTile("spaspace", "device.spaspace", width: 4, height: 2, decoration: "flat", canChangeBackground: false) {
            state "val", label:'SPA MODE ->', action:"", defaultState: true, icon: "st.Bath.bath4" //, backgroundColor: "#00a0dc"
		}        
        valueTile("timedate", "device.timedate", width: 3, height: 2, decoration: "flat", canChangeBackground: false) {
            state "val", label:'${currentValue}', action:"setdatetime", defaultState: true
		}          
        valueTile("poolCntr", "device.poolCntr", width: 3, height: 2, decoration: "flat", canChangeBackground: false) {
            state "on", label:'${currentValue}', defaultState: true, nextState: "off", action:"displayCntr"
            state "off", label:'${currentValue}', defaultState: false, nextState: "on", action:"displayCntr"
        }   
        standardTile("freeze", "device.freeze", width:2, height: 2, canChangeBackground: false, decoration: "flat") {  
        	state "on", label:'Freeze', defaultState: false, nextState: "off", backgroundColor: "#bc2323", icon: "st.Weather.weather7" //, action: "noAction"
            state "off", label:'Freeze', defaultState: true, nextState: "on", backgroundColor: "#ffffff", icon: "st.Weather.weather7" //, action: "noAction"
        }        
        standardTile("config", "device.config", width:2, height: 2, canChangeBackground: false, decoration: "flat") {  
            state "unknown", label: 'Updating', defaultState: true, backgroundColor: "#F2F200", icon: "st.samsung.da.RC_ic_power"
        	state "on", label:'Ready', defaultState: false, nextState: "off", backgroundColor: "#79b821", icon: "st.samsung.da.RC_ic_power" //, action: "noAction"
            state "off", label:'Not Ready', defaultState: true, nextState: "on", backgroundColor: "#bc2323", icon: "st.samsung.da.RC_ic_power" //, action: "noAction"
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
        valueTile("poolTemp4", "device.poolTemp4", width: 2, height: 2, canChangeBackground: true) { 
        	state("temperature", label:'Heat:${currentValue}°', icon: "st.Health & Wellness.health2" /*, 
            backgroundColors:[
                [value: 31, color: "#153591"],
                [value: 44, color: "#1e9cbb"],
                [value: 59, color: "#90d2a7"],
                [value: 74, color: "#44b621"],
                [value: 84, color: "#f1d801"],
                [value: 95, color: "#d04e00"],
                [value: 96, color: "#bc2323"]
            ] */
            )
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
        valueTile("spaTemp4", "device.spaTemp4", width: 2, height: 2, canChangeBackground: true) { 
        	state("temperature", label:'Heat:${currentValue}°', icon: "st.Bath.bath4" /*, 
            backgroundColors:[
                [value: 31, color: "#153591"],
                [value: 44, color: "#1e9cbb"],
                [value: 59, color: "#90d2a7"],
                [value: 74, color: "#44b621"],
                [value: 84, color: "#f1d801"],
                [value: 95, color: "#d04e00"],
                [value: 96, color: "#bc2323"]
            ] */
            )
    	}
        
        // Pool controls
        //////////////////////////////////////////////////       
        standardTile("POOL LIGHT", "device.POOL LIGHT", width: 2, height: 2, canChangeBackground: true) {
            state "off", label: 'POOL', action: "poolLightToggle", icon: "st.Lighting.light13", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'POOL', action: "poolLightToggle", icon: "st.Lighting.light11", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("SPA LIGHT", "device.SPA LIGHT",   width: 2, height: 2, canChangeBackground: true) {
			state "off", label: 'SPA', action: "spaLightToggle", icon: "st.Lighting.light13", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'SPA', action: "spaLightToggle", icon: "st.Lighting.light11", backgroundColor: "#79b821", nextState: "off"
		}        
        standardTile("ALL LIGHTS", "device.ALL LIGHTS",   width: 2, height: 2, canChangeBackground: true) {
        	state "half", label: 'ALL LIGHTS', action: "allLightToggle", icon: "st.Lighting.light21", backgroundColor: "#F2F200", nextState: "off"
			state "off", label: 'ALL LIGHTS', action: "allLightToggle", icon: "st.Lighting.light21", backgroundColor: "#ffffff", nextState: "on", defaultState: true
			state "on", label: 'ALL LIGHTS', action: "allLightToggle", icon: "st.Lighting.light21", backgroundColor: "#79b821", nextState: "off", defaultState: false
		}  
        standardTile("HIGH SPEED", "device.HIGH SPEED", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'HIGH SPEED', action: "highspeedUnknown", icon: "st.thermostat.thermostat-right", backgroundColor: "#F2F200"
			state "off", label: 'HIGH SPEED', action: "highSpeedToggle", icon: "st.thermostat.thermostat-right", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'HIGH SPEED', action: "highSpeedToggle", icon: "st.thermostat.thermostat-right", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("CLEANER", "device.CLEANER", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'CLEANER', action: "cleanerUnknown", icon: "st.Weather.weather1", backgroundColor: "#F2F200"
			state "off", label: 'CLEANER', action: "cleanerToggle", icon: "st.Weather.weather1", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'CLEANER', action: "cleanerToggle", icon: "st.Weather.weather1", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("ALL PUMPS", "device.ALL PUMPS", width: 2, height: 2, canChangeBackground: true) {
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
        // Runs Pool filter & if phm > 0 then pool is heated
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
        
        // spaHeatMode/poolHeatMode phm/shm
        standardTile("poolHeatMode", "device.poolHeatMode", width:2, height: 2, canChangeBackground: true) {
            state "Off", label: '${name}', action: "poolModeToggle", backgroundColor: "#ffffff", nextState: "Heater", icon: "st.Health & Wellness.health2"
			state "Heater", label: '${name}', action: "poolModeToggle", backgroundColor: "#00a0dc", nextState: "Solar Pref", icon: "st.Health & Wellness.health2"
			state "Solar Pref", label: '${name}', action: "poolModeToggle", backgroundColor: "#e86d13", nextState: "Solar Only", icon: "st.Health & Wellness.health2"
			state "Solar Only", label: '${name}', action: "poolModeToggle", backgroundColor: "#f1d801", nextState: "Off", icon: "st.Health & Wellness.health2"
  
        }
        standardTile("spaHeatMode", "device.spaHeatMode", width:2, height: 2, canChangeBackground: true) {
            state "Off", label: '${currentValue}', action: "spaModeToggle", backgroundColor: "#ffffff", nextState: "Heater", icon: "st.Bath.bath4"
			state "Heater", label: '${currentValue}', action: "spaModeToggle", nextState: "Solar Pref", backgroundColor: "#00a0dc", icon: "st.Bath.bath4"
               //[value: "Heater", color: "#00a0dc"] ]
			state "Solar Pref", label: '${currentValue}', action: "spaModeToggle", backgroundColor: "#e86d13", nextState: "Solar Only", icon: "st.Bath.bath4"
			state "Solar Only", label: '${currentValue}', action: "spaModeToggle", backgroundColor: "#f1d801", nextState: "Off", icon: "st.Bath.bath4"
        }
        
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
                attributeState("spatemp", label:'${currentValue}°', unit:"dF", defaultState: true, icon: "st.Bath.bath4")
    		}
            // right up/down - controls for increasing or decreasing the temperature
   			tileAttribute("device.spatemperature", key: "VALUE_CONTROL", unit: "dF") {
        		attributeState("VALUE_UP", action: "spatempUp", unit: "dF", label: '${currentValue}°')
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
                attributeState("OFF", backgroundColor:"#00A0DC")
                attributeState("Heater", backgroundColor:"#00A0DC")
                attributeState("Solar Pref", backgroundColor:"#00A0DC")
                attributeState("Solar Only", backgroundColor:"#00A0DC")
                
    		}
            // bottom center - mode (i.e. Heat, Cool, or Auto)
    		tileAttribute("device.spaHeatModeBottom", key: "THERMOSTAT_MODE", label: '${name}') {
                attributeState("off", label:'${name}')
        		attributeState("heat", label: '${name}')
                attributeState("Solar pref", label:'${name}')
                attributeState("Solar only", label:'${name}')
    		}
            
		}  
        
        multiAttributeTile(name:"thermostatFullpool", type:"generic", width:6, height:4) {
        	// center
    		tileAttribute("device.poolTemp", key: "PRIMARY_CONTROL") {
        		attributeState("pooltemp", label:'${currentValue}°', unit:"dF", defaultState: true, icon: "st.Health & Wellness.health2",
                backgroundColor:"#00a0dc")
    		}
            
            // right up/down
            
   			tileAttribute("device.pooltemperature", key: "VALUE_CONTROL", unit: "dF") {
        		attributeState("VALUE_UP", action: "pooltempUp", unit: "dF", label: '${currentValue}°')
        		attributeState("VALUE_DOWN", action: "pooltempDown", unit: "dF")
    		}
            
            // lower left corner
            tileAttribute("device.pooltemperature", key: "SECONDARY_CONTROL") {
        		attributeState("pooltemperature", label:'Heat to ${currentValue}°', unit:"dF", defaultState: true)
    		}
            
            tileAttribute("device.pooltemperature", key: "SLIDER_CONTROL", range:"(0..105)") {
                attributeState("pooltemperature", action:"setRangedLevel", defaultState: true)
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
            
    		tileAttribute("device.poolHeatModeBottom", key: "THERMOSTAT_MODE", label:'${name}') {
                attributeState("off", label:'${name}')
        		attributeState("heat", label:'${name}')
                attributeState("Solar Pref", label:'${name}')
                attributeState("Solar Only", label:'${name}')
    		}
            
            
		} 
        
        controlTile("poolLevelSliderControl", "device.poolTemp4", "slider", height: 2, width: 2, range:"(20..105)", inactiveLabel: false, label: "hello") {
           state "pooltemperature", action:"poolSetRangedLevel"
        }
        controlTile("spaLevelSliderControl", "device.spaTemp4", "slider", height: 2, width: 2, range:"(20..105)") {
           state "spatemperature", action:"spaSetRangedLevel"
        }
        //////////////////////////////////////////////////         
        
        valueTile("scheduleTile", "device.scheduleTile", width: 6, height: 8, decoration: "flat", canChangeBackground: false) {
			state "schedule", label:'${currentValue}', defaultState: true
		}        
        valueTile("eggTimerTile", "device.eggTimerTile", width: 6, height: 6, decoration: "flat", canChangeBackground: false) {
			state "eggTimerTile", label:'${currentValue}', defaultState: true
		}  
      
        standardTile("addscheduleTile", "device.addschedule", width: 2, height:2, canChangeBackground: true) {
            state "unknown", label: 'add', action: "Unknown", backgroundColor: "#F2F200", icon: "st.custom.buttons.add-icon"
			state "add", label: '${currentValue}', action: "addSchedule", backgroundColor: "#ffffff", icon: "st.custom.buttons.add-icon", nextState: "activated", defaultState: true
            state "activated", label: '${currentValue}', action: "addSchedule", backgroundColor: "#cccccc",icon: "st.custom.buttons.add-icon", nextState: "add"         
 		}           
        valueTile("eggTimerSchedule", "device.eggTimerSchedule", width:4, height: 4, canChangeBackground: false) {
        	state "on", label: '${currentValue}', nextState: "off"
            state "off", label: '${currentValue}', nextState: "on"
        }        
        standardTile("delscheduleTile", "device.delschedule", width: 2, height: 2, canChangeBackground: true) {
            state "unknown", label: 'delete', action: "Unknown", backgroundColor: "#F2F200", icon: "st.custom.buttons.subtract-icon"
			state "delete", label: '${currentValue}', action: "delSchedule", backgroundColor: "#ffffff",icon: "st.custom.buttons.subtract-icon", nextState: "activated", defaultState: true
		    state "activated", label: '${currentValue}', action: "delSchedule", backgroundColor: "#cccccc",icon: "st.custom.buttons.subtract-icon", nextState: "delete"
        }   
        
        main(["poolTemp"])

        details([
        "poolCntr", "timedate",
        "config", "refresh", "freeze", 
        "airTemp", "poolTemp", "spaTemp",
        
        "ALL LIGHTS", "POOL LIGHT", "SPA LIGHT",
        
        "spaspace","spaHeatMode",      
        
        "spaLevelSliderControl","spaTemp4","SPA", 
        
        "ALL PUMPS", "HIGH SPEED", "CLEANER", 
        "SPILLWAY", "AIR BLOWER", "POOL", 
        
        "poolspace","poolHeatMode",
        
        "poolLevelSliderControl","poolTemp4","POOLHEAT", 
        
        "Pump 1","Pump 2",        
        "eggTimerSchedule","addscheduleTile","delscheduleTile",
        "eggTimerTile",
        "scheduleTile", 
        "config", "refresh", "debug"
 		])
	}

}

// parse routines

def parse(String description) {   
	log.debug "------------- Parse -------------"         
    if (state.DBG == "CONFIG") state.DEBUG = state.DEBUG = 0
    
    def json = parseLanMessage(description).data
    if (!json) {
       logerror "parsed lan message was nil"
       def evt = createEvent(name: "refresh", isStateChange: "true", value: "Idle")
       return evt
    } else {
        loginfo "json = ${json}"
        logdebug("parse - config = ${json.config}")
        logdebug("parse - circuit = ${json.circuit}")
    	//state.includeSolar = mesg.config.equipment.solar.installed == 1
    	//state.includeChem = mesg.config.equipment.intellichem.installed == 1
    	//state.includeChlor = mesg.config.equipment.chlorinator.installed == 1
    	//state.includeSpa = mesg.config.equipment.spa.installed == 1       
    	//state.controller = mesg.config.equipment.controller
    	//state.circuitHudeAux = mesg.config.equipment.circuit.hideAux
    	//state.numCircuits =  mesg.config.equipment.circuit.nonLightCircuit.size() + mesg.config.equipment.circuit.lightCircuit.size()
    	//state.nonLightCircuits = mesg.config.equipment.circuit.nonLightCircuit
    	//state.lightCircuits = mesg.config.equipment.circuit.lightCircuit
    	state.pumps = json.pump        
    	state.circuitData = json.circuit
        state.temperature = json.temperature
        state.time = json.time
    	state.config = true
        state.uom = json.UOM.UOMStr

        processTemperatures(state.temperature)
        processCircuits(state.circuitData)
        processTime(state.time)
        processPumps(state.pumps)
        processSchedule(json.schedule)
        processConfig(json.config)
    }
    state.DEBUG = 0
} 

def parseAction(physicalgraph.device.HubResponse hubResponse) {
   log.debug "------------- parseAction -------------"  
   if (state.DBG == "ACTION") state.DEBUG = 1
   def json = hubResponse.json
   logdebug "json = ${json}"
   
   if (json) {
      loginfo "processing text"
      logwarn "text: ${json.text}"
      logwarn "status: ${json.status}"
      logwarn "value: ${json.value}"
              
      if (json.text.contains("POOL LIGHT")) { 
          loginfo "status = POOL LIGHT ${json.status}"
          sendEvent(name: "POOL LIGHT", value: "${json.status}")
                  
      } else if (json.text.contains("SPA LIGHT")) {
          loginfo "status = SPA LIGHT ${json.status}"
          sendEvent(name: "SPA LIGHT", value: "${json.status}")
                  
      } else if (json.text.contains("CLEANER")) {
          log.info "STATUS = CLEANER ${json.status}"
          sendEvent(name: "CLEANER", value: "${json.status}")
                  
      } else if (json.text.contains("HIGH SPEED")) {
          log.info "STATUS = HIGH SPEED ${json.status}"
          sendEvent(name: "HIGH SPEED", value: "${json.status}")
                                   
      } else if (json.text.contains("SPILLWAY")) {
          loginfo "status = SPILLWAY ${json.status}"
          sendEvent(name: "SPILLWAY", value: "${json.status}")
                 
      } else if ((json.text.contains("Request to set spa heat mode to") || json.text.contains("User request to update spa heat set point")) || json.text.contains("Request to set spa heat setpoint")) {    //|| json.text.contains("set spa heat setpoint")) { 
          loginfo "spa heat modified"
          state.shm = json.value
          state.shmStr = json.status
          state.spaSetPoint = json.value

          sendEvent(name: "spaHeatMode", value: "${state.shmStr}")
          sendEvent(name: "spaHeatModeBottom", value: "${state.shmStr}")
          sendEvent(name: "device.spatemperature", value: "${state.spaSetPoint}")
                         
      } else if ((json.text.contains("Request to set pool heat mode to") || json.text.contains("User request to update pool heat set point")) || json.text.contains("Request to set pool heat setpoint")) {   //|| json.text.contains("set pool heat setpoint")) {
          log.info "pool heat modified"
          state.phm = json.value
          state.phmStr = json.status
          state.poolSetPoint = json.value
            
          sendEvent(name: "poolHeatMode", value: "${state.phmStr}")
          sendEvent(name: "poolHeatModeBottom", value: "${state.phmStr}")
          sendEvent(name: "device.pooltemperature", value: "${state.poolSetPoint}")  
          
       } else if (json.text.contains("toggle SPA to")) {
          loginfo "SPA toggle"
          def spaheatvalue = json.value?.is(0) ? "idle" : "heating"
           
          if (spaheatvalue == "idle") {
             sendEvent(name: "spathermostatOperatingState", value: "${spaheatvalue}")     
          } else {  
             sendEvent(name: "spathermostatOperatingState", value: "${state.spaTemp}° ${spaheatvalue}") 
          }
                  
       } else if (json.text.contains("toggle POOL to")) {
           loginfo "POOL toggle"
           sendEvent(name: "POOL", value: "${json.status}")  
           
       } else if (json.text.contains("BLOWER")) {
           loginfo "status = BLOWER ${json.status}"
           sendEvent(name: "AIR BLOWER", value: "${json.status}")
       }
   }
   state.DEBUG = 0
   setAllPumps()
   setAllLights()
}

def parseDevice(physicalgraph.device.HubResponse hubResponse) {
    log.debug "------------- parseDevice -------------"  
    if (state.DBG == "CONFIG") state.DEBUG = 1

    def msg = hubResponse.xml
    logdebug "msg = ${msg}"
    if (msg) {
        loginfo "processing xml"

        def body = msg
        loginfo "body = ${body}"
        if (!body) {
            logerror "body was nil, returning"
            return null
        }    
        def verMajor = body.specVersion.major
        def verMinor = body.specVersion.minor
        def verPatch = body.specVersion.patch
        def manufacturer = body.device.manufacturer
        def modelDescription = body.device.modelDescription
        def friendlyName = body.device.friendlyName

        def dbug = state.debug?.is(true) ? "on" : "off"

        state.version = "${friendlyName} Version\n${verMajor}.${verMinor}.${verPatch}\n Debug ${dbug}" 
        state.manufacturer = "By ${manufacturer}\n${modelDescription}"
        logdebug "version = ${state.version}"
        logdebug "manufacturer = ${state.manufacturer}"
      
        sendEvent(name: "refresh", isStateChange: "true", value: "Idle")
        sendEvent(name: "poolCntr", value: state.version, isStateChange: "true")
    }  
    state.DEBUG = 0
}

def parseSchedule(physicalgraph.device.HubResponse hubResponse) {
    log.debug "------------- parseSchedule -------------"   
    if (state.DBG == "SCHEDULE") state.DEBUG = 1
      
    def json = hubResponse.json
    logdebug "parseSchedule json = ${json}"
    if (!json) {
       logerror "parsed lan message was nil"
       def evt = createEvent(name: "refresh", isStateChange: "true", value: "Idle")
       return evt
    } 
    processSchedule(json.schedule)
    state.DEBUG = 0
}

def parseCircuit(physicalgraph.device.HubResponse hubResponse) {
    log.debug "------------- parseCircuit -------------"  
    if (state.DBG == "CIRCUITS") state.DEBUG = 1
      
    def json = hubResponse.json
    logdebug "parseCircuit json = ${json}"
    if (!json) {
       logerror "parsed lan message was nil"
       def evt = createEvent(name: "refresh", isStateChange: "true", value: "Idle")
       return evt
    } 
    processCircuits(json.circuit)
    state.DEBUG = 0
}

// process routines

def processTemperatures(temperatures) {
    log.info "processing temperature"  
    if (state.DBG == "TEMPERATURES") state.DEBUG = 1

    loginfo "temperatures = ${temperatures}"
    if (state.DEBUG) {
    	temperatures.keySet().each {
            def item = temperatures[it]
        	logwarn "${it} =  ${item}"
        }
    }    

    loginfo "state.SPAstate = ${state.SPAstate}"
    loginfo "state.POOLstate = ${state.POOLstate}"
    
    def poolTemp = state.SPAstate?.is(1) ? temperatures.poolLastKnownTemperature : temperatures.poolTemp
    def spaTemp = state.POOLstate?.is(1) ? temperatures.spaLastKnownTemperature : temperatures.spaTemp
 
    state.spaTemp = spaTemp
    state.poolTemp = poolTemp
    def airTemp = temperatures.airTemp
    def freeze = temperatures.freeze?.is(1) ? "on" : "off"
              
    //pool
    state.poolSetPoint = temperatures.poolSetPoint
    state.phmStr = temperatures.poolHeatModeStr  
    state.phm = temperatures.poolHeatMode
              
    //spa
    state.spaSetPoint = temperatures.spaSetPoint
    state.shmStr = temperatures.spaHeatModeStr
    state.shm = temperatures.spaHeatMode
    
    loginfo "state.phm = ${state.phm}"
    loginfo "state.shm = ${state.shm}"
 
    //sendEvents for Temperature 
    sendEvent(name: "poolTemp", value: "${poolTemp}")
    sendEvent(name: "spaTemp", value: "${spaTemp}")
    sendEvent(name: "airTemp", value: "${airTemp}")
    sendEvent(name: "freeze", value: "${freeze}")
              
    //sendEvents for Spa
    if (state.shm > 0)
       sendEvent(name: "spaHeatMode", value: "${state.shmStr}")
    sendEvent(name: "spaHeatModeBottom", value: "${state.shmStr}")
    sendEvent(name: "spatemperature", value: "${state.spaSetPoint}")
          
    //sendEvents for Pool
    if (state.phm > 0)
       sendEvent(name: "poolHeatMode", value: "${state.phmStr}")
    sendEvent(name: "poolHeatModeBottom", value: "${state.phmStr}")
    sendEvent(name: "pooltemperature", value: "${state.poolSetPoint}")
    state.DEBUG = 0
}   

def processCircuits(circuits) {
    log.info "processing circuit" 
    if (state.DBG == "CIRCUITS") state.DEBUG = 1

    loginfo "circuits = ${circuits}"           
              
    circuits.keySet().each {
       def c = circuits[it]
       def String cname = c.name
       if (!cname.contains('FEATURE') && !cname.contains("NOT USED") && !cname.contains("AUX")) {
          state.circuit[cname] = c.number
       }    
             
       state.circuit.keySet().each {
          def num = state.circuit[it]
          def status = circuits."${num}".status
          logdebug "${it}: ciruit ${num} status = ${status}"
          sendEvent(name: "${it}", value: status?.is(0) ? "off" : "on")
          if (it == "SPA") {
             logdebug "it = SPA"
             if ((state.shm > 0) && (status == 1)) {
                sendEvent(name: "spathermostatOperatingState", value: "${state.spaTemp}° heating")
                state.SPAstate = 1
             } else {
                sendEvent(name: "spathermostatOperatingState", value: "idle")
                state.SPAstate = 0
             }     
                            
           }
           if (it == "POOL") {
              logdebug "it = POOL"                     
              if ((state.phm > 0) && (status == 1)) {
                  sendEvent(name: "poolthermostatOperatingState", value: "${state.poolTemp}° heating")   
                  state.POOLstate = 1
              } else {
                  sendEvent(name: "poolthermostatOperatingState", value: "idle")
                  state.POOLstate = 0
              }      
           }       
         }
         
    }
    state.DEBUG = 0
    setAllPumps()
    setAllLights()
}
 
def processTime(time) {
    log.info "processing time"
    if (state.DBG == "TIME") state.DEBUG = 1

    loginfo "time = ${time}"

    def controllerTime = time.controllerTime
    def controllerDate = time.controllerDateStr
    def controllerDoW = time.controllerDayOfWeekStr
    logdebug "time : ${time} - \n DoW:${controllerDoW}\n Date:${controllerDate}\n controllerTime:${controllerTime}\n state:${state.uom}"
    sendEvent(name: "timedate", value: "${controllerDoW}\n${controllerDate}\n${controllerTime}\n("+state.uom+")")
    state.DEBUG = 0
}

def processPumps(pumps) {
    loginfo "processing pump"
    if (state.DBG == "PUMPS") state.DEBUG = 1

    loginfo "pumps = ${pumps}"
    pumps.keySet().each {      
       def pump = pumps[it]
       logdebug "pump ${it} = ${pump}"           
       logwarn "Pump name = " + pump.name
       logwarn "friendlyName = " + pump.friendlyName
       sendEvent(name: "Pump ${it}", value: "${pump.friendlyName}\n---Pump---\nWatts :${pump.watts} \nRPM :${pump.rpm} \nError :${pump.err}\nState :${pump.drivestate}\nMode :${pump.run}")
    }
    setAllPumps()
    state.DEBUG = 0
}

def processConfig(config) {
    log.info "processing config"
    if (state.DBG == "CONFIG") state.DEBUG = 1
    logdebug "config = ${config}"
    
    sendEvent(name: "config", value: config.systemReady?.is(1) ? "on" : "off", descriptionText: "System Status is ${config.systemReady?.is(1) ? 'on' : 'off'}")
    state.DEBUG = 0
}

def processSchedule(schedule) { 
    log.info "processing schedule"
    if (state.DBG == "SCHEDULE") state.DEBUG = 1

    logdebug "schedule = ${schedule}"
    def fullSchedule = "----- SCHEDULE -----\n"
    //fullSchedule = fullSchedule + "#      Circuit     StartTime     EndTime\n"  
    fullSchedule = fullSchedule + "#      Circuit     StartTime     EndTime\n"  
    fullSchedule = fullSchedule + "________________________________________\n"
              
    def eggSchedule = "----- EGG TIMER -----\n"
    eggSchedule = eggSchedule + "   #         Circuit        Duration\n"
    eggSchedule =   eggSchedule + "________________________________________\n"  
            
    def int circuitSize = 0
    def space = ""
    def int i
    def active = 0
    def ison = ""
    def int ID
    def CIRCUIT
    def CN
    def MODE
    def bytes
    def DURATION
    def START_TIME
    def END_TIME
    def DAYS
    def schmap = [:]
    
    schedule.keySet().each {
        space = ""
        def event = schedule[it]
        loginfo "event = ${event}"
        if (event.circuit) {
           ID = event.id
           CIRCUIT = event.circuit
           CN = event.circuitNum
           MODE = event.mode
           bytes = event.bytes     
           DURATION = event.duration
           START_TIME = event.startTime.time24
           END_TIME = event.endTime.time24
           DAYS = event.days
        } else {
           ID = event.ID     
           CIRCUIT = event.CIRCUIT
           CN = event.CIRCUITNUM
           MODE = event.MODE
           bytes = event.BYTES  
           DURATION = event.DURATION
           START_TIME = event.START_TIME
           END_TIME = event.END_TIME
           DAYS = event.DAYS
       }
        logdebug "V6 - it:${it}-ID:${ID} CIRCUIT:${CN}-${CIRCUIT} MODE:${MODE} BYTES:${bytes}\n ----- DURATION:${DURATION} DAYS:${DAYS} START_TIME:${START_TIME} END_TIME:${END_TIME}"
            
        if (MODE == "Egg Timer") {
           circuitSize = 16 - CIRCUIT.size()
           for (i = 0; i <circuitSize; i++) {
                space = space + " "
           }
           if (CIRCUIT == "SPA") 
              space = space + " "
           eggSchedule = eggSchedule + "${ID}${space}${CIRCUIT}${space}${DURATION}\n"
        } else if (MODE == "Schedule") {
           if (CIRCUIT != "NOT USED") {
              circuitSize = 16 - CIRCUIT.size()
              for (i = 0; i <circuitSize; i++) {
                  space = space + " "
              }
              logdebug "START_TIME = ${START_TIME}"
              logdebug "END_TIME = ${END_TIME}"
              
              ison = ""
              def between = timeOfDayIsBetween(START_TIME, END_TIME, new Date(), location.timeZone) 
              def circuit_status = device.currentState(CIRCUIT)?.value
              logdebug "circuit_status = ${circuit_status}"
    		  if (between && circuit_status == "on") {
                 logdebug "current time is between ${START_TIME} and ${END_TIME} for ID ${ID} circuit ${CIRCUIT}"
                 ison = "*"
                 active = active + 1
              }
         
              def day_list = DAYS.split(" ")
              def days = []
              day_list.each {
                  days << it.substring(0,3)
              }  
              loginfo "----------- ID ${ID} -------------\n ${event}"
              schmap.put(ID,"${ison}${ID}${space}${CIRCUIT}${space}${START_TIME}${space}${END_TIME}\nDAYS:${days}\n\n")
            }
         }
  	}	
    logwarn "schmap = ${schmap}"
    
    for (i = 1; i < 13; i++) {
        if (schmap[i])
    	   fullSchedule = fullSchedule + schmap[i]   
    }
    
    logwarn "fullSchedule = ${fullSchedule}"
    
    fullSchedule = fullSchedule + "* ${active} active schedule(s)"
    loginfo "fullschedule = ${fullSchedule}"
    sendEvent(name: "scheduleTile", value: "${fullSchedule}")
    sendEvent(name: "eggTimerTile", value: "${eggSchedule}")
    state.DEBUG = 0
}

// sendCommand : sends commands to controller

def sendCommandCallBack(command, callBack) {
    log.warn "In sendCommandCallBack"
    def userpass = encodeCredentials(username, password)
    def headers = getHeader(userpass)
    def dni = setDeviceNetworkId("${controllerIP}","${controllerPort}")

    def params = [
        method: "GET",
		path: command,
        headers: headers,
        dni: [dni]       
    ]    
    
    def opts = [
        callback : callBack,
        type: 'LAN_TYPE_CLIENT'
    ]
    
    logwarn "sendCommand command: ${command}\npoolCommand =\n${params}"
    try {
       sendHubCommand(new physicalgraph.device.HubAction(params, null, opts))
       logwarn "SENT: $params $opts callback : ${callBack}"
    } catch (e) {
       log.error "something went wrong: $e"
    }
}

def sendCommand(command) {
    def userpass = encodeCredentials(username, password)
    def headers = getHeader(userpass)
    def dni = setDeviceNetworkId("${controllerIP}","${controllerPort}")

    def params = [
        method: "GET",
		path: command,
        headers: headers,
        dni: [dni]
    ]    
    
    loginfo "sendCommand command: ${command}\npoolCommand =\n${params}"
    try {
       sendHubCommand(new physicalgraph.device.HubAction(params))
       logdebug "SENT: $params"
    } catch (e) {
       log.error "something went wrong: $e"
    }
    
}

// log routines

def loginfo(msg) {
    if (state.DEBUG) log.info msg    
}

def logdebug(msg) {
    if (state.DEBUG) log.debug msg
}

def logwarn(msg) {
    if (state.DEBUG) log.warn msg
}

def logerror(msg) {
    if (state.DEBUG) log.error msg
}

// Init routines

def buildCircuit() {
   sendCommandCallBack("/device",'parseDevice')
   state.circuit = [:]
   sendCommandCallBack("/circuit",'parseCircuit')
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
}

def initialize() {
	runEvery1Minute(refresh)
    
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
    state.debug = false  
    
    state.SPAstate = 0
    state.POOLstate = 0

    sendEvent(name:"debug", value: state.debug?.is(true) ? "on" : "off", isStateChange: "true")
    
    state.eggTimerORschedule = eggTimerORschedule

    state.controllerTime = controllerTime
    state.controllerDay = controllerDay
    state.controllerMonth = controllerMonth
    state.controllerDoW = controllerDoW
    state.version = "${name} Version\n${verMajor}.${verMinor}.${verPatch}\n Debug ${state.debug}"

    buildCircuit()
}

def refresh() {
    // this runs every minute
    logwarn "Requested a refresh"
    sendEvent(name: "config", value:"unknown", descriptionText: "System Status is unknown");
    sendCommand("/all")   
    sendCommandCallBack("/device",'parseDevice')
    updateSchedule()
}

def set_debug() {
    def verStr = state.version
    logerror "verStr = ${verStr}"
    def curr = state.debug
    state.debug = !state.debug 
    def newVerStr = verStr.replaceAll("is ${curr?.is(true) ? "on" : "off"}","is ${state.debug?.is(true) ? "on" : "off"}")
    logerror "newVerStr = ${newVerStr}"
    state.version = newVerStr
    
    def dbug = state.debug?.is(true) ? "on" : "off"
    sendEvent(name:"debug", value: dbug, isStateChange: "true")
    state.Cntr = true
    sendEvent(name:"poolCntr", value: state.version, isStateChange: "true")    
}

def updated() {
    log.info "######### UPDATED #########" 
    state.DBG = DBG
    loginfo "DBG = ${DBG}"
    loginfo "state.DBG = ${state.DBG}"
    if (DBG == "ALL") state.DEBUG = 1
    initialize()   
    sendEvent(name:"poolCntr", value: state.version, isStateChange: "true")
    refresh()
}
     
def installed() {
    loginfo "########## installed ###########"
    initialize()
    setDeviceNetworkId("${controllerIP}","${controllerPort}")  
}

// Schedule/Egg timer routines

def get_hour(time) {
   return time.format('HH')
}

def get_minute(time) {
   return time.format('mm')
}

def build_dow() {
    logdebug "state.dayMap = ${state.dayMap}"
       
    def dow = []
    state.dayMap.keySet().each {
       if (state.dayMap[it]) dow << it
    }   
    return (dow)
}

def calc_dow() {      
    def calc = 0
    state.dayMap.keySet().each {
       logwarn "it = ${it}"
       if (state.dayMap[it]) {        
          calc = calc + state.dayValueMap[it]
       }   
    }   
    logdebug "calc = ${calc}"    
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
        sendEvent(name: "eggTimerSchedule", value: "Egg Timer\nID:${sch_id? sch_id : "Not Set"} Name:${sch_circuit? sch_circuit : "Not Set"}\nDuration:${egg_hour? egg_hour : "Not Set"}:${egg_min}", isStateChange: "true")
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
        sendEvent(name: "eggTimerSchedule", value: "Schedule\nID:${sch_id? sch_id : "Not Set"} Name:${sch_circuit? sch_circuit : "Not Set"}\n${times}\n\n${dow}", isStateChange: "true")
    }
    else 
        sendEvent(name: "eggTimerSchedule", value: "Schedule or Egg Timer not set")


}

def addSchedule() {
    loginfo "In addSchedule"
    loginfo "state.eggTimerORschedule = ${state.eggTimerORschedule}"
    if (!sch_circuit || !sch_id) {
       sendEvent(name: "addschedule", isStateChange: "true", value: "add", descriptionText: "addschedule was updated")
       return
    }   
    def sch_circuit_num = state.circuit[sch_circuit]
    if (state.eggTimerORschedule == "Egg Timer") {
       sendCommandCallBack("/eggtimer/set/id/${sch_id}/circuit/${sch_circuit_num}/hour/${egg_hour}/min/${egg_min}",'parseAction')
    } else if (state.eggTimerORschedule == "Schedule") {
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
       loginfo "start time: ${sch_starthh}, ${sch_startmm}"
       loginfo "end time: ${sch_endhh}, ${sch_endmm}"
       def sch_dow_num = calc_dow()
        
       // ----- setFeature : query = /schedule/set/7/3/21/00/22/00/127 -------
       // Response: REST API received request to set schedule 7 with values (start) 21:0 (end) 22:0 with days value 127
       // Response: REST API received request to set circuit on schedule with ID (7) to POOL LIGHT
 
       sendCommandCallBack("/schedule/set/id/${sch_id}/circuit/${sch_circuit_num}",'parseAction')
       loginfo "scheduleCircuit = ${scheduleCircuit}"
       loginfo "schedule set id:${sch_id} to circuit:${sch_circuit_num}"    
             
       sendCommandCallBack("/schedule/set/${sch_id}/${sch_circuit_num}/${sch_starthh}/${sch_startmm}/${sch_endhh}/${sch_endmm}/${sch_dow_num}",'parseAction')
       loginfo "schedule set id:${sch_id} to circuit:${sch_circuit_num} for time: start hour:${sch_starthh} start min:${sch_startmm} end hour:${sch_endhh} end min:${sch_endmm} day of the week number:${sch_dow_num}"
       loginfo "addsch = ${addsch}"
    }
    sendEvent(name: "addschedule", isStateChange: "true", value: "add", descriptionText: "addsechedule was updated")
    sendCommandCallBack("/schedule",'parseSchedule')
}

def delSchedule() {
   // app.get('/schedule/delete/id/:id'
   // 'REST API received request to delete schedule or egg timer with ID:' + id;
   if (!sch_id) {
       sendEvent(name: "delschedule", isStateChange: "true", value: "delete", descriptionText: "delschedule was updated")
       return
   }    
   sendCommandCallBack("/schedule/delete/id/${sch_id}",'parseAction') 
   sendEvent(name: "delschedule", isStateChange: "true", value: "delete", descriptionText: "delschedule was updated")
   sendCommandCallBack("/schedule",'parseSchedule')
}

// Temperature routines

def getHeatMode(mode) {
    def num = 0
    loginfo "mode = ${mode}"
    switch (mode) {
       case "OFF":
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
    sendCommandCallBack("/poolheat/setpoint/${value}",'parseAction')
}

def setSpaHeatPoint(value) {
    sendCommandCallBack("/spaheat/setpoint/${value}",'parseAction')
}

def setPoint(num) {
    sendCommandCallBack("/spaheat/setpoint/${num}",'parseAction')
}

def changeSpaTemp(direction) {
    def ts = device.currentState("spatemperature")
    def value = direction?.is("Up") ? (ts ? ts.integerValue + 1 : 72) : (ts ? ts.integerValue - 1 : 72)
    spaalterSetpoint(value)
    logwarn "ts = ${ts.integerValue} : value = ${value}"
    
    sendEvent(name: "spatemperature", value: "${value}")
    sendEvent(name: "spa${direction}", isStateChange: "true")
}

def changePoolTemp(direction) {
    def ts = device.currentState("pooltemperature")
    def value = direction?.is("Up") ? (ts ? ts.integerValue + 1 : 72) : (ts ? ts.integerValue - 1 : 72)
    poolalterSetpoint(value)

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

def poolSetRangedLevel(value) {
	logdebug "setting POOL ranged level to $value"
    poolalterSetpoint(value)
	sendEvent(name:"poolTemp4", value:value)
} 

def spaSetRangedLevel(value) {
	log.debug "setting SPA ranged level to $value"
    spaalterSetpoint(value)
	sendEvent(name:"spaTemp4", value:value)
} 

// Set Controller Date Time

def setdatetime() {
// datetime/set/time/{hour}/{min}/{dow}/{day}/{mon}/{year}/{dst}
// set the schedule on the controller for the particular schedule ID. 
// dow= day of week as expressed as [0=Sunday, 1=Monday, 2=Tuesday, 4=Wednesday, 8=Thursday, 16=Friday, 32=Saturday] 
// or a combination thereof [3=Monday+Tuesday]. To set a schedule set a valid start and end time (hh:mm). 
// To set an egg timer, set the start time to 25:00 and the endtime to the duration (hh:mm) you want the egg timer to run.
//"text": "FAIL: SOCKET API - hour (NaN) should be 0-23 and minute (NaN) should be 0-59. 
// Received: NaN:NaNDay (NaN) should be 0-31, month (NaN) should be 0-12 and year (NaN) should be 0-99.
// Day of week (NaN) should be one of: [1,2,4,8,16,32,64] [Sunday->Saturday]dst (0) should be 0 or 1" }
    log.info "------- In setdatetime -------"
    if ((!state.controllerTime) || (!state.controllerDay) || (!state.controllerMonth) || (!state.controllerDoW)) {
       log.info "missing something in setdatetime returning"
       return
    }
    
    loginfo "controllerTime = ${controllerTime}"
    Date newDate = Date.parse("yyyy-MM-dd'T'HH:mm",controllerTime)  
    loginfo "Date = ${newDate}"
    
    String year = newDate.format('yy')
    
    //String month = newDate.format('MM')
    String month = state.Month[controllerMonth]
    //month = 11
    loginfo "month = ${month}"
    
    String day = controllerDay
    //String day = newDate.format('dd')
    loginfo "day = ${day}"
    //day = 8
    
    String hour = newDate.format('HH')
    String minute = newDate.format('mm')
    String DayOfWeek = controllerDoW
    //String DayOfWeek = newDate.format('EE')
    loginfo "DayOfWeek = ${DayOfWeek}"
    //DayOfWeek = "Sat"
    

    def downum = state.dayValueMap[DayOfWeek]
    sendCommandCallBack("/datetime/set/time/${hour}/${minute}/date/${downum}/${day}/${month}/${year}/0",'parseAction')
}

// Display Controller version

def displayCntr() {
    logwarn "state.Cntr = ${state.Cntr}"
    state.Cntr = !state.Cntr
    if (state.Cntr) {
        sendEvent(name:"poolCntr", value: state.version, isStateChange: "true")
    } else {  
        sendEvent(name:"poolCntr", value: state.manufacturer, isStateChange: "true")  
    }   
}

// Set ALL Pumps/All Lights

def setAllPumps() {
    def highspeed = device.currentState("HIGH SPEED")?.value
    def cleaner = device.currentState("CLEANER")?.value
    logerror "highspeed = ${highspeed}"
    logerror "cleaner = ${cleaner}"
    if (highspeed == "on" && cleaner == "on")
       sendEvent(name: "ALL PUMPS", value: "on")
    else    
       sendEvent(name: "ALL PUMPS", value: "off")
}  

def setAllLights() {
    def poolLight = device.currentState("POOL LIGHT")?.value
    def spaLight = device.currentState("SPA LIGHT")?.value
    logerror "poolLight = ${poolLight}"
    logerror "spaLight = ${spaLight}"
    if (poolLight == "on" && spaLight == "on")
       sendEvent(name: "ALL LIGHTS", value: "on")
    else    
       sendEvent(name: "ALL LIGHTS", value: "off")
}
    
// Toggle routines

def spaModeToggle() {
    def num = ModeToggle(state.shmStr)
    sendCommandCallBack("/spaheat/mode/${num}",'parseAction')
}

def poolModeToggle() {  
    def num = ModeToggle(state.phmStr)
    sendCommandCallBack("/poolheat/mode/${num}",'parseAction')
}

def ModeToggle(mode) {
	loginfo "--------- ${mode} Toggle ----------"
    loginfo "mode = ${mode}" 

    def num   
    switch (mode) {
    	case "OFF":
            num = "1"
        	break
        case "Off":  
            num = "1"
        	break    
    	case "Heater":
            num = "2"
        	break
        case "Solar Pref":
            num = "3"
        	break
        case "Solar Only":
            num = "0"
        	break
        default:
            num = "1"
    }        
        
    return num
}

def Toggle(device) {
    def num = state.circuit[device]
    sendCommandCallBack("/circuit/${num}/toggle/",'parseAction')
}

def spaToggle() {
    // turns spa heater on/off
	logwarn "Executing 'spaToggle'"
    Toggle('SPA')
}

def blowerToggle() {
	logwarn "Executing 'blowerToggle'"  
    Toggle('AIR BLOWER')
}

def poolLightToggle() {
	logwarn "Executing 'poolLightToggle'"
    Toggle('POOL LIGHT')
}

def allLightToggle() {
	logwarn "Executing 'allLightToggle'"
    def allLightstate = device.currentValue("ALL LIGHTS")

    def value 
    
    if (allLightstate == "on")
        value = 0
    else
        value = 1
    
    def poolnum = state.circuit["POOL LIGHT"]
    def spanum = state.circuit["SPA LIGHT"]    
    sendCommandCallBack("/circuit/${poolnum}/set/${value}",'parseAction')
    sendCommandCallBack("/circuit/${spanum}/set/${value}",'parseAction')
}

def spaLightToggle() {
	logwarn "Executing 'spaLightToogle'"
    Toggle('SPA LIGHT')
}

def cleanerToggle() {
	logwarn "Executing 'cleanerToogle'"
    Toggle('CLEANER')
}

def allPumpsToggle() {
	log.warn "Executing 'allPumpsToogle'"
    def allpumpstate = device.currentValue("ALL PUMPS")

    def value 
    log.debug "allpumpstate = ${allpumpstate}"
    
    if (allpumpstate == "on")
        value = 0
    else
        value = 1
    
    log.debug = "value = ${value}"
    
    def highspeednum = state.circuit["HIGH SPEED"]
    def cleanernum = state.circuit["CLEANER"]    
    
    sendCommandCallBack("/circuit/${highspeednum}/set/${value}",'parseAction')
    sendCommandCallBack("/circuit/${cleanernum}/set/${value}",'parseAction')
}

def cleanWayToggle() {
	log.warn "Executing 'cleanWayToogle'"

    Toggle('CLEANER')
    Toggle('SPILLWAY')
}

def poolToggle() {
	logwarn "Executing 'poolToogle'"
    Toggle('POOL')
}

def highSpeedToggle() {
	logwarn "Executing 'highSpeedToggle'"
    Toggle('HIGH SPEED')
}

def spillWayToggle() {
	logwarn "Executing 'spillWayToogle'"
    Toggle('SPILLWAY')
}

def poolHeatToggle() {
    logwarn "Executing 'poolHeatToogle'"
    Toggle('POOL')
} 

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
    headers.put("Accept","application/json")
    return headers
}