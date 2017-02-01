from io import BytesIO
import socket
import struct
from picamera import PiCamera
import time
import sys

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server.bind(('', 2223))
server.listen(1)

camera = PiCamera()
camera.resolution = (640, 480)
camera.framerate = 30
camera.start_preview()

stream = BytesIO()

while True:
	try:
		(client, address) = server.accept()
		for foo in camera.capture_continuous(stream, 'rgb', use_video_port=True):
			print(len(stream.getvalue()))
			client.sendall(bytes(str(len(stream.getvalue())) + '\r\n'))
			stream.seek(0)
			client.sendall(stream.getvalue())
			stream.seek(0)
			stream.truncate()
	except socket.error:
		#server.close()
		print('Client disconnected')
