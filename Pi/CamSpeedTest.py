from io import BytesIO
import socket
import pygame
import sys
import struct

server = socket.socket()
server.connect(('192.168.1.125', 2225))
pygame.init()

#size = (640, 480)

#screen = pygame.display.set_mode(size)

c = pygame.time.Clock()

conn = server.makefile('rb')
while True:
	print('hi')
	img_len = struct.unpack('<L', conn.read(struct.calcsize('<L')))[0]
	print(img_len)
	conn.read(img_len)