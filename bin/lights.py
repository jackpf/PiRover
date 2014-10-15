import RPi.GPIO as gpio
import time
import sys

ledPin = 23

gpio.setwarnings(False)
gpio.setmode(gpio.BOARD)

gpio.setup(ledPin, gpio.OUT)

while True:
	gpio.output(ledPin, True)
	time.sleep(1)
	gpio.output(ledPin, False)
	time.sleep(1)
