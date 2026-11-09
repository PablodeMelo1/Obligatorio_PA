package sistema.catedra;

import interfaz.Modalidad;
import interfaz.Retorno;
import sistema.ImplementacionSistema;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sistema.catedra.Salidas.*;

public class Test08ListarDesplieguesPorModalidad {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();


    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void listarVacio() {
        retorno = s.listarDesplieguesPorModalidad(Modalidad.TEXTO);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());

        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);
        retorno = s.listarDesplieguesPorModalidad(Modalidad.AUDIO);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    void listar1DespliegueTexto() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);
        retorno = s.listarDesplieguesPorModalidad(Modalidad.TEXTO);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(d001, retorno.getValorString());
    }

    @Test
    void listarSolo2DesplieguesOtros() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0002", "LLM-128-A2C4", "Modelo de 128B en producción", true, Modalidad.AUDIO);
        s.registrarDespliegue("DEP-0003", "LLM-037-A7DW", "Modelo de 37B en producción", true, Modalidad.OTROS);
        s.registrarDespliegue("DEP-0004", "LLM-002-R754", "Modelo de 2B en producción", true, Modalidad.OTROS);
        retorno = s.listarDesplieguesPorModalidad(Modalidad.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(d004 + separador + d003, retorno.getValorString());
    }


    @Test
    void listarPorModalidad() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0002", "LLM-128-A2C4", "Modelo de 128B en producción", true, Modalidad.AUDIO);
        s.registrarDespliegue("DEP-0003", "LLM-037-A7DW", "Modelo de 37B en producción", true, Modalidad.OTROS);
        s.registrarDespliegue("DEP-0004", "LLM-002-R754", "Modelo de 2B en producción", true, Modalidad.OTROS);

        retorno = s.listarDesplieguesPorModalidad(Modalidad.TEXTO);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(d001, retorno.getValorString());

        retorno = s.listarDesplieguesPorModalidad(Modalidad.AUDIO);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(d002, retorno.getValorString());

        retorno = s.listarDesplieguesPorModalidad(Modalidad.OTROS);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(d004 + separador + d003, retorno.getValorString());
    }


}
