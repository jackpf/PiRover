#!/bin/bash

#################################################
# Script to start and stop the PiRover controller
#################################################

base_dir="/home/pi/workspace/PiRover"
logging=false

#################################################

bin_dir="$base_dir/bin"
log_dir="$base_dir/logs"

processes=( "controller" "camera" "broadcast" "lights" )

if [ "$1" = "start" ]
then
	log_redir=$([ "$logging" == true ] && echo "&>$log_dir/$(date +"%d_%m_%y-%T").txt" || echo "")

	for process in "${processes[@]}"
	do
		eval "$bin_dir/$process $log_redir &"
	done
elif [ "$1" = "stop" ]
then
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
else
    echo "Invalid command"
fi
