#!/bin/bash

if [ `echo $2 | cut -c1` -eq "0" ]; then
typeset -i dia=`echo $2 | cut -c2`
grep -i "$1  $dia" $3 | egrep 'session opened|password check failed|incorrect password' 
else
grep -i "$1 $2" $3 | egrep 'session opened|password check failed|incorrect password'
fi
