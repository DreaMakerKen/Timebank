package vote;
import java.io.File;
import java.util.Scanner;

public class vBlockchainTest {
	
	public static void addBlock(String data, String issue, String price, Boolean agree, String voter) throws Exception {
        vBlockchain blockchain = vBlockchain.newBlockchain();
        blockchain.addBlock(data, issue, price, agree, voter);
    }
	
	public static void printChain() {
        vBlockchain blockchain = vBlockchain.newBlockchain();
        for (vBlockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hasNext(); ) {
            vBlock block = iterator.next();

            if (block != null) {
                boolean validate = vProofOfWork.newProofOfWork(block).validate();
                System.out.println(block.toString() + ", validate = " + validate);
            }
        }
    }
	
	public static void firstTimeResult() {
		try{
		addBlock("result","wash","10",true,"0");
		addBlock("result","cut hair","11",true,"0");
		addBlock("result","play","12",true,"0");
		addBlock("result","teach","13",true,"0");
		addBlock("result","cook","14",true,"0");
		addBlock("result","uber","15",true,"0");
		}catch (Exception e) {
			System.out.print("ERROR: fail"+e);	
		}
	}
	
	public static void sum_up(String issue) {
		
		vBlockchain blockchain = vBlockchain.newBlockchain();
		/*Scanner in = new Scanner(System.in);
		System.out.println("Enter sum up which issue:");
		String sumUpIssue = in.nextLine();
		in.close();
		*/
		int agreevote=0;
		int disagreevote=0;
		String sumUpIssue = issue;
		String sumUpPrice="0";
		String changesumUpPrice="0";
		boolean isThisTimeVote = true;
		boolean isThatTimeVote = true;
		
		for (vBlockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hasNext(); ) {
			vBlock block = iterator.next();
			
			if(isThatTimeVote && block!=null && sumUpIssue.equals(block.getIssue()) && block.getData().equals("result")) {
				sumUpPrice = block.getPrice(); 
			}
			if(block.getData().equals("result")&&sumUpIssue.equals(block.getIssue())) {
				isThatTimeVote = false;
			}
				
		}
		
		for (vBlockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hasNext(); ) {
			vBlock block = iterator.next();
			if(block.getData().equals("result")&&sumUpIssue.equals(block.getIssue())) {
				isThisTimeVote = false;
			}
			if (isThisTimeVote&&block != null && sumUpIssue.equals(block.getIssue())) {
				//System.out.println("{\nissue:"+block.getIssue()+"\nprice:"+block.getPrice()+"\nagree:"+block.getAgree()+"\n}");
				if(block.getData().equals("data")&&block.getAgree()==true) {
					agreevote++;
					if(changesumUpPrice =="0") {
					changesumUpPrice = block.getPrice();
					}
				}
				if(isThisTimeVote&&block.getData().equals("data")&&block.getAgree()==false) {
					disagreevote++;
				}	
			}		
		}
		if(agreevote>disagreevote) {
			System.out.println("change " + sumUpIssue +" price = " + changesumUpPrice);
			try{addBlock("result", sumUpIssue, changesumUpPrice, true, "0");}
			catch (Exception e) {
				System.out.print("ERROR: fail"+e);	
			}
		}
		if(agreevote<=disagreevote) {
			System.out.println("NO change " + sumUpIssue +" price = " + sumUpPrice);
			try{addBlock("result", sumUpIssue, sumUpPrice, true, "0");}
			catch (Exception e) {
				System.out.print("ERROR: fail"+e);	
			}
		}
	}
	
