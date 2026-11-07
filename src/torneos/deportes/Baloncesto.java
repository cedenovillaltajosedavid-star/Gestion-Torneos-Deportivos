package torneos.deportes;

/** Baloncesto: ganador 2 puntos, perdedor 1 punto, no existen empates. */
public class Baloncesto extends Deporte {

    private static final int MAX_JUGADORES = 12;

    public Baloncesto() {
        super("Baloncesto", MAX_JUGADORES);
    }

    @Override
    public int[] calcularPuntos(int marcadorLocal, int marcadorVisitante) {
        if (marcadorLocal > marcadorVisitante) {
            return new int[]{2, 1};
        }
        // En baloncesto no hay empates: el gestor los rechaza antes de llegar aquí.
        return new int[]{1, 2};
    }

    @Override
    public boolean permiteEmpate() {
        return false;
    }

    @Override
    public String getReglasPuntuacion() {
        return "Victoria: 2 puntos | Derrota: 1 punto | No hay empates";
    }
}
