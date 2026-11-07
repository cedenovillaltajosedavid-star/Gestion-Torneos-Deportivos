package torneos.gestion;

import java.util.ArrayList;
import torneos.deportes.Deporte;
import torneos.modelo.Equipo;
import torneos.modelo.Jugador;
import torneos.modelo.Partido;
import torneos.modelo.Torneo;

/**
 * Coordina el registro, la administración y la consulta de los torneos.
 * Aplica las reglas de negocio. Los métodos de validación devuelven un mensaje
 * de error, o null cuando la operación es válida (sin usar excepciones).
 */
public class GestorTorneos {

    private final ArrayList<Torneo> torneos;

    public GestorTorneos() {
        this.torneos = new ArrayList<>();
    }

    public ArrayList<Torneo> getTorneos() {
        return new ArrayList<>(torneos);
    }

    // ---------------------------------------------------------------- Torneos

    public String validarNombreTorneo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre del torneo no puede estar vacío.";
        }
        for (Torneo t : torneos) {
            if (t.getNombre().equalsIgnoreCase(nombre.trim())) {
                return "Ya existe un torneo llamado \"" + t.getNombre() + "\".";
            }
        }
        return null;
    }

    /** Crea el torneo y lo agrega a la lista. Devuelve null si el nombre no es válido. */
    public Torneo crearTorneo(String nombre, Deporte deporte) {
        if (validarNombreTorneo(nombre) != null) {
            return null;
        }
        Torneo torneo = new Torneo(nombre.trim(), deporte);
        torneos.add(torneo);
        return torneo;
    }

    // ---------------------------------------------------------------- Equipos

    public String registrarEquipo(Torneo torneo, String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre del equipo no puede estar vacío.";
        }
        if (torneo.tienePartidosProgramados()) {
            return "Ya hay partidos programados; no se pueden agregar más equipos.";
        }
        if (torneo.estaLleno()) {
            return "El torneo ya alcanzó el máximo de " + Torneo.MAX_EQUIPOS + " equipos.";
        }
        if (torneo.buscarEquipo(nombre.trim()) != null) {
            return "Ya existe un equipo llamado \"" + nombre.trim() + "\" en este torneo.";
        }
        torneo.agregarEquipo(new Equipo(nombre.trim(), torneo.getNombre()));
        return null;
    }

    // --------------------------------------------------------------- Jugadores

    public String verificarCupoJugador(Torneo torneo, Equipo equipo) {
        int maximo = torneo.getDeporte().getMaxJugadoresPorEquipo();
        if (equipo.getCantidadJugadores() >= maximo) {
            return "El equipo \"" + equipo.getNombre() + "\" ya tiene el máximo de "
                    + maximo + " jugadores para " + torneo.getDeporte() + ".";
        }
        return null;
    }

    public String registrarJugador(Torneo torneo, Equipo equipo, String nombre,
                                   int edad, int numeroCamiseta) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre del jugador no puede estar vacío.";
        }
        if (equipo == null || torneo.buscarEquipo(equipo.getNombre()) == null) {
            return "El jugador debe pertenecer a un equipo registrado en el torneo.";
        }
        String errorCupo = verificarCupoJugador(torneo, equipo);
        if (errorCupo != null) {
            return errorCupo;
        }
        if (edad < Jugador.EDAD_MINIMA || edad > Jugador.EDAD_MAXIMA) {
            return "La edad debe estar entre " + Jugador.EDAD_MINIMA + " y "
                    + Jugador.EDAD_MAXIMA + " años.";
        }
        if (numeroCamiseta < Jugador.CAMISETA_MINIMA || numeroCamiseta > Jugador.CAMISETA_MAXIMA) {
            return "El número de camiseta debe estar entre " + Jugador.CAMISETA_MINIMA
                    + " y " + Jugador.CAMISETA_MAXIMA + ".";
        }
        if (equipo.existeCamiseta(numeroCamiseta)) {
            return "El número de camiseta " + numeroCamiseta + " ya está usado en el equipo.";
        }
        equipo.agregarJugador(new Jugador(nombre.trim(), edad, numeroCamiseta, equipo.getNombre()));
        return null;
    }

    // ---------------------------------------------------------------- Partidos

    /** Se necesitan al menos 2 equipos y una cantidad par para programar partidos o ver la tabla. */
    public String validarCantidadEquiposPar(Torneo torneo) {
        int cantidad = torneo.getCantidadEquipos();
        if (cantidad < 2) {
            return "Se necesitan al menos 2 equipos registrados (hay " + cantidad + ").";
        }
        if (cantidad % 2 != 0) {
            return "La cantidad de equipos debe ser par (hay " + cantidad
                    + "). Registre un equipo más.";
        }
        return null;
    }

    public String programarPartido(Torneo torneo, Equipo local, Equipo visitante) {
        String error = validarCantidadEquiposPar(torneo);
        if (error != null) {
            return error;
        }
        if (local == null || visitante == null) {
            return "Debe seleccionar dos equipos registrados.";
        }
        if (local.equals(visitante)) {
            return "Un equipo no puede enfrentarse a sí mismo.";
        }
        if (torneo.existePartido(local, visitante)) {
            return "Esos dos equipos ya tienen un partido programado entre sí.";
        }
        torneo.agregarPartido(new Partido(local, visitante));
        return null;
    }

    public String generarTodosContraTodos(Torneo torneo) {
        String error = validarCantidadEquiposPar(torneo);
        if (error != null) {
            return error;
        }
        if (torneo.generarTodosContraTodos() == 0) {
            return "Ya están programados todos los enfrentamientos posibles.";
        }
        return null;
    }

    public String registrarResultado(Torneo torneo, Partido partido,
                                     int marcadorLocal, int marcadorVisitante) {
        if (partido == null || partido.isJugado()) {
            return "El partido no existe o ya tiene un resultado registrado.";
        }
        if (marcadorLocal < 0 || marcadorVisitante < 0) {
            return "Los marcadores deben ser números enteros no negativos.";
        }
        if (!torneo.getDeporte().permiteEmpate() && marcadorLocal == marcadorVisitante) {
            return "En " + torneo.getDeporte() + " no puede haber empates. Ingrese un ganador.";
        }
        // Polimorfismo: el Deporte (referencia padre) calcula los puntos según sus reglas.
        partido.registrarResultado(marcadorLocal, marcadorVisitante, torneo.getDeporte());
        return null;
    }
}
