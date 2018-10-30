/**
 *  Copyright 2015 SmartThings
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
 */
metadata {
    //
	definition (name: "Rpi Rf Switch", namespace: "smartthings", author: "afzalive", ocfDeviceType: "oic.d.smartplug") {
		capability "Switch"
    capability "Actuator"
	}

    // when setting up your device via the smartthings app, these preference settings are available
  preferences {
        // the LAN IP address and port for the server for example: 192.168.0.100:8080
        input("host", "string", title:"Host", description: "The IP address and port of the Raspberry Pi.", required: true, displayDuringSetup: true)
	}

  // UI tile definitions
  tiles(scale: 2) {
        standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
          state "off", label:'Off', action:"switch.on", icon:"st.switches.switch.off", backgroundColor:"#ffffff", nextState:"turningOn"
          state "on", label:'On', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#00a0dc", nextState:"turningOff"
          state "turningOn", label:'Turning on', icon:"st.switches.switch.on", backgroundColor:"#00a0dc", nextState: "turningOff"
          state "turningOff", label:'Turning off', icon:"st.switches.switch.off", backgroundColor:"#ffffff", nextState: "turningOn"
        }

        main(["switch"])
    }
}

def parse(description) {
  log.debug "parsing"
  def msg = parseLanMessage(description)
  log.debug msg
}

//
def getHostAddress() {
	return "${host}"
}

def on() {
  def cmds = []
  cmds << http_command("/switch/switch/on")
  log.debug cmds
	sendEvent(name: "switch", value: "pushed", data: [buttonNumber: "2"], descriptionText: "$device.displayName on was pushed", isStateChange: true)
	return cmds
}


//
def off() {
  def cmds = []
  cmds << http_command("/switch/switch/off")
  log.debug cmds
	sendEvent(name: "switch", value: "pushed", data: [buttonNumber: "2"], descriptionText: "$device.displayName off was pushed", isStateChange: true)
	return cmds
}

//
private http_command(uri) {
	log.debug("Executing hubaction ${uri} on " + getHostAddress())

    def hubAction = new physicalgraph.device.HubAction(
    	method: "GET",
        path: uri,
        headers: [HOST:getHostAddress()])

    return hubAction
}
