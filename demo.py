from yandex_music import Client

from auth.get_token import GetToken
from music.get_current_track import CurrentTrack
from music.radio.track_by_mood import TrackByMood
from music.radio.track_by_mood import play_next_radio_track


<<<<<<< HEAD
token = ""
    # GetToken().token
=======
token = ""
>>>>>>> 4250a740a2347651ace8a092e45532b345a0058a
print(token)

client: Client = Client(token).init()

track_by_mood = TrackByMood(client)

track_by_mood.radi()
rad = track_by_mood.radi()[1]

print("next track:::")
play_next_radio_track(rad)

# while True:
#     curTrack = CurrentTrack(client).get_label()
#     print(curTrack)
