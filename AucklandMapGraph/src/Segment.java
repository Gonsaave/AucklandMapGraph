
import java.util.*;
import java.awt.Graphics;
import java.awt.Point;

public class Segment{

	private Road road;      
	private double length;   
	private Node start;
	private Node end;
	private List<Location> coordinates  = new ArrayList<Location>(); 


	public Segment(Road road, double length, Node start, Node end){
		this.road = road;
		this.length = length;
		this.start=start;
		this.end=end;
	}

	public Segment(String line, Map<Integer, Road> roads,Map<Integer, Node> nodes) {
		String[] values = line.split("\t");
		road = roads.get(Integer.parseInt(values[0]));
		length = Double.parseDouble(values[1]);
		start = nodes.get(Integer.parseInt(values[2]));
		end = nodes.get(Integer.parseInt(values[3]));
		for (int i = 4; i<values.length; i+=2){
			double lat = Double.parseDouble(values[i]);
			double lon = Double.parseDouble(values[i+1]);
			coordinates.add(Location.newFromLatLon(lat, lon));
		}
	}

	public Road getRoad(){
		return road;
	}
	public double getLength(){
		return length;
	}

	public List<Location> getCoords(){
		return coordinates;
	}
	public void addCoordinate(Location loc){
		coordinates.add(loc);
	}

	public Node getStartNode() {
		return start;
	}

	public Node getEndNode() {
		return end;
	}

	public Segment reverse(){
		Segment reverse =  new Segment(road, length, end, start);
		reverse.coordinates = this.coordinates;
		return reverse;
	}

	public String toString(){
		return("Road: "+road.getID()+", Length: "+length+", From/To: "+start.getID()+"/"+end.getID());
	}

	public void draw(Graphics g, Location origin, double scale){
		if (!coordinates.isEmpty()){
			Point p1 = coordinates.get(0).getPoint(origin, scale);
			for (int i=1; i<coordinates.size(); i++){
				Point p2 = coordinates.get(i).getPoint(origin, scale);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
				p1=p2;

			}

		}
	}

	public boolean isOneWay() {
		return this.road.isOneWay();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((coordinates == null) ? 0 : coordinates.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		long temp;
		temp = Double.doubleToLongBits(length);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((road == null) ? 0 : road.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Segment other = (Segment) obj;
		if (coordinates == null) {
			if (other.coordinates != null)
				return false;
		} else if (!coordinates.equals(other.coordinates))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (Double.doubleToLongBits(length) != Double
				.doubleToLongBits(other.length))
			return false;
		if (road == null) {
			if (other.road != null)
				return false;
		} else if (!road.equals(other.road))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

}
