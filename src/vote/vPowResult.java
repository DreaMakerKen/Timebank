package vote;

import lombok.Data;

@Data

public class vPowResult {
	private long nonce;//�H����
	private String hash;//hash��
	public vPowResult(long nonce, String hash) {
		this.nonce = nonce;
		this.hash = hash;
	}

}
