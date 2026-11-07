package torneos.ui;

import java.util.Scanner;

/** Lectura de datos por teclado con validación (sin usar excepciones). */
public class Consola {

    private static final int MAX_LONGITUD_TEXTO = 30;
    private static final int MAX_DIGITOS = 9;

    private final Scanner scanner;

    public Consola() {
        this.scanner = new Scanner(System.in);
    }

    private String leerLinea() {
        if (!scanner.hasNextLine()) {
            System.out.println("\nSe cerró la entrada de datos. Fin del programa.");
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }

    /** Pide un texto que no esté vacío y no sea demasiado largo. */
    public String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = leerLinea();
            if (texto.isEmpty()) {
                System.out.println("  [!] Este dato no puede quedar en blanco.");
            } else if (texto.length() > MAX_LONGITUD_TEXTO) {
                System.out.println("  [!] Use máximo " + MAX_LONGITUD_TEXTO + " caracteres.");
            } else {
                return texto;
            }
        }
    }

    /** Pide un número entero (solo dígitos). */
    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = leerLinea();
            if (esEnteroValido(texto)) {
                return Integer.parseInt(texto);
            }
            System.out.println("  [!] Ingrese un número entero positivo (solo dígitos).");
        }
    }

    /** Pide un número entero dentro de un rango (incluye los extremos). */
    public int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            System.out.println("  [!] Opción fuera de rango. Ingrese un valor entre "
                    + minimo + " y " + maximo + ".");
        }
    }

    public void esperarEnter() {
        System.out.print("\nPresione ENTER para continuar...");
        leerLinea();
    }

    private boolean esEnteroValido(String texto) {
        if (texto.isEmpty() || texto.length() > MAX_DIGITOS) {
            return false;
        }
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
