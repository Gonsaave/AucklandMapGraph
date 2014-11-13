import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AucklandMapGraph {

	Location mapOrigin;
	double mapScale;
	double[] mapLimits;
	private int window = 700;
	private double zoomPerClick = 1.2;  
	private double panPerClick = 0.2;
	double[] limits;
	double leftBound ;
	double rightBound ;
	double botBound;
	double topBound;



	private JFileChooser fc;

	private JFrame frame;
	private JComponent component; 
	private JTextArea textArea;
	private JTextField textField;
	private JButton button;;

	// Road Data Collections
	Map<Integer,Road> roads = new HashMap<Integer,Road>();;
	Set<String> roadNames = new HashSet<String>();
	Map<String,Set<Road>> roadsByName = new HashMap<String,Set<Road>>();

	//Node Data Collections
	static Map<Integer,Node> nodes = new HashMap<Integer,Node>();

	//Articulation Points
	static Map<Integer,Node> articulationPoints = new HashMap<Integer,Node>();

	// Astar collections
	private List<Segment> pathway = new ArrayList<Segment>(2);
	private Queue<Node> fringe = new PriorityQueue<Node>();
	private Stack<Node> pathStack = new Stack<Node>();

	private Node selected;
	private Node selected2;

	private Node as1;	//Selected Nodes kept seperate from Normal highlighting, For Route highlighting using A*
	private Node as2;


	public AucklandMapGraph(){
		setupGUI();
		loadData();
		setUpScaling();
		component.repaint();
	}

	@SuppressWarnings("serial")
	private void setupGUI() {
		frame = new JFrame("Map of Auckland Roads");
		frame.setSize(window, window);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
		component = new JComponent(){
			protected void paintComponent(Graphics g){redraw(g);}
		};
		frame.add(component, BorderLayout.CENTER);

		textArea = new JTextArea(5, 100);
		textArea.setEditable(false);

		JScrollPane TSP = new JScrollPane(textArea);
		frame.add(TSP, BorderLayout.SOUTH);


		JPanel panel = new JPanel();
		frame.add(panel, BorderLayout.NORTH);

		button = new JButton("Quit");
		panel.add(button);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){System.exit(0);}});


		button = new JButton("<");
		panel.add(button);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){pan("left");}});

		button = new JButton(">");
		panel.add(button);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){pan("right");}});

		button = new JButton("^");
		panel.add(button);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){pan("up");}});

		button = new JButton("v");
		panel.add(button);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){pan("down");}});

		button = new JButton("+");
		panel.add(button);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){zoom("in");}});

		button = new JButton("-");
		panel.add(button);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){zoom("out");}});

		textField = new JTextField(20);
		panel.add(textField);
		frame.setVisible(true);
		textField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				for(Node n : nodes.values()){
					for(Segment s : n.getOutNeighbours()){
						if(s.getRoad().toString()==textField.getText()){
							textArea.append(s.toString()+" Found");
							return;
						}
					}
				}
				textArea.setText("Road not found");



				component.repaint();
			}});
		component.repaint();


		component.addMouseListener(new MouseAdapter(){


			public void mouseReleased(MouseEvent e){

				if(selected==null){	
					try{
						selected = findNode(e.getPoint(),mapOrigin,mapScale);
						textArea.setText(selected.toString());
						as1 = selected;
						component.repaint();
					}catch(NullPointerException error){
						textArea.append("Node not selected");
					}
					finally {component.repaint();}
				}
				else{
					try{
						selected2 = findNode(e.getPoint(),mapOrigin,mapScale);
						textArea.append("\n"+selected2.toString());
						as2 = selected2;
						component.repaint();
					}catch(NullPointerException error){
						textArea.append("Node not selected");
					}
					finally {component.repaint();}
				}

			}
			;});

		button = new JButton("AP's");
		panel.add(button);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				if(selected==null){
					textArea.setText("Please First Select a Root Node");
					return;
				}
				for(Node n : nodes.values()){
					n.setDepth(Integer.MAX_VALUE);
					n.setReachBack(Integer.MAX_VALUE);
				}
				articulationPoints(selected);
				component.repaint();
			}});


		button = new JButton("A*D");		
		panel.add(button);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){	
				if(as1==null||as2==null){						
					textArea.setText("Please Select two nodes"); return; }
				else{
					pathway = Astar(as1, as2,true);
					component.repaint();
//					pathway = null;
//					pathway = new ArrayList<Segment>(2);
					fringe = new PriorityQueue<Node>();
					pathStack = new Stack<Node>();
//					as1 = null;
//					as2 = null;
				}
			}});

	}




	private void loadData(){
		loadRoads();
		loadNodes();
		loadSegments();
		textArea.setText("Roads loaded: "+roads.size()+", Nodes loaded: "+nodes.size());
		int ans = 0;
		for (Node node : nodes.values()){
			ans += node.getOutNeighbours().size();
		}
		textArea.append("\nSegments loaded: " + ans);
		try {
			Thread.sleep(3000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();				// clear the text areas after several seconds
		}
		textArea.setText("");
	}

	private void loadRoads(){
		fc = new JFileChooser();
		if (fc.showOpenDialog(frame)!=JFileChooser.APPROVE_OPTION){return;}
		String f = fc.getSelectedFile().getPath();
		File roadData = new File(f);
		BufferedReader BR;
		try{
			BR = new BufferedReader(new FileReader(roadData));
			BR.readLine(); 
			while (true){
				String line = BR.readLine();
				if (line==null) {break;}
				String[] info = line.split("\t");
				int id = Integer.parseInt(info[0]);
				String name = info[2];
				String city = info[3];
				if (city.equals("-")) { city = ""; }
				boolean oneway = info[4].equals("1");  
				int speed = Integer.parseInt(info[5]);
				int roadClass = Integer.parseInt(info[6]);  
				boolean notForCars = info[7].equals("1");  
				boolean notForPedestrians = info[8].equals("1");  
				boolean notForBicycles = info[9].equals("1"); 
				Road r = new Road(id,name,oneway,speed,roadClass,notForCars,notForPedestrians,notForBicycles);
				roads.put(r.getID(), r);
				String fullName = r.getFullName();
				roadNames.add(fullName);
				Set<Road> rds = roadsByName.get(fullName);
				if (rds==null){
					rds = new HashSet<Road>(4);
					roadsByName.put(fullName, rds);
				}
				rds.add(r);

			}
		}catch(IOException e){System.out.println("Failed to open roadID-roadInfo.tab: " + e);}
	}

	private void loadNodes(){
		fc = new JFileChooser();
		if (fc.showOpenDialog(frame)!=JFileChooser.APPROVE_OPTION){return;}
		String f = fc.getSelectedFile().getPath();
		File nodeData = new File(f);
		BufferedReader BR;
		try{
			BR = new BufferedReader(new FileReader(nodeData));
			while (true){
				String line = BR.readLine();
				if (line==null) { break; }
				Node node = new Node(line);
				nodes.put(node.getID(), node);
			}

		} catch(IOException e){System.out.println("Failed to open roadID-roadInfo.tab: " + e);}
	}

	private void loadSegments(){
		fc = new JFileChooser();
		if (fc.showOpenDialog(frame)!=JFileChooser.APPROVE_OPTION){return;}
		String f = fc.getSelectedFile().getPath();
		File segmentData = new File(f);
		BufferedReader BR;
		try{
			BR = new BufferedReader(new  FileReader(segmentData));
			BR.readLine();  

			while (true){
				String line = BR.readLine();
				if (line==null) { break; }

				Segment seg = new Segment(line, roads, nodes);
				Node node1 = seg.getStartNode();
				Node node2 = seg.getEndNode();
				if(node1!=null){
					node1.addOutSegment(seg);
				}
				if(node2!=null){
					node2.addInSegment(seg);
				}
				Road road = seg.getRoad();
				road.addSegment(seg);
				if (!road.isOneWay()){
					Segment revSeg = seg.reverse();
					if(node2!=null){
						node2.addOutSegment(revSeg);
					}
					if(node1!=null){
						node1.addInSegment(revSeg);
					}
				}
			}
		} catch(IOException e){System.out.println("Failed to open roadID-roadInfo.tab: " + e);}
	} 

	private void setUpScaling(){
		limits = getBoundaries();
		leftBound = limits[0];
		rightBound = limits[1];
		botBound = limits[2];
		topBound = limits[3];
		resetOrigin();
	}

	public double[] getBoundaries(){
		double leftBound = Double.MAX_VALUE;
		double rightBound = Double.MIN_VALUE;
		double botBound = Double.MAX_VALUE;
		double topBound = Double.MIN_VALUE;

		for (Node node : nodes.values()){
			Location location = node.getLocation();
			if (location.x < leftBound) {leftBound = location.x;}
			if (location.x > rightBound) {rightBound = location.x;}
			if (location.y < botBound) {botBound = location.y;}
			if (location.y > topBound) {topBound = location.y;}
		}
		return new double[]{leftBound, rightBound, botBound, topBound};
	}

	private void resetOrigin(){
		mapOrigin = new Location(leftBound, topBound);
		mapScale = Math.min(window/(rightBound-leftBound),
				window/(topBound-botBound));
	}

	private void redraw(Graphics g){
		g.setColor(Color.blue);
		for (Node n : nodes.values()){
			for (Segment seg : n.getOutNeighbours()){
				seg.draw(g, mapOrigin, mapScale);
			}
		}
		g.setColor(Color.black);
		for(Node node : nodes.values()){
			node.draw(g, mapOrigin, mapScale,false);
		}

		if(selected!=null){
			g.setColor(Color.red);
			selected.draw(g, mapOrigin, mapScale,true);
		}
		if(selected2!=null){
			g.setColor(Color.red);
			selected2.draw(g, mapOrigin, mapScale,true);
			selected = null;
			selected2 = null;

		}											
		if(pathway!=null){							// reset pathway, selected,selected2- bug - cant zoom on pathway - add polygons and roadsize/colour
			g.setColor(Color.red);					// different heuristics
			for(Segment s : pathway){				// loading screen on start up
				s.draw(g, mapOrigin, mapScale);
				//pathway = null;
			}
		}
		if(articulationPoints!=null){
			g.setColor(Color.red);
			for(Node n : articulationPoints.values()){
				n.draw(g,mapOrigin, mapScale, true);
			}
		}


	}

	private void pan(String p){

		double change = window*panPerClick/mapScale;
		if(p=="left") 		{mapOrigin = new Location(mapOrigin.x-change, mapOrigin.y); }
		else if(p=="right") 	{mapOrigin = new Location(mapOrigin.x+change, mapOrigin.y); }
		else if(p=="up")		{mapOrigin = new Location(mapOrigin.x, mapOrigin.y+change); }
		else if(p=="down") 	{mapOrigin = new Location(mapOrigin.x, mapOrigin.y-change); }

		component.repaint();	    
	}

	private void zoom(String zoom){
		double change = 0;
		if(zoom.equals("out")){
			mapScale =  mapScale/zoomPerClick;
			change = window/mapScale*(zoomPerClick-1)/zoomPerClick/2;
			mapOrigin = new Location(mapOrigin.x - change, mapOrigin.y + change);
		}
		else{
			change = window/mapScale*(zoomPerClick-1)/zoomPerClick/2;
			mapOrigin = new Location(mapOrigin.x + change, mapOrigin.y - change);
			mapScale =  mapScale*zoomPerClick; 
		}
		component.repaint();
	}

	public Node findNode(Point mouseP, Location origin, double scale){
		Location mouse = Location.newFromPoint(mouseP, origin, scale);
		Node closestNode = null;
		double minimumDistance = Double.MAX_VALUE;
		for (Node node : nodes.values()){
			double distance = node.distanceTo(mouse);
			if (distance<minimumDistance){
				minimumDistance = distance;
				closestNode = node;
			}
		}
		return closestNode;
	}

	public void articulationPoints(Node root){
		root.setDepth(0);	//set start depth to 0
		int numSubTrees = 0;	//intialise number sub trees to 0
		for(Segment s : root.getOutNeighbours()){
			Node neighbour = s.getEndNode();	// for each neighbour of start
			if(neighbour.getDepth()==Integer.MAX_VALUE){	// if neighbours depth is Infinite
				recArtPts(neighbour, 1, root); 
				numSubTrees++;
			}
		}	
		if(numSubTrees>0){
			articulationPoints.put(root.getID(), root);
		}
	}
	public int recArtPts(Node n, int d, Node FN){	// node, depth, fromNode
		n.setDepth(d);
		n.setReachBack(d);
		for(Segment s : n.getOutNeighbours()){
			Node neighbour = s.getEndNode();
			if(!neighbour.equals(FN)){			// for each neighbour of Node other than FromNode
				if(neighbour.getDepth()<Integer.MAX_VALUE){
					n.setReachBack(Math.min(neighbour.getDepth(),n.getReachBack()));		//WHAT RB getting set here, and what is 2nd arg to min
				}
				else{
					neighbour.setReachBack(recArtPts(neighbour,d+1,n));		//neigbour.setRB or n.setRB
					n.setReachBack(Math.min(n.getReachBack(),neighbour.getReachBack()));
					if(neighbour.getReachBack()>=d){
						articulationPoints.put(n.getID(),n);
					}
				}
			}
		}	
		return n.getReachBack();
	}

	List<Segment> Astar(Node start, Node goal, boolean h) {	// start - goal , boolean for time / speed
		textArea.setText("");
		Node process = null;	
		fringe.add(start);		// put start node onto fringe
		while (!fringe.isEmpty()) {	// while fringe isnt empty
			process = fringe.poll();		// process is 'Current' node being processed
			if (!process.isVisited()) {	// if process hasnt been visited before
				process.setVisited(true);	// visit it

				if (process.getID() == goal.getID()) {	// if process is our goal node
					Node current = goal;		// current is set as goal node
					while (current.getID() != start.getID()) {	
						pathStack.add(current);
						current = current.getPreviousNode();
					}
					pathStack.add(start);
					break;
				}

				for (Segment outSegment : process.getOutNeighbours()) {
					Node n = outSegment.getEndNode();
					if (!n.isVisited()) {
						n.setCostSoFar(process.getCostSoFar()
								+ outSegment.getLength());
						n.heuristic(goal);
						n.setPreviousNode(process);
						fringe.add(n);
					}
				}

			}
		}	

		if (pathStack.isEmpty()) {
			textArea.append("\nerror: could not find a path to the goal node!");
			return null;
		}
		List<Segment> segmentList = new ArrayList<Segment>();
		String prevRoad = null;
		double prevRoadLengthSoFar = 0;
		while (!pathStack.isEmpty()) {
			Node nd = pathStack.pop();
			textArea.append("\nNodeID: " + nd.getID() + " - " + nd.toString());
			if (nd.getID() != start.getID()) {
				for (Segment segment : nd.getInNeighbours()) {
					if (segment.getStartNode().getID() == nd.getPreviousNode().getID()&& segment!=null) {
						if (prevRoad != null && !prevRoad.equals(segment.getRoad().getName())) {
							//textArea.append("\n" + prevRoad + ": " + prevRoadLengthSoFar + " km");
							prevRoadLengthSoFar = 0;
						}
						segmentList.add(segment);
						prevRoad = segment.getRoad().getName();
						prevRoadLengthSoFar += segment.getLength();

					}
				}
			}
		}
		textArea.setText("");
		double distance = 0;
		textArea.append("From "+ start.toString()+" To "+ goal.toString()+"\nvia:\n");
		Map <String,Double> distances = new HashMap<String,Double>();
		for(Segment p : segmentList){
			if(distances.containsKey(p.getRoad().toString())){
				double temp = p.getLength();	
				distance = distance + temp;		
				temp = temp + distances.get(p.getRoad().toString());
				if(!(p.getRoad().toString().equals(""))){
					distances.put(p.getRoad().toString(), temp);
				}

			}
			else{
				distance = distance + p.getLength();
				distances.put(p.getRoad().toString(),p.getLength());
			}
		}
		for(String s : distances.keySet()){
			if(distances.get(s)!=null){
				textArea.append(s+" With Length: "+distances.get(s)+"\n");
			}

		}
		textArea.append("Total Length: "+ distance+"\n");
		return segmentList;

	}


	@SuppressWarnings("unused")
	public static void main(String[] arguments){
		AucklandMapGraph obj = new AucklandMapGraph();
	}	


}
