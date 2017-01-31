from serial import Serial
import time
import platform
import socket

serialPort = Serial('COM3' if platform.system() == 'Windows' else '/dev/ttyUSB0', 9600)
time.sleep(2)

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(('', 2222))
server.listen(1)

while True:
	(client, address) = server.accept()
	print('Connected')
	while True:
		data = client.recv(6)#.decode()
		if 'CLOSE' in data: break

		#print(data)
		serialPort.write(data)
