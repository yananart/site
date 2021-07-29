#!/bin/sh

current_dir=$(dirname "$0")

cd ${current_dir}/.. || exit

pkg=$(ls site-*.jar)

mypid=$(ps aux | grep java | grep ${pkg} | awk '{print $2}')

if [ -n "$mypid" ]; then
    echo "stopping ${pkg}($mypid) ..."
    kill -9 $mypid
else
    echo "${pkg} is not running"
fi
