package TCHAIN;
import com.google.common.collect.Maps;
import lombok.Getter;


import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.WriteOptions;
import org.rocksdb.Options;
import java.util.Map;



public class RocksDBUtils {

  
    private static final String DB_FILE = "D:\\123\\Tbank2\\blockchain.db";
    
    private static final String BLOCKS_BUCKET_KEY = "blocks";
    
    private static final String CHAINSTATE_BUCKET_KEY = "chainstate";

    
    private static final String LAST_BLOCK_KEY = "1";
    private static final String LAST_Index_KEY = "2";

    private volatile static RocksDBUtils instance;

    public static RocksDBUtils getInstance() {
        if (instance == null) {
            synchronized (RocksDBUtils.class) {
                if (instance == null) {
                    instance = new RocksDBUtils();
                }
            }
        }
        return instance;
    }
    
    private final Options options = new Options().setUseFsync(true).setCreateIfMissing(true);
    private final WriteOptions write_options=new WriteOptions().setDisableWAL(false).setSync(true);
    
    private RocksDB db;

    /**
     * block buckets
     */
    private Map<String, byte[]> blocksBucket;
    /**
     * chainstate buckets
     */
    @Getter
    private Map<String, byte[]> chainstateBucket;

    private RocksDBUtils() {
        openDB();
        initBlockBucket();
        initChainStateBucket();
    }

 
    private void openDB() {
        try {
            db = RocksDB.open(options,DB_FILE);
        } catch (RocksDBException e) {
        	System.out.print("Fail to open db ! "+ e);
            throw new RuntimeException("Fail to open db ! ", e);
        }
    }

    
    private void initBlockBucket() {
        try {
            byte[] blockBucketKey = SerializeUtils.serialize(BLOCKS_BUCKET_KEY);
            byte[] blockBucketBytes = db.get(blockBucketKey);
            if (blockBucketBytes != null) {
                blocksBucket = (Map) SerializeUtils.deserialize(blockBucketBytes);
            } else {
                blocksBucket = Maps.newHashMap();
                db.put(write_options,blockBucketKey, SerializeUtils.serialize(blocksBucket));
            }
        } catch (RocksDBException e) {
        	System.out.print("Fail to init block bucket ! "+e);
            throw new RuntimeException("Fail to init block bucket ! ", e);
        }
    }

    private void initChainStateBucket() {
        try {
            byte[] chainstateBucketKey = SerializeUtils.serialize(CHAINSTATE_BUCKET_KEY);
            byte[] chainstateBucketBytes = db.get(chainstateBucketKey);
            if (chainstateBucketBytes != null) {
                chainstateBucket = (Map) SerializeUtils.deserialize(chainstateBucketBytes);
            } else {
                chainstateBucket = Maps.newHashMap();
                db.put(write_options,chainstateBucketKey, SerializeUtils.serialize(chainstateBucket));
            }
        } catch (RocksDBException e) {
        	System.out.print("Fail to init chainstate bucket ! "+e);
            throw new RuntimeException("Fail to init chainstate bucket ! ", e);
        }
    }

  
    public void putLastBlockHash(String tipBlockHash) {
        try {
            blocksBucket.put(LAST_BLOCK_KEY, SerializeUtils.serialize(tipBlockHash));
            db.put(write_options,SerializeUtils.serialize(BLOCKS_BUCKET_KEY), SerializeUtils.serialize(blocksBucket));
        } catch (RocksDBException e) {
        	System.out.print("Fail to put last block hash ! tipBlockHash=" + tipBlockHash+e);
            throw new RuntimeException("Fail to put last block hash ! tipBlockHash=" + tipBlockHash, e);
        }
    }
    public void putindex(int index) {
        try {
            blocksBucket.put(LAST_Index_KEY, SerializeUtils.serialize(index));
            db.put(write_options,SerializeUtils.serialize(LAST_Index_KEY), SerializeUtils.serialize(blocksBucket));
        } catch (RocksDBException e) {
            System.out.print("Fail to put height ! " + index+e);
            throw new RuntimeException("Fail to put index ! tipBlockHash=" + index, e);
        }
    }
    public int getLastIndex() {
        byte[] indexbyte = blocksBucket.get(LAST_Index_KEY);
        if ( indexbyte!= null) {
            return (int) SerializeUtils.deserialize(indexbyte);
        }
        return 0;
    }
    public String getLastBlockHash() {
        byte[] lastBlockHashBytes = blocksBucket.get(LAST_BLOCK_KEY);
        if (lastBlockHashBytes != null) {
            return (String) SerializeUtils.deserialize(lastBlockHashBytes);
        }
        return "";
    }


    public void putBlock(Block block) {
        try {
            blocksBucket.put(block.getHash(), SerializeUtils.serialize(block));
            db.put(write_options,SerializeUtils.serialize(BLOCKS_BUCKET_KEY), SerializeUtils.serialize(blocksBucket));
        } catch (RocksDBException e) {
        	System.out.print("Fail to put block ! block=" + block.toString()+e);
            throw new RuntimeException("Fail to put block ! block=" + block.toString(), e);
        }
    }

    public Block getBlock(String blockHash) {
        byte[] blockBytes = blocksBucket.get(blockHash);
        if (blockBytes != null) {
            return (Block) SerializeUtils.deserialize(blockBytes);
        }
        throw new RuntimeException("Fail to get block ! blockHash=" + blockHash);
    }


    
    public void cleanChainStateBucket() {
        try {
            chainstateBucket.clear();
        } catch (Exception e) {
        	System.out.print("Fail to clear chainstate bucket ! "+e);
            throw new RuntimeException("Fail to clear chainstate bucket ! ", e);
        }
    }


    public void putUTXOs(String key, TXOutput[] utxos) {
        try {
            chainstateBucket.put(key, SerializeUtils.serialize(utxos));
            db.put(write_options,SerializeUtils.serialize(CHAINSTATE_BUCKET_KEY), SerializeUtils.serialize(chainstateBucket));
        } catch (Exception e) {
        	System.out.print("Fail to put UTXOs into chainstate bucket ! key=" + key+e);
            throw new RuntimeException("Fail to put UTXOs into chainstate bucket ! key=" + key, e);
        }
    }


    public TXOutput[] getUTXOs(String key) {
        byte[] utxosByte = chainstateBucket.get(key);
        if (utxosByte != null) {
            return (TXOutput[]) SerializeUtils.deserialize(utxosByte);
        }
        return null;
    }


    public void deleteUTXOs(String key) {
        try {
            chainstateBucket.remove(key);
            db.put(write_options,SerializeUtils.serialize(CHAINSTATE_BUCKET_KEY), SerializeUtils.serialize(chainstateBucket));
        } catch (Exception e) {
        	System.out.print("Fail to delete UTXOs by key ! key=" + key+ e);
            throw new RuntimeException("Fail to delete UTXOs by key ! key=" + key, e);
        }
    }

    public void closeDB() {
        try {
        	
            db.close();
           
            System.out.print("DBÃö³¬");
        } catch (Exception e) {
        	System.out.print("Fail to close db ! "+ e);
            throw new RuntimeException("Fail to close db ! ", e);
        }
    }
}