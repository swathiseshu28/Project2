package swathi.lac;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class StudentReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");
		Connection con;
		PreparedStatement st;
		TreeMap<Integer, String> classMap = new TreeMap<Integer, String>();
		LinkedHashMap<String, String> studentReportMap = new LinkedHashMap<String, String>();
		
    	con = null;
		st = null;
		ResultSet rs;
		
		try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT ID, NAME FROM CLASS ORDER BY ID";
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
		String tdOpen = "<TD class=\"borders\">";
		String tdClose = "</TD>";
		String thOpen = "<TH class=\"borders\">";
		String thClose = "</TH>";
		try	{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
			con = ds.getConnection();
			String query = "SELECT C.NAME AS CLASS, S.ID AS STUDENTID, S.NAME AS STUDENT "
					+ " FROM STUDENT S, CLASS C "
					+ " WHERE S.CLASS = C.ID ORDER BY C.ID, S.ID";
			st=con.prepareStatement(query);			
			rs = st.executeQuery();
			String header = thOpen+"Class"+thClose+thOpen+"Student ID"+thClose+thOpen+"Student Name"+thClose;			
			studentReportMap.put("HEADER", header);
			int rowCount = 1;			
			while(rs.next()) {				
				String currentClass = rs.getString("CLASS");				
				String currentStudentID = rs.getString("STUDENTID");
				String currentStudentName = rs.getString("STUDENT");
				studentReportMap.put(rowCount+"", tdOpen+currentClass+tdClose+tdOpen+currentStudentID+tdClose+tdOpen+currentStudentName+tdClose);
				rowCount++;
			}
			
			rs.close();
			st.close();
			con.close();
		} catch(Exception e){
			System.out.println("\nFailed !\n\n"+e.getMessage());			
		} finally {
			System.out.println("\nDone !\n\n");
		}
    	
		RequestDispatcher req = request.getRequestDispatcher("StudentReport.jsp");		
		request.setAttribute("StudentReportMap", studentReportMap);
		req.include(request, response);
    }    
}