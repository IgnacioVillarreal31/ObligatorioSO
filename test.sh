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



. /home/ivillarreal/Escritorio/ObligatorioSO-main/shunit2/shunit2
