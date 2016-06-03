package edu.hawaii.its.EmployeeIOB.access;

import java.util.Date;
import java.util.List;

/**
 * Created by bobbyfiggs on 6/1/16.
 */
public class AbsenceDate {
    private Date date;
    private List<Absence> absences;

    public AbsenceDate(){

    }

    public AbsenceDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(List<Absence> absences) {
        this.absences = absences;
    }

    public void add(Absence ab){
        absences.add(ab);
    }
}
