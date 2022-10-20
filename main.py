from yandex_music import Client
import os


token = 'Your token'

client = Client(token).init()

# namesound = client.users_likes_tracks()[0].fetch_track().title
#
# client.users_likes_tracks()[0].fetch_track().download(f'./{namesound}')

# Sample - Getting genres
def print_genres():
    genres = client.genres()
    for genre in genres:
        print(genre['title'])
        for sub_genre in genre['sub_genres']:
            print('sub:', sub_genre['title'])
        print()


def print_tracks():
    chart = client.chart('world').chart

    for track_short in chart.tracks:
        track, chart = track_short.track, track_short.chart
        print(track['title'], '-', end=' ')
        for artist in track['artists']:
            print(artist['name'], end=' ')
        print()


# using rhythmbox-client
def play_first_chart_track():
    chart = client.chart('world').chart

    if len(chart.tracks) > 0:
        first_track = chart.tracks[1].track
        name = first_track.title
        artists = first_track.get_supplement

        print(artists)

        filename = f'./{name}'

        if not os.path.exists(filename):
            first_track.download(filename, codec='mp3')
            exit_status = os.system(f'rhythmbox-client --play-uri {filename}')
            print(f'"rhythmbox-client --play-uri" command exit status: {exit_status}')


# using rhythmbox-client
def play_by_track_id():
    # September Earth, Wind & Fire
    track_id = 645494
    tracks = client.tracks([track_id])

    if len(tracks) > 0:
        track = tracks[0]

        filename = './september_track_YMusic.mp3'

        if not os.path.exists(filename):
            track.download(filename, codec='mp3')
            exit_status = os.system(f'rhythmbox-client --play-uri {filename}')
            print(f'"rhythmbox-client --play-uri" command exit status: {exit_status}')


#
# print_genres()
# print_tracks()

#play_by_track_id()

play_first_chart_track()

#play_first_chart_track()
# only if you have rhythmbox-client player
# play_first_chart_track()
# play_by_track_id()
