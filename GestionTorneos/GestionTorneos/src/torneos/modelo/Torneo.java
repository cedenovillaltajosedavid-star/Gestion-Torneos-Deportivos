package torneos.modelo;

import java.util.ArrayList;
import torneos.deportes.Deporte;

/** Torneo relámpago de un deporte: contiene sus equipos y sus partidos. */
public class Torneo {

    public static final int MAX_EQUIPOS = 8;

    private final String nombre;
    private final Deporte deporte;
    private final ArrayList<Equipo> equipos;
    private final ArrayList<Partido> partidos;

    public Torneo(String nombre, Deporte deporte) {
        this.nombre = nombre;
        this.deporte = deporte;
        this.equipos = new ArrayList<>();
        this.partidos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public int getCantidadEquipos() {
        return equipos.size();
    }

    public boolean estaLleno() {
        return equipos.size() >= MAX_EQUIPOS;
    }

    public void agregarEquipo(Equipo equipo) {
        equipos.add(equipo);
    }

    /** Busca un equipo por su nombre (ignora mayúsculas). Devuelve null si no existe. */
    public Equipo buscarEquipo(String nombreEquipo) {
        for (Equipo e : equipos) {
            if (e.getNombre().equalsIgnoreCase(nombreEquipo)) {
                return e;
            }
        }
        return null;
    }

    /** Busca un equipo por su posición en la lista (empezando en 1). Devuelve null si no existe. */
    public Equipo buscarEquipo(int posicion) {
        if (posicion < 1 || posicion > equipos.size()) {
            return null;
        }
        return equipos.get(posicion - 1);
    }

    public ArrayList<Equipo> getEquipos() {
        return new ArrayList<>(equipos);
    }

    public ArrayList<Partido> getPartidos() {
        return new ArrayList<>(partidos);
    }

    public ArrayList<Partido> getPartidosPendientes() {
        ArrayList<Partido> pendientes = new ArrayList<>();
        for (Partido p : partidos) {
            if (!p.isJugado()) {
                pendientes.add(p);
            }
        }
        return pendientes;
    }

    public boolean tienePartidosProgramados() {
        return !partidos.isEmpty();
    }

    public boolean existePartido(Equipo a, Equipo b) {
        for (Partido p : partidos) {
            if (p.enfrenta(a, b)) {
                return true;
            }
        }
        return false;
    }

    public void agregarPartido(Partido partido) {
        partidos.add(partido);
    }

    /** Programa todos los enfrentamientos posibles que aún no existan. Devuelve cuántos creó. */
    public int generarTodosContraTodos() {
        int creados = 0;
        for (int i = 0; i < equipos.size(); i++) {
            for (int j = i + 1; j < equipos.size(); j++) {
                if (!existePartido(equipos.get(i), equipos.get(j))) {
                    partidos.add(new Partido(equipos.get(i), equipos.get(j)));
                    creados++;
                }
            }
        }
        return creados;
    }

    public int getTotalPartidosPosibles() {
        int n = equipos.size();
        return n * (n - 1) / 2;
    }

    /** El torneo termina cuando todos los equipos jugaron entre sí y no quedan partidos pendientes. */
    public boolean estaFinalizado() {
        return equipos.size() >= 2
                && partidos.size() == getTotalPartidosPosibles()
                && getPartidosPendientes().isEmpty();
    }

    /**
     * Copia de los equipos ordenada de mayor a menor puntaje (ordenamiento burbuja).
     * Si dos equipos tienen los mismos puntos, desempata la mayor diferencia de puntos.
     */
    public ArrayList<Equipo> obtenerTablaPosiciones() {
        ArrayList<Equipo> tabla = new ArrayList<>(equipos);
        for (int i = 0; i < tabla.size() - 1; i++) {
            for (int j = 0; j < tabla.size() - 1 - i; j++) {
                Equipo actual = tabla.get(j);
                Equipo siguiente = tabla.get(j + 1);
                boolean menosPuntos = actual.getPuntos() < siguiente.getPuntos();
                boolean empatadoConMenorDiferencia = actual.getPuntos() == siguiente.getPuntos()
                        && actual.getDiferenciaTantos() < siguiente.getDiferenciaTantos();
                if (menosPuntos || empatadoConMenorDiferencia) {
                    Equipo temporal = tabla.get(j);
                    tabla.set(j, tabla.get(j + 1));
                    tabla.set(j + 1, temporal);
                }
            }
        }
        return tabla;
    }

    /** El campeón es el primero de la tabla, pero solo cuando el torneo ya finalizó. */
    public Equipo obtenerCampeon() {
        if (!estaFinalizado()) {
            return null;
        }
        return obtenerTablaPosiciones().get(0);
    }

    @Override
    public String toString() {
        return nombre + " [" + deporte + "] - " + equipos.size() + " equipo(s)";
    }

    /** Dos torneos son iguales si tienen el mismo nombre, sin distinguir mayúsculas. */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Torneo)) {
            return false;
        }
        return nombre.equalsIgnoreCase(((Torneo) obj).nombre);
    }

    @Override
    public int hashCode() {
        return nombre.toLowerCase().hashCode();
    }
}
