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
	}
   
    preferences {
       	section("Select your controller") {
       		input "controllerIP", "text", title: "Controller hostname/IP", required: true
       		input "controllerPort", "port", title: "Controller port", required: true, defaultValue: "3000"
            input "username", "string", title:"Username", description: "username", required: true, displayDuringSetup: true
            input "password", "password", title:"Password", description: "Password", required: true, displayDuringSetup: true
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
        valueTile("timedate", "device.timedate", width: 2, height: 2, decoration: "flat") {
        //standardTile("timedate", "device.timedate", width: 2, height: 2, decoration: "flat") {
			state "Idle", label:'${currentValue}', defaultState: true, action:"setdatetime", nextState: "Active", backgroundColor: "#ffffff"
            state "Active", label:'updaing time', defaultState: true, action:"setdatetime", nextState: "Idle", backgroundColor: "#cccccc"
		}        
        valueTile("freeze", "device.freeze", width:2, height: 2) {
        	state "val", label:'${currentValue}', defaultState: true
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
			state "off", label: 'blower ${name}', action: "blowerToggle", icon: "st.vents.vent", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'blower ${name}', action: "blowerToggle", icon: "st.vents.vent-open", backgroundColor: "#79b821", nextState: "off"
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
        valueTile("blank", "device.blank", width: 4, height: 2) {
			state "blank", label:'', defaultState: true
		}     
        
        //////////////////////////////////////////////////
        
        valueTile("Pump 1", "device.Pump 1", width: 3, height: 4) {
			state "val", label:'${currentValue}', defaultState: true
		}     
        valueTile("Pump 2", "device.Pump 2", width: 3, height: 4) {
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
        
        valueTile("Schedule", "device.Schedule", width: 6, height: 7, type:"generic", decoration:"flat") {
			state "val", label:'${currentValue}', defaultState: false
		}
        valueTile("EggTimer", "device.EggTimer", width: 6, height: 3, type:"generic", decoration: "flat") {
			state "val", label:'${currentValue}', defaultState: false
		}  

        //////////////////////////////////////////////////  
        // spa tile turns on heater
        // use thermostatFull to change heater set point
        
        standardTile("spa", "device.spa", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'Spa', action: "spaUnknown", icon: "st.Bath.bath4", backgroundColor: "#F2F200"
			state "off", label: '${currentValue°}', action: "spaToggle", backgroundColor: "#ffffff", nextState: "on", icon: "st.Bath.bath4" //,icon: "st.thermostat.heat"
			state "on", label: '${currentValue°}', action: "spaToggle", backgroundColor: "#79b821", nextState: "off", icon: "st.Bath.bath4" //,icon: "st.thermostat.heating"
        }
        
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
        "refresh", 		"timedate", 	"freeze", 
        "poolHeatMode", "spaHeatMode",
        "airTemp", "poolTemp", "spaTemp",
        "poolLight", "spaLight", "highspeed", 
        "cleaner", "spillWay", "blower", 
        "spaDown","heatingSetpoint","spaUp",
        "pool","setschedule", "delschedule", 
        "thermostatFullspa",        
        "Pump 1","Pump 2",
        "EggTimer",        
        "Schedule"
 		])
	}
}

def updated() {
    log.info "######### UPDATED #########"
    //unsubscribe()
    initialize()
}

def installed() {
	log.info "########## In installed ###########"
    initialize()
    setDeviceNetworkId("${controllerIP}","${controllerPort}")
    sch_dow = []
    
}

def initialize() {
	runEvery1Minute(refresh)
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
}

def refresh() {
    log.warn "Requested a refresh"
    poll()
    sendEvent(name: "refresh", isStateChange: "true")
}

def poll() {
	// poll gets /all status messages from pool controller (raspberry Pi)
    def userpass = encodeCredentials(username, password)
    def headers = getHeader(userpass)
    
    def dni = setDeviceNetworkId("${controllerIP}","${controllerPort}")
    //log.warn "IP : ${controllerIP} - Port : ${controllerPort}"
    def poolAction = new physicalgraph.device.HubAction(
		method: "GET",
		path: "/all",
        headers: headers,
        dni,
        query: []
	)   
    sendHubCommand(poolAction)
}

