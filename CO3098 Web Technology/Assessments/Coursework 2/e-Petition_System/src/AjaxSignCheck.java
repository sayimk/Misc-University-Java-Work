import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxSignCheck extends HttpServlet{
 
	//http://localhost:8080/e-Petition_System/AjaxSignCheck?email=&id=
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	
		String email = null;
		String id = null;
		
		if (!req.getParameter("email").isEmpty()) 
			email = req.getParameter("email");
		else email = "";
		
		if (!req.getParameter("id").isEmpty()) 
			id = req.getParameter("id");
		else id = "";
		
		String sqlStatement = "select * from petition_signitures where email = '"+email+"' && PETITIONID ="+id;
		PetitionDB petitiondb = new PetitionDB();
		
		PrintWriter out = res.getWriter();
		
		if((!email.isEmpty())&& (!id.isEmpty())) {
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
			
			if(count!=0) {
				out.println("You Have Already Signed this Petition");
			}else {
				out.println(" you havent signed");
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
