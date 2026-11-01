package com.canchapro.archivos;

import com.canchapro.modelo.DetalleVenta;
import com.canchapro.modelo.Venta;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivosVenta {

    private String rutaVentas = "datos" + File.separator + "ventas.txt";
    private String rutaDetalles = "datos" + File.separator + "detalle_ventas.txt";

    public void guardarVenta(Venta v) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            FileWriter archivo = new FileWriter(rutaVentas, true);
            BufferedWriter bw = new BufferedWriter(archivo);

            String linea = v.getCodigo() + "," + v.getFecha() + "," + v.getIdReserva() + "," + v.getTotal() + "," + v.getMetodoPago();
            bw.write(linea);
            bw.newLine();

            bw.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void guardarDetalle(DetalleVenta dv) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            FileWriter archivo = new FileWriter(rutaDetalles, true);
            BufferedWriter bw = new BufferedWriter(archivo);

            String linea = dv.getIdVenta() + "," + dv.getIdProducto() + "," + dv.getCantidad() + "," + dv.getPrecioUnitario() + "," + dv.getSubtotal();
            bw.write(linea);
            bw.newLine();

            bw.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Venta[] leerVentas() {
        Venta[] ventas = new Venta[100];
        int total = 0;

        File f = new File(rutaVentas);
        if (!f.exists()) {
            return new Venta[0];
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
                        ventas[total] = new Venta(
                                datos[0].trim(),
                                datos[1].trim(),
                                datos[2].trim(),
                                Double.parseDouble(datos[3].trim()),
                                datos[4].trim()
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

        Venta[] resultado = new Venta[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = ventas[i];
        }
        return resultado;
    }

    public DetalleVenta[] leerDetalles() {
        DetalleVenta[] detalles = new DetalleVenta[200];
        int total = 0;

        File f = new File(rutaDetalles);
        if (!f.exists()) {
            return new DetalleVenta[0];
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
                        detalles[total] = new DetalleVenta(
                                datos[0].trim(),
                                datos[1].trim(),
                                Integer.parseInt(datos[2].trim()),
                                Double.parseDouble(datos[3].trim()),
                                Double.parseDouble(datos[4].trim())
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

        DetalleVenta[] resultado = new DetalleVenta[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = detalles[i];
        }
        return resultado;
    }
}
