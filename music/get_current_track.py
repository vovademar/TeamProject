from yandex_music import Client
import os

token = 'token'

client = Client(token).init()


def catch_track():
    try:
        queues = client.queues_list()
        last_queue = client.queue(queues[0].id)
        track_id = last_queue.get_current_track()
        track = track_id.fetch_track()
        return track
    except Exception as e:
        print("Cannot catch trackID. Restart programm.\n\n")
        return 0


def catch_label():
    try:
        track = catch_track()
        artists = ', '.join(track.artists_name())
        title = track.title
        return f"{artists} - {title}"
    except Exception as e:
        return 'No track'


print(catch_label())