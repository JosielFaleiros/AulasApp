package mobile.aulasapp.com.aulasapp.model;

import java.util.Date;

public class Presence {
    Date date;

    public Presence() {
    }

    public Presence(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
