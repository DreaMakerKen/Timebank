package TCHAIN;

import java.io.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.HashMap;

public class Rate {
	
	private String Transtype;
	
	public HashMap<String,Integer> map = new HashMap<String,Integer>();
	
	
	public  void InitRateTable() throws IOException {
		//HashMap<String,Integer> map = new HashMap<String,Integer>();
		FileReader fr = new FileReader("D:\\123\\Tbank2\\RateTable.txt");
		BufferedReader br = new BufferedReader(fr);
		String value="",newvalue;
		while(br.ready()) {
			newvalue=br.readLine();
			value=value+" "+newvalue;
		}
		fr.close();
		StringTokenizer st = new StringTokenizer(value);
		while(st.hasMoreTokens()) {
			map.put(st.nextToken(),Integer.valueOf(st.nextToken()));
		}
	}
	
	
	public  int RateAmount(int amount,String transtype)throws IOException {
		
		int rate = map.get(transtype);
		amount=amount*rate;
		return amount;
	}
	
	public void ChangeRate(String transtype,int rateamount) throws IOException {
		String value="";
		map.put(transtype,rateamount);
		
		for (Object key : map.keySet()) {
			   value = value + key +" "+map.get(key)+"\r\n";
			  }
		value=value.substring(0,value.length()-2);
		File writename = new File("D:\\123\\Tbank2\\RateTable.txt"); 
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));  
        out.write(value); 
        out.flush(); 
        out.close();
		
		System.out.println("change rate success");
	
	}

	
}