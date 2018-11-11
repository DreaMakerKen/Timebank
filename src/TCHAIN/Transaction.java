package TCHAIN;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;
import java.math.BigInteger;
import vote.*;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Transaction implements java.io.Serializable{

    private static final int SUBSIDY = 10;

    private byte[] txId;
    private TXInput[] inputs;
    private TXOutput[] outputs;
    private long createTime;
    private String kind;
    private String data; 
    private String from;
    private String to;
    public byte[] hash() {
    	
        byte[] serializeBytes = SerializeUtils.serialize(this);
        Transaction copyTx = (Transaction) SerializeUtils.deserialize(serializeBytes);
        copyTx.setTxId(new byte[]{});
        return DigestUtils.sha256(SerializeUtils.serialize(copyTx));
    }
    public static Transaction newCoinbaseTX(String to, String data) {
        if (StringUtils.isBlank(data)) {
            data = String.format("Reward to '%s'", to);
        }
       
        TXInput txInput = new TXInput(new byte[]{}, -1, null, data.getBytes());
       
        TXOutput txOutput = TXOutput.newTXOutput(SUBSIDY, to);
     
        Transaction tx = new Transaction(null, new TXInput[]{txInput},
                new TXOutput[]{txOutput}, System.currentTimeMillis(),"coinbase","coinbase","coinbase",to);
       
        tx.setTxId(tx.hash());
        return tx;
    }

    public boolean isCoinbase() {
        return this.getInputs().length == 1
                && this.getInputs()[0].getTxId().length == 0
                && this.getInputs()[0].getTxOutputIndex() == -1;
    }


    public static Transaction newUTXOTransaction(String from,byte[] pubkey,String privitekey, String to, int amount, Blockchain blockchain,String kind,String data) throws Exception {
        
       // Wallet senderWallet = WalletUtils.getInstance().getWallet(from);
       // byte[] pubKey = senderWallet.getPublicKey();
    	byte[] pubKey = pubkey;
    	byte[] privateKey = Base64.getDecoder().decode(privitekey);
    	Security.addProvider(new BouncyCastleProvider());
        
     
    	KeyFactory kf = KeyFactory.getInstance("ECDSA",BouncyCastleProvider.PROVIDER_NAME);
    	PrivateKey privateKey2 = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
    	BCECPrivateKey PrivateKey = (BCECPrivateKey)privateKey2;
        byte[] pubKeyHash = BtcAddressUtils.ripeMD160Hash(pubKey);
           amount=amount*Integer.parseInt(vBlockchainTest.getPrice(kind));
        SpendableOutputResult result = new UTXOSet(blockchain).findSpendableOutputs(pubKeyHash, amount);
       
        int accumulated = result.getAccumulated();
        Map<String, int[]> unspentOuts = result.getUnspentOuts();

        if (accumulated < amount) {
            System.out.print("ERROR: Not enough funds ! accumulated=" + accumulated + ", amount=" + amount);
            throw new RuntimeException("ERROR: Not enough funds ! ");
        }
        Iterator<Map.Entry<String, int[]>> iterator = unspentOuts.entrySet().iterator();

        TXInput[] txInputs = {};
        while (iterator.hasNext()) {
            Map.Entry<String, int[]> entry = iterator.next();
            String txIdStr = entry.getKey();
            int[] outIds = entry.getValue();
            byte[] txId = Hex.decodeHex(txIdStr.toCharArray());
            for (int outIndex : outIds) {
                txInputs = ArrayUtils.add(txInputs, new TXInput(txId, outIndex, null, pubKey));
            }
        }

        TXOutput[] txOutput = {};
        txOutput = ArrayUtils.add(txOutput, TXOutput.newTXOutput(amount, to));
        if (accumulated > amount) {
            txOutput = ArrayUtils.add(txOutput, TXOutput.newTXOutput((accumulated - amount), from));
        }

        Transaction newTx = new Transaction(null, txInputs, txOutput, System.currentTimeMillis(),kind,data,from,to);
        newTx.setTxId(newTx.hash());

        blockchain.signTransaction(newTx, PrivateKey);

        return newTx;
    }
    public Transaction trimmedCopy() {
        TXInput[] tmpTXInputs = new TXInput[this.getInputs().length];
        for (int i = 0; i < this.getInputs().length; i++) {
            TXInput txInput = this.getInputs()[i];
            tmpTXInputs[i] = new TXInput(txInput.getTxId(), txInput.getTxOutputIndex(), null, null);
        }

        TXOutput[] tmpTXOutputs = new TXOutput[this.getOutputs().length];
        for (int i = 0; i < this.getOutputs().length; i++) {
            TXOutput txOutput = this.getOutputs()[i];
            tmpTXOutputs[i] = new TXOutput(txOutput.getValue(), txOutput.getPubKeyHash());
        }

        return new Transaction(this.getTxId(), tmpTXInputs, tmpTXOutputs, this.getCreateTime(),kind,data,from,to);
    }
    public void sign(BCECPrivateKey privateKey, Map<String, Transaction> prevTxMap) throws Exception {
      
        if (this.isCoinbase()) {
            return;
        }
       
        for (TXInput txInput : this.getInputs()) {
            if (prevTxMap.get(Hex.encodeHexString(txInput.getTxId())) == null) {
                throw new RuntimeException("ERROR: Previous transaction is not correct");
            }
        }

        
        Transaction txCopy = this.trimmedCopy();

        Security.addProvider(new BouncyCastleProvider());
        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", BouncyCastleProvider.PROVIDER_NAME);
        ecdsaSign.initSign(privateKey);

        for (int i = 0; i < txCopy.getInputs().length; i++) {
            TXInput txInputCopy = txCopy.getInputs()[i];
            
            Transaction prevTx = prevTxMap.get(Hex.encodeHexString(txInputCopy.getTxId()));
           
            TXOutput prevTxOutput = prevTx.getOutputs()[txInputCopy.getTxOutputIndex()];
            txInputCopy.setPubKey(prevTxOutput.getPubKeyHash());
            txInputCopy.setSignature(null);
       
            txCopy.setTxId(txCopy.hash());
            txInputCopy.setPubKey(null);

           
            ecdsaSign.update(txCopy.getTxId());
            byte[] signature = ecdsaSign.sign();

            
            this.getInputs()[i].setSignature(signature);
        }
    }
    public boolean verify(Map<String, Transaction> prevTxMap) throws Exception {
        
        if (this.isCoinbase()) {
            return true;
        }

        
        for (TXInput txInput : this.getInputs()) {
            if (prevTxMap.get(Hex.encodeHexString(txInput.getTxId())) == null) {
                throw new RuntimeException("ERROR: Previous transaction is not correct");
            }
        }

    
        Transaction txCopy = this.trimmedCopy();

        Security.addProvider(new BouncyCastleProvider());
        ECParameterSpec ecParameters = ECNamedCurveTable.getParameterSpec("secp256k1");
        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", BouncyCastleProvider.PROVIDER_NAME);

        for (int i = 0; i < this.getInputs().length; i++) {
            TXInput txInput = this.getInputs()[i];
          
            Transaction prevTx = prevTxMap.get(Hex.encodeHexString(txInput.getTxId()));
            
            TXOutput prevTxOutput = prevTx.getOutputs()[txInput.getTxOutputIndex()];

            TXInput txInputCopy = txCopy.getInputs()[i];
            txInputCopy.setSignature(null);
            txInputCopy.setPubKey(prevTxOutput.getPubKeyHash());
            
            txCopy.setTxId(txCopy.hash());
            txInputCopy.setPubKey(null);

            
            BigInteger x = new BigInteger(1, Arrays.copyOfRange(txInput.getPubKey(), 1, 33));
            BigInteger y = new BigInteger(1, Arrays.copyOfRange(txInput.getPubKey(), 33, 65));
            ECPoint ecPoint = ecParameters.getCurve().createPoint(x, y);

            ECPublicKeySpec keySpec = new ECPublicKeySpec(ecPoint, ecParameters);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(txCopy.getTxId());
            if (!ecdsaVerify.verify(txInput.getSignature())) {
                return false;
            }
        }
        return true;
    }
}