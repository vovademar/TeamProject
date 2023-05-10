from itertools import groupby

from yandex_music import Track

from music.radio.radio import Radio


class TrackByMood(object):
    def __init__(self, client):
        self.client = client

    def get_chosen_tracks(self):
        return choose_what_to_play(self.client)

    def radi(self):
        return play_chosen_radio(self.client, self.chose[0], self.chose[1])

    def play_sad(self):
        return play_mood_radio(self.client, "sad")

    def play_angry(self):
        return play_mood_radio(self.client, "aggressive")

    def play_disgust(self):
        return play_mood_radio(self.client, "haunting")

    def play_fear(self):
        return play_mood_radio(self.client, "dark")

    def play_happy(self):
        return play_mood_radio(self.client, "happy")

    def play_neutral(self):
        return play_mood_radio(self.client, "dream")

    def play_surprise(self):
        return play_mood_radio(self.client, "epic")


# angry, disgust, fear, happy, neutral, sad, surprise
# aggressive, haunting, dark, happy, dream, sad, epic

def play_mood_radio(client, mood):
    radio = Radio(client)
    mood_track = radio.start_radio(f"mood:{mood}", f"mood-{mood}")

    return mood_track, radio


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
    print(_station_id, " - id\n", _station_from, " - form")
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
    filename = f'{name}.mp3'
    track_from_radio.download(filename, codec="mp3")
    return filename
