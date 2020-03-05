import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxEmailChecker extends HttpServlet {
	
	//http://localhost:8080/e-Petition_System/AjaxEmailChecker?email=
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	
		String email;
		
		if (!req.getParameter("email").isEmpty()) 
			email = req.getParameter("email");
		else email = "";
		
		String sqlStatement = "select * from account_records where EMAIL ='"+email+"'";
		PetitionDB petitiondb = new PetitionDB();
		
		PrintWriter out = res.getWriter();
		
		if(!email.isEmpty()) {
		petitiondb.makeNewConnection();
		
		try {
			PreparedStatement check = petitiondb.getConnection().prepareStatement(sqlStatement);
			ResultSet result = check.executeQuery();
			int count=0;
			
			while(result.next()) {
				count=count+1;
			}
			
			result.close();
			check.close();
			petitiondb.endConnection();
			
			if(count==0) {
				out.println("This Email is not linked to any existing NICs");
			}else {
				out.println("This Email is linked to an existing NIC");
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else {
			out.println("");
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}
}
