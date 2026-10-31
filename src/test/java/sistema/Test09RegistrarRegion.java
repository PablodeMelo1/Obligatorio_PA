package sistema;

import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test09RegistrarRegion {
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    void preparar() {
        s.inicializarSistema(4);
    }

    @Test
    void registraHastaLaCapacidadMaxima() {
        for (int i = 0; i < 4; i++) {
            assertEquals(Retorno.ok(), s.registrarRegion("R" + i, "Region " + i, "UY", "z1"));
        }
        assertEquals(Retorno.Resultado.ERROR_1,
                s.registrarRegion("R4", "Region 4", "UY", "z1").getResultado());
    }

    @Test
    void validaTodosLosCamposSinConsumirCapacidad() {
        for (String invalido : new String[]{null, "", "  "}) {
            assertEquals(Retorno.Resultado.ERROR_2,
                    s.registrarRegion(invalido, "Nombre", "UY", "z1").getResultado());
            assertEquals(Retorno.Resultado.ERROR_2,
                    s.registrarRegion("A", invalido, "UY", "z1").getResultado());
            assertEquals(Retorno.Resultado.ERROR_2,
                    s.registrarRegion("A", "Nombre", invalido, "z1").getResultado());
            assertEquals(Retorno.Resultado.ERROR_2,
                    s.registrarRegion("A", "Nombre", "UY", invalido).getResultado());
        }
        registraHastaLaCapacidadMaxima();
    }

    @Test
    void codigoUnicoAunqueCambienLosDemasDatos() {
        assertEquals(Retorno.ok(), s.registrarRegion("A", "Original", "UY", "z1"));
        assertEquals(Retorno.Resultado.ERROR_3,
                s.registrarRegion("A", "Otro", "AR", "z2").getResultado());
        assertEquals(Retorno.ok("A;Original;UY;z1"), s.redRegionesPorCantidadDeSaltos("A", 0));
        assertEquals(Retorno.ok(), s.registrarRegion("B", "Original", "UY", "z1"));
    }

    @Test
    void capacidadTienePrioridadSobreLosDemasErrores() {
        for (int i = 0; i < 4; i++) {
            s.registrarRegion("R" + i, "Region", "UY", "z1");
        }
        assertEquals(Retorno.Resultado.ERROR_1, s.registrarRegion(null, null, null, null).getResultado());
        assertEquals(Retorno.Resultado.ERROR_1,
                s.registrarRegion("R0", "Region", "UY", "z1").getResultado());
    }

    @Test
    void reinicializarLiberaCapacidadYPermiteReutilizarCodigos() {
        s.registrarRegion("A", "Vieja", "UY", "z1");
        assertEquals(Retorno.ok(), s.inicializarSistema(5));
        assertEquals(Retorno.ok(), s.registrarRegion("A", "Nueva", "AR", "z2"));
        assertEquals(Retorno.ok("A;Nueva;AR;z2"), s.redRegionesPorCantidadDeSaltos("A", 0));
        for (int i = 0; i < 4; i++) {
            assertEquals(Retorno.ok(), s.registrarRegion("R" + i, "Region", "UY", "z1"));
        }
    }
}
