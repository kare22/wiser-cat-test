#!/bin/bash

# Get the PID of the process using port 3000, excluding docker-proxy
PID=$(lsof -i :3000 | grep -v "docker-proxy" | awk 'NR==2 {print $2}')

# Check if a PID was found
if [ -z "$PID" ]; then
    echo "No non-Docker process is using port 3000."
else
    # Kill the process
    kill -9 $PID
    echo "Killed non-Docker process $PID that was using port 3000."
fi
