public abstract class Sensor {
    private final String id;
    private volatile String ubicacion;
    private volatile boolean activo;

    public Sensor(String id, String ubicacion, boolean activo) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("El id no puede ser nulo o vacio");
        }

        validarUbicacion(ubicacion);

        this.id = id;
        this.ubicacion = ubicacion;
        this.activo = activo;
    }

    public abstract double tomarLectura();
    public abstract String evaluarEstado();

    public String getId() {return id;}
    public String getUbicacion() {return ubicacion;}
    public boolean isActivo() {return activo;}
    
    public void setUbicacion(String ubicacion) {
        validarUbicacion(ubicacion);
        this.ubicacion = ubicacion;
    }

    public void activar() {this.activo = true;}
    public void desactivar() {this.activo = false;}

    private void validarUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.isEmpty()) {
            throw new IllegalArgumentException("La ubicacion no puede ser nulo o vacio");
        }
    }
}
