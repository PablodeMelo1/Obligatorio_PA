package grafo;

import abb.ABB;
import cola.Cola;
import cola.ICola;
import dominio.Region;

/**
 * Grafo del profe: matriz de adyacencia + array de vértices.
 * Los vértices son regiones y las aristas tienen latencia y costo.
 */
public class Grafo {

    private final int cantMaxVertices;
    private int cantVertices;
    private final boolean esDirigido;
    private final Arista[][] aristas;
    private final Region[] vertices;

    public Grafo(int cantMaxVertices, boolean esDirigido) {
        this.cantMaxVertices = cantMaxVertices;
        this.esDirigido = esDirigido;
        this.aristas = new Arista[cantMaxVertices][cantMaxVertices];
        this.vertices = new Region[cantMaxVertices];

        if (esDirigido) {
            for (int i = 0; i < cantMaxVertices; i++) {
                for (int j = 0; j < cantMaxVertices; j++) {
                    aristas[i][j] = new Arista();
                }
            }
        } else {
            // No dirigido: [i][j] y [j][i] apuntan a la MISMA arista,
            // así que al agregar A->B también queda B->A.
            for (int i = 0; i < cantMaxVertices; i++) {
                for (int j = i; j < cantMaxVertices; j++) {
                    Arista arista = new Arista();
                    aristas[i][j] = arista;
                    aristas[j][i] = arista;
                }
            }
        }
    }

    public void agregarVertice(Region v) {
        if (cantVertices < cantMaxVertices) {
            int posLibre = obtenerPosLibre();
            vertices[posLibre] = v;
            cantVertices++;
        }
    }

    // Pre: existeVertice(vOrigen) && existeVertice(vDestino)
    public void agregarArista(Region vOrigen, Region vDestino, Arista a) {
        int posVOrigen = obtenerPos(vOrigen);
        int posVDestino = obtenerPos(vDestino);

        aristas[posVOrigen][posVDestino].setExiste(true);
        aristas[posVOrigen][posVDestino].setLatencia(a.getLatencia());
        aristas[posVOrigen][posVDestino].setCosto(a.getCosto());
    }

    public boolean existeVertice(Region v) {
        return obtenerPos(v) != -1;
    }

    public boolean esVacio() {
        return cantVertices == 0;
    }

    public boolean estaLleno() {
        return cantVertices == cantMaxVertices;
    }

    public boolean existeArista(Region origen, Region destino) {
        int posOrigen = obtenerPos(origen);
        int posDestino = obtenerPos(destino);
        return posOrigen != -1 && posDestino != -1 && aristas[posOrigen][posDestino].isExiste();
    }

    // Idea: bfsConTuplas del profe. Cada tupla es (posición, saltos).
    // Solo se expanden los vecinos de quienes tienen menos de "cantidad" saltos.
    // En vez de imprimir, cada región alcanzada se inserta en un ABB (ordena por código).
    public String redRegionesPorCantidadDeSaltos(Region origen, int cantidad) {
        boolean[] visitados = new boolean[cantMaxVertices];
        int posOrigen = obtenerPos(origen);
        ABB<Region> alcanzadas = new ABB<>();

        ICola<Tupla<Integer, Integer>> cola = new Cola<>();
        visitados[posOrigen] = true;
        cola.encolar(new Tupla<>(posOrigen, 0));
        while (!cola.estaVacia()) {
            Tupla<Integer, Integer> tuplaDesencolada = cola.desencolar();
            int pos = tuplaDesencolada.getDato1();
            int salto = tuplaDesencolada.getDato2();

            alcanzadas.insertar(vertices[pos]);
            if (salto < cantidad) {
                for (int i = 0; i < aristas.length; i++) {
                    if (aristas[pos][i].isExiste() && !visitados[i]) {
                        visitados[i] = true;
                        cola.encolar(new Tupla<>(i, salto + 1));
                    }
                }
            }
        }
        return alcanzadas.listarAscendente();
    }

    // Pre: existeVertice(origen) && existeVertice(destino)
    // Post: Tupla(latencia total, ruta) o null si no hay ruta.
    public Tupla<Integer, String> rutaMenorLatencia(Region origen, Region destino) {
        return dijkstra(origen, destino, true);
    }

    // Pre: existeVertice(origen) && existeVertice(destino)
    // Post: Tupla(costo total, ruta) o null si no hay ruta.
    public Tupla<Integer, String> rutaMenorCosto(Region origen, Region destino) {
        return dijkstra(origen, destino, false);
    }

    /*
     * Idea: el dijkstra del profe (visitados, costos y vengo).
     * Es el mismo algoritmo para los dos ejercicios; lo único que cambia es
     * qué peso de la arista se suma: la latencia o el costo.
     * Al terminar, si el destino sigue en "infinito" no hay ruta.
     */
    private Tupla<Integer, String> dijkstra(Region origen, Region destino, boolean usarLatencia) {
        int posOrigen = obtenerPos(origen);
        int posDestino = obtenerPos(destino);

        // Inicializar estructuras
        boolean[] visitados = new boolean[cantMaxVertices];
        int[] costos = new int[cantMaxVertices];
        int[] vengo = new int[cantMaxVertices];
        for (int i = 0; i < cantMaxVertices; i++) {
            costos[i] = Integer.MAX_VALUE;
            vengo[i] = -1;
        }

        // Marcar el origen con costo cero
        costos[posOrigen] = 0;

        for (int i = 0; i < cantVertices; i++) {
            // 1) Obtener el vértice no visitado de menor costo
            int pos = obtenerVerticeNoVisitadoDeMenorCosto(visitados, costos);

            if (pos != -1) {
                // 2) Visitarlo
                visitados[pos] = true;

                // 3) Actualizar el costo de los adyacentes NO visitados
                for (int j = 0; j < cantMaxVertices; j++) {
                    if (!visitados[j] && aristas[pos][j].isExiste()) {
                        int peso = usarLatencia ? aristas[pos][j].getLatencia() : aristas[pos][j].getCosto();
                        int nuevoCosto = costos[pos] + peso;
                        if (nuevoCosto < costos[j]) {
                            costos[j] = nuevoCosto;
                            vengo[j] = pos;
                        }
                    }
                }
            }
        }

        if (costos[posDestino] == Integer.MAX_VALUE) {
            return null;
        }
        return new Tupla<>(costos[posDestino], armarCamino(vengo, posDestino));
    }

    private int obtenerVerticeNoVisitadoDeMenorCosto(boolean[] visitados, int[] costos) {
        int min = Integer.MAX_VALUE;
        int posMin = -1;
        for (int i = 0; i < cantMaxVertices; i++) {
            if (!visitados[i] && costos[i] < min) {
                min = costos[i];
                posMin = i;
            }
        }
        return posMin;
    }

    /*
     * Idea: vengo[] guarda desde dónde se llegó a cada vértice.
     * Se recorre del destino hacia atrás hasta el origen (vengo == -1).
     * La recursión arma primero el camino hasta el anterior y después agrega
     * el actual, así la ruta queda en orden origen -> destino.
     */
    private String armarCamino(int[] vengo, int pos) {
        if (vengo[pos] == -1) {
            return vertices[pos].toString();
        }
        return armarCamino(vengo, vengo[pos]) + "|" + vertices[pos];
    }

    private int obtenerPos(Region v) {
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] != null && vertices[i].equals(v)) {
                return i;
            }
        }
        return -1;
    }

    private int obtenerPosLibre() {
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] == null) {
                return i;
            }
        }
        return -1;
    }
}
