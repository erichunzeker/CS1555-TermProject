from bs4 import BeautifulSoup
import requests

def parseStation(url):
	r = requests.get(url)
	data = r.text
	soup = BeautifulSoup(data, features="html.parser")
	content = soup.findAll("div", {"id" : "septa_main_content"})
	stations = []
	for i in content:
		address = i.find("p")
		return str(address).replace("\t", "").replace("\n", "").replace("<p>", "").replace("<br>", " ").replace("&amp;", "&").replace("</p>", "").split("<br/>")[0].split("</br>")[0]
		
def getStations(tables, toDowntown):
	stations = []
	j = 0
	for row in tables[0].findAll("tr"):
		cells = row.findAll("th", {"scope" : "row"})
		for i in cells:
			for link in i.findAll("a"):
				stationURL = 'http://www.septa.org/' + link.get('href')[9:]
				address = parseStation(stationURL)
				if address == '<font color="red"><strong>NOTICE | </strong>This Station now includes motorcycle parking</font>' or address in [d['address'] for d in stations]:
					break
				if not toDowntown and address == "16th St. & JFK Blvd. Philadelphia PA 19102":
					stations.append({'address': address, 'distance': 2 * j, 'open': '9:00', 'close': '5:00'})
					toDowntown = True
					j += 1
				elif address == "16th St. & JFK Blvd. Philadelphia PA 19102":
					stations.append({'address': address, 'distance': 2 * j, 'open': '09:00:00', 'close': '05:00:00'})
					return stations
				elif toDowntown:
					stations.append({'address': address, 'distance': 2 * j, 'open': '09:00:00', 'close': '05:00:00'})
					j += 1
	return stations

#url = raw_input("Enter a SEPTA Schedule URL (http://www.septa.org/schedules/rail/w/WAR_1.html): ")
url = ["http://www.septa.org/schedules/rail/w/AIR_1.html", "http://www.septa.org/schedules/rail/w/CHE_1.html", "http://www.septa.org/schedules/rail/w/CHW_1.html", "http://www.septa.org/schedules/rail/w/CYN_1.html", "http://www.septa.org/schedules/rail/w/FOX_1.html", "http://www.septa.org/schedules/rail/w/GLN_1.html", "http://www.septa.org/schedules/rail/w/LAN_1.html", "http://www.septa.org/schedules/rail/w/NOR_1.html", "http://www.septa.org/schedules/rail/w/MED_1.html", "http://www.septa.org/schedules/rail/w/PAO_1.html", "http://www.septa.org/schedules/rail/w/TRE_1.html", "http://www.septa.org/schedules/rail/w/WAR_1.html", "http://www.septa.org/schedules/rail/w/WTR_1.html", "http://www.septa.org/schedules/rail/w/WIL_1.html"]

allstations = []
allRails = []
k = 1
for i in url:
	r  = requests.get(i)
	data = r.text
	soup = BeautifulSoup(data, features="html.parser")
	tables = soup.find_all("table", { "id" : "timeTable" })
	toDowntown = False
	if "_1.html" in i:
		toDowntown = True
	stations = getStations(tables, toDowntown)
	allRails.append(stations)
	print("INSERT INTO railline VALUES (" + str(k) + ", 40);")
	for j in stations:
		if j['address'] not in allstations:
			allstations.append(j['address'])
			print("INSERT INTO station (address, opentime, closetime, distance) VALUES ('" + j['address'] + "', time '09:00:00', time '19:00:00', " + str(j['distance']) + ");")
		print("INSERT INTO station_railline VALUES ((SELECT station_id FROM station WHERE address = '" + j['address'] + "'), " + str(k) + ");")
	k += 1

# PAIRS OF RAILS BY INDEX: 0,3 4,5 1,2 7,10 8,11 12,13 6,9
k = 0
for i in allRails:
	print("INSERT INTO route VALUES (" + str(k*2 + 1)+ ", 'Every Stop Rail #" + str(k) + " to CC');")
	print("INSERT INTO railline_route VALUES (" + str(k + 1) + ", " + str(k*2 + 1) + ");")
	print("INSERT INTO route VALUES (" + str(k*2 + 2)+ ", 'Every Stop Rail #" + str(k) + " from CC');")
	print("INSERT INTO railline_route VALUES (" + str(k + 1) + ", " + str(k*2 + 2) + ");")
	for j in range(1, len(i)):
		# GENERATING 
		print("INSERT INTO stop (Station_A_ID, Station_B_ID, distancebetween) VALUES ((SELECT station_id FROM station WHERE address = '" + i[j - 1]['address'] + "'), (SELECT station_id FROM station WHERE address = '" + i[j]['address'] + "'), 2);")
		print("INSERT INTO route_stop VALUES ((SELECT stop_id FROM stop WHERE Station_A_ID = (SELECT station_id FROM station WHERE address = '" + i[j - 1]['address'] + "') AND Station_B_ID = (SELECT station_id FROM station WHERE address = '" + i[j]['address'] + "')), " + str(k*2 + 1) + ");")
		print("INSERT INTO stop (Station_A_ID, Station_B_ID, distancebetween) VALUES ((SELECT station_id FROM station WHERE address = '" + i[j]['address'] + "'), (SELECT station_id FROM station WHERE address = '" + i[j - 1]['address'] + "'), 2);")
		print("INSERT INTO route_stop VALUES ((SELECT stop_id FROM stop WHERE Station_A_ID = (SELECT station_id FROM station WHERE address = '" + i[j]['address'] + "') AND Station_B_ID = (SELECT station_id FROM station WHERE address = '" + i[j - 1]['address'] + "')), " + str(k*2 + 2) + ");")
	k += 1