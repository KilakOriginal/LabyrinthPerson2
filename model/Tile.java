package model;

/**
 * A class for storing tile information like position, costs, type etc.
 */
public class Tile {
    
    /** Position */
    private int x, y;
    /** Tile checked (A*: this tile has already been checked)? */
    private boolean checked;
    /** Tile open (A*: this tile sould be taken into consideration)? */
    private boolean open;
    /** Costs (A*)? */
    private double hCost, gCost;
    /** Type (texture) */
    private int type;
    /** Predecessor (A*) */
    private Tile predecessor;

    public Tile(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.checked = false;
        this.open = false;
        this.hCost = Double.POSITIVE_INFINITY;
        this.gCost = Double.POSITIVE_INFINITY;
        this.type = type;
    }

    public Tile(Tile other) {
        this.x = other.x;
        this.y = other.y;
        this.checked = false;
        this.open = false;
        this.hCost = Double.POSITIVE_INFINITY;
        this.gCost = Double.POSITIVE_INFINITY;
        this.type = other.type;
    }

    public double getFcost() { return this.gCost + this.hCost; }
    public double getGcost() { return this.gCost; }
    public boolean getChecked() { return this.checked; }
    public boolean getOpen() { return this.open; }
    public int getType() { return this.type; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public Tile getPredecessor() { return this.predecessor; }

    public void setGcost(double gCost) { this.gCost = gCost; }
    public void setHcost(double hCost) { this.hCost = hCost; }
    public void setChecked(boolean state) { this.checked = state; }
    public void setOpen(boolean state) { this.open = state; }
    public void setPredecessor(Tile predecessor) { this.predecessor = predecessor; }

    /** Reset tile (see Tracker.reset() for more info) */
    public void reset() {
        this.checked = false;
        this.open = false;
        this.hCost = Double.POSITIVE_INFINITY;
        this.gCost = Double.POSITIVE_INFINITY;
    }
}
