#!/bin/bash

ruta="/home/ivillarreal/Escritorio/ObligatorioSO-main"

ayer=`date +%Y-%m-%d -d "yesterday"`
semana=`date -d "$ayer" +%W`

mkdir $ruta/"`date +%W`" 2>/dev/null
mkdir $ruta/"`date +%W`/`date +%Y-%m-%d`" 2>/dev/null
bash $ruta/inicioEnFecha.sh `date -d $ayer +%m` `date -d $ayer +%d`

for user in `bash $ruta/usuariosValidos.sh`
do
archivoHoy=`mktemp`
archivoAyer=`mktemp`
archivoDiferencia=`mktemp`
archivoCut=`mktemp`

usuario=`echo $user | cut -d: -f1`
touch "$ruta/`date +%W`/`date +%Y-%m-%d`/$usuario.txt"

directorio=`echo $user | cut -d: -f3`
find $directorio > $archivoHoy
  
if test -f "$ruta/$semana/$ayer/$usuario.txt"; then
	grep -v '^-' "$ruta/$semana/$ayer/$usuario.txt" > $archivoAyer
	cut -c2- $archivoAyer > $archivoCut
	bash "$ruta/diferenciaArchivos.sh" $archivoCut $archivoHoy > $archivoDiferencia

	if [ -s $archivoDiferencia ]; then
		cat $archivoDiferencia > "$ruta/`date +%W`/`date +%Y-%m-%d`/$usuario.txt"
	else
		sed -e 's/^/ /' $archivoHoy > "$ruta/`date +%W`/`date +%Y-%m-%d`/$usuario.txt"
	fi
else 
	sed -e 's/^/+/' $archivoHoy > "$ruta/`date +%W`/`date +%Y-%m-%d`/$usuario.txt"
fi
done
