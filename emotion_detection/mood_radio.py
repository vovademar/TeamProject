from yandex_music import Client
from auth.get_token import GetToken
import music.radio.track_by_mood as tbym
from deepface import DeepFace as df

emotion_mapper = {'angry': 'aggressive',
                  'disgust': 'beautiful',
                  'fear': 'dark',
                  'happy': 'happy',
                  'sad': 'sad',
                  'surprised': 'energetic',
                  'neutral': 'calm'}

man_analyze = df.analyze(img_path="../resources/woman.jpeg", actions=['emotion'])

s_tag = emotion_mapper[man_analyze['dominant_emotion']]
print(s_tag)

token = GetToken().token
client = Client(token).init()

s_id, s_form = tbym.choose_mood_by_emotion(s_tag)
tbym.play_chosen_radio(client, s_id, s_form)

