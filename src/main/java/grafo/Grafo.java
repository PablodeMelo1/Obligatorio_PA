package grafo;

import dominio.Region;

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

    /** Recibe un origen existente y una cantidad no negativa, validados por el sistema. */
    public String redRegionesPorCantidadDeSaltos(Region origen, int cantidad) {
        int[] distancias = new int[cantMaxVertices];
        for (int i = 0; i < distancias.length; i++) {
            distancias[i] = -1;
        }

        // Cola FIFO: cada vértice se encola una sola vez, al descubrirlo.
        int[] cola = new int[cantVertices];
        int inicio = 0;
        int fin = 0;
        int posOrigen = obtenerPos(origen);
        distancias[posOrigen] = 0;
        cola[fin++] = posOrigen;

        while (inicio < fin) {
            int actual = cola[inicio++];
            if (distancias[actual] == cantidad) {
                continue;
            }
            for (int vecino = 0; vecino < vertices.length; vecino++) {
                if (vertices[vecino] != null && aristas[actual][vecino].isExiste()
                        && distancias[vecino] == -1) {
                    distancias[vecino] = distancias[actual] + 1;
                    cola[fin++] = vecino;
                }
            }
        }

        // Finalizado el BFS, la cola contiene exactamente las regiones alcanzadas.
        // Se ordenan sus índices por código mediante inserción.
        for (int i = 1; i < fin; i++) {
            int posicion = cola[i];
            int j = i - 1;
            while (j >= 0 && vertices[cola[j]].compareTo(vertices[posicion]) > 0) {
                cola[j + 1] = cola[j];
                j--;
            }
            cola[j + 1] = posicion;
        }

        StringBuilder listado = new StringBuilder();
        for (int i = 0; i < fin; i++) {
            if (i > 0) {
                listado.append('|');
            }
            listado.append(vertices[cola[i]]);
        }
        return listado.toString();
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
