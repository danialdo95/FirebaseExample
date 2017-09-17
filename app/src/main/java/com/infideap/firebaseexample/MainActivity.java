package com.infideap.firebaseexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.infideap.firebaseexample.entity.Person;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
    //need to add this line
        implements PersonFragment.OnListFragmentInteractionListener{

    private TextView totalSalaryTextView;
    private TextView totalCarsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        totalSalaryTextView = (TextView)findViewById(R.id.textView_total_salary);
        totalCarsTextView = (TextView)findViewById(R.id.textView_total_cars);
        // To display fragment in MainActivity (Replace)
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, PersonFragment.newInstance(1))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onListFragmentInteraction(Person item) {

    }

    @Override
    public void onTotalUpdateFragmentInteraction(double totalSalary, int totalCars) {
        totalSalaryTextView.setText(
                String.format(
                        Locale.getDefault(),
                        "RM %.2f",
                        totalSalary
                )
        );
        totalCarsTextView.setText(
                String.format(
                        Locale.getDefault(),
                        "%d Car(s)",
                        totalCars
                )
        );
    }
}
