package com.thins15.d308vacationplanner.UI;

import static androidx.core.app.PendingIntentCompat.getActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.thins15.d308vacationplanner.R;
import com.thins15.d308vacationplanner.database.Repository;
import com.thins15.d308vacationplanner.entities.Excursion;
import com.thins15.d308vacationplanner.entities.Vacation;

import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    int excursionID;
    int vacationID;
    String excursionTitle;
    String date;
    //String excursionDate;

    EditText editName;
    EditText editNote;
    TextView editDate;
    Spinner editVacayID;
    String newID;


    Repository repository;

    DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalendarStart = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursions_details);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        repository = new Repository(getApplication());

        editName = findViewById(R.id.excursionName);
        editDate = findViewById(R.id.excursionDate);
        editNote=findViewById(R.id.note);
        final Spinner editVacayID=findViewById(R.id.spinner);

        excursionID = getIntent().getIntExtra("excursionID", -1);
        excursionTitle = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("startDate");
        vacationID = getIntent().getIntExtra("vacationID", -1);
        //vacationID = Integer.parseInt(getIntent().getStringExtra("vacayID"));
        //vacationID = getIntent().getStringExtra(("vacayID");


        editName.setText(excursionTitle);
        editDate.setText(date);
        //editVacayID.setId(vacationID);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        // Date picker stuff
        // Use same format for Vacation start and end dates
        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                updateLabelStart();
            }
        };


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Date picker stuff
                new DatePickerDialog(ExcursionDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        //Spinner stuff
        // Associate excursion with vacation title using spinner
        // FIX ME: Not working
        Spinner spinner = findViewById(R.id.spinner);
        ArrayList<Vacation> vacationArrayList = new ArrayList<>();
        vacationArrayList.addAll(repository.getAllVacations());
        ArrayAdapter<Vacation> vacationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vacationArrayList);
        spinner.setAdapter(vacationAdapter);


    }
    // Date picker stuff
    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }

    // FIX ME: Excursions are not saving to db
    // need some way to associate vacation in spinner selection when saving excursion
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.excursionsave) {
            Excursion excursion;
            if (excursionID == -1) {
                if (repository.getAllExcursions().size() == 0)
                    excursionID = 1;
                else{
                    excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionID() + 1;
                excursion = new Excursion(excursionID, editName.getText().toString(), editDate.getText().toString(), vacationID);
                repository.insert(excursion);
                Toast.makeText(ExcursionDetails.this, "Excursion saved", Toast.LENGTH_LONG).show();
                this.finish();}
            } else {
                excursion = new Excursion(excursionID, editName.getText().toString(), editDate.toString(), vacationID);
                repository.update(excursion);
                Toast.makeText(ExcursionDetails.this, "Excursion updated", Toast.LENGTH_LONG).show();

                this.finish();
            }
        }


        if (item.getItemId() == R.id.share){
            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString()+ "EXTRA_TEXT");
            sentIntent.putExtra(Intent.EXTRA_TITLE, editNote.getText().toString()+ "EXTRA_TITLE");
            sentIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);
            return true;

        }
        // Date picker stuff. Use for sending messages about start date/end date validation
        if (item.getItemId() == R.id.notify){
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;

            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e){
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
            intent.putExtra("key", "Message I want to see");
            PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

            return true;
        }
        // Enables top left back button
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return true;
    }


}