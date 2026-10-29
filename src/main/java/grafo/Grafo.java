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
