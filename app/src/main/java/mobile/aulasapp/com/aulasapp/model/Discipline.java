package mobile.aulasapp.com.aulasapp.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "CLASS")
public class Discipline implements Serializable {
    @DatabaseField(generatedId = true, columnName = "IDDISCIPLINE")
    int id;
    @DatabaseField(columnName = "NAME")
    String name;
    @ForeignCollectionField
    private ForeignCollection<Schedule> schedules;
    @ForeignCollectionField
    private ForeignCollection<Presence> presences;

    public Discipline() {
    }

    public Discipline(String name) {
        this.name = name;
    }

    public ForeignCollection<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(ForeignCollection<Schedule> schedules) {
        this.schedules = schedules;
    }

    public ForeignCollection<Presence> getPresences() {
        return presences;
    }

    public void setPresences(ForeignCollection<Presence> presences) {
        this.presences = presences;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Discipline setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
