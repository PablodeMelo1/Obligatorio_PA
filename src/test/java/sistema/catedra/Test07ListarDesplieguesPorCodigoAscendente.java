package sistema.catedra;

import interfaz.Modalidad;
import interfaz.Retorno;
import sistema.ImplementacionSistema;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sistema.catedra.Salidas.*;

public class Test07ListarDesplieguesPorCodigoAscendente {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();


    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void listarVacio() {
        retorno = s.listarDesplieguesPorCodigoAscendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    void listar1Despliegue() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);
        retorno = s.listarDesplieguesPorCodigoAscendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(d001, retorno.getValorString());
    }

    @Test
    void listar2Despliegues() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0003", "LLM-037-A7DW", "Modelo de 37B en producción", true, Modalidad.OTROS);
        retorno = s.listarDesplieguesPorCodigoAscendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(d003 + separador + d001, retorno.getValorString());
    }
}
