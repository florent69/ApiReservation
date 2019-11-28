package api.reservation.reservation.controller;

import api.reservation.reservation.dao.ReservationDao;
import api.reservation.reservation.dto.AverageDto;
import api.reservation.reservation.dto.ClientDto;
import api.reservation.reservation.dto.StartEndDatesDto;
import api.reservation.reservation.dto.CarDto;
import api.reservation.reservation.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static java.util.Calendar.*;

@RestController
public class ReservationController {

    @Autowired
    private ReservationDao reservationDao;

    //****************************** CRUD ***************************************************/
    @GetMapping(value = "/Reservation")
    public List<Reservation> reservationsList() {
        return reservationDao.findAll();
    }

    // affiche une réservation
    public Reservation displayReservation(@PathVariable int id) {
        return reservationDao.findById(id).orElse(null);
    }

    //ajoute une réservation
    @PostMapping(value = "/Reservation")
    public void addReservation(@RequestBody Reservation reservation) {
        reservationDao.save(reservation);
    }

    //modifie une réservation
    @PutMapping(value = "/Reservation/{id}")
    public Reservation updateReservation(@RequestBody Reservation reservationUp, @PathVariable int id) {
        Reservation reservationCurrent = this.displayReservation(id);

        if (reservationUp.getIdVehicle() != reservationCurrent.getIdVehicle()) {
            reservationCurrent.setIdVehicle(reservationUp.getIdVehicle());
        }

        if (reservationUp.getStartDate() != reservationCurrent.getStartDate()) {
            reservationCurrent.setStartDate(reservationUp.getStartDate());
        }

        if (reservationUp.getEndDate() != reservationCurrent.getEndDate()) {
            reservationCurrent.setEndDate(reservationUp.getEndDate());
        }
        reservationDao.save(reservationCurrent);
        return reservationCurrent;
    }

    @DeleteMapping(value = "Reservation/{id}")
    public void deleteReservation(@PathVariable int id) {
        Reservation reservation = this.displayReservation(id);
        reservationDao.delete(reservation);
    }

    //************************ Methods **************************************/
    // retourne toutes les réservations par rapport a un vehicle donné
    @GetMapping(value = "/Reservation/{idVehicle}")
    public List<Reservation> unavailableDateForOneCar(@PathVariable int idVehicle) {
        List<Reservation> reservationsList = reservationsList();
        List<Reservation> unavailabilityForOneCar = new ArrayList<>();
        for (Reservation x : reservationsList) {
            if (x.getIdVehicle() == idVehicle) {
                unavailabilityForOneCar.add(x);
            }
        }
        return unavailabilityForOneCar;
    }

    // retourne tous les vehicles disponibles par rapport à des dates de début et de fin
    @PostMapping(value = "/Reservation/CarAvailable/{idClient}")
    public List<CarDto> availableCarsList(@RequestBody StartEndDatesDto startEndDatesDto, @PathVariable int idClient) {
        List<Reservation> allReservationsList = reservationsList();
        List<CarDto> allCarsList = ageFilter(idClient);
        //---------------------------------------------//
        List<Integer> unavailableIdCarsList = new ArrayList<>();
        List<CarDto> availableCarsList = new ArrayList<>();

        for (Reservation reservation : allReservationsList) {
            Date startDateInput = startEndDatesDto.getStartDate();
            Date endDateInput = startEndDatesDto.getEndDate();
            if (
                    ((startDateInput.after(reservation.getStartDate()) || startDateInput.equals(reservation.getStartDate()))
                            && (startDateInput.before(reservation.getEndDate()) || startDateInput.equals(reservation.getEndDate()))
                    ) ||
                            ((endDateInput.after(reservation.getStartDate()) || endDateInput.equals(reservation.getStartDate()))
                                    && (endDateInput.before(reservation.getEndDate()) || endDateInput.equals(reservation.getStartDate()))
                            )
            ) {
                unavailableIdCarsList.add(reservation.getIdVehicle());
            }
        }
        for (CarDto car : allCarsList) {
            availableCarsList.add(car);
            for (int unavailableIdCar : unavailableIdCarsList)
                if (car.getId() == unavailableIdCar) {
                    availableCarsList.remove(car);
                }
        }
        return availableCarsList;
    }

    // retourne un message "est ce que je peux louer ce vehicule a cette date"
    @PostMapping(value = "/Reservation/Available/{idClient}/{idVehicle}")
    public String chooseAnotherCarOrNot(@PathVariable int idClient, @PathVariable int idVehicle, @RequestBody StartEndDatesDto startEndDatesDto) {
        List<CarDto> availabilityCars = availableCarsList(startEndDatesDto, idClient);
        String message = "vous pouvez louer ce vehicule de " + startEndDatesDto.getStartDate() + " à " + startEndDatesDto.getEndDate() + "!";
        for (CarDto availabilityCar : availabilityCars) {
            if (availabilityCar.getId() != idVehicle) {
                message = "vous ne pouvez pas louer ce vehicule de " + startEndDatesDto.getStartDate() + " à " + startEndDatesDto.getEndDate() + "!";
                break;
            }
        }
        return message;
    }

    //Un conducteur de moins de 21 ans ne peut pas louer un véhicule possédant 8 chevaux fiscaux ou plus.
    //Un conducteur de moins de 25 ans ne peut pas louer un véhicule possédant 13 chevaux fiscaux ou plus.
    @GetMapping(value = "/Reservation/AgeFilter/{idClient}")
    public List<CarDto> ageFilter(@PathVariable int idClient) {
        //Récupère la liste de tous les véhicules
        RestTemplate templateCar = new RestTemplate();
        CarDto[] allCarsAsArray = templateCar.getForObject("http://192.168.88.20:8080/car", CarDto[].class);
        List<CarDto> allCars = new ArrayList<>();
        allCars.addAll(Arrays.asList(allCarsAsArray));
        //Récupère un client
        RestTemplate templateClient = new RestTemplate();
        ClientDto Client = templateClient.getForObject("http://192.168.88.46:8080/user/" + idClient, ClientDto.class);
        Date today = new Date();
        Date birthDate = Client.getBirthDate();
        int age = getDiffYears(birthDate, today);
    //**********************************************************************************/
        List<CarDto> carAvail = new ArrayList<>();
        carAvail.addAll(allCars);
        for (CarDto car : allCars) {
            if (age <= 21) {
                if (car.getChevauxFiscaux() > 8) {
                    carAvail.remove(car);
                }
            } else if ( age<=25){
                if (car.getChevauxFiscaux()>13){
                    carAvail.remove(car);
                }
            }
        }
        return carAvail;
    }
    //
    @PostMapping(value = "/Reservation/locationPrice")
    public static int locationPrice(@RequestBody AverageDto average){
        CarDto car = average.getCar();
        int carPrixBase = car.getPrixBase();
        int carPrixKm = car.getPrixKm();
        int estimKm = average.getAverageKm();
        int locationPrice = carPrixBase + (estimKm*carPrixKm);
        return locationPrice;
    }

    //***************************FUNCTION FOR DATE**************************//
    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }
    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }
    //***********************************************/

}
