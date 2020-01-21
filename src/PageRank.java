package hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PageRank {
	 public static final double _LAMBDA = 0.2;
	 public static final double _TAU = 0.02;
	

	public Map<String, Set<String>> outlinks;
	public Map<String, Set<String>> inlinks;
	public Set<String> pages; //Set Of All Pages
	Map<String, Double> prev = new LinkedHashMap<String, Double>(); 
	Map<String, Double> cur = new LinkedHashMap<String, Double>(); 

	public PageRank(File fileName) {
		outlinks = new LinkedHashMap<>();
		inlinks = new LinkedHashMap<>();
		pages = new LinkedHashSet<>();
		readData(fileName);
	}

	private void readData(File fileName) {
		// TODO Auto-generated method stub
		if (!fileName.exists()) {
			System.err.println("NO FILE EXISTS!");
		} else {
			Scanner scan;
			try {
				scan = new Scanner(fileName);
				while (scan.hasNextLine()) {
					String[] temp = scan.nextLine().split("\t");
					for(String s : temp) {
						if(!pages.contains(s))
							pages.add(s);
					}
					//Storing inlinks
					if (!inlinks.containsKey(temp[1])) {
						Set<String> tempSet = new LinkedHashSet();
						tempSet.add(temp[0]);
						inlinks.put(temp[1], tempSet);
					} else {
						inlinks.get(temp[1]).add(temp[0]);
					}
					//Storing outlinks
					if (!outlinks.containsKey(temp[0])) {
						Set<String> tempSet = new LinkedHashSet();
						tempSet.add(temp[1]);
						outlinks.put(temp[0], tempSet);
					} else {
						outlinks.get(temp[0]).add(temp[1]);
					}
				}
				scan.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Map<String, Set<String>> getSortedInlinks() {
		 List<Map.Entry<String, Set<String>>> list = new LinkedList<Map.Entry<String, Set<String>>>(inlinks.entrySet());
		 Collections.sort(list, new Comparator<Map.Entry<String, Set<String>>>() {
	            public int compare(Map.Entry<String, Set<String>> o1,
	                               Map.Entry<String, Set<String>> o2) {
	                return (Integer.valueOf(o2.getValue().size())).compareTo(Integer.valueOf(o1.getValue().size()));
	            }
	        });
		  Map<String, Set<String>> sortedMap = new LinkedHashMap<String, Set<String>>();
	        for (Map.Entry<String, Set<String>> entry : list) {
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }
	        return sortedMap;
	}

	
	public void printSortedInlinks(Map<String, Set<String>> sortedMap) {
		int count = 1;
		for (Map.Entry<String, Set<String>> entry : sortedMap.entrySet()) {
			if(count >= 51)
				return;
			System.out.println(count + " " + entry.getKey() + "\t" + entry.getValue().size());
			count++;
        }
	}
	
	
	public Map<String, Double> getSortedPG() {
		 List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(cur.entrySet());
		 Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
	            public int compare(Map.Entry<String, Double> o1,
	                               Map.Entry<String, Double> o2) {
	                return ((o2.getValue())).compareTo((o1.getValue()));
	            }
	        });
		  Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
	        for (Map.Entry<String, Double> entry : list) {
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }
	        return sortedMap;
	}
	
	public void printSortedPG(Map<String, Double> sortedMap) {
		int count = 1;
		for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
			if(count >= 51)
				return;
			System.out.println(count + " " + entry.getKey() + "\t" + entry.getValue());
			count++;
        }
	}
	
	public void computePageRank() {
		for(String p : pages) {
			prev.put(p, new Double(1/outlinks.size()));
		}
		int count = 0;
		while(true) {
			System.out.println(count);
			count++;
			for(String p : pages) {
				cur.put(p, new Double(_LAMBDA/outlinks.size()));
			}
			for(String p : pages) {
				Set<String> Q = new LinkedHashSet();
				Q = outlinks.get(p);
				if(Q != null) {
					Double tempCal = new Double(prev.get(p)*(1-_LAMBDA)/Q.size());
					for(String q : Q) {
						Double Rq = cur.get(q);
						cur.replace(q, new Double(Double.sum(Rq, tempCal)));
					}
				}else {
					Double tempCal = new Double(prev.get(p)*(1-_LAMBDA)/pages.size());
					for(String q : pages) {
						Double Rq = cur.get(q);
						cur.replace(q, new Double(Double.sum(Rq, tempCal)));
					}
				}
				if(converged(cur, prev))
				{
					return;
				}else {
					prev = new LinkedHashMap<String, Double>(); 
					for(Map.Entry<String, Double> entry : cur.entrySet()) {
						prev.put(entry.getKey(), entry.getValue());
					}
				}
			}
		}
	}

	private boolean converged(Map<String, Double> cur, Map<String, Double> prev) {
		double [] curArr = new double[cur.size()];
		double [] prevArr = new double[prev.size()];
		int ct = 0;
		for(Map.Entry<String, Double> entry : cur.entrySet()) {
			curArr[ct] = entry.getValue();
			ct++;
		}
		ct = 0;
		for(Map.Entry<String, Double> entry : prev.entrySet()) {
			prevArr[ct] = entry.getValue();
			ct++;
		}
		double diff = 0;
		for(int i = 0; i < curArr.length; i++) {
			diff += (curArr[i]-prevArr[i])*(curArr[i]-prevArr[i]);
		}
		return diff < _TAU;
	}
}
