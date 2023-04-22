#!/bin/bash
# Fecha actual
actual=$(date +%F)
# Fecha ayer
ayer=$(date -d "yesterday" +%F)
# Archivo en el escritorio
archivo="/home/ivillarreal/Escritorio/secure$date.log"
# Sed para extraer las lineas entre las fechas dadas y redireccionar la salida a otro archivo
sed -n "/$ayer/,/$actual/p" /var/log/secure > $archivo
echo "El archivo se creo con exito y se guardo en $archivo"
