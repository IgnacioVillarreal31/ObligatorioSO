#!/bin/bash
awk -F ':' '{if ($7~/nologin/ && $3>=1000) print $1":"$3":"$6}' $1
