package edu.hawaii.its.EmployeeIOB.access;

/**
 * Created by bobbyfiggs on 5/31/16.
 */
public class Absence {
    private String firstname;
    private String lastname;
    private String date;
    private String absid;

    public Absence(String firstname, String lastname, String date, String absid) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.date = date;
        this.absid = absid;
    }

    public String getFirstname(){
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAbsid() {
        return absid;
    }

    public void setAbsid(String absid) {
        this.absid = absid;
    }
}
