import java.io.IOException;
import java.security.MessageDigest;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import java.sql.*;

public class registrationServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
		responseJsp.forward(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String date = req.getParameter("dateOfBirth");
		String emailAddress = req.getParameter("emailAddress");
		String confirmEmail = req.getParameter("confirmEmailAddress");
		String password = req.getParameter("password");
		String confirmPassword = req.getParameter("confirmPassword");
		String nic = req.getParameter("nic");
		HttpSession session = req.getSession();
		PetitionDB petitionDB = new PetitionDB();
		Boolean error = false;
		String hpass = null;
		
		try {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes("UTF-8"));
        	hpass= DatatypeConverter.printHexBinary(hash);
		}catch(Exception ex) {
        ex.printStackTrace();
		
		}
		
		//DB Connection to check NIC and email-address
		String sql = "select * from nic_records";
		String recordSQL = "";
		
		
		
		
		
		try {
			
			petitionDB.makeNewConnection();
			PreparedStatement pstmt = petitionDB.getConnection().prepareStatement(sql);
			
			//executing Query
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(nic.equals(rs.getString(1))) {
					error=true;
				}
			}
		
			rs.close();
			pstmt.close();			
			
			//if infomation is valid then add the a new record to the account_record
			if(!error) {
			recordSQL = "insert into account_records values ('"+nic+"','"+emailAddress+"','"+hpass+"','"+firstName+"','"+lastName+"','"+date+"')";
			PreparedStatement addRecord = petitionDB.getConnection().prepareStatement(recordSQL);
			addRecord.executeUpdate();
			addRecord.close();
			session.setAttribute("email", emailAddress);
			}
			petitionDB.endConnection();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(error) {
			RequestDispatcher responseJsp= req.getRequestDispatcher("ErrorPage.jsp");
			req.setAttribute("error", "NIC Already Exists");
			responseJsp.forward(req, res);
		}else {
		
		RequestDispatcher responseJsp= req.getRequestDispatcher("SuccessPage.jsp");
		req.setAttribute("success", "Your account has been created");

		responseJsp.forward(req, res);
		}
	}
}


