package torneos.ui;

import java.util.ArrayList;
import torneos.deportes.Baloncesto;
import torneos.deportes.Deporte;
import torneos.deportes.Futbol;
import torneos.gestion.GestorTorneos;
import torneos.modelo.Equipo;
import torneos.modelo.Jugador;
import torneos.modelo.Partido;
import torneos.modelo.Torneo;

/** Menús de consola. Solo interactúa con el usuario; las reglas están en GestorTorneos. */
public class MenuPrincipal {

    private static final int MAX_MARCADOR = 200;

    private final Consola consola;
    private final GestorTorneos gestor;

    public MenuPrincipal() {
        this.consola = new Consola();
        this.gestor = new GestorTorneos();
    }

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\n========================================");
            System.out.println("  SISTEMA DE GESTIÓN DE TORNEOS DEPORTIVOS");
            System.out.println("========================================");
            System.out.println("1.- Crear nuevo torneo");
            System.out.println("2.- Gestión de equipos y jugadores");
            System.out.println("3.- Gestión de partidos y resultados");
            System.out.println("4.- Consultas y reportes");
            System.out.println("5.- Salir");
            opcion = consola.leerEntero("Seleccione una opción (1-5): ", 1, 5);
            switch (opcion) {
                case 1:
                    crearTorneo();
                    break;
                case 2:
                    menuEquiposJugadores();
                    break;
                case 3:
                    menuPartidos();
                    break;
                case 4:
                    menuConsultas();
                    break;
                default:
                    System.out.println("\nGracias por usar el sistema. ¡Hasta pronto!");
                    break;
            }
        } while (opcion != 5);
    }

    // ------------------------------------------------------------------ 1. Torneo

    private void crearTorneo() {
        System.out.println("\n--- CREAR NUEVO TORNEO ---");
        String nombre = consola.leerTexto("Nombre del torneo: ");
        String error = gestor.validarNombreTorneo(nombre);
        if (error != null) {
            System.out.println("[Error] " + error);
            return;
        }
        System.out.println("Deportes disponibles:");
        System.out.println("1.- Fútbol");
        System.out.println("2.- Baloncesto");
        int eleccion = consola.leerEntero("Seleccione el deporte (1-2): ", 1, 2);
        Deporte deporte;
        if (eleccion == 1) {
            deporte = new Futbol();
        } else {
            deporte = new Baloncesto();
        }
        Torneo torneo = gestor.crearTorneo(nombre, deporte);
        System.out.println("[OK] Torneo creado: " + torneo);
        System.out.println("     Reglas: " + deporte.getReglasPuntuacion());
    }

    // ------------------------------------------------- 2. Equipos y jugadores

    private void menuEquiposJugadores() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE EQUIPOS Y JUGADORES ---");
            System.out.println("1.- Registrar equipo en el torneo");
            System.out.println("2.- Registrar jugador en un equipo");
            System.out.println("0.- Volver al menú principal");
            opcion = consola.leerEntero("Seleccione una opción (0-2): ", 0, 2);
            if (opcion == 1) {
                registrarEquipo();
            } else if (opcion == 2) {
                registrarJugador();
            }
        } while (opcion != 0);
    }

    private void registrarEquipo() {
        Torneo torneo = elegirTorneo();
        if (torneo == null) {
            return;
        }
        String nombre = consola.leerTexto("Nombre del equipo: ");
        String error = gestor.registrarEquipo(torneo, nombre);
        if (error != null) {
            System.out.println("[Error] " + error);
        } else {
            System.out.println("[OK] Equipo \"" + nombre + "\" registrado ("
                    + torneo.getCantidadEquipos() + "/" + Torneo.MAX_EQUIPOS + ").");
        }
    }

    private void registrarJugador() {
        Torneo torneo = elegirTorneo();
        if (torneo == null) {
            return;
        }
        Equipo equipo = elegirEquipo(torneo);
        if (equipo == null) {
            return;
        }
        String errorCupo = gestor.verificarCupoJugador(torneo, equipo);
        if (errorCupo != null) {
            System.out.println("[Error] " + errorCupo);
            return;
        }
        String nombre = consola.leerTexto("Nombre del jugador: ");
        int edad = consola.leerEntero("Edad (" + Jugador.EDAD_MINIMA + "-"
                + Jugador.EDAD_MAXIMA + "): ", Jugador.EDAD_MINIMA, Jugador.EDAD_MAXIMA);
        int camiseta = consola.leerEntero("Número de camiseta (" + Jugador.CAMISETA_MINIMA
                + "-" + Jugador.CAMISETA_MAXIMA + "): ",
                Jugador.CAMISETA_MINIMA, Jugador.CAMISETA_MAXIMA);
        String error = gestor.registrarJugador(torneo, equipo, nombre, edad, camiseta);
        if (error != null) {
            System.out.println("[Error] " + error);
        } else {
            System.out.println("[OK] Jugador registrado en " + equipo.getNombre() + " ("
                    + equipo.getCantidadJugadores() + "/"
                    + torneo.getDeporte().getMaxJugadoresPorEquipo() + ").");
        }
    }

    // ------------------------------------------------- 3. Partidos y resultados

    private void menuPartidos() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE PARTIDOS Y RESULTADOS ---");
            System.out.println("1.- Programar partidos");
            System.out.println("2.- Registrar resultados de partidos");
            System.out.println("0.- Volver al menú principal");
            opcion = consola.leerEntero("Seleccione una opción (0-2): ", 0, 2);
            if (opcion == 1) {
                programarPartidos();
            } else if (opcion == 2) {
                registrarResultado();
            }
        } while (opcion != 0);
    }

    private void programarPartidos() {
        Torneo torneo = elegirTorneo();
        if (torneo == null) {
            return;
        }
        String errorPar = gestor.validarCantidadEquiposPar(torneo);
        if (errorPar != null) {
            System.out.println("[Error] " + errorPar);
            return;
        }
        System.out.println("1.- Programar un partido eligiendo los equipos");
        System.out.println("2.- Generar todos los enfrentamientos (todos contra todos)");
        int modo = consola.leerEntero("Seleccione una opción (1-2): ", 1, 2);
        if (modo == 2) {
            String error = gestor.generarTodosContraTodos(torneo);
            if (error != null) {
                System.out.println("[Error] " + error);
            } else {
                System.out.println("[OK] Partidos programados. Total: "
                        + torneo.getPartidos().size());
            }
            return;
        }
        System.out.println("Equipo local:");
        Equipo local = elegirEquipo(torneo);
        System.out.println("Equipo visitante:");
        Equipo visitante = elegirEquipo(torneo);
        String error = gestor.programarPartido(torneo, local, visitante);
        if (error != null) {
            System.out.println("[Error] " + error);
        } else {
            System.out.println("[OK] Partido programado: " + local.getNombre()
                    + " vs " + visitante.getNombre());
        }
    }

    private void registrarResultado() {
        Torneo torneo = elegirTorneo();
        if (torneo == null) {
            return;
        }
        ArrayList<Partido> pendientes = torneo.getPartidosPendientes();
        if (pendientes.isEmpty()) {
            System.out.println("[Aviso] No hay partidos pendientes. Programe partidos primero.");
            return;
        }
        System.out.println("Partidos pendientes:");
        for (int i = 0; i < pendientes.size(); i++) {
            System.out.println((i + 1) + ".- " + pendientes.get(i));
        }
        int eleccion = consola.leerEntero("Seleccione el partido (1-" + pendientes.size() + "): ",
                1, pendientes.size());
        Partido partido = pendientes.get(eleccion - 1);
        String error;
        do {
            int local = consola.leerEntero("Marcador del equipo local: ", 0, MAX_MARCADOR);
            int visitante = consola.leerEntero("Marcador del equipo visitante: ", 0, MAX_MARCADOR);
            error = gestor.registrarResultado(torneo, partido, local, visitante);
            if (error != null) {
                System.out.println("[Error] " + error);
            }
        } while (error != null);
        System.out.println("[OK] Resultado registrado: " + partido);
        if (torneo.estaFinalizado()) {
            mostrarCampeon(torneo);
        }
    }

    // ------------------------------------------------- 4. Consultas y reportes

    private void menuConsultas() {
        int opcion;
        do {
            System.out.println("\n--- CONSULTAS Y REPORTES ---");
            System.out.println("1.- Tabla de posiciones");
            System.out.println("2.- Listado de equipos y jugadores");
            System.out.println("3.- Historial de partidos");
            System.out.println("0.- Volver al menú principal");
            opcion = consola.leerEntero("Seleccione una opción (0-3): ", 0, 3);
            if (opcion >= 1) {
                Torneo torneo = elegirTorneo();
                if (torneo != null) {
                    if (opcion == 1) {
                        mostrarTablaPosiciones(torneo);
                    } else if (opcion == 2) {
                        mostrarEquiposYJugadores(torneo);
                    } else {
                        mostrarHistorialPartidos(torneo);
                    }
                    consola.esperarEnter();
                }
            }
        } while (opcion != 0);
    }

    private void mostrarTablaPosiciones(Torneo torneo) {
        String error = gestor.validarCantidadEquiposPar(torneo);
        if (error != null) {
            System.out.println("[Aviso] No se puede generar la tabla. " + error);
            return;
        }
        System.out.println("\nTABLA DE POSICIONES - " + torneo.getNombre()
                + " (" + torneo.getDeporte() + ")");
        System.out.println("Reglas: " + torneo.getDeporte().getReglasPuntuacion());
        System.out.printf("%-4s %-20s %3s %5s %4s%n", "POS", "EQUIPO", "PJ", "DIF", "PTS");
        ArrayList<Equipo> tabla = torneo.obtenerTablaPosiciones();
        for (int i = 0; i < tabla.size(); i++) {
            Equipo e = tabla.get(i);
            System.out.printf("%-4d %-20s %3d %5d %4d%n", i + 1, e.getNombre(),
                    e.getPartidosJugados(), e.getDiferenciaTantos(), e.getPuntos());
        }
        System.out.println("PJ=Partidos jugados DIF=Diferencia de puntos PTS=Puntos");
        System.out.println("Desempate: mayor diferencia de puntos.");
        if (torneo.estaFinalizado()) {
            mostrarCampeon(torneo);
        }
    }

    private void mostrarEquiposYJugadores(Torneo torneo) {
        System.out.println("\nEQUIPOS Y JUGADORES - " + torneo);
        ArrayList<Equipo> equipos = torneo.getEquipos();
        if (equipos.isEmpty()) {
            System.out.println("Aún no hay equipos registrados.");
            return;
        }
        for (Equipo equipo : equipos) {
            System.out.println("\n* " + equipo);
            ArrayList<Jugador> jugadores = equipo.getJugadores();
            if (jugadores.isEmpty()) {
                System.out.println("    (sin jugadores registrados)");
            }
            for (Jugador j : jugadores) {
                System.out.println("    " + j);
            }
        }
    }

    private void mostrarHistorialPartidos(Torneo torneo) {
        System.out.println("\nHISTORIAL DE PARTIDOS - " + torneo.getNombre());
        ArrayList<Partido> partidos = torneo.getPartidos();
        if (partidos.isEmpty()) {
            System.out.println("Aún no hay partidos programados.");
            return;
        }
        for (int i = 0; i < partidos.size(); i++) {
            System.out.println((i + 1) + ".- " + partidos.get(i));
        }
    }

    private void mostrarCampeon(Torneo torneo) {
        System.out.println("\n*** TORNEO FINALIZADO ***");
        System.out.println("Campeón: " + torneo.obtenerCampeon().getNombre());
    }

    // ------------------------------------------------------------ Selecciones

    /** Devuelve el torneo elegido por el usuario, o null si no hay torneos. */
    private Torneo elegirTorneo() {
        ArrayList<Torneo> torneos = gestor.getTorneos();
        if (torneos.isEmpty()) {
            System.out.println("[Aviso] No hay torneos. Cree uno con la opción 1 del menú principal.");
            return null;
        }
        if (torneos.size() == 1) {
            System.out.println("Torneo: " + torneos.get(0));
            return torneos.get(0);
        }
        System.out.println("Torneos registrados:");
        for (int i = 0; i < torneos.size(); i++) {
            System.out.println((i + 1) + ".- " + torneos.get(i));
        }
        int eleccion = consola.leerEntero("Seleccione el torneo (1-" + torneos.size() + "): ",
                1, torneos.size());
        return torneos.get(eleccion - 1);
    }

    /** Devuelve el equipo elegido por el usuario, o null si el torneo no tiene equipos. */
    private Equipo elegirEquipo(Torneo torneo) {
        ArrayList<Equipo> equipos = torneo.getEquipos();
        if (equipos.isEmpty()) {
            System.out.println("[Aviso] El torneo no tiene equipos. Registre equipos primero.");
            return null;
        }
        for (int i = 0; i < equipos.size(); i++) {
            System.out.println((i + 1) + ".- " + equipos.get(i));
        }
        int eleccion = consola.leerEntero("Seleccione el equipo (1-" + equipos.size() + "): ",
                1, equipos.size());
        return torneo.buscarEquipo(eleccion);
    }
}
