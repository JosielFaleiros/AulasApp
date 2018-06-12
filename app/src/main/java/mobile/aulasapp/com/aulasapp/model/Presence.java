package mobile.aulasapp.com.aulasapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "PRESENCE")
public class Presence {
    @DatabaseField(generatedId = true, columnName = "IDPRESENCE")
    int id;
    @DatabaseField(columnName = "DATE")
    Date date;
    @DatabaseField(foreign = true, columnName = "DISCIPLINE_IDDISCIPLINE")
    private Discipline discipline;
    @DatabaseField(foreign = true, columnName = "SCHEDULE_IDSCHEDULE")
    private Schedule schedule;

    public Presence() {
    }

    public Presence(Date date) {
        this.date = date;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
