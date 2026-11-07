package torneos.deportes;

/**
 * Clase abstracta que representa una disciplina deportiva.
 * Las clases hijas definen sus reglas de puntuación.
 */
public abstract class Deporte implements CalculadoraPuntos {

    private final String nombre;
    private final int maxJugadoresPorEquipo;

    protected Deporte(String nombre, int maxJugadoresPorEquipo) {
        this.nombre = nombre;
        this.maxJugadoresPorEquipo = maxJugadoresPorEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMaxJugadoresPorEquipo() {
        return maxJugadoresPorEquipo;
    }

    /** Indica si en este deporte un partido puede terminar empatado. */
    public abstract boolean permiteEmpate();

    /** Texto breve con las reglas de puntuación del deporte. */
    public abstract String getReglasPuntuacion();

    @Override
    public String toString() {
        return nombre;
    }
}
