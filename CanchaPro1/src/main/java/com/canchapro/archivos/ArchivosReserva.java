package com.canchapro.archivos;

import com.canchapro.modelo.Reserva;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivosReserva {

    private String ruta = "datos" + File.separator + "reservas.txt";

    public void guardarReserva(Reserva reserva) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            FileWriter archivo = new FileWriter(ruta, true);
            BufferedWriter bw = new BufferedWriter(archivo);

            String linea = reserva.getCodigo() + "," +
                    reserva.getIdCliente() + "," +
                    reserva.getIdCancha() + "," +
                    reserva.getFecha() + "," +
                    reserva.getHoraInicio() + "," +
                    reserva.getHoraFin() + "," +
                    reserva.getTotal() + "," +
                    reserva.getTotalPagado() + "," +
                    reserva.getSaldo() + "," +
                    reserva.getEstado() + "," +
                    reserva.getMotivoCancelacion() + "," +
                    reserva.getOperadorResponsable() + "," +
                    reserva.getServiciosCodigos();

            bw.write(linea);
            bw.newLine();

            bw.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void guardarTodas(Reserva[] reservas) {
        try {
            File carpeta = new File("datos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            FileWriter archivo = new FileWriter(ruta, false);
            BufferedWriter bw = new BufferedWriter(archivo);

            for (int i = 0; i < reservas.length; i++) {
                Reserva r = reservas[i];
                String linea = r.getCodigo() + "," +
                        r.getIdCliente() + "," +
                        r.getIdCancha() + "," +
                        r.getFecha() + "," +
                        r.getHoraInicio() + "," +
                        r.getHoraFin() + "," +
                        r.getTotal() + "," +
                        r.getTotalPagado() + "," +
                        r.getSaldo() + "," +
                        r.getEstado() + "," +
                        r.getMotivoCancelacion() + "," +
                        r.getOperadorResponsable() + "," +
                        r.getServiciosCodigos();

                bw.write(linea);
                bw.newLine();
            }

            bw.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public boolean actualizarReserva(Reserva reservaActualizada) {
        Reserva[] actuales = leerReservas();
        boolean encontrada = false;
        for (int i = 0; i < actuales.length; i++) {
            if (actuales[i].getCodigo().equalsIgnoreCase(reservaActualizada.getCodigo())) {
                actuales[i] = reservaActualizada;
                encontrada = true;
                break;
            }
        }
        if (encontrada) {
            guardarTodas(actuales);
            return true;
        }
        return false;
    }

    public Reserva[] leerReservas() {
        Reserva[] reservas = new Reserva[100];
        int total = 0;

        File f = new File(ruta);
        if (!f.exists()) {
            return new Reserva[0];
        }

        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String lineatmp;
            lineatmp = br.readLine();
            while (lineatmp != null) {
                if (!lineatmp.trim().isEmpty()) {
                    String[] datos = lineatmp.split(",");
                    if (datos.length >= 10) {
                        String motivo = (datos.length > 10) ? datos[10].trim() : "Ninguno";
                        String operador = (datos.length > 11) ? datos[11].trim() : "Admin";
                        String servs = (datos.length > 12) ? datos[12].trim() : "";

                        reservas[total] = new Reserva(
                                datos[0].trim(),
                                datos[1].trim(),
                                datos[2].trim(),
                                datos[3].trim(),
                                datos[4].trim(),
                                datos[5].trim(),
                                Double.parseDouble(datos[6].trim()),
                                Double.parseDouble(datos[7].trim()),
                                Double.parseDouble(datos[8].trim()),
                                datos[9].trim(),
                                motivo,
                                operador,
                                servs
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

        Reserva[] resultado = new Reserva[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = reservas[i];
        }
        return resultado;
    }
}
