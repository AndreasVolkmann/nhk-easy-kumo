#!/bin/bash
FILENAME="nhk-easy-crawler-1.2.jar"
mv build/libs/* /

java -jar ${FILENAME} -c 330868 -ffmpeg D:/Programme/ffmpeg-20180411-9825f77-win64-static
