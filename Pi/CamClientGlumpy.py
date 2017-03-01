from io import BytesIO
import socket
import sys
import struct
import numpy
import glumpy

server = socket.socket()
server.connect(('192.168.1.125', 2224))

size = (640, 480)

conn = server.makefile('rb')
buf = numpy.zeros(size, dtype=np.float32)
im = glumpy.image.Image(buf)
while True:
	img_len = struct.unpack('<L', conn.read(struct.calcsize('<L')))[0]
	print(img_len)

	fig = glumpy.figure(size)
	buf = numpy.frombuffer(conn.read(img_len))
		
	fig.clear()
	im.update()
	im.draw(x=0, y=0, width=fig.width, height=fig.height)	