package edu.hawaii.its.EmployeeIOB.service;


import edu.hawaii.its.EmployeeIOB.access.Absence;
import edu.hawaii.its.EmployeeIOB.access.User;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Date;

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

    public String getEmpidFromUsername(String username){
        String result = "";
        try {
            String sql = "SELECT EMPID FROM EMPLOYEE WHERE EMPUSERNAME = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString("EMPID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
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
            ps.setDate(3,new Date(new java.util.Date().getTime()));
            ps.setString(4,managerEmpid);
            ps.setString(5,notes);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String addAbsence(String username, long start, long end, String notes) {
        String result = "";
        ArrayList<Date> dates = getDateRange(start, end);

        try {
            String sql;
            String empid = getEmpidFromUsername(username);
            for (Date i : dates) {

                sql = "INSERT INTO ABSENT (EMPID, ABSNOTES) values(?,?)";
                ps = con.prepareStatement(sql);
                ps.setString(1, empid);
                ps.setString(2, notes);
                ps.executeUpdate();

                sql = "INSERT INTO ABSENTDATE (ABSID, ABSDATE) VALUES (LAST_INSERT_ID(),?)";
                ps = con.prepareStatement(sql);
                ps.setDate(1, i);
                ps.executeUpdate();

                logAction(empid, "1", "");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String validateAdd(String username, long startDate, long endDate){
        String result = "SUCCESS";
        String empid = getEmpidFromUsername(username);
        if(empid == "") {
            result = "User not Found!";
        }
        else if(startDate > endDate){
            result = "start Date cannot be after end Date";
        }else{
            try {
                String sql = "select absentdate.absdate from absentdate right join absent on " +
                        "(absent.empid = ? and absent.absid = absentdate.absid) " +
                        "where absentdate.absdate >= ? and absentdate.absdate <= ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, empid);
                ps.setDate(2, new Date(startDate));
                ps.setDate(3, new Date(endDate));
                rs = ps.executeQuery();
                if (rs.next()) {
                    result = "Absence on " + rs.getDate("absdate") +" already exists!";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    public void removeAbsence(String absid) {
        try {
            String empid = getEmpidFromAbsid(absid);
            String sql = "delete from absentdate where absid = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, absid);
            ps.executeUpdate();

            sql = "delete from absent where absid = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, absid);
            ps.executeUpdate();

            logAction(empid,"2","");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String,List<Absence>> getAbsences(long start, long end){
        Map<String,List<Absence>> absences = new HashMap<String, List<Absence>>();
        if(start <= end){
            try {
                String sql = "Select empfirstname, emplastname, absent.absid, absdate " +
                        "From absent " +
                        "join absentdate on absent.absid = absentdate.absid " +
                        "join employee on employee.empid = absent.empid " +
                        "where absdate >= ? and absdate <= ?";
                ps = con.prepareStatement(sql);
                ps.setDate(1,new Date(start));
                ps.setDate(2,new Date(end));
                rs = ps.executeQuery();
                while(rs.next()){
                    String firstname = rs.getString("empfirstname");
                    String lastname = rs.getString("emplastname");
                    Date date = rs.getDate("absdate");
                    SimpleDateFormat format =  new SimpleDateFormat("EEE MMM dd yyyy");
                    String formatted = format.format(new java.util.Date(date.getTime()));
                    String absid = rs.getString("absid");
                    Absence ab = new Absence(firstname, lastname, formatted, absid);
                    if(!absences.containsKey(formatted)){
                        absences.put(formatted,new ArrayList<Absence>());
                    }
                   absences.get(formatted).add(ab);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return absences;
    }

    private static ArrayList<Date> getDateRange(long startDate, long endDate) {
        ArrayList<Date> dates = new ArrayList<Date>();
        if( startDate <= endDate ) {
            Date it = new Date(startDate);
            do {
                dates.add(it);
                Calendar c = Calendar.getInstance();
                c.setTime(it);
                c.add(Calendar.DATE, 1);
                it = new Date(c.getTime().getTime());
            } while (it.getTime()<=endDate);
        }
        return dates;
    }
}
