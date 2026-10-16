package com.canchapro;

import com.canchapro.logica.GestorDatos;
import com.canchapro.modelo.Cliente;
import com.canchapro.modelo.Cancha;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   CANCHAPRO - SEMANA 1 (SESION 2)");
        System.out.println("==========================================");

        GestorDatos gestor = new GestorDatos();

        System.out.println("\n--- CLIENTES CARGADOS DESDE CLIENTES.TXT ---");
        for (Cliente c : gestor.getClientes()) {
            System.out.println("-> " + c);
        }

        System.out.println("\n--- CANCHAS CARGADAS DESDE CANCHAS.TXT ---");
        for (Cancha c : gestor.getCanchas()) {
            System.out.println("-> " + c);
        }

        System.out.println("\n==========================================");
        System.out.println("Datos cargados correctamente desde los .txt");
        System.out.println("==========================================");
    }
}
