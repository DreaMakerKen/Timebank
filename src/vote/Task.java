package vote;

import java.util.*;

import org.rocksdb.RocksDBException;

public class Task extends TimerTask{
	public static String issue ="";
	//����k�n�мg
	//�Q�n�w�ɰ��檺�u�@�g�b��method��
	public void run(){
		vBlockchainTest.sum_up(issue);
	}

	public static void sumUpTask(String issue){
		//�إ߭p�ɾ�
		Timer timer = new Timer();
		Task.issue= issue;
		//�]�w�p�ɾ�
		//�Ĥ@�ӰѼƬ�"�����檺�u�@",�|�I�s������run() method
		//�ĤG�ӰѼƬ��{���Ұʫ�,"����"���w���@��ƫ�"�Ĥ@��"����Ӥu�@
		//�ĤT�ӰѼƬ��C���j�h�ֲ@�����Ӥu�@
		
		timer.schedule(new Task(), 5000);//15�Ѱ���@��
	}
	
	/*public static void main(String[] args){
		sumUpTask("cut hair");
	}
	*/
}
