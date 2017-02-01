from io import BytesIO
import socket
import pygame
import sys
import struct
import queue.Queue
import thread
import threading

server = socket.socket()
server.connect(('192.168.1.125', 2223))
pygame.init()

size = (640, 480)

screen = pygame.display.set_mode(size)

c = pygame.time.Clock()

conn = server.makefile('rb')

q = Queue(maxsize=0)

def process_frame(conn, queue):
    img_len = struct.unpack('<L', conn.read(struct.calcsize('<L')))[0]
    img = pygame.image.frombuffer(conn.read(img_len), size, 'RGB')
    queue.put(img)

while True:

    thread.start_new_thread(process_frame, (conn, q))

    screen.blit(queue.get(block=True, timeout=None), (0, 0))
    queue.task_done()
	pygame.display.update()

	for event in pygame.event.get():
		if event.type == pygame.QUIT:
            print('Shutting down...')
            for thread in threading.enumerate():
                thread.join()
                queue.join()
            server.close()
			sys.exit()

	#c.tick(30)
