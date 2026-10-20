package abb.comparadores;

import dominio.Despliegue;
import java.util.Comparator;

public class ComparadorDesplieguePorCodigo implements Comparator<Despliegue> {
    @Override
    public int compare(Despliegue o1, Despliegue o2) {
        return o1.getCodigo().compareTo(o2.getCodigo());
    }
}
