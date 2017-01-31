from io import BytesIO
import socket
import pygame
import sys
import struct

server = socket.socket()
server.connect(('192.168.1.125', 2223))
pygame.init()

size = (640, 480)

screen = pygame.display.set_mode(size)

c = pygame.time.Clock()

conn = server.makefile('rb')
while True:
	img_len = struct.unpack('<L', conn.read(struct.calcsize('<L')))[0]

	#img_stream = BytesIO()
	#img_stream.write(conn.read(img_len))
	#img_stream.seek(0)

	#img_b = img_stream.read()
	#img_str = conn.read(img_len).decode()

	img = pygame.image.frombuffer(conn.read(img_len), size, 'RGB')
	screen.blit(img, (0, 0))
	pygame.display.update()

	for event in pygame.event.get():
		if event.type == pygame.QUIT:
			sys.exit()

	#c.tick(30)