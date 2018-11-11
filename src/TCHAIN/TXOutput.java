package TCHAIN;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TXOutput implements java.io.Serializable{

 
    private int value;
 
    private byte[] pubKeyHash;


   
    public static TXOutput newTXOutput(int value, String address) {
       
        byte[] versionedPayload = Base58Check.base58ToBytes(address);
        byte[] pubKeyHash = Arrays.copyOfRange(versionedPayload, 1, versionedPayload.length);
        return new TXOutput(value, pubKeyHash);
    }

 
    public boolean isLockedWithKey(byte[] pubKeyHash) {
        return Arrays.equals(this.getPubKeyHash(), pubKeyHash);
    }

}