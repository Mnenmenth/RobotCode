from io import BytesIO
import socket
from PIL import Image
import sys
from queue import Queue
import struct
import thread
import threading
import tkinter
from ImageTk import PhotoImage
import time

server = socket.socket()
server.connect(('192.168.1.125', 2223))

conn = server.makefile('rb')

#q = Queue(maxsize=0)

root = tkinter.Tk()

#frame = tkinter.Frame(root)

running = True

def update_img(container, img_stream):
    img = PhotoImage(Image.open(img_stream))
    container.configure(image = img)
    container.image = img

def get_img(container, conn)

def update_loop(container, conn):
    time.sleep(1)
    while running:
        img_len = struct.unpack('<L', conn.read(struct.calcsize('<L')))[0]
        stream = BytesIO(conn.read(image_len))
        thread.start_new_thread(update_img, (container, stream))
def close_callback():
    running = False
    root.destroy()
imgContainer = tkinter.Label(root)
imgContainer.pack(side = 'bottom', fill = 'both', expand = 'yes')
thread.start_new_thread(update_loop, (imgContainer, conn, q))
root.protocol("WM_DELETE_WINDOW", callback)
root.mainloop()
