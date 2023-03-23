from itertools import groupby
from time import sleep

from yandex_music import Track

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


def play_chosen_radio(client, st_id, st_from):
    radio = Radio(client)
    first_track = radio.start_radio(st_id, st_from)
    print('[Radio] First track is:', first_track)

    download_track(first_track)
    return first_track, radio


def play_next_radio_track(radio: Radio):
    next_track: Track = radio.play_next()
    print('[Radio] Next track is:', next_track)
    download_track(next_track)
    return next_track


def download_track(track_from_radio):
    track_from_radio: Track
    name = track_from_radio.title
    filename = f'./{name}.mp3'
    track_from_radio.download(filename, codec="mp3")
    return filename
