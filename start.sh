#!/bin/bash
FILENAME="nhk-easy-crawler-1.2.jar"
mv build/libs/* /

java -jar ${FILENAME} -c 330868
