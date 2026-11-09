package sistema.catedra;

import interfaz.Modalidad;
import interfaz.Retorno;
import sistema.ImplementacionSistema;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sistema.catedra.Salidas.*;

public class Test06BuscarDesplieguePorCodigo {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void buscarDespliegueOk() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0002", "LLM-128-A2C4", "Modelo de 128B en producción", true, Modalidad.AUDIO);
        s.registrarDespliegue("DEP-0003", "LLM-037-A7DW", "Modelo de 37B en producción", true, Modalidad.OTROS);

        retorno = s.buscarDesplieguePorCodigo("LLM-070-A1B2");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorInteger());
        assertEquals(d001, retorno.getValorString());

        retorno = s.buscarDesplieguePorCodigo("LLM-128-A2C4");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(2, retorno.getValorInteger());
        assertEquals(d002, retorno.getValorString());

        retorno = s.buscarDesplieguePorCodigo("LLM-037-A7DW");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(2, retorno.getValorInteger());
        assertEquals(d003, retorno.getValorString());
    }

    @Test
    void buscarDespliegueError1() {
        retorno = s.buscarDesplieguePorCodigo("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.buscarDesplieguePorCodigo("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.buscarDesplieguePorCodigo(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);

        retorno = s.buscarDesplieguePorCodigo("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.buscarDesplieguePorCodigo("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.buscarDesplieguePorCodigo(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void buscarDespliegueError2() {
        retorno = s.buscarDesplieguePorCodigo("LLM-070-A1B2");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);

        retorno = s.buscarDesplieguePorCodigo("LLM-128-A2C4");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

    }
}
