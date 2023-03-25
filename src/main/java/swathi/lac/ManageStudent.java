package swathi.lac;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class ManageStudent extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");
    	
    	boolean loadStudentData = false;    	
    	String requestPath = request.getRequestURI(); // /LearnerAcademy/LoadStudentData
		if(requestPath != null && requestPath.equals(request.getContextPath()+"/LoadStudentData")) {
			loadStudentData = true;
		}
		Connection con;
		PreparedStatement st;
		TreeMap<Integer, String> studentMap = new TreeMap<Integer, String>();
		TreeMap<Integer, String> classMap = new TreeMap<Integer, String>();
		
    	if(!loadStudentData) {
	    	String action = "";    	
	    	String studentId = request.getParameter("DeleteStudent");
	    	int age = 0;
	    	String gender = "";
	    	String classId = "";
	    	String name = "";
	    	String message = "";
	    	if(studentId != null && studentId.trim().length() != 0) {
	    		action = "Delete";
	    	}else {
	    		studentId = request.getParameter("AddStudent");
	    		if(studentId != null && studentId.trim().length() != 0) {
		    		name = request.getParameter("name");
		    		String ageValue = request.getParameter("age");
		    		if(ageValue != null) {
		    			age = Integer.parseInt(ageValue);
		    		}
		    		gender = request.getParameter("gender");
		    		classId = request.getParameter("class");
		    		action = "Add";
	    		}else {
	    			message = "Please input data";
	    		}
	    	}
	    	if(action.equals("Delete") || 
	    			action.equals("Add") && !isStudentExist(Integer.parseInt(studentId))) {
	    		   	
				try	{
					Context ctx = new InitialContext();
					DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
					con = ds.getConnection();
					String query = "";
					if(action.equalsIgnoreCase("Add")) {
						query = "INSERT INTO STUDENT (ID, NAME, AGE, GENDER, CLASS) VALUES(?, ?, ?, ?, ?)";
					}else {
						query = "DELETE FROM STUDENT WHERE ID = ? ";
					}
					st=con.prepareStatement(query);
					st.setInt(1, Integer.parseInt(studentId));
					if(action.equalsIgnoreCase("Add")) {
						st.setString(2, name);
						st.setInt(3, age);
						st.setString(4, gender);
						st.setString(5, classId);
					}
					st.executeUpdate();
					
					st.close();
					con.close();
				}			
				catch(Exception e){
					System.out.println("\nFailed !\n\n"+e.getMessage());			
				}
				finally {
					System.out.println("\nDone !\n\n");
				}
				message = "Student "+name+" has been deleted!";
				if(action.equalsIgnoreCase("Add")) {
					message = "Student "+name+" has been added!";
				}				
	    	}
	    	request.setAttribute("StudentMessage", message);
    	}
    	con = null;
		st = null;
		ResultSet rs;
		
		try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT ID, NAME FROM STUDENT";
			st=con.prepareStatement(query);			
			rs = st.executeQuery();
			while(rs.next()) {
				studentMap.put(rs.getInt("ID"), rs.getString("NAME"));
			}
			rs.close();
			st.close();
			con.close();
		} catch(Exception e){
			System.out.println("\nFailed !\n\n"+e.getMessage());			
		} finally {
			System.out.println("\nDone !\n\n");
		}
		
		con = null;
		st = null;
		rs = null;
		
		try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT ID, NAME FROM CLASS";
			st=con.prepareStatement(query);			
			rs = st.executeQuery();
			while(rs.next()) {
				classMap.put(rs.getInt("ID"), rs.getString("NAME"));
			}
			rs.close();
			st.close();
			con.close();
		} catch(Exception e){
			System.out.println("\nFailed !\n\n"+e.getMessage());			
		} finally {
			System.out.println("\nDone !\n\n");
		}
    	
		RequestDispatcher req = request.getRequestDispatcher("ManageStudent.jsp");
		request.setAttribute("StudentMap", studentMap);		
		request.setAttribute("ClassMap", classMap);
		req.include(request, response);
    }
    
    public boolean isStudentExist(int studentId) {
    	int rowCount = 0;
    	Connection con;
		PreparedStatement st;
		ResultSet rs;
    	try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT COUNT(1) FROM STUDENT WHERE ID = ?";
			st=con.prepareStatement(query);			
			st.setInt(1, studentId);
			rs = st.executeQuery();
			if(rs.next()) {
				rowCount = rs.getInt(1);
			}
			rs.close();
			st.close();
			con.close();
		} catch(Exception e){
			System.out.println("\nFailed !\n\n"+e.getMessage());			
		} finally {
			System.out.println("\nDone !\n\n");
		}
    	if(rowCount > 0)
    		return true;
    	else
    		return false;
    }
}