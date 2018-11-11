package TCHAIN;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Block implements java.io.Serializable
{
	private static final String ZERO_HASH = Hex.encodeHexString(new byte[32]);
	private String hash;
	private int Index;
	private String prevBlockHash;
	private Transaction[] transactions;
	//private String data;
	private long timeStamp;
	private long nonce;
    

public static Block newGenesisBlock(Transaction coinbase)
{
	return Block.newBlock("",1,new Transaction[]{coinbase});
}
public static Block newBlock(String previousHash,int index, Transaction[] transactions) {
    Block block = new Block("",index,previousHash,transactions, Instant.now().getEpochSecond(),0);
    ProofOfWork pow = ProofOfWork.newProofOfWork(block);
    PowResult powResult = pow.run();
    block.setHash(powResult.getHash());
    block.setNonce(powResult.getNonce());
    block.setIndex(index+1);
    return block;
}
public static Block getBlock2()
{
	Blockchain blockchain = Blockchain.initBlockchainFromDB();
	Blockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator();
	Block block=iterator.next();
	
	return block;
}
public byte[] hashTransaction() {
    byte[][] txIdArrays = new byte[this.getTransactions().length][];
    for (int i = 0; i < this.getTransactions().length; i++) {
        txIdArrays[i] = this.getTransactions()[i].getTxId();
    }
    return new MerkleTree(txIdArrays).getRoot().getHash();
}
/*private void setHash() {
    byte[] prevBlockHashBytes = {};
    if (StringUtils.isNotBlank(this.getPrevBlockHash())) {
        prevBlockHashBytes = new BigInteger(this.getPrevBlockHash(), 16).toByteArray();
    }

    byte[] headers = ByteUtils.merge(
           prevBlockHashBytes,
           this.getData().getBytes(),
           ByteUtils.toBytes(this.getTimeStamp()));

    this.setHash(DigestUtils.sha256Hex(headers));
}*/
}
	
