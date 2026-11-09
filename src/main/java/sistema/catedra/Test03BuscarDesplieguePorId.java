package sistema.catedra;

import interfaz.Modalidad;
import interfaz.Retorno;
import sistema.ImplementacionSistema;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sistema.catedra.Salidas.*;

public class Test03BuscarDesplieguePorId {
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

        retorno = s.buscarDesplieguePorId("DEP-0001");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorInteger());
        assertEquals(d001, retorno.getValorString());

        retorno = s.buscarDesplieguePorId("DEP-0002");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(2, retorno.getValorInteger());
        assertEquals(d002, retorno.getValorString());

        retorno = s.buscarDesplieguePorId("DEP-0003");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(3, retorno.getValorInteger());
        assertEquals(d003, retorno.getValorString());
    }

    @Test
    void buscarDespliegueError1() {
        retorno = s.buscarDesplieguePorId("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.buscarDesplieguePorId("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.buscarDesplieguePorId(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);

        retorno = s.buscarDesplieguePorId("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.buscarDesplieguePorId("   ");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.buscarDesplieguePorId(null);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void buscarDespliegueError2() {
        retorno = s.buscarDesplieguePorId("DEP-0001");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);

        retorno = s.buscarDesplieguePorId("DEP-0002");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

    }
}
