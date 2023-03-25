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


public class ManageClass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");
    	
    	boolean loadClassData = false;    	
    	String requestPath = request.getRequestURI(); // /LearnerAcademy/LoadClassData
		if(requestPath != null && requestPath.equals(request.getContextPath()+"/LoadClassData")) {
			loadClassData = true;
		}
		Connection con;
		PreparedStatement st;
		TreeMap<Integer, String> classMap = new TreeMap<Integer, String>();
		
    	if(!loadClassData) {
	    	String action = "";    	
	    	String classID = request.getParameter("DeleteClass");
	    	if(classID != null && classID.trim().length() != 0) {
	    		action = "Delete";
	    	}
	    	if(classID == null || classID.trim().length() == 0) {
	    		classID = request.getParameter("AddClass");
	    		action = "Add";
	    	}
	    	if(action.equals("Delete") || 
	    			action.equals("Add") && !isClassExist(Integer.parseInt(classID))) {
	    		   	
				try	{
					Context ctx = new InitialContext();
					DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
					con = ds.getConnection();
					String query = "";
					if(action.equalsIgnoreCase("Add")) {
						query = "INSERT INTO CLASS (ID, NAME) VALUES(?, ?)";
					}else {
						query = "DELETE FROM CLASS WHERE ID = ? ";
					}
					st=con.prepareStatement(query);
					st.setInt(1, Integer.parseInt(classID));
					if(action.equalsIgnoreCase("Add")) {
						st.setString(2, "Class "+classID);
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
				String message = "Class "+classID+" has been deleted!";
				if(action.equalsIgnoreCase("Add")) {
					message = "Class "+classID+" has been added!";
				}
				request.setAttribute("ClassMessage", message);
	    	}
    	}
    	con = null;
		st = null;
		ResultSet rs;
		
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
    	
		RequestDispatcher req = request.getRequestDispatcher("ManageClass.jsp");
		request.setAttribute("ClassMap", classMap);
		req.include(request, response);
    }
    
    public boolean isClassExist(int classId) {
    	int rowCount = 0;
    	Connection con;
		PreparedStatement st;
		ResultSet rs;
    	try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT COUNT(1) FROM CLASS WHERE ID = ?";
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