package structures;

public class Neighbor {
	public int vnum;
	public Neighbor next;
	public Neighbor(int vnum, Neighbor nbr) {
		this.vnum = vnum;
		next = nbr;
	}
}