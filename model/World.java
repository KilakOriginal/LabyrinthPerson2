package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import view.View;

/**
 * The world is our model. It saves the bare minimum of information required to
 * accurately reflect the state of the game. Note how this does not know
 * anything about graphics.
 */
public class World {

	public static final int DIR_RIGHT = 3;
	public static final int DIR_LEFT = 2;
	public static final int DIR_DOWN = 1;
	public static final int DIR_UP = 0;

	/* Maximum iterations for a while loop */
	static final int MAX_ITERATIONS = 10_000;

	/** The world's width. */
	private final int width;
	/** The world's height. */
	private final int height;
	/** The player's x position in the world. */
	private int playerX = 0;
	/** The player's y position in the world. */
	private int playerY = 0;
	/** Trackers */
	private ArrayList<Tracker> trackers = new ArrayList<Tracker>();
	/* Difficulty (number of trackers) */
	private int difficulty;
	/* Tracker speed (tiles per round) */
	private int speed;
	/* Count iterations */
	private int iteration;
	/** Labyrinth map */
	private ArrayList<Tile> map = new ArrayList<Tile>();
	/** Start */
	private int start;
	/** Destination */
	private int destination;
	/** Is the game currently running? */
	private boolean running;

	/** Set of views registered to be notified of world updates. */
	private final ArrayList<View> views = new ArrayList<>();

