package com.thins15.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thins15.d308vacationplanner.R;
import com.thins15.d308vacationplanner.database.Repository;
import com.thins15.d308vacationplanner.entities.Excursion;
import com.thins15.d308vacationplanner.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class VacationDetails extends AppCompatActivity {
    int vacationID;
    int numExcursions;
    Vacation currentVacay;
    String vacationTitle;
    String vacationAccomod;
    String startDate;
    String endDate;
    EditText editTitle;
    EditText editVacayAccomod;
    EditText editStartDate;
    EditText editEndDate;

    Repository repository;

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);

        // Enables click action on floating action button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

        // Pulls existing item details and shows in activity_vacation_details
        editTitle = findViewById(R.id.vacayTitle2);
        editVacayAccomod = findViewById(R.id.vacayStay);
        editStartDate = findViewById(R.id.vacayStart);
        editEndDate = findViewById(R.id.vacayEnd);

        vacationID = getIntent().getIntExtra("id", -1);
        vacationTitle = getIntent().getStringExtra("title");
        vacationAccomod = getIntent().getStringExtra("accommodations");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");


        editTitle.setText(vacationTitle);
        editVacayAccomod.setText(vacationAccomod);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);

    }

    private boolean validateNonBlank(String title, String hotel, String startDate, String endDate) {
        if (title.isBlank() || hotel.isBlank() || startDate.isBlank() || endDate.isBlank()) {
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


    private void showEmptyError() {
        Toast.makeText(this, "Please complete all fields before saving", Toast.LENGTH_LONG).show();
    }

    private void showFormatError() {
        Toast.makeText(this, "Please use the correct date format of MM/dd/yy", Toast.LENGTH_LONG).show();
    }

    private void showTimelineError() {
        Toast.makeText(this, "End date must be after start date", Toast.LENGTH_LONG).show();
    }
    private void showSuccess() {
        Toast.makeText(this, "All fields entered correctly", Toast.LENGTH_SHORT).show();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

    // ORIGINAL VERSION
    /*
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.vacaysave) {
            Vacation vacation;
            if (vacationID == -1) {
                if (repository.getAllVacations().size() == 0) vacationID = 1;
                else
                    vacationID = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationID() + 1;
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editVacayAccomod.getText().toString(),
                        editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.insert(vacation);
                Toast.makeText(VacationDetails.this, "Vacation saved", Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editVacayAccomod.getText().toString(),
                        editStartDate.getText().toString(), editEndDate.getEditableText().toString());
                repository.update(vacation);
                Toast.makeText(VacationDetails.this, "Vacation updated", Toast.LENGTH_LONG).show();
                this.finish();
            }
        }*/

    // Validate notBlank fields on save
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.vacaysave) {
            boolean validBlank = validateNonBlank(editTitle.getText().toString(), editVacayAccomod.getText().toString(),
                    editStartDate.getText().toString(), editEndDate.getText().toString());
            boolean validStartDate = isValidDate(editStartDate.getText().toString());
            boolean validEndDate = isValidDate(editEndDate.getText().toString());

            Date dateEndDate = null;
            Date dateStartDate = null;
            try {
                dateStartDate = sdf.parse(editStartDate.getText().toString());
                dateEndDate = sdf.parse(editEndDate.getText().toString());
            } catch (ParseException e) {
                //Toast.makeText(this, "In parse date try/catch", Toast.LENGTH_LONG).show();
                // FIX ME: need better exception handling here
            }

            if (validBlank && validStartDate && validEndDate && Objects.requireNonNull(dateEndDate).compareTo(dateStartDate) > 0) {
                showSuccess();
                Vacation vacation;
                if (vacationID == -1) {
                    if (repository.getAllVacations().isEmpty()) vacationID = 1;
                    else
                        vacationID = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationID() + 1;
                    vacation = new Vacation(vacationID, editTitle.getText().toString(), editVacayAccomod.getText().toString(),
                            editStartDate.getText().toString(), editEndDate.getText().toString());
                    repository.insert(vacation);
                    Toast.makeText(VacationDetails.this, "Vacation saved", Toast.LENGTH_LONG).show();
                    this.finish();
                } else {
                    vacation = new Vacation(vacationID, editTitle.getText().toString(), editVacayAccomod.getText().toString(),
                            editStartDate.getText().toString(), editEndDate.getEditableText().toString());
                    repository.update(vacation);
                    Toast.makeText(VacationDetails.this, "Vacation updated", Toast.LENGTH_LONG).show();
                    this.finish();
                }
            } else if (!validBlank) {
                //Toast.makeText(this, "You're checking for blank spaces", Toast.LENGTH_LONG).show();
                showEmptyError();
            } else if (!validStartDate || !validEndDate) {
                //Toast.makeText(this, "You're in the date format validation", Toast.LENGTH_LONG).show();

                //FIX ME: This is returning correct format even if the year is in yy and not yyyy
                // also allowing letters into format. May need to change to regex
                // this looks helpful: https://stackoverflow.com/questions/226910/how-to-sanity-check-a-date-in-java
                showFormatError();
            } else if ((dateEndDate.compareTo(dateStartDate)) <= 0) {
                //Toast.makeText(this, "You're in the date comparison", Toast.LENGTH_LONG).show();
                showTimelineError();
            }
            //Original code resumes
            if (item.getItemId() == R.id.vacaydelete) {
                for (Vacation vacay : repository.getAllVacations()) {
                    if (vacay.getVacationID() == vacationID) currentVacay = vacay;
                }
                numExcursions = 0;
                for (Excursion excursion : repository.getAllExcursions()) {
                    if (excursion.getVacationID() == vacationID) ++numExcursions;
                }
                // Prevent deletion of vacations that have excursions associated with them
                if (numExcursions == 0) {
                    repository.delete(currentVacay);
                    Toast.makeText(VacationDetails.this, currentVacay.getVacationTitle() + " was deleted", Toast.LENGTH_LONG).show();
                    this.finish();
                } else {
                    Toast.makeText(VacationDetails.this, "Can't delete a vacation that has excursions", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            }
        // Enables top left back button
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        } return true;
    }
}

