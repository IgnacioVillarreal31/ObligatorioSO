#!/bin/bash

test_archivo_semana21(){
    ruta="/home/ivillarreal/Escritorio/ObligatorioSO-main"
    date --set "2023-05-26"
    bash "$ruta/main.sh"

    assertTrue "Existe la carpeta: " " [ -d $ruta/21/2023-05-26 ] "
}

test_archivo_semana22(){
    ruta="/home/ivillarreal/Escritorio/ObligatorioSO-main"
    date --set "2023-05-29"
    bash "$ruta/main.sh"

    assertTrue "Existe la carpeta: " " [ -d $ruta/22/2023-05-29 ] "
}

test_archivo_semana_inexistente(){
    ruta="/home/ivillarreal/Escritorio/ObligatorioSO-main"

    assertFalse "Existe la carpeta: " " [ -d $ruta/21/2023-05-27 ] "
}

test_archivo_(){
    ruta="/home/ivillarreal/Escritorio/ObligatorioSO-main"
    archivo=`mktemp`
    diff -u -b --ignore-all-space --ignore-space-change "$ruta/archivoPruebaTest2" "$ruta/21/2023-05-26/ivillarreal.txt" > $archivo 
    
    tamano=$(wc -c < "$archivo")
    assertTrue "El archivo esta vacio" "[[ $tamano -eq 0 ]]"
    
}


. /home/ivillarreal/Escritorio/ObligatorioSO-main/shunit2/shunit2
