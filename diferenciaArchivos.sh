#!/bin/bash
diff -u --ignore-all-space $1 $2|grep -v "+++\|---\|@@\|No newline at end of file"

