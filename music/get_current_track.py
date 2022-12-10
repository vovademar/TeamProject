class CurrentTrack(object):
    def __init__(self, client):
        self.client = client
        self.label = catch_label(client)

    def get_label(self):
        return self.label


def catch_track(client):
    try:
        queues = client.queues_list()
        last_queue = client.queue(queues[0].id)
        track_id = last_queue.get_current_track()
        track = track_id.fetch_track()
        return track
    except Exception as e:
        print("Cannot catch trackID.\n\n")
        return 0


def catch_label(client):
    try:
        track = catch_track(client)
        artists = ', '.join(track.artists_name())
        title = track.title
        return f"{artists} - {title}"
    except Exception as e:
        return 'No track'
