
import java.util.*;

public class Road{
	
	// lots of extra data in line

    private int id;  
    private String name;  
    private List<Segment> segments = new ArrayList<Segment>();
    private boolean oneWay;
    private int speed;
    private int roadClass;
    private boolean notForCars;
    private boolean notForPedestrians;
    private boolean notForBicycles;
    
    
    public Road(int id, String n, boolean oneway, int speed, int roadClass, boolean notForCars, boolean notForPedestrians, boolean notForBicycles){
    	this.id = id;  
    	this.name = n;  
    	this.oneWay = oneway;  
    	this.speed = speed;  
    	this.roadClass = roadClass;  
    	this.notForCars = notForCars;  
    	this.notForPedestrians = notForPedestrians;  
    	this.notForBicycles = notForBicycles; 
    }
    

	public int getID(){
    	return this.id;
    }
   
    public String getName(){
    	return this.name;
    }
    public String getFullName(){
    	return this.name;
    } 
    public int getRoadclass(){return this.roadClass;}
    public int getSpeed(){return this.speed;}
    public boolean isOneWay(){return this.oneWay;}
    public boolean isNotForCars(){return this.notForCars;}
    public boolean isNotForPedestrians(){return this.notForPedestrians;}
    public boolean isNotForBicycles(){return this.notForBicycles;}

    public void addSegment(Segment seg){
	this.segments.add(seg);
    }

    public List<Segment> getSegments(){
	return this.segments;
    }

    public String toString(){
    return "Road: "+getFullName();
    }
}
