import RPi.GPIO as gpio
import time
import sys

gpio.setwarnings(False)
gpio.setmode(gpio.BOARD)

gpio.setup(5, gpio.OUT)
gpio.setup(7, gpio.OUT)
gpio.setup(13, gpio.OUT)
gpio.setup(15, gpio.OUT)

cmd = sys.argv[1]
t = sys.argv[2]

if cmd == "forward":
	gpio.output(5, True)
	gpio.output(7, False)
	gpio.output(13, True)
	gpio.output(15, False)
elif cmd == "backward":
	gpio.output(5, False)
	gpio.output(7, True)
	gpio.output(13, False)
	gpio.output(15, True)
elif cmd == "left":
	gpio.output(5, False)
	gpio.output(7, True)
	gpio.output(13, True)
	gpio.output(15, False)
elif cmd == "right":
	gpio.output(5, True)
	gpio.output(7, False)
	gpio.output(13, False)
	gpio.output(15, True)

time.sleep(float(t))

gpio.output(5, False)
gpio.output(7, False)
gpio.output(13, False)
gpio.output(15, False)
