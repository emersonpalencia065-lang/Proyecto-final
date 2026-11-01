package com.canchapro.archivos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivosConfiguracion {

    private String ruta = "datos" + File.separator + "configuracion.txt";

    public boolean isModuloTiendaActivo() {
        File f = new File(ruta);
        if (!f.exists()) {
            return true;
        }

        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String lineatmp;
            lineatmp = br.readLine();
            while (lineatmp != null) {
                lineatmp = lineatmp.trim();
                if (lineatmp.startsWith("modulo_tienda=")) {
                    String valor = lineatmp.substring("modulo_tienda=".length()).trim();
                    br.close();
                    fr.close();
                    return Boolean.parseBoolean(valor);
                }
                lineatmp = br.readLine();
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return true;
    }

    public void setModuloTiendaActivo(boolean activo) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            FileWriter archivo = new FileWriter(ruta, false);
            BufferedWriter bw = new BufferedWriter(archivo);

            bw.write("modulo_tienda=" + activo);
            bw.newLine();

            bw.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
