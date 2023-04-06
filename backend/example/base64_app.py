# Скрипт для преобразования изображений в текст и обратно через base64

import argparse
import base64
import os

MAX_FILE_SIZE = 2 * 1024 * 1024  # 2MB

def encode_image(input: bytearray) -> bytes:
    return base64.b64encode(input)

def decode_image(input: bytearray) -> bytes:
    return base64.b64decode(input)

if __name__ == '__main__':
    # Создание парсера для использования через терминал
    # Пример: script.py filename --encode или --decode [-e|-d]
    parser = argparse.ArgumentParser(
        prog='base64-app',
        description='image base64 encoder decoder')

    parser.add_argument('file', nargs='+', type=argparse.FileType('rb'))

    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument('-e', '--encode', action='store_true')
    group.add_argument('-d', '--decode', action='store_true')

    args = parser.parse_args()

    files = args.file

    # Проверка размера файла
    for file in files:
        file_stat = os.stat(file.name)
        if file_stat.st_size > MAX_FILE_SIZE:
            raise Exception(f'File should has less than {MAX_FILE_SIZE} bytes size')

    # Кодирование / декодирование и запись файлов
    for file in files:
        file_input = bytearray(file.read())

        if args.encode:
            output = encode_image(file_input)
            with open(file.name + '_encoded', 'wt') as f:
                f.write(str(output, 'ascii'))
        else:
            output = decode_image(file_input)
            with open(file.name + '_decoded', 'wb') as f:
                f.write(output)
