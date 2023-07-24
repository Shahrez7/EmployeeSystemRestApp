package com.logicbig.example.services;
import com.logicbig.example.domain.Employee;
import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@ApplicationPath("/h")
public class MyRestApp extends Application {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        config.setJdbcUrl("jdbc:mysql://localhost:3306/employee_db");
        config.setUsername("root");
        config.setPassword("Shahrez7");
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }
    public static Employee getEmployeeById(int empId) {
        Employee emp = null;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employee WHERE id = ?")) {

            stmt.setInt(1, empId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    emp = new Employee();
                    emp.setId(rs.getInt("id"));
                    emp.setName(rs.getString("name"));
                    emp.setDesignation(rs.getString("designation"));
                    emp.setDepartment(rs.getString("department"));
                    emp.setSalary(rs.getDouble("salary"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emp;
    }
    public static Employee updateEmployee(int empId, String empDetails) {
        Employee updatedEmp = new Gson().fromJson(empDetails, Employee.class);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE employee SET name=?, designation=?, department=?, salary=? WHERE id=?")) {

            pstmt.setString(1, updatedEmp.getName());
            pstmt.setString(2, updatedEmp.getDesignation());
            pstmt.setString(3, updatedEmp.getDepartment());
            pstmt.setDouble(4, updatedEmp.getSalary());
            pstmt.setInt(5, empId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                return updatedEmp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


public static List<String> getE(){

    String USER = "root";
    String PASSWORD = "Shahrez7";

     String URL = "jdbc:mysql://localhost:3306/employee_db";
    List<String> listOfEmp = new ArrayList<>();

    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
        Statement stmt = conn.createStatement();
        ResultSet rs;

        rs = stmt.executeQuery("SELECT * FROM employee");
        while ( rs.next() ) {
            String Name = rs.getString("name");
            listOfEmp.add(Name);
        }
        conn.close();

    }
    catch (Exception e){
        e.printStackTrace();
    }

    return listOfEmp;
}


    public static String createEmployee(String empDetails) {
        Employee emp = new Gson().fromJson(empDetails, Employee.class);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employee (id,name,designation, department,salary) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, emp.getId());
            pstmt.setString(2, emp.getName());
            pstmt.setString(3, emp.getDesignation());
            pstmt.setString(4, emp.getDepartment());
            pstmt.setDouble(5, emp.getSalary());
            int affectedRows = pstmt.executeUpdate();
            int id = 0;
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
            }
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employee WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Gson().toJson(emp);
    }

    public static boolean deleteEmp(int empid) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM employee WHERE id=?")) {

            pstmt.setInt(1, empid);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static List<Employee> getAllEmployees() {
        List<Employee> empList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employee")) {

            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setDesignation(rs.getString("designation"));
                emp.setDepartment(rs.getString("department"));
                emp.setSalary(rs.getDouble("salary"));
                empList.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empList;
    }

}