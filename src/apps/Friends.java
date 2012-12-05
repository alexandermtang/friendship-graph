// Alexander Tang 
// Brian Li

package apps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import structures.Graph;

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
	
	public static void main(String[] args) 
	throws IOException { 
		System.out.print("Enter Friendship Graph file name => ");
		String graphFile = stdin.next();
		Graph graph = new Graph(new Scanner(new File(graphFile)));
		System.out.println(graph);
		char option;
		while((option = getOption()) != 'q') {
			switch (option) {
				case 's':
					System.out.print("\t\tWhich school? => ");
					String school = stdin.next().toLowerCase();
					ArrayList<Graph> subgraphs = graph.subgraph(school);
					for(int i=0;i < subgraphs.size(); i++) {
						System.out.println(subgraphs.get(i));
					}
					break;
				case 'i':
					System.out.print("\t\tWho wants the intro? => ");
					String name1 = stdin.next().toLowerCase();
					System.out.print("\t\tWho does "+ name1 + " want to meet? ;) => ");
					String name2 = stdin.next().toLowerCase();
					System.out.println(graph.shortestPath(name1, name2));
					//ShortestPaths path = new ShortestPaths(graph, name1, name2);
				
					break;
				case 'c':
					break;
				case 'o':
					break;
				default: break;
			}
		}
	}

}
