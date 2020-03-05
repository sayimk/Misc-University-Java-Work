

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
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
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

public class LoginServlet extends HttpServlet{


	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//suggest using cookie
		HttpSession session = req.getSession();

		if (session.getAttribute("email")!=null) {
			RequestDispatcher responseJsp= req.getRequestDispatcher("PetitionHome.jsp");
			
			//fetch data
			PetitionDB petitionDB = new PetitionDB();
			petitionDB.makeNewConnection();

			String fetchSQLQuery = "select * from petitions";
			try {
				PreparedStatement fetchTables = petitionDB.getConnection().prepareStatement(fetchSQLQuery);
				
				ResultSet petitions = fetchTables.executeQuery();
				petitionSetDTO temp;
				List<petitionSetDTO> petitionList= new ArrayList<petitionSetDTO>();
				
				while(petitions.next()) {
					temp= new petitionSetDTO(petitions.getInt(1),petitions.getString(2),petitions.getString(3),
							petitions.getString(4),petitions.getString(5),petitions.getInt(6));
					
					petitionList.add(temp);
				}
				
				req.setAttribute("petitionList", petitionList);
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			req.setAttribute("email", session.getAttribute("email"));
			responseJsp.forward(req, resp);

		}else {
			RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
			responseJsp.forward(req, resp);
		}
	}
	
	public void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String loginEmail = req.getParameter("email");
		String loginPassword = req.getParameter("password");
		
		String hpass = null;
		
		try {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(loginPassword.getBytes("UTF-8"));
        	hpass= DatatypeConverter.printHexBinary(hash);
		}catch(Exception ex) {
        ex.printStackTrace();
		
		}
		
		
		
		String sqlQuery = "select * from account_records where EMAIl = '"+loginEmail+"' AND PASSWORD = '"+hpass+"'";
		int size =0;
		
		PetitionDB petitionDB = new PetitionDB();
		petitionDB.makeNewConnection();
		
		
		try {
			PreparedStatement statement = petitionDB.getConnection().prepareStatement(sqlQuery);
			
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				size=size+1;
			}
			result.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (size==0) {
			
			RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
			req.setAttribute("error", "Invalid Login Details");
			responseJsp.forward(req, resp);
			
		}else {
			RequestDispatcher responseJsp= req.getRequestDispatcher("PetitionHome.jsp");

			//create new Connection
			petitionDB.makeNewConnection();

			String fetchSQLQuery = "select * from petitions";
			try {
				PreparedStatement fetchTables = petitionDB.getConnection().prepareStatement(fetchSQLQuery);
				
				ResultSet petitions = fetchTables.executeQuery();
				petitionSetDTO temp;
				List<petitionSetDTO> petitionList= new ArrayList<petitionSetDTO>();
				
				while(petitions.next()) {
					temp= new petitionSetDTO(petitions.getInt(1),petitions.getString(2),petitions.getString(3),
							petitions.getString(4),petitions.getString(5),petitions.getInt(6));
					
					petitionList.add(temp);
				}
				
				req.setAttribute("petitionList", petitionList);

			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HttpSession session = req.getSession();
			session.setAttribute("email", req.getParameter("email"));
			req.setAttribute("email", req.getParameter("email"));
			responseJsp.forward(req, resp);
			}
	}

}
