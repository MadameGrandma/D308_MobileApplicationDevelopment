package com.thins15.d308vacationplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.thins15.d308vacationplanner.dao.ExcursionDAO;
import com.thins15.d308vacationplanner.dao.VacationDAO;
import com.thins15.d308vacationplanner.entities.Vacation;
import com.thins15.d308vacationplanner.entities.Excursion;

//Increment version number to empty db for now
@Database(entities = {Excursion.class, Vacation.class}, version = 13, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (VacationDatabaseBuilder.class){
                if(INSTANCE==null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VacationDatabaseBuilder.class, "MyVacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
            }
        }
