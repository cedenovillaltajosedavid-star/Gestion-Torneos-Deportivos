package torneos.modelo;

/** Jugador que pertenece a un equipo de un torneo. */
public class Jugador {

    public static final int EDAD_MINIMA = 6;
    public static final int EDAD_MAXIMA = 50;
    public static final int CAMISETA_MINIMA = 1;
    public static final int CAMISETA_MAXIMA = 999; // máximo tres dígitos

    private final String nombre;
    private final int edad;
    private final int numeroCamiseta;
    private final String nombreEquipo;

    public Jugador(String nombre, int edad, int numeroCamiseta, String nombreEquipo) {
        this.nombre = nombre;
        this.edad = edad;
        this.numeroCamiseta = numeroCamiseta;
        this.nombreEquipo = nombreEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    @Override
    public String toString() {
        return "#" + numeroCamiseta + " " + nombre + " (" + edad + " años)";
    }

    /** Dos jugadores son iguales si tienen el mismo número de camiseta en el mismo equipo. */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Jugador)) {
            return false;
        }
        Jugador otro = (Jugador) obj;
        return numeroCamiseta == otro.numeroCamiseta
                && nombreEquipo.equalsIgnoreCase(otro.nombreEquipo);
    }

    @Override
    public int hashCode() {
        return 31 * numeroCamiseta + nombreEquipo.toLowerCase().hashCode();
    }
}
