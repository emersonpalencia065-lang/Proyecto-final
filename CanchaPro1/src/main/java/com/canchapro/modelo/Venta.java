package com.canchapro.modelo;

public class Venta {
    private String codigo;
    private String fecha;
    private String idReserva;
    private double total;
    private String metodoPago;

    public Venta(String codigo, String fecha, String idReserva, double total, String metodoPago) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.idReserva = (idReserva == null) ? "" : idReserva;
        this.total = total;
        this.metodoPago = metodoPago;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    @Override
    public String toString() {
        return codigo + " | " + fecha + " - Total: Q" + total + " (" + metodoPago + ")";
    }
}
