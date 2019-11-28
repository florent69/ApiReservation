package api.reservation.reservation.dto;

public class AverageDto {
    private int averageKm;
    private CarDto Car;

    public AverageDto() {
    }

    public AverageDto(int averageKm) {
        this.averageKm = averageKm;
    }

    public int getAverageKm() {
        return averageKm;
    }

    public void setAverageKm(int averageKm) {
        this.averageKm = averageKm;
    }

    public CarDto getCar() {
        return Car;
    }

    public void setCar(CarDto car) {
        Car = car;
    }
}
