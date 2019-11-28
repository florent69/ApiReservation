package api.reservation.reservation.dao;


import api.reservation.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationDao extends JpaRepository<Reservation, Integer> {

}
