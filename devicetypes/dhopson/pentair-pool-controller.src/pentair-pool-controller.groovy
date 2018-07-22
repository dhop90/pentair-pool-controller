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
 
import groovy.json.JsonSlurper
import groovy.util.XmlSlurper
 
metadata {
	definition (name: "Pentair Pool Controller", namespace: "dhopson", author: "David Hopson", oauth: true) {
        capability "Switch"
    	capability "Polling"        
        capability "Refresh"
        capability "Temperature Measurement"
        capability "Thermostat"
       
        // modify these functions to match your environment
        command "poolToggle"
        command "spaToggle"
        command "spaModeToggle"
        command "poolLightToggle"
        command "spaLightToggle"
		command "blowerToggle"
		command "cleanerToggle"
        command "highspeedToggle"
        command "spillWayToggle"	   
        command "tempUp"
		command "tempDown"
        command "setdatetime"
        command "addSchedule"
        command "delSchedule"
        command "testToggle"
        command "setuom"
	}
   
    preferences {
       	section("Select your controller") {
       		input "controllerIP", "text", title: "Controller hostname/IP", required: true
       		input "controllerPort", "port", title: "Controller port", required: true, defaultValue: "3000"
            input "username", "string", title:"Username", description: "username", required: true, displayDuringSetup: true
            input "password", "password", title:"Password", description: "Password", required: true, displayDuringSetup: true
            input name: "debug", type: "bool", title: "Enable debug?", description: "Debug", required: false
            input name: "debug_pump", type: "bool", title: "Enable pump debug?", description: "Debug pump", required: false
            }
            
         section("Schedules") {   
            input name: "controllerTime", type: "time", title: "System Time", descripiton: "Enter Time, then select time tile to update", required: false
            input name: "EggTimer", type: "bool", title: "Is this an egg timer?", description: "Egg timer and not a Schedule?", required: false
            input name: "sch_circuit", type: "enum", title: "Circuit Name", description: "Which circuit should be used for this schedule", required: false, options:[
                    "spa",
        			"blower",
        			"poolLight",
        			"spaLight",
        			"cleaner",
        			"pool",
        			"highSpeed",
        			"spillway"]
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
            /*
            input name: "sch_dow", type: "enum", title: "Schedule Day of Week", description: "Day of Week", required: false, multiple: true, refreshAfterSelection:true, options:[
                    "Monday", 
                    "Tuesday", 
                    "Wednesday", 
                    "Thursday", 
                    "Friday", 
                    "Saturday", 
                    "Sunday"]
                    //https://community.smartthings.com/t/input-preferences-for-device-type-with-multiple-true/15330 - issue   
             */       
             input name: "Monday", type: "bool", title: "Schedule Monday?", description: "Should the task be scheduled for this day?", required: false 
             input name: "Tuesday", type: "bool", title: "Schedule Tuesday?", description: "Should the task be scheduled for this day?", required: false
             input name: "Wednesday", type: "bool", title: "Schedule Wednesday?", description: "Should the task be scheduled for this day?", required: false
             input name: "Thursday", type: "bool", title: "Schedule Thursday?", description: "Should the task be scheduled for this day?", required: false
             input name: "Friday", type: "bool", title: "Schedule Friday?", description: "Should the task be scheduled for this day?", required: false
             input name: "Saturday", type: "bool", title: "Schedule Saturday?", description: "Should the task be scheduled for this day?", required: false
             input name: "Sunday", type: "bool", title: "Schedule Sunday?", description: "Should the task be scheduled for this day?", required: false 
        }
      }  
    
	simulator {
		// TODO: define status and reply messages here
	}
    
	tiles(scale: 2) {
    	// Display time and date from pool controller
        
        // refresh
        standardTile("refresh", "device.refresh", width: 2, height: 2, canChangeBackground: true) {
        	state "Idle", label:'refresh', action:"refresh", icon:"st.secondary.refresh-icon", nextState: "Active", backgroundColor: "#ffffff"
            state "Active", label:'refresh', action:"refresh", icon:"st.secondary.refresh-icon", nextState: "Idle", backgroundColor: "#cccccc"
    	}      
        //valueTile("timedate", "device.timedate", width: 6, height: 2){ //, decoration: "flat") {
        valueTile("timedate", "device.timedate", width: 3, height: 2, decoration: "flat", canChangeBackground: false) {
			//state "ON", label:'${currentValue}', action:"setdatetime", nextState: "OFF", backgroundColor: "#79b821"
            //state "OFF", label:'${currentValue}', action:"setdatetime", nextState: "ON", backgroundColor: "#bc2323"
            state "val", label:'${currentValue}', action:"setdatetime", defaultState: true
		}   
        
        standardTile("uomTile", "device.uomTile", width: 3, height: 1, canChangeBackground: false, icon:"st.secondary.refresh-icon") {
            state "ON", label:'${currentValue}', action:"setuom", defaultState: true, nextState: "OFF"
            state "OFF", label:'${currentValue}', action:"setuom", defaultState: true, nextState: "ON"
		}          
        
        valueTile("poolCntr", "device.poolCntr", width: 3, height: 2, decoration: "flat", canChangeBackground: false) {
            state "on", label:'${currentValue}', defaultState: true
        }    
        
        standardTile("freeze", "device.freeze", width:2, height: 2) {
        	state "ON", label:'Freeze:${currentValue}', defaultState: true, nextState: "ON", backgroundColor: "#bc2323", icon: "st.Weather.weather2"
            state "OFF", label:'Freeze:${currentValue}', defaultState: true, nextState: "OFF", backgroundColor: "#79b821", icon: "st.Weather.weather2"
        }
        
        standardTile("config", "device.config", width:2, height: 2) {
        	state "ON", label:'Ready', defaultState: true, nextState: "OFF", backgroundColor: "#79b821", icon: "st.Health & Wellness.health2"
            state "OFF", label:'Not Ready', defaultState: true, nextState: "ON", backgroundColor: "#bc2323", icon: "st.Health & Wellness.health2"
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
        
        standardTile("test", "device.test", width: 2, height: 2, canChangeBackground: true) {
			//state "unknown", label: 'poolLight', action: "poolLightUnknown", icon: "st.Lighting.light11", backgroundColor: "#F2F200"
            state "off", label: 'test ${currentValue}', action: "testToggle", nextState: "on"
			state "on", label: 'test ${currentValue}', action: "testToggle", nextState: "off"
		}
        
        //////////////////////////////////////////////////
        
        standardTile("poolLight", "device.poolLight", width: 2, height: 2, canChangeBackground: true) {
			state "unknown", label: 'poolLight', action: "poolLightUnknown", icon: "st.Lighting.light11", backgroundColor: "#F2F200"
            state "off", label: 'poolLight ${currentValue}', action: "poolLightToggle", icon: "st.Lighting.light11", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'poolLight ${currentValue}', action: "poolLightToggle", icon: "st.Lighting.light11", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("spaLight", "device.spaLight",   width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'spaLight', action: "spaLightUnknown", icon: "st.Lighting.light11", backgroundColor: "#F2F200"
			state "off", label: 'spaLight ${currentValue}', action: "spaLightToggle", icon: "st.Lighting.light11", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'spaLight ${currentValue}', action: "spaLightToggle", icon: "st.Lighting.light11", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("highspeed", "device.highspeed", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'Highspeed', action: "highspeedUnknown", icon: "st.thermostat.thermostat-right", backgroundColor: "#F2F200"
			state "off", label: 'Highspeed ${currentValue}', action: "highspeedToggle", icon: "st.thermostat.thermostat-right", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Highspeed ${currentValue}', action: "highspeedToggle", icon: "st.thermostat.thermostat-right", backgroundColor: "#79b821", nextState: "off"
		}
 
        //////////////////////////////////////////////////
        standardTile("cleaner", "device.cleaner", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'Cleaner', action: "cleanerUnknown", icon: "st.Appliances.appliances2", backgroundColor: "#F2F200"
			state "off", label: 'Cleaner ${currentValue}', action: "cleanerToggle", icon: "st.Appliances.appliances2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Cleaner ${currentValue}', action: "cleanerToggle", icon: "st.Appliances.appliances2", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("spillWay", "device.spillWay", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'SpillWay', action: "spillWayUnknown", icon: "st.Bath.bath13", backgroundColor: "#F2F200"
			state "off", label: 'SpillWay ${currentValue}', action: "spillWayToggle", icon: "st.Bath.bath13", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'SpillWay ${currentValue}', action: "spillWayToggle", icon: "st.Bath.bath13", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("blower", "device.blower", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'blower', action: "blowerUnknown", icon: "st.vents.vent", backgroundColor: "#F2F200"
			state "off", label: 'blower ${currentValue}', action: "blowerToggle", icon: "st.vents.vent", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'blower ${currentValue}', action: "blowerToggle", icon: "st.vents.vent-open", backgroundColor: "#79b821", nextState: "off"
		}
     
        //////////////////////////////////////////////////
        // HeatingSetpoint
 		// Turns Spa heat on/off
        // Can use thermostatFull to change heater set point
        
        standardTile("spaDown", "device.spaDown", width: 2, height: 2, canChangeBackground: true) {
			state "down", label: 'Down', action: "tempDown",icon: "st.thermostat.thermostat-down",nextState: "push", backgroundColor: "#ffffff"
  			state "push", label: 'Down', action: "tempDown",icon: "st.thermostat.thermostat-down",nextState: "down", backgroundColor: "#cccccc"           
		}
        standardTile("heatingSetpoint", "device.heatingSetpoint", width: 2, height: 2, canChangeBackground: true, key: "HEATING_SETPOINT") {
			 state "off", label: '${name}: ${currentValue}', action: "spaToggle", unit: "dF", icon: "st.Bath.bath4", backgroundColor: "#ffffff", nextState: "on"
			 state "on" , label: '${name}: ${currentValue}', action: "spaToggle", unit: "dF", icon: "st.Bath.bath4", backgroundColor: "#79b821", nextState: "off"
		}
        
        standardTile("spa", "device.spa", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'Spa', action: "spaUnknown", icon: "st.Bath.bath4", backgroundColor: "#F2F200"
			state "off", label: 'Spa ${currentValue}', action: "spaToggle", backgroundColor: "#ffffff", nextState: "on", icon: "st.Bath.bath4" //,icon: "st.thermostat.heat"
			state "on", label: 'Spa ${currentValue}', action: "spaToggle", backgroundColor: "#79b821", nextState: "off", icon: "st.Bath.bath4" //,icon: "st.thermostat.heating"
        }
   
        standardTile("spaUp", "device.spaUp", width: 2, height: 2, canChangeBackground: true) {
			state "up", label: 'Up', action: "tempUp",icon: "st.thermostat.thermostat-up",nextState: "push", backgroundColor: "#ffffff"
			state "push", label: 'Up', action: "tempUp",icon: "st.thermostat.thermostat-up",nextState: "up", backgroundColor: "#cccccc"
    	}
        
        //////////////////////////////////////////////////
        
        standardTile("pool", "device.pool", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'Pool', action: "poolUnknown", icon: "st.Health & Wellness.health2", backgroundColor: "#F2F200"
			state "off", label: 'Filter ${currentValue}', action: "poolToggle", icon: "st.Health & Wellness.health2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Filter ${currentValue}', action: "poolToggle", icon: "st.Health & Wellness.health2", backgroundColor: "#79b821", nextState: "off"
		}
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
        //valueTile("PumpHdr1", "device.PumpHdr1", width: 3, height: 1) {
		//	state "val", label:'${currentValue}', defaultState: true
		//}
        //valueTile("PumpHdr2", "device.PumpHdr2", width: 3, height: 1) {
		//	state "val", label:'${currentValue}', defaultState: true
		//}

        
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
        valueTile("Schedule", "device.Schedule", width: 6, height: 10, decoration: "flat", canChangeBackground: false) {
			state "val", label:'${currentValue}', defaultState: false
		}
        //valueTile("EggTimer", "device.EggTimer", width: 6, height: 4, type:"generic", decoration: "flat") {
        valueTile("EggTimer", "device.EggTimer", width: 6, height: 5, decoration: "flat", canChangeBackground: false) {
			state "val", label:'${currentValue}', defaultState: true
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
        
        // Run Pool filter
        standardTile("pool", "device.pool", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'Pool', action: "poolUnknown", icon: "st.Health & Wellness.health2", backgroundColor: "#F2F200"
			state "off", label: 'Filter ${currentValue}', action: "poolToggle", icon: "st.Health & Wellness.health2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Filter ${currentValue}', action: "poolToggle", icon: "st.Health & Wellness.health2", backgroundColor: "#79b821", nextState: "off"
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
        
        standardTile("setschedule", "device.setschedule", width: 2, height: 2, canChangeBackground: true) {
			state "on", label: 'Schedule', action: "addSchedule", backgroundColor: "#ffffff",icon: "st.custom.buttons.add-icon"
		}   
        
        standardTile("delschedule", "device.delschedule", width: 2, height: 2, canChangeBackground: true) {
			state "on", label: 'Schedule', action: "delSchedule", backgroundColor: "#ffffff",icon: "st.custom.buttons.subtract-icon"
		}   
        
        main(["poolTemp"])

        details([
        "timedate", "poolCntr",
        "refresh", "config", "freeze", 
        //"poolHeatMode", "spaHeatMode",
        "airTemp", "poolTemp", "spaTemp",
        "poolLight", "spaLight", "highspeed", 
        "cleaner", "spillWay", "blower", 
        "spaDown","spa","spaUp",
        "pool","setschedule", "delschedule",
        "thermostatFullspa",
        "poolHeatMode", "spaHeatMode",
        //"PumpHdr1","PumpHdr2",
        "Pump 1","Pump 2",
        "EggTimer",        
        "Schedule"
 		])
	}
}
            
def updated() {
    if (state.debug) log.info "######### UPDATED #########" 
    if (debug) 
        state.debug = true
    else
        state.debug = false
        
    if (debug_pump) 
        state.debug_pump = true
    else
        state.debug_pump = false    
        
    state.uom = ""    
        
    initialize()
}

def installed() {
	if (state.debug) log.info "########## installed ###########"
    initialize()
    setDeviceNetworkId("${controllerIP}","${controllerPort}")  
}

def initialize() {
	runEvery1Minute(refresh)
    // modify these functions to match your environment
    state.circuit = [
        spa:'1',
        blower:'2',
        poolLight:'3',
        spaLight:'4',
        cleaner:'5',
        pool:'6',
        highSpeed:'7',
        spillway:'8'
        ]

     state.dayValueMap = [Sunday:1,Monday:2,Tuesday:4,Wednesday:8,Thursday:16,Friday:32,Saturday:64,
                          Sun:1,Mon:2,Tue:4,Wed:8,Thu:16,Fri:32,Sat:64]    
     state.dayMap = ["Sunday":Sunday, 
                     "Monday":Monday, 
                     "Tuesday":Tuesday, 
                     "Wednesday":Wednesday, 
                     "Thursday":Thursday, 
                     "Friday":Friday, 
                     "Saturday":Saturday]
     state.dayList = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
}

def refresh() {
    if (state.debug) log.warn "Requested a refresh"
    poll()
    sendEvent(name: "refresh", isStateChange: "true")
    sendEvent(name: "config", value:"OFF");
}

def poll() {
	// poll gets /all status messages from pool controller (raspberry Pi)
    // this runs every minute
    def userpass = encodeCredentials(username, password)
    def headers = getHeader(userpass)
    
    def dni = setDeviceNetworkId("${controllerIP}","${controllerPort}")

    def poolAction = new physicalgraph.device.HubAction(
		method: "GET",
		path: "/all",
        headers: headers,
        dni,
        query: []
	)   
    sendHubCommand(poolAction)
    def action = setFeature("/device")
    sendHubCommand(action)
}

// schedule 

def get_hour(time) {
   //String hour = time.format('HH')
   //return hour
   return time.format('HH')
}

def get_minute(time) {
   //String minute = time.format('mm')
   //return minute
   return time.format('mm')
}

def calc_dow() {      
    def calc = 0
    state.dayList.each {
       if (state.dayMap[it]) {        
          calc = calc + state.dayValueMap[it]
       }   
    }   
    if (state.debug) log.debug "calc = ${calc}"    
    return (calc)
}

// API calls in server.js file on https://github.com/tagyoureit/nodejs-poolController/blob/b3d31a94daf486058d9a8d8043d9039d3a459487/src/lib/comms/server.js
// curl -X GET http://user:pass@pi-pool:3000/schedule | jq '.schedule | ."7"'
// curl -X GET http://user:pass@pi-pool:3000/schedule/set/7/3/21/00/22/00/127
// curl -X GET http://user:pass@pi-pool:3000/schedule/set/id/7/circuit/3

def addSchedule() {
    if (!sch_circuit | !sch_id) return
    def sch_circuit_num = state.circuit[sch_circuit]
    if (EggTimer) {
       def addeggtimer = setFeature("/eggtimer/set/id/${sch_id}/circuit/${sch_circuit_num}/hour/${egg_hour}/min/${egg_min}")
       // "/circuit/${state.circuit['spa']}/toggle/"
       //def addeggtimer = setFeature("/eggtimer/set/id/${sch_id}/circuit/${state.circuit[${sch_circuit}]}/hour/${egg_hour}/min/${egg_min}")
       sendHubCommand(addeggtimer)
    } else {
       if (!sch_start | !sch_end) return
       Date startTime = Date.parse("yyyy-MM-dd'T'HH:mm",sch_start)  
       Date endTime = Date.parse("yyyy-MM-dd'T'HH:mm",sch_end)  

       String sch_starthh = get_hour(startTime)
       String sch_startmm = get_minute(startTime)
       String sch_endhh = get_hour(endTime)
       String sch_endmm = get_minute(endTime)
       if (state.debug) log.info "start time: ${sch_starthh}, ${sch_startmm}"
       if (state.debug) log.info "end time: ${sch_endhh}, ${sch_endmm}"
       def sch_dow_num = calc_dow()
        
       // ----- setFeature : query = /schedule/set/7/3/21/00/22/00/127 -------
       // Response: REST API received request to set schedule 7 with values (start) 21:0 (end) 22:0 with days value 127
       // Response: REST API received request to set circuit on schedule with ID (7) to POOL LIGHT
 
       def scheduleCircuit = setFeature("/schedule/set/id/${sch_id}/circuit/${sch_circuit_num}")
       if (state.debug) log.info "scheduleCircuit = ${scheduleCircuit}"
       if (state.debug) log.info "schedule set id:${sch_id} to circuit:${sch_circuit_num}"    
       sendHubCommand(scheduleCircuit)
             
       def addschedule = setFeature("/schedule/set/${sch_id}/${sch_circuit_num}/${sch_starthh}/${sch_startmm}/${sch_endhh}/${sch_endmm}/${sch_dow_num}")
       if (state.debug) log.info "schedule set id:${sch_id} to circuit:${sch_circuit_num} for time: start hour:${sch_starthh} start min:${sch_startmm} end hour:${sch_endhh} end min:${sch_endmm} day of the week number:${sch_dow_num}"
       if (state.debug) log.info "addschedule = ${addschedule}"
       sendHubCommand(addschedule)
    }
}

def delSchedule() {
   // app.get('/schedule/delete/id/:id'
   // 'REST API received request to delete schedule or egg timer with ID:' + id;
   if (!sch_id) return
   def action = setFeature("/schedule/delete/id/${sch_id}") 
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

    //log.debug "In setdatetime"
    
    //def controllerTime = new Date().format("yyyy-MM-dd'T'HH:mm")
    //log.debug "controllerTime = ${controllerTime}"
    //log.debug "to string = "+controllerTime.toString()
    //def downum = convertDow(dow)
    
    //        controllerTime = 2018-06-18T14:10:24.000-0500
    if (!controllerTime) return
    Date newDate = Date.parse("yyyy-MM-dd'T'HH:mm",controllerTime)  
    //log.debug "newDate = ${newDate}"
    String year = newDate.format('yy')
    String month = newDate.format('MM')
    String day = newDate.format('dd')
    String hour = newDate.format('HH')
    String minute = newDate.format('mm')
    String DayOfWeek = newDate.format('EE')
    //log.info "state.dayValueMap = ${state.dayValueMap}, DayOfWeek = ${DayOfWeek}"
    def downum = state.dayValueMap[DayOfWeek]
    //def downum = convertDow(DayOfWeek)
       
    //log.debug "year = ${year}"
    //log.debug "month = ${month}"
    //log.debug "day = ${day}"
    //log.debug "hour = ${hour}"
    //log.debug "minute = ${minute}"
    //log.debug "dow = ${dow} : ${downum}"
    //log.debug "DayOfWeek = ${DayOfWeek}"
    def action = setFeature("/datetime/set/time/${hour}/${minute}/date/${downum}/${day}/${month}/${year}/0")
    sendHubCommand(action)
}

// parse

def parse(String description) {
	log.debug "############ Parse #############"
    def msg = parseLanMessage(description)

    def xmlerror = msg.xmlError
    if (xmlerror)
       log.debug "Error: ${xmlerror}"
          
    if (msg.xml) {
        def body = new XmlSlurper().parseText(msg.body)
        def verMajor = body.specVersion.major
        def verMinor = body.specVersion.minor
        def verPatch = body.specVersion.patch
        def name = body.device.friendlyName
        sendEvent(name: "poolCntr", value: "${name} Version\n${verMajor}.${verMinor}.${verPatch}")
        return null
    }    

    def json = msg.data
    //log.warn "json = ${json}"
    
    if (!json)
        return null
        
    def keys = json.keySet()
    keys.each() {
      def key = it
      switch (key) {
        case "value":
              log.warn "value = ${json.value}"
              break
        case "status":
              log.warn "status = ${json.status}"
              break
        case "text":
              if (state.debug) log.info "### parse : text ###"
              log.warn "parse: response: json = ${json}"
              if (state.debug) log.warn "Response: ${json.text}"
              if (state.debug) log.warn "Status: ${json.status}"
              if (state.debug) log.warn "value: ${json.value}"
              if (json.text.contains("POOL LIGHT")) {       
                  sendEvent(name: "poolLight", value: "${json.status}")
              } else if (json.text.contains("SPA LIGHT")) {
                  sendEvent(name: "spaLight", value: "${json.status}")
              } else if (json.text.contains("CLEANER")) {
                  sendEvent(name: "cleaner", value: "${json.status}")
              } else if (json.text.contains("HIGH SPEED")) {
                  sendEvent(name: "highspeed", value: "${json.status}") 
              } else if (json.text.contains("SPILLWAY")) {
                  sendEvent(name: "spillWay", value: "${json.status}")
              } else if (json.text.contains("set spa heat setpoint")) {
              	  sendEvent(name: "heatingSetpoint", value: "${json.value}")
                  sendEvent(name: "device.temperature", value: "${json.value}")
              } else if (json.text.contains("toggle SPA to")) {
                  sendEvent(name: "spa", value: "${json.status}")
              } else if (json.text.contains("POOL")) {
                  sendEvent(name: "pool", value: "${json.status}")
              } else if (json.text.contains("BLOWER")) {
                  sendEvent(name: "blower", value: "${json.status}")
              }
              break
        case "time":
              if (state.debug) log.info "### parse : time ###"
              if (state.debug) log.debug "time = ${json.time}"
              def time = json.time
              if (state.warn) log.info "time = ${time}"
              def controllerTime = time.controllerTime
              def controllerDate = time.controllerDateStr
              def controllerDoW = time.controllerDayOfWeekStr
              sendEvent(name: "timedate", value: "${controllerDoW}\n${controllerDate}\n${controllerTime}\n("+state.uom+")")
              sendEvent(name: "uomTile", value: state.uom)
              break
        case "heat":  
              if (state.debug) log.info "### parse : heat ###"
              if (state.debug) log.info "heat = ${json.heat}"
              def heat = json.heat
              def poolSetPoint = heat.poolSetPoint
              def poolHeatMode = heat.poolHeatMode
              def poolHeatModeStr = heat.poolHeatModeStr
              def spaSetPoint = heat.spaSetPoint
              def spaHeatMode = heat.spaHeatMode
              def spaHeatModeStr = heat.spaHeatModeStr
              def heaterActive = heat.heaterActive 

              sendEvent(name: "temperature", value: "${spaSetPoint}")
              sendEvent(name: "heatingSetpoint", value: "${spaSetPoint}°")
              sendEvent(name: "spaHeatMode", value: "Spa Heater Mode: ${spaHeatModeStr}\nSpa Set Point: ${spaSetPoint}°")
              sendEvent(name: "poolHeatMode", value: "Pool Heater Mode: ${poolHeatModeStr}\nPool Set Point: ${poolSetPoint}°")
              sendEvent(name: "spaMode", value: "${spaHeatModeStr}")
              break
        case "temperature":
              if (state.debug) log.info "### parse : temperatures ###"  
              if (state.debug) log.info "temperature = ${json.temperature}"
              def temperatures = json.temperature
              def poolTemp = temperatures.poolTemp
              def spaTemp = temperatures.spaTemp
              def airTemp = temperatures.airTemp
              def solarTemp = temperatures.solarTemp
              def freeze = OnOffconvert(temperatures.freeze)
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
              sendEvent(name: "poolHeatMode", value: "Pool Heater Mode: ${poolHeatModeStr}\nPool Set\nPoint: ${poolSetPoint}°")
              sendEvent(name: "spaMode", value: "${spaHeatModeStr}")
              sendEvent(name: "heatingSetpoint", value: "${spaSetPoint}")
              sendEvent(name: "temperature", value: "${spaSetPoint}")
              break
        case "circuit":
              if (state.debug) log.info "### parse : circuits ###" 
              def circuits = json.circuit
              if (state.warn) log.info "******** circuits = ${circuits} ************"
              def spaStatus = circuits[state.circuit.spa].status
              def airBlowerStatus = circuits[state.circuit.blower].status
              def poolLightStatus = circuits[state.circuit.poolLight].status
              def spaLightStatus = circuits[state.circuit.spaLight].status
              def cleanerStatus = circuits[state.circuit.cleaner].status
              def poolStatus = circuits[state.circuit.pool].status
              def highSpeedStatus = circuits[state.circuit.highSpeed].status
              def spillwayStatus = circuits[state.circuit.spillway].status                   
           
              //log.info "poolLightStatus: ${poolLightStatus}\n, spaLightStatus: ${spaLightStatus}\n, poolStatus: ${poolStatus}\n, spaStatus: ${spaStatus}"
              //log.info "cleanerStatus: ${cleanerStatus}\n, spillwayStatus: ${spillwayStatus}\n, blowerStatus: ${airBlowerStatus}\n, highSpeedStatus: ${highSpeedStatus}"
                       
              sendEvent(name: "poolLight", value: OnOffconvert("${poolLightStatus}"))
              sendEvent(name: "spaLight", value: OnOffconvert("${spaLightStatus}"))
              sendEvent(name: "pool", value: OnOffconvert("${poolStatus}"))
              sendEvent(name: "cleaner", value: OnOffconvert("${cleanerStatus}"))
              sendEvent(name: "highspeed", value: OnOffconvert("${highSpeedStatus}"))
              sendEvent(name: "spillWay", value: OnOffconvert("${spillwayStatus}"))
              sendEvent(name: "blower", value: OnOffconvert("${airBlowerStatus}"))
              
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
              //('1'..'2').each {        
                  def pump = pumps[it]
                  if (state.debug_pump) log.debug "pump ${it} = ${pump}"
                  //if (state.debug_pump) log.info "Pump " + it + " Data -- " + pump            
                  if (state.debug_pump) log.info "Pump name = " + pump.name
                  if (state.debug_pump) log.info "friendlyName = " + pump.friendlyName
                  //sendEvent(name: "PumpHdr${it}", value: "${pump.friendlyName} Pump")
                  sendEvent(name: "${pump.name}", value: "${pump.friendlyName}\n---Pump---\nWatts :${pump.watts} \nRPM :${pump.rpm} \nError :${pump.err}\nState :${pump.drivestate}\nMode :${pump.run}")
                                                   //sendEvent(name: "${pump.name}", value: "Watts :${pump.watts} \nRPM :${pump.rpm} \nError :${pump.err}\nState :${pump.drivestate}\nMode :${pump.run}")
              } 
              break
        case "chlorinator":
              def chlor = json.chlorinator
              //log.info "chlor = ${chlor}"
              break
        case "config":
              def config = json.config
              if (state.debug) log.debug "config = ${config}"
              def state = OnOffconvert(config.systemReady)
              sendEvent(name: "config", value: "${state}")
              break
        case "UOM":
              state.uom = json.UOM.UOMStr
              //log.info "uom = ${state.uom}"
              break
        case "valve":
              def valve = json.valve
              //log.info "valve = ${valve}"
              break
        case "intellichem":
              def intellichem = json.intellichem
              //log.info "intell = ${intell}"
              break
        case "schedule":
           	  if (state.debug) log.info "### parse : schedule ##"
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
              
              schedule.keySet().each {
                  space = ""
                  def event = schedule[it]
                  def ID = event.ID
                  def CIRCUIT = event.CIRCUIT
                  def MODE = event.MODE
                  if (MODE == "Egg Timer") {
                      //log.warn "Event is an Egg Timer"
                      def DURATION = event.DURATION
                      circuitSize = 16 - CIRCUIT.size()
                      for (i = 0; i <circuitSize; i++) {
                     	 space = space + " "
                      }
                      eggSchedule = eggSchedule + "${ID}${space}${CIRCUIT}${space}${DURATION}\n"
                  } else if (CIRCUIT != "NOT USED") {
                      //log.warn "Event is a Schedule"
                      circuitSize = 16 - CIRCUIT.size()
                      for (i = 0; i <circuitSize; i++) {
                      	 space = space + " "
                      }
                	  def START_TIME = event.START_TIME
                	  def END_TIME = event.END_TIME
                	  def DAYS = event.DAYS
                      def day_list = DAYS.split(" ")
                      def days = []
                      day_list.each {
                          days << it.substring(0,3)
                      }    
                      fullSchedule = fullSchedule + "${ID}${space}${CIRCUIT}${space}${START_TIME}${space}${END_TIME}\nDAYS:${days}\n\n"
                  }
  			 }	
             sendEvent(name: "Schedule", value: "${fullSchedule}")
             sendEvent(name: "EggTimer", value: "${eggSchedule}")
            
           	 break
		default:
            log.info "### parse : default ###"
            log.info "key = ${key}"
		}  
     }   
}  

def OnOffconvert(value) {
    if (value == "0")
       return ("off")
    if (value == "1")   
       return ("on")
    if (value == 0)
    	return ("OFF")
    if (value == 1)
        return ("ON")
}

def setFeature(query) {
    //log.debug "----- setFeature : query = ${query} -------"
    def userpass = encodeCredentials(username, password)
    def headers = getHeader(userpass)
    def dni = setDeviceNetworkId("${controllerIP}","${controllerPort}")
     
    def poolAction = new physicalgraph.device.HubAction(
		method: "GET",
		path: query,
        headers: headers,
        dni
	)
    
    log.info "poolAction = ${poolAction}"
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
	def locationScale = getTemperatureScale()
	def deviceScale = (state.scale == 1) ? "F" : "C" 
    setSpaHeatPoint(targetValue)  
}

def setSpaHeatPoint(value) {
    def action = setFeature("/spaheat/setpoint/${value}")
    sendHubCommand(action)
}
// commands

def setuom() {
    log.debug "setuom"
}

def setPoint(num) {
	log.debug "setSpaHeatPoint : num = ${num}"
    def action = setFeature("/spaheat/setpoint/${num}")
    sendHubCommand(action)
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
	log.info "--------- spaMode Toggle ----------"
    def mode = device.currentValue("spaMode")
    def num = ""
    def value = ""
    //log.info "mode = ${mode}"    
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
        
    //log.info "new mode = ${value}"
    //log.info "num = ${num}"
	def action = setFeature("/spaheat/mode/${num}")
    sendEvent(name: "spaMode", value: "${value}")
    sendHubCommand(action)
}

def testToggle() {
    //log.error "TEST"
    def action = setFeature("/device")
    sendHubCommand(action)
}

def spaToggle() {
    // turns spa heater on/off
	log.error "Executing 'spaToggle'"
    def action = setFeature("/circuit/${state.circuit['spa']}/toggle/")
    sendHubCommand(action)
}

def blowerToggle() {
	log.error "Executing 'blowerToggle'"  
    def action = setFeature("/circuit/${state.circuit['blower']}/toggle/")
    sendHubCommand(action)
}

def poolLightToggle() {
	log.error "Executing 'poolLightToggle'"
    def action = setFeature("/circuit/${state.circuit['poolLight']}/toggle/")
    sendHubCommand(action)
}

def spaLightToggle() {
	log.error "Executing 'spaLightToogle'"
    def action = setFeature("/circuit/${state.circuit['spaLight']}/toggle/")
    sendHubCommand(action)
}

def cleanerToggle() {
	log.error "Executing 'cleanerToogle'"
    def action = setFeature("/circuit/${state.circuit['cleaner']}/toggle/")
    sendHubCommand(action)
}

def poolToggle() {
	log.error "Executing 'poolToogle'"
    def action = setFeature("/circuit/${state.circuit['pool']}/toggle/")
    sendHubCommand(action)
}

def highspeedToggle() {
	log.error "Executing 'highspeedToggle'"
    def action = setFeature("/circuit/${state.circuit['highSpeed']}/toggle/")
    sendHubCommand(action)
}

def spillWayToggle() {
	log.error "Executing 'spillWayToogle'"
    def action = setFeature("/circuit/${state.circuit['spillway']}/toggle/")
    sendHubCommand(action)
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