#!/bin/bash
myPath="$(dirname "$(readlink -f "$0")")"
shellPath="/codetuner"
shellPath="$myPath$shellPath"

sudo cp  $shellPath /usr/local/bin


jarPath="/codetuner-1.0-SNAPSHOT-all.jar"
jarPath="$myPath$jarPath"
sudo cp  $jarPath /usr/local/bin

sudo chmod +777 /usr/local/bin/codetuner