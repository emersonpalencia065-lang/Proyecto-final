package com.canchapro.archivos;

import com.canchapro.modelo.Cancha;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivosCancha {

    private String ruta = "datos" + File.separator + "canchas.txt";

    public void guardarCancha(Cancha cancha) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            FileWriter archivo = new FileWriter(ruta, true);
            BufferedWriter bw = new BufferedWriter(archivo);

            String linea = cancha.getCodigo() + "," + cancha.getNombre() + "," + cancha.getTipo() + "," + cancha.getCapacidad() + "," + cancha.getEstado() + "," + cancha.getHoraApertura() + "," + cancha.getHoraCierre() + "," + cancha.getPrecioBase();
            bw.write(linea);
            bw.newLine();

            bw.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Cancha[] leerCanchas() {
        Cancha[] canchas = new Cancha[50];
        int total = 0;

        File f = new File(ruta);
        if (!f.exists()) {
            return new Cancha[0];
        }

        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String lineatmp;
            lineatmp = br.readLine();
            while (lineatmp != null) {
                if (!lineatmp.trim().isEmpty()) {
                    String[] datos = lineatmp.split(",");
                    if (datos.length >= 8) {
                        canchas[total] = new Cancha(
                                datos[0].trim(),
                                datos[1].trim(),
                                datos[2].trim(),
                                Integer.parseInt(datos[3].trim()),
                                datos[4].trim(),
                                datos[5].trim(),
                                datos[6].trim(),
                                Double.parseDouble(datos[7].trim())
                        );
                        total++;
                    }
                }
                lineatmp = br.readLine();
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        Cancha[] resultado = new Cancha[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = canchas[i];
        }
        return resultado;
    }
}
