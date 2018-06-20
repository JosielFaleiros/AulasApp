package mobile.aulasapp.com.aulasapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;
import java.util.Date;

@DatabaseTable(tableName = "SCHEDULE")
public class Schedule {
    @DatabaseField(generatedId = true, columnName = "IDSCHEDULE")
    int id;

    @DatabaseField(columnName = "DAY")
    int day;

    @DatabaseField(columnName = "STARTHOUR")
    int startHour;
    @DatabaseField(columnName = "STARTMINUTE")
    int startMinute;

    @DatabaseField(columnName = "FINISHHOUR")
    int finishHour;
    @DatabaseField(columnName = "FINISHMINUTE")
    int finishMinute;

    @DatabaseField(foreign = true, columnName = "DISCIPLINE_IDDISCIPLINE")
    private Discipline discipline;

    public Schedule() {
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public Schedule setDiscipline(Discipline discipline) {
        this.discipline = discipline;
        return this;
    }

    public int getId() {
        return id;
    }

    public Schedule setId(int id) {
        this.id = id;
        return this;
    }

    public int getDay() {
        return day;
    }

    public Schedule setDay(int day) {
        this.day = day;
        return this;
    }

    public int getStartHour() {
        return startHour;
    }

    public Schedule setStartHour(int startHour) {
        this.startHour = startHour;
        return this;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public Schedule setStartMinute(int startMinute) {
        this.startMinute = startMinute;
        return this;
    }

    public int getFinishHour() {
        return finishHour;
    }

    public Schedule setFinishHour(int finishHour) {
        this.finishHour = finishHour;
        return this;
    }

    public int getFinishMinute() {
        return finishMinute;
    }

    public Schedule setFinishMinute(int finishMinute) {
        this.finishMinute = finishMinute;
        return this;
    }

    @Override
    public String toString() {
        Date startDate = new Date();
        startDate.setHours(startHour);
        startDate.setMinutes(startMinute);

        Date finishDate = new Date();
        finishDate.setHours(finishHour);
        finishDate.setMinutes(finishMinute);
        startDate.setDate(day);

        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDay = new SimpleDateFormat("dd");

        return formatDay.format(startDate) + " " + formater.format(startDate) + " - " + formater.format(finishDate);
    }
}
