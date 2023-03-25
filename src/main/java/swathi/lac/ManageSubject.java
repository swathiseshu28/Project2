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


public class ManageSubject extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");
    	
    	boolean loadSubjectData = false;    	
    	String requestPath = request.getRequestURI(); // /LearnerAcademy/LoadSubjectData
		if(requestPath != null && requestPath.equals(request.getContextPath()+"/LoadSubjectData")) {
			loadSubjectData = true;
		}
		Connection con;
		PreparedStatement st;
		TreeMap<Integer, String> subjectMap = new TreeMap<Integer, String>();
		
    	if(!loadSubjectData) {
	    	String action = "";    	
	    	String subjectId = request.getParameter("DeleteSubject");	    	
	    	String name = "";
	    	String message = "";
	    	if(subjectId != null && subjectId.trim().length() != 0) {
	    		action = "Delete";
	    	}else {
	    		subjectId = request.getParameter("AddSubject");
	    		if(subjectId != null && subjectId.trim().length() != 0) {
		    		name = request.getParameter("name");		    		
		    		action = "Add";
	    		}else {
	    			message = "Please input data";
	    		}
	    	}
	    	if(action.equals("Delete") || 
	    			action.equals("Add") && !isSubjectExist(Integer.parseInt(subjectId))) {
	    		   	
				try	{
					Context ctx = new InitialContext();
					DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
					con = ds.getConnection();
					String query = "";
					if(action.equalsIgnoreCase("Add")) {
						query = "INSERT INTO SUBJECT (ID, NAME) VALUES(?, ?)";
					}else {
						query = "DELETE FROM SUBJECT WHERE ID = ? ";
					}
					st=con.prepareStatement(query);
					st.setInt(1, Integer.parseInt(subjectId));
					if(action.equalsIgnoreCase("Add")) {
						st.setString(2, name);
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
				message = "Subject "+name+" has been deleted!";
				if(action.equalsIgnoreCase("Add")) {
					message = "Subject "+name+" has been added!";
				}				
	    	}
	    	request.setAttribute("SubjectMessage", message);
    	}
    	con = null;
		st = null;
		ResultSet rs;
		
		try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT ID, NAME FROM SUBJECT";
			st=con.prepareStatement(query);			
			rs = st.executeQuery();
			while(rs.next()) {
				subjectMap.put(rs.getInt("ID"), rs.getString("NAME"));
			}
			rs.close();
			st.close();
			con.close();
		} catch(Exception e){
			System.out.println("\nFailed !\n\n"+e.getMessage());			
		} finally {
			System.out.println("\nDone !\n\n");
		}
    	
		RequestDispatcher req = request.getRequestDispatcher("ManageSubject.jsp");
		request.setAttribute("SubjectMap", subjectMap);		
		req.include(request, response);
    }
    
    public boolean isSubjectExist(int subjectId) {
    	int rowCount = 0;
    	Connection con;
		PreparedStatement st;
		ResultSet rs;
    	try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT COUNT(1) FROM SUBJECT WHERE ID = ?";
			st=con.prepareStatement(query);			
			st.setInt(1, subjectId);
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