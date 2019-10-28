/* Name: Yoo (Daniel) Choi
 * Net ID: ychoi42
 * Assignment Name: CSC 172 Project 3: Street Mapping
 * Lab Section: MW 2:00 - 3:15
 * I did not collaborate with anyone on this assignment.
 */

/* This class implements the Edge class. It contains ID, start point, end point, and its length in miles
 */
public class Edge {
	//class data (public for manipulation in other classes)
	public String ID, from, to;
	public double length;
	
	public Edge(String ID, String from, String to, double length) {
		this.ID = ID;
		this.from = from;
		this.to = to;
		this.length = length;
	}
	
	//return its partner point on the other side of the edge
	public String getNeighbor(String name) {
		if (name.equals(this.from))
			return this.to;
		else
			return this.from;
	}
}