def delSchedule() {
   // app.get('/schedule/delete/id/:id'
   // 'REST API received request to delete schedule or egg timer with ID:' + id;
   if (!sch_id) return
   def action = setFeature("/schedule/delete/id/${sch_id}") 
   sendHubCommand(action)
}

def get_hour(time) {
   String hour = time.format('HH')
   return hour
}

def get_minute(time) {
   String minute = time.format('mm')
   return minute
}

def calc_dow() {
/*
    def day_value = [Sunday:1,Monday:2,Tuesday:4,Wednesday:8,Thursday:16,Friday:32,Saturday:64] 
    def list = days.split(",")
    log.debug "list = ${list}"
    def calc = 0
    def index
    //list.each {
    def end = list.size()-1
    (0..end).each {
       index = list[it]
       log.debug "index = ${index}"
       //log.debug day_value[index]         
       calc = calc + day_value[index]
    }
    log.debug "calc = "+ calc
*/    
    
    def dayValueMap = [Sunday:1,Monday:2,Tuesday:4,Wednesday:8,Thursday:16,Friday:32,Saturday:64]    
    def dayMap = ["Sunday":Sunday, 
                     "Monday":Monday, 
                     "Tuesday":Tuesday, 
                     "Wednesday":Wednesday, 
                     "Thursday":Thursday, 
                     "Friday":Friday, 
                     "Saturday":Saturday]
    def dayList = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
    
    def calc = 0

    dayList.each {
       log.debug "it = ${it}"
       if (dayMap[it]) {        
          calc = calc + dayValueMap[it]
       }   
    }   
    log.debug "calc = ${calc}"
    
    return (calc)
}

def get_circuit_num(name) {
/*
    def circuit = [
        spa:'1',
        blower:'2',
        poolLight:'3',
        spaLight:'4',
        cleaner:'5',
        pool:'6',
        highSpeed:'7',
        spillway:'8'
        ]
 */       
    log.debug "get_circuit_num: name = ${name}, number = ${state.circuit[name]}"    
    return state.circuit[name]
}

// API calls in server.js file on https://github.com/tagyoureit/nodejs-poolController/blob/b3d31a94daf486058d9a8d8043d9039d3a459487/src/lib/comms/server.js
// curl -X GET http://user:pass@pi-pool:3000/schedule | jq '.schedule | ."7"'
// curl -X GET http://user:pass@pi-pool:3000/schedule/set/7/3/21/00/22/00/127
// curl -X GET http://user:pass@pi-pool:3000/schedule/set/id/7/circuit/3
def addSchedule() {
    if (!sch_circuit | !sch_id) return
    //def sch_circuit_num = get_circuit_num(sch_circuit)
    def sch_circuit_num = state.circuit[sch_circuit]
    if (EggTimer) {
       if (!sch_id) return 
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
       log.debug "start time: ${sch_starthh}, ${sch_startmm}"
       log.debug "end time: ${sch_endhh}, ${sch_endmm}"
       def sch_dow_num = calc_dow()
        
       // ----- setFeature : query = /schedule/set/7/3/21/00/22/00/127 -------
       // Response: REST API received request to set schedule 7 with values (start) 21:0 (end) 22:0 with days value 127
       // Response: REST API received request to set circuit on schedule with ID (7) to POOL LIGHT
 
       def scheduleCircuit = setFeature("/schedule/set/id/${sch_id}/circuit/${sch_circuit_num}")
       log.debug "scheduleCircuit = ${scheduleCircuit}"
       log.debug "schedule set id:${sch_id} to circuit:${sch_circuit_num}"    
       sendHubCommand(scheduleCircuit)
             
       def addschedule = setFeature("/schedule/set/${sch_id}/${sch_circuit_num}/${sch_starthh}/${sch_startmm}/${sch_endhh}/${sch_endmm}/${sch_dow_num}")
       log.debug "addschedule = ${addschedule}"
       sendHubCommand(addschedule)
    }
}

