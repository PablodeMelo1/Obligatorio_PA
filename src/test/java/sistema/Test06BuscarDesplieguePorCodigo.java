package sistema;

import interfaz.Modalidad;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test06BuscarDesplieguePorCodigo {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void buscarDespliegueOk() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de texto 70B en producción", true, Modalidad.TEXTO);
        retorno = s.buscarDesplieguePorCodigo("LLM-070-A1B2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorInteger());
        assertEquals("DEP-0001;LLM-070-A1B2;Modelo de texto 70B en producción;true;Texto", retorno.getValorString());
    }

    @Test
    void rechazaCodigoVacioONulo() {
        for (String codigo : new String[]{null, "", "   "}) {
            assertEquals(Retorno.Resultado.ERROR_1, s.buscarDesplieguePorCodigo(codigo).getResultado());
        }
    }

    @Test
    void codigoInexistente() {
        assertEquals(Retorno.Resultado.ERROR_2, s.buscarDesplieguePorCodigo("LLM-070-A1B2").getResultado());
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Texto", true, Modalidad.TEXTO);
        assertEquals(Retorno.Resultado.ERROR_2, s.buscarDesplieguePorCodigo("LLM-070-A1B3").getResultado());
    }

    @Test
    void cuentaNodosSegunElArbolPorCodigoNoPorId() {
        // Los ids van en orden inverso a los códigos: el árbol por código tiene otra forma.
        s.registrarDespliegue("DEP-0004", "LLM-070-A1B2", "Raíz", true, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0003", "AUD-002-C3D4", "Audio", false, Modalidad.AUDIO);
        s.registrarDespliegue("DEP-0002", "VIS-004-aB12", "Visión", false, Modalidad.VISION);
        s.registrarDespliegue("DEP-0001", "MUL-010-X9Y8", "Multi", true, Modalidad.MULTIMODAL);
        // Árbol por código: LLM (raíz) -> AUD (izq), VIS (der) -> MUL (izq de VIS)
        assertEquals(1, s.buscarDesplieguePorCodigo("LLM-070-A1B2").getValorInteger());
        assertEquals(2, s.buscarDesplieguePorCodigo("AUD-002-C3D4").getValorInteger());
        assertEquals(2, s.buscarDesplieguePorCodigo("VIS-004-aB12").getValorInteger());
        assertEquals(3, s.buscarDesplieguePorCodigo("MUL-010-X9Y8").getValorInteger());
        assertEquals("DEP-0001;MUL-010-X9Y8;Multi;true;Multimodal",
                s.buscarDesplieguePorCodigo("MUL-010-X9Y8").getValorString());
    }

    @Test
    void reinicializarVaciaElArbolPorCodigo() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Texto", true, Modalidad.TEXTO);
        s.inicializarSistema(10);
        assertEquals(Retorno.Resultado.ERROR_2, s.buscarDesplieguePorCodigo("LLM-070-A1B2").getResultado());
    }
}
