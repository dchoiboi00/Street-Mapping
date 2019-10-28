/* Name: Yoo (Daniel) Choi
 * Net ID: ychoi42
 * Assignment Name: CSC 172 Project 3: Street Mapping
 * Lab Section: MW 2:00 - 3:15
 * I did not collaborate with anyone on this assignment.
 */

/* This class takes in parameters and finds the shortest path using Dijkstra's algorithm. It also paints a map and prints directions.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class StreetMap extends JComponent {
	private static final long serialVersionUID = 1L;
	
	Graph graph = new Graph();
	double x_min = 200, x_max = -200, y_min = 100, y_max = -100;   //for the latitudes and longitudes
	
	//constructor will create the graph based on input txt file
	public StreetMap(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String s;
			while ( (s=br.readLine()) != null) {
				//tokenize the next line, and store in an ArrayList
				StringTokenizer st = new StringTokenizer(s);
				List<String> split = new ArrayList<String>();
				while (st.hasMoreTokens())
					split.add(st.nextToken());
				
				if (split.get(0).equals("i")) {
					graph.addNode(split.get(1), Double.parseDouble(split.get(2)), Double.parseDouble(split.get(3)));
					//set min and max for x,y
					if (Double.parseDouble(split.get(2)) < x_min)
						x_min = Double.parseDouble(split.get(2));
					if (Double.parseDouble(split.get(2)) > x_max)
						x_max = Double.parseDouble(split.get(2));
					if (Double.parseDouble(split.get(3)) < y_min)
						y_min = Double.parseDouble(split.get(3));
					if (Double.parseDouble(split.get(3)) > y_max)
						y_max = Double.parseDouble(split.get(3));
				} else if (split.get(0).equals("r")) {
					graph.addEdge(split.get(1), split.get(2), split.get(3));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		for(Edge e:graph.edges){
        	if( (graph.Nodes.get(e.from).highlighted) && (graph.Nodes.get(e.to).highlighted)) {  //if both points on the edge are highlighted
        		g2.setColor(Color.RED);             //then use red, and a thicker width
        		g2.setStroke(new BasicStroke(2.5f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f));
        	}
        	else{   //if not, then just use black and a thinner width
        		g2.setColor(Color.BLACK);         
        		g2.setStroke(new BasicStroke(1.2f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f));
        	}
        	double x_diff = x_max - x_min;
        	double y_diff = y_max - y_min;
        	
        	double width = getWidth();
        	double height = getHeight();
        	
        	double from_lat = graph.NodeLoc.get(e.from)[0];
        	double from_long = graph.NodeLoc.get(e.from)[1];
        	double to_lat = graph.NodeLoc.get(e.to)[0];
        	double to_long = graph.NodeLoc.get(e.to)[1];
        	
        	//need for it to scale by window size
        	double x1 = width * (from_long - y_min) / y_diff;
        	double x2 = width * (to_long - y_min) / y_diff;
        	double y1 = height * (x_max - from_lat) / x_diff;
        	double y2 = height * (x_max - to_lat) / x_diff;

        	Line2D line = new Line2D.Double(x1,y1,x2,y2);
        	g2.draw(line);
		}
	}
	
	public static void main(String[] args) {
		String filename = args[0];
		StreetMap map = new StreetMap(filename);
		map.graph.processInfo();
		
		//if there is show, then directions and start/end are in positions 3 and 4
		if (Arrays.asList(args).contains("--show")) {
			//if args are "ur.txt --show --directions HOYT MOREY"
			if (Arrays.asList(args).contains("--directions")) {
				String from = args[3];
				String to = args[4];
				List<String> path = map.graph.getShortestPath(from, to);
				if (path.size() == 0)
					System.out.println("There is no available path between " + from + " and " + to + ".");
				else {
					System.out.println("Shortest path: " + path);
					System.out.println("Total distance traveled: " + map.graph.Nodes.get(to).distance + " miles.");
				}
			}
			
			//if args are only "ur.txt --show"
			JFrame frame = new JFrame("Street Mapping");
			frame.add(map);
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			if (filename.equals("ur.txt"))
				frame.setSize(600, 750);
			else if (filename.equals("monroe.txt"))
				frame.setSize(800, 750);
			else if (filename.equals("nys.txt"))
				frame.setSize(1200, 750);
			else         //note that you can still resize the window manually
				frame.setSize(750, 750);
			frame.setVisible(true);
		} 
		
		//if args are "ur.txt --directions HOYT MOREY"
		else if (Arrays.asList(args).contains("--directions")) { 
			String from = args[2];
			String to = args[3];
			List<String> path = map.graph.getShortestPath(from, to);
			if (path.size() == 0)
				System.out.println("There is no available path between " + from + " and " + to + ".");
			else {
				System.out.println("Shortest path: " + path);
				System.out.println("Total distance traveled: " + map.graph.Nodes.get(to).distance + " miles.");
			}
		}
	}

}
