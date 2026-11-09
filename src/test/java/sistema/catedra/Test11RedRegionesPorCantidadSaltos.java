package sistema.catedra;

import interfaz.Retorno;
import sistema.ImplementacionSistema;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sistema.catedra.Salidas.*;

public class Test11RedRegionesPorCantidadSaltos {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(30);
    }

    @Test
    void redRegionesPorCantidadSaltosError1() {
        s.registrarRegion("us-east-1", "USE1", "EE. UU.", "Norte de Virginia");

        retorno = s.redRegionesPorCantidadDeSaltos("us-east-1", -1);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.redRegionesPorCantidadDeSaltos("us-east-1", -5);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.redRegionesPorCantidadDeSaltos("us-east-1", -100);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void redRegionesPorCantidadSaltosError2() {
        retorno = s.redRegionesPorCantidadDeSaltos("", 3);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.redRegionesPorCantidadDeSaltos("   ", 3);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.redRegionesPorCantidadDeSaltos(null, 3);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void redRegionesPorCantidadSaltosError3() {
        retorno = s.redRegionesPorCantidadDeSaltos("us-east-1", 0);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());

        retorno = s.redRegionesPorCantidadDeSaltos("us-east-1", 5);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    void redRegionesPorCantidadSaltosSoloUnaCiudad() {
        s.registrarRegion("R1", "R1", "Pais1", "Zona1");

        retorno = s.redRegionesPorCantidadDeSaltos("R1", 0);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(region1, retorno.getValorString());

        retorno = s.redRegionesPorCantidadDeSaltos("R1", 5);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(region1, retorno.getValorString());

        retorno = s.redRegionesPorCantidadDeSaltos("R1", 10);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(region1, retorno.getValorString());
    }

    @Test
    void redRegionesPorCantidadSaltosDosCiudades() {
        s.registrarRegion("R1", "R1", "Pais1", "Zona1");
        s.registrarRegion("R2", "R2", "Pais2", "Zona2");
        retorno = s.registrarEnlace("R1", "R2", 10, 10);

        retorno = s.redRegionesPorCantidadDeSaltos("R1", 0);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(region1, retorno.getValorString());

        retorno = s.redRegionesPorCantidadDeSaltos("R1", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(region1 + separador + region2, retorno.getValorString());

        retorno = s.redRegionesPorCantidadDeSaltos("R1", 2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(region1 + separador + region2, retorno.getValorString());


    }

    @Test
    void redRegionesPorCantidadSaltosCompleto() {
        s.registrarRegion("R1", "R1", "Pais1", "Zona1");
        s.registrarRegion("R2", "R2", "Pais2", "Zona2");
        s.registrarRegion("R3", "R3", "Pais3", "Zona3");
        s.registrarRegion("R4", "R4", "Pais4", "Zona4");
        s.registrarRegion("R5", "R5", "Pais5", "Zona5");
        s.registrarRegion("R6", "R6", "Pais6", "Zona6");
        s.registrarRegion("R7", "R7", "Pais7", "Zona7");

        retorno = s.registrarEnlace("R1", "R3", 10, 10);
        retorno = s.registrarEnlace("R1", "R4", 10, 10);

        retorno = s.registrarEnlace("R3", "R2", 10, 10);
        retorno = s.registrarEnlace("R3", "R5", 10, 10);
        retorno = s.registrarEnlace("R3", "R4", 10, 10);

        retorno = s.registrarEnlace("R7", "R2", 10, 10);
        retorno = s.registrarEnlace("R7", "R5", 10, 10);
        retorno = s.registrarEnlace("R7", "R6", 10, 10);

        retorno = s.registrarEnlace("R6", "R4", 10, 10);

        retorno = s.redRegionesPorCantidadDeSaltos("R1", 0);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(region1, retorno.getValorString());

        retorno = s.redRegionesPorCantidadDeSaltos("R1", 1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(region1 + separador + region3 + separador + region4, retorno.getValorString());

        retorno = s.redRegionesPorCantidadDeSaltos("R1", 2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(region1 + separador + region2 + separador + region3 + separador + region4 + separador + region5 + separador + region6, retorno.getValorString());

        retorno = s.redRegionesPorCantidadDeSaltos("R1", 3);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(region1 + separador + region2 + separador + region3 + separador + region4 + separador + region5 + separador + region6 + separador + region7, retorno.getValorString());

        retorno = s.redRegionesPorCantidadDeSaltos("R1", 4);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(region1 + separador + region2 + separador + region3 + separador + region4 + separador + region5 + separador + region6 + separador + region7, retorno.getValorString());

    }
}
