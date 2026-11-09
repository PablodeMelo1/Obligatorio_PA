package sistema.catedra;

import interfaz.Retorno;
import sistema.ImplementacionSistema;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test09RegistrarRegion {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(15);
    }

    @Test
    void registrarCiudadOk() {
        retorno = s.registrarRegion("us-east-1", "USE1", "EE. UU.", "Norte de Virginia");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarCiudadError1() {
        s.inicializarSistema(5);
        s.registrarRegion("us-east-1", "USE1", "EE. UU.", "Norte de Virginia");
        s.registrarRegion("us-east-2", "USE2", "EE. UU.", "Ohio");
        s.registrarRegion("us-west-1", "USW1", "EE. UU.", "Norte de California");
        s.registrarRegion("us-west-2", "USW2", "EE. UU.", "Oregón");
        s.registrarRegion("eu-west-1", "EUW1", "Irlanda", "Europa");

        retorno = s.registrarRegion("eu-west-2", "EUW2", "Londres", "Europa");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void registrarCiudadError2() {
        retorno = s.registrarRegion("", "USE1", "EE. UU.", "Norte de Virginia");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarRegion("us-east-1", "", "EE. UU.", "Norte de Virginia");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarRegion("us-east-1", "USE1", "", "Norte de Virginia");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarRegion("us-east-1", "USE1", "EE. UU.", "");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());


        retorno = s.registrarRegion("   ", "USE1", "EE. UU.", "Norte de Virginia");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarRegion("us-east-1", "   ", "EE. UU.", "Norte de Virginia");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarRegion("us-east-1", "USE1", "   ", "Norte de Virginia");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarRegion("us-east-1", "USE1", "EE. UU.", "  ");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());


        retorno = s.registrarRegion(null, "USE1", "EE. UU.", "Norte de Virginia");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarRegion("us-east-1", null, "EE. UU.", "Norte de Virginia");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarRegion("us-east-1", "USE1", null, "Norte de Virginia");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        retorno = s.registrarRegion("us-east-1", "USE1", "EE. UU.", null);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

    }

    @Test
    void registrarCiudadError3() {
        s.registrarRegion("us-east-1", "USE1", "EE. UU.", "Norte de Virginia");
        retorno = s.registrarRegion("us-east-1", "USE1", "EE. UU.", "Ohio");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

}
