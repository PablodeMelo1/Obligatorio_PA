package grafo;

public class Arista {
    private boolean existe;
    private int latencia;
    private int costo;

    public Arista() {
    }

    public Arista(int latencia, int costo) {
        this.latencia = latencia;
        this.costo = costo;
        this.existe = true;
    }

    public boolean isExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
        if (!existe) {
            latencia = 0;
            costo = 0;
        }
    }

    public int getLatencia() {
        return latencia;
    }

    public void setLatencia(int latencia) {
        this.latencia = latencia;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }
}
