
package sistema;

import interfaz.Modalidad;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test03BuscarDesplieguePorId {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void buscarDespliegueOk() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de texto 70B en producción", true, Modalidad.TEXTO);
        retorno = s.buscarDesplieguePorId("DEP-0001");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorInteger());
        assertEquals("DEP-0001;LLM-070-A1B2;Modelo de texto 70B en producción;true;Texto", retorno.getValorString());
    }

    @Test
    void rechazaIdVacioONulo() {
        for (String id : new String[]{null, "", "   "}) {
            assertEquals(Retorno.Resultado.ERROR_1, s.buscarDesplieguePorId(id).getResultado());
        }
    }

    @Test
    void idInexistente() {
        assertEquals(Retorno.Resultado.ERROR_2, s.buscarDesplieguePorId("DEP-0001").getResultado());
        s.registrarDespliegue("DEP-0002", "VIS-004-aB12", "Visión", false, Modalidad.VISION);
        assertEquals(Retorno.Resultado.ERROR_2, s.buscarDesplieguePorId("DEP-0001").getResultado());
    }

    @Test
    void cuentaNodosEnAmbasRamasYFormateaModalidad() {
        s.registrarDespliegue("DEP-0003", "LLM-070-A1B2", "Raíz", true, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0001", "AUD-002-C3D4", "Audio", false, Modalidad.AUDIO);
        s.registrarDespliegue("DEP-0004", "VIS-004-aB12", "Visión", false, Modalidad.VISION);
        s.registrarDespliegue("DEP-0002", "LLM-007-E5F6", "Texto", true, Modalidad.TEXTO);
        assertEquals(1, s.buscarDesplieguePorId("DEP-0003").getValorInteger());
        assertEquals(2, s.buscarDesplieguePorId("DEP-0001").getValorInteger());
        assertEquals(2, s.buscarDesplieguePorId("DEP-0004").getValorInteger());
        assertEquals(3, s.buscarDesplieguePorId("DEP-0002").getValorInteger());
        assertEquals("DEP-0004;VIS-004-aB12;Visión;false;Visión",
                s.buscarDesplieguePorId("DEP-0004").getValorString());
    }

    @Test
    void reinicializarEliminaDesplieguesDeAmbosIndices() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Texto", true, Modalidad.TEXTO);
        s.inicializarSistema(10);
        assertEquals(Retorno.Resultado.ERROR_2, s.buscarDesplieguePorId("DEP-0001").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Texto", true, Modalidad.TEXTO).getResultado());
    }

}
