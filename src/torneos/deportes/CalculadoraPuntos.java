package torneos.deportes;

/**
 * Contrato para calcular los puntos que obtiene cada equipo en un partido.
 * Cada deporte implementa sus propias reglas de puntuación.
 */
public interface CalculadoraPuntos {

    /**
     * @param marcadorLocal     tantos del equipo local
     * @param marcadorVisitante tantos del equipo visitante
     * @return arreglo de dos posiciones: [0] puntos del local, [1] puntos del visitante
     */
    int[] calcularPuntos(int marcadorLocal, int marcadorVisitante);
}
