# !/bin/bash

echo "########################### Start Deploy!! ###########################"

DEFAULT_CONF="/root/nginx/default.conf"
IS_BLUE_RUNNING=$(docker ps | grep blue)

if [ -n "$IS_BLUE_RUNNING" ]; then
    IDLE_SERVER="green"
    CURRENT_SERVER="blue"
else
    IDLE_SERVER="blue"
    CURRENT_SERVER="green"
fi

echo ">>>>>>>>>>>>>>>>>> $CURRENT_SERVER is running..."

echo ">>>>>>>>>>>>>>>>>> Pull docker image to $IDLE_SERVER..."
docker compose pull $IDLE_SERVER

echo ">>>>>>>>>>>>>>>>>> Deploy $IDLE_SERVER..."
docker compose up -d $IDLE_SERVER

for ((RETRY_COUNT=0; RETRY_COUNT <= 10; RETRY_COUNT++));
do
    echo " health checking $IDLE_SERVER..."
    REQUEST=$(docker exec nginx curl http://$IDLE_SERVER:8080)
    echo $REQUEST
    if [ -n "$REQUEST" ]; then
        echo ">>>>>>>>>>>>>>>>>> Success health checking!!"
        break
    fi
    if [ $RETRY_COUNT -eq 10 ]; then
        echo " Health checking $IDLE_SERVER failed "
        echo ">>>>>>>>>>>>>>>>>> Stop deploying new application"
        exit 1
    fi
    sleep 3
done;


echo ">>>>>>>>>>>>>>>>>> Nginx reload..."
sed -i "s/$CURRENT_SERVER/$IDLE_SERVER/g" $DEFAULT_CONF
docker exec -d nginx service nginx reload

echo ">>>>>>>>>>>>>>>>>> Stop $CURRENT_SERVER..."
docker compose stop $CURRENT_SERVER

echo "########################### Deploy finished!! ###########################"