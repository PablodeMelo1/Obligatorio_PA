package sistema.catedra;

import interfaz.Retorno;
import sistema.ImplementacionSistema;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test10RegistrarEnlace {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(15);
    }

    @Test
    void registrarConexionOk() {
        s.registrarRegion("us-east-1", "USE1", "EE. UU.", "Norte de Virginia");
        s.registrarRegion("us-east-2", "USE2", "EE. UU.", "Ohio");
        retorno = s.registrarEnlace("us-east-1", "us-east-2", 12, 10);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarConexionError1() {
        retorno = s.registrarEnlace("", "us-east-2", 12, 10);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEnlace("us-east-1", "", 12, 10);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEnlace("   ", "us-east-2", 12, 10);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEnlace("us-east-1", "   ", 12, 10);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEnlace(null, "us-east-2", 12, 10);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.registrarEnlace("us-east-1", null, 12, 10);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void registrarConexionError2() {
        s.registrarRegion("us-east-2", "USE2", "EE. UU.", "Ohio");
        retorno = s.registrarEnlace("us-east-1", "us-east-2", 12, 10);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

    }

    @Test
    void registrarConexionError3() {
        s.registrarRegion("us-east-1", "USE1", "EE. UU.", "Norte de Virginia");
        retorno = s.registrarEnlace("us-east-1", "us-east-2", 12, 10);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());

    }

    @Test
    void registrarConexionError4() {
        s.registrarRegion("us-east-1", "USE1", "EE. UU.", "Norte de Virginia");
        s.registrarRegion("us-east-2", "USE2", "EE. UU.", "Ohio");

        retorno = s.registrarEnlace("us-east-1", "us-east-2", 0, 10);
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());

        retorno = s.registrarEnlace("us-east-1", "us-east-2", -1, 10);
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());

        retorno = s.registrarEnlace("us-east-1", "us-east-2", -20, 10);
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

    @Test
    void registrarConexionError5() {
        s.registrarRegion("us-east-1", "USE1", "EE. UU.", "Norte de Virginia");
        s.registrarRegion("us-east-2", "USE2", "EE. UU.", "Ohio");

        retorno = s.registrarEnlace("us-east-1", "us-east-2", 12, 0);
        assertEquals(Retorno.Resultado.ERROR_5, retorno.getResultado());

        retorno = s.registrarEnlace("us-east-1", "us-east-2", 12, -1);
        assertEquals(Retorno.Resultado.ERROR_5, retorno.getResultado());

        retorno = s.registrarEnlace("us-east-1", "us-east-2", 12, -10);
        assertEquals(Retorno.Resultado.ERROR_5, retorno.getResultado());
    }

    @Test
    void registrarConexionError6() {
        s.registrarRegion("us-east-1", "USE1", "EE. UU.", "Norte de Virginia");
        s.registrarRegion("us-east-2", "USE2", "EE. UU.", "Ohio");
        s.registrarEnlace("us-east-1", "us-east-2", 12, 10);

        retorno = s.registrarEnlace("us-east-1", "us-east-2", 12, 10);
        assertEquals(Retorno.Resultado.ERROR_6, retorno.getResultado());
    }
}