	public static void money_sum_up(String towhichpeer) {
		
		vBlockchain blockchain = vBlockchain.newBlockchain();
		/*Scanner in = new Scanner(System.in);
		System.out.println("Enter sum up which issue:");
		String sumUpIssue = in.nextLine();
		in.close();
		*/
		int agreevote=0;
		int disagreevote=0;
		String sumUpIssue = towhichpeer;
		String sumUpPrice="0";
		String changesumUpPrice="0";
		boolean isThisTimeVote = true;
		
		for (vBlockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hasNext(); ) {
			vBlock block = iterator.next();
			if(block.getData().equals("MoneyResult")&&sumUpIssue.equals(block.getIssue())) {
				isThisTimeVote = false;
			}
			if (isThisTimeVote&&block != null && sumUpIssue.equals(block.getIssue())) {
				//System.out.println("{\nissue:"+block.getIssue()+"\nprice:"+block.getPrice()+"\nagree:"+block.getAgree()+"\n}");
				if(block.getData().equals("MakemoneyProcess")&&block.getAgree()==true) {
					agreevote++;
					if(changesumUpPrice =="0") {
					changesumUpPrice = block.getPrice();
					}
				}
				if(isThisTimeVote&&block.getData().equals("MakemoneyProcess")&&block.getAgree()==false) {
					disagreevote++;
				}	
			}		
		}
		if(agreevote>disagreevote) {
			System.out.println("give " + sumUpIssue +" money = " + changesumUpPrice);
			try{addBlock("MoneyResult", sumUpIssue, changesumUpPrice, true, "0");}
			catch (Exception e) {
				System.out.print("ERROR: fail"+e);	
			}
		}
		if(agreevote<=disagreevote) {
			System.out.println("NO give " + sumUpIssue +" money");
			try{addBlock("MoneyResult", sumUpIssue, "0", false, "0");}
			catch (Exception e) {
				System.out.print("ERROR: fail"+e);	
			}
		}
	}
	
	public static void sumUpTime(String issue) {
		Task.sumUpTask(issue);
	}
	
	public static boolean isVoted(String data, String issue, String price, String voter) {
		vBlockchain blockchain = vBlockchain.newBlockchain();
		boolean hasVoted = false;
		boolean isNotInRange = true;
		for (vBlockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hasNext(); ) {
			vBlock block = iterator.next();
			if(block.getIssue().equals(issue)&& (block.getData().equals("result")||block.getData().equals("MoneyResult"))) {
				isNotInRange = false; 
			}
			
			if(block.getData().equals(data)&& block.getIssue().equals(issue)&&block.getPrice().equals(price)&&block.getVoter().equals(voter)&&isNotInRange) {
				hasVoted = true;
			}
		}
		
		return hasVoted;
	}
public static String getPrice(String issue) {
		
		vBlockchain blockchain = vBlockchain.newBlockchain();
		
		String findIssue = issue;
		String resultPrice="0";
		boolean isThisResult = true;
		
		for (vBlockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hasNext(); ) {
			vBlock block = iterator.next();
			
			if(block!=null && findIssue.equals(block.getIssue()) && block.getData().equals("result")&&isThisResult) {
				resultPrice = block.getPrice();
				isThisResult = false;
			}
				
		}
		
		System.out.println(resultPrice);
		
		return resultPrice;
	}
public static int getVoterSum(String issue){
	int nowvotersum=0;
	boolean isThisResult = true;
	vBlockchain blockchain = vBlockchain.newBlockchain();
	for (vBlockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hasNext(); ) {
		vBlock block = iterator.next();
		if(block!=null&&block.getData().equals("result")&&isThisResult) {
			isThisResult = false;
		}
		if(block!=null&&block.getIssue().equals(issue)&&isThisResult) {
			nowvotersum++;
		}	
	}
	return nowvotersum;
}

	public static void main(String[] args){
		
		//sum_up("cut hair");
		/*vRocksDBUtils.getInstance().getLastBlockHash();
		vRocksDBUtils.getInstance().pauseDB();
		 
		File file = new File("D:\\123\\Tbank2\\vote.db\\LOCK");
        if(file.delete())
        {
        	System.out.println("delete");
         
        }
        else
        {
        	System.out.println("false");
        }*/
		//Scanner sc=new Scanner(System.in);
		//firstTimeResult();
		printChain();
		//vRocksDBUtils.getInstance().getLastBlockHash();
		
		//vRocksDBUtils.getInstance().getLastBlockHash();
		//vRocksDBUtils.getInstance().putLastBlockHash("123");
		
		
		/*for(Block block: blockchain.getBlockList()) {
			System.out.println("Prev.hash: " + block.getPrevBlockHash());
			System.out.println("Data: " + block.getData());
			System.out.println("Hash: " + block.getHash());
			System.out.println();
			
			ProofOfWork pow = ProofOfWork.newProofOfWork(block);
			System.out.println("Pow valid: " + pow.validate() + "\n");
		}*/
		
	}
}
