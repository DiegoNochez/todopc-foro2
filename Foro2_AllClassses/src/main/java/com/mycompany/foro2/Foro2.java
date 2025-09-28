package com.mycompany.foro2;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class Foro2 {

    // Inventario global
    private static final ArrayList<Equipo> equipos = new ArrayList<>();

    public static void main(String[] args) {
        mostrarMenuPrincipal();
    }

    private static void mostrarMenuPrincipal() {
        while (true) {
            String opcion = JOptionPane.showInputDialog(
                "MENÚ PRINCIPAL - TODOPC\n\n" +
                "1. Registrar equipo\n" +
                "2. Ver equipos\n" +
                "3. Salir\n\n" +
                "Seleccione una opción:"
            );

            if (opcion == null || opcion.equals("3")) {
                JOptionPane.showMessageDialog(null, "¡Gracias por usar TODOPC!");
                break;
            }

            switch (opcion) {
                case "1":
                    mostrarMenuRegistro();
                    break;
                case "2":
                    mostrarMenuVer();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Intente nuevamente.");
            }
        }
    }

    // -------------------- Registrar --------------------
    private static void mostrarMenuRegistro() {
        while (true) {
            String op = JOptionPane.showInputDialog(
                "REGISTRAR\n\n" +
                "1. Desktops\n" +
                "2. Laptops\n" +
                "3. Tablets\n" +
                "4. Volver\n\n" +
                "Seleccione una opción:"
            );
            if (op == null || op.equals("4")) break;

            switch (op) {
                case "1":
                    try {
                        Desktop.registrarEnLista(equipos);
                    } catch (RuntimeException ex) {
                        // usuario canceló en algún diálogo -> no hacemos nada
                    }
                    break;
                case "2":
                    //JOptionPane.showMessageDialog(null,
                     //  "Disponible cuando la clase Laptop esté lista.");
               
                    Laptop.registrarEnLista(equipos);
                    break;
                case "3":
                    try {
                        Tablet.registrarEnLista(equipos);
                    } catch (RuntimeException ex) {
                        // usuario canceló en algún diálogo -> no hacemos nada
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        }
    }

    // -------------------- Ver --------------------
    private static void mostrarMenuVer() {
        while (true) {
            String op = JOptionPane.showInputDialog(
                "VER EQUIPOS\n\n" +
                "1. Desktops\n" +
                "2. Laptops\n" +
                "3. Tablets\n" +
                "4. Todos\n" +
                "5. Volver\n\n" +
                "Seleccione una opción:"
            );
            if (op == null || op.equals("5")) break;

            switch (op) {
                case "1":
                    Desktop.verDesktops(equipos);
                    break;
                case "2":
                    //JOptionPane.showMessageDialog(null,
                    //    "Disponible cuando la clase Laptop esté lista.");
                    
                    Laptop.verLaptops(equipos);
                    break;
                case "3":
                    Tablet.verTablets(equipos);
                    break;
                case "4":
                    verTodos();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        }
    }

    // Ver todo el inventario (mezclado)
    private static void verTodos() {
        if (equipos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay equipos registrados.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Equipo e : equipos) {
            sb.append(e.getClass().getSimpleName()).append('\n');
            sb.append(e.mostrarInformacion()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(),
                "Todos los equipos", JOptionPane.INFORMATION_MESSAGE);
    }
}
