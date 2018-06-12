package mobile.aulasapp.com.aulasapp.model.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import mobile.aulasapp.com.aulasapp.model.Discipline;
import mobile.aulasapp.com.aulasapp.model.Presence;
import mobile.aulasapp.com.aulasapp.model.Schedule;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME    = "aulasapp.db";
    private static final int    DB_VERSION = 1;

    private static DatabaseHelper instance;

    private Context context;

    private Dao<Discipline, Integer> disciplineDao;
    private Dao<Presence, Integer> presenceDao;
    private Dao<Schedule, Integer> scheduleDao;

    public static DatabaseHelper getInstance(Context context){
        if (instance == null){
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    private DatabaseHelper(Context contexto) {
        super(contexto, DB_NAME, null, DB_VERSION);
        context = contexto;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, Discipline.class);

//            List<Tipo> listaTipos = carregaTiposIniciais(R.array.tipos_iniciais);
//
//            getTipoDao().create(listaTipos);

            TableUtils.createTable(connectionSource, Presence.class);

//            List<TipoContato> listaTiposContatos = carregaTiposContatosIniciais(R.array.tipos_contatos_iniciais);

//            getTipoContatoDao().create(listaTiposContatos);

            TableUtils.createTable(connectionSource, Schedule.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "onCreate", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Schedule.class,      true);
            TableUtils.dropTable(connectionSource, Presence.class,     true);
            TableUtils.dropTable(connectionSource, Discipline.class, true);

            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "onUpgrade", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Discipline, Integer> getDisciplineDao() throws SQLException {
        if (disciplineDao == null) {
            disciplineDao = getDao(Discipline.class);
        }
        return disciplineDao;
    }

    public Dao<Presence, Integer> getPresenceDao() throws SQLException {
        if (presenceDao == null) {
            presenceDao = getDao(Presence.class);
        }
        return presenceDao;
    }

    public Dao<Schedule, Integer> getScheduleDao() throws SQLException {
        if (scheduleDao == null) {
            scheduleDao = getDao(Schedule.class);
        }
        return scheduleDao;
    }

}
