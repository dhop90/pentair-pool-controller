/**
 *  Pentair Pool Controller
 *
 *  Copyright 2015 Michael Usner
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
 * Fork from (name: "Pentair Controller", namespace: "michaelusner", author: "Michael Usner", oauth: true)
 */
 
metadata {
	definition (name: "Pentair Pool Controller", namespace: "dhopson", author: "David Hopson", oauth: true) {
        capability "Switch"
        //capability "Switch Level"
    	capability "Polling"        
        capability "Refresh"
        capability "Temperature Measurement"
        capability "Thermostat"

        
        command "poolToggle"
        command "spaToggle"
        command "spaModeToggle"
        command "poolLightToggle"
        command "spaLightToggle"
		command "blowerToggle"
		command "cleanerToggle"
        command "highspeedToggle"
        command "spillWayToggle"
        //command "lowerHeatingSetpoint"
		//command "raiseHeatingSetpoint"	
        
        command "tempUp"
		command "tempDown"
		command "heatUp"
		command "heatDown"
		command "coolUp"
		command "coolDown"
		command "setTemperature", ["number"]
	}

	preferences {
       	section("Select your controller") {
       		input "controllerIP", "text", title: "Controller hostname/IP", required: true
       		input "controllerPort", "port", title: "Controller port", required: true
            //input "hSetpoint", "capability.temperatureMeasurement"
            //input "username", "string", title:"Username", description: "username", required: true, displayDuringSetup: true
            //input "password", "password", title:"Password", description: "Password", required: true, displayDuringSetup: true
 		}
    }
    
	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {
        valueTile("timedate", "device.timedate", width: 2, height: 2) {
			state "val", label:'${currentValue}', defaultState: true
		}
 
        valueTile("tempData", "device.tempData", width:4, height: 2) {
        	state "val", label:'${currentValue}', defaultState: true
        }
        
        // Temp
        valueTile("airTemp",  "device.airTemp",  width: 2, height: 2, canChangeBackground: true) {
        	state("temperature", label:'${currentValue}°', icon: "st.Home.home1",
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

        // spa turns on heater
        // This changes spa state
        
        standardTile("spa", "device.spa", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'Spa', action: "spaUnknown", icon: "st.thermostat.heat", backgroundColor: "#F2F200"
			state "off", label: '', action: "spaToggle", icon: "st.thermostat.heat", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: '', action: "spaToggle", icon: "st.thermostat.heating", backgroundColor: "#79b821", nextState: "off"
		}
        
        
        // HeatingSetpoint
 		// Turns Spa heat on/off
        standardTile("heatingSetpoint", "device.heatingSetpoint", width: 2, height: 2, canChangeBackground: true, key: "HEATING_SETPOINT") {
        	//state "unknown", label: '${currentValue}° heat', action: "spaUnknown", icon: "st.Weather.weather2"
			state "off", label: '${currentValue}', action: "spaToggle", icon: "st.Weather.weather2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: '${currentValue}', action: "spaToggle", unit:"dF", icon: "st.Weather.weather2", backgroundColor: "#79b821", nextState: "off"
		}
        
        ///////////////////////////////
 		multiAttributeTile(name:"thermostatFull", type:"thermostat", width:6, height:4) {
    		tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
        		attributeState("temp", label:'${currentValue}', unit:"dF", defaultState: true)
    		}
   			tileAttribute("device.temperature", key: "VALUE_CONTROL") {
        		attributeState("VALUE_UP", action: "tempUp")
        		attributeState("VALUE_DOWN", action: "tempDown")
    		}
    		tileAttribute("device.thermostatOperatingState", key: "OPERATING_STATE") {
        		attributeState("idle", backgroundColor:"#00A0DC")
       	 		attributeState("heating", backgroundColor:"#e86d13")
    		}
    		tileAttribute("device.thermostatMode", key: "THERMOSTAT_MODE") {
        		attributeState("off", label:'${name}')
        		attributeState("heat", label:'${name}')
    		}
    		tileAttribute("device.heatingSetpoint", key: "HEATING_SETPOINT") {
        		attributeState("heatingSetpoint", label:'${currentValue}', unit:"dF", defaultState: true)
    		}
		} 
        ////////////////////////////////
        
        // spaMode : NOT USED
        /*
        standardTile("spaMode", "device.spaMode", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'SpaMode', action: "spaModeUnknown", icon: "st.thermostat.heat", backgroundColor: "#F2F200"
			state "off", label: '', action: "spaModeToggle", icon: "st.thermostat.heat", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: '', action: "spaModeToggle", icon: "st.thermostat.heating", backgroundColor: "#79b821", nextState: "off"
        }    
        */
        
        // Pool filter
        standardTile("pool", "device.pool", width: 2, height: 2, canChangeBackground: true) {
        	state "unknown", label: 'Pool', action: "poolUnknown", icon: "st.Health & Wellness.health2", backgroundColor: "#F2F200"
			state "off", label: 'Pool ${currentValue}', action: "poolToggle", icon: "st.Health & Wellness.health2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Pool ${currentValue}', action: "poolToggle", icon: "st.Health & Wellness.health2", backgroundColor: "#79b821", nextState: "off"
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
        valueTile("Pump 1", "device.Pump 1", width: 3, height: 2) {
			state "val", label:'${currentValue}', defaultState: true
		}     
        valueTile("Pump 2", "device.Pump 2", width: 3, height: 2) {
			state "val", label:'${currentValue}', defaultState: true
		}
        
        // blank
        valueTile("blank", "device.blank", width: 2, height: 2) {
			state "blank", label:'', defaultState: true
		}

        // refresh
        standardTile("refresh", "command.refresh2", width: 2, height: 2, inactiveLabel: false) {
        	state "default", label:'refresh', action:"refresh.refresh", icon:"st.secondary.refresh-icon"
    	}
        
        main(["poolTemp"])

        details(["timedate", "tempData", "thermostatFull", //"freeze",
        		"airTemp", "poolTemp","spaTemp",
                "poolLight", "spaLight", "heatingSetpoint", "pool", "highspeed", "cleaner", 
                "spillWay", "blower", "Pump 1","Pump 2", "refresh"])
	}
}

def refresh() {
    log.info "Requested a refresh"
    poll()
}

def poll() {
	// poll gets /all status messages from pool controller (raspberry Pi)
    def userpass = encodeCredentials(username, password)
    def headers = getHeader(userpass)
    
    def dni = setDeviceNetworkId("${controllerIP}","${controllerPort}")
    //log.debug "Polling for data: " + headers

    def poolAction = new physicalgraph.device.HubAction(
		method: "GET",
		path: "/all",
        headers: headers,
        dni,
        query: []
	)
    
    sendHubCommand(poolAction)
}

private encodeCredentials(username, password){
    //log.debug "Encoding credentials"
    def userpassascii = "${username}:${password}"
    def userpass = "Basic " + userpassascii.encodeAsBase64().toString()
    //log.debug "ASCII credentials are ${userpassascii}"
    //log.debug "Credentials are ${userpass}"
    return userpass
}

private getHeader(userpass){
  //log.debug "controller IP:port = ${controllerIP}:${controllerPort}"
    def headers = [:]
    headers.put("HOST", "${controllerIP}:${controllerPort}")
    headers.put("Authorization", userpass)
    //log.info "Headers are ${headers}"
    return headers
}
      
def parse(String description) {
	log.debug "Parse"
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
     
     if (json.text) { //process response from toggle command
              log.debug "Text: ${json.text}"
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
              } else if (json.text.contains("toggle SPA to")) {
                  //sendEvent(name: "spa", value: "${json.status}")
                  sendEvent(name: "heatingSetPoint", value: "${json.status}")
              } else if (json.text.contains("POOL")) {
                  sendEvent(name: "pool", value: "${json.status}")
              } else if (json.text.contains("BLOWER")) {
                  sendEvent(name: "blower", value: "${json.status}")
              }
      } else {  //process response from poll      
    
  	  msg.data.keySet().each {   
        switch (it) {
        case "time":
            log.info "### parse:time ###"
            def time = msg.data.get(it)
            def controllerTime = time.controllerTime
            def controllerDate = time.controllerDateStr
            // sendEvent
            // log.info "updating timedate Time: ${controllerTime} Date: ${controllerDate}"
            sendEvent(name: "timedate", value: "Time: ${controllerTime}\n Date: ${controllerDate}")
            log.info "### finished time"
            break
        case "heat":  
            log.info "### parse:heat ###"
            def heat = msg.data.get(it)
            def poolSetPoint = heat.poolSetPoint
            def poolHeatMode = heat.poolHeatModeStr
            def spaSetPoint = heat.spaSetPoint
            def spaHeatMode = heat.spaHeatModeStr
            def heaterActive = heat.heaterActive 
            sendEvent(name: "temperature", value: "${spaSetPoint}")
            sendEvent(name: "heatingSetpoint", value: "${spaSetPoint}")
            log.info "### finished heat"
            break
        case "temperatures":
            log.info "### parse:temperatures ###"
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
            //state.heatingSetpoint = spaSetPoint

            //log.debug "#### state.heatingSetpoint = ${state.heatingSetpoint}"
          
            // sendEvent
            log.info "updating airTemp:${airTemp}, poolTemp:${poolTemp}, spaTemp:${spaTemp}"
            sendEvent(name: "poolTemp", value: "${poolTemp}")
            sendEvent(name: "spaTemp", value: "${spaTemp}")
            sendEvent(name: "airTemp", value: "${airTemp}")
            sendEvent(name: "solarTemp", value: "${solarTemp}")
            sendEvent(name: "freeze", value: "Freeze: ${freeze}")
            sendEvent(name: "poolSetPoint", value: "Pool Set Point: ${poolSetPoint}")
            sendEvent(name: "poolHeatMode", value: "Pool Heat Mode: ${poolHeatModeStr}")
            sendEvent(name: "spaSetPoint", value: "Spa Set Point: ${spaSetPoint}")
            sendEvent(name: "spaHeatMode", value: "Spa Heat Mode: ${spaHeatModeStr}")
            sendEvent(name: "heaterActive", value: "Heater Active: ${heaterActive}")

            sendEvent(name: "heatingSetpoint", value: "${spaSetPoint}")
            sendEvent(name: "temperature", value: "${spaSetPoint}")
         
            def tempData = "Freeze: ${freeze}\n Pool Set Point: ${poolSetPoint}°\n Pool Heat Mode: ${poolHeatModeStr}\n Spa Set Point: ${spaSetPoint}°\n Spa Heat Mode: ${spaHeatModeStr}"
            log.info "tempData = ${tempData}"
            sendEvent(name: "tempData", value: "${tempData}")
			log.info "### finished temperatures"
            break
        case "circuits":
              log.info "### parse:circuits ###"
              // Circuits        
              def circuits = msg.data.get(it)      
              //log.info "count of circuits = " + circuits.size          
              //log.info "##### circuits = " + circuits
              //log.info "***** circuit = " + circuit             
              def spaStatus = circuits[circuit.spa].status
              def airBlowerStatus = circuits[circuit.blower].status
              def poolLightStatus = circuits[circuit.poolLight].status
              def spaLightStatus = circuits[circuit.spaLight].status
              def cleanerStatus = circuits[circuit.cleaner].status
              def poolStatus = circuits[circuit.pool].status
              def highSpeedStatus = circuits[circuit.highSpeed].status
              def spillwayStatus = circuits[circuit.spillway].status 
                                      
			  //log.debug "circuits: ${circuits}"
              //log.debug "spa: ${spaStatus}"
              //log.debug "blower: ${airBlowerStatus}"
              //log.debug "poolStatus: ${poolStatus}"
              //log.debug "highSpeedStatus: ${highSpeedStatus}"                   
           
              // sendEvent
              log.info "poolLightStatus: ${poolLightStatus}\n, spaLightStatus: ${spaLightStatus}\n, poolStatus: ${poolStatus}\n, spaStatus: ${spaStatus}"
              log.info "cleanerStatus: ${cleanerStatus}\n, spillwayStatus: ${spillwayStatus}\n, blowerStatus: ${airBlowerStatus}\n, highSpeedStatus: ${highSpeedStatus}"
                       
              sendEvent(name: "poolLight", value: OnOffconvert("${poolLightStatus}"))
              sendEvent(name: "spaLight", value: OnOffconvert("${spaLightStatus}"))
              sendEvent(name: "pool", value: OnOffconvert("${poolStatus}"))
              //sendEvent(name: "spa", value: OnOffconvert("${spaStatus}"))
              //sendEvent(name: "heatingSetpoint", value: OnOffconvert("${spaStatus}"))
              
              if (${spaStatus} == 1) {
            	 def heatvalue = "heating"
              } else {
            	 def heatvalue = "idle"
              }    
              log.info "heatvalue = ${heatvalue}"
              sendEvent(name: "thermostatOperatingState" , value: ${heatvalue})
              sendEvent(name: "cleaner", value: OnOffconvert("${cleanerStatus}"))
              sendEvent(name: "highspeed", value: OnOffconvert("${highSpeedStatus}"))
              sendEvent(name: "spillWay", value: OnOffconvert("${spillwayStatus}"))
              sendEvent(name: "blower", value: OnOffconvert("${airBlowerStatus}"))
              log.info "### finished circuits"
        	  break
        case "pumps":      
          log.info "### parse:pumps ###"
          //log.info "-------- pumps ---------"
          //log.debug "-#-#- ${it} -> " + msg.data.get(it)
          def pdata = msg.data.get(it)
          def myit = it
          //log.info "myit = " + myit     
          // ver 3 requires (1..2), were as ver 4 needs ('1'..'2')
          ('1'..'2').each {
            //log.info "it =" + it
            def pump = pdata[it]
            log.info "Pump " + it + " Data -- " + pump            
            //log.info "####### pump: ${pump}"
            //log.info "Pump name = " + pump.name         
            //sendEvent
            sendEvent(name: "${pump.name}", value: "----- ${pump.name} -----\nWatts :${pump.watts} \nRPM :${pump.rpm} \nError :${pump.err}\nState :${pump.drivestate}\nMode :${pump.run}")
            } 
            log.info "### finished pumps"
            break
         default:
              log.info "### parse:default"
              log.info "it = ${it}"
              log.info "### finished default"
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
    //def userpass = encodeCredentials(username, password)
    def userpass = ""
    def headers = getHeader(userpass)

	log.info "Sending request to host: " + headers
    log.info("Query: $query")
	//sendEvent(name: nfeatureNameame, value: "unknown")
  
    def dni = setDeviceNetworkId("${controllerIP}","${controllerPort}")
     
    def poolAction = new physicalgraph.device.HubAction(
		method: "GET",
		path: query,
        headers: headers,
        dni
        //query: "${query}"
	)
    log.debug "Action: " + poolAction
	return poolAction
}

/*
def updated() {
    log.info "######### UPDATED #########"
    unsubscribe()
    initialize()
}

def installed() {
	log.info "########## In installed ###########"
    initialize()
}

def initialize() {
    log.info "########### in initialize #########"
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
*/

def incremenentCount() {
	state.temp = state.temp + 1
    state.switchCounter = state.switchCounter + 1
    log.debug "state switchCounter = $state.switchCounter"
}

// handle Toggle commands 
// Set spa heat mode: /spaheat/mode/# (0=off, 1=heater, 2=solar pref, 3=solar only) 

def spaModeToggle() {
	log.info "--------- spaMode Toggle ----------"
	setFeature("/spaheat/mode/1")
    sendEvent(name: "spaMode", value: "heater")
}

def setSpaHeatPoint(num) {
    log.info "-------- setSpaHeatPoint --------"
    log.info "num = ${num}"
    setFeature("/spaheat/setpoint/${num}")
    //refresh()
}

def getTempInLocalScale(state) {
	def temp = device.currentState(heatingSetpoint)
	if (temp && temp.value && temp.unit) {
		return getTempInLocalScale(temp.value.toBigDecimal(), temp.unit)
	}
	return 0
}

def alterSetpoint(targetValue) {
    log.info "In alterSetpoint"
	def locationScale = getTemperatureScale()
	def deviceScale = (state.scale == 1) ? "F" : "C"
    //def heatingSetpoint = getTempInLocalScale("state.heatingSetpoint")
    
    log.info "current heatingSetpoint ${targetValue}"
   
	//sendEvent(name: "heatingSetpoint", "value": targetValue, unit: getTemperatureScale(), eventType: "ENTITY_UPDATE", displayed: true) 
    setSpaHeatPoint(targetValue)  
    //runIn(240,refresh())
}

// toggle circuit

def spaToggle() {
    // turns spa heater off
	log.debug "Executing 'spaToggle'"
	setFeature("/circuit/1/toggle/")
    //sendEvent(name: "device.thermostatMode", value:"Mode")
    //sendEvent(name: "device.thermostatOperatingState", value:"OperatingState")
}

def blowerToggle() {
	log.debug "Executing 'blowerToggle'"  
	setFeature("/circuit/2/toggle/")
}

def poolLightToggle() {
	log.debug "Executing 'poolLightToggle'"
	setFeature("/circuit/3/toggle/")
}

def spaLightToggle() {
	log.debug "Executing 'spaLightToogle'"
	setFeature("/circuit/4/toggle/")
}

def cleanerToggle() {
	log.debug "Executing 'cleanerToogle'"
	setFeature("/circuit/5/toggle/")
}

def poolToggle() {
	log.debug "Executing 'poolToogle'"
	setFeature("/circuit/6/toggle/")
}

def highspeedToggle() {
	log.debug "Executing 'highspeedToggle'"
	setFeature("/circuit/7/toggle/")
}

def spillWayToggle() {
	log.debug "Executing 'spillWayToogle'"
	setFeature("/circuit/8/toggle/")
}

private delayAction(long time) {
    new physicalgraph.device.HubAction("delay $time")
}

private setDeviceNetworkId(ip,port){
      def iphex = convertIPtoHex(ip)
      def porthex = convertPortToHex(port)
      device.deviceNetworkId = "$iphex:$porthex"
      log.debug "Device Network Id set to ${iphex}:${porthex}"
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

def tempUp() {
	def ts = device.currentState("temperature")
    def hs = device.currentState("heatingSetpoint")
    log.info "tempUp:ts = ${ts.value}"
    log.info "tempUp:hs = ${hs.value}"
    //def ts = device.currentState("heatingSetpoint")
	def value = ts ? ts.integerValue + 1 : 72
    log.info "value = ${value}"
	sendEvent(name:"temperature", value: "${value}")
    sendEvent(name:"heatingSetpoint", value: "${value}")
    log.info "calling evaluate"
	evaluate(value, device.currentValue("heatingSetpoint"), "heat")
    log.info "calling alterSetpoint"
    alterSetpoint(value)
}

def tempDown() {
	def ts = device.currentState("temperature")
    def hs = device.currentState("heatingSetpoint")
    log.info "tempUp:ts = ${ts.value}"
    log.info "tempUp:hs = ${hs.value}"
    //def ts = device.currentState("heatingSetpoint")
	def value = ts ? ts.integerValue - 1 : 72
    log.info "value = ${value}"
	sendEvent(name:"temperature", value: "${value}")
    sendEvent(name:"heatingSetpoint", value: "${value}")
    log.info "calling evaluate"
	evaluate(value, device.currentValue("heatingSetpoint"), "heat")
    log.info "calling alterSetpoint"
    alterSetpoint(value)
}

def setTemperature(value) {
	def ts = device.currentState("temperature")
	sendEvent(name:"temperature", value: value)
	evaluate(value, device.currentValue("heatingSetpoint"), "off")
}

/*
def heatUp() {
    log.info "HEATUP"
	def ts = device.currentState("heatingSetpoint")
	def value = ts ? ts.integerValue + 1 : 68
	sendEvent(name:"heatingSetpoint", value: "${value}°")
	evaluate(device.currentValue("temperature"), value, "off")
}

def heatDown() {
    log.info "TEMPDOWN"
	def ts = device.currentState("heatingSetpoint")
	def value = ts ? ts.integerValue - 1 : 68
	sendEvent(name:"heatingSetpoint", value: "${value}°")
	evaluate(device.currentValue("temperature"), value, "off")
}
*/

def evaluate(temp, heatingSetpoint, mode) {
	log.debug "evaluate($temp, $heatingSetpoint)"
	def threshold = 1.0
	def current = device.currentValue("thermostatOperatingState")
	//def mode = device.currentValue("thermostatMode")

	def heating = false
	def cooling = false
	def idle = false
	if (mode in ["heat","emergency heat","auto"]) {
		if (heatingSetpoint - temp >= threshold) {
			heating = true
			sendEvent(name: "thermostatOperatingState", value: "heating")
		}
		else if (temp - heatingSetpoint >= threshold) {
			idle = true
		}
		sendEvent(name: "thermostatSetpoint", value: heatingSetpoint)
	}
    /*
	if (mode in ["cool","auto"]) {
		if (temp - coolingSetpoint >= threshold) {
			cooling = true
			sendEvent(name: "thermostatOperatingState", value: "cooling")
		}
		else if (coolingSetpoint - temp >= threshold && !heating) {
			idle = true
		}
		sendEvent(name: "thermostatSetpoint", value: coolingSetpoint)
	}
    */
	else {
		sendEvent(name: "thermostatSetpoint", value: heatingSetpoint)
	}

	if (mode == "off") {
		idle = true
	}

	if (idle && !heating && !cooling) {
		sendEvent(name: "thermostatOperatingState", value: "idle")
	}
}