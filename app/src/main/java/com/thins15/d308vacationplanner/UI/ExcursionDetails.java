package com.thins15.d308vacationplanner.UI;

import static androidx.core.app.PendingIntentCompat.getActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    int excursionID;
    int vacationID;
    String excursionTitle;
    String date;
    Excursion currentExcursion;

    EditText editName;
    EditText editNote;
    TextView editDate;
    int newVacationID;
    Repository repository;
    Vacation currentVacay;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    HashMap<Integer, Vacation> vacationHashMap = new HashMap<>();






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

        excursionID = getIntent().getIntExtra("id", -1);
        excursionTitle = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("startDate");
        vacationID = getIntent().getIntExtra("vacayID", -1);
        Toast.makeText(this, "Vacation ID is " + vacationID, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Excursion ID is " + excursionID, Toast.LENGTH_SHORT).show();


        editName.setText(excursionTitle);
        editDate.setText(date);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        // SPINNER
        // Associate excursion with vacation title using spinner
        Spinner spinner = findViewById(R.id.spinner);
        ArrayList<Vacation> vacationArrayList = new ArrayList<>();
        vacationArrayList.addAll(repository.getAllVacations());
        ArrayList<Integer> vacationIdList= new ArrayList<>();
        for(Vacation vacation:vacationArrayList){
            vacationIdList.add(vacation.getVacationID());
        }
        ArrayAdapter<Integer> vacationIdAdapter= new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item,vacationIdList);
        Spinner spinner2=findViewById(R.id.spinner);
        spinner.setAdapter(vacationIdAdapter);

        ArrayAdapter<Vacation> vacationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vacationArrayList);
        spinner.setAdapter(vacationAdapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String newItemName = spinner2.getSelectedItem().toString();
                long newItemID = spinner2.getSelectedItemId();
                newVacationID = Math.toIntExact(newItemID) + 1;
                Toast.makeText(getApplicationContext(), "You selected " + newVacationID, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    // VALIDATION METHODS
    private boolean validateNonBlank(String title, String date) {
        if (title.isBlank() || date.isBlank()) {
            //showEmptyError();
            //Toast.makeText(this,startDate, Toast.LENGTH_LONG).show();
            //Toast.makeText(this,endDate, Toast.LENGTH_LONG).show();
            return false;
        } else {
            //showSuccess();
            return true;
        }
    }
    public boolean isValidDate(String date){

        try {
            sdf.setLenient(false);
            sdf.parse(date);
            return true;

        } catch (ParseException e) {
            return false;
        }
    }


    //FIX ME: This thing just messed up man
    // Module keeps trying to use Vacation with ID 1, instead of basing it off of the selected ID
    // Unsure how to call object using the new ID to validate the start & end dates
    public boolean isInDateRange(String date, int vacayID) throws ParseException {
        ArrayList<Vacation> vacationArrayList = new ArrayList<>();
        vacationArrayList.addAll(repository.getAllVacations());
        Vacation vacay = vacationArrayList.get(vacayID - 1);
        //String startVacay = vacay.getStartDate();
        Date startVacay = sdf.parse(vacay.getStartDate());
        //String endVacay = vacay.getEndDate();
        Date endVacay = sdf.parse(vacay.getEndDate());
        Date dateDate = sdf.parse(date);

        Toast.makeText(this, "startVacay: " + startVacay, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "endVacay: " + endVacay, Toast.LENGTH_SHORT).show();
        // Check that date is after/on startVacay or before/on endVacay
        try {
            //if (date.compareTo(startVacay) > 0 || date.compareTo(startVacay) == 0) {
            if (dateDate.compareTo(startVacay) > 0 || dateDate.compareTo(startVacay) == 0) {
                Toast.makeText(this, "Excursion is on or after vacation start", Toast.LENGTH_SHORT).show();
                //if (date.compareTo(endVacay) < 0 || date.compareTo(startVacay) == 0) {
                if (dateDate.compareTo(endVacay) < 0 || dateDate.compareTo(startVacay) == 0) {
                    Toast.makeText(this, "Excursion is on or before vacation end", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Toast.makeText(this, "Excursion is after vacation end. Please choose another date.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(this, "Excursion is before vacation starts. Please choose another date.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            //throw new RuntimeException(e);
            Toast.makeText(this, "Something went wrong with daterange check", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


/*
    public boolean isInDateRange(String date) {


        vacation.setVacationID(newVacationID);

        String startDate = vacation.getStartDate();
        String endDate = vacation.getEndDate();

        Toast.makeText(this, "Vacation start: " + startDate + "End: " + endDate, Toast.LENGTH_SHORT).show();



        return true;
    }

      */


    private void showRangeError(){
        Toast.makeText(this, "Excursion does not take place during vacation dates", Toast.LENGTH_SHORT).show();
    }
    private void showEmptyError() {
        Toast.makeText(this, "Please complete all fields before saving", Toast.LENGTH_LONG).show();
    }
    private void showFormatError() {
        Toast.makeText(this, "Please use the correct date format of MM/dd/yy", Toast.LENGTH_LONG).show();
    }

    private void showSuccess() {
        Toast.makeText(this, "All fields entered correctly", Toast.LENGTH_SHORT).show();
    }
    // END VALIDATION METHODS


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.excursionsave) {
            // Validate no fields are blank, date is correct format, and excursion occurs during vacation dates
            boolean validBlank = validateNonBlank(editName.getText().toString(), editDate.getText().toString());
            boolean validDate = isValidDate(editDate.getText().toString());
            //
            // BROKEN HERE. Prob is module is only comparing the excursion dates to the first vacay
            // in the db, not the dates for the selected vacay. Need to figure out how to retrieve
            // vacation by id, and gather start and end dates to be used for validation

            // Toast.makeText(this, "Right before validrange module", Toast.LENGTH_SHORT).show();
            boolean validRange = false;
            try {
                validRange = isInDateRange(editDate.getText().toString(), newVacationID);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            //Toast.makeText(this, "Excursion occurs during vacay: " + validRange, Toast.LENGTH_LONG).show();


            if (validBlank && validDate) {
                if (validRange) {
                    showSuccess();
                    Excursion excursion;
                    if (excursionID == -1) {
                        //Toast.makeText(this, "excursion ID is: " + excursionID, Toast.LENGTH_LONG).show();
                        if (repository.getAllExcursions().isEmpty()) {
                            excursionID = 1;
                            //Toast.makeText(this, "excursion ID is changed to: " + excursionID, Toast.LENGTH_LONG).show();
                            excursion = new Excursion(excursionID, editName.getText().toString(), editDate.getText().toString(), newVacationID);
                            repository.insert(excursion);

                            Toast.makeText(ExcursionDetails.this, "Excursion saved", Toast.LENGTH_LONG).show();
                            this.finish();
                        } else {
                            //Increment the excursionID
                            excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionID() + 1;
                            excursion = new Excursion(excursionID, editName.getText().toString(), editDate.getText().toString(), newVacationID);
                            repository.insert(excursion);

                            Toast.makeText(ExcursionDetails.this, "Excursion saved", Toast.LENGTH_LONG).show();
                            this.finish();
                        }
                    } else {
                        excursion = new Excursion(excursionID, editName.getText().toString(), editDate.getText().toString(), newVacationID);
                        repository.update(excursion);

                        Toast.makeText(this, "Excursion updated", Toast.LENGTH_LONG).show();
                        this.finish();
                    }
                } else if (!validRange) {
                    //Toast.makeText(this, "You're checking for excursion occurring during vacation dates", Toast.LENGTH_LONG).show();
                    showRangeError();
                }
            } else if (!validBlank) {
                //Toast.makeText(this, "You're checking for blank spaces", Toast.LENGTH_LONG).show();
                showEmptyError();
            } else if (!validDate) {
                //Toast.makeText(this, "You're in the date format validation", Toast.LENGTH_LONG).show();

                //FIX ME: This is returning correct format even if the year is in yy and not yyyy
                // also allowing letters into format. May need to change to regex
                // this looks helpful: https://stackoverflow.com/questions/226910/how-to-sanity-check-a-date-in-java
                showFormatError();
            }
        }

        if (item.getItemId() == R.id.excursiondelete){
            //Toast.makeText(this, "You have entered the excursiondelete method, excursionID is " + excursionID, Toast.LENGTH_SHORT).show();
            if (excursionID == -1){
                Toast.makeText(this, "Can't delete an empty excursion. " +
                        "Please choose a saved excursion", Toast.LENGTH_LONG).show();
                //this.finish();
            } else {
                for (Excursion excursion : repository.getAllExcursions()) {
                    if (excursion.getExcursionID() == excursionID) {
                        currentExcursion = excursion;
                    }
                }

                try {
                    repository.delete(currentExcursion);
                    Toast.makeText(this, currentExcursion.getExcursionTitle() + " was deleted", Toast.LENGTH_LONG).show();
                    this.finish();
                } catch (Exception e) {
                    Toast.makeText(this, "Couldn't delete excursion", Toast.LENGTH_LONG).show();
                }
            }
        }

        if (item.getItemId() == R.id.excursionshare) {
            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            //sentIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString() + "EXTRA_TEXT");
            sentIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString());
            //sentIntent.putExtra(Intent.EXTRA_TITLE, editNote.getText().toString() + "EXTRA_TITLE");
            sentIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);
            return true;

        }
            // Date picker stuff. Use for sending messages about start date/end date validation
        if (item.getItemId() == R.id.excursionnotify) {
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;

            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
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