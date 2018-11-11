package vote;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigInteger;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class vBlock implements java.io.Serializable{

	private static final String ZERO_HASH = Hex.encodeHexString(new byte[32]); //第一個區塊的ZERO_HASH值
	private String hash; //區塊hash值
	private String prevBlockHash; //前一個區塊的hash值
	private String data; //區塊數據
	private String issue; //議題
	private String price; //價格
	private Boolean agree; //同意與否
	private String voter;//選票
	
	private long timeStamp; //區塊創建時間
	private long nonce; //工作量證明計數器
	
	public static vBlock newGenesisBlock (){
		return vBlock.newBlock(ZERO_HASH, "Genesis Block", "issue", "price", true, "voter");
    }
	
	public static vBlock newBlock(String previousHash,String data,String issue, String price, boolean agree,String voter) {
		vBlock block = new vBlock(ZERO_HASH,previousHash, data, issue, price, agree, voter, Instant.now().getEpochSecond(),0);
		vProofOfWork pow = vProofOfWork.newProofOfWork(block);
		vPowResult powResult = pow.run();
		block.setHash(powResult.getHash());
		block.setNonce(powResult.getNonce());
		return block;
	}
	
	@Override
	public String toString() {
		return "Block{" + 
				"hash='" + hash + '\'' + 
				", prevBlockHash='" + prevBlockHash + '\'' +
                ", data='" + data + '\'' +
                ", issue='" + issue + '\'' +
                ", price='" + price + '\'' +
                ", agree='" + agree + '\'' +
                ", voter='" + voter + '\'' +
                ", timeStamp=" + timeStamp +
                ", nonce=" + nonce +
                '}';
	}
	
	/*private void setHash(){
		byte[] prevBlockHashBytes = {};
		if (StringUtils.isNotBlank(this.getPrevBlockHash())) {
			prevBlockHashBytes = new BigInteger(this.getPrevBlockHash(),16).toByteArray();
		}
	
	byte[] headers = ByteUtils.merge(prevBlockHashBytes,this.getData().getBytes(),ByteUtils.toBytes(this.getTimeStamp()));
	
	this.setHash(DigestUtils.sha256Hex(headers));
	}
	*/
}