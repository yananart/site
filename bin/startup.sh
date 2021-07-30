#!/bin/sh

current_dir=$(dirname "$0")

cd ${current_dir}/.. || exit
current_dir=$(pwd)

if [ ! -d ${current_dir}/tmpdir ]; then
    mkdir ${current_dir}/tmpdir
fi

pkg=$(ls site-*.jar)

mypid=$(ps aux | grep java | grep ${pkg} | awk '{print $2}')
if [ -n "${mypid}" ]; then
    echo "${pkg}(${mypid}) is running ..."
else
    echo "starting ${pkg} ..."
    nohup java -Djava.io.tmpdir="${current_dir}/tmpdir" -Dfile.encoding=UTF-8 -jar ${pkg} >/dev/null 2>&1 &
fi
