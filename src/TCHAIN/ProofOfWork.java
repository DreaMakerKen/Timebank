package TCHAIN;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ProofOfWork {

 
    public static final int TARGET_BITS = 10;

   
    private Block block;
   
    private BigInteger target;


  
    public static ProofOfWork newProofOfWork(Block block) {
        BigInteger targetValue = BigInteger.valueOf(1).shiftLeft((256 - TARGET_BITS));
        return new ProofOfWork(block, targetValue);
    }

    public PowResult run() {
        long nonce = 0;
        String shaHex = "";
        long startTime = System.currentTimeMillis();
        while (nonce < Long.MAX_VALUE) {
            log.info("POW running, nonce=" + nonce);
            byte[] data = this.prepareData(nonce);
            shaHex = DigestUtils.sha256Hex(data);
            if (new BigInteger(shaHex, 16).compareTo(this.target) == -1) {
                log.info("Elapsed Time: {} seconds \n", new Object[]{(float) (System.currentTimeMillis() - startTime) / 1000});
                log.info("correct hash Hex: {} \n", new Object[]{shaHex});
                break;
            } else {
                nonce++;
            }
        }
        return new PowResult(nonce, shaHex);
    }

   
    public boolean validate() {
        byte[] data = this.prepareData(this.getBlock().getNonce());
        return new BigInteger(DigestUtils.sha256Hex(data), 16).compareTo(this.target) == -1;
    }

   
    private byte[] prepareData(long nonce) {
        byte[] prevBlockHashBytes = {};
        if (StringUtils.isNotBlank(this.getBlock().getPrevBlockHash())) {
            prevBlockHashBytes = new BigInteger(this.getBlock().getPrevBlockHash(), 16).toByteArray();
        }

        return ByteUtils.merge(
                prevBlockHashBytes,
                this.getBlock().hashTransaction(),
                ByteUtils.toBytes(this.getBlock().getTimeStamp()),
                ByteUtils.toBytes(TARGET_BITS),
                ByteUtils.toBytes(nonce)
        );
    }

}