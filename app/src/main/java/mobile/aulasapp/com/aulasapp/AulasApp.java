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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    private ArrayAdapter<Discipline> listaAdapter;

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

        popularLista();

    }

    private void popularLista() {

        List<Discipline> lista = null;

        try {
            DatabaseHelper conexao = DatabaseHelper.getInstance(this);

            lista = conexao.getDisciplineDao()
                    .queryBuilder()
                    .orderBy("NAME", true)
                    .query();
        } catch (SQLException e) {
            Log.e("popularLista()", e.getMessage(), e);
            return;
        }

        listaAdapter = new ArrayAdapter<Discipline>(this,
                android.R.layout.simple_list_item_1,
                lista);

        lvDisciplines.setAdapter(listaAdapter);
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
