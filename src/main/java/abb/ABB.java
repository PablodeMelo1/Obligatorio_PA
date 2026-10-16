package abb;

import java.util.Comparator;

/** Adaptado del proyecto ABB; incluye conteo de nodos visitados. */
public class ABB<T extends Comparable<T>> {

    private NodoABB<T> raiz;
    private final Comparator<T> comparador;

    public ABB(Comparator<T> comparador) {
        this.comparador = comparador;
    }

    public ABB() {
        this.comparador = null;
    }

    private int comparar(T dato1, T dato2) {
        if (this.comparador == null) {
            return dato1.compareTo(dato2);
        } else {
            return this.comparador.compare(dato1, dato2);
        }
    }


    public void insertar(T dato) {
        if (raiz == null) {
            raiz = new NodoABB<>(dato);
        } else {
            insertar(dato, raiz);
        }
    }

    private void insertar(T dato, NodoABB<T> nodo) {
        if (comparar(nodo.dato, dato) > 0) {
            if (nodo.izq == null) {
                nodo.izq = new NodoABB<>(dato);
            } else {
                insertar(dato, nodo.izq);
            }
        } else {
            if (nodo.der == null) {
                nodo.der = new NodoABB<>(dato);
            } else {
                insertar(dato, nodo.der);
            }
        }
    }

    public T pertenece(T x) {
        return buscar(x).getValor();
    }

    public ResultadoBusqueda<T> buscar(T x) {
        return buscar(x, raiz, 0);
    }

    private ResultadoBusqueda<T> buscar(T x, NodoABB<T> nodo, int visitados) {
        if (nodo == null) {
            return new ResultadoBusqueda<>(null, visitados);
        }
        int comparacion = comparar(nodo.dato, x);
        if (comparacion == 0) {
            return new ResultadoBusqueda<>(nodo.dato, visitados + 1);
        } else if (comparacion > 0) {
            return buscar(x, nodo.izq, visitados + 1);
        } else {
            return buscar(x, nodo.der, visitados + 1);
        }
    }

    public String listarAscendente() {
        StringBuilder listado = new StringBuilder();
        listarAscendente(raiz, listado);
        return listado.toString();
    }

    private void listarAscendente(NodoABB<T> nodo, StringBuilder listado) {
        if (nodo == null) {
            return;
        }
        listarAscendente(nodo.izq, listado);
        agregarAlListado(nodo.dato, listado);
        listarAscendente(nodo.der, listado);
    }

    public String listarDescendente() {
        StringBuilder listado = new StringBuilder();
        listarDescendente(raiz, listado);
        return listado.toString();
    }

    private void listarDescendente(NodoABB<T> nodo, StringBuilder listado) {
        if (nodo == null) {
            return;
        }
        listarDescendente(nodo.der, listado);
        agregarAlListado(nodo.dato, listado);
        listarDescendente(nodo.izq, listado);
    }

    private void agregarAlListado(T dato, StringBuilder listado) {
        if (listado.length() > 0) {
            listado.append('|');
        }
        listado.append(dato);
    }

    public void vaciar() {
        raiz = null;
    }

    private class NodoABB<T> {
        private T dato;
        private NodoABB<T> izq;
        private NodoABB<T> der;

        public NodoABB(T dato) {
            this.dato = dato;
        }
    }
}
