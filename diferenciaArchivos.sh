#!/bin/bash
diff -u -b --ignore-all-space --ignore-space-change $1 $2|grep -v "+++\|---\|@@\|No newline at end of file"

