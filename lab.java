import java.util.ArrayList;
import java.util.List;

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

public class SensorHumedadSuelo extends Sensor{
    private double humedadPct;
    public double getHumedadPct(){return humedadPct;}

    public SensorHumedadSuelo(String id, String ubicacion, boolean activo) {
        super(id, ubicacion, activo);
    }

    @Override
    public double tomarLectura() {
        humedadPct = Math.random() * 100;
        return humedadPct;
    }

    @Override
    public String evaluarEstado() {
        if (humedadPct < 20) {
            return "CRITICO";
        }

        return "NORMAL";
    }
}

public class SensorTemperatura extends Sensor{
    private double celsius;
    public double getCelsius(){return celsius;}

    public SensorTemperatura(String id, String ubicacion, boolean activo) {
        super(id, ubicacion, activo);
    }

    @Override
    public double tomarLectura() {
        celsius = 15 + (Math.random() * 30);
        return celsius;
    }

    @Override
    public String evaluarEstado() {
        if (celsius > 38) {
            return "CRITICO";
        }

        return "NORMAL";
    }
}

public class EstacionMonitoreo {
    private final List<Sensor> sensores;

    public EstacionMonitoreo() {
        sensores = new ArrayList<>();
    }

    public void agregarSensor(Sensor sensor) {
        if (sensor == null) {
            throw new IllegalArgumentException("El sensor no puede ser nulo");
        }
        sensores.add(sensor);
    }

    public void procesarLecturas() {
        for (Sensor sensor : sensores) {
            if (sensor.isActivo()) {
                double lectura = sensor.tomarLectura();
                System.out.println(
                    "Sensor: " + sensor.getId()
                    + " | Ubicacion: " + sensor.getUbicacion()
                    + " | Lectura: " + lectura
                    + " | Estado: " + sensor.evaluarEstado()
                );
            }
        }
    }

    public List<Sensor> obtenerSensoresCriticos() {
        List<Sensor> sensoresCriticos = new ArrayList<>();
        for (Sensor sensor : sensores) {
            if (sensor.isActivo() && "CRITICO".equals(sensor.evaluarEstado())) {
                sensoresCriticos.add(sensor);
            }
        }
        return sensoresCriticos;
    }

    public List<Sensor> getSensores() {
        return new ArrayList<>(sensores);
    }
}
