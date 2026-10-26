package com.canchapro.modelo;

public class Pago {
    private String codigo;
    private String idReserva;
    private String fechaHora;
    private double monto;
    private String metodoPago;
    private String observacion;

    public Pago(String codigo, String idReserva, String fechaHora, double monto, String metodoPago, String observacion) {
        this.codigo = codigo;
        this.idReserva = idReserva;
        this.fechaHora = fechaHora;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.observacion = observacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public String toString() {
        return codigo + " | Reserva: " + idReserva + " | Monto: Q" + monto + " (" + metodoPago + ") | Fecha: " + fechaHora;
    }
}
