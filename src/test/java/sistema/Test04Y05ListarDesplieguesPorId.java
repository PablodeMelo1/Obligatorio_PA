package sistema;

import interfaz.Modalidad;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test04Y05ListarDesplieguesPorId {
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    private void verificar(String ascendente, String descendente) {
        Retorno asc = s.listarDesplieguesPorIdAscendente();
        Retorno desc = s.listarDesplieguesPorIdDescendente();
        assertEquals(Retorno.Resultado.OK, asc.getResultado());
        assertEquals(Retorno.Resultado.OK, desc.getResultado());
        assertEquals(ascendente, asc.getValorString());
        assertEquals(descendente, desc.getValorString());
    }

    @Test
    void sistemaVacio() {
        verificar("", "");
    }

    @Test
    void unSoloDespliegueSinSeparadorFinal() {
        s.registrarDespliegue("DEP-1", "VIS-004-aB12", "Visión", false, Modalidad.VISION);
        String datos = "DEP-1;VIS-004-aB12;Visión;false;Visión";
        verificar(datos, datos);
    }

    @Test
    void ordenLexicograficoEnAmbasRamasIndependienteDelCodigo() {
        s.registrarDespliegue("DEP-2", "AUD-002-C3D4", "Audio", false, Modalidad.AUDIO);
        s.registrarDespliegue("DEP-10", "VIS-004-aB12", "Visión", false, Modalidad.VISION);
        s.registrarDespliegue("DEP-3", "LLM-070-A1B2", "Texto", true, Modalidad.TEXTO);
        String primero = "DEP-10;VIS-004-aB12;Visión;false;Visión";
        String segundo = "DEP-2;AUD-002-C3D4;Audio;false;Audio";
        String tercero = "DEP-3;LLM-070-A1B2;Texto;true;Texto";
        verificar(primero + "|" + segundo + "|" + tercero,
                tercero + "|" + segundo + "|" + primero);
        // Los recorridos no modifican el árbol.
        assertEquals(Retorno.Resultado.OK, s.buscarDesplieguePorId("DEP-2").getResultado());
        s.inicializarSistema(10);
        verificar("", "");
    }

    @Test
    void arbolDegenerado() {
        s.registrarDespliegue("A", "LLM-001-A001", "A", true, Modalidad.TEXTO);
        s.registrarDespliegue("B", "LLM-002-A002", "B", true, Modalidad.TEXTO);
        s.registrarDespliegue("C", "LLM-003-A003", "C", true, Modalidad.TEXTO);
        String a = "A;LLM-001-A001;A;true;Texto";
        String b = "B;LLM-002-A002;B;true;Texto";
        String c = "C;LLM-003-A003;C;true;Texto";
        verificar(a + "|" + b + "|" + c, c + "|" + b + "|" + a);
    }
}
