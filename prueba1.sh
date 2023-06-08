#!/bin/bash

ruta="/home/ivillarreal/Escritorio/ObligatorioSO-main"
echo "Prueba 1"

echo "Dia 27/5/2023 Sabado, se crean 2 archivos nuevos"
date --set "2023-05-27"
touch "$ruta/archivoPrueba1.txt"
touch "$ruta/archivoPrueba2.txt"
bash "$ruta/main.sh"

echo "Dia 28/5/2023 Domingo, se crea 1 elemento y se borra 1 elemento"
date --set "2023-05-28"
touch "$ruta/archivoPrueba3.txt"
rm -rf "$ruta/archivoPrueba1.txt"
bash "$ruta/main.sh"

echo "Dia 29/5/2023 Lunes, cambio de semana, se crea 1 elemento."
date --set "2023-05-29"
bash "$ruta/main.sh"
touch "$ruta/archivoPrueba4.txt"

echo "Dia 30/5/2023 Martes."
date --set "2023-05-30"
bash "$ruta/main.sh"

echo "Se han creado todos los archivos. Visite las carpetas de la semana correspondiente, ya sea 21 y 22, y verifique los datos."
