package com.thins15.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);

        // Enables click action on floating action button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getAllVacations();
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.sample){
            repository = new Repository(getApplication());
            // FIX ME: populate with desired data. Currently based on example data from webseries
            Vacation vacation = new Vacation (0,"Florence Spring Break", 550, "4/5/2025", "4/15/2025");
            repository.insert(vacation);

            Vacation vacation2 = new Vacation (0,"Italian Honeymoon", 600, "4/5/2025", "4/15/2025");
            Excursion excursion = new Excursion(0,"Hot Air Balloon Ride", 80, 1);
            repository.insert(vacation2);
            repository.insert(excursion);



            return true;
        }
        if(item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }
        return true;
    }
}