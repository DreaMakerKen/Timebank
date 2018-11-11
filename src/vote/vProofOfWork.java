package vote;

import lombok.Data;
import java.math.BigInteger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

@Data

public class vProofOfWork {
	public static final int TARGET_BITS = 10;//���ץؼЦ�
	private vBlock block; //�϶�
	private BigInteger target; //���ץؼЭ�
	
	private vProofOfWork(vBlock block,BigInteger target) { //�Ыطs���u�@�q�ҩ� �]�w���ץؼЭ� 
		this.block = block; 							 //��1�i�沾��B��A�N1�V������(256 - TARGET_BITS)��
		this.target = target;							 //�o��ڭ̪����ץؼЭ�
	}
	
	public static vProofOfWork newProofOfWork(vBlock block) { 					//�]�w�@�����ץؼЦ�TARGET_BITS
		BigInteger targetValue = BigInteger.valueOf(1).shiftLeft((256 - TARGET_BITS)); //��̲ܳ׫��q���X��Hash�� ��Ƭ��G�i���
		return new vProofOfWork(block, targetValue); 							//�P256�ۤ���פ֤F�h��bit�A�]�Y�G�i��e�����h��bit�O�s.
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