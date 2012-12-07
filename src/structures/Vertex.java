package structures;

import java.util.LinkedList;

public class Vertex {
	public String name;
	public String school;
	public LinkedList<Integer> neighbors;

	public boolean visited = false;
	public int dfsnum;
	public int back;

	public Vertex(String name, String school) {
		this.name = name;
		this.school = school;
		neighbors = new LinkedList<Integer>();
	}
	
	public boolean equals(Vertex other) {
		if((other != null) && (other instanceof Vertex)) {
			return (name.equals(other.name) && school.equals(other.school));
		}
		return false;
	}
}