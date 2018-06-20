package mobile.aulasapp.com.aulasapp;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mobile.aulasapp.com.aulasapp.model.Discipline;
import mobile.aulasapp.com.aulasapp.model.Schedule;
import mobile.aulasapp.com.aulasapp.model.persistence.DatabaseHelper;

public class ClassActivity extends AppCompatActivity {
    private static final String ID = "ID";
    private static final String MODO = "MODO";
    private static final int ALTER = 1;
    private static final int NEW = 2;
    private Dao<Discipline, Integer> disciplineDao;
    private List<Discipline> disciplineList;
    Button btnSave;
    private EditText etName;
    private int modo;
    private Discipline discipline;
    private ImageButton btnNewSchedule;
    private ListView lvSchedules;
    private Dao<Schedule, Integer> schedulesDao;
    private ArrayList<Schedule> schedulesList;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private Schedule schedule;
    private TextView tvNoSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        etName = findViewById(R.id.etDisciplineName);
        lvSchedules = findViewById(R.id.lv_schedules);
        tvNoSchedule = findViewById(R.id.tvNoSchedule);

        TextView tvNoSchedule = findViewById(R.id.tvNoSchedule);
        try {
            disciplineDao = DatabaseHelper.getInstance(this).getDisciplineDao();
            disciplineList = disciplineDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (disciplineList != null && disciplineList.size() > 0) {
            tvNoSchedule.setVisibility(View.GONE);
        }

        modo = getIntent().getIntExtra(MODO, NEW);

        if (modo == ALTER) {
            int id = getIntent().getIntExtra(ID, -1);
            if (id > 0) {
                try {
                    discipline = disciplineDao.queryForId(id);
                    populateView(discipline);

                    schedulesDao = DatabaseHelper.getInstance(this).getScheduleDao();
                    populateSchedules();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
//                populateView(discipline);
            }
        }

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getdataFromView();

                    if (!validInputs()) {
                        Toast.makeText(ClassActivity.this, "Insira um nome", Toast.LENGTH_SHORT).show();
                        throw new Exception("Invalid input");
                    }


                    if (modo == ALTER) {
                        disciplineDao.update(discipline);
                    } else {
                        disciplineDao.create(discipline);
                    }

                    Toast.makeText(ClassActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        btnNewSchedule = findViewById(R.id.btNewSchedule);
        btnNewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScheduleDialog();
            }
        });
    }

    private void startScheduleDialog() {
        if (builder == null) builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        if (inflater == null) inflater = (this).getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the
        // dialog layout
        if (schedule == null) schedule = new Schedule();
        schedule.setStartHour(-1);
        schedule.setFinishHour(-1);
        builder.setTitle("Horário");
        builder.setCancelable(false);
//        builder.setIcon(R.drawable.galleryalart);
        builder.setView(inflater.inflate(R.layout.schedule_creation_alert, null))
                // Add action buttons
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        schedule.setFinishHour(-1);
                        schedule.setStartHour(-1);
                    }
                })
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (discipline == null) {
                            Toast.makeText(ClassActivity.this, "Primeiro Crie uma disciplina!", Toast.LENGTH_SHORT).show();
                        } else if (!validAlertInputs()) {
                            Toast.makeText(ClassActivity.this, "Informe data inicio e fim!", Toast.LENGTH_SHORT).show();
                        } else {
                            discipline.getSchedules().add(schedule);
                            populateSchedules();
                        }
                    }
                });
        builder.create();
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        if (schedule == null || schedule.getStartHour() < 0 || schedule.getFinishHour() < 0) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }

        Button btnStartDate = alertDialog.findViewById(R.id.btnStartDate);
        Button btnFinishDate = alertDialog.findViewById(R.id.btnFinishDate);
        final TextView tvStart = alertDialog.findViewById(R.id.tvStartDate);
        final TextView tvFinish = alertDialog.findViewById(R.id.tvFinishDate);

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ClassActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (schedule == null) schedule = new Schedule();
                        schedule.setStartHour(selectedHour).setStartMinute(selectedMinute);
                        tvStart.setText(getStringTime(selectedHour, selectedMinute));

                        if (schedule.getFinishHour() >= 0) {
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Selecione o horário");
                mTimePicker.show();
            }
        });

        btnFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ClassActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (schedule == null) schedule = new Schedule();
                        schedule.setFinishHour(selectedHour).setFinishMinute(selectedMinute);
                        tvFinish.setText(getStringTime(selectedHour, selectedMinute));

                        if (schedule.getStartHour() >= 0) {
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Selecione o horário");
                mTimePicker.show();
            }
        });

    }

    private String getStringTime(int selectedHour, int selectedMinute) {
        Date date = new Date();
        date.setHours(selectedHour);
        date.setMinutes(selectedMinute);
        return new SimpleDateFormat("HH:mm").format(date);
    }

    private void populateSchedules() {
        schedulesList = new ArrayList<>(discipline.getSchedules());
        if (schedulesList.size() > 0) tvNoSchedule.setVisibility(View.GONE);
        else tvNoSchedule.setVisibility(View.VISIBLE);

        lvSchedules.setAdapter(new ArrayAdapter<Schedule>(this, android.R.layout.simple_list_item_1, schedulesList));
    }

    private boolean validAlertInputs() {
        if (schedule != null && schedule.getStartHour() >= 0 && schedule.getFinishHour() >= 0) {
            return true;
        }
        return false;
    }

    private boolean validInputs() {
        if (!etName.getText().toString().trim().isEmpty()) {
            return true;
        }
        etName.setError("Insira um texto");
        return false;
    }

    public void populateView(Discipline discipline) {
        etName.setText(discipline.getName());
    }


    public static void alter(Activity activity, Discipline discipline) {

        Intent intent = new Intent((Context) activity, ClassActivity.class);

        intent.putExtra(MODO, ALTER);
        intent.putExtra(ID, discipline.getId());

        activity.startActivityForResult(intent, ALTER);
    }

    public static void newDiscipline(Activity activity, int requestNewDiscipline) {

        Intent intent = new Intent(activity, ClassActivity.class);

        intent.putExtra(MODO, NEW);

        activity.startActivityForResult(intent, NEW);
    }

    public void getdataFromView() {
        if (discipline == null)
            discipline = new Discipline().setName( String.valueOf(etName.getText()).trim());
        else
            discipline.setName(String.valueOf(etName.getText()).trim());
    }
}
