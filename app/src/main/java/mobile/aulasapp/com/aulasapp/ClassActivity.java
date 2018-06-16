package mobile.aulasapp.com.aulasapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import mobile.aulasapp.com.aulasapp.model.Discipline;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        etName = findViewById(R.id.etDisciplineName);

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

    public Discipline getdataFromView() {
        if (discipline == null)
            return new Discipline().setName( String.valueOf(etName.getText()));
        else
            return discipline.setName(String.valueOf(etName.getText()));
    }
}
