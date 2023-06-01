#!/bin/bash

ayer=`date +%Y-%m-%d -d "yesterday"`
semana=`date -d "$ayer" +%W`

mkdir "`date +%W`" 2>/dev/null
mkdir "`date +%W`/`date +%Y-%m-%d`" 2>/dev/null

for user in `bash usuariosValidos.sh`
do
archivo=`mktemp`
archivo2=`mktemp`
archivo3=`mktemp`
archivoCut=`mktemp`

usuario=`echo $user | cut -d: -f1`
touch "`date +%W`/`date +%Y-%m-%d`/$usuario.txt"

directorio=`echo $user | cut -d: -f3`
find $directorio > $archivo
  
if test -f "$semana/$ayer/$usuario.txt"; then
	echo "ARCHIVO ANTES:"
	cat "$semana/$ayer/$usuario.txt"
	grep -v '^-' "$semana/$ayer/$usuario.txt" > $archivo2

	echo "ARCHIVO DESPUES:"
	#sed -e 's/\s\+//g' $archivo2

	cut -c2- $archivo2 > $archivoCut
	cat $archivoCut
	bash diferenciaArchivos.sh $archivoCut $archivo > $archivo3

	if [ -s $archivo3 ]; then
		cat $archivo3 > "`date +%W`/`date +%Y-%m-%d`/$usuario.txt"

	else
		sed -e 's/^/ /' $archivo > "`date +%W`/`date +%Y-%m-%d`/$usuario.txt"
	fi
else 
	sed -e 's/^/+/' $archivo > "`date +%W`/`date +%Y-%m-%d`/$usuario.txt"
fi


echo "archivo 1"
cat "$archivo"
echo "archivo cut"
cat "$archivoCut"
echo "archivo 3"
cat "$archivo3"

done
