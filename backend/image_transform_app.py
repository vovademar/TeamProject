import base64
from io import BytesIO

import numpy as np
from PIL import Image
from deepface import DeepFace as df
from flask import Flask
from flask import request, make_response, jsonify, json
from flask_api import status

app = Flask(__name__)


@app.post("/transform-image")
def transform():
    resp_status = status.HTTP_200_OK
    resp_message = ""
    resp_image = ""

    content_type = request.headers.get("Content-Type")
    if content_type == "application/json":
        data: dict = json.loads(request.data)

        image_encoded = data.get("image", "")
        image_transformed, error_status = _transform_(image_encoded)

        if image_transformed == "":
            resp_message = error_status
            resp_status = status.HTTP_415_UNSUPPORTED_MEDIA_TYPE

        resp_image = image_transformed
    else:
        resp_message = "Request must be provided in JSON format"
        resp_status = status.HTTP_400_BAD_REQUEST

    resp = make_response(
        jsonify({"message": resp_message, "image": resp_image}),
        resp_status)

    return resp


# image_transformed, error_status
def _transform_(image_encoded: str) -> tuple[str, str]:
    image_transformed = ""
    error_status = ""

    try:
        image = base64.b64decode(image_encoded)
        image = np.frombuffer(image, dtype='uint8')
        image = np.asarray(Image.open(BytesIO(image.tobytes())), dtype='uint8')

        faces = df.extract_faces(image, detector_backend='retinaface', enforce_detection=False)

        if len(faces) != 0:
            face = faces[0]['face']
            face *= 255
            face = face.astype('uint8')
            image_transformed = str(base64.b64encode(face), 'ascii')

    except Exception as e:
        error_status = str(e)

    return image_transformed, error_status


if __name__ == "__main__":
    app.run(debug=True)

# Пример POST запроса
# В теле запроса надо передать изображение в формате base64
# curl -X POST -H "Content-type: application/json" -v -d "{\"image\": \"Изображение закодированное в base64\"}" "http://127.0.0.1:5000/transform-image"