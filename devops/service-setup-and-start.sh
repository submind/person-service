#!/usr/bin/env bash
sudo chmod 500 startup.sh
sudo ln -s /home/ec2-user/app/startup-script.sh /etc/init.d/person-service
sudo service person-service start