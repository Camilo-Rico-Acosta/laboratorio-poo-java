import java.util.ArrayList;
import java.util.List;

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
