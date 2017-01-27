from serial import Serial
import time

port = Serial('COM3', 9600, timeout=10)
time.sleep(2)
port.write(b'03 090')
time.sleep(2)
port.write(b'03 000')
port.close()