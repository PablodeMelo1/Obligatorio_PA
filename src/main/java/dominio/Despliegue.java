package dominio;

import interfaz.Modalidad;

/**
 * Representa un despliegue de un modelo registrado en el sistema.
 */
public class Despliegue implements Comparable<Despliegue> {
    private final String id;
    private final String codigo;
    private final String descripcion;
    private final boolean cuantizado;
    private final Modalidad modalidad;

    public Despliegue(String id, String codigo, String descripcion, boolean cuantizado, Modalidad modalidad) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cuantizado = cuantizado;
        this.modalidad = modalidad;
    }

    @Override
    public String toString() {
        return id + ";" + codigo + ";" + descripcion + ";" + cuantizado + ";" + modalidad.getTexto();
    }

    @Override
    public int compareTo(Despliegue otro) {
        return id.compareTo(otro.id);
    }

    public String getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isCuantizado() {
        return cuantizado;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }
}
