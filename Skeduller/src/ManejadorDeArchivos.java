import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileReader;

public class ManejadorDeArchivos {

    public static String leerArchivo(String filePath) {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contenido.append(line);
                contenido.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contenido.toString();
    }

    public static void escribirArchivo(String filePath, String contenido) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true)))) {
            writer.write(contenido);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
