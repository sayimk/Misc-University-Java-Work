package co3099_underground;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

public class Server {

	public static void main(String[] args) {
		
		//variables
		ArrayList<String> userIdList = new ArrayList<String>();
		ArrayList<String> plaintextList = new ArrayList<String>();
		ArrayList<String> userName8MD5 = new ArrayList<String>();
		ArrayList<String> EncryptedMessages = new ArrayList<String>();



		
		try {
			BufferedReader userIDReader = new BufferedReader(new FileReader("userid.txt"));
			BufferedReader plaintextReader = new BufferedReader(new FileReader("plaintext.txt"));
			
			boolean eof =false;
			while(!eof) {
				
				String userIdline = userIDReader.readLine();
				String plainTextLine = plaintextReader.readLine();
			
				if ((userIdline!=null)&&(plainTextLine!=null)) {
					userIdList.add(userIdline);
					plaintextList.add(plainTextLine);
				
				}else
					eof = true;

			}
			
			userIDReader.close();
			plaintextReader.close();
			
			
			
			//testing MD5
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			
			
			for (int i =0; i<userIdList.size(); i++) {
				byte[] hash = md5.digest(userIdList.get(i).getBytes());
			
				//DatatypeConverter converts bytes[] to String Representation
				String stringHash = DatatypeConverter.printHexBinary(hash);
				
				userName8MD5.add(stringHash.substring(0, 8));
			
				//System.out.println(userName8MD5.get(i));
			}
			
			//Encrypting Messages using RSA then Base64
			//Vars
			String pubKeyFileName;
			ObjectInputStream publicKeyFile;
			PublicKey key;
			Cipher cipher;
			byte[] encrypted;
			
			
			for(int i=0; ((i<userIdList.size())&&(i<plaintextList.size())); i++) {
			
				pubKeyFileName = (userIdList.get(i)+".pub");
				
				
				publicKeyFile = new ObjectInputStream(new FileInputStream(pubKeyFileName));
				key=(PublicKey)publicKeyFile.readObject();
				publicKeyFile.close();
			
				
				cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.ENCRYPT_MODE, key);
				encrypted = cipher.doFinal(plaintextList.get(i).getBytes());
				
				EncryptedMessages.add(DatatypeConverter.printBase64Binary(encrypted));
			
				
				
	
				
				
			}
			
			
			//Writing to external file cipherText
			File outputCipherFile = new File("ciphertext.txt");
									
			if(!outputCipherFile.exists())
				outputCipherFile.createNewFile();
			
			BufferedWriter cipherTextWriter = new BufferedWriter(new FileWriter(outputCipherFile));
			String outputString;
			
			for (int i =0; ((i<userName8MD5.size())&&(i<EncryptedMessages.size())); i++) {
			
				outputString = userName8MD5.get(i)+" "+ EncryptedMessages.get(i);
				cipherTextWriter.write(outputString);
								
				cipherTextWriter.newLine();
			}
			
			System.out.println("Encrypted all messages");
			
			cipherTextWriter.flush();
			cipherTextWriter.close();
			
			
		/*	
			//Secret Key Decryption Test
			ObjectInputStream secretKeyFile = new ObjectInputStream(new FileInputStream("winter.prv"));
			PrivateKey sKey = (PrivateKey) secretKeyFile.readObject();
			secretKeyFile.close();
			cipher.init(Cipher.DECRYPT_MODE, sKey);
			
			byte[] decrypted = cipher.doFinal(encrypted);

			//System.out.println(DatatypeConverter.printByte(val)(decrypted));
			
			String test = new String(decrypted);
			
			System.out.println(test);
			*/
			
		} catch (FileNotFoundException e) {
			
			System.out.println("Unable to locate/read userId file");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//starting server on localhost on a arguement port Number
		try {

			
			int portNumber = Integer.parseInt(args[0]);
			ServerSocket serverSocket = new ServerSocket(portNumber, 0, InetAddress.getLoopbackAddress());
			
			System.out.println();
			System.out.println("Server Started and Listening to "+serverSocket.getInetAddress()+": "+serverSocket.getLocalPort());
			//While true to always listen on the port
			while(true) {
			
				System.out.println("Waiting for Clients");

				
				//Listening and waiting for incoming requests
				Socket client = serverSocket.accept();
				System.out.println("New Request Accepted");
			
				//Setting up Reader and Writer
				BufferedWriter toClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
				//Waiting for user ID			
				while(!fromClient.ready()) 
					System.out.println("Waiting for User ID");
			
				//Reading User ID
				String userID = fromClient.readLine();
				System.out.println("Completing Request "+userID);

				
				
				System.out.println("Generating Signture");
				//Generating Server Signature
				Signature serverSignature = Signature.getInstance("SHA1withRSA");
				
					//using Server Private ATM
				ObjectInputStream serverPrivate = new ObjectInputStream(new FileInputStream("Server.prv"));
				PrivateKey serverKey = (PrivateKey) serverPrivate.readObject();
				serverPrivate.close();
				
				serverSignature.initSign(serverKey);
				
					//creating timestamp and 
				
		        long t1 = (new Date()).getTime();
				String signatureData  = "GOLDSTEIN";
				ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
				buffer.putLong(t1);
				
					//putting data in 
				serverSignature.update(buffer);
				serverSignature.update(signatureData.getBytes());
				
				byte[] signature = serverSignature.sign();
								
				System.out.println("Sending Signature");
				toClient.write(DatatypeConverter.printBase64Binary(signature));
				toClient.newLine();
				toClient.flush();
				
				
				//Searching for Messages for the Requested User ID
				int messagePos=-1;
			
				for(int i=0; i<userName8MD5.size(); i++) {
			
					if(userName8MD5.get(i).equals(userID))
						messagePos = i;
					
				}
				
				if(messagePos>=0) {
					
					toClient.write(EncryptedMessages.get(messagePos));
					toClient.newLine();
					toClient.flush();
					
				}else {
					toClient.write("No Messages");
					toClient.newLine();
					toClient.flush();

				}
			
				System.out.println("Completed Request "+userID);
				System.out.println("Disconnecting Client...");

				toClient.flush();
				toClient.close();
				
				client.close();
				
				System.out.println("Client Disconnected");
				System.out.println(" ");

				
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
	}

}
