#!/bin/bash
grep "$1""$2"|egrep 'session opened|password check failed|incorrect password'
