from yandex_music import Client

from auth.get_token import GetToken
from music.get_current_track import CurrentTrack
from music.radio.track_by_mood import TrackByMood
from music.radio.track_by_mood import play_next_radio_track

token = ""
print(token)

client: Client = Client(token).init()

# print(client.account_status)
#
#angry, disgust, fear, happy, neutral, sad, surprise
#aggressive, haunting, dark, happy, dream, sad, epic
track_by_mood = TrackByMood(client)
#print(track_by_mood.play_sad())
print(track_by_mood.get_chosen_tracks())

# track_by_mood.radi()
# rad = track_by_mood.radi()[1]
# #
# # print("next track:::")
# # play_next_radio_track(rad)
#
#
# curTrack = CurrentTrack(client).get_label()
# #CurrentTrack(client)
#
# print(curTrack)
