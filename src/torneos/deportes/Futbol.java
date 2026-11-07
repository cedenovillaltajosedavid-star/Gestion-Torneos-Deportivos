package torneos.deportes;

/** Fútbol: ganador 3 puntos, empate 1 punto para cada equipo, perdedor 0. */
public class Futbol extends Deporte {

    private static final int MAX_JUGADORES = 16;

    public Futbol() {
        super("Fútbol", MAX_JUGADORES);
    }

    @Override
    public int[] calcularPuntos(int marcadorLocal, int marcadorVisitante) {
        if (marcadorLocal > marcadorVisitante) {
            return new int[]{3, 0};
        }
        if (marcadorLocal < marcadorVisitante) {
            return new int[]{0, 3};
        }
        return new int[]{1, 1};
    }

    @Override
    public boolean permiteEmpate() {
        return true;
    }

    @Override
    public String getReglasPuntuacion() {
        return "Victoria: 3 puntos | Empate: 1 punto cada equipo | Derrota: 0 puntos";
    }
}
