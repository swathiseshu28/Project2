package swathi.lac;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LACLogin
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
				
		String username = request.getParameter("username");
		String password = request.getParameter("password"); 
		int rowCount = 0;
		RequestDispatcher req;
		if(username != null	&& password != null) {
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			
			try	{
				Context ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/LAC");
				con = ds.getConnection();
				String query = "SELECT COUNT(1) FROM USERS WHERE USERNAME = ? AND PASSWORD = ? AND TYPE = 'Admin'";
				st=con.prepareStatement(query);
				st.setString(1, username);
				st.setString(2, password);
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
		}
		if(rowCount > 0) {
			request.getSession().setAttribute("user", username);
			req = request.getRequestDispatcher("Main.jsp");				
		}else {			
			req = request.getRequestDispatcher("index.jsp");
			request.setAttribute("InvalidLogin", "Invalid username or password");
		}
		req.include(request, response);
	}
}
