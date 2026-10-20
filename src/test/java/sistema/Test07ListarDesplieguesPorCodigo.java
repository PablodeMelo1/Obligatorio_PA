package sistema;

import interfaz.Modalidad;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test07ListarDesplieguesPorCodigo {
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    private void verificar(String esperado) {
        Retorno retorno = s.listarDesplieguesPorCodigoAscendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void sistemaVacio() {
        verificar("");
    }

    @Test
    void ejemploDeLaLetra() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de texto 70B", true, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0002", "AUD-002-C3D4", "Modelo de audio 2B", false, Modalidad.AUDIO);
        verificar("DEP-0002;AUD-002-C3D4;Modelo de audio 2B;false;Audio|"
                + "DEP-0001;LLM-070-A1B2;Modelo de texto 70B;true;Texto");
    }

    @Test
    void ordenaPorCodigoAunqueElIdDigaOtraCosa() {
        s.registrarDespliegue("A", "VIS-004-aB12", "V", false, Modalidad.VISION);
        s.registrarDespliegue("B", "AUD-002-C3D4", "Au", false, Modalidad.AUDIO);
        s.registrarDespliegue("C", "LLM-070-A1B2", "T", true, Modalidad.TEXTO);
        verificar("B;AUD-002-C3D4;Au;false;Audio|C;LLM-070-A1B2;T;true;Texto|A;VIS-004-aB12;V;false;Visión");
    }

    @Test
    void mayusculasAntesQueMinusculasEnElBuild() {
        // Orden lexicográfico de Java: 'Z' < 'a'
        s.registrarDespliegue("A", "LLM-070-abcd", "min", true, Modalidad.TEXTO);
        s.registrarDespliegue("B", "LLM-070-ZZZZ", "may", true, Modalidad.TEXTO);
        verificar("B;LLM-070-ZZZZ;may;true;Texto|A;LLM-070-abcd;min;true;Texto");
    }
}
