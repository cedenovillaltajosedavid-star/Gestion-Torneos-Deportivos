package torneos.modelo;

import torneos.deportes.CalculadoraPuntos;

/** Encuentro entre dos equipos distintos. */
public class Partido {

    private final Equipo local;
    private final Equipo visitante;
    private int marcadorLocal;
    private int marcadorVisitante;
    private boolean jugado;

    public Partido(Equipo local, Equipo visitante) {
        this.local = local;
        this.visitante = visitante;
        this.jugado = false;
    }

    public boolean isJugado() {
        return jugado;
    }

    /** Indica si este partido enfrenta a los equipos dados, sin importar el orden. */
    public boolean enfrenta(Equipo a, Equipo b) {
        return (local.equals(a) && visitante.equals(b))
                || (local.equals(b) && visitante.equals(a));
    }

    /**
     * Registra el marcador y actualiza a ambos equipos. Usa una referencia de tipo
     * interfaz (CalculadoraPuntos): el cálculo depende del deporte concreto (polimorfismo).
     */
    public void registrarResultado(int marcadorLocal, int marcadorVisitante,
                                   CalculadoraPuntos calculadora) {
        int[] puntos = calculadora.calcularPuntos(marcadorLocal, marcadorVisitante);
        this.marcadorLocal = marcadorLocal;
        this.marcadorVisitante = marcadorVisitante;
        local.registrarResultado(marcadorLocal, marcadorVisitante, puntos[0]);
        visitante.registrarResultado(marcadorVisitante, marcadorLocal, puntos[1]);
        this.jugado = true;
    }

    @Override
    public String toString() {
        if (jugado) {
            return local.getNombre() + " " + marcadorLocal + " - " + marcadorVisitante
                    + " " + visitante.getNombre();
        }
        return local.getNombre() + " vs " + visitante.getNombre() + " (pendiente)";
    }

    /** Dos partidos son iguales si enfrentan a los mismos equipos (en cualquier orden). */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Partido)) {
            return false;
        }
        Partido otro = (Partido) obj;
        return enfrenta(otro.local, otro.visitante);
    }

    @Override
    public int hashCode() {
        return local.hashCode() + visitante.hashCode();
    }
}
