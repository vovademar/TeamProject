from backend.app import app
from flask import send_file
from yandex_music import Client
from flask import request

from music.get_current_track import CurrentTrack

token_music = ""
client: Client = Client(token_music).init()


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
    return send_file(f'/backend/{CurrentTrack(client).get_label()}.png')


@app.route('/sendtoken', methods=['POST'])
def json_example():
    request_data = request.args.get("token")
    return save_data(request_data)


def save_data(text):
    file = open('token.txt', 'w')
    file.write(text)
    file.close()
    return "token saved"
