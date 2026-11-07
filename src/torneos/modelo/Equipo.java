package torneos.modelo;

import java.util.ArrayList;

/** Equipo participante de un torneo. Administra su plantilla y sus estadísticas. */
public class Equipo {

    private final String nombre;
    private final String nombreTorneo;
    private final ArrayList<Jugador> jugadores;
    private int partidosJugados;
    private int puntos;
    private int tantosFavor;
    private int tantosContra;

    public Equipo(String nombre, String nombreTorneo) {
        this.nombre = nombre;
        this.nombreTorneo = nombreTorneo;
        this.jugadores = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getDiferenciaTantos() {
        return tantosFavor - tantosContra;
    }

    public int getCantidadJugadores() {
        return jugadores.size();
    }

    /** Devuelve una copia para que la lista interna no se modifique desde fuera. */
    public ArrayList<Jugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }

    public boolean existeCamiseta(int numero) {
        for (Jugador j : jugadores) {
            if (j.getNumeroCamiseta() == numero) {
                return true;
            }
        }
        return false;
    }

    public void agregarJugador(Jugador jugador) {
        jugadores.add(jugador);
    }

    /**
     * Actualiza las estadísticas del equipo con el resultado de un partido.
     *
     * @param favor          tantos anotados por este equipo
     * @param contra         tantos recibidos
     * @param puntosGanados  puntos que le corresponden según el deporte
     */
    public void registrarResultado(int favor, int contra, int puntosGanados) {
        partidosJugados++;
        tantosFavor += favor;
        tantosContra += contra;
        puntos += puntosGanados;
    }

    @Override
    public String toString() {
        return nombre + " (" + jugadores.size() + " jugadores)";
    }

    /** Dos equipos son iguales si tienen el mismo nombre dentro del mismo torneo. */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Equipo)) {
            return false;
        }
        Equipo otro = (Equipo) obj;
        return nombre.equalsIgnoreCase(otro.nombre)
                && nombreTorneo.equalsIgnoreCase(otro.nombreTorneo);
    }

    @Override
    public int hashCode() {
        return 31 * nombre.toLowerCase().hashCode() + nombreTorneo.toLowerCase().hashCode();
    }
}
