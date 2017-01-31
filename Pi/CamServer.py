from io import BytesIO
import socket
import struct
from picamera import PiCamera
import time

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(('', 2223))
server.listen(1)

camera = PiCamera()
camera.start_preview()
time.sleep(2)
camera.resolution = (640, 480)
camera.framerate = 30
while True:
	try:
		client = server.accept()[0].makefile('wb')
		#(client, address) = server.accept()
		stream = BytesIO()
		camera.start_recording('/home/pi/FTP/files/vid1.h264', resize=(1920, 1080))
		for foo in camera.capture_continuous(stream, 'rgb', splitter_port=2, use_video_port=True):
			client.write(struct.pack('<L', stream.tell()))
			#client.sendall(struct.pack('<L', stream.tell()))
			client.flush()

			stream.seek(0)
			client.write(stream.read())
			#client.sendall(stream.read())

			stream.seek(0)
			stream.truncate()
			
	except socket.error:
		#server.close()
		camera.stop_recording()
		print('Client disconnected')
