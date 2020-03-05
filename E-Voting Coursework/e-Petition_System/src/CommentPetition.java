import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommentPetition extends HttpServlet {

public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
		req.setAttribute("error", "Invalid Login Details");
		responseJsp.forward(req, res);
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		if(req.getSession().getAttribute("email").toString().isEmpty()) {
			RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
			req.setAttribute("error", "Invalid Login Details");
			responseJsp.forward(req, res);
		}else if (req.getParameter("comment").isEmpty()) {
			RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
			req.setAttribute("error", "Empty Comment");
			responseJsp.forward(req, res);
		}else {
	
		PetitionDB petitionDB = new PetitionDB();
		String id= req.getParameter("PetitionId");
		String fetchSQL = "select * from MPs where EMAIL ='"+req.getSession().getAttribute("email")+"'";		
		petitionDB.makeNewConnection();
		
		try {
			PreparedStatement fetchMP = petitionDB.getConnection().prepareStatement(fetchSQL);
			
			ResultSet foundMPs = fetchMP.executeQuery();
			
			int count =0;
			
			while(foundMPs.next()) {
				count = count+1;
			}
			foundMPs.close();
			fetchMP.close();
			
			if(count!=0) {
				String insertSQL = "insert into comments (COMMENT,BY_MP,PETITIONID) values ('"+req.getParameter("comment")+"','"+req.getSession().getAttribute("email")+"',"+id+")";
				PreparedStatement PreparedInsert = petitionDB.getConnection().prepareStatement(insertSQL);
				PreparedInsert.executeUpdate();
				PreparedInsert.close();
				petitionDB.endConnection();
				
				RequestDispatcher responseJsp= req.getRequestDispatcher("SuccessPage.jsp");
				req.setAttribute("success", "Your Comment has been posted");
				responseJsp.forward(req, res);
				
			}else {
				petitionDB.endConnection();
				RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
				req.setAttribute("error", "You are not an MP");
				responseJsp.forward(req, res);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
}
