package TCHAIN;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;

import com.google.common.collect.Maps;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Blockchain {

   // private List<Block> blockList;
    private String lastBlockHash;

  

    public static Blockchain initBlockchainFromDB() {
        String lastBlockHash = RocksDBUtils.getInstance().getLastBlockHash();
        if (lastBlockHash == null) {
            throw new RuntimeException("ERROR: Fail to init blockchain from db. ");
        }
        return new Blockchain(lastBlockHash);
    }
    
    
    public static Blockchain createBlockchain(String address) {
        String lastBlockHash = RocksDBUtils.getInstance().getLastBlockHash();
       
        if (StringUtils.isBlank(lastBlockHash)) {
           
            String genesisCoinbaseData = "The Times 03/Jan/2009 Chancellor on brink of second bailout for banks";
            Transaction coinbaseTX = Transaction.newCoinbaseTX(address, genesisCoinbaseData);
            Block genesisBlock = Block.newGenesisBlock(coinbaseTX);
            lastBlockHash = genesisBlock.getHash();
            int lastindex = genesisBlock.getIndex();
          //  int lastindex=Integer.parseInt(RocksDBUtils.getInstance().getLastIndex());
            RocksDBUtils.getInstance().putBlock(genesisBlock);
            RocksDBUtils.getInstance().putindex(lastindex);
            RocksDBUtils.getInstance().putLastBlockHash(lastBlockHash);
        }
        
        return new Blockchain(lastBlockHash);
    }
    public Block mineBlock(Transaction[] transactions) {
        
        for (Transaction tx : transactions) {
            if (!this.verifyTransactions(tx)) {
                System.out.print("ERROR: Fail to mine block ! Invalid transaction ! tx=" + tx.toString());
                throw new RuntimeException("ERROR: Fail to mine block ! Invalid transaction ! ");
            }
        }
        String lastBlockHash = RocksDBUtils.getInstance().getLastBlockHash();
        int lastindex=RocksDBUtils.getInstance().getLastIndex();    
        		
        if (lastBlockHash == null) {
            throw new RuntimeException("ERROR: Fail to get last block hash ! ");
        }

        Block block = Block.newBlock(lastBlockHash,lastindex,transactions);
        this.addBlock(block);
        
        return block;
    }
    public void addBlock(Block block) {
        RocksDBUtils.getInstance().putLastBlockHash(block.getHash());
        RocksDBUtils.getInstance().putindex(block.getIndex());
        RocksDBUtils.getInstance().putBlock(block);
        this.lastBlockHash = block.getHash();
       
    }

    public class BlockchainIterator {

        private String currentBlockHash;

        private BlockchainIterator(String currentBlockHash) {
            this.currentBlockHash = currentBlockHash;
        }

  
        public boolean hashNext() {
            if (StringUtils.isBlank(currentBlockHash)) {
                return false;
            }
            Block lastBlock = RocksDBUtils.getInstance().getBlock(currentBlockHash);
            if (lastBlock == null) {
                return false;
            }
            
            if (lastBlock.getPrevBlockHash().length() == 0) {
                return true;
            }
            return RocksDBUtils.getInstance().getBlock(lastBlock.getPrevBlockHash()) != null;
        }



        public Block next() {
            Block currentBlock = RocksDBUtils.getInstance().getBlock(currentBlockHash);
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
    public Map<String, TXOutput[]> findAllUTXOs() {
        Map<String, int[]> allSpentTXOs = this.getAllSpentTXOs();
        Map<String, TXOutput[]> allUTXOs = Maps.newHashMap();
        for (BlockchainIterator blockchainIterator = this.getBlockchainIterator(); blockchainIterator.hashNext(); ) {
            Block block = blockchainIterator.next();
            for (Transaction transaction : block.getTransactions()) {

                String txId = Hex.encodeHexString(transaction.getTxId());

                int[] spentOutIndexArray = allSpentTXOs.get(txId);
                TXOutput[] txOutputs = transaction.getOutputs();
                for (int outIndex = 0; outIndex < txOutputs.length; outIndex++) {
                    if (spentOutIndexArray != null && ArrayUtils.contains(spentOutIndexArray, outIndex)) {
                        continue;
                    }
                    TXOutput[] UTXOArray = allUTXOs.get(txId);
                    if (UTXOArray == null) {
                        UTXOArray = new TXOutput[]{txOutputs[outIndex]};
                    } else {
                        UTXOArray = ArrayUtils.add(UTXOArray, txOutputs[outIndex]);
                    }
                    allUTXOs.put(txId, UTXOArray);
                }
            }
        }
        return allUTXOs;
    }
      

      
        private Map<String, int[]> getAllSpentTXOs() {
           
            Map<String, int[]> spentTXOs = Maps.newHashMap();
            for (BlockchainIterator blockchainIterator = this.getBlockchainIterator(); blockchainIterator.hashNext(); ) {
                Block block = blockchainIterator.next();

                for (Transaction transaction : block.getTransactions()) {
                   
                    if (transaction.isCoinbase()) {
                        continue;
                    }
                    for (TXInput txInput : transaction.getInputs()) {
                        String inTxId = Hex.encodeHexString(txInput.getTxId());
                        int[] spentOutIndexArray = spentTXOs.get(inTxId);
                        if (spentOutIndexArray == null) {
                            spentOutIndexArray = new int[]{txInput.getTxOutputIndex()};
                        } else {
                            spentOutIndexArray = ArrayUtils.add(spentOutIndexArray, txInput.getTxOutputIndex());
                        }
                        spentTXOs.put(inTxId, spentOutIndexArray);
                    }
                }
            }
            return spentTXOs;
        }
    

        private Transaction findTransaction(byte[] txId) {
            for (BlockchainIterator iterator = this.getBlockchainIterator(); iterator.hashNext(); ) {
                Block block = iterator.next();
                for (Transaction tx : block.getTransactions()) {
                    if (Arrays.equals(tx.getTxId(), txId)) {
                        return tx;
                    }
                }
            }
            throw new RuntimeException("ERROR: Can not found tx by txId ! ");
        }
    public void signTransaction(Transaction tx, BCECPrivateKey privateKey) throws Exception {
        
        Map<String, Transaction> prevTxMap = Maps.newHashMap();
        for (TXInput txInput : tx.getInputs()) {
            Transaction prevTx = this.findTransaction(txInput.getTxId());
            prevTxMap.put(Hex.encodeHexString(txInput.getTxId()), prevTx);
        }
        tx.sign(privateKey, prevTxMap);
    }


    private boolean verifyTransactions(Transaction tx) {
        if (tx.isCoinbase()) {
            return true;
        }
        Map<String, Transaction> prevTx = Maps.newHashMap();
        for (TXInput txInput : tx.getInputs()) {
            Transaction transaction = this.findTransaction(txInput.getTxId());
            prevTx.put(Hex.encodeHexString(txInput.getTxId()), transaction);
        }
        try {
            return tx.verify(prevTx);
        } catch (Exception e) {
            System.out.print("Fail to verify transaction ! transaction invalid ! "+ e);
            throw new RuntimeException("Fail to verify transaction ! transaction invalid ! ", e);
        }
    }
 
}