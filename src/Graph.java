/* Name: Yoo (Daniel) Choi
 * Net ID: ychoi42
 * Assignment Name: CSC 172 Project 3: Street Mapping
 * Lab Section: MW 2:00 - 3:15
 * I did not collaborate with anyone on this assignment.
 */

/* This class implements a Graph class. This contains all the methods relating to the overall purpsoe of the program.
 */

import java.util.*;
public class Graph {
	//graph data
	Map<String, double[]> NodeLoc;   //label --> (lat, long)
	Map<String, Node> Nodes;         //label --> Node object
	ArrayList<Edge> edges;
	
	public Graph() {
		NodeLoc = new HashMap<String, double[]>();
		Nodes = new HashMap<String, Node>();
		edges = new ArrayList<Edge>();
	}
	
	//adds nodes into NodeLoc using the 3 pieces of data from the txt files
	public void addNode(String name, double latitude, double longitude) {
		NodeLoc.put(name, new double[] {latitude, longitude});
	}
	
	//calculates edge length in miles using distance formula, to store in the edge object. Source: Haversine Formula
	public double getEdgeLength(String node1, String node2) {
		double lat1 = Math.toRadians(NodeLoc.get(node1)[0]);
		double lat2 = Math.toRadians(NodeLoc.get(node2)[0]);
		double lon1 = Math.toRadians(NodeLoc.get(node1)[1]);
		double lon2 = Math.toRadians(NodeLoc.get(node2)[1]);
		
		double d_lat = lat2 - lat1;
		double d_lon = lon2 - lon1; 
		
		double a = Math.pow(Math.sin(d_lat / 2), 2) + Math.cos(lat1)*Math.cos(lat2) * Math.pow(Math.sin(d_lon / 2), 2);
		double earthRadius = 3958.8;  //in miles
		return (2*earthRadius * Math.asin(Math.sqrt(a)));
	}
	
	//add edge to ArrayList edges, using the 3 pieces of data from the txt files
	public void addEdge(String ID, String from, String to) {
		edges.add(new Edge(ID, from, to, this.getEdgeLength(from, to)));
	}
	
	//this method is crucial in that we   1. Create an edges array   2. Put Node objects in Nodes   3. Store adjacent edges in each Node that apply
	public void processInfo() {
		//The edges array is created so we can loop through it when we store adjacent edges into each Node object
		Edge[] edges = new Edge[this.edges.size()];
		for (int i = 0; i < edges.length; i++)
			edges[i] = this.edges.get(i);
		
		//Initialize the Nodes map: label --> Node object
		for (String name : NodeLoc.keySet())
			Nodes.put(name, new Node());
		
		//Store adjacent edges into the List<Edge> stored in each Node object
		for (int i = 0; i < edges.length; i++) {
			Nodes.get(edges[i].from).edges.add(edges[i]);   //add the edge to its starting node
			Nodes.get(edges[i].to).edges.add(edges[i]);     //add the edge to its end node
		}
	}
	
	//Calculate single-source shortest-path using Dijkstra's Algorithm. Notice we break the loop when 'end' is visited, so we might not cover all nodes
	public void dijkstra(String start, String end) {
//		System.out.println("Start Dijkstra");
		String current = start;
		Nodes.get(current).setDistance(0);  //initialize source as distance 0
		
		//iterate over all nodes, notice 'name' is not used. It's just for the correct number of iterations
		//It's because the order in which we iterate is by distance (shortest distance goes next), not by name
		for (String name : Nodes.keySet()) {
			List<Edge> adjEdges = new ArrayList<Edge>();
			
			//stop conditions
			if (Nodes.get(end).visited)
				break;   //if we visited the end node, just stop
			if (current.equals(""))
				break;   //if there are no more unvisited nodes (in the case that some are unreachable), then stop
			
			//For each adjacent node from the 'current' node we're currently working on, update distance
			adjEdges = Nodes.get(current).edges;
			for (int i = 0; i < adjEdges.size(); i++) {
				String neighbor = adjEdges.get(i).getNeighbor(current);
				if (!Nodes.get(neighbor).visited) {  //if the neighbor is not yet visited
					double dist = Nodes.get(current).distance + adjEdges.get(i).length;   //distance of neighbor through current node
					if (dist < Nodes.get(neighbor).distance) {  //if we did indeed find a shorter path
						Nodes.get(neighbor).setDistance(dist);  //update distance
						Nodes.get(neighbor).setPrev(current);      //have its prev pointer point to 'current'
					}
				}
			}
			Nodes.get(current).setVisited(true);
			
			current = this.getNext();
		}
//		System.out.println("End Dijkstra");
	}
	
	//find the next node to work on in Dijkstra's algo: has to be unvisited, and have the shortest-distance
	public String getNext() {
//		System.out.println("Started getting next");
		String next = "";
		double shortestDistance = Integer.MAX_VALUE;
		for (String key : Nodes.keySet()) {
			double dist = Nodes.get(key).distance;
			if ( !(Nodes.get(key).visited) && (dist < shortestDistance)) {   //if the key is unvisited and has a shorter distance, this is the one
				shortestDistance = dist;
				next = key;
			}
		}
//		System.out.println("Ended getting next");
		return next;
	}
	
	//uses a stack to store the shortest path step-by-step, using the 'prev' pointers. Also, along the way, we will highlight the path we took.
	public List<String> getShortestPath(String start, String end){
		List<String> path = new ArrayList<String>();
		this.dijkstra(start, end);
		
//		System.out.println("Started stacking paths");
		//if start is same as end
		if (start.equals(end)) {
			path.add(start);
			return path;
		}
		
		//if after running dijkstra, 'end' still has a distance of infinity, it means 'end' was unreachable from 'start'
		else if (Nodes.get(end).distance == Integer.MAX_VALUE)
			return path;
		
		else {
			Stack<String> stack = new Stack<String>();
			String nodeOnPath = end;
			stack.push(nodeOnPath);     //first push the destination node
			Nodes.get(end).setHighlighted(true);
			
			//keep going to its 'prev' node, until we reach the node right before the start node
			while( !(Nodes.get(nodeOnPath).prev.equals(start))) {
				nodeOnPath = Nodes.get(nodeOnPath).prev;
				Nodes.get(nodeOnPath).setHighlighted(true);
				stack.push(nodeOnPath);
			}
			stack.push(start);   //finally push the start node
			Nodes.get(start).setHighlighted(true);
			
			//now pop the Strings from the stack, this is our shortest-path
			while (!stack.isEmpty()) {
				path.add(stack.pop());
			}
			
//			System.out.println("Ended stacking path");
			return path;
		}
	}
}
