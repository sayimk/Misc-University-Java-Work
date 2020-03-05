package co3099_underground;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

public class Client {

	public static void main(String[] args) {
		
		
		try {
			
			//Getting Network Args
			InetAddress hostAddress = InetAddress.getByName(args[0]);
			int portNo = Integer.parseInt(args[1]);
			String hashID;
			
			//Generating hashed ID
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] hash = md5.digest(args[2].getBytes());
			String stringHash = DatatypeConverter.printHexBinary(hash);
				
			//Getting 8 characters for ID
			hashID= stringHash.substring(0, 8);
			System.out.println("Your Hashed User ID is "+ hashID);
			
			
			//Connecting to Server
			System.out.println("Connecting to Server...");
			Socket ServerConnection = new Socket(hostAddress, portNo);
			
			System.out.println("Setting up I/O");
			
			//Setting up Reader and Writers
			BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(ServerConnection.getOutputStream()));
			BufferedReader fromServer = new BufferedReader(new InputStreamReader(ServerConnection.getInputStream()));
			
			toServer.write(hashID);
			toServer.newLine();
			toServer.flush();
					
			System.out.println("Sent ID");
			
		//	while(!fromServer.ready())
		//		System.out.println("Waiting for Server Signature...");
			
			
			Signature serverSignature = Signature.getInstance("SHA1withRSA");
			
			//using Server Private ATM
			ObjectInputStream serverPublic = new ObjectInputStream(new FileInputStream("Server.pub"));
			PublicKey serverPubKey = (PublicKey) serverPublic.readObject();
			serverPublic.close();
		
			serverSignature.initVerify(serverPubKey);
			
	
			
	        long t1 = (new Date()).getTime();
			String signatureData  = "GOLDSTEIN";
			ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
			buffer.putLong(t1);
			
				//putting data in 
			serverSignature.update(buffer);
			serverSignature.update(signatureData.getBytes());
			
			String sigBase64 = fromServer.readLine();
						
			byte[] sigToTest = DatatypeConverter.parseBase64Binary(sigBase64);
			
			
			if(serverSignature.verify(sigToTest)) {
			
			System.out.println("Signature Verified");
			
			String encryptedMessage = fromServer.readLine();
			System.out.println("Response Received");
			
			
			toServer.flush();
			toServer.close();
			System.out.println("Disconnecting from Server");
			System.out.println();
			
			//Decrypting Message
			
			//Setting up RSA Cipher for Decryption
			ObjectInputStream secretKeyFile = new ObjectInputStream(new FileInputStream((args[2]+".prv")));
			PrivateKey sKey = (PrivateKey) secretKeyFile.readObject();
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");;
			cipher.init(Cipher.DECRYPT_MODE, sKey);
			secretKeyFile.close();
			
			//Converting Message from Base64 to Bytes
			byte[] enMessageBytes = DatatypeConverter.parseBase64Binary(encryptedMessage);
			byte[] decryptedMessageBytes = cipher.doFinal(enMessageBytes);
			String message = new String(decryptedMessageBytes);
			
			System.out.println("Successfully Decrypted Messages");

			System.out.println("");
			System.out.println("Your Messages:");
			System.out.println(message);
			
			}else {
				toServer.flush();
				toServer.close();
				System.out.println("Server Not Verified, Disconnected");
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
