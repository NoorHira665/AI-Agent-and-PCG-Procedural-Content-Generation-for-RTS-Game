package s3.ai;


import java.util.*;

import gatech.mmpm.Entity;
import s3.base.S3;
import s3.entities.S3PhysicalEntity;
import s3.entities.WUnit;
import s3.util.Pair;


public class AStar {
	private double startX, startY, goalX, goalY;
	private S3PhysicalEntity entity;
	private S3 game;
	private final int mapWidth, mapHeight;
	private S3PhysicalEntity temp;


	public static int pathDistance(double start_x, double start_y, double goal_x, double goal_y, S3PhysicalEntity i_entity, S3 the_game) {
		AStar a = new AStar(start_x,start_y,goal_x,goal_y,i_entity,the_game);
		List<Pair<Double, Double>> path = a.computePath();
		if (path!=null) return path.size();
		return -1;
	}

	public AStar(double start_x, double start_y, double goal_x, double goal_y,
			S3PhysicalEntity i_entity, S3 the_game) {
            this.startX = start_x;
			this.startY = start_y;
			this.goalX = goal_x;
			this.goalY = goal_y;
			this.entity = i_entity;
			this.game = the_game;

			//gets map height and width ro prevent generating nodes that are out of bound
			this.mapWidth = game.getMap().getWidth();
			this.mapHeight = game.getMap().getHeight();

			//cloning 1 s3 entity to be used throughout
			this.temp = (S3PhysicalEntity) entity.clone();
	}

	public List<Pair<Double, Double>> computePath() {
		//converting the doubles to int to prevent floating point comparison errors and to get indices for my 2d closed and open arrays
		int startXInt = (int)Math.round(startX);
		int startYInt = (int)Math.round(startY);
		int goalXInt = (int)Math.round(goalX);
		int goalYInt = (int)Math.round(goalY);

		//check to ensure starting point or goal is not out of bounds of the map
		if (startXInt<0 || startYInt <0 || goalXInt <0 || goalYInt<0 || startXInt >= mapWidth || startYInt >= mapHeight || goalXInt >= mapWidth || goalYInt>= mapHeight) {
			return null;
		}

		Node start_node = new Node(startXInt,startYInt,0,heuristic(startXInt,startYInt,goalXInt,goalYInt), null);
		//using 2d boolean arrays the size of the map for very fast lookups.
		//I tried using hashsets with pairs of coordinates first but it was causing my game to freeze after some cycles
		boolean[][] closed = new boolean[mapWidth][mapHeight];
		boolean[][] inOpen = new boolean[mapWidth][mapHeight];
		PriorityQueue<Node> openList = new PriorityQueue<>();

		//Adding node to openList and setting relevant inOpen coordinate to true
		openList.add(start_node);
		inOpen[startXInt][startYInt] = true;


		int[][] neighborDirections = {
				{0,-1}, {0,1}, {-1,0}, {1,0}
		};

		while(!openList.isEmpty()){
			//Removing the head node from openList and setting relevant inOpen coordinate to false
			Node N = openList.poll();
			int Nx = (int)Math.round(N.x);
			int Ny = (int)Math.round(N.y);
			inOpen[Nx][Ny] = false;

			//checking if goal found
			if (Nx == goalXInt && Ny == goalYInt){
				return pathToGoal(N);
			}
			closed[Nx][Ny] = true;

			//expanding node neighbors and adding to openList if not seen before
			for(int[] direction: neighborDirections){
				int neighborX = Nx + direction[0];
				int neighborY = Ny + direction[1];

				//checking if neighbor is out of bounds of the map
				if (neighborX< 0 || neighborY < 0 || neighborX >= mapWidth || neighborY >= mapHeight)
					continue;

				temp.setX(neighborX);
				temp.setY(neighborY);

				//checking if cell in the map is free
				if(game.anyLevelCollision(temp)==null){
					if(!inOpen[neighborX][neighborY] && !closed[neighborX][neighborY]){
						Node neighbor = new Node(neighborX, neighborY, N.g+1, heuristic(neighborX,neighborY, goalXInt, goalYInt), N);
						openList.add(neighbor);
						inOpen[neighborX][neighborY] = true;
				}
				}
			}
		}
		return null;
	}

	public List<Pair<Double, Double>> pathToGoal(Node N) {
		List<Pair<Double,Double>> path = new LinkedList<>();
		while (N != null) {
			if (N.parent != null) { //to skip the start node
				path.addFirst(new Pair<>(N.x, N.y));
			}
			N = N.parent;
		}
		return path;
	}


	private double heuristic(double x1, double y1, double x2, double y2){
		return Math.abs(x1-x2) + Math.abs(y1-y2);
	}

	public class Node implements Comparable<Node>{
		double x,y;
		double g;
		double h;
		Node parent;

		public Node(double x, double y, double g, double h, Node parent){
			this.x=x;
			this.y=y;
			this.g=g;
			this.h=h;
			this.parent=parent;

		}

		public double getF(){
			return g+h;
		}

		@Override //-1 if a<b, 0 if a==b, 1 if a>b
		public int compareTo(Node other){
			return Double.compare(this.getF(), other.getF());
		}

		//I used AI for the equals and hashCode function below
		@Override
		public boolean equals(Object obj){
			if (this == obj) return true;

			Node n = (Node) obj;
			return (int)Math.round(this.x) == (int)Math.round(n.x) && (int)Math.round(this.y) == (int)Math.round(n.y);
		}

		@Override public int hashCode(){
			int xRounded = (int)Math.round(x);
			int yRounded = (int)Math.round(y);
			return 31 * xRounded + yRounded;
		}




	}

}