def parse(String description) {
	log.trace "Parse"
    log.debug "device = ${device}"
    log.debug "id = ${device.id} hub = ${device.hub} data = ${device.data}"
    def msg = parseLanMessage(description)
    // version 4 requires pump number be a string, version 3 used integers 
    /*
    def circuit = [
        spa:'1',
        blower:'2',
        poolLight:'3',
        spaLight:'4',
        cleaner:'5',
        pool:'6',
        highSpeed:'7',
        spillway:'8'
        ]
    */
    def json = msg.json
    
    //process response from toggle commands
    if (json.text) { 
              log.warn "Response: ${json.text}"
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
              //} else if (json.text.contains("set spa heat setpoint")) {
              //	  sendEvent(name: "heatingSetpoint", value: "${json.status}")
              //} else if (json.text.contains("toggle SPA to")) {
              //	  log.debug "status = ${json.status}"
              //    sendEvent(name: "heatingSetpoint", value: "${json.status}")
              } else if (json.text.contains("POOL")) {
                  sendEvent(name: "pool", value: "${json.status}")
              } else if (json.text.contains("BLOWER")) {
                  sendEvent(name: "blower", value: "${json.status}")
              }
      } else {  //process response from poll      
    
  	  msg.data.keySet().each {
        //log.info "parse:case switch on : ${it}"
        switch (it) {
        case "time":
            log.info "### parse : time ###"
            def time = msg.data.get(it)
            log.info "time = ${time}"
            def controllerTime = time.controllerTime
            def controllerDate = time.controllerDateStr
            def controllerDoW = time.controllerDayOfWeekStr
            sendEvent(name: "timedate", value: "Time: ${controllerTime}\n Date: ${controllerDate}\n ${controllerDoW}")
            break
        case "heat":  
            log.info "### parse : heat ###"
            def heat = msg.data.get(it)
            def poolSetPoint = heat.poolSetPoint
            def poolHeatMode = heat.poolHeatMode
            def poolHeatModeStr = heat.poolHeatModeStr
            def spaSetPoint = heat.spaSetPoint
            def spaHeatMode = heat.spaHeatMode
            def spaHeatModeStr = heat.spaHeatModeStr
            def heaterActive = heat.heaterActive 
            log.info "spaSetPoint = ${spaSetPoint}"
            sendEvent(name: "temperature", value: "${spaSetPoint}")
            sendEvent(name: "heatingSetpoint", value: "${spaSetPoint}°")
            sendEvent(name: "spaHeatMode", value: "Spa Heat Mode: ${spaHeatModeStr}\nSpa Set Point: ${spaSetPoint}°")
            sendEvent(name: "poolHeatMode", value: "Pool Heat Mode: ${poolHeatModeStr}\nPool Set Point: ${poolSetPoint}°")
            sendEvent(name: "spaMode", value: "${spaHeatModeStr}")
            break
        case "temperature":
            log.info "### parse : temperatures ###"
            def temperatures = msg.data.get(it)          
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
            sendEvent(name: "freeze", value: "Freeze Protect:\n${freeze}")
            log.info "spaSetPoint = ${spaSetPoint}"
            sendEvent(name: "spaHeatMode", value: "Spa Heat Mode: ${spaHeatModeStr}\nSpa Set Point: ${spaSetPoint}°")
            sendEvent(name: "poolHeatMode", value: "Pool Heat Mode: ${poolHeatModeStr}\nPool Set Point: ${poolSetPoint}°")
            sendEvent(name: "spaMode", value: "${spaHeatModeStr}")
            sendEvent(name: "heatingSetpoint", value: "${spaSetPoint}")
            sendEvent(name: "temperature", value: "${spaSetPoint}")
            break
        case "circuit":
              log.info "### parse : circuits ###" 
              def cir = msg.data.get(it)
              def circuits = msg.data.get(it) 
              log.info "******** circuits = ${circuits} ************"
              if (!circuits) return
              def spaStatus = circuits[state.circuit.spa].status
              def airBlowerStatus = circuits[state.circuit.blower].status
              def poolLightStatus = circuits[state.circuit.poolLight].status
              def spaLightStatus = circuits[state.circuit.spaLight].status
              def cleanerStatus = circuits[state.circuit.cleaner].status
              def poolStatus = circuits[state.circuit.pool].status
              def highSpeedStatus = circuits[state.circuit.highSpeed].status
              def spillwayStatus = circuits[state.circuit.spillway].status                   
           
              log.info "poolLightStatus: ${poolLightStatus}\n, spaLightStatus: ${spaLightStatus}\n, poolStatus: ${poolStatus}\n, spaStatus: ${spaStatus}"
              log.info "cleanerStatus: ${cleanerStatus}\n, spillwayStatus: ${spillwayStatus}\n, blowerStatus: ${airBlowerStatus}\n, highSpeedStatus: ${highSpeedStatus}"
                       
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
              
              //log.info "heatvalue = ${heatvalue}"
              sendEvent(name: "thermostatOperatingState" , value: "${heatvalue}")                         
        	  break
        case "pump":      
           //log.info "### parse : pumps ###"
           //log.debug "-#-#- ${it} -> " + msg.data.get(it)
           def pdata = msg.data.get(it)
           def myit = it
           //log.info "myit = " + myit     
           // ver 3 requires (1..2), were as ver 4 needs ('1'..'2')
           ('1'..'2').each {
             //log.info "it =" + it
             def pump = pdata[it]
             log.info "Pump " + it + " Data -- " + pump            
             log.info "####### pump: ${pump}"
             log.info "Pump name = " + pump.name
             log.info "friendlyName = " + pump.friendlyName
             sendEvent(name: "${pump.name}", value: "${pump.friendlyName}\n---------\nWatts :${pump.watts} \nRPM :${pump.rpm} \nError :${pump.err}\nState :${pump.drivestate}\nMode :${pump.run}")
           } 
           break
        case "schedule":
        	// This creates a pretty ugly output of schedules and egg timers
           	//log.info "### parse : schedule ##"
            
           	def schedule = msg.data.get(it)
            log.info "schedule = ${schedule}"
            def fullSchedule = "----- SCHEDULE -----\n\n#\t\t\tCircuit\t\t\tStartTime\t\t\tEndTime\n"
            fullSchedule = fullSchedule + "----------------------------------\n"
            def eggSchedule = "----- EGG TIMER -----\n\n#\t\tCircuit\t\tDuration\n"
            eggSchedule = eggSchedule + "-----------------------------------\n"
            //def TAB3 = "\t\t\t"
            //def TAB2 = "\t\t"
            def TAB3 = "\t\t\t"
            def TAB2 = "\t\t"
            for ( i in ["1","2","3","4","5","6","7","8","9","10","11","12"] ) {
           		def event = schedule[i]
                log.info "event = ${event}"
                def ID = event.ID
                def CIRCUIT = event.CIRCUIT
                def MODE = event.MODE
                if (MODE == "Egg Timer") {
                	def DURATION = event.DURATION
                    if (CIRCUIT.size() > 4) {
                    	eggSchedule = eggSchedule + "${ID}${TAB2}${CIRCUIT}${TAB2}${DURATION}\n"
                    } else {
                		eggSchedule = eggSchedule + "${ID}${TAB3}${CIRCUIT}${TAB3}${DURATION}\n"
                    }    
                } else if (CIRCUIT != "NOT USED") {
                	def START_TIME = event.START_TIME
                	def END_TIME = event.END_TIME
                	def DAYS = event.DAYS
                	fullSchedule = fullSchedule + "${ID}\t\t\t${CIRCUIT}\t\t\t${START_TIME}\t\t\t${END_TIME}\n ${DAYS}\n\n"
                }
  			}	
            sendEvent(name: "Schedule", value: "${fullSchedule}")
            sendEvent(name: "EggTimer", value: "${eggSchedule}")
            
           	break
		default:
			log.info "### parse:default"
			log.info "it = ${it}"
		}  
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

def convertDow(dow) {
     switch (dow) {
        case "Sun":
           return 1
           break
        case "Mon":
           return 2
           break
        case "Tue":
           return 4
           break
        case "Wed":
           return 8
           break
        case "Thu":
           return 16
           break
        case "Fri":
           return 32
           break
        case "Sat":
           return 64
           break   
     }    
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

    log.debug "In setdatetime"
    
    //def controllerTime = new Date().format("yyyy-MM-dd'T'HH:mm")
    log.debug "controllerTime = ${controllerTime}"
    log.debug "to string = "+controllerTime.toString()
    //def downum = convertDow(dow)
    
    //        controllerTime = 2018-06-18T14:10:24.000-0500
    Date newDate = Date.parse("yyyy-MM-dd'T'HH:mm",controllerTime)  
    log.debug "newDate = ${newDate}"
    String year = newDate.format('yy')
    String month = newDate.format('MM')
    String day = newDate.format('dd')
    String hour = newDate.format('HH')
    String minute = newDate.format('mm')
    String DayOfWeek = newDate.format('EE')
    def downum = convertDow(DayOfWeek)
       
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

def setFeature(query) {
    log.debug "----- setFeature : query = ${query} -------"
    def userpass = encodeCredentials(username, password)

    def headers = getHeader(userpass)
 
    def dni = setDeviceNetworkId("${controllerIP}","${controllerPort}")
     
    def poolAction = new physicalgraph.device.HubAction(
		method: "GET",
		path: query,
        headers: headers,
        dni
	)
    
	return poolAction
}

def setSpaHeatPoint(num) {
	log.debug "setSpaHeatPoint : num = ${num}"
    def action = setFeature("/spaheat/setpoint/${num}")
    sendHubCommand(action)
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

// handle Toggle commands 
// Set spa heat mode: /spaheat/mode/# (0=off, 1=heater, 2=solar pref, 3=solar only) 

// currently not used, tile has been disabled
def spaModeToggle() {
	log.info "--------- spaMode Toggle ----------"
    def mode = device.currentValue("spaMode")
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
        
    log.info "new mode = ${value}"
    log.info "num = ${num}"
	setFeature("/spaheat/mode/${num}")
    sendEvent(name: "spaMode", value: "${value}")
}

def spaToggle() {
    // turns spa heater on/off
	log.info "Executing 'spaToggle'"
	//setFeature("/circuit/1/toggle/")
    setFeature("/circuit/${state.circuit['spa']}/toggle/")
}

def blowerToggle() {
	log.info "Executing 'blowerToggle'"  
	//setFeature("/circuit/2/toggle/")
    setFeature("/circuit/${state.circuit['blower']}/toggle/")
}

def poolLightToggle() {
	log.info "Executing 'poolLightToggle'"
    setFeature("/circuit/${state.circuit['poolLight']}/toggle/")
}

def spaLightToggle() {
	log.info "Executing 'spaLightToogle'"
	//setFeature("/circuit/4/toggle/")
    setFeature("/circuit/${state.circuit['spaLight']}/toggle/")
}

def cleanerToggle() {
	log.info "Executing 'cleanerToogle'"
	//setFeature("/circuit/5/toggle/")
    setFeature("/circuit/${state.circuit['cleaner']}/toggle/")
}

def poolToggle() {
	log.info "Executing 'poolToogle'"
	//setFeature("/circuit/6/toggle/")
    setFeature("/circuit/${state.circuit['pool']}/toggle/")
}

def highspeedToggle() {
	log.info "Executing 'highspeedToggle'"
	//setFeature("/circuit/7/toggle/")
    setFeature("/circuit/${state.circuit['highSpeed']}/toggle/")
}

def spillWayToggle() {
	log.info "Executing 'spillWayToogle'"
	//setFeature("/circuit/8/toggle/")
    setFeature("/circuit/${state.circuit['spillway']}/toggle/")
}

// private functions

private delayAction(long time) {
    new physicalgraph.device.HubAction("delay $time")
}

private setDeviceNetworkId(ip,port){
      def iphex = convertIPtoHex(ip)
      def porthex = convertPortToHex(port)
      device.deviceNetworkId = "$iphex:$porthex"
      //log.debug "Device Network Id set to ${iphex}:${porthex}"
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