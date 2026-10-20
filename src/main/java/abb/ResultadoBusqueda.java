package abb;

public class ResultadoBusqueda<T> {
    private final T valor;
    private final int visitados;

    ResultadoBusqueda(T valor, int visitados) {
        this.valor = valor;
        this.visitados = visitados;
    }

    public T getValor() {
        return valor;
    }

    public int getVisitados() {
        return visitados;
    }
}
