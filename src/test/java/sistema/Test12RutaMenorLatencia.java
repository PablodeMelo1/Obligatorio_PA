package sistema;

import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test12RutaMenorLatencia {
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    void preparar() {
        s.inicializarSistema(10);
        for (String codigo : new String[]{"A", "B", "C", "D", "E", "F", "G", "H"}) {
            s.registrarRegion(codigo, "Region " + codigo, "UY", "z1");
        }
        // Grafo de las notas de Dijkstra del profe (latencias). El costo es 1 en todos.
        s.registrarEnlace("C", "F", 4, 1);
        s.registrarEnlace("C", "A", 2, 1);
        s.registrarEnlace("A", "D", 2, 1);
        s.registrarEnlace("D", "F", 9, 1);
        s.registrarEnlace("D", "H", 8, 1);
        s.registrarEnlace("F", "G", 3, 1);
        s.registrarEnlace("G", "H", 7, 1);
        s.registrarEnlace("G", "E", 2, 1);
        s.registrarEnlace("E", "B", 4, 1);
        s.registrarEnlace("H", "B", 1, 1);
    }

    private String ruta(String... codigos) {
        StringBuilder ruta = new StringBuilder();
        for (String codigo : codigos) {
            if (ruta.length() > 0) {
                ruta.append('|');
            }
            ruta.append(codigo).append(";Region ").append(codigo).append(";UY;z1");
        }
        return ruta.toString();
    }

    @Test
    void ejemploDeLasNotasDelProfe() {
        // C -> A -> D -> H con latencia 12, igual que en las notas
        assertEquals(Retorno.ok(12, ruta("C", "A", "D", "H")), s.rutaMenorLatencia("C", "H"));
        // vengo(B) = E, vengo(E) = G, vengo(G) = F, vengo(F) = C
        assertEquals(Retorno.ok(13, ruta("C", "F", "G", "E", "B")), s.rutaMenorLatencia("C", "B"));
        assertEquals(Retorno.ok(9, ruta("C", "F", "G", "E")), s.rutaMenorLatencia("C", "E"));
    }

    @Test
    void elGrafoNoEsDirigido() {
        assertEquals(Retorno.ok(12, ruta("H", "D", "A", "C")), s.rutaMenorLatencia("H", "C"));
    }

    @Test
    void enlaceDirectoPeroMasLento() {
        // Hay enlace directo D-F (9), pero D -> A -> C -> F suma 8
        assertEquals(Retorno.ok(8, ruta("D", "A", "C", "F")), s.rutaMenorLatencia("D", "F"));
    }

    @Test
    void mismoOrigenYDestino() {
        assertEquals(Retorno.ok(0, ruta("C")), s.rutaMenorLatencia("C", "C"));
    }

    @Test
    void error4SinRuta() {
        s.registrarRegion("Z", "Region Z", "UY", "z1");
        assertEquals(Retorno.Resultado.ERROR_4, s.rutaMenorLatencia("C", "Z").getResultado());
        assertEquals(Retorno.Resultado.ERROR_4, s.rutaMenorLatencia("Z", "C").getResultado());
    }

    @Test
    void erroresYPrecedencia() {
        for (String invalido : new String[]{null, "", "  "}) {
            assertEquals(Retorno.Resultado.ERROR_1, s.rutaMenorLatencia(invalido, "C").getResultado());
            assertEquals(Retorno.Resultado.ERROR_1, s.rutaMenorLatencia("C", invalido).getResultado());
        }
        assertEquals(Retorno.Resultado.ERROR_1, s.rutaMenorLatencia("X", "").getResultado());
        assertEquals(Retorno.Resultado.ERROR_2, s.rutaMenorLatencia("X", "C").getResultado());
        assertEquals(Retorno.Resultado.ERROR_2, s.rutaMenorLatencia("X", "Y").getResultado());
        assertEquals(Retorno.Resultado.ERROR_3, s.rutaMenorLatencia("C", "X").getResultado());
    }
}
