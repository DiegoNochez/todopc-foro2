/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.foro2;

//Desktop
import javax.swing.JOptionPane;
import java.util.List;

public class Desktop extends Equipo {

    // ----- Atributos específicos -----
    private String memoria;               // p. ej. "8GB RAM"
    private String tarjetaGrafica;        // p. ej. "GeForce 8400 GS"
    private double tamanoTorrePulgadas;   // en pulgadas (19, 19.5, etc.)
    private int capacidadDiscoGB;         // siempre en GB (convierte TB -> GB)

    // ----- Constructor -----
    public Desktop(String fabricante, String modelo, String microprocesador,
                   String memoria, String tarjetaGrafica,
                   double tamanoTorrePulgadas, int capacidadDiscoGB) {
        super(fabricante, modelo, microprocesador);
        this.memoria = memoria;
        this.tarjetaGrafica = tarjetaGrafica;
        this.tamanoTorrePulgadas = tamanoTorrePulgadas;
        this.capacidadDiscoGB = capacidadDiscoGB;
    }

    // ----- Getters  -----
    public String getMemoria() { return memoria; }
    public String getTarjetaGrafica() { return tarjetaGrafica; }
    public double getTamanoTorrePulgadas() { return tamanoTorrePulgadas; }
    public int getCapacidadDiscoGB() { return capacidadDiscoGB; }

    // ----- Mostrar información (sobrescribe Equipo.mostrarInformacion) -----
    @Override
    public String mostrarInformacion() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.mostrarInformacion()).append('\n');
        sb.append("Memoria: ").append(memoria).append('\n');
        sb.append("Tarjeta gráfica: ").append(tarjetaGrafica).append('\n');
        sb.append(String.format("Tamaño de torre: %.1f pulgadas%n", tamanoTorrePulgadas));
        sb.append("Capacidad de disco duro: ").append(capacidadDiscoGB).append(" GB");
        return sb.toString();
    }

    // =====================================================================
    // ======  MÉTODOS ESTÁTICOS PARA USAR EN EL MENÚ GENERAL  =============
    // =====================================================================

    /** Abre diálogos, valida y devuelve un Desktop (o null si cancelan). */
    public static Desktop registrarConDialogos() {
        try {
            String fab = promptNoVacio("Fabricante:");
            String mod = promptNoVacio("Modelo:");
            String mic = promptNoVacio("Microprocesador:");
            String mem = promptNoVacio("Memoria (ej. \"8GB RAM\"):");
            String gpu = promptNoVacio("Tarjeta gráfica:");
            double torre = promptDoublePositivo("Tamaño de torre en pulgadas (ej. 19):");
            int disco = promptCapacidadGB("Capacidad de disco (ej. \"500 GB\" o \"1 TB\"):");

            Desktop d = new Desktop(fab, mod, mic, mem, gpu, torre, disco);
            JOptionPane.showMessageDialog(null, "Desktop registrado:\n\n" + d.mostrarInformacion(),
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            return d;

        } catch (Cancelled e) {
            JOptionPane.showMessageDialog(null, "Registro cancelado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    /** Crea un Desktop y lo agrega a la lista global (si no cancelan). */
    public static void registrarEnLista(List<Equipo> inventario) {
        Desktop d = registrarConDialogos();
        if (d != null) inventario.add(d);
    }

    /** Muestra solo los Desktop del inventario. */
    public static void verDesktops(List<Equipo> inventario) {
        StringBuilder sb = new StringBuilder();
        for (Equipo e : inventario) {
            if (e instanceof Desktop) {
                sb.append(e.mostrarInformacion()).append("\n\n");
            }
        }
        if (sb.length() == 0) sb.append("No hay desktops registrados.");
        JOptionPane.showMessageDialog(null, sb.toString(),
                "Desktops registrados", JOptionPane.INFORMATION_MESSAGE);
    }

    // =====================================================================
    // ===================== VALIDACIONES / HELPERS =========================
    // =====================================================================

    private static class Cancelled extends RuntimeException { private static final long serialVersionUID = 1L; }

    /** Lee texto no vacío; si Cancelar, lanza Cancelled. */
    private static String promptNoVacio(String msg) {
        while (true) {
            String in = JOptionPane.showInputDialog(null, msg, "Registro Desktop", JOptionPane.QUESTION_MESSAGE);
            if (in == null) throw new Cancelled();
            in = in.trim();
            if (!in.isEmpty()) return in;
            JOptionPane.showMessageDialog(null, "Este campo no puede quedar vacío.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Número > 0; acepta 19, 19.5, 19", 19 pulgadas. */
    private static double promptDoublePositivo(String msg) {
        while (true) {
            String in = promptNoVacio(msg).toLowerCase()
                    .replace("\"", "").replace("pulgadas", "").replace("pulgada", "").trim();
            try {
                double v = Double.parseDouble(in);
                if (v > 0) return v;
            } catch (NumberFormatException ignore) {}
            JOptionPane.showMessageDialog(null, "Ingresa un número mayor que 0 (ej. 19).",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Convierte a GB: admite "500", "500gb", "500 GB", "1 tb", "1.5TB".
     * Sin sufijo => GB.
     */
    private static int promptCapacidadGB(String msg) {
        while (true) {
            String in = promptNoVacio(msg).toLowerCase().replace(" ", "");
            try {
                int gb;
                if (in.endsWith("tb")) {
                    double tb = Double.parseDouble(in.substring(0, in.length() - 2));
                    gb = (int) Math.round(tb * 1024);
                } else if (in.endsWith("gb")) {
                    double val = Double.parseDouble(in.substring(0, in.length() - 2));
                    gb = (int) Math.round(val);
                } else {
                    gb = (int) Math.round(Double.parseDouble(in)); // sin unidad => GB
                }
                if (gb > 0) return gb;
            } catch (NumberFormatException ignore) {}
            JOptionPane.showMessageDialog(null, "Ejemplos válidos: 500, 500 GB, 1 TB, 1.5 TB.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }
}
