import requests

API_ENDPOINT = 'http://localhost:8080'

def send_log():
  data = {
    "logType" : "ERROR",
    "ipAddress" : "192.168.0.1",
    "operatingSystem" : "Debian",
    "software" : "Powerpoint",
    "username" : "SkinnyPete",
    "message" : "Failed login"
  }

  req = requests.post(url = API_ENDPOINT + '/logs', data = data)
  res = req.text
  print(res)

send_log()