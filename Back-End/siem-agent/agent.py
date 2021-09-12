import requests
import json
import random
import time

API_ENDPOINT = 'http://localhost:8080'
HEADERS = {'Content-Type' : 'application/json'}


def randomLogType():
  logTypes = ['ERROR', 'INFORMATION', 'WARNING']
  index = random.randint(0, len(logTypes)-1)
  return logTypes[index]

def randomIpAddress():
  ipAddresses = ['192.168.0.1', '192.168.1.1', '192.168.0.2', '192.168.2.0', '192.168.2.1', '192.168.2.2', '192.168.175.0', '192.168.175.1']
  index = random.randint(0, len(ipAddresses)-1)
  return ipAddresses[index]

def randomOs():
  operatingSystems = ['Windows 10', 'Ubuntu 20.01', 'Debian', 'Fedora', 'Windows 7', 'Catalina', 'Mojave']
  index = random.randint(0, len(operatingSystems)-1)
  return operatingSystems[index]

def randomSoftware():
  software = ['Adobe XD', 'MySQL Workbench', 'MS Excel', 'MS Word', 'Student2Students']
  index = random.randint(0, len(software)-1)
  return software[index]

def randomUsername():
  usernames = ['SkinnyPete', 'Misic98', 'Zoran55', 'admin']
  index = random.randint(0, len(usernames)-1)
  return usernames[index]

def randomMessage():
  messages = ['Log for login subsystem', 'Log for payment subsystem', 'Generic log']
  index = random.randint(0, len(messages)-1)
  return messages[index]

def sendLog():
  logType = randomLogType()
  ipAddress = randomIpAddress()
  os = randomOs()
  software = randomSoftware()
  username = randomUsername()
  message = randomMessage()

  payload = {
    "logType" : logType,
    "ipAddress" : ipAddress,
    "operatingSystem" : os,
    "software" : software,
    "username" : username,
    "message" : message
  }

  data = json.dumps(payload)

  req = requests.post(url = API_ENDPOINT + '/logs', data = data, headers = HEADERS)
  res = req.text
  print(req)

def sendRandomLogs():
    while 1:
      send_log()
      time.sleep(3)

def paymentDDoS():
  payload = {
  "logType" : "WARNING",
  "ipAddress" : "192.168.5.5",
  "operatingSystem" : "Ubuntu 20.01",
  "software" : "Paypal",
  "username" : "Zoran55",
  "message" : "Log regarding payment subsystem"
  }

  for _ in range(51):
    data = json.dumps(payload)
    req = requests.post(url = API_ENDPOINT + '/logs', data = data, headers = HEADERS)
    res = req.text
    print(req)
    time.sleep(0.5)

def loginDDoS():
  payload = {
  "logType" : "WARNING",
  "ipAddress" : "192.168.5.5",
  "operatingSystem" : "Ubuntu 20.01",
  "software" : "Paypal",
  "username" : "Zoran55",
  "message" : "Log regarding login subsystem"
  }

  for _ in range(51):
    data = json.dumps(payload)
    req = requests.post(url = API_ENDPOINT + '/logs', data = data, headers = HEADERS)
    res = req.text
    print(req)
    time.sleep(0.1)

def main():
  # sendRandomLogs()
  # paymentDDoS()
  loginDDoS()

if __name__ == '__main__':
  main()