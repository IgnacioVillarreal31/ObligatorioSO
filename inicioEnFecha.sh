#!/bin/bash

if [ `echo $2 | cut -c1` -eq "0" ]; then
grep "$1  `echo $2 | cut -c2`" $3 |egrep 'session opened|password check failed|incorrect password' 
else
grep "$1  $2" $3 |egrep 'session opened|password check failed|incorrect 
fi
