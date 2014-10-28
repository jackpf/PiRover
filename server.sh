#!/bin/bash

#################################################
# Script to start and stop the PiRover controller
#################################################

base_dir="/home/pi/workspace/PiRover"
logging=false
broadcast_port=1337
controller_port=1338
camera_port=1339
blink_speed=100000

#################################################

bin_dir="$base_dir/bin"
log_dir="$base_dir/logs"

declare -A processes=(
	["controller"]="--port=$controller_port"
	["camera"]="--port=$camera_port"
	["broadcast"]="--port=$broadcast_port --ctrlport=$controller_port --camport=$camera_port"
	["lights"]="--blink=100000"
)

if [ "$1" = "start" ]
then
	log_redir=$([ "$logging" == true ] && echo "&>$log_dir/$(date +"%d_%m_%y-%T").txt" || echo "")

	for process in "${!processes[@]}"
	do
		eval "$bin_dir/$process $log_redir ${processes[$process]} &"
	done
elif [ "$1" = "stop" ]
then
	for process in "${!processes[@]}"
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
