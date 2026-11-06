package grafo;

public class Tupla<Q,V>{
    private Q dato1;
    private V dato2;

    public Tupla(Q dato1, V dato2) {
        this.dato1 = dato1;
        this.dato2 = dato2;
    }

    public Q getDato1() {
        return dato1;
    }

    public V getDato2() {
        return dato2;
    }
}
