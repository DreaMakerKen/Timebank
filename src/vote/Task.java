package vote;

import java.util.*;

import org.rocksdb.RocksDBException;

public class Task extends TimerTask{
	public static String issue ="";
	//此方法要覆寫
	//想要定時執行的工作寫在該method中
	public void run(){
		vBlockchainTest.sum_up(issue);
	}

	public static void sumUpTask(String issue){
		//建立計時器
		Timer timer = new Timer();
		Task.issue= issue;
		//設定計時器
		//第一個參數為"欲執行的工作",會呼叫對應的run() method
		//第二個參數為程式啟動後,"延遲"指定的毫秒數後"第一次"執行該工作
		//第三個參數為每間隔多少毫秒執行該工作
		
		timer.schedule(new Task(), 5000);//15天執行一次
	}
	
	/*public static void main(String[] args){
		sumUpTask("cut hair");
	}
	*/
}
