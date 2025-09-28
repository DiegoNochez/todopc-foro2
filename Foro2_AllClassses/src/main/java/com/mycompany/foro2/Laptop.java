/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.foro2;

import java.util.List;

import javax.swing.JOptionPane;

public class Laptop extends Equipo {

    //  Atributos específicos 
    private String memoria;                   // ej.: "16 GB RAM"
    private int almacenamientoGB;             // convierte TB -> GB
    private double tamanoPantallaPulgadas;    // ej.: 15.6
    private String sistemaOperativo;          // ej.: Windows / macOS / Linux
    private double autonomiaHoras;            // ej.: 7.5
    private double pesoKg;                    // ej.: 1.8

    //  Constructor 
    public Laptop(String fabricante, String modelo, String microprocesador,
                  String memoria, int almacenamientoGB,
                  double tamanoPantallaPulgadas, String sistemaOperativo,
                  double autonomiaHoras, double pesoKg) {
        super(fabricante, modelo, microprocesador);
        this.memoria = memoria;
        this.almacenamientoGB = almacenamientoGB;
        this.tamanoPantallaPulgadas = tamanoPantallaPulgadas;
        this.sistemaOperativo = sistemaOperativo;
        this.autonomiaHoras = autonomiaHoras;
        this.pesoKg = pesoKg;
    }

    //  Mostrar información 
    @Override
    public String mostrarInformacion() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.mostrarInformacion()).append('\n');
        sb.append("Memoria: ").append(memoria).append('\n');
        sb.append("Almacenamiento: ").append(almacenamientoGB).append(" GB").append('\n');
        sb.append(String.format("Pantalla: %.1f pulgadas%n", tamanoPantallaPulgadas));
        sb.append("Sistema Operativo: ").append(sistemaOperativo).append('\n');
        sb.append(String.format("Autonomía: %.1f horas%n", autonomiaHoras));
        sb.append(String.format("Peso: %.2f kg", pesoKg));
        return sb.toString();
    }

    //  MENÚ GENERAL 

    public static Laptop registrarConDialogos() {
        try {
            String fab = promptNoVacio("Fabricante:");
            String mod = promptNoVacio("Modelo:");
            String mic = promptNoVacio("Microprocesador:");

            String mem = promptNoVacio("Memoria (ej.: \"16 GB RAM\"):");
            int almGB = promptCapacidadGB("Almacenamiento (ej.: \"512 GB\" o \"1 TB\"):");
            double pantalla = promptDoublePositivoConUnidades(
                    "Tamaño de pantalla en pulgadas (ej.: 15.6):",
                    "\"", "pulgadas", "pulgada"
            );
            String so = promptNoVacio("Sistema Operativo (Windows, macOS, Linux):");
            double horas = promptDoublePositivoConUnidades(
                    "Autonomía de batería en horas (ej.: 7.5):",
                    "horas", "hora", "hrs", "hr", "h"
            );
            double peso = promptDoublePositivoConUnidades(
                    "Peso en kg (ej.: 1.8):",
                    "kg", "kgs", "kilogramos", "kilogramo", "kg."
            );

            Laptop l = new Laptop(fab, mod, mic, mem, almGB, pantalla, so, horas, peso);
            JOptionPane.showMessageDialog(null, "Laptop registrada:\n\n" + l.mostrarInformacion(),
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            return l;

        } catch (Cancelled e) {
            JOptionPane.showMessageDialog(null, "Registro cancelado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    /** Crea una Laptop y la agrega a la lista global (si no cancelan). */
    public static void registrarEnLista(List<Equipo> inventario) {
        Laptop l = registrarConDialogos();
        if (l != null) inventario.add(l);
    }

    /** Muestra solo las Laptops del inventario. */
    public static void verLaptops(List<Equipo> inventario) {
        StringBuilder sb = new StringBuilder();
        for (Equipo e : inventario) {
            if (e instanceof Laptop) {
                sb.append(e.mostrarInformacion()).append("\n\n");
            }
        }
        if (sb.length() == 0) sb.append("No hay laptops registradas.");
        JOptionPane.showMessageDialog(null, sb.toString(),
                "Laptops registradas", JOptionPane.INFORMATION_MESSAGE);
    }

    // HELPERS
    private static class Cancelled extends RuntimeException { private static final long serialVersionUID = 1L; }

    private static String promptNoVacio(String msg) {
        while (true) {
            String in = JOptionPane.showInputDialog(null, msg, "Registro Laptop", JOptionPane.QUESTION_MESSAGE);
            if (in == null) throw new Cancelled();
            in = in.trim();
            if (!in.isEmpty()) return in;
            JOptionPane.showMessageDialog(null, "Este campo no puede quedar vacío.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

   
    private static double promptDoublePositivoConUnidades(String msg, String... unidadesAEliminar) {
        while (true) {
            String in = promptNoVacio(msg).toLowerCase().trim();
            // Elimina posibles unidades/textos
            for (String u : unidadesAEliminar) {
                in = in.replace(u.toLowerCase(), "");
            }
            in = in.trim();
            try {
                double v = Double.parseDouble(in);
                if (v > 0) return v;
            } catch (NumberFormatException ignore) {}
            JOptionPane.showMessageDialog(null, "Ingresa un número mayor que 0 (ej.: 15.6).",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Convierte a GB: admite "512", "512gb", "512 GB", "1 tb", "1.5TB".
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
            JOptionPane.showMessageDialog(null, "Ejemplos válidos: 512, 512 GB, 1 TB, 1.5 TB.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }
}