import random
import pprint
import webbrowser

from yandex_music import Client
from auth.get_token import GetToken
import music.track_by_mood as tbm

from deepface import DeepFace as df


def open_several_tracks(track_ids):
    url = 'https://music.yandex.ru/track/'
    urls = [url + str(t) for t in track_ids if str(t).isdigit()]

    for u in urls:
        webbrowser.open(u, 2)


def open_track(track_id):
    if str(track_id).isdigit():
        url = 'https://music.yandex.ru/track/' + str(track_id)
        webbrowser.open(url, 2)


def full_info():
    client = Client().init()

    genres = client.genres()
    station_results = client.rotor_stations_list()

    for sr in station_results:
        s = sr.station
        print(s.name)
        # print('mood:', s.restrictions.mood)
        # print('energy:', s.restrictions.energy)
        # print('mood_energy:', s.restrictions.mood_energy)

    for g in genres:
        print(g['title'])
        for s in g['sub_genres']:
            print('  ', s['title'])

        print()


def get_genre_by_image(img_path) -> (str, str):
    """
    :param img_path: path to image
    :return: genre type, dominant emotion
    """
    emotion_mapper = {'angry': ['metal', 'folkmetal', 'hardrock'],
                      'disgust': ['meditation'],
                      'fear': ['naturesounds', 'classicalmusic'],
                      'happy': ['pop', 'dance', 'modern'],
                      'sad': ['classicalmusic'],
                      'surprised': ['electronics'],
                      'neutral': ['relax', 'eastern']}

    analyze = df.analyze(img_path, actions=('emotion',))

    genres = emotion_mapper[analyze['dominant_emotion']]

    return genres[random.randrange(0, len(genres))], analyze['dominant_emotion']


def example_with_image():
    token = GetToken().token
    client = Client(token).init()

    print('enter image path:')
    img_path = input()

    genre_type, dominant_emotion = get_genre_by_image(img_path)
    print(f'genre type: {genre_type}, dominant emotion: {dominant_emotion}')

    _station_id, _station_from = tbm.choose_mood_by_emotion('genre', genre_type)

    tracks = tbm.get_first_n_tracks(client, _station_id, _station_from, 3)

    pp.pprint(tracks)
    open_several_tracks([t.id for t in tracks])


def example():
    token = GetToken().token
    client = Client(token).init()

    track_by_mood = tbm.TrackByMood(client)
    tracks = track_by_mood.radi_n(3)

    pp.pprint(tracks)
    open_several_tracks([t.id for t in tracks])


pp = pprint.PrettyPrinter(indent=2)

example()
