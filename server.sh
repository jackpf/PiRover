#!/bin/bash

dir="/home/pi/workspace/PiRover/bin"

if [ "$1" = "start" ]
then
	eval "$dir/controller &"
	eval "$dir/camera &"
	eval "$dir/lights &"
elif [ "$1" = "stop" ]
then
	processes=( "controller" "camera" "lights" )
	for process in "${processes[@]}"
	do
		pid=$(pidof "$process")
		if [ "$pid" -eq "$pid" ] 2>/dev/null # If is an actual PID
		then
			echo "Stopping $process"
			kill -9 "$pid"
		else
			echo "$process is not running"
		fi
	done
fi
