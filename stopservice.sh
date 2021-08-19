#!/bin/bash
service=myApp.service

if (( $(ps -ef | grep -v grep | grep $service | wc -l) > 2 ))
then
sudo systemctl stop $service
fi
