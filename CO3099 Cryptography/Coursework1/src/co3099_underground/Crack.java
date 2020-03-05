package co3099_underground;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;

public class Crack {

	public static void main(String[] args) {
		
		
		try {
		//getting Winstons pub Key
		//using Server Private ATM
			
			
		ObjectInputStream serverPublic = new ObjectInputStream(new FileInputStream("winston.pub"));
		PublicKey publicKey = (PublicKey) serverPublic.readObject();		
		serverPublic.close();
			
		//Casting to RSAPublicKey
		RSAPublicKey RSAKey = (RSAPublicKey) publicKey;
			
		//getting Exponent and Modulus
		
		BigInteger pubExponent = RSAKey.getPublicExponent();
		BigInteger pubModulus = RSAKey.getModulus();
		
		BigInteger firstPrime = null;
		BigInteger secondPrime = null;
		
		boolean foundFirst = false;
		boolean foundSecond = false;
		boolean finished = false;
		int currentN = 2;
		
		System.out.println("Started");
		
		
		while((BigInteger.valueOf(currentN).compareTo(pubModulus)==-1)&&(!finished)) {
		
			if(pubModulus.mod(BigInteger.valueOf(currentN))==BigInteger.ZERO) {
			
			System.out.println(currentN +" Is Factor");
			}else {
				System.out.println(currentN +" Not Factor");

			}

			if(pubModulus.mod(BigInteger.valueOf(currentN))==BigInteger.ZERO) {

				
				BigInteger otherFactor = pubModulus.divide(BigInteger.valueOf(currentN));
			
				if(otherFactor.isProbablePrime(100)&&BigInteger.valueOf(currentN).isProbablePrime(100)) {
					firstPrime = otherFactor;
					secondPrime = BigInteger.valueOf(currentN);
					finished = true;
				}
					
				
			}
			
			currentN=currentN+1;
		}

		BigInteger one = BigInteger.valueOf(1);
		
		
		BigInteger phi = (firstPrime.subtract(one)).multiply((secondPrime.subtract(one)));
		
		BigInteger d =pubExponent.modInverse(phi);
		
		
		System.out.println("Crack Finished");

		System.out.println("p = "+firstPrime);
			
		System.out.println("q = "+secondPrime);
		
		System.out.println("d = " +d);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
