import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Posiciones {

    public ArrayList<Posicion> aterrizar01;
    public ArrayList<Posicion> aterrizar06;
    public ArrayList<Posicion> aterrizar19;
    public ArrayList<Posicion> aterrizar24;

    public ArrayList<Posicion> despegar01;
    public ArrayList<Posicion> despegar06;
    public ArrayList<Posicion> despegar19;
    public ArrayList<Posicion> despegar24;
    public ArrayList<Posicion> esperar;
    public ArrayList<Posicion> taxear01porton;
    public ArrayList<Posicion> taxear06porton;
    public ArrayList<Posicion> taxear19porton;
    public ArrayList<Posicion> taxear24porton;


    public ArrayList<Posicion> taxearporton01;
    public ArrayList<Posicion> taxearporton06;
    public ArrayList<Posicion> taxearporton19;
    public ArrayList<Posicion> taxearporton24;

    public HashMap<String, ArrayList<Posicion>> mapa;

    public int posicionEsperar = 0;


    public Posiciones() {

        mapa = new HashMap<String, ArrayList<Posicion>>();
        aterrizar01 = new ArrayList<Posicion>();
        aterrizar06 = new ArrayList<Posicion>();
        aterrizar19 = new ArrayList<Posicion>();
        aterrizar24 = new ArrayList<Posicion>();

        despegar01 = new ArrayList<Posicion>();
        despegar06 = new ArrayList<Posicion>();
        despegar19 = new ArrayList<Posicion>();
        despegar24 = new ArrayList<Posicion>();

        esperar = new ArrayList<Posicion>();

        taxear01porton = new ArrayList<Posicion>();
        taxear06porton = new ArrayList<Posicion>();
        taxear19porton = new ArrayList<Posicion>();
        taxear24porton = new ArrayList<Posicion>();


        taxearporton01 = new ArrayList<Posicion>();
        taxearporton06 = new ArrayList<Posicion>();
        taxearporton19 = new ArrayList<Posicion>();
        taxearporton24 = new ArrayList<Posicion>();

        mapa.put("src/Posiciones/Aterrizar01.txt", aterrizar01);
        mapa.put("src/Posiciones/Aterrizar06.txt", aterrizar06);
        mapa.put("src/Posiciones/Aterrizar19.txt", aterrizar19);
        mapa.put("src/Posiciones/Aterrizar24.txt", aterrizar24);

        mapa.put("src/Posiciones/Despegar01.txt", despegar01);
        mapa.put("src/Posiciones/Despegar06.txt", despegar06);
        mapa.put("src/Posiciones/Despegar19.txt", despegar19);
        mapa.put("src/Posiciones/Despegar24.txt", despegar24);

        mapa.put("src/Posiciones/Esperar.txt", esperar);

        mapa.put("src/Posiciones/Taxear01Porton.txt", taxear01porton);
        mapa.put("src/Posiciones/Taxear06Porton.txt", taxear06porton);
        mapa.put("src/Posiciones/Taxear19Porton.txt", taxear19porton);
        mapa.put("src/Posiciones/Taxear24Porton.txt", taxear24porton);


        mapa.put("src/Posiciones/TaxearPorton01.txt", taxearporton01);
        mapa.put("src/Posiciones/TaxearPorton06.txt", taxearporton06);
        mapa.put("src/Posiciones/TaxearPorton19.txt", taxearporton19);
        mapa.put("src/Posiciones/TaxearPorton24.txt", taxearporton24);

        cargarPosiciones(this.mapa);

    }

    public static void cargarPosiciones(HashMap<String, ArrayList<Posicion>> mapa) {
        for (Map.Entry<String, ArrayList<Posicion>> entry : mapa.entrySet()) {

            String[] datos = ManejadorArchivosGenerico.leerArchivo(entry.getKey());
            for (String s : datos) {
                String[] arr = s.split(",");
                int x = Integer.valueOf(arr[0]);
                int y = Integer.valueOf(arr[1]);
                boolean permiso = false;
                if (arr.length == 3) {
                    permiso = Boolean.valueOf(arr[2]);
                }
                entry.getValue().add(new Posicion(x, y, permiso));
            }
        }

    }

}
