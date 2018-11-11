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

	private static final String ZERO_HASH = Hex.encodeHexString(new byte[32]); //�Ĥ@�Ӱ϶���ZERO_HASH��
	private String hash; //�϶�hash��
	private String prevBlockHash; //�e�@�Ӱ϶���hash��
	private String data; //�϶��ƾ�
	private String issue; //ĳ�D
	private String price; //����
	private Boolean agree; //�P�N�P�_
	private String voter;//�ﲼ
	
	private long timeStamp; //�϶��Ыخɶ�
	private long nonce; //�u�@�q�ҩ��p�ƾ�
	
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