package swathi.lac;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class AssignClassSubjectTeacher extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");    	
		String action = request.getParameter("Action");
		Connection con;
		PreparedStatement st;
		TreeMap<Integer, String> classMap = new TreeMap<Integer, String>();
		TreeMap<Integer, String> subjectMap = new TreeMap<Integer, String>();
		TreeMap<Integer, String> teacherMap = new TreeMap<Integer, String>();
		LinkedHashMap<String, String> classSubjectTeacherMap = new LinkedHashMap<String, String>();
		
    	if(action != null && action.trim().length() != 0) {
	    	    	
	    	String classID = request.getParameter("Class");
	    	String subjectId = request.getParameter("Subject");
	    	String teacherId = request.getParameter("Teacher");
	    	if(classID != null && classID.trim().length() != 0
	    			&& subjectId != null && subjectId.trim().length() != 0
	    			&& teacherId != null && teacherId.trim().length() != 0) {
	    		
	    		try	{
					Context ctx = new InitialContext();
					DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
					con = ds.getConnection();
					String query = "";
					if(action.equalsIgnoreCase("Assign")) {
						query = "UPDATE CLASSXTEACHERXSUBJECT SET TEACHERID = ? WHERE ClassID = ? AND SubjectID = ? ";
					}else {
						query = "UPDATE CLASSXTEACHERXSUBJECT SET TEACHERID = '-' WHERE ClassID = ? AND SubjectID = ? ";
					}
					st=con.prepareStatement(query);					
					if(action != null && action.equals("Assign")) {
						st.setInt(1, Integer.parseInt(teacherId));
						st.setInt(2, Integer.parseInt(classID));
						st.setInt(3, Integer.parseInt(subjectId));
					}else {
						st.setInt(1, Integer.parseInt(classID));
						st.setInt(2, Integer.parseInt(subjectId));
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
	    	}else {
	    		
	    	}
	    	String message = "Unassigned Successfully";
			if(action.equalsIgnoreCase("Assign")) {
				message = "Assigned Successfully";
			}
			request.setAttribute("ClassSubjectTeacherMessage", message);
	    	
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
			String query = "SELECT ID, NAME FROM SUBJECT ORDER BY ID";
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
		
		con = null;
		st = null;
		rs = null;
		String tdOpen = "<TD class=\"borders\">";
		String tdClose = "</TD>";
		String thOpen = "<TH class=\"borders\">";
		String thClose = "</TH>";
		try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT C.NAME AS CLASS, S.NAME AS SUBJECT, T.NAME AS TEACHER "
					+ " FROM CLASSXTEACHERXSUBJECT CTS,  CLASS C, TEACHER T, SUBJECT S "
					+ " WHERE CTS.CLASSID = C.ID AND CTS.TEACHERID = T.ID AND CTS.SUBJECTID = S.ID "
					+ " ORDER BY CLASSID, SUBJECTID";
			st=con.prepareStatement(query);			
			rs = st.executeQuery();
			String prevClass = "";
			String teachers = "";
			String subjects = thOpen+"Class"+thClose;
			for( Map.Entry<Integer, String> entry : subjectMap.entrySet()){
				  //int key = entry.getKey();
				  String value = entry.getValue();	
				  subjects = subjects+thOpen+value+thClose;
			}
			classSubjectTeacherMap.put("Class", subjects);
			while(rs.next()) {				
				String currentClass = rs.getString("CLASS");				
				String currentTeacher = rs.getString("TEACHER");
				
				if(!prevClass.equals(currentClass)) {					
					if(prevClass.trim().length() != 0)
						classSubjectTeacherMap.put(prevClass, tdOpen+prevClass+tdClose+teachers);
					teachers = tdOpen+currentTeacher+tdClose;
					prevClass = currentClass;
					
				}else {
					teachers = teachers + tdOpen+currentTeacher+tdClose;
				}				
			}
			
			if(prevClass.trim().length() != 0)
				classSubjectTeacherMap.put(prevClass, tdOpen+prevClass+tdClose+teachers);
			
			rs.close();
			st.close();
			con.close();
		} catch(Exception e){
			System.out.println("\nFailed !\n\n"+e.getMessage());			
		} finally {
			System.out.println("\nDone !\n\n");
		}
    	
		RequestDispatcher req = request.getRequestDispatcher("AssignClassSubjectTeacher.jsp");
		request.setAttribute("ClassMap", classMap);
		request.setAttribute("SubjectMap", subjectMap);
		request.setAttribute("TeacherMap", teacherMap);
		request.setAttribute("ClassSubjectTeacherMap", classSubjectTeacherMap);
		req.include(request, response);
    }    
}