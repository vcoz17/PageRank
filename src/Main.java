package hw2;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("/Users/DucNguyen/Downloads/links.srt");
		PageRank pg = new PageRank(file);
		pg.printSortedInlinks(pg.getSortedInlinks());
		pg.computePageRank();
		System.out.println("*******\n\n");
		pg.printSortedPG(pg.getSortedPG());
	}

}
