package com.thins15.d308vacationplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    private String vacationTitle;
    private double vacationPrice;
    private String startDate;
    private String endDate;

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

    public double getVacationPrice() {
        return vacationPrice;
    }

    public void setVacationPrice(double vacationPrice) {
        this.vacationPrice = vacationPrice;
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

    public Vacation(int vacationID, String vacationTitle, double vacationPrice, String startDate, String endDate) {
        this.vacationID = vacationID;
        this.vacationTitle = vacationTitle;
        this.vacationPrice = vacationPrice;
        this.startDate = startDate;
        this.endDate = endDate;
    }


}
