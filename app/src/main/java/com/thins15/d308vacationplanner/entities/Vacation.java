package com.thins15.d308vacationplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    private String vacationTitle;
    private String vacationAccomod;

    private String startDate;
    private String endDate;

    //Constructor
    public Vacation(int vacationID, String vacationTitle, String vacationAccomod, String startDate, String endDate) {
        this.vacationID = vacationID;
        this.vacationTitle = vacationTitle;
        this.vacationAccomod = vacationAccomod;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String toString() {
        return vacationTitle;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getVacationTitle() {
        return vacationTitle;
    }

    public void setVacationTitle(String vacationTitle) {
        this.vacationTitle = vacationTitle;
    }

    public String getVacationAccomod() {
        return vacationAccomod;
    }

    public void setVacationAccomod(String vacationAccomod) {
        this.vacationAccomod = vacationAccomod;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}