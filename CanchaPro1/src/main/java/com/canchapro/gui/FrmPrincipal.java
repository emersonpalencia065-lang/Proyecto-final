package com.canchapro.gui;

import com.canchapro.archivos.ArchivosCancha;
import com.canchapro.archivos.ArchivosCliente;
import com.canchapro.archivos.ArchivosPago;
import com.canchapro.archivos.ArchivosReserva;
import com.canchapro.archivos.ArchivosServicio;
import com.canchapro.logica.LogicaReservas;
import com.canchapro.modelo.Cancha;
import com.canchapro.modelo.Cliente;
import com.canchapro.modelo.Pago;
import com.canchapro.modelo.Reserva;
import com.canchapro.modelo.ServicioAdicional;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FrmPrincipal extends JFrame {

    private ArchivosCliente archCliente;
    private ArchivosCancha archCancha;
    private ArchivosServicio archServicio;
    private ArchivosReserva archReserva;
    private ArchivosPago archPago;
    private LogicaReservas logica;

    private JTabbedPane pestanias;

    private DefaultTableModel modeloReservas;
    private DefaultTableModel modeloClientes;
    private DefaultTableModel modeloCanchas;
    private DefaultTableModel modeloPagos;

    private JLabel lblTotalReservasHoy;
    private JLabel lblReservasPendientes;
    private JLabel lblIngresosTotales;
    private JLabel lblCanchaPopular;

    private JComboBox<String> cbClientes;
    private JComboBox<String> cbCanchas;
    private JTextField txtFechaReserva;
    private JComboBox<String> cbHoraInicio;
    private JComboBox<String> cbDuracion;
    private JCheckBox chkBalon;
    private JCheckBox chkChalecos;
    private JCheckBox chkArbitro;
    private JCheckBox chkLuz;
    private JLabel lblPrecioCalculado;
    private JTextField txtAnticipo;
    private JComboBox<String> cbMetodoPago;

    public FrmPrincipal() {
        this.archCliente = new ArchivosCliente();
        this.archCancha = new ArchivosCancha();
        this.archServicio = new ArchivosServicio();
        this.archReserva = new ArchivosReserva();
        this.archPago = new ArchivosPago();
        this.logica = new LogicaReservas();

        setTitle("CanchaPro - Sistema de Gestion y Reservas");
        setSize(950, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarInterfaz();
        actualizarTodasLasTablas();
    }

    private void inicializarInterfaz() {
        pestanias = new JTabbedPane();

        pestanias.addTab("Dashboard", crearPanelDashboard());
        pestanias.addTab("Nueva Reserva", crearPanelNuevaReserva());
        pestanias.addTab("Reservas", crearPanelReservas());
        pestanias.addTab("Clientes", crearPanelClientes());
        pestanias.addTab("Canchas", crearPanelCanchas());
        pestanias.addTab("Pagos y Abonos", crearPanelPagos());

        add(pestanias);
    }

    private JPanel crearPanelDashboard() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Resumen Operativo - Complejo La 10", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel panelTarjetas = new JPanel(new GridLayout(2, 2, 15, 15));

        lblTotalReservasHoy = new JLabel("Reservas hoy: 0", JLabel.CENTER);
        lblTotalReservasHoy.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalReservasHoy.setOpaque(true);
        lblTotalReservasHoy.setBackground(new Color(220, 240, 255));
        lblTotalReservasHoy.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        lblReservasPendientes = new JLabel("Reservas pendientes de pago: 0", JLabel.CENTER);
        lblReservasPendientes.setFont(new Font("Arial", Font.BOLD, 16));
        lblReservasPendientes.setOpaque(true);
        lblReservasPendientes.setBackground(new Color(255, 243, 205));
        lblReservasPendientes.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        lblIngresosTotales = new JLabel("Ingresos totales: Q0.00", JLabel.CENTER);
        lblIngresosTotales.setFont(new Font("Arial", Font.BOLD, 16));
        lblIngresosTotales.setOpaque(true);
        lblIngresosTotales.setBackground(new Color(212, 237, 218));
        lblIngresosTotales.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        lblCanchaPopular = new JLabel("Cancha mas solicitada: -", JLabel.CENTER);
        lblCanchaPopular.setFont(new Font("Arial", Font.BOLD, 16));
        lblCanchaPopular.setOpaque(true);
        lblCanchaPopular.setBackground(new Color(230, 230, 250));
        lblCanchaPopular.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        panelTarjetas.add(lblTotalReservasHoy);
        panelTarjetas.add(lblReservasPendientes);
        panelTarjetas.add(lblIngresosTotales);
        panelTarjetas.add(lblCanchaPopular);

        panel.add(panelTarjetas, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar Indicadores");
        btnActualizar.addActionListener(e -> actualizarDashboard());
        panel.add(btnActualizar, BorderLayout.SOUTH);

        return panel;
    }

    private void actualizarDashboard() {
        String hoy = LocalDate.now().toString();
        int countHoy = 0;
        int countPendientes = 0;
        double ingresos = 0.0;

        Reserva[] reservas = archReserva.leerReservas();
        Cancha[] canchas = archCancha.leerCanchas();
        int[] conteoCanchas = new int[canchas.length];

        for (int i = 0; i < reservas.length; i++) {
            Reserva r = reservas[i];
            if (r.getFecha().equals(hoy)) {
                countHoy++;
            }
            if (r.getEstado().equalsIgnoreCase("PENDIENTE") || r.getSaldo() > 0) {
                countPendientes++;
            }
            if (!r.getEstado().equalsIgnoreCase("CANCELADA")) {
                ingresos += r.getTotalPagado();

                for (int j = 0; j < canchas.length; j++) {
                    if (canchas[j].getCodigo().equalsIgnoreCase(r.getIdCancha())) {
                        conteoCanchas[j]++;
                        break;
                    }
                }
            }
        }

        String canchaTop = "-";
        int max = 0;
        for (int j = 0; j < canchas.length; j++) {
            if (conteoCanchas[j] > max) {
                max = conteoCanchas[j];
                canchaTop = canchas[j].getCodigo() + " - " + canchas[j].getNombre();
            }
        }

        lblTotalReservasHoy.setText("Reservas hoy (" + hoy + "): " + countHoy);
        lblReservasPendientes.setText("Reservas con saldo pendiente: " + countPendientes);
        lblIngresosTotales.setText(String.format("Ingresos recaudados: Q%.2f", ingresos));
        lblCanchaPopular.setText("Cancha mas solicitada: " + canchaTop + " (" + max + " reservas)");
    }

    private JPanel crearPanelNuevaReserva() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel form = new JPanel(new GridLayout(10, 2, 8, 8));

        cbClientes = new JComboBox<>();
        actualizarComboClientes();

        cbCanchas = new JComboBox<>();
        actualizarComboCanchas();

        txtFechaReserva = new JTextField(LocalDate.now().toString());

        String[] horas = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00"};
        cbHoraInicio = new JComboBox<>(horas);

        String[] duraciones = {"1 hora", "2 horas", "3 horas"};
        cbDuracion = new JComboBox<>(duraciones);

        JPanel pnlServicios = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkBalon = new JCheckBox("Balon (+Q20)");
        chkChalecos = new JCheckBox("Chalecos (+Q15)");
        chkArbitro = new JCheckBox("Arbitro (+Q100)");
        chkLuz = new JCheckBox("Luz (+Q30)");
        pnlServicios.add(chkBalon);
        pnlServicios.add(chkChalecos);
        pnlServicios.add(chkArbitro);
        pnlServicios.add(chkLuz);

        lblPrecioCalculado = new JLabel("Total Estimado: Q0.00");
        lblPrecioCalculado.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrecioCalculado.setForeground(new Color(0, 100, 0));

        JButton btnCalcular = new JButton("Calcular Precio");
        btnCalcular.addActionListener(e -> calcularPrecioFormulario());

        txtAnticipo = new JTextField("0.0");
        String[] metodos = {"Efectivo", "Transferencia", "Tarjeta"};
        cbMetodoPago = new JComboBox<>(metodos);

        form.add(new JLabel("1. Cliente:"));
        form.add(cbClientes);

        form.add(new JLabel("2. Cancha:"));
        form.add(cbCanchas);

        form.add(new JLabel("3. Fecha (YYYY-MM-DD):"));
        form.add(txtFechaReserva);

        form.add(new JLabel("4. Hora Inicio:"));
        form.add(cbHoraInicio);

        form.add(new JLabel("5. Duracion:"));
        form.add(cbDuracion);

        form.add(new JLabel("6. Servicios Adicionales:"));
        form.add(pnlServicios);

        form.add(btnCalcular);
        form.add(lblPrecioCalculado);

        form.add(new JLabel("7. Anticipo o Primer Pago (Q):"));
        form.add(txtAnticipo);

        form.add(new JLabel("8. Metodo de Pago:"));
        form.add(cbMetodoPago);

        JButton btnGuardar = new JButton("Confirmar y Guardar Reserva");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.addActionListener(e -> guardarNuevaReserva());

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnGuardar, BorderLayout.SOUTH);

        return panel;
    }

    private ServicioAdicional[] obtenerServiciosSeleccionados() {
        int cuenta = 0;
        if (chkBalon.isSelected()) cuenta++;
        if (chkChalecos.isSelected()) cuenta++;
        if (chkArbitro.isSelected()) cuenta++;
        if (chkLuz.isSelected()) cuenta++;

        ServicioAdicional[] seleccionados = new ServicioAdicional[cuenta];
        int pos = 0;
        if (chkBalon.isSelected()) {
            seleccionados[pos++] = new ServicioAdicional("S01", "Balon profesional", 20.0);
        }
        if (chkChalecos.isSelected()) {
            seleccionados[pos++] = new ServicioAdicional("S02", "Chalecos petos", 15.0);
        }
        if (chkArbitro.isSelected()) {
            seleccionados[pos++] = new ServicioAdicional("S03", "Arbitro", 100.0);
        }
        if (chkLuz.isSelected()) {
            seleccionados[pos++] = new ServicioAdicional("S04", "Iluminacion nocturna", 30.0);
        }
        return seleccionados;
    }

    private double calcularPrecioFormulario() {
        try {
            String fecha = txtFechaReserva.getText().trim();
            String horaIniStr = (String) cbHoraInicio.getSelectedItem();
            int horas = cbDuracion.getSelectedIndex() + 1;

            ServicioAdicional[] extras = obtenerServiciosSeleccionados();
            double total = logica.calcularPrecio(fecha, LocalTime.parse(horaIniStr), horas, extras);
            lblPrecioCalculado.setText(String.format("Total Estimado: Q%.2f", total));
            return total;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use YYYY-MM-DD.");
            return 0.0;
        }
    }

    private void guardarNuevaReserva() {
        if (cbClientes.getSelectedItem() == null || cbCanchas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente y una cancha.");
            return;
        }

        String clienteItem = (String) cbClientes.getSelectedItem();
        String idCliente = clienteItem.split(" - ")[0].trim();

        String canchaItem = (String) cbCanchas.getSelectedItem();
        String idCancha = canchaItem.split(" - ")[0].trim();

        String fecha = txtFechaReserva.getText().trim();
        String horaIniStr = (String) cbHoraInicio.getSelectedItem();
        int horas = cbDuracion.getSelectedIndex() + 1;

        LocalTime horaIni = LocalTime.parse(horaIniStr);
        LocalTime horaFin = horaIni.plusHours(horas);

        if (!logica.estaDisponible(idCancha, fecha, horaIni, horaFin)) {
            JOptionPane.showMessageDialog(this, "La cancha " + idCancha + " NO esta disponible en ese horario o fecha (Conflicto de horario o mantenimiento).", "Conflicto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double total = calcularPrecioFormulario();
        double anticipo = 0.0;
        try {
            anticipo = Double.parseDouble(txtAnticipo.getText().trim());
            if (anticipo < 0) {
                JOptionPane.showMessageDialog(this, "El anticipo no puede ser negativo.");
                return;
            }
            if (anticipo > total) {
                JOptionPane.showMessageDialog(this, "El anticipo no puede ser mayor al total.");
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor numerico valido para el anticipo.");
            return;
        }

        String metodo = (String) cbMetodoPago.getSelectedItem();
        String codReserva = logica.generarSiguienteCodigoReserva();

        StringBuilder sbServ = new StringBuilder();
        if (chkBalon.isSelected()) sbServ.append("S01;");
        if (chkChalecos.isSelected()) sbServ.append("S02;");
        if (chkArbitro.isSelected()) sbServ.append("S03;");
        if (chkLuz.isSelected()) sbServ.append("S04;");

        Reserva r = new Reserva(
                codReserva,
                idCliente,
                idCancha,
                fecha,
                horaIniStr,
                horaFin.format(DateTimeFormatter.ofPattern("HH:mm")),
                total,
                0.0,
                total,
                "PENDIENTE",
                "Ninguno",
                "Admin",
                sbServ.toString()
        );

        boolean ok = logica.crearReservaConAnticipo(r, anticipo, metodo);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Reserva " + codReserva + " registrada con exito.");
            actualizarTodasLasTablas();
            actualizarDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar la reserva.");
        }
    }

    private JPanel crearPanelReservas() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"Codigo", "Cliente", "Cancha", "Fecha", "Inicio", "Fin", "Total", "Pagado", "Saldo", "Estado"};
        modeloReservas = new DefaultTableModel(cols, 0);
        JTable tabla = new JTable(modeloReservas);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar Reserva Seleccionada");
        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.BLACK);

        btnCancelar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String cod = (String) modeloReservas.getValueAt(fila, 0);
                String motivo = JOptionPane.showInputDialog(this, "Ingrese el motivo de cancelacion:");
                if (motivo != null && !motivo.trim().isEmpty()) {
                    boolean ok = logica.cancelarReserva(cod, motivo, "Operador1");
                    if (ok) {
                        JOptionPane.showMessageDialog(this, "Reserva cancelada correctamente.");
                        actualizarTablaReservas();
                        actualizarDashboard();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo cancelar la reserva seleccionada.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una reserva de la tabla.");
            }
        });

        pnlAcciones.add(btnCancelar);
        panel.add(pnlAcciones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"Codigo", "Nombre", "Telefono", "Correo", "Fecha Registro"};
        modeloClientes = new DefaultTableModel(cols, 0);
        JTable tabla = new JTable(modeloClientes);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel pnlNuevo = new JPanel(new GridLayout(2, 5, 5, 5));
        JTextField txtNombre = new JTextField();
        JTextField txtTel = new JTextField();
        JTextField txtCorreo = new JTextField();
        JButton btnAgregar = new JButton("Registrar Cliente");

        pnlNuevo.add(new JLabel("Nombre:"));
        pnlNuevo.add(new JLabel("Telefono:"));
        pnlNuevo.add(new JLabel("Correo:"));
        pnlNuevo.add(new JLabel(""));
        pnlNuevo.add(new JLabel(""));

        pnlNuevo.add(txtNombre);
        pnlNuevo.add(txtTel);
        pnlNuevo.add(txtCorreo);
        pnlNuevo.add(btnAgregar);

        btnAgregar.addActionListener(e -> {
            String nom = txtNombre.getText().trim();
            String tel = txtTel.getText().trim();
            String cor = txtCorreo.getText().trim();

            if (nom.isEmpty() || tel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y telefono son obligatorios.");
                return;
            }

            Cliente[] clientes = archCliente.leerClientes();
            for (int i = 0; i < clientes.length; i++) {
                if (clientes[i].getTelefono().equals(tel)) {
                    JOptionPane.showMessageDialog(this, "Ya existe un cliente con ese telefono.");
                    return;
                }
            }

            String cod = "CLI0" + (clientes.length + 1);
            Cliente nuevo = new Cliente(cod, nom, tel, cor, LocalDate.now().toString());
            archCliente.guardarCliente(nuevo);

            JOptionPane.showMessageDialog(this, "Cliente " + nom + " registrado.");
            txtNombre.setText("");
            txtTel.setText("");
            txtCorreo.setText("");
            actualizarTablaClientes();
            actualizarComboClientes();
        });

        panel.add(pnlNuevo, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelCanchas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"Codigo", "Nombre", "Tipo", "Capacidad", "Estado", "Horario", "Precio Base"};
        modeloCanchas = new DefaultTableModel(cols, 0);
        JTable tabla = new JTable(modeloCanchas);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel pnlCambio = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JComboBox<String> cbEstado = new JComboBox<>(new String[]{"DISPONIBLE", "MANTENIMIENTO", "INACTIVA"});
        JButton btnCambiar = new JButton("Cambiar Estado");

        btnCambiar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String cod = (String) modeloCanchas.getValueAt(fila, 0);
                String nuevoEst = (String) cbEstado.getSelectedItem();
                archCancha.actualizarEstado(cod, nuevoEst);
                JOptionPane.showMessageDialog(this, "Estado de " + cod + " actualizado a " + nuevoEst);
                actualizarTablaCanchas();
                actualizarComboCanchas();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una cancha de la tabla.");
            }
        });

        pnlCambio.add(new JLabel("Nuevo Estado:"));
        pnlCambio.add(cbEstado);
        pnlCambio.add(btnCambiar);

        panel.add(pnlCambio, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelPagos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"Cod Pago", "Reserva", "Fecha / Hora", "Monto", "Metodo", "Observacion"};
        modeloPagos = new DefaultTableModel(cols, 0);
        JTable tabla = new JTable(modeloPagos);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel pnlAbono = new JPanel(new GridLayout(2, 4, 5, 5));
        JTextField txtIdRes = new JTextField();
        JTextField txtMonto = new JTextField();
        JComboBox<String> cbMetodo = new JComboBox<>(new String[]{"Efectivo", "Transferencia", "Tarjeta"});
        JButton btnPagar = new JButton("Registrar Abono");

        pnlAbono.add(new JLabel("Codigo Reserva (Ej: RES01):"));
        pnlAbono.add(new JLabel("Monto a abonar (Q):"));
        pnlAbono.add(new JLabel("Metodo de Pago:"));
        pnlAbono.add(new JLabel(""));

        pnlAbono.add(txtIdRes);
        pnlAbono.add(txtMonto);
        pnlAbono.add(cbMetodo);
        pnlAbono.add(btnPagar);

        btnPagar.addActionListener(e -> {
            String idRes = txtIdRes.getText().trim();
            try {
                double m = Double.parseDouble(txtMonto.getText().trim());
                String met = (String) cbMetodo.getSelectedItem();
                boolean ok = logica.registrarAbono(idRes, m, met, "Abono a saldo");
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Abono de Q" + m + " registrado exitosamente.");
                    txtIdRes.setText("");
                    txtMonto.setText("");
                    actualizarTablaPagos();
                    actualizarTablaReservas();
                    actualizarDashboard();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Verifique el codigo de reserva, que el monto sea positivo y no supere el saldo pendiente.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un monto numerico valido.");
            }
        });

        panel.add(pnlAbono, BorderLayout.SOUTH);
        return panel;
    }

    private void actualizarComboClientes() {
        cbClientes.removeAllItems();
        Cliente[] clientes = archCliente.leerClientes();
        for (int i = 0; i < clientes.length; i++) {
            cbClientes.addItem(clientes[i].getCodigo() + " - " + clientes[i].getNombre());
        }
    }

    private void actualizarComboCanchas() {
        cbCanchas.removeAllItems();
        Cancha[] canchas = archCancha.leerCanchas();
        for (int i = 0; i < canchas.length; i++) {
            cbCanchas.addItem(canchas[i].getCodigo() + " - " + canchas[i].getNombre() + " (" + canchas[i].getEstado() + ")");
        }
    }

    private void actualizarTablaReservas() {
        if (modeloReservas == null) return;
        modeloReservas.setRowCount(0);
        Reserva[] reservas = archReserva.leerReservas();
        for (int i = 0; i < reservas.length; i++) {
            Reserva r = reservas[i];
            modeloReservas.addRow(new Object[]{
                    r.getCodigo(), r.getIdCliente(), r.getIdCancha(), r.getFecha(),
                    r.getHoraInicio(), r.getHoraFin(), "Q" + r.getTotal(),
                    "Q" + r.getTotalPagado(), "Q" + r.getSaldo(), r.getEstado()
            });
        }
    }

    private void actualizarTablaClientes() {
        if (modeloClientes == null) return;
        modeloClientes.setRowCount(0);
        Cliente[] clientes = archCliente.leerClientes();
        for (int i = 0; i < clientes.length; i++) {
            Cliente c = clientes[i];
            modeloClientes.addRow(new Object[]{
                    c.getCodigo(), c.getNombre(), c.getTelefono(), c.getCorreo(), c.getFechaRegistro()
            });
        }
    }

    private void actualizarTablaCanchas() {
        if (modeloCanchas == null) return;
        modeloCanchas.setRowCount(0);
        Cancha[] canchas = archCancha.leerCanchas();
        for (int i = 0; i < canchas.length; i++) {
            Cancha c = canchas[i];
            modeloCanchas.addRow(new Object[]{
                    c.getCodigo(), c.getNombre(), c.getTipo(), c.getCapacidad(),
                    c.getEstado(), c.getHoraApertura() + " a " + c.getHoraCierre(), "Q" + c.getPrecioBase()
            });
        }
    }

    private void actualizarTablaPagos() {
        if (modeloPagos == null) return;
        modeloPagos.setRowCount(0);
        Pago[] pagos = archPago.leerPagos();
        for (int i = 0; i < pagos.length; i++) {
            Pago p = pagos[i];
            modeloPagos.addRow(new Object[]{
                    p.getCodigo(), p.getIdReserva(), p.getFechaHora(), "Q" + p.getMonto(),
                    p.getMetodoPago(), p.getObservacion()
            });
        }
    }

    public void actualizarTodasLasTablas() {
        actualizarTablaReservas();
        actualizarTablaClientes();
        actualizarTablaCanchas();
        actualizarTablaPagos();
        actualizarDashboard();
    }
}
