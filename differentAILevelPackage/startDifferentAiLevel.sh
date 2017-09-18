#!/bin/bash
path=$(cd `dirname $0`;pwd)
cd $path
touch data/data.lock.db
touch data/data.trace.db
chmod 666 data/data.lock.db
chmod 555 data/data.trace.db
java -jar ai.jar
