package edu.hawaii.its.EmployeeIOB.service;


import edu.hawaii.its.EmployeeIOB.access.Absence;
import edu.hawaii.its.EmployeeIOB.access.AbsenceDate;
import edu.hawaii.its.EmployeeIOB.access.User;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by bobbyfiggs on 6/1/16.
 */
public class MysqlService {

    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private DataSource ds;

    public MysqlService(){
        try {
            ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");
            con = ds.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            close();
        }
    }

    public void close(){
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (SQLException e) {

        }
    }

    public String getEmpidFromAbsid(String absid){
        String result = "";
        String sql = "SELECT EMPID FROM ABSENT " +
                "WHERE ABSID = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, absid);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString("EMPID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getRoleFromUhnumber(String uhnumber) {
        String result = "";
        String sql = "SELECT EMPTYPE FROM EMPLOYEE " +
                "WHERE EMPUHNUMBER = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, uhnumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString("EMPTYPE");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getUhnumberFromUsername(String username){
        String uhNumber = "";
        try{
            String sql = "SELECT EMPUHNUMBER FROM EMPLOYEE " +
                    "WHERE EMPUSERNAME = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                uhNumber = rs.getString("EMPUHNUMBER");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uhNumber;
    }

    public String getEmpidFromUhnumber(String uhNumber){
        String result = "";
        try {
            String sql = "SELECT EMPID FROM EMPLOYEE " +
                    "WHERE EMPUHNUMBER = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, uhNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString("EMPID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void logAction(String empid, String actionId, String notes){

        try {
            User user = (User)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            String managerEmpid = getEmpidFromUhnumber(String.valueOf(user.getUhuuid()));
            String sql = "INSERT INTO ACTIONLOG (EMPID, ACTIONID, LOGDATE, MANAGERID, LOGNOTES) VALUES (?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, empid);
            ps.setString(2, actionId);
            ps.setDate(3,new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            ps.setString(4,managerEmpid);
            ps.setString(5,notes);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String addAbsence(String username, String start, String end, String notes) {

        String result = "";
        ArrayList<Date> dates = getDateRange(start, end);
        try {
            String uhNumber = getUhnumberFromUsername(username);
            String sql;

            for (Date i : dates) {
                sql = "Select MAX(ABSID) from ABSENT";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                int absid;
                if (rs.next()) {
                    absid = rs.getInt("MAX(ABSID)") + 1;
                } else {
                    absid = 0;
                }
                String empid = getEmpidFromUhnumber(uhNumber);

                sql = "INSERT INTO ABSENT (ABSID, EMPID, ABSNOTES) values(?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1, absid);
                ps.setString(2, empid);
                ps.setString(3, notes);
                ps.executeUpdate();

                sql = "INSERT INTO ABSENTDATE (ABSID, ABSDATE) VALUES (?,?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1, absid);
                ps.setDate(2, new java.sql.Date(i.getTime()));
                ps.executeUpdate();

                logAction(empid,"1","");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String validateAdd(String username, String start, String end){
        String result = "SUCCESS";
        String uhNumber = getUhnumberFromUsername(username);
        Date startDate = toDate(start);
        Date endDate = toDate(end);
        if(uhNumber == ""){
            result = "User not Found!";
        }else if(startDate == null || endDate == null){
            result = "Invalid date please use format yyyy-mm-dd";
        }else if(startDate.after(endDate)){
            result = "start Date cannot be after end Date";
        }else{
            String empid = getEmpidFromUhnumber(uhNumber);
            try {
                String sql = "select absentdate.absdate from absentdate right join absent on " +
                        "(absent.empid = ? and absent.absid = absentdate.absid) " +
                        "where absentdate.absdate >= ? and absentdate.absdate <= ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, empid);
                ps.setDate(2, new java.sql.Date(startDate.getTime()));
                ps.setDate(3, new java.sql.Date(endDate.getTime()));
                rs = ps.executeQuery();
                if (rs.next()) {
                    result = "An absence already exists in this range";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    public void removeAbsence(String absid) {
        try {
            String sql = "delete from absentdate where absid = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, absid);
            ps.executeUpdate();

            sql = "delete from absent where absid = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, absid);
            ps.executeUpdate();

            String empid = getEmpidFromAbsid(absid);
            logAction(empid,"2","");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String,List<Absence>> getAbsences(String start, String end){
        return getAbsences(toDate(start),toDate(end));
    }

    public Map<String,List<Absence>> getAbsences(Date start, Date end){
        Map<String,List<Absence>> absences = new HashMap<String, List<Absence>>();
        if(!start.after(end)){
            try {
                String sql = "Select empfirstname, emplastname, absent.absid, absdate " +
                        "From absent " +
                        "join absentdate on absent.absid = absentdate.absid " +
                        "join employee on employee.empid = absent.empid " +
                        "where absdate >= ? and absdate <= ?";
                ps = con.prepareStatement(sql);
                ps.setDate(1,new java.sql.Date(start.getTime()));
                ps.setDate(2,new java.sql.Date(end.getTime()));
                rs = ps.executeQuery();
                while(rs.next()){
                    String firstname = rs.getString("empfirstname");
                    String lastname = rs.getString("emplastname");
                    String date = rs.getString("absdate");
                    SimpleDateFormat incoming = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat outgoing = new SimpleDateFormat("EEE MMM dd yyyy");
                    String formatted = outgoing.format(incoming.parse(date));
                    String absid = rs.getString("absid");
                    Absence ab = new Absence(firstname, lastname, formatted, absid);
                    if(!absences.containsKey(formatted)){
                        absences.put(formatted,new ArrayList<Absence>());
                    }
                   absences.get(formatted).add(ab);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return absences;
    }

    private static Date toDate(String day) {
        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss ");
             date = formatter.parse(day);

        } catch (ParseException e) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                date = formatter.parse(day);

            } catch (ParseException e1) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy");
                    date = formatter.parse(day);

                } catch (ParseException e2) {
                    System.out.println("Invlaid date format");
                }
            }
        }
        finally {
            return date;
        }
    }

    private static ArrayList<Date> getDateRange(String start, String end) {

        ArrayList<Date> dates = new ArrayList<Date>();
        Date startDate = toDate(start);
        Date endDate = toDate(end);
        if(!startDate.after(endDate)) {
            Date it = startDate;
            do {
                dates.add(it);
                Calendar c = Calendar.getInstance();
                c.setTime(it);
                c.add(Calendar.DATE, 1);
                it = c.getTime();
            } while (!it.after(endDate));
        }


        return dates;
    }
}
