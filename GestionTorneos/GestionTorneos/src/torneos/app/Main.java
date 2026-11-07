package torneos.app;

import torneos.seguridad.LoginConsola;
import torneos.ui.MenuPrincipal;

public class Main {

    public static void main(String[] args) {
        boolean accesoConcedido = false;
        
        while (!accesoConcedido) {
            accesoConcedido = LoginConsola.mostrarLogin();
        }
        
        MenuPrincipal menu = new MenuPrincipal();
        menu.iniciar();
    }
}
