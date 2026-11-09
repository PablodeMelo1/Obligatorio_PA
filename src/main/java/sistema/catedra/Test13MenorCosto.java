package sistema.catedra;

import interfaz.Retorno;
import sistema.ImplementacionSistema;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sistema.catedra.Salidas.*;

public class Test13MenorCosto {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(50);
    }

    @Test
    void costoMinimoError1() {
        retorno = s.rutaMenorCosto("", "R2");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.rutaMenorCosto("   ", "R2");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.rutaMenorCosto(null, "R2");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.rutaMenorCosto("R1", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.rutaMenorCosto("R1", "   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.rutaMenorCosto("R1", null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void costoMinimoError2() {
        s.registrarRegion("R2", "R2", "Pais2", "Zona2");
        retorno = s.rutaMenorCosto("R1", "R2");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void costoMinimoError3() {
        s.registrarRegion("R1", "R1", "Pais1", "Zona1");
        retorno = s.rutaMenorCosto("R1", "R2");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    void costoMinimoError4() {
        s.registrarRegion("R1", "R1", "Pais1", "Zona1");
        s.registrarRegion("R2", "R2", "Pais2", "Zona2");
        s.registrarRegion("R3", "R3", "Pais3", "Zona3");
        retorno = s.registrarEnlace("R1", "R2", 10, 10);

        retorno = s.rutaMenorCosto("R1", "R3");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

    @Test
    void costoMinimo() {
        s.registrarRegion("RA", "RA", "PaisA", "ZonaA");
        s.registrarRegion("RB", "RB", "PaisB", "ZonaB");
        s.registrarRegion("RC", "RC", "PaisC", "ZonaC");
        s.registrarRegion("RD", "RD", "PaisD", "ZonaD");
        s.registrarRegion("RE", "RE", "PaisE", "ZonaE");
        s.registrarRegion("RF", "RF", "PaisF", "ZonaF");
        s.registrarRegion("RG", "RG", "PaisG", "ZonaG");
        s.registrarRegion("RH", "RH", "PaisH", "ZonaH");

        retorno = s.registrarEnlace("RF", "RG", 10, 4);
        retorno = s.registrarEnlace("RF", "RD", 10, 1);
        retorno = s.registrarEnlace("RF", "RE", 10, 2);

        retorno = s.registrarEnlace("RG", "RH", 10, 7);
        retorno = s.registrarEnlace("RG", "RB", 10, 3);
        retorno = s.registrarEnlace("RG", "RD", 10, 1);

        retorno = s.registrarEnlace("RB", "RH", 10, 2);
        retorno = s.registrarEnlace("RB", "RD", 10, 2);
        retorno = s.registrarEnlace("RB", "RC", 10, 3);
        retorno = s.registrarEnlace("RB", "RA", 10, 7);

        retorno = s.registrarEnlace("RC", "RD", 10, 6);
        retorno = s.registrarEnlace("RC", "RA", 10, 2);
        retorno = s.registrarEnlace("RC", "RE", 10, 1);

        retorno = s.registrarEnlace("RE", "RD", 10, 4);

        retorno = s.rutaMenorCosto("RG", "RG");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorInteger());
        assertEquals(regionG, retorno.getValorString());

        retorno = s.rutaMenorCosto("RG", "RA");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(7, retorno.getValorInteger());
        assertEquals(regionG + separador + regionD + separador + regionF + separador + regionE + separador + regionC + separador + regionA, retorno.getValorString());

        retorno = s.rutaMenorCosto("RG", "RH");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(5, retorno.getValorInteger());
        assertEquals(regionG + separador + regionB + separador + regionH, retorno.getValorString());

        retorno = s.rutaMenorCosto("RG", "RD");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorInteger());
        assertEquals(regionG + separador + regionD, retorno.getValorString());

    }


}
