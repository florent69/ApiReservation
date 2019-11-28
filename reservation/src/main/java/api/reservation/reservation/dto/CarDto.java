package api.reservation.reservation.dto;

public class CarDto {

    private int id;
    private String brand;
    private String model;
    private String imat;
    private String color;
    private int prixBase;
    private int prixKm;
    private int chevauxFiscaux;


    public CarDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImat() {
        return imat;
    }

    public void setImat(String imat) {
        this.imat = imat;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(int prixBase) {
        this.prixBase = prixBase;
    }

    public int getPrixKm() {
        return prixKm;
    }

    public void setPrixKm(int prixKm) {
        this.prixKm = prixKm;
    }

    public int getChevauxFiscaux() {
        return chevauxFiscaux;
    }

    public void setChevauxFiscaux(int chevauxFiscaux) {
        this.chevauxFiscaux = chevauxFiscaux;
    }
}
