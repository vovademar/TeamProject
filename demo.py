from yandex_music import Client

from auth.get_token import GetToken
from music.get_current_track import CurrentTrack
from music.radio.track_by_mood import TrackByMood

token = ""
print(token)

client: Client = Client(token).init()

track_by_mood = TrackByMood(client)

track_by_mood.radi()

# while True:
#     curTrack = CurrentTrack(client).get_label()
#     print(curTrack)
