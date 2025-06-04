#!/bin/bash
cd /home/pi/
sleep 2
sh ./deploy/Ueberduengung/start.sh ./deploy/Ueberduengung/ Ueberduengung &
sleep 5
wmctrl -a "Plastic Dive"