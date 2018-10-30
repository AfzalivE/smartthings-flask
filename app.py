from lights.rf_things import FloorLamp
from flask import Flask, jsonify
import time


# create the flask server app
app = Flask(__name__)


# Configuration for devices, incoming via REST call as type/thingname/action
# The lookup is then mapped to GPIO output to carryout the task
things = {
  'switch':   [{'name': 'switch'}]
}

# Run the action via GPIO output
def thing_do(f,action):
  print('thing_do')
  print(action)
  if action is 'on':
    FloorLamp().turnOn()
  elif action is 'off':
    FloorLamp().turnOff()
  else:
    return 400

  # response code
  return 200


@app.route("/status")
def status():
  print("answering /status request")
  # response code
  return 'online', 200


@app.route("/<ttype>/<thing>/<action>")
def action(ttype,thing, action):
  # response code
  res = 400
  if ttype in things:
      for f in things[ttype]:
          if thing == f['name'] or thing == "all":
              # run an action based on lookup
              res = thing_do(f,action)

  return jsonify({'res':res}), res


# start the flask app
if __name__ == "__main__":
  app.run(host='0.0.0.0', debug=False)
