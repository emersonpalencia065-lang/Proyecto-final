package com.canchapro.archivos;

import com.canchapro.modelo.ServicioAdicional;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivosServicio {

    private String ruta = "datos" + File.separator + "servicios.txt";

    public void guardarServicio(ServicioAdicional servicio) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            FileWriter archivo = new FileWriter(ruta, true);
            BufferedWriter bw = new BufferedWriter(archivo);

            String linea = servicio.getCodigo() + "," + servicio.getNombre() + "," + servicio.getPrecio();
            bw.write(linea);
            bw.newLine();

            bw.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public ServicioAdicional[] leerServicios() {
        ServicioAdicional[] servicios = new ServicioAdicional[50];
        int total = 0;

        File f = new File(ruta);
        if (!f.exists()) {
            return new ServicioAdicional[0];
        }

        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String lineatmp;
            lineatmp = br.readLine();
            while (lineatmp != null) {
                if (!lineatmp.trim().isEmpty()) {
                    String[] datos = lineatmp.split(",");
                    if (datos.length >= 3) {
                        servicios[total] = new ServicioAdicional(
                                datos[0].trim(),
                                datos[1].trim(),
                                Double.parseDouble(datos[2].trim())
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

        ServicioAdicional[] resultado = new ServicioAdicional[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = servicios[i];
        }
        return resultado;
    }
}
