package api.reservation.reservation.dto;

import java.util.Date;

public class StartEndDatesDto {
    private Date startDate;
    private Date endDate;

    public StartEndDatesDto() {
    }

    public StartEndDatesDto(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
