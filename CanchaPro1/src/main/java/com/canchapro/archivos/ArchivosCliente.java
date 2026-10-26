package com.canchapro.archivos;

import com.canchapro.modelo.Cliente;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivosCliente {

    private String ruta = "datos" + File.separator + "clientes.txt";

    public void guardarCliente(Cliente cliente) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            FileWriter archivo = new FileWriter(ruta, true);
            BufferedWriter bw = new BufferedWriter(archivo);

            String linea = cliente.getCodigo() + "," + cliente.getNombre() + "," + cliente.getTelefono() + "," + cliente.getCorreo() + "," + cliente.getFechaRegistro();
            bw.write(linea);
            bw.newLine();

            bw.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Cliente[] leerClientes() {
        Cliente[] clientes = new Cliente[100];
        int total = 0;

        File f = new File(ruta);
        if (!f.exists()) {
            return new Cliente[0];
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
                        clientes[total] = new Cliente(datos[0].trim(), datos[1].trim(), datos[2].trim(), datos[3].trim(), datos[4].trim());
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

        Cliente[] resultado = new Cliente[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = clientes[i];
        }
        return resultado;
    }
}
