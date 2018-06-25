package mobile.aulasapp.com.aulasapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import mobile.aulasapp.com.aulasapp.model.Discipline;
import mobile.aulasapp.com.aulasapp.model.persistence.DatabaseHelper;

public class AulasApp extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_NEW_DISCIPLINE = 1;
    private static final int REQUEST_EDIT_DISCIPLINE = 2;
    private ListView lvDisciplines;
    private ArrayAdapter<Discipline> disciplineListAdapter;
    private List<Discipline> disciplineList;
    private Dao<Discipline, Integer> disciplineDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aulas_app);

        lvDisciplines = findViewById(R.id.lv_disciplines);

        lvDisciplines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Discipline discipline = (Discipline) parent.getItemAtPosition(position);

                ClassActivity.alter(AulasApp.this, discipline);
            }
        });

        registerForContextMenu(lvDisciplines);

        popularLista();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.item_selected, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Discipline discipline = disciplineList.get(info.position);
        if (item.getItemId() == R.id.itemDeleteCtx) {
            deleteDiscipline(discipline);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    private void deleteDiscipline(Discipline discipline) {
        try {
            int result = disciplineDao.delete(discipline);
            if (result <= 0) throw new SQLException();
            else Toast.makeText(this, "Aula apagada com sucesso!", Toast.LENGTH_SHORT).show();
            popularLista();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro, Tente novamente", Toast.LENGTH_SHORT).show();
        }
    }

    private void popularLista() {

        try {
            DatabaseHelper conexao = DatabaseHelper.getInstance(this);
            disciplineDao = conexao.getDisciplineDao();
            disciplineList = disciplineDao
                    .queryBuilder()
                    .orderBy("NAME", true)
                    .query();
        } catch (SQLException e) {
            Log.e("popularLista()", e.getMessage(), e);
            return;
        }

        disciplineListAdapter = new ArrayAdapter<Discipline>(this,
                android.R.layout.simple_list_item_1,
                disciplineList);

        lvDisciplines.setAdapter(disciplineListAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.aulas_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuItemNew) {
            ClassActivity.newDiscipline(this, REQUEST_NEW_DISCIPLINE);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_NEW_DISCIPLINE || requestCode == REQUEST_EDIT_DISCIPLINE)
                && resultCode == Activity.RESULT_OK){

            popularLista();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        return true;
    }
}
