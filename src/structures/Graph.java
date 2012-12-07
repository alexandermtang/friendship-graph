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
	
	public boolean containsEdge(int vnum, int nbr) { return adjlists.get(vnum).neighbors.contains(nbr); }
 
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
		
		if(curr != finish) {
			return "\t\t\tLooks like "+name1+" is out of luck! :(\n\t\t\t"
					+ name1 + " and " + name2 + " don't have enough mutual friends.";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(nameOfIndex(finish));
		for(int i=finish; i != start; i=previous.get(i)) { 
			sb.insert(0, nameOfIndex(previous.get(i)) + " --> ");
		}
		sb.insert(0,"\t\t\tTry this route! \n\t\t\t");
		return sb.toString();
	}
	
	public Graph subgraph(String school) { 
		if(!studentsInSchool.containsKey(school)) return null;
		LinkedList<Integer> schoolList = studentsInSchool.get(school);
		Graph subgraph = new Graph(schoolList.size());
		
		for(int v : schoolList) {	
			subgraph.addVertex(new Vertex(nameOfIndex(v),school));
		}
		
		Queue<Integer> q = new LinkedList<Integer>();
		boolean[] visited = new boolean[adjlists.size()];
		Arrays.fill(visited, false);
		
		q.add(schoolList.get(0));
		visited[schoolList.get(0)] = true;
		
		while(!q.isEmpty()) {
			int curr = (int)q.remove();
			
			for(int nbr : adjlists.get(curr).neighbors) {
				if(!visited[nbr] && adjlists.get(nbr).school.equals(school)) {
					q.add(nbr);
					visited[nbr] = true;
					String currname = nameOfIndex(curr);
					String nbrname = nameOfIndex(nbr);
					subgraph.addEdge(subgraph.indexOfName(currname),subgraph.indexOfName(nbrname));
				} 
			}
			
			visited[curr] = true;
			
			if(q.isEmpty()) {
				for(int name : schoolList) { if(!visited[name]) { q.add(name); } }
			}
		}
		return subgraph;
	}
	
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
	
	ArrayList<String> connectors = new ArrayList<String>();
	int dfsnum = 0;
	int startAt = 0;
	
	private void dfs(Vertex v) {
		dfsnum++;
		v.dfsnum = dfsnum;
		v.back = dfsnum;
		v.visited = true;

		for(int wnum : v.neighbors) {
			Vertex w = adjlists.get(wnum);
			if(!w.visited) {
				dfs(w);
				if(indexOfName(v.name) != startAt && v.dfsnum <= w.back) {
					if(!connectors.contains(v.name)) connectors.add(v.name);
				}
				if(v.dfsnum > w.back) v.back = Math.min(v.back, w.back);
			} else v.back = Math.min(v.back, w.dfsnum);
		}		
	}
	
	public String connectors() {
		for(int i = 0; i < adjlists.size(); i++) {
			Vertex v = adjlists.get(i);
			if(!v.visited && v.neighbors.size() == 1) {
				// start new dfs, so set dfsnum=0 and startAt=current index
				dfsnum = 0;
				startAt = i;
				dfs(v);
			}
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(connectors.get(0));
		for(int i = 1; i < connectors.size(); i++) sb.insert(0, connectors.get(i)+", ");
		sb.insert(0, "\t\tConnectors: ");
		return sb.toString();
	}	
}
