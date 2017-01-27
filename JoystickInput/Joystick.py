import pygame, sys
from pygame import joystick
from enum import Enum

pygame.init()
joystick.init()
print(joystick.get_count())

stick = joystick.Joystick(0)
stick.init()
print(stick.get_numaxes())

clock = pygame.time.Clock()

#mx and my are motor values (values not between 0 and 1)
x, y, mx, my = 0, 0, 0, 0

#True = Forwards; False = Reverse
direction = True

maxval = 255
deadzone = 89
deadzoned_max = maxval - deadzone

changed = True

def calc_speed():
	directional_speed = -(my+deadzone) if my < 0 else my+deadzone
	left_side, right_side = directional_speed, directional_speed
	if mx < 0:
		#if x additive is negative, then joystick is to left. 
		#This means add x to left side, therefore subtracting and slowing side down, causing turning to left
		left_side = directional_speed + mx
	elif mx > 0:
		#if x additive is positive, then joystick is to right.
		#This means subtract x from right side, therefore slowing side down, causing turning to right
		right_side = directional_speed - mx
	if left_side <= 0: left_side = 0
	if right_side <= 0: right_side = 0
	return left_side, right_side


while True:
	#clock.tick(60)
	pygame.event.poll()
	x1 = round(stick.get_axis(0), 3)
	y1 = round(stick.get_axis(1), 3)
	mx1 = mx
	my1 = my

	if y1 > 0 and direction == True:
		direction = False
		print('Direction set to reverse\n')
	elif y < 0 and direction == False:
		direction = True
		print('Direction set to forwards\n')

	if x1 != x:# and (x1 >= 0.01 or x1 <= -0.01):
		if x1 <= -0.01 and x1 >= -1.0:
			x = x1
			mx = round(x * deadzoned_max, 0)
		elif x1 > 0.01 and x1 <= 1.0:
			x = x1
			mx = round(x * deadzoned_max, 0)
		else:
			mx, x = 0, 0

	if y1 != y:# and (y1 >= 0.01 or y1 <= -0.01):
		if y1 <= -0.01 and y1 >= -1.0:
			y = y1
			my = round(y * -deadzoned_max, 0)
		elif y1 > 0.01 and y1 <= 1.0:
			y = y1
			my = round(y * deadzoned_max, 0)
		else:
			my, y = 0, 0

	if mx1 != mx:
		changed = True
	if my1 != my:
		changed = True

	if changed:
		changed = False
		#yzero = y < 0.01 and y > -0.01
		#xzero = x < 0.01 and x > -0.01

		left_side, right_side = calc_speed()

		yzero = left_side <= 89 and right_side <= 89
		xzero = left_side >= 89 and right_side >= 89


		if yzero and xzero:
			#print('X: 0, MX: 0, Y: 0, MY: 0\n')
			print('Direction: None; Left Side: 0; Right Side: 0\n')
		else:
			print('Direction: {}; Left Side: {}; Right Side: {}\n'.format('Forwards' if direction else 'Reverse', left_side, right_side))
			'''if yzero and not xzero:
				#print('X: {}, MX: {}, Y: 0, MY: 0\n'.format(x, mx+deadzone))
				print('Direction: {}; Left Side: {}; Right Side: 0\n'.format('Forwards' if direction else 'Reverse', left_side))
			if xzero and not yzero:
				#print('X: 0, MX: 0, Y: {}, MY: {}\n'.format(y, my+deadzone))
				print('Direction: {}; Left Side: 0; Right Side: {}\n'.format('Forwards' if direction else 'Reverse', right_side))
			if not yzero and not xzero:
				#print('X: {}, MX: {}, Y: {}, MY: {}\n'.format(x, mx+deadzone, y, my+deadzone))
				print('Direction: {}; Left Side: {}; Right Side: {}\n'.format('Forwards' if direction else 'Reverse', left_side, right_side))
'''
	for event in pygame.event.get():
		if event.type == pygame.QUIT: sys.exit()