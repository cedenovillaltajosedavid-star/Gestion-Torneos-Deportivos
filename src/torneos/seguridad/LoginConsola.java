package torneos.seguridad;

import torneos.ui.Consola;

public class LoginConsola {
    
    public static boolean mostrarLogin() {
        Consola consola = new Consola();
        System.out.println("========================================");
        System.out.println("     SISTEMA DE GESTION DE TORNEOS      ");
        System.out.println("       --- MODULO DE SEGURIDAD ---      ");
        System.out.println("========================================");
        
        // Usamos leerTexto que es public
        String usuario = consola.leerTexto("Ingrese usuario: ");
        String contrasenia = consola.leerTexto("Ingrese contraseña: ");
        
        if (Sesion.autenticar(usuario, contrasenia)) {
            System.out.println("\n[Acceso Concedido] ¡Bienvenido, " + usuario + "!\n");
            return true;
        } else {
            System.out.println("\n[Error] Usuario o contraseña incorrectos. Intente nuevamente.\n");
            return false;
        }
    }
}