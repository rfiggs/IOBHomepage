package edu.hawaii.its.EmployeeIOB.service;
import edu.hawaii.its.EmployeeIOB.access.User;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
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
    public static String getRole(String uhnumber) {
        String result = "";
        String sql = "SELECT EMPTYPE FROM EMPLOYEE " +
                "WHERE EMPUHNUMBER = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DataSource mysqlDS = null;

        try {
            mysqlDS = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");
            con = mysqlDS.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, uhnumber);
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
        return result;
    }

    public static String getEmpid(String uhnumber) {
        String result = "";
        String sql = "SELECT EMPID FROM EMPLOYEE " +
                "WHERE EMPUHNUMBER = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DataSource mysqlDS = null;

        try {
            mysqlDS = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");
            con = mysqlDS.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, uhnumber);
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
        return result;
    }

    public static String getUhNumber(String username) {
        String result = "";
        String sql = "SELECT EMPUHNUMBER FROM EMPLOYEE " +
                "WHERE EMPUSERNAME = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DataSource mysqlDS = null;

        try {
            mysqlDS = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");
            con = mysqlDS.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
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
        return result;
    }

    public static String validateAdd(String username, String start, String end){
        String result = "SUCCESS";
        String uhNumber = getUhNumber(username);
        Date startDate = toDate(start);
        Date endDate = toDate(end);
        DataSource mysqlDS = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        if(uhNumber == ""){
            result = "User not Found!";
        }else if(startDate == null || endDate == null){
            result = "Invalid date please use format yyyy-mm-dd";
        }else if(startDate.after(endDate)){
            result = "start Date cannot be Greater than end Date";
        }else{
            String empid =getEmpid(uhNumber);
            try {
                String sql = "select absentdate.absdate from absentdate right join absent on " +
                        "(absent.empid = ? and absent.absid = absentdate.absid) " +
                        "where absentdate.absdate >= ? and absentdate.absdate <= ?";

                mysqlDS = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");
                con = mysqlDS.getConnection();
                ps = con.prepareStatement(sql);
                ps.setString(1, empid);
                ps.setDate(2, new java.sql.Date(startDate.getTime()));
                ps.setDate(3, new java.sql.Date(endDate.getTime()));
                rs = ps.executeQuery();
                if (rs.next()) {
                    result = "An absence already exists in this range";
                }


            } catch (NamingException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
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
        }

        return result;
    }


    public static String addAbsence(String username, String start, String end, String notes) {

        String result = "";
        ArrayList<Date> dates = getDateRange(start, end);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DataSource mysqlDS = null;
        try {

            mysqlDS = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");
            con = mysqlDS.getConnection();

            String sql = "SELECT EMPUHNUMBER FROM EMPLOYEE " +
                    "WHERE EMPUSERNAME = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            String uhNumber = "";
            if (rs.next()) {
                uhNumber = rs.getString("EMPUHNUMBER");
            }



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

                String empid = "";
                sql = "SELECT EMPID FROM EMPLOYEE " +
                        "WHERE EMPUHNUMBER = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, uhNumber);
                rs = ps.executeQuery();
                if (rs.next()) {
                    empid = rs.getString("EMPID");
                }


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

                User user = (User)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                String managerEmpid = "";
                sql = "SELECT EMPID FROM EMPLOYEE " +
                        "WHERE EMPUHNUMBER = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, String.valueOf(user.getUhuuid()));
                rs = ps.executeQuery();
                if (rs.next()) {
                    managerEmpid = rs.getString("EMPID");
                }

                //Add to action log
                sql = "INSERT INTO ACTIONLOG (EMPID, ACTIONID, LOGDATE, MANAGERID, LOGNOTES) VALUES (?,?,?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setString(1, empid);
                ps.setString(2, "1");
                ps.setDate(3,new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                ps.setString(4,managerEmpid);
                ps.setString(5,"");
                ps.executeUpdate();



            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
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
        return result;
    }

    private static Date toDate(String day) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss ");
            Date date = formatter.parse(day);
            return date;
        } catch (ParseException e) {
            return null;
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
