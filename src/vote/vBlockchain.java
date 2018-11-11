package vote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
//import java.util.LinkedList;
//import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class vBlockchain {
	//private List<Block> blockList;
	private String lastBlockHash;
	
	public static vBlockchain newBlockchain() {
		String lastBlockHash = vRocksDBUtils.getInstance().getLastBlockHash();
		if (StringUtils.isBlank(lastBlockHash)) {
            vBlock genesisBlock = vBlock.newGenesisBlock();
            lastBlockHash = genesisBlock.getHash();
            vRocksDBUtils.getInstance().putBlock(genesisBlock);
            vRocksDBUtils.getInstance().putLastBlockHash(lastBlockHash);
        }
        return new vBlockchain(lastBlockHash);
		
		/*List<Block> blocks = new LinkedList<>();
		blocks.add(Block.newGenesisBlock());
		return new Blockchain(blocks);
		*/
	}
	
	public void addBlock(String data, String issue, String price, Boolean agree, String voter)throws Exception {
		String lastBlockHash = vRocksDBUtils.getInstance().getLastBlockHash();
		if (StringUtils.isBlank(lastBlockHash)) {
            throw new Exception("Fail to add block into blockchain ! ");
        }
        this.addBlock(vBlock.newBlock(lastBlockHash, data, issue, price, agree, voter));
        
		/*Block previousBlock = blockList.get(blockList.size()-1);
		this.addBlock(Block.newBlock(previousBlock.getHash(),data));
		*/
	}
	
	public void addBlock(vBlock block) {
		//this.blockList.add(block);
		
		vRocksDBUtils.getInstance().putLastBlockHash(block.getHash());
        vRocksDBUtils.getInstance().putBlock(block);
        this.lastBlockHash = block.getHash();
        
      // vRocksDBUtils.getInstance().closeqq();
        
		
	}
	
	public class BlockchainIterator {//∞œ∂Ù√Ï≠°•Næπ
        private String currentBlockHash;

        public BlockchainIterator(String currentBlockHash) {
            this.currentBlockHash = currentBlockHash;
        }
	
        public boolean hasNext() {
        	if(ByteUtils.ZERO_HASH.equals(currentBlockHash)) {
        		return false;
        	}
        	vBlock lastBlock = vRocksDBUtils.getInstance().getBlock(currentBlockHash);
        	if (lastBlock == null) {
        		return false;
        	}
        	if (ByteUtils.ZERO_HASH.equals(lastBlock.getPrevBlockHash())) {
        		return true;
        	}
        	return vRocksDBUtils.getInstance().getBlock(lastBlock.getPrevBlockHash()) != null;
        }

        public vBlock next() {
        	vBlock currentBlock = vRocksDBUtils.getInstance().getBlock(currentBlockHash);
        	if (currentBlock != null) {
        		this.currentBlockHash = currentBlock.getPrevBlockHash();
        		return currentBlock;
        	}
        	return null;
        }
	}
	
	public BlockchainIterator getBlockchainIterator() {
        return new BlockchainIterator(lastBlockHash);
    }
	
}
