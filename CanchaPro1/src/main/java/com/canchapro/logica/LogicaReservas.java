package com.canchapro.logica;

import com.canchapro.archivos.ArchivosCancha;
import com.canchapro.archivos.ArchivosPago;
import com.canchapro.archivos.ArchivosReserva;
import com.canchapro.modelo.Cancha;
import com.canchapro.modelo.Pago;
import com.canchapro.modelo.Reserva;
import com.canchapro.modelo.ServicioAdicional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogicaReservas {

    private ArchivosCancha archCancha;
    private ArchivosReserva archReserva;
    private ArchivosPago archPago;

    public LogicaReservas() {
        this.archCancha = new ArchivosCancha();
        this.archReserva = new ArchivosReserva();
        this.archPago = new ArchivosPago();
    }

    public boolean hayConflicto(LocalTime iniNuevo, LocalTime finNuevo, LocalTime iniExistente, LocalTime finExistente) {
        return iniNuevo.isBefore(finExistente) && finNuevo.isAfter(iniExistente);
    }

    public boolean estaDisponible(String idCancha, String fechaStr, LocalTime iniNuevo, LocalTime finNuevo) {
        Cancha canchaEncontrada = null;
        Cancha[] canchas = archCancha.leerCanchas();
        for (int i = 0; i < canchas.length; i++) {
            if (canchas[i].getCodigo().equalsIgnoreCase(idCancha)) {
                canchaEncontrada = canchas[i];
                break;
            }
        }

        if (canchaEncontrada == null) {
            return false;
        }

        if (!canchaEncontrada.getEstado().equalsIgnoreCase("DISPONIBLE")) {
            return false;
        }

        LocalTime apertura = LocalTime.parse(canchaEncontrada.getHoraApertura());
        LocalTime cierre = LocalTime.parse(canchaEncontrada.getHoraCierre());
        if (iniNuevo.isBefore(apertura) || finNuevo.isAfter(cierre) || !finNuevo.isAfter(iniNuevo)) {
            return false;
        }

        Reserva[] reservas = archReserva.leerReservas();
        for (int i = 0; i < reservas.length; i++) {
            Reserva r = reservas[i];
            if (r.getIdCancha().equalsIgnoreCase(idCancha) && r.getFecha().equals(fechaStr)) {
                if (!r.getEstado().equalsIgnoreCase("CANCELADA")) {
                    LocalTime iniExist = LocalTime.parse(r.getHoraInicio());
                    LocalTime finExist = LocalTime.parse(r.getHoraFin());
                    if (hayConflicto(iniNuevo, finNuevo, iniExist, finExist)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public double calcularPrecio(String fechaStr, LocalTime ini, int horas, ServicioAdicional[] servicios) {
        LocalDate fecha = LocalDate.parse(fechaStr);
        DayOfWeek dia = fecha.getDayOfWeek();

        double costoPorHora;
        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
            costoPorHora = 250.0;
        } else {
            LocalTime corteTarde = LocalTime.of(17, 0);
            if (ini.isBefore(corteTarde)) {
                costoPorHora = 150.0;
            } else {
                costoPorHora = 200.0;
            }
        }

        double subtotalCancha = costoPorHora * horas;
        double subtotalServicios = 0.0;

        if (servicios != null) {
            for (int i = 0; i < servicios.length; i++) {
                subtotalServicios += servicios[i].getPrecio();
            }
        }

        return subtotalCancha + subtotalServicios;
    }

    public Reserva buscarReserva(String codigo) {
        Reserva[] reservas = archReserva.leerReservas();
        for (int i = 0; i < reservas.length; i++) {
            if (reservas[i].getCodigo().equalsIgnoreCase(codigo)) {
                return reservas[i];
            }
        }
        return null;
    }

    public String generarSiguienteCodigoPago() {
        Pago[] pagos = archPago.leerPagos();
        int max = 0;
        for (int i = 0; i < pagos.length; i++) {
            String cod = pagos[i].getCodigo();
            if (cod.startsWith("PAG")) {
                try {
                    int num = Integer.parseInt(cod.substring(3));
                    if (num > max) {
                        max = num;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        int sig = max + 1;
        return (sig < 10) ? "PAG0" + sig : "PAG" + sig;
    }

    public String generarSiguienteCodigoReserva() {
        Reserva[] reservas = archReserva.leerReservas();
        int max = 0;
        for (int i = 0; i < reservas.length; i++) {
            String cod = reservas[i].getCodigo();
            if (cod.startsWith("RES")) {
                try {
                    int num = Integer.parseInt(cod.substring(3));
                    if (num > max) {
                        max = num;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        int sig = max + 1;
        return (sig < 10) ? "RES0" + sig : "RES" + sig;
    }

    public boolean crearReservaConAnticipo(Reserva r, double anticipo, String metodoPago) {
        LocalTime ini = LocalTime.parse(r.getHoraInicio());
        LocalTime fin = LocalTime.parse(r.getHoraFin());

        if (!estaDisponible(r.getIdCancha(), r.getFecha(), ini, fin)) {
            return false;
        }

        if (anticipo < 0 || anticipo > r.getTotal()) {
            return false;
        }

        r.setTotalPagado(anticipo);
        r.actualizarSaldo();

        if (r.getSaldo() == 0) {
            r.setEstado("CONFIRMADA");
        } else {
            r.setEstado("PENDIENTE");
        }

        archReserva.guardarReserva(r);

        if (anticipo > 0) {
            String codPago = generarSiguienteCodigoPago();
            String ahora = LocalDate.now().toString() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            Pago p = new Pago(codPago, r.getCodigo(), ahora, anticipo, metodoPago, "Anticipo de reserva");
            archPago.guardarPago(p);
        }

        return true;
    }

    public boolean registrarAbono(String codigoReserva, double monto, String metodoPago, String observacion) {
        if (monto <= 0) {
            return false;
        }

        Reserva r = buscarReserva(codigoReserva);
        if (r == null) {
            return false;
        }

        if (r.getEstado().equalsIgnoreCase("CANCELADA") || r.getEstado().equalsIgnoreCase("FINALIZADA")) {
            return false;
        }

        if (monto > r.getSaldo()) {
            return false;
        }

        double nuevoPagado = r.getTotalPagado() + monto;
        r.setTotalPagado(nuevoPagado);
        r.actualizarSaldo();

        if (r.getSaldo() == 0) {
            r.setEstado("CONFIRMADA");
        }

        boolean actualizada = archReserva.actualizarReserva(r);
        if (!actualizada) {
            return false;
        }

        String codPago = generarSiguienteCodigoPago();
        String ahora = LocalDate.now().toString() + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        Pago nuevoPago = new Pago(codPago, r.getCodigo(), ahora, monto, metodoPago, observacion);
        archPago.guardarPago(nuevoPago);

        return true;
    }

    public boolean cancelarReserva(String codigoReserva, String motivo, String operador) {
        Reserva r = buscarReserva(codigoReserva);
        if (r == null) {
            return false;
        }

        if (r.getEstado().equalsIgnoreCase("FINALIZADA") || r.getEstado().equalsIgnoreCase("CANCELADA")) {
            return false;
        }

        r.setEstado("CANCELADA");
        r.setMotivoCancelacion(motivo);
        r.setOperadorResponsable(operador);

        return archReserva.actualizarReserva(r);
    }
}
