package vote;
import com.google.common.collect.Maps;


import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.WriteOptions;

import java.util.Map;

public class vRocksDBUtils {
	
	private static final String DB_FILE = "C:\\Users\\Ken\\eclipse-workspace\\Tbank4\\vote.db"; //�϶���ƾڤ��
	private static final String BLOCKS_BUCKET_KEY = "blocks"; //�϶���e��
	private static final String LAST_BLOCK_KEY = "l"; //�̷s�@�Ӱ϶�
	private volatile static vRocksDBUtils instance;
	
	public static vRocksDBUtils getInstance() {
        if (instance == null) {
            synchronized (vRocksDBUtils.class) {
                if (instance == null) {
                	System.out.println("\n-------------------------nNEWDb\n----------------------------");
                    instance = new vRocksDBUtils();
                    
                }
            }
        }
        return instance;
    }
	//setUseFsync(true).setCreateIfMissing(true).setAllowMmapWrites(true).setAllowMmapReads(true)
	private RocksDB db;
	 
	 private final Options options = new Options().setUseFsync(true).setCreateIfMissing(true);
	    private final WriteOptions write_options=new WriteOptions().setDisableWAL(false).setSync(false);
	private Map<String, byte[]> blocksBucket;
	
	private vRocksDBUtils() {
        openDB();
        initBlockBucket();
    }
	
	private void openDB() { //���}DB
        try {
            db = RocksDB.open(options,DB_FILE);
        } catch (RocksDBException e) {
        	
            throw new RuntimeException("Fail to open db ! ", e);
        	
        }
    }
	
	private void initBlockBucket() { //��l��blocks �ƾڱ�
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
        	
            throw new RuntimeException("Fail to init block bucket ! ", e);
        }
    }
	
	public void putLastBlockHash(String tipBlockHash) { //�O�s�̷s���@�Ӱ϶�Hash
        try {
            blocksBucket.put(LAST_BLOCK_KEY, SerializeUtils.serialize(tipBlockHash));
            db.put(write_options,SerializeUtils.serialize(BLOCKS_BUCKET_KEY), SerializeUtils.serialize(blocksBucket));
        } catch (RocksDBException e) {
            throw new RuntimeException("Fail to put last block hash ! ", e);
        }
    }
	
	public String getLastBlockHash() { //�d�̷߳s�@�Ӱ϶���Hash��
        byte[] lastBlockHashBytes = blocksBucket.get(LAST_BLOCK_KEY);
        if (lastBlockHashBytes != null) {
            return (String) SerializeUtils.deserialize(lastBlockHashBytes);
        }
        return "";
    }
	
	public void putBlock(vBlock block) { //�O�s�϶�
        try {
            blocksBucket.put(block.getHash(), SerializeUtils.serialize(block));
            db.put(write_options,SerializeUtils.serialize(BLOCKS_BUCKET_KEY), SerializeUtils.serialize(blocksBucket));
        } catch (RocksDBException e) {
            throw new RuntimeException("Fail to put block ! ", e);
        }
    }
	
	public vBlock getBlock(String blockHash) { //�d�߰϶�
        return (vBlock) SerializeUtils.deserialize(blocksBucket.get(blockHash));
    }
	
	public void closeqq() { //����DB
        try {
        	 
             db.close();
            
        } catch (Exception e) {
            throw new RuntimeException("Fail to close db ! ", e);
        }
    }


	
	
}
