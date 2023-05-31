from deepface import DeepFace
from flask import request
from flask import send_file
from yandex_music import Client

from backend.app import app
from music.get_current_track import CurrentTrack
from music.radio.track_by_mood import TrackByMood

with open('token.txt', 'r') as file:
    token_music = file.read().rstrip()
# token_music = ""
client: Client = Client(token_music).init()


@app.route('/asd')
def asd():
    return "success"


@app.route('/file-downloads')
def file_downloads():
    try:
        return send_file('/backend/So_Lost.mp3')
    except Exception as e:
        return str(e)


@app.route('/gettrackname')
def get_track_id():
    curTrack = CurrentTrack(client).get_label()
    return curTrack


@app.route('/getcover')
def get_picture():
    CurrentTrack(client).get_pic()
    return send_file(f'/Users/valdemar/PycharmProjects/TeamProject/backend/{CurrentTrack(client).get_label()}.png')


@app.route('/sendtoken', methods=['POST'])
def json_example():
    request_data = request.args.get("token")
    pre_download_each()
    return save_data(request_data)


def pre_download_each():
    pre_download_sad()
    pre_download_angry()
    pre_download_happy()
    pre_download_disgust()
    pre_download_fear()
    pre_download_neutral()
    pre_download_surprise()


def save_data(text):
    token_file = open('token.txt', 'w')
    token_file.write(text)
    token_file.close()
    return "token saved"


@app.route('/play_sad_radio')
def sad_radio():
    with open('sad.txt', 'r') as file:
        track_path = file.read().rstrip()
    pre_download_sad()
    return send_file(track_path, as_attachment=True)


def pre_download_sad():
    track_by_mood = TrackByMood(client)
    track = track_by_mood.play_sad()[0]
    artists = ', '.join(track.artists_name())
    title = track.title
    filename = f'{artists}-{title}.mp3'
    track.download(filename)
    file_sad = open('sad.txt', 'w')
    file_sad.write(f"/Users/valdemar/PycharmProjects/TeamProject/backend/{filename}")


@app.route('/play_angry_radio')
def angry_radio():
    with open('angry.txt', 'r') as file_angry:
        track_path = file_angry.read().rstrip()
    pre_download_angry()
    return send_file(track_path, as_attachment=True)


def pre_download_angry():
    track_by_mood = TrackByMood(client)
    track = track_by_mood.play_angry()[0]
    artists = ', '.join(track.artists_name())
    title = track.title
    filename = f'{artists}-{title}.mp3'
    track.download(filename)
    file_sad = open('angry.txt', 'w')
    file_sad.write(f"/Users/valdemar/PycharmProjects/TeamProject/backend/{filename}")


@app.route('/play_disgust_radio')
def disgust_radio():
    with open('disgust.txt', 'r') as file_disgust:
        track_path = file_disgust.read().rstrip()
    pre_download_disgust()
    return send_file(track_path, as_attachment=True)


def pre_download_disgust():
    track_by_mood = TrackByMood(client)
    track = track_by_mood.play_disgust()[0]
    artists = ', '.join(track.artists_name())
    title = track.title
    filename = f'{artists}-{title}.mp3'
    track.download(filename)
    file_sad = open('disgust.txt', 'w')
    file_sad.write(f"/Users/valdemar/PycharmProjects/TeamProject/backend/{filename}")


@app.route('/play_fear_radio')
def fear_radio():
    with open('fear.txt', 'r') as file_fear:
        track_path = file_fear.read().rstrip()
    pre_download_fear()
    return send_file(track_path, as_attachment=True)


def pre_download_fear():
    track_by_mood = TrackByMood(client)
    track = track_by_mood.play_fear()[0]
    artists = ', '.join(track.artists_name())
    title = track.title
    filename = f'{artists}-{title}.mp3'
    track.download(filename)
    file_sad = open('fear.txt', 'w')
    file_sad.write(f"/Users/valdemar/PycharmProjects/TeamProject/backend/{filename}")


@app.route('/play_happy_radio')
def happy_radio():
    with open('happy.txt', 'r') as file_fear:
        track_path = file_fear.read().rstrip()
    pre_download_happy()
    return send_file(track_path, as_attachment=True)


def pre_download_happy():
    track_by_mood = TrackByMood(client)
    track = track_by_mood.play_happy()[0]
    artists = ', '.join(track.artists_name())
    title = track.title
    filename = f'{artists}-{title}.mp3'
    track.download(filename)
    file_sad = open('happy.txt', 'w')
    file_sad.write(f"/Users/valdemar/PycharmProjects/TeamProject/backend/{filename}")


@app.route('/play_neutral_radio')
def neutral_radio():
    with open('neutral.txt', 'r') as file_neutral:
        track_path = file_neutral.read().rstrip()
    pre_download_neutral()
    return send_file(track_path, as_attachment=True)


def pre_download_neutral():
    track_by_mood = TrackByMood(client)
    track = track_by_mood.play_neutral()[0]
    artists = ', '.join(track.artists_name())
    title = track.title
    filename = f'{artists}-{title}.mp3'
    track.download(filename)
    file_sad = open('neutral.txt', 'w')
    file_sad.write(f"/Users/valdemar/PycharmProjects/TeamProject/backend/{filename}")


@app.route('/play_surprise_radio')
def surprise_radio():
    with open('surprise.txt', 'r') as file_neutral:
        track_path = file_neutral.read().rstrip()
    pre_download_surprise()
    return send_file(track_path, as_attachment=True)


def pre_download_surprise():
    track_by_mood = TrackByMood(client)
    track = track_by_mood.play_surprise()[0]
    artists = ', '.join(track.artists_name())
    title = track.title
    filename = f'{artists}-{title}.mp3'
    track.download(filename)
    file_sad = open('surprise.txt', 'w')
    file_sad.write(f"/Users/valdemar/PycharmProjects/TeamProject/backend/{filename}")


@app.route("/analyze", methods=["POST"])
def analyze():
    input_args = request.get_json()

    if input_args is None:
        return {"message": "empty input set passed"}

    img_path = input_args.get("img_path")
    if img_path is None:
        return {"message": "you must pass img_path input"}

    detector_backend = input_args.get("detector_backend", "retinaface")
    enforce_detection = input_args.get("enforce_detection", False)
    align = input_args.get("align", True)
    actions = input_args.get("actions", ["emotion"])

    demographies = analyze_image(
        img_path=img_path,
        actions=actions,
        detector_backend=detector_backend,
        enforce_detection=enforce_detection,
        align=align,
    )

    return demographies["results"][0]["dominant_emotion"]


def analyze_image(img_path, actions, detector_backend, enforce_detection, align):
    result = {}
    demographies = DeepFace.analyze(
        img_path=img_path,
        actions=actions,
        detector_backend=detector_backend,
        enforce_detection=enforce_detection,
        align=align,
    )
    result["results"] = demographies
    return result
