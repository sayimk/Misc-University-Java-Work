import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PetitionSignServlet extends HttpServlet {

	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
		req.setAttribute("error", "You are not Signed in");
		responseJsp.forward(req, res);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException{
		
		String petitionId = req.getParameter("PetitionId");
		HttpSession session = req.getSession();
		String SQLInsert = "insert into petition_signitures (EMAIL,PETITIONID) values ('"+session.getAttribute("email")+"',"+petitionId+")";
		String SQLUpdate = ("update petitions set SIGN =SIGN+1 WHERE ID ="+petitionId);
		String SQLCheck = "select * from petition_signitures where EMAIL = '"+session.getAttribute("email")+"' && PETITIONID ="+petitionId;
		
		if(petitionId.isEmpty()) {
			RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
			req.setAttribute("error", "Invalid Petition ID");
			responseJsp.forward(req, res);
		}
		
		if(session.getAttribute("email").toString().isEmpty()) {
			RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
			req.setAttribute("error", "Please Log in");
			responseJsp.forward(req, res);
		}
		
		PetitionDB petitionDB = new PetitionDB();
		petitionDB.makeNewConnection();

		try {
			
			PreparedStatement checkPrep = petitionDB.getConnection().prepareStatement(SQLCheck);
			ResultSet result = checkPrep.executeQuery();
			int count=0;
			while(result.next()) {
				count=count+1;
			}
			result.close();
			checkPrep.close();
			
			if(count==0) {
			
			PreparedStatement insert = petitionDB.getConnection().prepareStatement(SQLInsert);
			insert.executeUpdate();			
			insert.close();
			
			PreparedStatement update = petitionDB.getConnection().prepareStatement(SQLUpdate);
			update.executeUpdate();
			update.close();
			
			petitionDB.endConnection();
			
			RequestDispatcher responseJsp= req.getRequestDispatcher("SuccessPage.jsp");
			req.setAttribute("success", "You Have signed this Petition");
			responseJsp.forward(req, res);
			
			}else {
				petitionDB.endConnection();
				RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
				req.setAttribute("error", "You Have signed this Petition already");
				responseJsp.forward(req, res);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
