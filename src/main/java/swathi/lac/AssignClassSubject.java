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


public class AssignClassSubject extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");    	
		String action = request.getParameter("Action");
		Connection con;
		PreparedStatement st;
		TreeMap<Integer, String> classMap = new TreeMap<Integer, String>();
		TreeMap<Integer, String> subjectMap = new TreeMap<Integer, String>();
		TreeMap<Integer, String> classSubjectsMap = new TreeMap<Integer, String>();
		
    	if(action != null && action.trim().length() != 0) {
	    	    	
	    	String classID = request.getParameter("Class");
	    	String subjectId = request.getParameter("Subject");
	    	boolean recordExist = false;
	    	if(classID != null && classID.trim().length() != 0
	    			&& subjectId != null && subjectId.trim().length() != 0) {
	    		if(action.equalsIgnoreCase("Assign")) 
	    			recordExist = isClassAssignedSubject(Integer.parseInt(classID), Integer.parseInt(subjectId));
	    		if(action.equalsIgnoreCase("Unassign") || !recordExist) {
		    		try	{
						Context ctx = new InitialContext();
						DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
						con = ds.getConnection();
						String query = "";
						if(action.equalsIgnoreCase("Assign")) {
							query = "INSERT INTO ClassxTeacherxSubject (ClassID, SubjectID, TeacherID) VALUES(?, ?, 0)";
						}else {
							query = "DELETE FROM ClassxTeacherxSubject WHERE ClassID = ? AND SubjectID = ? ";
						}
						st=con.prepareStatement(query);
						st.setInt(1, Integer.parseInt(classID));
						st.setInt(2, Integer.parseInt(subjectId));
						
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
	    		}
		    	String message = "Unassigned Successfully";
				if(action.equalsIgnoreCase("Assign")) {
					message = "Assigned Successfully";
				}
				if(recordExist) {
					message = "Assignment is already done!";
				}
				request.setAttribute("ClassSubjectMessage", message);	    	
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
		
		con = null;
		st = null;
		rs = null;
		
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
		
		con = null;
		st = null;
		rs = null;
		
		try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT C.ID AS CLASS, S.NAME AS SUBJECT FROM SUBJECT S, CLASS C,ClassxTeacherxSubject SC "
					+ " WHERE S.ID = SC.SUBJECTID AND C.ID = SC.CLASSID ORDER BY C.ID, S.ID";
			st=con.prepareStatement(query);			
			rs = st.executeQuery();
			String prevClass = "";
			String subjects = "";
			while(rs.next()) {
				subjects = subjects + " | ";
				String currentClass = rs.getString("CLASS");
				String currentSubject = rs.getString("SUBJECT");
				if(!prevClass.equals(currentClass)) {					
					if(prevClass.trim().length() != 0)
						classSubjectsMap.put(Integer.parseInt(prevClass), subjects);
					
					subjects = currentSubject;
					prevClass = currentClass;
					
				}else {
					subjects = subjects + currentSubject;
				}				
			}
			
			if(prevClass.trim().length() != 0)
				classSubjectsMap.put(Integer.parseInt(prevClass), subjects + " |");
			
			rs.close();
			st.close();
			con.close();
		} catch(Exception e){
			System.out.println("\nFailed !\n\n"+e.getMessage());			
		} finally {
			System.out.println("\nDone !\n\n");
		}
    	
		RequestDispatcher req = request.getRequestDispatcher("AssignClassSubject.jsp");
		request.setAttribute("ClassMap", classMap);
		request.setAttribute("SubjectMap", subjectMap);
		request.setAttribute("ClassSubjectMap", classSubjectsMap);
		req.include(request, response);
    }
    
    public boolean isClassAssignedSubject(int classId, int subjectId) {
    	int rowCount = 0;
    	Connection con;
		PreparedStatement st;
		ResultSet rs;
    	try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT COUNT(1) FROM ClassxTeacherxSubject WHERE CLASSID = ? AND SUBJECTID = ?";
			st=con.prepareStatement(query);			
			st.setInt(1, classId);
			st.setInt(2, subjectId);
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