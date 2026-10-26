package com.canchapro.modelo;

public class Reserva {
    private String codigo;
    private String idCliente;
    private String idCancha;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private double total;
    private double totalPagado;
    private double saldo;
    private String estado;
    private String motivoCancelacion;
    private String operadorResponsable;
    private String serviciosCodigos;

    public Reserva(String codigo, String idCliente, String idCancha, String fecha, String horaInicio, String horaFin, double total, double totalPagado, double saldo, String estado, String motivoCancelacion, String operadorResponsable, String serviciosCodigos) {
        this.codigo = codigo;
        this.idCliente = idCliente;
        this.idCancha = idCancha;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.total = total;
        this.totalPagado = totalPagado;
        this.saldo = saldo;
        this.estado = estado;
        this.motivoCancelacion = (motivoCancelacion == null || motivoCancelacion.isEmpty()) ? "Ninguno" : motivoCancelacion;
        this.operadorResponsable = (operadorResponsable == null || operadorResponsable.isEmpty()) ? "Admin" : operadorResponsable;
        this.serviciosCodigos = (serviciosCodigos == null) ? "" : serviciosCodigos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdCancha() {
        return idCancha;
    }

    public void setIdCancha(String idCancha) {
        this.idCancha = idCancha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
        actualizarSaldo();
    }

    public double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
        actualizarSaldo();
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void actualizarSaldo() {
        this.saldo = this.total - this.totalPagado;
        if (this.saldo < 0) {
            this.saldo = 0;
        }
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public String getOperadorResponsable() {
        return operadorResponsable;
    }

    public void setOperadorResponsable(String operadorResponsable) {
        this.operadorResponsable = operadorResponsable;
    }

    public String getServiciosCodigos() {
        return serviciosCodigos;
    }

    public void setServiciosCodigos(String serviciosCodigos) {
        this.serviciosCodigos = serviciosCodigos;
    }

    @Override
    public String toString() {
        return codigo + " | Cliente: " + idCliente + " | Cancha: " + idCancha + " | Fecha: " + fecha + " (" + horaInicio + " a " + horaFin + ") | Total: Q" + total + " | Saldo: Q" + saldo + " | Estado: " + estado;
    }
}
