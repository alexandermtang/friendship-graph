package structures;

import java.util.*;

public class Graph {
	
	Vertex[] adjlists;
	Scanner sc;
	
	public Graph(Scanner sc) { 
		this.sc = sc; 
		build();
	}
	
	// nameHash used to translate from person's name to vertex number
	HashMap<String,Integer> nameHash;
	
	// schoolSize used to determine # of students at each school
	HashMap<String,Integer> schoolSize = new HashMap<String,Integer>();
	
	// Initialize graph with input from file, builds adjacencyLL
	void build() {
		sc.useDelimiter("\n");
		int size = sc.nextInt();
		adjlists = new Vertex[size];
		nameHash = new HashMap<String,Integer>(size/10,1f);
		// may need to change initial capacity of nameHash!!!!!!!!!!
		
		// read vertices
		for(int v = 0; v < adjlists.length; v++) {	
			String[] line = sc.next().split("\\|");
			String name = line[0];
			nameHash.put(name,v); // stores <name,v> in nameHash
			if(line[1].equals("y")) {
				String school = line[2];
				adjlists[v] = new Vertex(name, school, null);
				
				// stores <school,# of students> in schoolSize
				schoolSize.put(school, schoolSize.containsKey(school) ? schoolSize.get(school) + 1 : 1); 
			} else {
				adjlists[v] = new Vertex(name, "", null);
			}
			
		//	adjlists[v] = new Vertex(name, (line[1].equals("y") ? line[2] : ""), null);
		}

		// read edges
		while(sc.hasNext()) {
			String[] line = sc.next().split("\\|");
			int v1 = indexOfName(line[0]);
			int v2 = indexOfName(line[1]);
			adjlists[v1].neighbors = new Neighbor(v2, adjlists[v1].neighbors);
			adjlists[v2].neighbors = new Neighbor(v1, adjlists[v2].neighbors);
		}
	}	
	
	// returns vnum of name using nameHash, -1 if not present
	public int indexOfName(String name) { return nameHash.containsKey(name) ? nameHash.get(name) : -1; }
	
	// Uses BFS to print out an adjlists of subgraph of students in school
	public ArrayList<Graph> subgraph(String school) {
		// if(!schoolSize.containsKey(school)) return; // returns if school not found in schoolSize
		boolean[] visited = new boolean[adjlists.length];
		Arrays.fill(visited, false); // sets every entry in visited array as false
		ArrayList<Graph> subgraphs = new ArrayList<Graph>();
		
		ArrayList<Vertex> subadjlists = new ArrayList<Vertex>();
	//	Vertex[] subadjlists = new Vertex[schoolSize.get(school)];
		Queue<Integer> q = new LinkedList<Integer>();
		
		/* FINISH THIS SHIT NIGGAAAAAAAAAAAAAAAAAA */
		visited[0] = true;
		if(adjlists[0].school.equals(school)) {
	//		subadjlists[0] = new Vertex(adjlists[0].name, adjlists[0].school, null);
		}
		q.add(0);
		while(q.size() != 0) {
			int w = q.remove();
		}
		
		return subgraphs;
	}
	
	public String shortestPath(String name1, String name2) {
		
		Queue<Integer> q = new LinkedList<Integer>();
		boolean[] visited = new boolean[adjlists.length];
		Arrays.fill(visited, false);
		HashMap<Integer,Integer> previous = new HashMap<Integer,Integer>();
		
		int start = indexOfName(name1);
		int finish = indexOfName(name2);
		int curr = start;
		
		previous.put(curr,curr);
		q.add(curr);
		visited[curr] = true;
		
		while(!q.isEmpty()) {
			curr = (int)q.remove(); 
			if(curr == finish) {
				break;
			} else {
				for(Neighbor ptr = adjlists[curr].neighbors; ptr != null; ptr = ptr.next) {
					if(!visited[ptr.vnum]) {
						q.add(ptr.vnum);
						visited[ptr.vnum] = true;
						previous.put(ptr.vnum,curr);
					}
				}
			}
		}
		
		if(curr != finish) return "\t\t\tLooks like "+name1+" is out of luck! :(";
		
		ArrayList<String> reversepath = new ArrayList<String>();
		reversepath.add(adjlists[finish].name);
		for(int i=finish; i != start; i=previous.get(i)) { 
			reversepath.add(adjlists[previous.get(i)].name); 
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("\t\t\tTry this route! \n\t\t\t");
		for(int i=reversepath.size()-1; i >= 0; i--) {
			sb.append(reversepath.get(i));
			if(i != 0) sb.append(" --> ");
		}
		return sb.toString();
	}
	
	
	public int numberOfVertices() { return adjlists.length; }
	public Neighbor firstNeighbor(int vnum) { return adjlists[vnum].neighbors; }
	//public Neighbor nextNeighbor(int vnum) { return adjlists[vnum].neighbors.next; }
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(adjlists.length+"\n");
		
		// appends name|y|school or name|n
		for(int v = 0; v < adjlists.length; v++) {
			sb.append(adjlists[v].name + 
					(adjlists[v].school.equals("") ? "|n" : "|y|" + adjlists[v].school) + "\n");
		}
		
		// appended ensures undirected edges are not printed twice
		boolean[][] appended = new boolean[adjlists.length][adjlists.length];
		for(int i = 0; i < adjlists.length; i++) {
			for(int j = 0; j < adjlists.length; j++) {
				appended[i][j] = false;
			}
		}
		
		// appends edges: name1|name2
		for(int v = 0; v < adjlists.length; v++) {
			for(Neighbor ptr = adjlists[v].neighbors; ptr != null; ptr = ptr.next) {
				if(!appended[v][ptr.vnum]) {
					sb.append(adjlists[v].name+"|"+adjlists[ptr.vnum].name + "\n");
					appended[v][ptr.vnum] = true; appended[ptr.vnum][v] = true;
				}
			}
		}
		return sb.toString();
	}
}
