#!/bin/bash
lxterminal --command "echo 'Starting game'; sleep 20" &
cd $HOME/
sh ./deploy/Ueberduengung/start.sh ./deploy/Ueberduengung/ Ueberduengung &
sleep 10
wmctrl -a "Plastic Dive"