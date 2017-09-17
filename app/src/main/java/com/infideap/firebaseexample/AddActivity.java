package com.infideap.firebaseexample;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;
import com.infideap.firebaseexample.entity.Person;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText nameTextInput;
    private TextInputEditText salaryTextInput;
    private TextInputEditText noCarsTextInput;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameTextInput = (TextInputEditText) findViewById(R.id.textInput_name);
        salaryTextInput = (TextInputEditText) findViewById(R.id.textInput_salary);
        noCarsTextInput = (TextInputEditText) findViewById(R.id.textInput_no_cars);
        addButton = (Button) findViewById(R.id.button_add);
        addButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                addData(v);
                finish();
                break;

        }
    }

    private void addData(View v) {
        if (nameTextInput.length() == 0
                || salaryTextInput.length() == 0
                || noCarsTextInput.length() == 0)
            return;
        Person person = new Person(
                nameTextInput.getText().toString(),
                Double.parseDouble(salaryTextInput.getText().toString()),
                Integer.parseInt(noCarsTextInput.getText().toString())
        );

        //Call set value firebase
        FirebaseDatabase.getInstance()
                .getReference("persons")
                .push()
                .setValue(person);
        //Complete add
    }
}
