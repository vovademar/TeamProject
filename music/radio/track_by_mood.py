
from time import sleep

from yandex_music import Client

from radio import Radio

# API instance
client = Client(token='token')

#sad music
_stations = client.rotor_stations_list()
_station = None
for i in _stations:
    _station = i.station
    print(_station.id_for_from)
    if _station.id_for_from == 'mood-sad':
        _station = i.station
        break

_station_id = f'{_station.id.type}:{_station.id.tag}'
_station_from = _station.id_for_from

# Radio instance
radio = Radio(client)

# print id and for from id
print(_station_id)
print(_station_from)

# start radio and get first track
first_track = radio.start_radio(_station_id, _station_from)
print('[Radio] First track is:', first_track)

# get new track every 5 sec
while True:
    sleep(5)
    next_track = radio.play_next()
    print('[Radio] Next track is:', next_track)
