package sistema.catedra;

import interfaz.Retorno;
import sistema.ImplementacionSistema;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sistema.catedra.Salidas.*;

public class Test12LatenciaMinima {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(50);
    }

    @Test
    void latenciaMinimoError1() {
        retorno = s.rutaMenorLatencia("", "R2");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.rutaMenorLatencia("   ", "R2");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.rutaMenorLatencia(null, "R2");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.rutaMenorLatencia("R1", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.rutaMenorLatencia("R1", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.rutaMenorLatencia("R1", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void latenciaMinimaError2() {
        s.registrarRegion("R2", "R2", "Pais2", "Zona2");
        retorno = s.rutaMenorLatencia("R1", "R2");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void latenciaMinimaError3() {
        s.registrarRegion("R1", "R1", "Pais1", "Zona1");
        retorno = s.rutaMenorLatencia("R1", "R2");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    void latenciaMinimaError4() {
        s.registrarRegion("R1", "R1", "Pais1", "Zona1");
        s.registrarRegion("R2", "R2", "Pais2", "Zona2");
        s.registrarRegion("R3", "R3", "Pais3", "Zona3");
        retorno = s.registrarEnlace("R1", "R2", 10, 10);

        retorno = s.rutaMenorLatencia("R1", "R3");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

    @Test
    void latenciaMinima() {
        s.registrarRegion("RA", "RA", "PaisA", "ZonaA");
        s.registrarRegion("RB", "RB", "PaisB", "ZonaB");
        s.registrarRegion("RC", "RC", "PaisC", "ZonaC");
        s.registrarRegion("RD", "RD", "PaisD", "ZonaD");
        s.registrarRegion("RE", "RE", "PaisE", "ZonaE");
        s.registrarRegion("RF", "RF", "PaisF", "ZonaF");
        s.registrarRegion("RG", "RG", "PaisG", "ZonaG");
        s.registrarRegion("RH", "RH", "PaisH", "ZonaH");

        retorno = s.registrarEnlace("RF", "RG", 6, 10);
        retorno = s.registrarEnlace("RF", "RD", 3, 10);
        retorno = s.registrarEnlace("RF", "RE", 1, 10);

        retorno = s.registrarEnlace("RG", "RH", 14, 10);
        retorno = s.registrarEnlace("RG", "RB", 4, 10);
        retorno = s.registrarEnlace("RG", "RD", 1, 10);

        retorno = s.registrarEnlace("RB", "RH", 2, 10);
        retorno = s.registrarEnlace("RB", "RD", 9, 10);
        retorno = s.registrarEnlace("RB", "RC", 2, 10);
        retorno = s.registrarEnlace("RB", "RA", 1, 10);

        retorno = s.registrarEnlace("RC", "RD", 1, 10);
        retorno = s.registrarEnlace("RC", "RA", 4, 10);
        retorno = s.registrarEnlace("RC", "RE", 3, 10);

        retorno = s.registrarEnlace("RE", "RD", 1, 10);

        retorno = s.rutaMenorLatencia("RG", "RG");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorInteger());
        assertEquals(regionG, retorno.getValorString());

        retorno = s.rutaMenorLatencia("RG", "RF");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(3, retorno.getValorInteger());
        assertEquals(regionG + separador + regionD + separador + regionE + separador + regionF, retorno.getValorString());

        retorno = s.rutaMenorLatencia("RG", "RA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(5, retorno.getValorInteger());
        assertEquals(regionG + separador + regionB + separador + regionA, retorno.getValorString());

        retorno = s.rutaMenorLatencia("RG", "RH");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(6, retorno.getValorInteger());
        assertEquals(regionG + separador + regionB + separador + regionH, retorno.getValorString());

    }


}
