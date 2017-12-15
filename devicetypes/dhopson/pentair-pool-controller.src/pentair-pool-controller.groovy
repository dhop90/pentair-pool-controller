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
	}

	preferences {
       	section("Select your controller") {
       		input "controllerIP", "text", title: "Controller hostname/IP", required: true
       		input "controllerPort", "port", title: "Controller port", required: true
            input "username", "string", title:"Username", description: "username", required: false, displayDuringSetup: true
            input "password", "password", title:"Password", description: "Password", required: false, displayDuringSetup: true
 		}
    }
    
	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {
    	// Display time and date from pool controller
        valueTile("timedate", "device.timedate", width: 2, height: 2) {
			state "val", label:'${currentValue}', defaultState: true
		}
        
        valueTile("freeze", "device.freeze", width:2, height: 2) {
        	state "val", label:'${currentValue}', defaultState: true
        }
        valueTile("poolHeatMode", "device.poolHeatMode", width:3, height: 2) {
        	state "val", label:'${currentValue}', defaultState: true
        }
        valueTile("spaHeatMode", "device.spaHeatMode", width:3, height: 2) {
        	state "val", label:'${currentValue}', defaultState: true
        }
        
        // Air, Pool and Spa Temperature
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
        
        // Lights
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

        // spa tile turns on heater
        // user thermostatFull below to change heater set point
        
        standardTile("spa", "device.spa", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'Spa', action: "spaUnknown", icon: "st.Bath.bath4", backgroundColor: "#F2F200"
			state "off", label: '${currentValue°}', action: "spaToggle", backgroundColor: "#ffffff", nextState: "on", icon: "st.Bath.bath4" //,icon: "st.thermostat.heat"
			state "on", label: '${currentValue°}', action: "spaToggle", backgroundColor: "#79b821", nextState: "off", icon: "st.Bath.bath4" //,icon: "st.thermostat.heating"
        }
          
        valueTile("Schedule", "device.Schedule", width: 6, height: 5, type:"generic", decoration:"flat") {
			state "val", label:'${currentValue}', defaultState: false
		}
        valueTile("EggTimer", "device.EggTimer", width: 6, height: 3, type:"generic", decoration: "flat") {
			state "val", label:'${currentValue}', defaultState: false
		}  

        // Widget to change heating set point

        // PRIMARY_CONTROL	Used to display the current temperature.
		// VALUE_CONTROL	Renders controls for increasing or decreasing the temperature.
		// SECONDARY_CONTROL	Used to display textual data about the thermostat, like humidity. Appears on the bottom-left of the tile.
		// OPERATING_STATE	What the thermostat is doing					The label will not show if OPERATING_STATE is omitted, as this is the baseline amount of meaningful information
		// THERMOSTAT_MODE	Thermostat Mode (i.e. Heat, Cool, or Auto)		This allows the user to know the Mode (and temperature) if the system is idle (e.g. “Idle—Heat at 66°”)
		// HEATING_SETPOINT	At which point the system will begin heating	Informs the user when heating will start (or stop, if currently heating)
		// COOLING_SETPOINT	At which point the system will begin cooling	Informs the user when cooling will start (or stop, if currently cooling)

        ///////////////////////////////
        
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
        
     /* 
     	# Uncomment if you want to control heating of the pool and not just the spa
        
 		multiAttributeTile(name:"thermostatFullpool", type:"thermostat", width:3, height:2) {
        	// center
    		tileAttribute("device.pooltemperature", key: "PRIMARY_CONTROL") {
        		attributeState("temp", label:'${currentValue}°', unit:"dF", defaultState: true)
    		}
            // right up/down
   			tileAttribute("device.pooltemperature", key: "VALUE_CONTROL") {
        		attributeState("VALUE_UP", action: "tempUp")
        		attributeState("VALUE_DOWN", action: "tempDown")
    		}
            // changes background color
    		tileAttribute("device.poolthermostatOperatingState", key: "OPERATING_STATE") {
        		attributeState("idle", backgroundColor:"#00A0DC", label: '${name}')
       	 		attributeState("heating", backgroundColor:"#e86d13", label: '${name}')
    		}
            // bottom center
    		tileAttribute("device.poolHeatMode", key: "THERMOSTAT_MODE") {
                attributeState("Off", label:'${name}')
        		attributeState("Heater", label:'${name}')
                attributeState("Solar Pref", label:'${name}')
                attributeState("Solar Only", label:'${name}')
    		}
            // Not sure what this does
    		tileAttribute("device.poolheatingSetpoint", key: "HEATING_SETPOINT") {
        		attributeState("poolheatingSetpoint", label:'HS ${currentValue}°', unit:"dF", defaultState: true)
    		}
            // lower left corner
            tileAttribute("device.poolTemp", key: "SECONDARY_CONTROL") {
        		attributeState("poolTemp", label:'${currentValue}°', defaultState: true)
    		}
		} 
    */
        
        
        ////////////////////////////////
        
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
        
        // Features
        
        // highspeed
        standardTile("highspeed", "device.highspeed", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'Highspeed', action: "highspeedUnknown", icon: "st.thermostat.thermostat-right", backgroundColor: "#F2F200"
			state "off", label: 'Highspeed ${currentValue}', action: "highspeedToggle", icon: "st.thermostat.thermostat-right", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Highspeed ${currentValue}', action: "highspeedToggle", icon: "st.thermostat.thermostat-right", backgroundColor: "#79b821", nextState: "off"
		}
        
        // cleaner
        standardTile("cleaner", "device.cleaner", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'Cleaner', action: "cleanerUnknown", icon: "st.Appliances.appliances2", backgroundColor: "#F2F200"
			state "off", label: 'Cleaner ${currentValue}', action: "cleanerToggle", icon: "st.Appliances.appliances2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Cleaner ${currentValue}', action: "cleanerToggle", icon: "st.Appliances.appliances2", backgroundColor: "#79b821", nextState: "off"
		}
        
        // spillWay
        standardTile("spillWay", "device.spillWay", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'SpillWay', action: "spillWayUnknown", icon: "st.Bath.bath13", backgroundColor: "#F2F200"
			state "off", label: 'SpillWay ${currentValue}', action: "spillWayToggle", icon: "st.Bath.bath13", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'SpillWay ${currentValue}', action: "spillWayToggle", icon: "st.Bath.bath13", backgroundColor: "#79b821", nextState: "off"
		}
        
        // blower
        standardTile("blower", "device.blower", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'blower', action: "blowerUnknown", icon: "st.vents.vent", backgroundColor: "#F2F200"
			state "off", label: 'blower ${name}', action: "blowerToggle", icon: "st.vents.vent", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'blower ${name}', action: "blowerToggle", icon: "st.vents.vent-open", backgroundColor: "#79b821", nextState: "off"
		}
        
        // Pumps
        valueTile("Pump 1", "device.Pump 1", width: 2, height: 2) {
			state "val", label:'${currentValue}', defaultState: true
		}     
        valueTile("Pump 2", "device.Pump 2", width: 2, height: 2) {
			state "val", label:'${currentValue}', defaultState: true
		}
        
        // blank
        valueTile("blank", "device.blank", width: 4, height: 2) {
			state "blank", label:'', defaultState: true
		}

        // refresh
        standardTile("refresh", "device.refresh", width: 2, height: 2) {
        	state "Idle", label:'refresh', action:"refresh", icon:"st.secondary.refresh-icon", nextState: "Active", backgroundColor: "#ffffff"
            state "Active", label:'refresh', action:"refresh", icon:"st.secondary.refresh-icon", nextState: "Idle", backgroundColor: "#cccccc"
    	}
        
        // Up/Down tiles
        standardTile("spaUp", "device.spaUp", width: 2, height: 2) {
			state "up", label: 'Up', action: "tempUp",icon: "st.thermostat.thermostat-up",nextState: "push", backgroundColor: "#ffffff"
			state "push", label: 'Up', action: "tempUp",icon: "st.thermostat.thermostat-up",nextState: "up", backgroundColor: "#cccccc"
    	}
        
        // HeatingSetpoint
 		// Turns Spa heat on/off
        // Can use thermostatFull to change heater set point
        standardTile("heatingSetpoint", "device.heatingSetpoint", width: 2, height: 2, canChangeBackground: true, key: "HEATING_SETPOINT") {
			 state "off", label: '${name}: ${currentValue}', action: "spaToggle", unit: "dF", icon: "st.Bath.bath4", backgroundColor: "#ffffff", nextState: "on"
			 state "on" , label: '${name}: ${currentValue}', action: "spaToggle", unit: "dF", icon: "st.Bath.bath4", backgroundColor: "#79b821", nextState: "off"
		}
        
        standardTile("spaDown", "device.spaDown", width: 2, height: 2) {
			state "down", label: 'Down', action: "tempDown",icon: "st.thermostat.thermostat-down",nextState: "push", backgroundColor: "#ffffff"
  			state "push", label: 'Down', action: "tempDown",icon: "st.thermostat.thermostat-down",nextState: "down", backgroundColor: "#cccccc"           
		}
        
        standardTile("poolUp", "device.poolUp", width: 2, height: 2) {
			state "up", label: 'Up', action: "tempUp", backgroundColor: "#ffffff",icon: "st.thermostat.thermostat-up"
		}
        
        standardTile("poolDown", "device.poolDown", width: 2, height: 2) {
			state "down", label: 'Down', action: "tempDown", backgroundColor: "#ffffff",icon: "st.thermostat.thermostat-down"
		}
        
        main(["poolTemp"])

        details(["refresh","timedate", "freeze", "poolHeatMode", "spaHeatMode",
        		"airTemp", "poolTemp", "spaTemp",
        		"poolLight", "spaLight", "highspeed", 
                "cleaner", "spillWay", "blower", 
                "spaDown","heatingSetpoint","spaUp",
                "pool","Pump 1","Pump 2",
                "thermostatFullspa",
                "Schedule","EggTimer"
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
}

def initialize() {
	runEvery1Minute(refresh)
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
      
def parse(String description) {
	//log.trace "Parse"
    //log.debug "device = ${device}"
    //log.debug "id = ${device.id} hub = ${device.hub} data = ${device.data}"
    def msg = parseLanMessage(description)
    // version 4 requires pump number be a string, version 3 used integers 
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
            //log.info "### parse : time ###"
            def time = msg.data.get(it)
            def controllerTime = time.controllerTime
            def controllerDate = time.controllerDateStr
            sendEvent(name: "timedate", value: "Time: ${controllerTime}\n Date: ${controllerDate}")
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
        case "temperatures":
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
        case "circuits":
              log.info "### parse : circuits ###" 
              def cir = msg.data.get(it)
              def circuits = msg.data.get(it)                 
              def spaStatus = circuits[circuit.spa].status
              def airBlowerStatus = circuits[circuit.blower].status
              def poolLightStatus = circuits[circuit.poolLight].status
              def spaLightStatus = circuits[circuit.spaLight].status
              def cleanerStatus = circuits[circuit.cleaner].status
              def poolStatus = circuits[circuit.pool].status
              def highSpeedStatus = circuits[circuit.highSpeed].status
              def spillwayStatus = circuits[circuit.spillway].status                   
           
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
        case "pumps":      
           //log.info "### parse : pumps ###"
           //log.debug "-#-#- ${it} -> " + msg.data.get(it)
           def pdata = msg.data.get(it)
           def myit = it
           //log.info "myit = " + myit     
           // ver 3 requires (1..2), were as ver 4 needs ('1'..'2')
           ('1'..'2').each {
             //log.info "it =" + it
             def pump = pdata[it]
             //log.info "Pump " + it + " Data -- " + pump            
             //log.info "####### pump: ${pump}"
             //log.info "Pump name = " + pump.name         
             sendEvent(name: "${pump.name}", value: "--- ${pump.name} ---\nWatts :${pump.watts} \nRPM :${pump.rpm} \nError :${pump.err}\nState :${pump.drivestate}\nMode :${pump.run}")
           } 
           break
        case "schedule":
        	// This creates a pretty ugly output of schedules and egg timers
           	//log.info "### parse : schedule ##"
           	def schedule = msg.data.get(it)
            def fullSchedule = "#\t\t\tCircuit\t\t\tStartTime\t\t\tEndTime\n"
            fullSchedule = fullSchedule + "----------------------------------\n"
            def eggSchedule = "#\t\tCircuit\t\tDuration\n"
            eggSchedule = eggSchedule + "-----------------------------------\n"
            def TAB3 = "\t\t\t"
            def TAB2 = "\t\t"
            for ( i in ["1","2","3","4","5","6","7","8","9","10","11","12"] ) {
           		def event = schedule[i]           
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
    //sendHubCommand(poolAction)
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

// currently no being used
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
	setFeature("/circuit/1/toggle/")
}

def blowerToggle() {
	log.info "Executing 'blowerToggle'"  
	setFeature("/circuit/2/toggle/")
}

def poolLightToggle() {
	log.info "Executing 'poolLightToggle'"
	setFeature("/circuit/3/toggle/")
}

def spaLightToggle() {
	log.info "Executing 'spaLightToogle'"
	setFeature("/circuit/4/toggle/")
}

def cleanerToggle() {
	log.info "Executing 'cleanerToogle'"
	setFeature("/circuit/5/toggle/")
}

def poolToggle() {
	log.info "Executing 'poolToogle'"
	setFeature("/circuit/6/toggle/")
}

def highspeedToggle() {
	log.info "Executing 'highspeedToggle'"
	setFeature("/circuit/7/toggle/")
}

def spillWayToggle() {
	log.info "Executing 'spillWayToogle'"
	setFeature("/circuit/8/toggle/")
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