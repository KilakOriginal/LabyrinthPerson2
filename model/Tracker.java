package model;

import java.util.ArrayList;

/**
 * Class for storing tracker's positions and costs/paths to player
 */
public class Tracker {

    private int width;
    private int height;
    private int x;
    private int y;

    private int mapWidth;
    private ArrayList<Tile> map = new ArrayList<Tile>();
    private ArrayList<Tile> openList = new ArrayList<Tile>();
    private ArrayList<Tile> checkedTiles = new ArrayList<Tile>();
    private Tile currentTile;
    private boolean destinationReached = false;

    public Tracker(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public Tracker(int x, int y) {
        this.width = 0;
        this.height = 0;
        this.x = x;
        this.y = y;
        this.currentTile = new Tile(x, y, 1);
    }

    public Tracker() {
        this.width = 0;
        this.height = 0;
        this.x = 0;
        this.y = 0;
        this.currentTile = new Tile(0, 0, 1);
    }

    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getMapWidth() { return this.mapWidth; }
    public ArrayList<Tile> getMap() { return this.map; }
    public ArrayList<Tile> getCheckedTiles() { return this.checkedTiles; }
    public ArrayList<Tile> getOpenList() { return this.openList; }
    public Tile getCurrenTile() { return this.currentTile; }
    public boolean getDestinationReached() { return this.destinationReached; }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setMap(ArrayList<Tile> map, int width) { 
        this.mapWidth = width;
        this.map = new ArrayList<Tile>(map);
    }
    /** Set tile checked (A*: this tile has already been checked) and remove it from the lists of tiles to check */
    public void setChecked(int tile) { 
        this.checkedTiles.add(this.map.get(tile));
        this.openList.remove(this.map.get(tile));
    }
    /** Set tile open (A*: this tile sould be taken into consideration) */
    public void setOpen(int tile) {
        if (this.map.get(tile).getOpen() == false && this.map.get(tile).getChecked() == false && this.map.get(tile).getFcost() != Double.POSITIVE_INFINITY) {
            this.map.get(tile).setOpen(true);
            this.map.get(tile).setPredecessor(this.currentTile);
            this.openList.add(this.map.get(tile));
        }
    }
    /** Calculate and update costs of a tile */
    public void setCost(int tile, int destinationX, int destinationY) {
        int hCost = Math.abs(this.map.get(tile).getX() - this.x) + Math.abs(this.map.get(tile).getY() - this.y);
		int gCost = Math.abs(this.map.get(tile).getX() - destinationX) + Math.abs(this.map.get(tile).getY() - destinationY);

		this.map.get(tile).setGcost(gCost);
		this.map.get(tile).setHcost(hCost);
    }
    public void setCurrentTile(Tile tile) { this.currentTile = tile; }
    public void setDestinationReached(boolean state) { this.destinationReached = state; }

    public void reset() {
        this.destinationReached = false;
        this.openList.clear();
        this.checkedTiles.clear();
        this.currentTile = this.map.get(this.x + this.y * this.mapWidth);
        for (int i = 0; i < this.map.size(); i++)
            this.map.get(i).reset();
    }

    public void reset(ArrayList<Tile> map) {
        this.map = new ArrayList<Tile>(map);
        this.reset();
    }
}
