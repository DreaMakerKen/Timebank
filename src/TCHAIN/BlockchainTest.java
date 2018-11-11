package TCHAIN;
import java.util.Set;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
public class BlockchainTest {
	  


final static Base64.Decoder decoder = Base64.getDecoder();
final Base64.Encoder encoder = Base64.getEncoder();
	public static String createWallet() throws Exception {
	        Wallet wallet = WalletUtils.getInstance().createWallet();
	        System.out.print("wallet address : " + wallet.getAddress());
	        byte[] key=wallet.getPrivateKey().getEncoded();
	        
	        String ss2=Base64.getEncoder().encodeToString(key);
	        
	        return wallet.getAddress()+" "+ss2;
	    }
	public static void printAddresses() {
        Set<String> addresses = WalletUtils.getInstance().getAddresses();
        if (addresses == null || addresses.isEmpty()) {
        	System.out.print("There isn't address");
            return;
        }
        for (String address : addresses) {
        	System.out.print("Wallet address: " + address+"\n");
            
        }
    }
	 public static void createBlockchain(String address) {
	        Blockchain blockchain = Blockchain.createBlockchain(address);
	        UTXOSet utxoSet = new UTXOSet(blockchain);
	        utxoSet.reIndex();
	        System.out.print("Done ! ");
	    }
	 public static int getBalance(String address) {
	      
	        try {
	            Base58Check.base58ToBytes(address);
	        } catch (Exception e) {
	        	System.out.print("ERROR: invalid wallet address"+ e);
	            throw new RuntimeException("ERROR: invalid wallet address", e);
	        }

	       
	        byte[] versionedPayload = Base58Check.base58ToBytes(address);
	        byte[] pubKeyHash = Arrays.copyOfRange(versionedPayload, 1, versionedPayload.length);

	        Blockchain blockchain = Blockchain.createBlockchain(address);
	        UTXOSet utxoSet = new UTXOSet(blockchain);

	        TXOutput[] txOutputs = utxoSet.findUTXOs(pubKeyHash);
	        int balance = 0;
	        if (txOutputs != null && txOutputs.length > 0) {
	            for (TXOutput txOutput : txOutputs) {
	                balance += txOutput.getValue();
	            }
	        }
	        System.out.printf("Balance of '%s': %d\n", address, balance);
	        return balance;
	    }
	 public static Block send(String from,String privatekey, String to, int amount,String kind,String data,String miner)throws Exception {
		
	        try {
	            Base58Check.base58ToBytes(from);
	        } catch (Exception e) {
	        	System.out.print("ERROR: sender address invalid ! address=" + from+e);
	            throw new RuntimeException("ERROR: sender address invalid ! address=" + from, e);
	        }
	       
	        try {
	            Base58Check.base58ToBytes(to);
	        } catch (Exception e) {
	        	System.out.print("ERROR: receiver address invalid ! address=" + to+ e);
	            throw new RuntimeException("ERROR: receiver address invalid ! address=" + to, e);
	        }
	        if (amount <0) {
	        	System.out.print("ERROR: amount invalid ! amount=" + amount);
	            throw new RuntimeException("ERROR: amount invalid ! amount=" + amount);
	        }
	        
	        //比對私鑰是否正確
	        	 byte[] bytes2 = Base64.getDecoder().decode(privatekey);
	        	 Security.addProvider(new BouncyCastleProvider());
	             
	             KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
	         	 KeyFactory kf = KeyFactory.getInstance("ECDSA",BouncyCastleProvider.PROVIDER_NAME);
	         	 PrivateKey privateKey2 = kf.generatePrivate(new PKCS8EncodedKeySpec(bytes2));
	         	 BCECPrivateKey privateKey = (BCECPrivateKey)privateKey2;
	         	 KeyFactory keyFactory = KeyFactory.getInstance("ECDSA",BouncyCastleProvider.PROVIDER_NAME);;

        	     BigInteger d = privateKey.getD();
        	     org.bouncycastle.jce.spec.ECParameterSpec ecSpec = 
        	    		privateKey.getParameters();
        	     ECPoint Q = privateKey.getParameters().getG().multiply(d);

      	        org.bouncycastle.jce.spec.ECPublicKeySpec pubSpec = new 
        	    org.bouncycastle.jce.spec.ECPublicKeySpec(Q, ecSpec);
        	    PublicKey publicKeyGenerated = keyFactory.generatePublic(pubSpec);
        	    //System.out.println(publicKeyGenerated);
        	     byte[]  publicKeyBytes = ((BCECPublicKey) publicKeyGenerated).getQ().getEncoded(false);
        	     byte[] ripemdHashedKey = BtcAddressUtils.ripeMD160Hash(publicKeyBytes);
                 ByteArrayOutputStream addrStream = new ByteArrayOutputStream();
                 addrStream.write((byte) 0);
                 addrStream.write(ripemdHashedKey);
                 byte[] versionedPayload = addrStream.toByteArray();
                 byte[] checksum = BtcAddressUtils.checksum(versionedPayload);
                 addrStream.write(checksum);
                 byte[] binaryAddress = addrStream.toByteArray();
	        	 String ss=Base58Check.rawBytesToBase58(binaryAddress);
	        	 if(!ss.equals(from))
	        	 {
	        		 System.out.print("ERROR: wrong privatekey ");
	 	             throw new RuntimeException("ERROR: wrong privatekey");
	        	 }
	       
	        Blockchain blockchain = Blockchain.createBlockchain(miner);
	        
	        Transaction transaction = Transaction.newUTXOTransaction(from,publicKeyBytes,privatekey, to, amount, blockchain,kind,data);
	       
	        Transaction rewardTx = Transaction.newCoinbaseTX(miner, "");
	        Block newBlock = blockchain.mineBlock(new Transaction[]{transaction, rewardTx});
	        new UTXOSet(blockchain).update(newBlock);
	        System.out.print("Success!");
	      // RocksDBUtils.getInstance().closeDB();
	        return newBlock;
	    }
	 public static void printChain() throws Exception {
	        Blockchain blockchain = Blockchain.initBlockchainFromDB();
	        for (Blockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hashNext(); ) {
	            Block block = iterator.next();
	            if (block != null) {
	                boolean validate = ProofOfWork.newProofOfWork(block).validate();
	                Transaction[] tx=block.getTransactions();
	                int amount=tx[0].getOutputs()[0].getValue();
	                
	               System.out.println("{\nindex:"+block.getIndex()+"\nhash:"+block.getHash()+"\nPrevBlockHash"+block.getPrevBlockHash()+"\ntimestamp:"+block.getTimeStamp()+"\nfrom:"+tx[0].getFrom()+"\nto:"+tx[0].getTo()+"\nkind:"+tx[0].getKind()+"\nData:"+tx[0].getData()+"\namount:"+amount+"\nvalidate = " + validate+"\n}");
	               // System.out.println(block.toString()+"validate = " + validate);
	               
	            }
	        }
	    }
    public static void main(String[] args) {
    	Scanner sc = new Scanner(System.in);
 
    	 //String strpri=sc.nextLine();
    	
    	 //byte[] bytes2 = Base64.getDecoder().decode(strpri);
    	 
        try {
        	//createWallet();
        	//printAddresses();
        	//createBlockchain("1CkdJLnnUyBBGDp4vb9aF3xC683VPs4Z6f");
        	
        	
        	
        	// Security.addProvider(new BouncyCastleProvider());
           
           // KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        	//KeyFactory kf = KeyFactory.getInstance("ECDSA",BouncyCastleProvider.PROVIDER_NAME);
        	//PrivateKey privateKey2 = kf.generatePrivate(new PKCS8EncodedKeySpec(bytes2));
        //	System.out.println(privateKey2);
           // BCECPrivateKey privateKey = (BCECPrivateKey)privateKey2;
        	//System.out.println(privateKey);
        	//System.out.println(Arrays.toString(keyPair.getPrivate().getEncoded()));
        	//System.out.println(RocksDBUtils.getInstance().getLastIndex());
       // send("1DFVSsd45fUD4R1eCDDcNGqdsqAWtofXT",strpri,"1CkdJLnnUyBBGDp4vb9aF3xC683VPs4Z6f",10,"wash","我是誰","1GkpE7BfaqfdaEavpbn5mMnb2adeZqvGoS");
        	//Wallet senderWallet = WalletUtils.getInstance().getWallet("1DFVSsd45fUD4R1eCDDcNGqdsqAWtofXT");
        //	System.out.println(senderWallet.getPublicKey());
                //	byte[] pubkey=senderWallet.getPublicKey();
        	//byte[] key=senderWallet.getPrivateKey().getEncoded();
        	//String ss=Base64.getEncoder().encodeToString(pubkey);
        //	byte[] bytes = Base64.getDecoder().decode(ss);
        	//String ss2=Base64.getEncoder().encodeToString(key);
        	//System.out.println(ss2);
        	//byte[] bytes3 = Base64.getDecoder().decode(ss2);
        //	System.out.println(ss);
        	
        	// KeyFactory keyFactory = KeyFactory.getInstance("ECDSA",BouncyCastleProvider.PROVIDER_NAME);;
//
        	  //  BigInteger d = privateKey.getD();
        	  //  org.bouncycastle.jce.spec.ECParameterSpec ecSpec = 
        	   // 		privateKey.getParameters();
        	//    ECPoint Q = privateKey.getParameters().getG().multiply(d);
//
        	  //  org.bouncycastle.jce.spec.ECPublicKeySpec pubSpec = new 
        	  //  org.bouncycastle.jce.spec.ECPublicKeySpec(Q, ecSpec);
        	  //  PublicKey publicKeyGenerated = keyFactory.generatePublic(pubSpec);
        	  //  System.out.println(publicKeyGenerated);
        	 //   byte[] publicKeyBytes = ((BCECPublicKey) publicKeyGenerated).getQ().getEncoded(false);
        	 //   String ss=Base64.getEncoder().encodeToString(publicKeyBytes);
        	  //  System.out.println(publicKeyBytes);
        	 //   byte[] ripemdHashedKey = BtcAddressUtils.ripeMD160Hash(publicKeyBytes);

                
              //  ByteArrayOutputStream addrStream = new ByteArrayOutputStream();
              //  addrStream.write((byte) 0);
             //   addrStream.write(ripemdHashedKey);
            //    byte[] versionedPayload = addrStream.toByteArray();
            //    byte[] checksum = BtcAddressUtils.checksum(versionedPayload);
            //    addrStream.write(checksum);
            //    byte[] binaryAddress = addrStream.toByteArray();
               // System.out.println(Base58Check.rawBytesToBase58(binaryAddress));
        	 //System.out.println(RocksDBUtils.getInstance().getLastIndex());
        	      // 	 getBalance("1KPohpS2JTEBn6btMKjxEVmsLYyB1V3Pps");
         getBalance("1DFVSsd45fUD4R1eCDDcNGqdsqAWtofXT");
         getBalance("1CkdJLnnUyBBGDp4vb9aF3xC683VPs4Z6f");
        	printChain();
        	//printChain();
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
