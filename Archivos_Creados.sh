#!/bin/bash

#Obtenemos la ruta del directorio personal del usuario actual
dir_usuario = $(echo $HOME)

#Obtenemos la fecha de ayer y la de hoy
fecha_ayer = $(date -d "yesterday" '+%Y-%m-%d')
fecha_hoy = $(date '+%Y-%m-%d')

#Creamos un archivo temporal con el fin de guardar los archivos generados ayer
archivos_ayer = $(mktemp)

#Buscamos los archivos generados ayer y los guardamos en el archivo temporal de ayer
find $dir_usuario -type f -user $USER -newermt "$fecha_ayer" ! -newermt "$fecha_hoy" > $archivos_ayer

#Creamos un archivo temporal con el fin de guardar los archivos generados hoy
archivos_hoy = $(mktemp)

#Buscamos los archivos generados hoy y los guardamos en el archivo temporal de hoy
find $dir_usuario -type f -user $USER -newermt "$fecha_hoy" > $archivos_hoy

#Comparamos los archivos generados hoy con los que fueron generados ayer y mostramos la diferencia
diff -u $archivos_hoy $archivos_ayer

#Eliminamos los archivos temporales
rm $archivos_hoy $archivos_ayer 

