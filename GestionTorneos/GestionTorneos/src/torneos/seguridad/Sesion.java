package torneos.seguridad;

public class Sesion {
    private static String usuarioActual = null;

    // Arreglo con los usuarios válidos del sistema
    private static final Usuario[] usuariosValidos = {
        new Usuario("administrador", "1234"),
        new Usuario("organizador", "4321")
    };

    public static boolean autenticar(String user, String password) {
        // Recorremos el arreglo estático buscando coincidencias
        for (Usuario u : usuariosValidos) {
            if (u != null &&
                u.getUsuario().equals(user) && 
                u.getContrasenia().equals(password)) {
                
                usuarioActual = user;
                return true;
            }
        }
        return false; 
    }

    public static String getUsuarioActual() {
        return usuarioActual;
    }
}