import static org.junit.jupiter.api.Assertions.*;

class SchedullerTest {

    @org.junit.jupiter.api.Test
    void ejecutarProcesosCorrecto() {
        String stringEsperado = ManejadorDeArchivos.leerArchivo("src/LogPrueba.txt");
        String stringResultado = ManejadorDeArchivos.leerArchivo("src/Logs.txt");
        assertEquals(stringResultado,stringEsperado);
    }

    @org.junit.jupiter.api.Test
    void ejecutarProcesosIncorrecto() {
        String stringEsperado = ManejadorDeArchivos.leerArchivo("src/LogPrueba2.txt");
        String stringResultado = ManejadorDeArchivos.leerArchivo("src/Logs.txt");
        assertNotEquals(stringResultado,stringEsperado);
    }
}