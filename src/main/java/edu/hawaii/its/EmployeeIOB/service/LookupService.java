package edu.hawaii.its.EmployeeIOB.service;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

/**
 * Created by bobbyfiggs on 5/18/16.
 */
public final class LookupService {
    public static boolean isEmployee(String uhnumber){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        boolean result = false;
        String sql = "SELECT * FROM EMPLOYEE "+
                "WHERE EMPUHNUMBER = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String url = "jdbc:mysql://localhost:3306/iob20160420?autoReconnect=true&useSSL=false";
        String user = "root";
        String password = "root";
        DataSource mysqlDS = null;

        try {

            mysqlDS = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/TestDB");

            con = mysqlDS.getConnection();

            ps = con.prepareStatement(sql);
            ps.setString(1,uhnumber);
            rs = ps.executeQuery();

            if (rs.next()) {
                result = true;
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


            } catch (SQLException e) {

            }
        }
        return result;
    }
}
