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
 * Fork from definition (name: "Pentair Controller", namespace: "michaelusner", author: "Michael Usner", oauth: true)
 */
metadata {
	definition (name: "Pentair Pool Controller", namespace: "dhopson", author: "David Hopson", oauth: true) {
    	capability "Polling"
        capability "Switch"
        capability "Switch Level"
        capability "Refresh"
        capability "Thermostat Setpoint"
        capability "Temperature Measurement"
        capability "Thermostat"

        //command "allOn"
        //command "allOff"
        //command "poolOff"
		//command "poolOn"
        command "poolToggle"
		//command "spaOff"
		//command "spaOn"
        command "spaToggle"
		//command "poolLightOff"
		//command "poolLightOn"
        command "poolLigtToggle"
		//command "spaLightOff"
		//command "spaLightOn"
        command "spaLightToggle"
		//command "blowerOff"
		//command "blowerOn"
		command "blowerToggle"
        //command "cleanerOff"
		//command "cleanerOn"
		command "cleanerToggle"
        //command "spillWayOff"
		//command "spillWayOn"
        command "spillWayToggle"
		//command "aux7Off"
		//command "aux7On"
        //command "tempUp"
		//command "tempDown"
        //command "heaton"
        //command "heatoff"
        command "heatToggle"
	}

	preferences {
       	section("Select your controller") {
       		input "controllerIP", "text", title: "Controller hostname/IP", required: true
       		input "controllerPort", "port", title: "Controller port", required: true
            input "username", "string", title:"Username", description: "username", required: true, displayDuringSetup: true
            input "password", "password", title:"Password", description: "Password", required: true, displayDuringSetup: true
 		}
    }
    
	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {
        valueTile("timedate", "device.timedate", width: 6, height: 3) {
			state "val", label:'${currentValue}', defaultState: true
		}
        // Pumps
        valueTile("Pump 1", "device.Pump 1", width: 3, height: 3) {
			state "val", label:'${currentValue}', defaultState: true
		}     
        valueTile("Pump 2", "device.Pump 2", width: 3, height: 3) {
			state "val", label:'${currentValue}', defaultState: true
		}
        // Lights
    	standardTile("poolLight", "device.poolLight", width: 3, height: 3, canChangeBackground: true) {
			state "unknown", label: 'poolLight', action: "poolLightUnknown", icon: "st.Lighting.light11", backgroundColor: "#F2F200"
            state "off", label: 'poolLight ${currentValue}', action: "poolLigtToggle", icon: "st.Lighting.light11", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'poolLight ${currentValue}', action: "poolLigtToggle", icon: "st.Lighting.light11", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("spaLight", "device.spaLight", width: 3, height: 3, canChangeBackground: true) {
        	state "unknown", label: 'spaLight', action: "spaLightUnknown", icon: "st.Lighting.light11", backgroundColor: "#F2F200"
			state "off", label: 'spaLight ${currentValue}', action: "spaLightToggle", icon: "st.Lighting.light11", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'spaLight ${currentValue}', action: "spaLightToggle", icon: "st.Lighting.light11", backgroundColor: "#79b821", nextState: "off"
		}
        // Pool/Spa
        standardTile("pool", "device.pool", width: 3, height: 3, canChangeBackground: true) {
        	state "unknown", label: 'Pool', action: "poolUnknown", icon: "st.Health & Wellness.health2", backgroundColor: "#F2F200"
			state "off", label: 'Pool ${currentValue}', action: "poolToggle", icon: "st.Health & Wellness.health2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Pool ${currentValue}', action: "poolToggle", icon: "st.Health & Wellness.health2", backgroundColor: "#79b821", nextState: "off"
		}
        // spa turns on heater
        standardTile("spa", "device.spa", width: 3, height: 3, canChangeBackground: true) {
            //label: 'Spa ${currentValue}'
        	state "unknown", label: 'Spa', action: "spaUnknown", icon: "st.thermostat.heat", backgroundColor: "#F2F200"
			state "off", label: '', action: "spaToggle", icon: "st.thermostat.heat", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: '', action: "spaToggle", icon: "st.thermostat.heating", backgroundColor: "#79b821", nextState: "off"
		}
        // Features
        standardTile("cleaner", "device.cleaner", width: 3, height: 3, canChangeBackground: true) {
        	state "unknown", label: 'Cleaner', action: "cleanerUnknown", icon: "st.Appliances.appliances2", backgroundColor: "#F2F200"
			state "off", label: 'Cleaner ${currentValue}', action: "cleanerToggle", icon: "st.Appliances.appliances2", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Cleaner ${currentValue}', action: "cleanerToggle", icon: "st.Appliances.appliances2", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("spillWay", "device.spillWay", width: 3, height: 3, canChangeBackground: true) {
        	state "unknown", label: 'SpillWay', action: "spillWayUnknown", icon: "st.Bath.bath13", backgroundColor: "#F2F200"
			state "off", label: 'SpillWay ${currentValue}', action: "spillWayToggle", icon: "st.Bath.bath13", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'SpillWay ${currentValue}', action: "spillWayToggle", icon: "st.Bath.bath13", backgroundColor: "#79b821", nextState: "off"
		}
        standardTile("blower", "device.blower", width: 3, height: 3, canChangeBackground: true) {
        	state "unknown", label: 'blower', action: "blowerUnknown", icon: "st.Bath.bath13", backgroundColor: "#F2F200"
			state "off", label: 'blower ${name}', action: "blowerToggle", icon: "st.Bath.bath13", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'blower ${name}', action: "blowerToggle", icon: "st.Bath.bath13", backgroundColor: "#79b821", nextState: "off"
		}
        // Temp
        valueTile("waterTemp", "device.waterTemp", width: 3, height: 3, canChangeBackground: true) {
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
        valueTile("airTemp", "device.airTemp", width: 3, height: 3) {
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
        valueTile("spaTemp", "device.spaTemp", width: 3, height: 3, canChangeBackground: true) {
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
        
        // spaMode not being used
        standardTile("spaMode", "device.spaMode", width: 3, height: 3) {
            //state "temperature", label:'${currentValue}°', icon:"st.thermostat.heat" 
        	state "off", label:'', icon:"st.thermostat.heat", action: "heatToggle", backgroundColor: "#ffffff", nextState: "on",decoration: "flat"
            state "on", label:'', icon:"st.thermostat.heating", action: "heatToggle", backgroundColor: "#44b621",nextState: "off",decoration: "flat"
    	} 
        
        // Misc
        standardTile("refresh", "command.refresh2", width: 3, height: 3, inactiveLabel: false) {
        	state "default", label:'refresh', action:"refresh.refresh", icon:"st.secondary.refresh-icon"
    	}
        /*
        standardTile("refresh", "command.refresh", width: 1, height: 1, inactiveLabel: false) {
        	state "default", label:'refresh', action:"refresh.refresh", icon:"st.secondary.refresh-icon"
    	}
        */
        
        standardTile("blank", "command.blank", width: 1, height: 1, inactiveLabel: false) {
        	state "default"
    	}
        // Up/Down not being used
        /*
        standardTile("tempDown", "device.tempDown", width: 2, height: 1, inactiveLabel: false, decoration: "flat") {
			state "tempDown", label:'${currentValue°}down', action:"tempDown", defaultState: true
		}
		standardTile("tempUp", "device.tempUp", width: 2, height: 1, inactiveLabel: false, decoration: "flat") {
			state "tempUp", label:'${currentValue°}up', action:"tempUp", defaultState: true
		}
        */
       
        main(["waterTemp"])
        /*
		details(["timedate","refresh", "blank",
        		"airTemp", "waterTemp",
                "poolLight", "spaLight", 
                "spa","spaTemp","refresh2",
                "pool", "cleaner", 
                "spillWay", "blower",
                "Pump 1","Pump 2",
                "refresh"])
         */ 
         		details(["timedate",
        		"airTemp", "waterTemp",
                "poolLight", "spaLight", 
                "spa","spaTemp",
                "pool", "cleaner", 
                "spillWay", "blower",
                "Pump 1","Pump 2",
                "refresh"])
	}
}


def refresh() {
    log.info "Requested a refresh"
    poll()
}

/*
def on() {
	log.info "--------- switch on ----------"
    sendEvent(name: "switch", value: "on")
}

def off() {
	log.info "--------- switch off ----------"
    sendEvent(name: "switch", value: "off")
}
*/

// heaton/off not being used
def heatToggle() {
	log.info "--------- heat Toggle ----------"
	setFeature("/spaheat/mode/1")
    sendEvent(name: "spaMode", value: "on")
}

/*
def heatoff() {
	log.info "--------- heat off ----------"
    setFeature("/spaheat/mode/0")
    sendEvent(name: "spaMode", value: "off")
}
*/
// Temp up/down not being used
/*
def tempUp() {
	def ts = device.currentState("spaTemp")
	def value = ts ? ts.integerValue + 1 : 72
	sendEvent(name: "spaTemp", value: value)
	evaluate(value, device.currentValue("heatingSetpoint"), device.currentValue("coolingSetpoint"))
}

def tempDown() {
	def ts = device.currentState("spaTemp")
	def value = ts ? ts.integerValue - 1 : 72
	sendEvent(name: "spaTemp", value: value)
	evaluate(value, device.currentValue("heatingSetpoint"), device.currentValue("coolingSetpoint"))
}
*/

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
    
// orginal parse    
def parse(String description) {
	log.debug "Parse"
    //def circuitMap = [:]
    def msg = parseLanMessage(description)
    def circuit = [
        spa:1,
        blower:2,
        poolLight:3,
        spaLight:4,
        cleaner:5,
        pool:6,
        highSpeed:7,
        spillway:8
        ]
    
     //def msgBody = msg.body
     def json = msg.json
     
     if (json.text) { //process response from toggle command
              log.debug "Text: ${json.text}"
              if (json.text.contains("POOL LIGHT")) {       
                  sendEvent(name: "poolLight", value: "${json.status}")
              } else if (json.text.contains("SPA LIGHT")) {
                  sendEvent(name: "spaLight", value: "${json.status}")
              } else if (json.text.contains("CLEANER")) {
                  sendEvent(name: "cleaner", value: "${json.status}")
              } else if (json.text.contains("SPILLWAY")) {
                  sendEvent(name: "spillWay", value: "${json.status}")
              } else if (json.text.contains("toggle SPA to")) {
                  sendEvent(name: "spa", value: "${json.status}")
              } else if (json.text.contains("POOL")) {
                  sendEvent(name: "pool", value: "${json.status}")
              } else if (json.text.contains("BLOWER")) {
                  sendEvent(name: "blower", value: "${json.status}")
              }
      } else {  //process response from poll      
    
  	  msg.data.keySet().each {
    	//log.debug "#-#-# ${it} -> " + msg.data.get(it)
        //sendEvent(name: it, value: msg.data.get(it))
        
        if (it == "time") {
            def time = msg.data.get(it)
            def controllerTime = time.controllerTime
            def controllerDate = time.controllerDateStr
            // sendEvent
            log.info "updating timedate Time: ${controllerTime} Date: ${controllerDate}"
            sendEvent(name: "timedate", value: "Time: ${controllerTime}\n Date: ${controllerDate}")
        } else if (it == "heat") {
            def heat = msg.data.get(it)
            def PoolSetPoint = heat.poolSetPoint
            def PoolHeatMode = heat.poolHeatModeStr
            def SpaSetPoint = heat.spaSetPoint
            def SpaHeatMode = heat.spaHeatModeStr
            def heaterActive = heat.heaterActive      
        } else if (it == "temperatures") {
            def temperatures = msg.data.get(it)
            def poolTemp = temperatures.poolTemp
            def sTemp = temperatures.spaTemp
            def airTemp = temperatures.airTemp
            def poolSetPoint = temperatures.poolSetPoint
            def poolHeatMode = temperatures.poolHeatModeStr
            def spaSetPoint = temperatures.spaSetPoint
            def spaHeatMode = temperatures.spaHeatModeStr
            def tempheaterActive = temperatures.heaterActive
            // sendEvent
            log.info "updating airTemp:${airTemp}, waterTemp:${poolTemp}, spaTemp:${sTemp}"
            sendEvent(name: "airTemp", value: "${airTemp}")
            sendEvent(name: "waterTemp", value: "${poolTemp}")
            sendEvent(name: "spaTemp", value: "${sTemp}")
            sendEvent(name: "tempUp", value: "${sTemp}")
            sendEvent(name: "tempDown", value: "${sTemp}")
            sendEvent(name: "spaMode", value: "{poolHeatMode}")
        
        } else if (it == "circuits") {
              // Circuits        
              def circuits = msg.data.get(it)      
              //log.info "count of circuits = " + circuits.size
              
              // add values to circuitMap
              log.info "##### circuits = " + circuits
              /*
              circuits.each {
              	    //log.debug "it = ${it}"
              		if (it != "blank") {
              			//log.debug "#### ${it} -> " + it.name + " , " + it.number
                        //circuitMap.put((it.name).toLowerCase(), it.number)
                    }    
              }
              */
             
              //log.info "##### circuitMap = " + circuitMap
             
              //state.cMap = circuitMap
              def spaStatus = circuits[circuit.spa].status
              def airBlowerStatus = circuits[circuit.blower].status
              def poolLightStatus = circuits[circuit.poolLight].status
              def spaLightStatus = circuits[circuit.spaLight].status
              def cleanerStatus = circuits[circuit.cleaner].status
              // poolStatus is the pool circulation pump
              def poolStatus = circuits[circuit.pool].status
              def highSpeedStatus = circuits[circuit.highSpeed].status
              def spillwayStatus = circuits[circuit.spillway].status 
              
                        
			  log.debug "circuits: ${circuits}"
              log.debug "spa: ${spaStatus}"
              log.debug "blower: ${airBlowerStatus}"
              //log.debug "poolLightStatus: ${poolLightStatus}"
              //log.debug "spaLightStatus: ${spaLightStatus}"
              //log.debug "cleanerStatus: ${cleanerStatus}"
              log.debug "poolStatus: ${poolStatus}"
              log.debug "highSpeedStatus: ${highSpeedStatus}"
              //log.debug "spillwayStatus: ${spillwayStatus}"                      
           
              // sendEvent
              log.info "poolLightStatus: ${poolLightStatus}, spaLightStatus: ${spaLightStatus}, poolStatus: ${poolStatus}, spaStatus: ${spaStatus}"
              log.info "cleanerStatus: ${cleanerStatus}, spillwayStatus: ${spillwayStatus}, blowerStatus: ${airBlowerStatus}"
                       
              sendEvent(name: "poolLight", value: OnOffconvert("${poolLightStatus}"))
              sendEvent(name: "spaLight", value: OnOffconvert("${spaLightStatus}"))
              sendEvent(name: "pool", value: OnOffconvert("${poolStatus}"))
              sendEvent(name: "spa", value: OnOffconvert("${spaStatus}"))
              sendEvent(name: "cleaner", value: OnOffconvert("${cleanerStatus}"))
              sendEvent(name: "spillWay", value: OnOffconvert("${spillwayStatus}"))
              sendEvent(name: "blower", value: OnOffconvert("${airBlowerStatus}"))
        
        } else if (it == "pumps") {
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
            sendEvent(name: "${pump.name}", value: "${pump.name}\nWatts :${pump.watts} \nRPM :${pump.rpm} \nError :${pump.err}\nState :${pump.drivestate}\nMode :${pump.run}")
          }
        }
     }   
    }
}  

def OnOffconvert(value) {
    if (value == "0")
       return ("off")
    if (value == "1")   
       return ("on")
}

def setFeature(query) {
    def userpass = encodeCredentials(username, password)
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

// handle commands 
 
def spaToggle() {
    // turns spa heater off
	log.debug "Executing 'spaToggle'"
	setFeature("/circuit/1/toggle/")
}

/*
def spaOn() {
    // turns spa heater on
	log.debug "Executing 'spaOn'"
	setFeature("/circuit/1/toggle/")  
} 
*/
def blowerToggle() {
	log.debug "Executing 'blowerToggle'"  
	setFeature("/circuit/2/toggle/")
}
/*
def blowerOn() {
	log.debug "Executing 'blowerOn'"   
	setFeature("/circuit/2/toggle/")
}
*/

def poolLigtToggle() {
	log.debug "Executing 'poolLightToggle'"
	setFeature("/circuit/3/toggle/")
}

/*
def poolLightOff() {
	log.debug "Executing 'poolLightOff'"
	setFeature("/circuit/3/toggle/")
}

def poolLightOn() {
	log.debug "Executing 'poolLightOn'"
    setFeature("/circuit/3/toggle/")
}    
*/   
def spaLightToggle() {
	log.debug "Executing 'spaLightOff'"
	setFeature("/circuit/4/toggle/")
}

/*
def spaLightOn() {
	log.debug "Executing 'spaLightOn'"
	setFeature("/circuit/4/toggle/")
}
*/

def cleanerToggle() {
	log.debug "Executing 'cleanerOff'"
	setFeature("/circuit/5/toggle/")
}

/*
def cleanerOn() {
	log.debug "Executing 'cleanerOn'"
	setFeature("/circuit/5/toggle/")
}   
*/

def poolToggle() {
	log.debug "Executing 'poolOff'"
	setFeature("/circuit/6/toggle/")
}

/*
def poolOn() {
	log.debug "Executing 'poolOn'"
	setFeature("/circuit/6/toggle/")
}
*/

def spillWayToggle() {
	log.debug "Executing 'spillWayOff'"
	setFeature("/circuit/8/toggle/")
}

/*
def spillWayOn() {
	log.debug "Executing 'spillWayOn'"
	setFeature("/circuit/8/toggle/")
}
*/

def setSpaTemperature(number) {
}

def setPoolTemperature(number) {
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