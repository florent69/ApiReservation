package api.reservation.reservation.dto;

import java.util.Date;

public class ClientDto {
    private String username;
    private String firstName;
    private String lastname;
    private Date birthDate;
    private String license;
    private Date graduatedAt;

    public ClientDto() {
    }

    public ClientDto(String username, String firstName, String lastname, Date birthDate, String license, Date graduatedAt) {
        this.username = username;
        this.firstName = firstName;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.license = license;
        this.graduatedAt = graduatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Date getGraduatedAt() {
        return graduatedAt;
    }

    public void setGraduatedAt(Date graduatedAt) {
        this.graduatedAt = graduatedAt;
    }
}
