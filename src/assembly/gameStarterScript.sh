#!/bin/bash
cd $HOME
sleep 5
sh ./deploy/PlasticDive/start.sh ./deploy/PlasticDive/ PlasticDive &
sleep 5
wmctrl -a "Plastic Dive"