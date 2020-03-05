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

public class GetPetitionPage extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
		req.setAttribute("error", "Invalid Login Details");
		responseJsp.forward(req, res);
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		//check session
		
		
		
		PetitionDB petitionDB = new PetitionDB();
		String id= req.getParameter("petitionId");
		String fetchSQL = "select * from petitions where ID ="+id+"";		
		petitionDB.makeNewConnection();
		
		try {
			PreparedStatement fetchPetition = petitionDB.getConnection().prepareStatement(fetchSQL);
			
			ResultSet result = fetchPetition.executeQuery();
			
			result.next();
			
			req.setAttribute("petitionId", result.getString(1));
			req.setAttribute("petitionTitle", result.getString(2));
			req.setAttribute("petitionContent", result.getString(3));
			req.setAttribute("petitionCreator", result.getString(5));
			req.setAttribute("petitionAddedDate", result.getString(4));
			req.setAttribute("petitionVotes", result.getInt(6));
	
			
			result.close();
			fetchPetition.close();
			
			//fetching comments
			String fetchComments ="select * from comments where PETITIONID ="+id;
			PreparedStatement comments = petitionDB.getConnection().prepareStatement(fetchComments);
			List<CommentDTO> petitionComments = new ArrayList<CommentDTO>();
			CommentDTO tempC;
			ResultSet resultComments = comments.executeQuery();
			
			while(resultComments.next()) {
				tempC = new CommentDTO(resultComments.getString(2), resultComments.getString(3));
				petitionComments.add(tempC);
			}
			resultComments.close();
			comments.close();
			
			req.setAttribute("comments", petitionComments);

			
			petitionDB.endConnection();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		RequestDispatcher responseJsp= req.getRequestDispatcher("PetitionInfo.jsp");
		req.setAttribute("UserEmail", req.getSession().getAttribute("email"));
		responseJsp.forward(req, res);
	}

}
