package sistema;

import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test13RutaMenorCosto {
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    void preparar() {
        s.inicializarSistema(10);
        for (int i = 1; i <= 7; i++) {
            s.registrarRegion("R" + i, "Region " + i, "UY", "z1");
        }
        // Grafo del MainDijkstra del profe (costos). La latencia es 1 en todos.
        s.registrarEnlace("R1", "R3", 1, 2);
        s.registrarEnlace("R1", "R5", 1, 6);
        s.registrarEnlace("R1", "R4", 1, 10);
        s.registrarEnlace("R2", "R3", 1, 2);
        s.registrarEnlace("R2", "R4", 1, 6);
        s.registrarEnlace("R2", "R5", 1, 1);
        s.registrarEnlace("R3", "R7", 1, 6);
        s.registrarEnlace("R4", "R7", 1, 3);
        s.registrarEnlace("R4", "R6", 1, 2);
        s.registrarEnlace("R5", "R6", 1, 2);
        s.registrarEnlace("R5", "R7", 1, 2);
    }

    private String ruta(int... numeros) {
        StringBuilder ruta = new StringBuilder();
        for (int n : numeros) {
            if (ruta.length() > 0) {
                ruta.append('|');
            }
            ruta.append("R").append(n).append(";Region ").append(n).append(";UY;z1");
        }
        return ruta.toString();
    }

    @Test
    void ejemploDelMainDijkstraDelProfe() {
        // De 1 a 4 el menor costo es 9: 1 -> 3 -> 2 -> 5 -> 6 -> 4
        assertEquals(Retorno.ok(9, ruta(1, 3, 2, 5, 6, 4)), s.rutaMenorCosto("R1", "R4"));
        assertEquals(Retorno.ok(5, ruta(1, 3, 2, 5)), s.rutaMenorCosto("R1", "R5"));
    }

    @Test
    void costoYLatenciaPuedenDarRutasDistintas() {
        s.inicializarSistema(10);
        s.registrarRegion("A", "Region A", "UY", "z1");
        s.registrarRegion("B", "Region B", "UY", "z1");
        s.registrarRegion("C", "Region C", "UY", "z1");
        s.registrarEnlace("A", "C", 5, 50);   // directo: rápido pero caro
        s.registrarEnlace("A", "B", 10, 1);
        s.registrarEnlace("B", "C", 10, 1);
        assertEquals(Retorno.ok(2, "A;Region A;UY;z1|B;Region B;UY;z1|C;Region C;UY;z1"),
                s.rutaMenorCosto("A", "C"));
        assertEquals(Retorno.ok(5, "A;Region A;UY;z1|C;Region C;UY;z1"),
                s.rutaMenorLatencia("A", "C"));
    }

    @Test
    void mismoOrigenYDestino() {
        assertEquals(Retorno.ok(0, ruta(4)), s.rutaMenorCosto("R4", "R4"));
    }

    @Test
    void error4SinRuta() {
        s.registrarRegion("R8", "Region 8", "UY", "z1");
        assertEquals(Retorno.Resultado.ERROR_4, s.rutaMenorCosto("R1", "R8").getResultado());
    }

    @Test
    void erroresYPrecedencia() {
        for (String invalido : new String[]{null, "", "  "}) {
            assertEquals(Retorno.Resultado.ERROR_1, s.rutaMenorCosto(invalido, "R1").getResultado());
            assertEquals(Retorno.Resultado.ERROR_1, s.rutaMenorCosto("R1", invalido).getResultado());
        }
        assertEquals(Retorno.Resultado.ERROR_2, s.rutaMenorCosto("X", "R1").getResultado());
        assertEquals(Retorno.Resultado.ERROR_3, s.rutaMenorCosto("R1", "X").getResultado());
    }

    @Test
    void reinicializarBorraLaRed() {
        s.inicializarSistema(10);
        assertEquals(Retorno.Resultado.ERROR_2, s.rutaMenorCosto("R1", "R4").getResultado());
    }
}
