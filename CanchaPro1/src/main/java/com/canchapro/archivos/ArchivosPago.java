package com.canchapro.archivos;

import com.canchapro.modelo.Pago;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivosPago {

    private String ruta = "datos" + File.separator + "pagos.txt";

    public void guardarPago(Pago pago) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            FileWriter archivo = new FileWriter(ruta, true);
            BufferedWriter bw = new BufferedWriter(archivo);

            String linea = pago.getCodigo() + "," +
                    pago.getIdReserva() + "," +
                    pago.getFechaHora() + "," +
                    pago.getMonto() + "," +
                    pago.getMetodoPago() + "," +
                    pago.getObservacion();

            bw.write(linea);
            bw.newLine();

            bw.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Pago[] leerPagos() {
        Pago[] pagos = new Pago[100];
        int total = 0;

        File f = new File(ruta);
        if (!f.exists()) {
            return new Pago[0];
        }

        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String lineatmp;
            lineatmp = br.readLine();
            while (lineatmp != null) {
                if (!lineatmp.trim().isEmpty()) {
                    String[] datos = lineatmp.split(",");
                    if (datos.length >= 5) {
                        String obs = (datos.length >= 6) ? datos[5].trim() : "";
                        pagos[total] = new Pago(
                                datos[0].trim(),
                                datos[1].trim(),
                                datos[2].trim(),
                                Double.parseDouble(datos[3].trim()),
                                datos[4].trim(),
                                obs
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

        Pago[] resultado = new Pago[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = pagos[i];
        }
        return resultado;
    }
}
