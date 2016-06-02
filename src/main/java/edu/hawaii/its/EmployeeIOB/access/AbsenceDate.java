package edu.hawaii.its.EmployeeIOB.access;

import java.util.Date;
import java.util.List;

/**
 * Created by bobbyfiggs on 6/1/16.
 */
public class AbsenceDate {
    private Date date;
    private List<Absence> absences;

    public AbsenceDate(Date date, List<Absence> absences) {
        this.date = date;
        this.absences = absences;
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
}
