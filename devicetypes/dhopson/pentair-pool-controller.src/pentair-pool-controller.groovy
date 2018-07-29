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
 
//import groovy.json.JsonSlurper
//import groovy.util.XmlSlurper
 
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
        command "poolLightToggle"
        command "spaLightToggle"
        command "allLightToggle"
		command "blowerToggle"
		command "cleanerToggle"
        command "allPumpsToggle"
        command "highspeedToggle"
        command "spillWayToggle"	   
        command "tempUp"
		command "tempDown"
        command "setdatetime"
        command "addSchedule"
        command "delSchedule"
        command "displayCntr"
        command "Toggle" ,["string"]
        //command "setuom"
        attribute "POOL LIGHT", "enum", ["on", "off"]
        attribute "SPA LIGHT", "enum", ["on", "off"]
	}
   
    preferences {
       	section("Select your controller") {
       		input "controllerIP", "text", title: "Controller hostname/IP", required: true, displayDuringSetup: true
       		input "controllerPort", "port", title: "Controller port", required: true, defaultValue: "3000", displayDuringSetup: true
            input "username", "string", title:"Username", description: "username", required: true, displayDuringSetup: true
            input "password", "password", title:"Password", description: "Password", required: true, displayDuringSetup: true
            //input name: "debug", type: "bool", title: "Enable debug?", description: "Debug", required: false
            //input name: "debug_pump", type: "bool", title: "Enable pump debug?", description: "Debug pump", required: false
            }
            
         section("schedules") {   
            input name: "controllerTime", type: "time", title: "System Time", descripiton: "Enter Time, then select time tile to update", required: false
            input name: "eggTimerORschedule", type: "bool", title: "Is this an egg timer?", description: "Egg timer and not a Schedule?", required: false
            input name: "sch_circuit", type: "enum", title: "Circuit Name", description: "Which circuit should be used for this schedule", required: false, options:[
                    "SPA",
        			"AIR BLOWER",
        			"POOL LIGHT",
        			"SPA LIGHT",
        			"CLEANER",
        			"POOL",
        			"HIGH SPEED",
        			"SPILLWAY"]
            input name: "sch_id", type: "enum", title: "Schedule ID", description: "ID is used for both schedule/eggtimer addition and deletion", required: false, options:[
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
            input name: "egg_hour", type: "number", title: "Egg timer hour", description: "Number of hours for egg timer", range: "0..12", required: false
            input name: "egg_min", type: "number", title: "Egg timer min", description: "Number of minutes for egg timer", range: "0..59", required: false        
            input name: "sch_start", type: "time", title: "Schedule Start Time", descripiton: "Start Time for schedule", required: false
            input name: "sch_end",   type: "time", title: "Schedule End Time",   descripiton: "End Time for schedule",   required: false             
            input name: "Monday", type: "bool", title: "Schedule Monday?", description: "Should the task be scheduled for this day?", required: false 
            input name: "Tuesday", type: "bool", title: "Schedule Tuesday?", description: "Should the task be scheduled for this day?", required: false
            input name: "Wednesday", type: "bool", title: "Schedule Wednesday?", description: "Should the task be scheduled for this day?", required: false
            input name: "Thursday", type: "bool", title: "Schedule Thursday?", description: "Should the task be scheduled for this day?", required: false
            input name: "Friday", type: "bool", title: "Schedule Friday?", description: "Should the task be scheduled for this day?", required: false
            input name: "Saturday", type: "bool", title: "Schedule Saturday?", description: "Should the task be scheduled for this day?", required: false
            input name: "Sunday", type: "bool", title: "Schedule Sunday?", description: "Should the task be scheduled for this day?", required: false 
            input name: "adebug", type: "enum", title: "Debug", description: "Which circuits should be debugged", required: false, multiple:false, refreshAfterSelection:false, options:[
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
        
        standardTile("freeze", "device.freeze", width:2, height: 2) {
        	state "on", label:'Freeze:${currentValue}', defaultState: true, nextState: "on", backgroundColor: "#bc2323", icon: "st.Weather.weather7"
            state "off", label:'Freeze:${currentValue}', defaultState: false, nextState: "off", backgroundColor: "#79b821", icon: "st.Weather.weather7"
        }
        
        standardTile("config", "device.config", width:2, height: 2) {
            /*
            state "unknown", label: 'Unknown', defaultState: false, backgroundColor: "#F2F200", icon: "st.Health & Wellness.health2"
        	state "on", label:'Ready', defaultState: true, nextState: "off", backgroundColor: "#79b821", icon: "st.Health & Wellness.health2"
            state "off", label:'Not Ready', defaultState: false, nextState: "on", backgroundColor: "#bc2323", icon: "st.Health & Wellness.health2"
            */
            state "unknown", label: 'Updating', defaultState: false, backgroundColor: "#F2F200", icon: "st.samsung.da.RC_ic_power"
            state "on", label:'Ready', defaultState: true, nextState: "off", backgroundColor: "#79b821", icon: "st.samsung.da.RC_ic_power"
            state "off", label:'Not Ready', defaultState: false, nextState: "on", backgroundColor: "#bc2323", icon: "st.samsung.da.RC_ic_power"
        }
        
        //////////////////////////////////////////////////
        
        valueTile("poolHeatMode", "device.poolHeatMode", width:3, height: 2) {
        	state "val", label:'${currentValue}', defaultState: true
        }
        valueTile("spaHeatMode", "device.spaHeatMode", width:3, height: 2) {
        	state "val", label:'${currentValue}', defaultState: true
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
              
        //////////////////////////////////////////////////
        
        standardTile("POOL LIGHT", "device.POOL LIGHT", width: 2, height: 2, canChangeBackground: true) {
			//state "unknown", label: 'POOL LIGHT', action: "poolLightUnknown", icon: "st.Lighting.light11", backgroundColor: "#F2F200"
            state "off", label: 'POOL LIGHT', action: "poolLightToggle", icon: "st.Lighting.light13", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'POOL LIGHT', action: "poolLightToggle", icon: "st.Lighting.light11", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("SPA LIGHT", "device.SPA LIGHT",   width: 2, height: 2, canChangeBackground: true) {
        	//state "unknown", label: 'SPA LIGHT', action: "spaLightUnknown", icon: "st.Lighting.light11", backgroundColor: "#F2F200"
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
        standardTile("ALL PUMPS", "device.ALLPUMPS", width: 2, height: 2, canChangeBackground: true) {
        	//state "unknown", label: 'CLEANER', action: "cleanerUnknown", icon: "st.Appliances.appliances2", backgroundColor: "#F2F200"
			state "off", label: 'ALL PUMPS', action: "allPumpsToggle", icon: "st.Appliances.appliances2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'ALL PUMPS', action: "allPumpsToggle", icon: "st.Appliances.appliances2", backgroundColor: "#79b821", nextState: "off"
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
			state "off", label: 'Filter', action: "poolToggle", icon: "st.Health & Wellness.health2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Filter', action: "poolToggle", icon: "st.Health & Wellness.health2", backgroundColor: "#79b821", nextState: "off"
		}
        //////////////////////////////////////////////////
        // HeatingSetpoint
 		// Turns Spa heat on/off
        // Can use thermostatFull to change heater set point
        standardTile("SPA", "device.SPA", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'SPA', action: "spaUnknown", icon: "st.Bath.bath4", backgroundColor: "#F2F200"
			state "off", label: 'SPA', action: "spaToggle", backgroundColor: "#ffffff", nextState: "on", icon: "st.Bath.bath4" //,icon: "st.thermostat.heat"
			state "on", label: 'SPA', action: "spaToggle", backgroundColor: "#79b821", nextState: "off", icon: "st.Bath.bath4" //,icon: "st.thermostat.heating"
        }     
        standardTile("spaDown", "device.spaDown", width: 2, height: 2, canChangeBackground: true) {
			state "down", label: 'Down', action: "tempDown",icon: "st.thermostat.thermostat-down",nextState: "push", backgroundColor: "#ffffff"
  			state "push", label: 'Down', action: "tempDown",icon: "st.thermostat.thermostat-down",nextState: "down", backgroundColor: "#cccccc"           
		}       
        standardTile("heatingSetpoint", "device.heatingSetpoint", width: 2, height: 2, canChangeBackground: true, key: "HEATING_SETPOINT") {
			 state "off", label: '${name}: ${currentValue}', action: "spaToggle", unit: "dF", icon: "st.Bath.bath4", backgroundColor: "#ffffff", nextState: "on"
			 state "on" , label: '${name}: ${currentValue}', action: "spaToggle", unit: "dF", icon: "st.Bath.bath4", backgroundColor: "#79b821", nextState: "off"
		}          
        standardTile("spaUp", "device.spaUp", width: 2, height: 2, canChangeBackground: true) {
			state "up", label: 'Up', action: "tempUp",icon: "st.thermostat.thermostat-up",nextState: "push", backgroundColor: "#ffffff"
			state "push", label: 'Up', action: "tempUp",icon: "st.thermostat.thermostat-up",nextState: "up", backgroundColor: "#cccccc"
    	}
        
        //////////////////////////////////////////////////
        
        valueTile("blank", "device.blank", width: 2, height: 2) {
			state "blank", label:'', defaultState: true
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
        	// center
    		tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
        		attributeState("temp", label:'${currentValue}°', unit:"dF", defaultState: true)
    		}
            // right up/down
   			tileAttribute("device.temperature", key: "VALUE_CONTROL") {
        		attributeState("VALUE_UP", action: "tempUp")
        		attributeState("VALUE_DOWN", action: "tempDown")
    		}
            // changes background color
    		tileAttribute("device.thermostatOperatingState", key: "OPERATING_STATE") {
        		attributeState("idle", backgroundColor:"#00A0DC", label: '${name}')
       	 		attributeState("heating", backgroundColor:"#e86d13", label: '${name}')
    		}
            // bottom center
    		tileAttribute("device.spaHeatMode", key: "THERMOSTAT_MODE") {
                attributeState("Off", label:'${name}')
        		attributeState("Heater", label:'${name}')
                attributeState("Solar Pref", label:'${name}')
                attributeState("Solar Only", label:'${name}')
    		}
            // Not sure what this does
    		tileAttribute("device.heatingSetpoint", key: "HEATING_SETPOINT") {
        		attributeState("heatingSetpoint", label:'HS ${currentValue}°', unit:"dF", defaultState: true)
    		}
            // lower left corner
            tileAttribute("device.spaTemp", key: "SECONDARY_CONTROL") {
        		attributeState("spaTemp", label:'${currentValue}°', defaultState: true)
    		}
		} 
  
        //////////////////////////////////////////////////  
        valueTile("scheduleTile", "device.scheduleTile", width: 6, height: 10, decoration: "flat", canChangeBackground: false) {
			state "schedule", label:'${currentValue}', defaultState: true
		}
        
        valueTile("eggTimerTile", "device.eggTimerTile", width: 6, height: 10, decoration: "flat", canChangeBackground: false) {
			state "eggTimerTile", label:'${currentValue}', defaultState: true
		}  

        //////////////////////////////////////////////////  
        // spa tile turns on heater
        // use thermostatFull to change heater set point
        
      
        // spaMode
        // spaheat/mode/# (0=off, 1=heater, 2=solar pref, 3=solar only)
        standardTile("spaMode", "device.spaMode", width: 2, height: 2, canChangeBackground: true) {
			state "Off", label: '${name}', action: "spaModeToggle", backgroundColor: "#ffffff", nextState: "Heater"
			state "Heater", label: '${name}', action: "spaModeToggle", backgroundColor: "#ffffff", nextState: "Solar Pref"
			state "Solar Pref", label: '${name}', action: "spaModeToggle", backgroundColor: "#ffffff", nextState: "Solar Only"
			state "Solar Only", label: '${name}', action: "spaModeToggle", backgroundColor: "#ffffff", nextState: "Off"
		}    
                  
        standardTile("poolUp", "device.poolUp", width: 2, height: 2) {
			state "up", label: 'Up', action: "tempUp", backgroundColor: "#ffffff",icon: "st.thermostat.thermostat-up"
		}
        
        standardTile("poolDown", "device.poolDown", width: 2, height: 2) {
			state "down", label: 'Down', action: "tempDown", backgroundColor: "#ffffff",icon: "st.thermostat.thermostat-down"
		}
        
        standardTile("settime", "device.settime", width: 2, height: 2) {
			state "on", label: 'Time', action: "setdatetime", backgroundColor: "#ffffff",icon: "st.secondary.refresh-icon"
		}
        
        standardTile("addscheduleTile", "device.addschedule", width: 2, height:2) {
			state "add", label: '${currentValue}', action: "addSchedule", backgroundColor: "#ffffff",icon: "st.custom.buttons.add-icon", nextState: "activated"
            state "activated", label: '${currentValue}', action: "addSchedule", backgroundColor: "#cccccc",icon: "st.custom.buttons.add-icon", nextState: "add"          
 		}   
        
        valueTile("eggTimerSchedule", "device.eggTimerSchedule", width:4, height: 4, canChangeBackground: false) {
        	state "on", label: '${currentValue}', nextState: "off"
            state "off", label: '${currentValue}', nextState: "on"
        }
        
        standardTile("delscheduleTile", "device.delschedule", width: 2, height: 2) {
			state "delete", label: '${currentValue}', action: "delSchedule", backgroundColor: "#ffffff",icon: "st.custom.buttons.subtract-icon", nextState: "activated"
		    state "activated", label: '${currentValue}', action: "delSchedule", backgroundColor: "#cccccc",icon: "st.custom.buttons.subtract-icon", nextState: "delete"
        }   
        
        main(["poolTemp"])

        details([
        "poolCntr", "timedate",
        "refresh", "config", "freeze", 
        "airTemp", "poolTemp", "spaTemp",
        "POOL LIGHT", "SPA LIGHT", "ALL LIGHTS", 
        "HIGH SPEED", "CLEANER", "ALL PUMPS", 
        "SPILLWAY", "AIR BLOWER", "POOL", 
        "spaDown","SPA","spaUp",
        "thermostatFullspa",
        "poolHeatMode", "spaHeatMode",
        "Pump 1","Pump 2",
        "addscheduleTile", "eggTimerSchedule", "delscheduleTile",
        "scheduleTile",
        "eggTimerTile"       
 		])
	}
}
            
def updated() {
    if (state.debug_all) log.info "######### UPDATED #########" 
    //state.debug = debug
    //state.debug_pump = debug_pump
    //log.debug "adebug = ${adebug}"
    state.debug_all = false
    state.debug_spa = false
    state.debug_airblower = false
    state.debug_poollight = false
    state.debug_spalight = false
    state.debug_cleaner = false
    state.debug_pool = false
    state.debug_highspeed = false
    state.debug_spillway = false
    state.debug_schedule = false
    state.debug_eggtimer = false
    state.debug_parse = false
    state.debug_temp = false
    state.debug_pump = false
    state.debug_circuit = false
    switch (adebug) {
       case "ALL":
          state.debug_all = true
          state.debug_spa = true
          state.debug_airblower = true
          state.debug_poollight = true
          state.debug_spalight = true
          state.debug_cleaner = true
          state.debug_pool = true
          state.debug_highspeed = true
          state.debug_spillway = true
          state.debug_schedule = true
          state.debug_eggtimer = true
          state.debug_parse = true
          state.debug_temp = true
          state.debug_pump = true
          state.debug_circuit = true
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
          state.debug_all = false
          state.debug_spa = false
          state.debug_airblower = false
          state.debug_poollight = false
          state.debug_spalight = false
          state.debug_cleaner = false
          state.debug_pool = false
          state.debug_highspeed = false
          state.debug_spillway = false  
          state.debug_parse = false 
          state.debug_schedule = false
          state.debug_eggtimer = false
          state.debug_temp = false
          state.debug_pump = false
          state.debug_circuit = false
    }      
      
    initialize()
    refresh()
}

def installed() {
	if (state.debug_all) log.info "########## installed ###########"
    initialize()
    setDeviceNetworkId("${controllerIP}","${controllerPort}")  
}

def initialize() {
	runEvery1Minute(refresh)

    sendHubCommand(setFeature("/circuit"))
    
    state.dayValueMap = [Sunday:1,Monday:2,Tuesday:4,Wednesday:8,Thursday:16,Friday:32,Saturday:64,
                          Sun:1,Mon:2,Tue:4,Wed:8,Thu:16,Fri:32,Sat:64] 
                          
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
    poll()
    sendEvent(name: "refresh", isStateChange: "true", value: "Active", descriptionText: "Refresh was activated")
    sendEvent(name: "config", value:"unknown", descriptionText: "System Status is unknown");
    updateSchedule()
}

def updateSchedule() {
    def ID = sch_id? sch_id : 0
    def mode = eggTimerORschedule?.is(0) ? "EggTimer" : "Schedule"
    def dow = build_dow()
    if (dow == []) dow = ""
    def times
    if (eggTimerORschedule) {
        //eggTimer
        sendEvent(name: "eggTimerSchedule", value: "Egg Timer\nID:${sch_id} Name:${sch_circuit}\nDuration ${egg_hour}:${egg_min}", isStateChange: "true")
    } else {
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
        sendEvent(name: "eggTimerSchedule", value: "Schedule\nID:${sch_id} Name:${sch_circuit}\n${times}\n ${dow}", isStateChange: "true")
    }    
}
def poll() {
	// poll gets /all & /device status messages from pool controller (raspberry Pi)
    // this runs every minute
    sendHubCommand(setFeature("/all"))
    sendHubCommand(setFeature("/device"))
}

// schedule 

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
    //state.dayList.each {
    state.dayMap.keySet().each {
       if (state.debug_schedule || state.debug_eggtimer) log.warn "it = ${it}"
       if (state.dayMap[it]) {        
          calc = calc + state.dayValueMap[it]
       }   
    }   
    if (state.debug_schedule || state.debug_eggtimer) log.debug "calc = ${calc}"    
    return (calc)
}

// API calls in server.js file on https://github.com/tagyoureit/nodejs-poolController/blob/b3d31a94daf486058d9a8d8043d9039d3a459487/src/lib/comms/server.js
// curl -X GET http://user:pass@pi-pool:3000/schedule | jq '.schedule | ."7"'
// curl -X GET http://user:pass@pi-pool:3000/schedule/set/7/3/21/00/22/00/127
// curl -X GET http://user:pass@pi-pool:3000/schedule/set/id/7/circuit/3

def addSchedule() {
    
    if (!sch_circuit | !sch_id) {
       sendEvent(name: "addschedule", isStateChange: "true", value: "add", descriptionText: "addschedule was updated")
       return
    }   
    def sch_circuit_num = state.circuit[sch_circuit]
    if (eggTimerORschedule) {
       def addeggtimer = setFeature("/eggtimer/set/id/${sch_id}/circuit/${sch_circuit_num}/hour/${egg_hour}/min/${egg_min}")
       sendHubCommand(addeggtimer)
    } else {
       if (!sch_start | !sch_end) {
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

def setdatetime() {
// datetime/set/time/{hour}/{min}/{dow}/{day}/{mon}/{year}/{dst}
// set the schedule on the controller for the particular schedule ID. 
// dow= day of week as expressed as [0=Sunday, 1=Monday, 2=Tuesday, 4=Wednesday, 8=Thursday, 16=Friday, 32=Saturday] 
// or a combination thereof [3=Monday+Tuesday]. To set a schedule set a valid start and end time (hh:mm). 
// To set an egg timer, set the start time to 25:00 and the endtime to the duration (hh:mm) you want the egg timer to run.
//"text": "FAIL: SOCKET API - hour (NaN) should be 0-23 and minute (NaN) should be 0-59. 
// Received: NaN:NaNDay (NaN) should be 0-31, month (NaN) should be 0-12 and year (NaN) should be 0-99.
// Day of week (NaN) should be one of: [1,2,4,8,16,32,64] [Sunday->Saturday]dst (0) should be 0 or 1" }
    if (!controllerTime) return
    Date newDate = Date.parse("yyyy-MM-dd'T'HH:mm",controllerTime)  
    String year = newDate.format('yy')
    String month = newDate.format('MM')
    String day = newDate.format('dd')
    String hour = newDate.format('HH')
    String minute = newDate.format('mm')
    String DayOfWeek = newDate.format('EE')

    def downum = state.dayValueMap[DayOfWeek]
    def action = setFeature("/datetime/set/time/${hour}/${minute}/date/${downum}/${day}/${month}/${year}/0")
    sendHubCommand(action)
}

// parse

def parse(String description) {
	if (state.debug_all) log.debug "############ Parse #############"
    if (state.debug_all) log.debug "${state.debug_spa} \
          ${state.debug_airblower} \
          ${state.debug_poollight} \
          ${state.debug_spalight} \
          ${state.debug_cleaner} \
          ${state.debug_pool} \
          ${state.debug_highspeed} \
          ${state.debug_spillway}"
          
    def msg = parseLanMessage(description)
    if (!msg) {
       sendEvent(name: "refresh", isStateChange: "true", value: "Idle", descriptionText: "Refresh was activated")
       return null
    }
    
    def xmlerror = msg.xmlError
    if (xmlerror) {
       log.error "Error: ${xmlerror}"
       return null
    }   
    
    // process xml response from /device
    // stop processing when done via return null
    if (msg.xml) {
        def body = new XmlSlurper().parseText(msg.body)
        def verMajor = body.specVersion.major
        def verMinor = body.specVersion.minor
        def verPatch = body.specVersion.patch
        def manufacturer = body.device.manufacturer
        def modelDescription = body.device.modelDescription
        //def udn = body.device.UDN
        def name = body.device.friendlyName
        state.version = "${name} Version\n${verMajor}.${verMinor}.${verPatch}"
        state.manufacturer = "By ${manufacturer}\n${modelDescription}"
        sendEvent(name: "refresh", isStateChange: "true", value: "Idle", descriptionText: "Refresh was activated")
        return null
    }    
   
    // process json messages
    def json = msg.data
    if (state.debug_parse) log.warn "json = ${json}"
    
    // return if no json
    if (!json) {
       sendEvent(name: "refresh", isStateChange: "true", value: "Idle", descriptionText: "Refresh was activated")
       return null
    }   
     
    // process each json key 
    json.keySet().each() {
      def key = it
      switch (key) {
        case "value":
              if (state.debug_parse) log.warn "value = ${json.value}"
              break
        case "status":
              if (state.debug_parse) log.warn "status = ${json.status}"
              break
        case "text":
              log.info "### parse : text ###"
              if (state.debug_parse) log.warn "parse: text response: json = ${json}"
              if (state.debug_parse) log.warn "Response: ${json.text}"
              if (state.debug_parse) log.warn "Status: ${json.status}"
              if (state.debug_parse) log.warn "value: ${json.value}"
              if (json.text.contains("POOL LIGHT")) { 
                  if (state.debug_parse) log.info "status = POOL LIGHT ${json.status}"
                  sendEvent(name: "POOL LIGHT", value: "${json.status}")
                  def slcurrent = device.currentState("SPA LIGHT")?.value                 
                  if (state.debug_parse) log.info "slcurrent = ${slcurrent}"
                  if (json.status == "on" && slcurrent == "on")
                     sendEvent(name: "ALL LIGHTS", value: "on")
                  else if (json.status == "off" && slcurrent == "off") 
                     sendEvent(name: "ALL LIGHTS", value: "off")                    
              } else if (json.text.contains("SPA LIGHT")) {
                  if (state.debug_parse) log.info "status = SPA LIGHT ${json.status}"
                  sendEvent(name: "SPA LIGHT", value: "${json.status}")
                  def plcurrent = device.currentState("POOL LIGHT")?.value                 
                  if (state.debug_parse) log.info "plcurrent = ${plcurrent}"
                  if (json.status == "on" && plcurrent == "on")
                     sendEvent(name: "ALL LIGHTS", value: "on")
                  else if (json.status == "off" && plcurrent == "off")   
                     sendEvent(name: "ALL LIGHTS", value: "off")
              } else if (json.text.contains("CLEANER")) {
                  sendEvent(name: "CLEANER", value: "${json.status}")
                  def ccurrent = device.currentState("CLEANER")?.value                 
                  if (state.debug_parse) log.info "ccurrent = ${ccurrent}"
                  if (json.status == "on" && ccurrent == "on")
                     sendEvent(name: "ALL PUMPS", value: "on")
                  else if (json.status == "off" && ccurrent == "off")   
                     sendEvent(name: "ALL PUMPS", value: "off")
              } else if (json.text.contains("HIGH SPEED")) {
                  sendEvent(name: "HIGH SPEED", value: "${json.status}") 
                  def hscurrent = device.currentState("HIGH SPEED")?.value                 
                  if (state.debug_parse) log.info "hscurrent = ${hscurrent}"
                  if (json.status == "on" && hscurrent == "on")
                     sendEvent(name: "ALL PUMPS", value: "on")
                  else if (json.status == "off" && hscurrent == "off")   
                     sendEvent(name: "ALL PUMPS", value: "off")
              } else if (json.text.contains("SPILLWAY")) {
                  sendEvent(name: "SPILLWAY", value: "${json.status}")
              } else if (json.text.contains("set spa heat setpoint")) {
              	  sendEvent(name: "heatingSetpoint", value: "${json.value}")
                  sendEvent(name: "device.temperature", value: "${json.value}")
              } else if (json.text.contains("toggle SPA to")) {
                  sendEvent(name: "SPA", value: "${json.status}")
              } else if (json.text.contains("toggle POOL to")) {
                  sendEvent(name: "POOL", value: "${json.status}")
              } else if (json.text.contains("BLOWER")) {
                  sendEvent(name: "AIR BLOWER", value: "${json.status}")
              }
              break
        case "time":
              if (state.debug_parse) log.info "### parse : time ###"
              if (state.debug_parse) log.debug "time = ${json.time}"
              def time = json.time
              if (state.debug_parse) log.info "time = ${time}"
              def controllerTime = time.controllerTime
              def controllerDate = time.controllerDateStr
              def controllerDoW = time.controllerDayOfWeekStr
              sendEvent(name: "timedate", value: "${controllerDoW}\n${controllerDate}\n${controllerTime}\n("+state.uom+")")
              break
        case "heat": 
              if (state.debug_parse) log.info "### parse : heat - merged with temperature - no break ###"
        case "temperature":
              if (state.debug_temp) log.info "### parse : temperatures ###"  
              if (state.debug_temp) log.info "temperature = ${json.temperature}"
              def temperatures = json.temperature
              def poolTemp = temperatures.poolTemp
              def spaTemp = temperatures.spaTemp
              def airTemp = temperatures.airTemp
              //def solarTemp = temperatures.solarTemp
              def freeze = temperatures.freeze?.is(1) ? "on" : "off"
              def poolSetPoint = temperatures.poolSetPoint
              //def poolHeatMode = temperatures.poolHeatMode
              def poolHeatModeStr = temperatures.poolHeatModeStr
              def spaSetPoint = temperatures.spaSetPoint
              //def spaHeatMode = temperatures.spaHeatMode
              def spaHeatModeStr = temperatures.spaHeatModeStr
              //def heaterActive = temperatures.heaterActive

              sendEvent(name: "poolTemp", value: "${poolTemp}")
              sendEvent(name: "spaTemp", value: "${spaTemp}")
              sendEvent(name: "airTemp", value: "${airTemp}")
              sendEvent(name: "freeze", value: "${freeze}")
              sendEvent(name: "spaHeatMode", value: "Spa Heater Mode: ${spaHeatModeStr}\nSpa Set\nPoint: ${spaSetPoint}°")
              sendEvent(name: "poolHeatMode", value: "Pool Heater Mode: ${poolHeatModeStr}\nPool Set\nPoint: ${poolSetPoint}°")
              sendEvent(name: "spaMode", value: "${spaHeatModeStr}")
              sendEvent(name: "heatingSetpoint", value: "${spaSetPoint}")
              sendEvent(name: "temperature", value: "${spaSetPoint}")
              break
        case "circuit":
              if (state.debug_circuit) log.info "### parse : circuits ###" 
              def circuits = json.circuit
              if (state.debug_circuit) log.info "state.circuit = ${state.circuit}"           
              if (state.debug_circuit) log.info "******** circuits = \n${circuits} ************"
              circuits.keySet().each {
                 def c = circuits[it]
                 def String cname = c.name
               
                 if (!cname.contains('FEATURE') && !cname.contains("NOT USED") && !cname.contains("AUX")) {
                     state.circuit[cname] = c.number
                 }    
              }
              
              // set status/state for each tile
              state.circuit.keySet().each {
                  def num = state.circuit[it]
                  def status = circuits."${num}".status
                  sendEvent(name: "${it}", value: status?.is(1) ? "on" : "off")
              }
              
              def heatvalue = "off"
              if (spaStatus == 1) {
            	 heatvalue = "heating"
              } else {
            	 heatvalue = "idle"
              }  
              
              sendEvent(name: "thermostatOperatingState" , value: "${heatvalue}")                         
        	  break
        case "pump":      
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
              def chlor = json.chlorinator
              if (state.debug_parse) log.info "chlor = ${chlor}"
              break
        case "config":
              def config = json.config
              if (state.debug_parse) log.debug "config = ${config}"
              sendEvent(name: "config", value: config.systemReady?.is(1) ? "on" : "off", descriptionText: "System Status is ${config.systemReady?.is(1) ? 'on' : 'off'}")
              break
        case "UOM":
              state.uom = json.UOM.UOMStr
              if (state.debug_parse) log.info "uom = ${state.uom}"
              break
        case "valve":
              def valve = json.valve
              if (state.debug_parse) log.info "valve = ${valve}"
              break
        case "intellichem":
              def intellichem = json.intellichem
              if (state.debug_parse) log.info "intell = ${intell}"
              break
        case "schedule":
           	  if (state.debug_schedule) log.info "### parse : schedule ##"
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
                  } else if (CIRCUIT != "NOT USED") {
                      circuitSize = 16 - CIRCUIT.size()
                      for (i = 0; i <circuitSize; i++) {
                      	 space = space + " "
                      }
                      
                	  def START_TIME = event.START_TIME
                	  def END_TIME = event.END_TIME
                      ison = ""
                      def between = timeOfDayIsBetween(START_TIME, END_TIME, new Date(), location.timeZone)
    				  if (between) {
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
                      fullSchedule = fullSchedule + "${ison}${ID}${space}${CIRCUIT}${space}${START_TIME}${space}${END_TIME}\n\nDAYS:${days}\n\n"
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
        sendEvent(name: "refresh", isStateChange: "true", value: "Idle", descriptionText: "Refresh was activated")
     }   
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

def getTempInLocalScale(state) {
	def temp = device.currentState(heatingSetpoint)
	if (temp && temp.value && temp.unit) {
		return getTempInLocalScale(temp.value.toBigDecimal(), temp.unit)
	}
	return 0
}

def alterSetpoint(targetValue) {
	//def locationScale = getTemperatureScale()
	//def deviceScale = (state.scale == 1) ? "F" : "C" 
    setSpaHeatPoint(targetValue)  
}


def setSpaHeatPoint(value) {
    //def action = setFeature("/spaheat/setpoint/${value}")
    sendHubCommand(setFeature("/spaheat/setpoint/${value}"))
}

// commands

def setPoint(num) {
	log.debug "setSpaHeatPoint : num = ${num}"
    //def action = setFeature("/spaheat/setpoint/${num}")
    sendHubCommand(setFeature("/spaheat/setpoint/${num}"))
}

def tempUp() {
	def ts = device.currentState("temperature")
    def hs = device.currentState("heatingSetpoint")
	def value = ts ? ts.integerValue + 1 : 72
    alterSetpoint(value)
    sendEvent(name: "heatingSetpoint", value: "${value}°")
    sendEvent(name: "temperature", value: "${value}")
    sendEvent(name: "spaUp", isStateChange: "true")
}

def tempDown() {
	def ts = device.currentState("temperature")
    def hs = device.currentState("heatingSetpoint")
	def value = ts ? ts.integerValue - 1 : 72
    alterSetpoint(value)
    sendEvent(name: "heatingSetpoint", value: "${value}°")
    sendEvent(name: "temperature", value: "${value}")
    sendEvent(name: "spaDown", isStateChange: "true")
}

// currently not being used
def evaluate(temp, heatingSetpoint) {
	def threshold = 1.0
	def current = device.currentValue("thermostatOperatingState")
	def mode = device.currentValue("thermostatOperatingState")

	def heating = false
	def cooling = false
	def idle = false
	if (mode in ["heat","emergency heat","auto"]) {
		if (heatingSetpoint - temp >= threshold) {
			heating = true
			sendEvent(name: "thermostatOperatingState", value: current)
		}
		else if (temp - heatingSetpoint >= threshold) {
			idle = true
		}
		sendEvent(name: "thermostatSetpoint", value: heatingSetpoint)
	}
	else {
		sendEvent(name: "thermostatSetpoint", value: "idle")
	}

	if (mode == "off") {
		idle = true
	}

	if (idle && !heating && !cooling) {
		sendEvent(name: "thermostatOperatingState", value: "idle")
	}
}

// currently not used, tile has been disabled
def spaModeToggle() {
	if (state.debug_spa) log.info "--------- spaMode Toggle ----------"
    def mode = device.currentValue("spaMode")
    def num = ""
    def value = ""
    if (state.debug_spa) log.info "mode = ${mode}"    
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
        
    sendHubCommand(setFeature("/spaheat/mode/${num}"))
    sendEvent(name: "spaMode", value: "${value}")
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
    Toggle('POOL LIGHT')
    Toggle('SPA LIGHT')
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