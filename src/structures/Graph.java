package structures;

import java.util.*;

public class Graph {
	
	protected ArrayList<Vertex> adjlists; 
	
	// nameHash used to translate from person's name to vertex number
	HashMap<String,Integer> nameHash = new HashMap<String,Integer>();
	
	// key is name of school, value is a LinkedList of all students in that school
	HashMap<String,LinkedList<Integer>> studentsInSchool = new HashMap<String,LinkedList<Integer>>();

	public Graph() { adjlists = new ArrayList<Vertex>(); }
	
	public Graph(int vertexCap) { adjlists = new ArrayList<Vertex>(vertexCap); }
	
	public int numberOfVertices() { return adjlists.size(); }
	
	public int addVertex(Vertex v) {
		if(!containsVertex(v)) { 
			adjlists.add(v);
			nameHash.put(v.name, adjlists.size()-1);
			if(!v.school.equals("")) {
				if(!studentsInSchool.containsKey(v.school)) {
					LinkedList<Integer> students = new LinkedList<Integer>();
					students.add(adjlists.size()-1);
					studentsInSchool.put(v.school, students);
				} else {
					studentsInSchool.get(v.school).add(adjlists.size()-1);
				}
			}
		}
		return adjlists.size()-1;
	}
	
	public boolean containsVertex(Vertex v) { return adjlists.indexOf(v) != -1; }
	
	public int vertexNumberOf(Vertex v) { return adjlists.indexOf(v); }
	
	public boolean containsEdge(int vnum, int nbr) {
		return adjlists.get(vnum).neighbors.contains(nbr);
	}
	
	public void addEdge(int vnum, int nbr) {
		Vertex v = adjlists.get(vnum);
		if(!v.neighbors.contains(nbr)) {
			v.neighbors.add(nbr);
			adjlists.get(nbr).neighbors.add(vnum);
		}
	}
	
	public int indexOfName(String name) { return nameHash.containsKey(name) ? nameHash.get(name) : -1; }
	
	public String nameOfIndex(int vnum) { return adjlists.get(vnum).name; }

	public String shortestPath(String name1, String name2) {
		Queue<Integer> q = new LinkedList<Integer>();
		boolean[] visited = new boolean[adjlists.size()];
		Arrays.fill(visited, false);
		HashMap<Integer,Integer> previous = new HashMap<Integer,Integer>();
		
		int start = indexOfName(name1);
		int finish = indexOfName(name2);
		int curr = start;
		
		previous.put(curr,start);
		q.add(curr);
		visited[curr] = true;
		
		while(!q.isEmpty()) {
			curr = (int)q.remove(); 
			if(curr == finish) {
				break;
			} else {
				for(int nbr : adjlists.get(curr).neighbors) {
					if(!visited[nbr]) {
						q.add(nbr);
						visited[nbr] = true;
						previous.put(nbr,curr);
					}
				}
			}
		}
		
		if(curr != finish) return "\t\t\tLooks like "+name1+" is out of luck! :(";
		
		ArrayList<String> reversepath = new ArrayList<String>();
		reversepath.add(nameOfIndex(finish));
		for(int i=finish; i != start; i=previous.get(i)) { 
			reversepath.add(nameOfIndex(previous.get(i))); 
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("\t\t\tTry this route! \n\t\t\t");
		for(int i=reversepath.size()-1; i >= 0; i--) {
			sb.append(reversepath.get(i));
			if(i != 0) sb.append(" --> ");
		}
		return sb.toString();
	}
	
	public Graph subgraph(String school) { 
		LinkedList<Integer> schoolList = studentsInSchool.get(school);
		Graph subgraph = new Graph(schoolList.size());
		
		for(int v : schoolList) {	
			subgraph.addVertex(new Vertex(nameOfIndex(v),school));
		}
		
		Queue<Integer> q = new LinkedList<Integer>();
		boolean[] visited = new boolean[schoolList.size()];
		Arrays.fill(visited, false);
		for(int i=0; i < schoolList.size(); i++) {
			for(int nbr : adjlists.get(i).neighbors) {
				if(adjlists.get(nbr).school.equals(school)) {
					subgraph.addEdge(i, subgraph.indexOfName(nameOfIndex(nbr)));
				}
			}
		}
		
		
		return subgraph;
	}
	
	/*
	// Uses BFS to print out an adjlists of subgraph of students in school
	public Graph subgraph(String school) {
		Queue<Integer> q = new LinkedList<Integer>();
		Vertex[] schooladjlists = new Vertex[studentsInSchool.get(school).size()];
		boolean[] visited = new boolean[schooladjlists.length];
		Arrays.fill(visited, false);

		HashMap<Integer,Integer> previous = new HashMap<Integer,Integer>();
		
		LinkedList<Integer> tovisit = studentsInSchool.get(school);
		for(int i=0; i < tovisit.size(); i++) {
			schooladjlists[i] = new Vertex(adjlists[tovisit.get(i)].name, school, null);
			
			
			
			
			
		}
		int start = studentsInSchool.get(school).get(0);
		int curr = start;
		int finish;
		
		previous.put(curr,start);
		q.add(curr);
		visited[curr] = true;
		
		while(!q.isEmpty()) {
			curr = (int)q.remove(); 

			for(Neighbor ptr = adjlists[curr].neighbors; ptr != null; ptr = ptr.next) {
				if(!visited[ptr.vnum]) {
					q.add(ptr.vnum);
					visited[ptr.vnum] = true;
					previous.put(ptr.vnum,curr);
				}
			}
			finish = curr;
		}
		
		
		ArrayList<String> reversepath = new ArrayList<String>();
		reversepath.add(adjlists[finish].name);
		for(int i=finish; i != start; i=previous.get(i)) { 
			reversepath.add(adjlists[previous.get(i)].name); 
		}
	

		visited[0] = true;
		if(adjlists[0].school.equals(school)) {
	//		subadjlists[0] = new Vertex(adjlists[0].name, adjlists[0].school, null);
		}
		q.add(0);
		while(q.size() != 0) {
			int w = q.remove();
		}
		
		Graph g = new Graph();
		g.adjlists = schooladjlists;
		return g;
	}
	*/
	
	
	public void printSchools() {
		for(Map.Entry entry : studentsInSchool.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
	}
	
	public ArrayList<Graph> cliques() {
		ArrayList<Graph> cliques = new ArrayList<Graph>();

		// used to iterate through studentsInSchool
		for(String school : studentsInSchool.keySet()) {
			cliques.add(subgraph(school));
		}
		return cliques; 
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(adjlists.size()+"\n");
		
		// appends name|y|school or name|n
		for(int v = 0; v < adjlists.size(); v++) {
			sb.append(nameOfIndex(v) + 
					(adjlists.get(v).school.equals("") ? "|n" : "|y|" + adjlists.get(v).school) + "\n");
		}
		
		// appended ensures undirected edges are not printed twice
		boolean[][] appended = new boolean[adjlists.size()][adjlists.size()];
		for(int i = 0; i < adjlists.size(); i++) {
			for(int j = 0; j < adjlists.size(); j++) {
				appended[i][j] = false;
			}
		}
		
		// appends edges: name1|name2
		for(int v = 0; v < adjlists.size(); v++) {
			for(int nbr : adjlists.get(v).neighbors) {
				if(!appended[v][nbr]) {
					sb.append(nameOfIndex(v) +"|"+nameOfIndex(nbr) + "\n");
					appended[v][nbr] = true; appended[nbr][v] = true;
				}
			}
		}
		return sb.toString();
	}
}
