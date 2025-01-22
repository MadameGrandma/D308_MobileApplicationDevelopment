package com.thins15.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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

public class VacationDetails extends AppCompatActivity {
    int vacationID;
    String vacationTitle;
    String vacationAccomod;
    String startDate;
    String endDate;
    EditText editTitle;
    EditText editVacayAccomod;
    EditText editStartDate;
    EditText editEndDate;

    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);

        // Enables click action on floating action button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

        editTitle = findViewById(R.id.vacayTitle2);
        editVacayAccomod = findViewById(R.id.vacayStay);
        editStartDate = findViewById(R.id.vacayStart);
        editEndDate = findViewById(R.id.vacayEnd);

        vacationID = getIntent().getIntExtra("id", -1);
        vacationTitle = getIntent().getStringExtra("title");
        vacationAccomod = getIntent().getStringExtra("accommodations");
        startDate = getIntent().getStringExtra("start");
        endDate = getIntent().getStringExtra("end");

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
        excursionAdapter.setExcursions(repository.getAllExcursions());

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

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
                this.finish();
            } else {
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editVacayAccomod.getText().toString(),
                        editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.update(vacation);
                this.finish();
            }
        }
        return true;
    }
}