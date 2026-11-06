package sistema;

import dominio.Despliegue;
import dominio.Region;
import interfaz.*;
import grafo.Grafo;
import grafo.Arista;
import abb.ABB;
import abb.ResultadoBusqueda;
import abb.comparadores.ComparadorDesplieguePorCodigo;

public class ImplementacionSistema implements Sistema {

    private int maxRegiones;
    private final ABB<Despliegue> desplieguesPorId = new ABB<>();
    private final ABB<Despliegue> desplieguesPorCodigo = new ABB<>(new ComparadorDesplieguePorCodigo());
    private final ABB<Despliegue>[] desplieguesPorModalidad = crearArbolesPorModalidad();
    private Grafo grafo;
  
    @Override
    public Retorno inicializarSistema(int maxRegiones) {
        if (maxRegiones <= 3) {
            return Retorno.error1("La cantidad máxima de regiones debe ser mayor a 3");
        }

        this.maxRegiones = maxRegiones;
        desplieguesPorId.vaciar();
        desplieguesPorCodigo.vaciar();
        grafo = new Grafo(maxRegiones, false);
        for (ABB<Despliegue> arbol : desplieguesPorModalidad) {
            arbol.vaciar();
        }
        return Retorno.ok();
    }

    @Override
    public Retorno registrarDespliegue(String id, String codigo, String descripcion, boolean cuantizado,
            Modalidad modalidad) {
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
        desplieguesPorModalidad[modalidad.getIndice()].insertar(despliegue);
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
        ResultadoBusqueda<Despliegue> busqueda = desplieguesPorId.buscar(new Despliegue(id, null, null, false, null));
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
        if (esVacio(codigo)) {
            return Retorno.error1("El código no puede ser vacío ni nulo");
        }
        ResultadoBusqueda<Despliegue> busqueda = desplieguesPorCodigo
                .buscar(new Despliegue(null, codigo, null, false, null));
        Despliegue despliegue = busqueda.getValor();
        if (despliegue == null) {
            return Retorno.error2("No existe un despliegue con ese código");
        }
        return Retorno.ok(busqueda.getVisitados(), despliegue.toString());
    }

    @Override
    public Retorno listarDesplieguesPorCodigoAscendente() {
        return Retorno.ok(desplieguesPorCodigo.listarAscendente());
    }

    @Override
    public Retorno listarDesplieguesPorModalidad(Modalidad unaModalidad) {
        if (unaModalidad == null) {
            return Retorno.ok("");
        }
        return Retorno.ok(desplieguesPorModalidad[unaModalidad.getIndice()].listarAscendente());
    }

    private ABB<Despliegue>[] crearArbolesPorModalidad() {
        ABB<Despliegue>[] arboles = new ABB[Modalidad.values().length];
        for (int i = 0; i < Modalidad.values().length; i++) {
            arboles[i] = new ABB<>(new ComparadorDesplieguePorCodigo());
        }
        return arboles;
    }

    @Override
    public Retorno registrarRegion(String codigo, String nombre, String pais, String zona) {
        if (grafo.estaLleno()) {
            return Retorno.error1("Se alcanzó la cantidad máxima de regiones");
        }
        if (esVacio(codigo) || esVacio(nombre) || esVacio(pais) || esVacio(zona)) {
            return Retorno.error2("El código, nombre, país y zona no pueden ser vacíos ni nulos");
        }
        Region region = new Region(codigo, nombre, pais, zona);
        if (grafo.existeVertice(region)) {
            return Retorno.error3("Ya existe una región con ese código");
        }
        grafo.agregarVertice(region);
        return Retorno.ok();
    }

    @Override
    public Retorno registrarEnlace(String codigoOrigen, String codigoDestino, int latencia, int costo) {
        if (esVacio(codigoOrigen) || esVacio(codigoDestino)) {
            return Retorno.error1("Los códigos no pueden ser vacíos ni nulos");
        }
        Region origen = new Region(codigoOrigen);
        Region destino = new Region(codigoDestino);
        if (!grafo.existeVertice(origen)) {
            return Retorno.error2("No existe la región de origen");
        }
        if (!grafo.existeVertice(destino)) {
            return Retorno.error3("No existe la región de destino");
        }
        if (latencia <= 0) {
            return Retorno.error4("La latencia debe ser mayor a cero");
        }
        if (costo <= 0) {
            return Retorno.error5("El costo debe ser mayor a cero");
        }
        if (grafo.existeArista(origen, destino)) {
            return Retorno.error6("Ya existe un enlace entre las regiones");
        }
        grafo.agregarArista(origen, destino, new Arista(latencia, costo));
        return Retorno.ok();
    }

    @Override
    public Retorno redRegionesPorCantidadDeSaltos(String codigoOrigen, int cantidad) {
        if (cantidad < 0) {
            return Retorno.error1("La cantidad de saltos no puede ser negativa");
        }
        if (esVacio(codigoOrigen)) {
            return Retorno.error2("El código no puede ser vacío ni nulo");
        }
        Region origen = new Region(codigoOrigen);
        if (!grafo.existeVertice(origen)) {
            return Retorno.error3("No existe la región de origen");
        }
        return Retorno.ok(grafo.redRegionesPorCantidadDeSaltos(origen, cantidad));
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
