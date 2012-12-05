// Alexander Tang 
// Brian Li

package apps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import structures.Graph;
import structures.Vertex;

public class Friends {

	static Scanner stdin = new Scanner(System.in);
	
	static char getOption() {
		System.out.print("\tChoose action: ");
		System.out.print("(s)tudents at a school, ");
		System.out.print("(i)ntro chain, ");
		System.out.print("(c)liques, ");
		System.out.print("c(o)nnectors, or ");
		System.out.print("(q)uit? => ");
		char response = stdin.next().toLowerCase().charAt(0);
		while (response != 's' && response != 'i' && response != 'c' && response != 'o' && response != 'q') {
			System.out.print("\tYou must enter either s, i c, o, or q => ");
			response = stdin.next().toLowerCase().charAt(0);
		}
		return response;
	}
	
	static Graph build(String graphFile) 
	throws IOException {
		Scanner sc = new Scanner(new File(graphFile));
		int numVerts = sc.nextInt();
		Graph g = new Graph(numVerts);
		
		sc.useDelimiter("\n");

		// read vertices
		for(int v = 0; v < numVerts; v++) {	
			String[] line = sc.next().split("\\|");
			String name = line[0];
			g.addVertex(new Vertex(name, (line[1].equals("y") ? line[2] : "")));
		}

		// read edges
		while(sc.hasNext()) {
			String[] line = sc.next().split("\\|");
			g.addEdge(g.indexOfName(line[0]),g.indexOfName(line[1]));
		}
		
		return g;
	}
	
	public static void main(String[] args)
	throws IOException {
		System.out.print("Enter Friendship Graph file name => ");
		String graphFile = stdin.next();
		Graph graph = build(graphFile);
		System.out.println(graph);
		graph.printSchools();
		char option;
		while((option = getOption()) != 'q') {
			switch (option) {
				case 's':
					System.out.print("\t\tWhich school? => ");
					BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
					String school = in.readLine();
					System.out.println(graph.subgraph(school));
					break;
				case 'i':
					System.out.print("\t\tWho wants the intro? => ");
					String name1 = stdin.next().toLowerCase();
					System.out.print("\t\tWho does "+ name1 + " want to meet? ;) => ");
					String name2 = stdin.next().toLowerCase();
					System.out.println(graph.shortestPath(name1, name2));
					break;
				case 'c':
					ArrayList<Graph> cliques = graph.cliques();
					for(int i=0; i < cliques.size(); i++) {
						System.out.println("Clique " + (i+1));
						System.out.println(cliques.get(i));
					}
					break;
				case 'o':
					break;
				default: break;
			}
		}
	}

}
