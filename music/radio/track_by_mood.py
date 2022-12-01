from itertools import groupby
from time import sleep
from music.radio.radio import Radio


class TrackByMood(object):
    def __init__(self, client):
        self.client = client
        self.chose = choose_what_to_play(client)

    def get_chosen_tracks(self):
        return self.chose

    def radi(self):
        return play_chosen_radio(self.client, self.chose[0], self.chose[1])


def choose_what_to_play(client):
    type_list = []
    tag_list = []
    print("Choose type:")
    _stations = client.rotor_stations_list()
    for i in _stations:
        _station = i.station
        type_list.append(_station.id.type)
    new_type_list = [el for el, _ in groupby(type_list)]
    print(new_type_list)
    s_type = input()

    while s_type not in new_type_list:
        print("incorrect input, try again:")
        s_type = input()

    print("Choose tag:")
    for i in _stations:
        _station = i.station
        if _station.id.type == s_type:
            tag_list.append(_station.id.tag)
    print(tag_list)
    s_tag = input()

    while s_tag not in tag_list:
        print("incorrect input, try again:")
        s_tag = input()

    _station_id = f'{s_type}:{s_tag}'
    _station_from = s_type + "-" + s_tag
    return _station_id, _station_from
    

def choose_mood_by_emotion(s_tag):
    s_type = 'mood'
    _station_id = f'{s_type}:{s_tag}'
    _station_from = s_type + "-" + s_tag
    return _station_id, _station_from


def play_chosen_radio(client, st_id, st_from):
    radio = Radio(client)
    first_track = radio.start_radio(st_id, st_from)
    print('[Radio] First track is:', first_track)

    # get new track every 5 sec
    while True:
        sleep(5)
        next_track = radio.play_next()
        print('[Radio] Next track is:', next_track)
