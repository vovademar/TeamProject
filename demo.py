from yandex_music import Client

from auth.get_token import GetToken
from music.get_current_track import CurrentTrack

token = GetToken().token
print(token)
client = Client(token).init()

while True:
    curTrack = CurrentTrack(client).get_label()
    print(curTrack)

