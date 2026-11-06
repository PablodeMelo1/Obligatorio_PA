package sistema;

import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test11RedRegionesPorCantidadDeSaltos {
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    void preparar() {
        s.inicializarSistema(10);
        // Orden de alta distinto del orden lexicográfico.
        for (String codigo : new String[]{"D", "B", "E", "A", "C"}) {
            s.registrarRegion(codigo, "Region " + codigo, "UY", "z1");
        }
    }

    private void enlace(String origen, String destino) {
        assertEquals(Retorno.ok(), s.registrarEnlace(origen, destino, 100, 50));
    }

    private void verificar(String origen, int saltos, String... codigos) {
        StringBuilder esperado = new StringBuilder();
        for (String codigo : codigos) {
            if (esperado.length() > 0) {
                esperado.append('|');
            }
            esperado.append(codigo).append(";Region ").append(codigo).append(";UY;z1");
        }
        assertEquals(Retorno.ok(esperado.toString()), s.redRegionesPorCantidadDeSaltos(origen, saltos));
    }

    @Test
    void ceroSaltosIncluyeSoloElOrigen() {
        enlace("A", "B");
        verificar("A", 0, "A");
    }

    @Test
    void origenAisladoSiempreFormaParteDelResultado() {
        verificar("C", 0, "C");
        verificar("C", Integer.MAX_VALUE, "C");
    }

    @Test
    void incluyeHastaElLimiteDeSaltosEIgnoraLatenciaYCosto() {
        enlace("A", "B");
        enlace("B", "C");
        enlace("C", "D");
        verificar("A", 1, "A", "B");
        verificar("A", 2, "A", "B", "C");
        verificar("A", 3, "A", "B", "C", "D");
        verificar("A", Integer.MAX_VALUE, "A", "B", "C", "D");
    }

    @Test
    void ciclosYCaminosAlternativosNoRepitenRegiones() {
        enlace("A", "B");
        enlace("A", "C");
        enlace("B", "C");
        enlace("B", "D");
        enlace("C", "D");
        verificar("D", 2, "A", "B", "C", "D");
        verificar("A", 100, "A", "B", "C", "D");
    }

    @Test
    void unCaminoLargoNoOcultaOtroMasCorto() {
        enlace("A", "B");
        enlace("B", "C");
        enlace("C", "D");
        enlace("A", "D");
        enlace("D", "E");
        verificar("A", 2, "A", "B", "C", "D", "E");
    }

    @Test
    void noIncluyeOtrasComponentesYPermiteRecorridosInversos() {
        enlace("A", "B");
        enlace("C", "D");
        verificar("B", 10, "A", "B");
        verificar("D", 10, "C", "D");
    }

    @Test
    void ordenLexicograficoConMayusculasMinusculasYNumeros() {
        for (String codigo : new String[]{"a", "A2", "A10"}) {
            s.registrarRegion(codigo, "Region " + codigo, "UY", "z1");
            enlace("B", codigo);
        }
        verificar("B", 1, "A10", "A2", "B", "a");
    }

    @Test
    void erroresYPrecedencia() {
        assertEquals(Retorno.Resultado.ERROR_1, s.redRegionesPorCantidadDeSaltos("A", -1).getResultado());
        assertEquals(Retorno.Resultado.ERROR_1, s.redRegionesPorCantidadDeSaltos(null, -1).getResultado());
        for (String invalido : new String[]{null, "", "  "}) {
            assertEquals(Retorno.Resultado.ERROR_2,
                    s.redRegionesPorCantidadDeSaltos(invalido, 0).getResultado());
        }
        assertEquals(Retorno.Resultado.ERROR_3, s.redRegionesPorCantidadDeSaltos("X", 1).getResultado());
        s.inicializarSistema(4);
        assertEquals(Retorno.Resultado.ERROR_3, s.redRegionesPorCantidadDeSaltos("A", 0).getResultado());
    }

    @Test
    void cadaConsultaUsaLaRedActualYSusPropiosVisitados() {
        enlace("A", "B");
        verificar("A", 1, "A", "B");
        verificar("C", 1, "C");
        enlace("B", "C");
        verificar("A", 2, "A", "B", "C");
        verificar("C", 1, "B", "C");
        verificar("A", 0, "A");
    }
}
