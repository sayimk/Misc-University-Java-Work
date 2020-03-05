import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PublishPetition extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
		req.setAttribute("error", "You took a wrong turn somewhere");
		responseJsp.forward(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String petitionName = req.getParameter("petitionTitle");
		String petitionContents = req.getParameter("petitionContents");
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		PetitionDB petitionDB = new PetitionDB();
		HttpSession session = req.getSession();
		
		if (petitionName.isEmpty()) {
			RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
			req.setAttribute("error", "No Petition Name");
			responseJsp.forward(req, res);
			
		}else if (petitionContents.isEmpty()) {
			RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
			req.setAttribute("error", "No Petition Contents");
			responseJsp.forward(req, res);
			
		}else if(session.getAttribute("email").equals(null)) {
			RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
			req.setAttribute("error", "Please Sign In");
			responseJsp.forward(req, res);	
		}
		
		String SQLCheckQuery = "select * from petitions where TITLE = '"+petitionName+"' && "
				+ "CREATOR = '"+session.getAttribute("email")+"'";

		//starting DB Connection
		
		petitionDB.makeNewConnection();
		
		try {
			PreparedStatement duplicateCheck = petitionDB.getConnection().prepareStatement(SQLCheckQuery);
			
			ResultSet CheckResult = duplicateCheck.executeQuery();
			int checkCount =0;
			
			while(CheckResult.next()) {
				checkCount = checkCount+1;
			}
			
			CheckResult.close();
			duplicateCheck.close();
			
			if (checkCount!=0) {
				petitionDB.endConnection();
				RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
				req.setAttribute("error", "This petition already Exists");
				responseJsp.forward(req, res);	
			}else {
				
				String SQLFindingID = "select ID from petitions";
				int currentId=0;
				
				PreparedStatement fetchLastPetition = petitionDB.getConnection().prepareStatement(SQLFindingID);
				ResultSet ids = fetchLastPetition.executeQuery();
				
				while(ids.next()) {
					currentId = ids.getInt(1);
				}
				
				currentId =currentId+1;
				//inserting new Data
				String SQLInserting = "insert into petitions values ('"+currentId+"','"+petitionName+"',"
						+ "'"+petitionContents+"', '"+date+"','"+session.getAttribute("email")+"',0)";
				
				PreparedStatement insertPetition = petitionDB.getConnection().prepareStatement(SQLInserting);
				insertPetition.executeUpdate();
				petitionDB.endConnection();

				
				RequestDispatcher responseJsp= req.getRequestDispatcher("SuccessPage.jsp");
				req.setAttribute("success", "Your Petition has been Added");
				responseJsp.forward(req, res);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
	}

}
