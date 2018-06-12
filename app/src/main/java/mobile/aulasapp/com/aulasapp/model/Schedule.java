package mobile.aulasapp.com.aulasapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "SCHEDULE")
public class Schedule {
    @DatabaseField(generatedId = true, columnName = "IDSCHEDULE")
    int id;
    @DatabaseField(columnName = "VALOR")
    private String valor;
    @DatabaseField(columnName = "START")
    Date start;
    @DatabaseField(columnName = "END")
    Date end;
    @DatabaseField(foreign = true, columnName = "DISCIPLINE_IDDISCIPLINE")
    private Discipline discipline;

    public Schedule() {
    }

    public Schedule(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
