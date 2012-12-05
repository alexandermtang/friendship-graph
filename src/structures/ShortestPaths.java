package structures;

import java.util.*;

public class ShortestPaths {
	
	int numverts;
	int sourceNum;
	int destNum;
	LinkedList<Neighbor> fringe; // change to minheap!!!!!!!!
	int[] Distance;
	int[] Previous;
	boolean[] Done;
	Graph G;
	
	public ShortestPaths(Graph G, String name1, String name2) {
		this.G = G;
		numverts = G.numberOfVertices();
		
		this.sourceNum = G.indexOfName(name1);
		this.destNum = G.indexOfName(name2);
		
		fringe = new LinkedList<Neighbor>();
		Distance = new int[numverts];
		Previous = new int[numverts];
		Done = new boolean[numverts];
		
		// step 1 of Dijkstra's
		for(int i=0; i < numverts; i++) {
			Done[i] = false;
			Distance[i] = Integer.MAX_VALUE;
			Previous[i] = i;
		}
		fringe.clear();
		
		// steps 2,3 of Dijkstra's
		Done[sourceNum] = true;
		Distance[sourceNum] = 0;
		
		Neighbor nbr = G.firstNeighbor(sourceNum);
		while(nbr != null) {
			int w = nbr.vnum;
			Distance[w] = 1;
			Previous[w] = sourceNum;
			fringe.add(nbr);
			nbr = nbr.next;
		}
	}
	
	public void runSome(int howManySteps) {
		int step = 0;
		while(step < howManySteps) {
			if(fringe.isEmpty()) break;
			
			//int minVertex = 
		}
	}
	
	//int fringeDeleteMin() {	}
	
}