	/**
	 * Creates a new world with the given size.t
	 */
	public World(int width, int height, String map, int start, int destination, int difficulty) throws Exception {
		// Normally, we would check the arguments for proper values
		this.width = width;
		this.height = height;
		// Load map (easier to implement the pathfinding with tile objects although I would't do this in an actual game)
		// TODO: Use FileReader instead
		List<Integer> mapList = Arrays.stream(map.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		for (int i = 0; i < width * height; i++)
			this.map.add(new Tile(i % width, (int)(i / width), mapList.get(i)));

		// Initialise variables
		this.playerX = start % width;
		this.playerY = (int) (start / width);
		this.start = start;
		this.destination = destination;
		this.difficulty = difficulty;
		this.speed = 1;
		this.iteration = 0;
		this.running = true;

		// Generate trackers
		for (int i = 0; i < difficulty && i < 3; i++) {
			this.trackers.add(new Tracker(3+i, 23));
			this.trackers.get(i).setMap(this.map, width);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Getters and Setters

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getPlayerX() {
		return playerX;
	}

	public Tracker getTracker(int i) {
		return this.trackers.get(i);
	}

	public int getDestination() {
		return this.destination;
	}

	public void setPlayerX(int playerX) {
		switch(this.map.get(this.playerY*this.width + playerX).getType()) {
			case 0:
				break;
			default:
				this.playerX = playerX % this.width;
				break;
		}
		
		updateViews();
	}

	public int getPlayerY() {
		return playerY;
	}

	public ArrayList<Tile> getMap() {
		return new ArrayList<Tile>(this.map);
	}

	public boolean getRunning() {
		return this.running;
	}

	public void setPlayerY(int playerY) {
		switch(this.map.get(playerY*this.width + this.playerX).getType()) {
			case 0:
				break;
			default:
				this.playerY = playerY % this.height;
				break;
		}
		
		updateViews();
	}

	///////////////////////////////////////////////////////////////////////////
	// Player Management
	
	/**
	 * Moves the player along the given direction.
	 * 
	 * @param direction where to move. 1 up, 2 down, 3, left, 4 right
	 */
	public void movePlayer(int direction) {	
		// The direction tells us exactly how much we need to move along
		// every direction
		if (this.running) {
			setPlayerX(getPlayerX() + Direction.getDeltaX(direction));
			setPlayerY(getPlayerY() + Direction.getDeltaY(direction));

			// Check if the trackers have won
			for (int i = 0; i < this.trackers.size(); i++) {
				int currentX = this.trackers.get(i).getX();
				int currentY = this.trackers.get(i).getY();

				if (this.playerX == currentX && this.playerY == currentY) {
					System.out.println("Game Over!");
					this.running = false;
				}

				this.trackers.get(i).reset();
			}

			// Update trackers
			this.updateCosts();
			this.search();
			for (int i = 0; i < this.speed; i++)
				this.trackPath();

			// Check if player has won
			if (this.playerX == this.destination % this.width && this.playerY == (int) (this.destination / this.height))
				{
					System.out.println("Good Job!");
					this.running = false;
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Tracker management
	/**
	 * For each tracker, update the costs of each tile.
	 */
	private void updateCosts() {
		for (int k = 0; k < this.trackers.size(); k++)
			for (int i = 0; i < this.width * this.height; i++) {
				switch (this.map.get(i).getType()) {
					case 0:
						this.map.get(i).setGcost(Double.POSITIVE_INFINITY);
						this.map.get(i).setHcost(Double.POSITIVE_INFINITY);
						break;
					default:
						this.trackers.get(k).setCost(i, this.playerX, this.playerY);
						break;
				}
			}
	}

	/**
	 * For each tracker, determine "shortest" path to the player using a slightly tweaked A* algorithm.
	 */
	private void search() {
		int it = 0;

		for (int k = 0; k < this.trackers.size(); k++) {
			Tracker tracker = this.trackers.get(k);
			while (tracker.getDestinationReached() == false && it < MAX_ITERATIONS) {
				int currentTile = tracker.getCurrenTile().getX() + tracker.getCurrenTile().getY() * this.width;
				tracker.setChecked(currentTile);
				tracker.setOpen((currentTile - 1) % (this.width * this.height));
				tracker.setOpen((currentTile + 1) % (this.width * this.height));
				tracker.setOpen((currentTile - this.width) % (this.width * this.height));
				tracker.setOpen((currentTile + this.width) % (this.width * this.height));

				int bestTileIndex = 0;
				double bestTilefCost = Double.POSITIVE_INFINITY;

				for (int i = 0; i < tracker.getOpenList().size(); i++) {
					if (tracker.getOpenList().get(i).getFcost() < bestTilefCost) {
						bestTileIndex = i;
						bestTilefCost = tracker.getOpenList().get(i).getFcost();
					} else if(tracker.getOpenList().get(i).getFcost() == bestTilefCost && tracker.getOpenList().get(i).getGcost() < tracker.getOpenList().get(bestTileIndex).getGcost()) {
						bestTileIndex = i;
						bestTilefCost = tracker.getOpenList().get(i).getFcost();
					}
				}

				tracker.setCurrentTile(tracker.getOpenList().get(bestTileIndex));

				if (tracker.getCurrenTile().getX() == playerX && tracker.getCurrenTile().getY() == playerY)
					tracker.setDestinationReached(true);

				it++;	// Prevent infinite loops
			}
		}
	}

	/**
	 * Trackers will track the path calculated by the search method.
	 */
	private void trackPath() {
		for (int k = 0; k < this.trackers.size(); k++) {
			ArrayList<Integer> path = new ArrayList<Integer>();
			path.add(this.playerX + this.playerY * this.width);
			Tracker tracker = this.trackers.get(k);
			Tile trackerTile = tracker.getMap().get(tracker.getX() + tracker.getY() * this.width);
			Tile currentTile = tracker.getCurrenTile();

			while (currentTile.getX() != trackerTile.getX() || currentTile.getY() != trackerTile.getY()) {
				path.add(currentTile.getX() + currentTile.getY() * this.width);
				currentTile = currentTile.getPredecessor();
			}
			tracker.setLocation(path.get(path.size()-1) % this.width, (int)(path.get(path.size()-1) / this.width));
			path.clear();
		}
	}

	/**
	 * Increases or decreases difficulty.
	 * As the tracker maps aren't deep copies, the application will no longer work properly with more than one active tracker as they will be accessing the same tiles.
	 * @param amount
	 */
	public void changeDifficulty(int amount) {
		this.difficulty += amount;
		
		// Value positive or negative?
		switch (amount >> 31) {
			case 0:
				//if ((this.difficulty-1) % 2 == 0)
				//	this.trackers.add(new Tracker((2 + difficulty + 1) % this.width, 23));
				this.speed++;
				break;
			case -1:
				//if (this.trackers.size() > 0)
				//	this.trackers.remove(this.trackers.size() - 1);
				this.speed--;
				break;
		}
	}

	/**
	 * Restart the game
	 */
	public void restart() {
		this.playerX = start % width;
		this.playerY = (int) (start / width);
		this.iteration = 0;
		this.running = true;
		this.trackers.clear();
		for (int i = 0; i < difficulty; i++) {
			this.trackers.add(new Tracker(3+i, 23));
			this.trackers.get(i).setMap(this.map, width);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// View Management

	/**
	 * Adds the given view of the world and updates it once. Once registered through
	 * this method, the view will receive updates whenever the world changes.
	 * 
	 * @param view the view to be registered.
	 */
	public void registerView(View view) {
		views.add(view);
		view.update(this);
	}

	/**
	 * Updates all views by calling their {@link View#update(World)} methods.
	 */
	private void updateViews() {
		for (int i = 0; i < views.size(); i++) {
			views.get(i).update(this);
		}
	}

}
