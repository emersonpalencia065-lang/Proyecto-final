package com.canchapro.archivos;

import com.canchapro.modelo.Producto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivosProducto {

    private String ruta = "datos" + File.separator + "productos.txt";

    public void guardarProducto(Producto p) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            FileWriter archivo = new FileWriter(ruta, true);
            BufferedWriter bw = new BufferedWriter(archivo);

            String linea = p.getCodigo() + "," + p.getNombre() + "," + p.getCategoria() + "," + p.getPrecio() + "," + p.getStock() + "," + p.getStockMinimo() + "," + p.getEstado();
            bw.write(linea);
            bw.newLine();

            bw.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void guardarTodos(Producto[] productos) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            FileWriter archivo = new FileWriter(ruta, false);
            BufferedWriter bw = new BufferedWriter(archivo);

            for (int i = 0; i < productos.length; i++) {
                Producto p = productos[i];
                String linea = p.getCodigo() + "," + p.getNombre() + "," + p.getCategoria() + "," + p.getPrecio() + "," + p.getStock() + "," + p.getStockMinimo() + "," + p.getEstado();
                bw.write(linea);
                bw.newLine();
            }

            bw.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public boolean actualizarStock(String codigo, int cantidadVendida) {
        Producto[] productos = leerProductos();
        boolean actualizado = false;
        for (int i = 0; i < productos.length; i++) {
            if (productos[i].getCodigo().equalsIgnoreCase(codigo)) {
                int nuevoStock = productos[i].getStock() - cantidadVendida;
                if (nuevoStock < 0) {
                    return false;
                }
                productos[i].setStock(nuevoStock);
                actualizado = true;
                break;
            }
        }
        if (actualizado) {
            guardarTodos(productos);
            return true;
        }
        return false;
    }

    public Producto[] leerProductos() {
        Producto[] productos = new Producto[100];
        int total = 0;

        File f = new File(ruta);
        if (!f.exists()) {
            return new Producto[0];
        }

        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String lineatmp;
            lineatmp = br.readLine();
            while (lineatmp != null) {
                if (!lineatmp.trim().isEmpty()) {
                    String[] datos = lineatmp.split(",");
                    if (datos.length >= 7) {
                        productos[total] = new Producto(
                                datos[0].trim(),
                                datos[1].trim(),
                                datos[2].trim(),
                                Double.parseDouble(datos[3].trim()),
                                Integer.parseInt(datos[4].trim()),
                                Integer.parseInt(datos[5].trim()),
                                datos[6].trim()
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

        Producto[] resultado = new Producto[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = productos[i];
        }
        return resultado;
    }
}
