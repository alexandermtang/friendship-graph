package structures;

public class Vertex {
	public String name;
	public String school;
	public Neighbor neighbors;
	public Vertex(String name, String school, Neighbor neighbors) {
		this.name = name;
		this.school = school;
		this.neighbors = neighbors;
	}
}