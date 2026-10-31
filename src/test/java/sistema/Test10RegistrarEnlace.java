package sistema;

import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test10RegistrarEnlace {
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    void preparar() {
        s.inicializarSistema(4);
        s.registrarRegion("A", "Region A", "UY", "z1");
        s.registrarRegion("B", "Region B", "AR", "z2");
    }

    @Test
    void enlaceEsNavegableEnAmbosSentidos() {
        assertEquals(Retorno.ok(), s.registrarEnlace("A", "B", 10, 5));
        String esperado = "A;Region A;UY;z1|B;Region B;AR;z2";
        assertEquals(Retorno.ok(esperado), s.redRegionesPorCantidadDeSaltos("A", 1));
        assertEquals(Retorno.ok(esperado), s.redRegionesPorCantidadDeSaltos("B", 1));
    }

    @Test
    void codigosNulosOVacios() {
        for (String invalido : new String[]{null, "", "  "}) {
            assertEquals(Retorno.Resultado.ERROR_1,
                    s.registrarEnlace(invalido, "B", 1, 1).getResultado());
            assertEquals(Retorno.Resultado.ERROR_1,
                    s.registrarEnlace("A", invalido, 1, 1).getResultado());
        }
    }

    @Test
    void regionesInexistentesYOrdenDeValidacion() {
        assertEquals(Retorno.Resultado.ERROR_2, s.registrarEnlace("X", "B", 1, 1).getResultado());
        assertEquals(Retorno.Resultado.ERROR_3, s.registrarEnlace("A", "X", 1, 1).getResultado());
        assertEquals(Retorno.Resultado.ERROR_2, s.registrarEnlace("X", "Y", 0, 0).getResultado());
        assertEquals(Retorno.Resultado.ERROR_3, s.registrarEnlace("A", "X", 0, 0).getResultado());
        assertEquals(Retorno.Resultado.ERROR_1, s.registrarEnlace("X", "", 0, 0).getResultado());
    }

    @Test
    void latenciaYCostoDebenSerPositivos() {
        for (int invalido : new int[]{0, -1, Integer.MIN_VALUE}) {
            assertEquals(Retorno.Resultado.ERROR_4,
                    s.registrarEnlace("A", "B", invalido, 1).getResultado());
            assertEquals(Retorno.Resultado.ERROR_5,
                    s.registrarEnlace("A", "B", 1, invalido).getResultado());
        }
        assertEquals(Retorno.Resultado.ERROR_4, s.registrarEnlace("A", "B", 0, 0).getResultado());
        assertEquals(Retorno.ok("A;Region A;UY;z1"), s.redRegionesPorCantidadDeSaltos("A", 1));
        assertEquals(Retorno.ok(), s.registrarEnlace("A", "B", 1, 1));
    }

    @Test
    void duplicadosEnAmbosSentidos() {
        s.registrarEnlace("A", "B", 10, 5);
        assertEquals(Retorno.Resultado.ERROR_6, s.registrarEnlace("A", "B", 20, 8).getResultado());
        assertEquals(Retorno.Resultado.ERROR_6, s.registrarEnlace("B", "A", 20, 8).getResultado());
        assertEquals(Retorno.Resultado.ERROR_4, s.registrarEnlace("A", "B", 0, 8).getResultado());
        assertEquals(Retorno.Resultado.ERROR_5, s.registrarEnlace("A", "B", 20, 0).getResultado());
    }

    @Test
    void enlaceDeUnaRegionASiMismaNoDuplicaElListado() {
        assertEquals(Retorno.ok(), s.registrarEnlace("A", "A", 1, 1));
        assertEquals(Retorno.Resultado.ERROR_6, s.registrarEnlace("A", "A", 1, 1).getResultado());
        assertEquals(Retorno.ok("A;Region A;UY;z1"), s.redRegionesPorCantidadDeSaltos("A", 10));
    }

    @Test
    void reinicializarBorraLosEnlaces() {
        s.registrarEnlace("A", "B", 10, 5);
        preparar();
        assertEquals(Retorno.ok("A;Region A;UY;z1"), s.redRegionesPorCantidadDeSaltos("A", 1));
        assertEquals(Retorno.ok(), s.registrarEnlace("B", "A", 20, 8));
    }
}
