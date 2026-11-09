package sistema.catedra;

import interfaz.Modalidad;
import interfaz.Retorno;
import sistema.ImplementacionSistema;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sistema.catedra.Salidas.*;

public class Test05ListarDesplieguesPorIDDescendente {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();


    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void listarVacio() {
        retorno = s.listarDesplieguesPorIdDescendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    void listar1Despliegue() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);
        retorno = s.listarDesplieguesPorIdDescendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(d001, retorno.getValorString());
    }

    @Test
    void listar2Despliegues() {
        s.registrarDespliegue("DEP-0001", "LLM-070-A1B2", "Modelo de 70B en producción", true, Modalidad.TEXTO);
        s.registrarDespliegue("DEP-0002", "LLM-128-A2C4", "Modelo de 128B en producción", true, Modalidad.AUDIO);
        retorno = s.listarDesplieguesPorIdDescendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(d002 + separador + d001, retorno.getValorString());
    }


}
