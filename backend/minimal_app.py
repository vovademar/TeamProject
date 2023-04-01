from flask import Flask

app = Flask(__name__)

@app.route("/")
def hello_world():
    return "<p>Hello, world!</p>"

# enter this command to run
# flask --app <file.py> run
