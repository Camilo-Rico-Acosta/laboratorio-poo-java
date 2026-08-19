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
