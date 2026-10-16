package com.canchapro.logica;

import com.canchapro.modelo.Cliente;
import com.canchapro.modelo.Cancha;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GestorDatos {

    private String rutaDatos = "datos" + File.separator;
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Cancha> canchas = new ArrayList<>();

    public GestorDatos() {
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        cargarCanchas();
        cargarClientes();
    }

    public void cargarClientes() {
        clientes.clear();
        File archivo = new File(rutaDatos + "clientes.txt");
        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(",");
                if (partes.length >= 5) {
                    Cliente cli = new Cliente(
                            partes[0].trim(),
                            partes[1].trim(),
                            partes[2].trim(),
                            partes[3].trim(),
                            partes[4].trim()
                    );
                    clientes.add(cli);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar clientes: " + e.getMessage());
        }
    }

    public void cargarCanchas() {
        canchas.clear();
        File archivo = new File(rutaDatos + "canchas.txt");
        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(",");
                if (partes.length >= 8) {
                    Cancha c = new Cancha(
                            partes[0].trim(),
                            partes[1].trim(),
                            partes[2].trim(),
                            Integer.parseInt(partes[3].trim()),
                            partes[4].trim(),
                            partes[5].trim(),
                            partes[6].trim(),
                            Double.parseDouble(partes[7].trim())
                    );
                    canchas.add(c);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar canchas: " + e.getMessage());
        }
    }

    public void guardarClientes() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaDatos + "clientes.txt"))) {
            for (Cliente c : clientes) {
                pw.println(c.getCodigo() + "," + c.getNombre() + "," + c.getTelefono() + "," + c.getCorreo() + "," + c.getFechaRegistro());
            }
        } catch (Exception e) {
            System.out.println("Error al guardar clientes: " + e.getMessage());
        }
    }

    public void guardarCanchas() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaDatos + "canchas.txt"))) {
            for (Cancha c : canchas) {
                pw.println(c.getCodigo() + "," + c.getNombre() + "," + c.getTipo() + "," + c.getCapacidad() + "," + c.getEstado() + "," + c.getHoraApertura() + "," + c.getHoraCierre() + "," + c.getPrecioBase());
            }
        } catch (Exception e) {
            System.out.println("Error al guardar canchas: " + e.getMessage());
        }
    }

    public boolean agregarCliente(Cliente nuevo) {
        for (Cliente c : clientes) {
            if (c.getCodigo().equalsIgnoreCase(nuevo.getCodigo()) || c.getTelefono().equalsIgnoreCase(nuevo.getTelefono())) {
                return false;
            }
        }
        clientes.add(nuevo);
        guardarClientes();
        return true;
    }

    public boolean agregarCancha(Cancha nueva) {
        for (Cancha c : canchas) {
            if (c.getCodigo().equalsIgnoreCase(nueva.getCodigo())) {
                return false;
            }
        }
        canchas.add(nueva);
        guardarCanchas();
        return true;
    }

    public Cliente buscarClientePorCodigo(String codigo) {
        for (Cliente c : clientes) {
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                return c;
            }
        }
        return null;
    }

    public Cancha buscarCanchaPorCodigo(String codigo) {
        for (Cancha c : canchas) {
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public ArrayList<Cancha> getCanchas() {
        return canchas;
    }
}
