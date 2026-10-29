package dominio;

public class Region implements Comparable<Region> {
    private final String codigo;
    private final String nombre;
    private final String pais;
    private final String zona;

    public Region(String codigo, String nombre, String pais, String zona) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.pais = pais;
        this.zona = zona;
    }

    public Region(String codigo) {
        this(codigo, null, null, null);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }

    public String getZona() {
        return zona;
    }

    @Override
    public String toString() {
        return codigo + ";" + nombre + ";" + pais + ";" + zona;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return codigo.equals(region.codigo);
    }

    @Override
    public int compareTo(Region otra) {
        return codigo.compareTo(otra.codigo);
    }
}
