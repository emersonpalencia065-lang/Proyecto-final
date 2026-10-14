package com.canchapro;

import com.canchapro.modelo.Cliente;
import com.canchapro.modelo.Cancha;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CANCHAPRO (SEMANA 1 - DIA 1) ===");

        Cliente cliente1 = new Cliente("CLI01", "Carlos Perez", "55551234", "carlos@gmail.com", "2026-10-12");
        System.out.println("Cliente creado: " + cliente1);

        Cancha cancha1 = new Cancha("C01", "La Bombonera", "Futbol 5", 10, "DISPONIBLE", "08:00", "23:00", 150.0);
        System.out.println("Cancha creada: " + cancha1);

        System.out.println("\nObjetos creados correctamente. Listo para pasar al Dia 2.");
    }
}
