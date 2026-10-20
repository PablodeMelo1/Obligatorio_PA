package sistema;

import dominio.Despliegue;
import interfaz.*;

import abb.ABB;
import abb.ResultadoBusqueda;
import abb.comparadores.ComparadorDesplieguePorCodigo;

public class ImplementacionSistema implements Sistema  {

    private int maxRegiones;
    private final ABB<Despliegue> desplieguesPorId = new ABB<>();
    private final ABB<Despliegue> desplieguesPorCodigo =
            new ABB<>(new ComparadorDesplieguePorCodigo());


    @Override
    public Retorno inicializarSistema(int maxRegiones) {
        if (maxRegiones <= 3) {
            return Retorno.error1("La cantidad máxima de regiones debe ser mayor a 3");
        }

        this.maxRegiones = maxRegiones;
        desplieguesPorId.vaciar();
        desplieguesPorCodigo.vaciar();
        return Retorno.ok();
    }

    @Override
    public Retorno registrarDespliegue(String id, String codigo, String descripcion, boolean cuantizado, Modalidad modalidad) {
        if (esVacio(id) || esVacio(codigo) || esVacio(descripcion) || modalidad == null) {
            return Retorno.error1("El id, código y descripción no pueden ser vacíos y la modalidad no puede ser nula");
        }

        if (!codigo.matches("^[A-Z]{3}-[0-9]{3}-[A-Za-z0-9]{4}$")) {
            return Retorno.error2("El código no tiene un formato válido");
        }

        Despliegue despliegue = new Despliegue(id, codigo, descripcion, cuantizado, modalidad);
        if (desplieguesPorId.pertenece(despliegue) != null) {
            return Retorno.error3("Ya existe un despliegue con ese id");
        }

        if (desplieguesPorCodigo.pertenece(despliegue) != null) {
            return Retorno.error4("Ya existe un despliegue con ese código");
        }

        desplieguesPorId.insertar(despliegue);
        desplieguesPorCodigo.insertar(despliegue);
        return Retorno.ok();
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    @Override
    public Retorno buscarDesplieguePorId(String id) {
        if (esVacio(id)) {
            return Retorno.error1("El id no puede ser vacío ni nulo");
        }
        ResultadoBusqueda<Despliegue> busqueda =
                desplieguesPorId.buscar(new Despliegue(id, null, null, false, null));
        Despliegue despliegue = busqueda.getValor();
        if (despliegue == null) {
            return Retorno.error2("No existe un despliegue con ese id");
        }
        return Retorno.ok(busqueda.getVisitados(), despliegue.toString());
    }

    @Override
    public Retorno listarDesplieguesPorIdAscendente() {
        return Retorno.ok(desplieguesPorId.listarAscendente());
    }

    @Override
    public Retorno listarDesplieguesPorIdDescendente() {
        return Retorno.ok(desplieguesPorId.listarDescendente());
    }

    @Override
    public Retorno buscarDesplieguePorCodigo(String codigo) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno listarDesplieguesPorCodigoAscendente() {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno listarDesplieguesPorModalidad(Modalidad unaModalidad) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno registrarRegion(String codigo, String nombre, String pais, String zona) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno registrarEnlace(String codigoOrigen, String codigoDestino, int latencia, int costo) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno redRegionesPorCantidadDeSaltos(String codigoOrigen, int cantidad) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno rutaMenorLatencia(String codigoOrigen, String codigoDestino) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno rutaMenorCosto(String codigoOrigen, String codigoDestino) {
        return Retorno.noImplementada();
    }
}
