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
