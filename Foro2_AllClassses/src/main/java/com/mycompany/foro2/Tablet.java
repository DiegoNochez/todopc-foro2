package com.mycompany.foro2;

import javax.swing.JOptionPane;
import java.util.List;

public class Tablet extends Equipo {

     private String tipoPantalla;             
    private double tamanoDiagonalPulgadas;   
    private int memoriaNandGB;               
    private String sistemaOperativo;         

    public Tablet(String fabricante, String modelo, String microprocesador,
                  String tipoPantalla, double tamanoDiagonalPulgadas,
                  int memoriaNandGB, String sistemaOperativo) {
        super(fabricante, modelo, microprocesador);
        this.tipoPantalla = tipoPantalla;
        this.tamanoDiagonalPulgadas = tamanoDiagonalPulgadas;
        this.memoriaNandGB = memoriaNandGB;
        this.sistemaOperativo = sistemaOperativo;
    }

    @Override
    public String mostrarInformacion() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.mostrarInformacion()).append('\n');
        sb.append("Tipo de pantalla: ").append(tipoPantalla).append('\n');
        sb.append(String.format("Tamaño diagonal: %.1f pulgadas%n", tamanoDiagonalPulgadas));
        sb.append("Memoria NAND: ").append(memoriaNandGB).append(" GB").append('\n');
        sb.append("Sistema Operativo: ").append(sistemaOperativo);
        return sb.toString();
    }

    // ===== Métodos estáticos para usar desde el menú =====
    public static void registrarEnLista(List<Equipo> inventario) {
        Tablet t = registrarConDialogos();
        if (t != null) inventario.add(t);
    }

    public static void verTablets(List<Equipo> inventario) {
        StringBuilder sb = new StringBuilder();
        for (Equipo e : inventario) {
            if (e instanceof Tablet) {
                sb.append(e.mostrarInformacion()).append("\n\n");
            }
        }
        if (sb.length() == 0) sb.append("No hay tablets registradas.");
        JOptionPane.showMessageDialog(null, sb.toString(),
                "Tablets registradas", JOptionPane.INFORMATION_MESSAGE);
    }

    // ===== Registro con validaciones simples =====
    private static Tablet registrarConDialogos() {
        try {
            String fab = promptNoVacio("Fabricante:");
            String mod = promptNoVacio("Modelo:");
            String mic = promptNoVacio("Microprocesador:");

            String tipo = promptNoVacio("Tipo de pantalla (Capacitiva/Resistiva):");
            double pulgadas = promptDoublePositivo("Tamaño diagonal en pulgadas (ej.: 10, 10.1, 12.9):");
            int nandGB = promptCapacidadGB("Memoria NAND (ej.: 64 GB, 128GB, 1 TB, 0.5 TB):");
            String so = promptNoVacio("Sistema Operativo (Android, iPadOS, Windows):");

            Tablet t = new Tablet(fab, mod, mic, tipo, pulgadas, nandGB, so);
            JOptionPane.showMessageDialog(null, "Tablet registrada:\n\n" + t.mostrarInformacion(),
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            return t;
        } catch (Cancelled e) {
            JOptionPane.showMessageDialog(null, "Registro cancelado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    // ===== Helpers de validación =====
    private static class Cancelled extends RuntimeException { private static final long serialVersionUID = 1L; }

    private static String promptNoVacio(String msg) {
        while (true) {
            String in = JOptionPane.showInputDialog(null, msg, "Registro Tablet", JOptionPane.QUESTION_MESSAGE);
            if (in == null) throw new Cancelled();
            in = in.trim();
            if (!in.isEmpty()) return in;
            JOptionPane.showMessageDialog(null, "Este campo no puede quedar vacío.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static double promptDoublePositivo(String msg) {
        while (true) {
            String in = promptNoVacio(msg).toLowerCase()
                    .replace("\"", "").replace("pulgadas", "").replace("pulgada", "").trim();
            try {
                double v = Double.parseDouble(in);
                if (v > 0) return v;
            } catch (NumberFormatException ignore) {}
            JOptionPane.showMessageDialog(null, "Ingrese un número mayor que 0. Ej.: 10, 10.1, 12.9.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Acepta: 64, 64 GB, 128gb, 1 TB, 0.5 TB -> convierte todo a GB (1 TB = 1024 GB)
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
                    gb = (int) Math.round(Double.parseDouble(in));
                }
                if (gb > 0) return gb;
            } catch (NumberFormatException ignore) {}
            JOptionPane.showMessageDialog(null, "Ejemplos: 64, 64 GB, 128GB, 1 TB, 0.5 TB.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }
}
