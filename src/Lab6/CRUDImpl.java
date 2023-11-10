package Lab6;

import java.beans.Statement;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class CRUDImpl extends UnicastRemoteObject implements CRUD, Serializable{

	protected CRUDImpl() throws RemoteException {
		super();
	}

	@Override
	public Connection connectMysql() throws RemoteException {
		String url = "jdbc:mysql://localhost:3306/db_rmi"; 
        String user = "root"; 
        String password = ""; 

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối đến MySQL thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối đến MySQL: " + e.getMessage());
        }

        return conn;
		
	}

	@Override
	public void add(String name) throws RemoteException {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = connectMysql();

            String insertQuery = "INSERT INTO student (name) VALUES (?)"; 

            preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Dữ liệu đã được thêm vào cơ sở dữ liệu!");
            } else {
                System.out.println("Không thể thêm dữ liệu vào cơ sở dữ liệu!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm dữ liệu: " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
	}

	@Override
	public String getData() throws RemoteException {
		  List<Student> studentsList = new ArrayList<>();

	        

	        Connection conn = null;
	        java.sql.Statement statement = null;
	        ResultSet resultSet = null;

	        try {
	            conn = connectMysql();
	            statement = conn.createStatement();

	            String selectQuery = "SELECT * FROM student";

	            resultSet = ((java.sql.Statement) statement).executeQuery(selectQuery);

	            while (resultSet.next()) {
	            	Student student = new Student();
	            	student.setId(resultSet.getInt("id"));
	            	student.setName(resultSet.getString("name"));	             

	            	studentsList.add(student);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (resultSet != null) {
	                    resultSet.close();
	                }
	                if (statement != null) {
	                    statement.close();
	                }
	                if (conn != null) {
	                    conn.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }

	        // Convert List<User> to JSON string
	        Gson gson = new Gson();
	        return gson.toJson(studentsList);
	    }
	
}
