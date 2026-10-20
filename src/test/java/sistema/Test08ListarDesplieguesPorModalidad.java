package sistema;

import interfaz.Modalidad;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test08ListarDesplieguesPorModalidad {
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    private void verificar(Modalidad modalidad, String esperado) {
        Retorno retorno = s.listarDesplieguesPorModalidad(modalidad);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(esperado, retorno.getValorString());
    }

    @Test
    void sinDespliegues() {
        for (Modalidad m : Modalidad.values()) {
            verificar(m, "");
        }
    }

    @Test
    void soloListaLaModalidadPedidaOrdenadaPorCodigo() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Texto 70B", true, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0002", "AUD-002-C3D4", "Audio 2B", false, Modalidad.AUDIO);
        s.registrarDespliegue("DEP-0003", "GPT-175-Z9Z9", "Texto 175B", false, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0004", "VIS-004-aB12", "Visión 4B", false, Modalidad.VISION);
        s.registrarDespliegue("DEP-0005", "LLM-008-0000", "Texto 8B", true, Modalidad.TEXTO);

        verificar(Modalidad.TEXTO, "DEP-0003;GPT-175-Z9Z9;Texto 175B;false;Texto|"
                + "DEP-0005;LLM-008-0000;Texto 8B;true;Texto|"
                + "DEP-0001;LLM-070-A1B2;Texto 70B;true;Texto");
        verificar(Modalidad.AUDIO, "DEP-0002;AUD-002-C3D4;Audio 2B;false;Audio");
        verificar(Modalidad.VISION, "DEP-0004;VIS-004-aB12;Visión 4B;false;Visión");
        verificar(Modalidad.MULTIMODAL, "");
        verificar(Modalidad.OTROS, "");
    }

    @Test
    void registroFallidoNoQuedaEnLaModalidad() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Texto", true, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0001", "AUD-002-C3D4", "Repite id", false, Modalidad.AUDIO);      // ERROR_3
        s.registrarDespliegue("DEP-0002", "LLM-070-A1B2", "Repite código", false, Modalidad.OTROS);  // ERROR_4
        verificar(Modalidad.AUDIO, "");
        verificar(Modalidad.OTROS, "");
    }

    @Test
    void reinicializarVaciaTodasLasModalidades() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Texto", true, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0002", "AUD-002-C3D4", "Audio", false, Modalidad.AUDIO);
        s.inicializarSistema(10);
        verificar(Modalidad.TEXTO, "");
        verificar(Modalidad.AUDIO, "");
    }
}
