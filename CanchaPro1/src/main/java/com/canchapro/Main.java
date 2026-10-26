package com.canchapro;

import com.canchapro.archivos.ArchivosPago;
import com.canchapro.archivos.ArchivosReserva;
import com.canchapro.logica.LogicaReservas;
import com.canchapro.modelo.Pago;
import com.canchapro.modelo.Reserva;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   CANCHAPRO - SEMANA 2 (SESIONES 5 Y 6)");
        System.out.println("==========================================");

        LogicaReservas logica = new LogicaReservas();
        ArchivosReserva archReserva = new ArchivosReserva();
        ArchivosPago archPago = new ArchivosPago();

        System.out.println("\n--- 1. CREACION DE RESERVA CON ANTICIPO PARCIAL ---");
        String codRes = logica.generarSiguienteCodigoReserva();
        Reserva nuevaRes = new Reserva(
                codRes,
                "CLI02",
                "C02",
                "2026-10-28",
                "15:00",
                "17:00",
                300.0,
                0.0,
                300.0,
                "PENDIENTE",
                "Ninguno",
                "Admin",
                ""
        );

        boolean creada = logica.crearReservaConAnticipo(nuevaRes, 100.0, "Efectivo");
        System.out.println("Reserva creada con exito: " + creada);

        Reserva consultada = logica.buscarReserva(codRes);
        System.out.println("Estado actual de " + codRes + ": " + consultada);

        System.out.println("\n--- 2. VALIDACIONES DE PAGO Y ABONOS ---");
        boolean pagoInvalido1 = logica.registrarAbono(codRes, 500.0, "Efectivo", "Intento pagar de mas");
        System.out.println("Intento de abonar Q500 (Mayor al saldo Q200): " + (pagoInvalido1 ? "ACEPTADO" : "RECHAZADO"));

        boolean pagoInvalido2 = logica.registrarAbono(codRes, -50.0, "Efectivo", "Intento pago negativo");
        System.out.println("Intento de abonar monto negativo (-Q50): " + (pagoInvalido2 ? "ACEPTADO" : "RECHAZADO"));

        boolean abonoCorrecto = logica.registrarAbono(codRes, 200.0, "Transferencia", "Liquidacion saldo");
        System.out.println("Abono de Q200 para liquidar saldo: " + (abonoCorrecto ? "EXITOSO" : "FALLIDO"));

        consultada = logica.buscarReserva(codRes);
        System.out.println("Reserva tras liquidar saldo: " + consultada);

        System.out.println("\n--- 3. CANCELACION DE RESERVA (SIN BORRAR HISTORIAL) ---");
        String codCancelar = logica.generarSiguienteCodigoReserva();
        Reserva resParaCancelar = new Reserva(
                codCancelar,
                "CLI03",
                "C03",
                "2026-10-29",
                "10:00",
                "11:00",
                150.0,
                0.0,
                150.0,
                "PENDIENTE",
                "Ninguno",
                "Admin",
                ""
        );
        logica.crearReservaConAnticipo(resParaCancelar, 0.0, "Efectivo");

        boolean cancelada = logica.cancelarReserva(codCancelar, "Cliente enfermo", "Operador1");
        System.out.println("Reserva " + codCancelar + " cancelada: " + cancelada);

        Reserva resCancelada = logica.buscarReserva(codCancelar);
        System.out.println("Registro conservado en archivo: " + resCancelada);
        System.out.println("Motivo: " + resCancelada.getMotivoCancelacion() + " | Operador: " + resCancelada.getOperadorResponsable());

        System.out.println("\n--- 4. HISTORIAL DE PAGOS REGISTRADOS ---");
        Pago[] pagos = archPago.leerPagos();
        for (int i = 0; i < pagos.length; i++) {
            System.out.println("-> " + pagos[i]);
        }

        System.out.println("\n==========================================");
        System.out.println("Semana 2 completada al 100%.");
        System.out.println("Pagos, saldos y cancelaciones validados.");
        System.out.println("==========================================");
    }
}
