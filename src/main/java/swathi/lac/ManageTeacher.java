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


public class ManageTeacher extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");
    	
    	boolean loadTeacherData = false;    	
    	String requestPath = request.getRequestURI(); // /LearnerAcademy/LoadTeacherData
		if(requestPath != null && requestPath.equals(request.getContextPath()+"/LoadTeacherData")) {
			loadTeacherData = true;
		}
		Connection con;
		PreparedStatement st;
		TreeMap<Integer, String> teacherMap = new TreeMap<Integer, String>();
		
    	if(!loadTeacherData) {
	    	String action = "";    	
	    	String teacherId = request.getParameter("DeleteTeacher");
	    	int age = 0;
	    	String gender = "";
	    	String name = "";
	    	String message = "";
	    	if(teacherId != null && teacherId.trim().length() != 0) {
	    		action = "Delete";
	    	}else {
	    		teacherId = request.getParameter("AddTeacher");
	    		if(teacherId != null && teacherId.trim().length() != 0) {
		    		name = request.getParameter("name");
		    		String ageValue = request.getParameter("age");
		    		if(ageValue != null) {
		    			age = Integer.parseInt(ageValue);
		    		}
		    		gender = request.getParameter("gender");
		    		action = "Add";
	    		}else {
	    			message = "Please input data";
	    		}
	    	}
	    	if(action.equals("Delete") || 
	    			action.equals("Add") && !isTeacherExist(Integer.parseInt(teacherId))) {
	    		   	
				try	{
					Context ctx = new InitialContext();
					DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
					con = ds.getConnection();
					String query = "";
					if(action.equalsIgnoreCase("Add")) {
						query = "INSERT INTO TEACHER (ID, NAME, AGE, GENDER) VALUES(?, ?, ?, ?)";
					}else {
						query = "DELETE FROM TEACHER WHERE ID = ? ";
					}
					st=con.prepareStatement(query);
					st.setInt(1, Integer.parseInt(teacherId));
					if(action.equalsIgnoreCase("Add")) {
						st.setString(2, name);
						st.setInt(3, age);
						st.setString(4, gender);
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
				message = "Teacher "+name+" has been deleted!";
				if(action.equalsIgnoreCase("Add")) {
					message = "Teacher "+name+" has been added!";
				}				
	    	}
	    	request.setAttribute("TeacherMessage", message);
    	}
    	con = null;
		st = null;
		ResultSet rs;
		
		try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT ID, NAME FROM TEACHER";
			st=con.prepareStatement(query);			
			rs = st.executeQuery();
			while(rs.next()) {
				teacherMap.put(rs.getInt("ID"), rs.getString("NAME"));
			}
			rs.close();
			st.close();
			con.close();
		} catch(Exception e){
			System.out.println("\nFailed !\n\n"+e.getMessage());			
		} finally {
			System.out.println("\nDone !\n\n");
		}
    	
		RequestDispatcher req = request.getRequestDispatcher("ManageTeacher.jsp");
		request.setAttribute("TeacherMap", teacherMap);		
		req.include(request, response);
    }
    
    public boolean isTeacherExist(int classId) {
    	int rowCount = 0;
    	Connection con;
		PreparedStatement st;
		ResultSet rs;
    	try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT COUNT(1) FROM TEACHER WHERE ID = ?";
			st=con.prepareStatement(query);			
			st.setInt(1, classId);
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