#!/usr/bin/env python3

from flask import Flask, request, abort
from time import sleep
import zlib
import xml.dom.minidom
import random
import sys

app = Flask(__name__)

count = 0

@app.route('/', defaults={'path': ''}, methods=['POST', 'GET'])
@app.route('/<path:path>', methods=['POST', 'GET'])
def catch_all(path):
    global count
    print('path: %s' % path)
    with open('post_file_%s.gz' % count,'w') as f:
       f.write(request.data)
       count = count + 1
       f.close()
    print('request.headers: %s' % request.headers.items())
    print('=============================================================================')
    sleep(random.uniform(30,600)/1000)
    return ''


if __name__ == '__main__':
    app.run(host = '0.0.0.0', port = sys.argv[1], debug=True)

