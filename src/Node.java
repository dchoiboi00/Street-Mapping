/* Name: Yoo (Daniel) Choi
 * Net ID: ychoi42
 * Assignment Name: CSC 172 Project 3: Street Mapping
 * Lab Section: MW 2:00 - 3:15
 * I did not collaborate with anyone on this assignment.
 */

/* This class implements the Node class. It contains distance (from the source), highlighted, visited, adjacent edges,
 * and a pointer to its previous node on the path
 */

import java.util.*;
public class Node {
	//class data
	public boolean highlighted, visited;  //highlighted means it's on the shortest-path using dijkstra's alg
	public double distance = Integer.MAX_VALUE;
	public List<Edge> edges = new ArrayList<Edge>(); //adjacent edges
	public String prev;
	
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	public void setPrev(String prev) {
		this.prev = prev;
	}
	
}
