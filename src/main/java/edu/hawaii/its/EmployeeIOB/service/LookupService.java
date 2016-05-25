package edu.hawaii.its.EmployeeIOB.service;

import com.mysql.cj.jdbc.MysqlDataSource;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by bobbyfiggs on 5/18/16.
 */
public final class LookupService {
    public static String getRole(String uhnumber){
        String result = "";
        String sql = "SELECT EMPTYPE FROM EMPLOYEE "+
                "WHERE EMPUHNUMBER = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DataSource mysqlDS = null;

        try {
            mysqlDS = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");
            con = mysqlDS.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1,uhnumber);
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getString("EMPTYPE");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                if(con != null){
                    con.close();
                }

            } catch (SQLException e){

            }
        }
        return result;
    }
    public static String getEmpid(String uhnumber){
        String result = "";
        String sql = "SELECT EMPID FROM EMPLOYEE "+
                "WHERE EMPUHNUMBER = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DataSource mysqlDS = null;

        try {
            mysqlDS = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");
            con = mysqlDS.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1,uhnumber);
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getString("EMPID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                if(con != null){
                    con.close();
                }

            } catch (SQLException e){

            }
        }
        return result;
    }
    public static String getUhNumber(String username){
        String result = "";
        String sql = "SELECT EMPUHNUMBER FROM EMPLOYEE "+
                "WHERE EMPUSERNAME = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DataSource mysqlDS = null;

        try {
            mysqlDS = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");
            con = mysqlDS.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1,username);
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getString("EMPUHNUMBER");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                if(con != null){
                    con.close();
                }

            } catch (SQLException e){

            }
        }
        return result;
    }
    public static boolean validateAdd(String uhNumber, String start, String end){
        boolean result = true;
        Date startDate = toDate(start);
        Date endDate = toDate(end);
        String empid = getEmpid(uhNumber);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DataSource mysqlDS = null;

        try{
            String sql = "select absentdate.absdate from absentdate right join absent on " +
                    "(absent.empid = ? and absent.absid = absentdate.absid) " +
                    "where absentdate.absdate >= ? and absentdate.absdate <= ?";

            mysqlDS = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");
            con = mysqlDS.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1,empid);
            ps.setDate(2,new java.sql.Date(startDate.getTime()));
            ps.setDate(3,new java.sql.Date(endDate.getTime()));
            rs = ps.executeQuery();
            if(rs.next()){
                result = false;
            }


        } catch (NamingException e) {
            result = false;
            e.printStackTrace();
        } catch (SQLException e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }


    public static String addAbsence(String uhNumber,String start, String end, String notes){
        //get all dates being added
        String result = "";
        ArrayList<Date> dates = new ArrayList<Date>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DataSource mysqlDS = null;
        try {
            System.out.println("In add absence "+start+" "+end );
            dates = getDateRange(start,end);
            mysqlDS = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");
            con = mysqlDS.getConnection();

            for(Date i:dates){
                String sql= "Select MAX(ABSID) from ABSENT";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                int absid;
                if(rs.next()){
                    absid = rs.getInt("MAX(ABSID)")+1;
                }
                else {
                    absid = 0;
                }

                String empid = "";
                sql = "SELECT EMPID FROM EMPLOYEE "+
                        "WHERE EMPUHNUMBER = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1,uhNumber);
                rs = ps.executeQuery();
                if(rs.next()) {
                    empid = rs.getString("EMPID");
                }

                sql = "INSERT INTO ABSENT (ABSID, EMPID, ABSNOTES) values(?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1,absid);
                ps.setString(2,empid);
                ps.setString(3,notes);
                ps.executeUpdate();

                sql = "INSERT INTO ABSENTDATE (ABSID, ABSDATE) VALUES (?,?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1,absid);
                ps.setDate(2, new java.sql.Date(i.getTime()));
                ps.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                if(con != null){
                    con.close();
                }

            } catch (SQLException e){

            }
        }
        return result;
    }

    private static Date toDate(String day){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date  = formatter.parse(day);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<Date> getDateRange(String start, String end){
        System.out.println("in get date Range " +start +" "+end);
        ArrayList<Date> dates = new ArrayList<Date>();
        Date startDate = toDate(start);
        Date endDate = toDate(end);
        System.out.println(startDate +" "+endDate);
        Date it = startDate;
        do{
            dates.add(it);
            Calendar c = Calendar.getInstance();
            c.setTime(it);
            c.add(Calendar.DATE, 1);
            it = c.getTime();
        }while(!it.after(endDate));


        return dates;
    }
}
