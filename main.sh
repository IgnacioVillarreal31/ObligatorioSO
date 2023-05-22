#!/bin/bash

ayer=`date +%Y-%m-%d -d "yesterday"`
semana=`date -d "$ayer" +%W`
archivo=`mktemp`
mkdir "`date +%W`/`date +%Y-%m-%d`" 2>/dev/null

for user in `bash usuariosValidos.sh /etc/passwd`
do

usuario=`echo $user | cut -d: -f1`
touch "`date +%W`/`date +%Y-%m-%d`/$usuario.txt"

directorio=`echo $user | cut -d: -f3`
find $directorio > $archivo
  
if test -f "$semana/$ayer/$usuario.txt"; then
bash diferenciaArchivos.sh `grep -v ^- "$semana/$ayer/$usuario.txt"|cut -c2-` $archivo > "`date +%W`/`date +%Y-%m-%d`/$usuario.txt"
else 
sed -e 's/^/+/' archivo > "`date +%W`/`date +%Y-%m-%d`/$usuario.txt"
fi
done
