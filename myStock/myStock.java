package myStock;

import java.io.*;
import java.math.*;
import java.util.*;
import java.util.Map.Entry;

import yahoofinance.*;

public class myStock {

    // TODO: choose the right data structures for the database 
	// HINT: HashMap provides fast retrieval
	// HINT: TreeSet allows key-value pairs to be sorted by value

	// This is the nested class provided for you to store the information associated with a stock symbol
	
	private static class stockInfo {
		private String name;
		private BigDecimal price;
		public stockInfo(String nameIn, BigDecimal priceIn) {
			name = nameIn;
			price = priceIn;
		}
		public String toString() {
			StringBuilder stockInfoString = new StringBuilder("");
			stockInfoString.append(name + " " + price.toString());
			return stockInfoString.toString();
		}
	}
	
	class MyComparator implements Comparator<Map.Entry<String, stockInfo>>{
		public int compare(Map.Entry<String, stockInfo> stock1, Map.Entry<String, stockInfo> stock2) {
			BigDecimal s1, s2;
			
			s1= stock1.getValue().price; 
			s2= stock2.getValue().price; 
			return s2.compareTo(s1);
		}
	 
		
	}
	
	HashMap<String, stockInfo> hMap;
	TreeSet<Map.Entry<String, stockInfo>> ts;
	public myStock () {
		// TODO: define the constructor to create the database
		// HINT: the according data structure's Comparator should be overridden sort record by price
		hMap = new HashMap<String, stockInfo>();
		ts = new TreeSet< Map.Entry<String, stockInfo> > (new MyComparator());
	}
    
	public void insertOrUpdate(String symbol, stockInfo stock) {
		// TODO: implement this method used to initialize and update the database
		// HINT: make sure the time complexity is O(log(n))   		
		hMap.put(symbol, stock);
		// map.entry object
		AbstractMap.SimpleEntry<String, stockInfo> e = new AbstractMap.SimpleEntry<String, stockInfo>(symbol, stock);
		ts.add(e);

	}
	
	public stockInfo get(String symbol) {
		// TODO: implement this method to quickly retrieve record from database
		// HINT: time complexity should be O(1) constant time  
		
		return hMap.get(symbol); 
	}
	
	public List<Map.Entry<String, stockInfo>> top(int k) {
		// TODO: return the stock records with top k prices.
		// HINT: this retrieval should be done in O(k) 
		
		ArrayList<Map.Entry<String, stockInfo>> topStock = new ArrayList<>(); 
        // Creating an iterator 
        Iterator<Map.Entry<String, stockInfo>> value = ts.iterator(); 

		int x = 0; 
		while(value.hasNext()&& x<k) {
			//System.out.println(value.next());
			topStock.add(value.next());
			x++; 
			
		}
		return topStock; 
		
	}
	

    public static void main(String[] args) throws IOException {   	
    	
    	// test the database creation based on the input file
    	myStock techStock = new myStock();
    	BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("./US-Tech-Symbols.txt"));
			String line = reader.readLine();
			while (line != null) {
				String[] var = line.split(":");
				
				// YahooFinance API is used and make sure the lib file is included in the project build path
				Stock stock = YahooFinance.get(var[0]);
				
				// test the insertOrUpdate operation
				techStock.insertOrUpdate(var[0], new stockInfo(var[1], stock.getQuote().getPrice())); 
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 1;
		System.out.println("===========Top 10 stocks===========");
		
		// test the top operation
		for (Map.Entry<String, stockInfo> element : techStock.top(10)) {
		    System.out.println("[" + i + "]" +element.getKey() + " " + element.getValue());
		    i++;
		}
		
		// test the get operation
		System.out.println("===========Stock info retrieval===========");
    	System.out.println("VMW" + " " + techStock.get("VMW"));
    	System.out.println("CHL" + " " + techStock.get("CHL"));
    }
}