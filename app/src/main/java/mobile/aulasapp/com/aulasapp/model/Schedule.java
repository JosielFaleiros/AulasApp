package mobile.aulasapp.com.aulasapp.model;

import java.util.Date;

public class Schedule {
    Date start;
    Date end;

    public Schedule() {
    }

    public Schedule(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
