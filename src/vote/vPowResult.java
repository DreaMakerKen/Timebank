package vote;

import lombok.Data;

@Data

public class vPowResult {
	private long nonce;//ÀH¾÷¼Æ
	private String hash;//hash­È
	public vPowResult(long nonce, String hash) {
		this.nonce = nonce;
		this.hash = hash;
	}

}
