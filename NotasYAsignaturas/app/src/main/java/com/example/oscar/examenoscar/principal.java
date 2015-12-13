package com.example.oscar.examenoscar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class principal extends AppCompatActivity implements ListView.OnItemClickListener, View.OnKeyListener {

    //Array de integer con las notas
    Integer [] notas = {0,0,0,0,0};
    //Array de string con las asignaturas
    String [] asignaturas ={"Programación multimedia y dispositivos móviles.","Acceso a datos","Sistemas de gestión empresaria","Programación de servicios y procesos","Desarrollo de interfaces"};

    //Variables globales del programa
    ListView listaAsignaturas;
    TextView notaTextView;
    ActionMode mActionMode;

    //Variable para no perder la posición seleccionada
    int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //Declaramos el adaptador
        ArrayAdapter<String> adaptador;

        //Cargamos el ListView
        listaAsignaturas =(ListView)findViewById(R.id.listView);

        //Creamos el editText y lo referenciamos
        EditText mostrarnota = (EditText)findViewById(R.id.nota);
        mostrarnota.setOnKeyListener(this);
        mostrarnota.setVisibility(View.INVISIBLE);
        //Creamos el adaptador
        adaptador = new ArrayAdapter<String>(this,R.layout.fila,asignaturas);

        //Pasamos el adaptador a la lista
        listaAsignaturas.setAdapter(adaptador);

        //Ponemos que el ListView que cuando se haga clic lo escuche y lo recoga desde esta clase
        listaAsignaturas.setOnItemClickListener(this);

        //Para el menu
        registerForContextMenu(listaAsignaturas);
    }

    //Crear el menu para meterle datos
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        MenuInflater m =getMenuInflater();
        m.inflate(R.menu.menu_contextual,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        posicion = info.position;
        mActionMode = principal.this.startActionMode(mActionModeCallback);

        //Comprobamos lo seleccionado
        switch (item.getItemId()) {
            case R.id.sumar:
                sumar(info.position);
                return true;
            case R.id.restar:
                restar(info.position);
                return true;
            default:

                return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Muestra abajo a la izquierda el nombre de la asignatura
        TextView t = (TextView)findViewById(R.id.asignaturaNota);
        t.setVisibility(view.VISIBLE);
        t.setText((parent.getItemAtPosition(position).toString()));

        //Guardamos la posición seleccionada
        posicion = position;

        //Muestra abajo a la derecha la nota
        EditText d = (EditText)findViewById(R.id.nota);
        d.setVisibility(view.VISIBLE);
        d.setText("" + notas[position]);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        EditText d = (EditText)findViewById(R.id.nota);

        TextView t = (TextView)findViewById(R.id.asignaturaNota);

        d.setVisibility(v.VISIBLE);
        t.setVisibility(v.VISIBLE);

        if(d.getText().toString().equals("")){

        }else{
            int nuevaNota = Integer.parseInt(d.getText().toString());
            if(nuevaNota<=10 && nuevaNota>=0){
                notas[posicion] = nuevaNota;
            }else{
                Toast.makeText(getApplicationContext(), "Nota mal introducida ", Toast.LENGTH_LONG).show();
                notas[posicion] = 0;
                d.setText("" + notas[posicion]);
            }
        }
        return false;
    }

    //ActionBar
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_contextual, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            //Comprobamos lo seleccionado
            switch (item.getItemId()) {
                case R.id.sumar:
                    sumar(posicion);
                    return true;
                case R.id.restar:
                    restar(posicion);
                    return true;
                default:
                    return false;
            }

        }

    // Called when the user exits the action mode
    @Override
    public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }};


    //Métodos para sumar y restar notas
    public void sumar(int asignatura){
        EditText d = (EditText)findViewById(R.id.nota);
        TextView t = (TextView)findViewById(R.id.asignaturaNota);

        d.setVisibility(View.VISIBLE);
        t.setVisibility(View.VISIBLE);

        //Comprobaciones para que la nota al sumarla no pase de diez
        if(notas[asignatura]<10){
            notas[asignatura] = notas[asignatura]+1;
            d.setText(notas[asignatura].toString());
            t.setText(asignaturas[asignatura]);
        }else{
            Toast.makeText(getApplicationContext(), "No es posible sumar mas nota ", Toast.LENGTH_LONG).show();
            d.setText(notas[asignatura].toString());
            t.setText(asignaturas[asignatura]);
        }
    }
    public void restar(int asignatura){
        EditText d = (EditText)findViewById(R.id.nota);
        TextView t = (TextView)findViewById(R.id.asignaturaNota);

        d.setVisibility(View.VISIBLE);
        t.setVisibility(View.VISIBLE);

        //Comprobaciones de la nota restada para que no pase de cero
        if(notas[asignatura]>0) {
            notas[asignatura] = notas[asignatura] - 1;
            d.setText(notas[asignatura].toString());
            t.setText(asignaturas[asignatura]);
        }else{
            Toast.makeText(getApplicationContext(), "No se puede restar mas", Toast.LENGTH_LONG).show();
            d.setText(notas[asignatura].toString());
            t.setText(asignaturas[asignatura]);
        }
    }
}
