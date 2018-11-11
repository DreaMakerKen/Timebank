package vote;

import lombok.Data;
import java.math.BigInteger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

@Data

public class vProofOfWork {
	public static final int TARGET_BITS = 10;//難度目標位
	private vBlock block; //區塊
	private BigInteger target; //難度目標值
	
	private vProofOfWork(vBlock block,BigInteger target) { //創建新的工作量證明 設定難度目標值 
		this.block = block; 							 //對1進行移位運算，將1向左移動(256 - TARGET_BITS)位
		this.target = target;							 //得到我們的難度目標值
	}
	
	public static vProofOfWork newProofOfWork(vBlock block) { 					//設定一個難度目標位TARGET_BITS
		BigInteger targetValue = BigInteger.valueOf(1).shiftLeft((256 - TARGET_BITS)); //表示最終挖礦挖出來Hash值 轉化為二進制後
		return new vProofOfWork(block, targetValue); 							//與256相比長度少了多少bit，也即二進制前面有多少bit是零.
	}
	
	public vPowResult run() {
		long nonce = 0;
		String shaHex = "";
		System.out.printf("Mining the block containing: %s \n", this.getBlock().getData());
		long startTime = System.currentTimeMillis();
		while (nonce < Long.MAX_VALUE){
			byte[] data = this.prepareData(nonce);
			shaHex = DigestUtils.sha256Hex(data);
			if(new BigInteger(shaHex, 16).compareTo(this.target)==-1) {
				System.out.printf("Elapsed Time: %s seconds \n", (float)(System.currentTimeMillis()-startTime)/1000);
				System.out.printf("corrent hash Hex: %s \n\n",shaHex);
				break;
			} else {
				nonce++;
			}
		}
		return new vPowResult(nonce, shaHex);
	}
		
	public boolean validate() {
		byte[] data = this.prepareData(this.getBlock().getNonce());
		return new BigInteger(DigestUtils.sha256Hex(data), 16).compareTo(this.target) == -1;	
	}
		
	private byte[] prepareData(long nonce) {
		byte[] prevBlockHashBytes = {};
		if (StringUtils.isNotBlank(this.getBlock().getPrevBlockHash())) {
			prevBlockHashBytes = new BigInteger(this.getBlock().getPrevBlockHash(),16).toByteArray();
		}
			
		return ByteUtils.merge(
				prevBlockHashBytes,
				this.getBlock().getData().getBytes(),
				ByteUtils.toBytes(this.getBlock().getTimeStamp()),
				ByteUtils.toBytes(TARGET_BITS),
				ByteUtils.toBytes(nonce)
		);			
	}
}