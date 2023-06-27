package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.*;

/**
 * A graphical view of the world.
 */
public class GraphicView extends JPanel implements View {
	
	/** The view's width. */
	private final int WIDTH;
	/** The view's height. */
	private final int HEIGHT;
	/** Map */
	private final ArrayList<Tile> map;
	/** Textures */
	private BufferedImage image;
	/** Destination */
	private Rectangle destination;
	/** Trackers */
	private ArrayList<Rectangle> trackers;
	
	private Dimension fieldDimension;
	
	public GraphicView(int width, int height, Dimension fieldDimension, ArrayList<Tile> map, int destination, int difficulty) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.fieldDimension = fieldDimension;
		this.bg = new Rectangle(WIDTH * fieldDimension.width, HEIGHT * fieldDimension.height);
		this.destination = new Rectangle((destination % width) * this.fieldDimension.width, ((int) destination / width) * this.fieldDimension.height, this.fieldDimension.width / 3, this.fieldDimension.height / 3);
		this.trackers = new ArrayList<Rectangle>();
		for (int i = 0; i < difficulty; i++) {
			this.trackers.add(new Rectangle(0, 0));
		}
		// Load and buffer textures
		this.map = map;
		try {
			this.image = ImageIO.read(getClass().getResourceAsStream("/Tileset_2.png"));
		} catch (Exception e) {
			System.out.println("Unable to load textures. Exiting...");
			System.exit(-1);
		}
	}
	
	/** The background rectangle. */
	private final Rectangle bg;
	/** The rectangle we're moving. */
	private final Rectangle player = new Rectangle(1, 1);

	/** Large font */
	private Font arial_24 = new Font("Arial", Font.PLAIN, 24);
	/** Medium font */
	private Font arial_18 = new Font("Arial", Font.PLAIN, 18);
	
	/**
	 * Creates a new instance.
	 */
	@Override
	public void paint(Graphics g) {
		// Paint background just for good measure
		g.setColor(Color.BLACK);
		g.fillRect(bg.x, bg.y, bg.width, bg.height);

		// Paint tile map (getSubimage() appears to be slow?)
		for (int y = 0; y < this.HEIGHT; y++) {
			for (int x = 0; x < this.WIDTH; x++) {
				g.drawImage(image.getSubimage((this.map.get((y * this.WIDTH) + x).getType() * 320) % 1600,
				((int)(this.map.get((y * this.WIDTH) + x).getType() / 5 )) * 320, 320, 320), 
				x*this.fieldDimension.width, y*this.fieldDimension.height, 
				fieldDimension.width, 
				fieldDimension.height, 
				null);
			}
		}
		// Draw destination
		g.setColor(Color.GREEN);
		g.fillRect(destination.x + destination.width, destination.y + destination.height, destination.width, destination.height);
		// Paint player
		g.setColor(Color.BLUE);
		g.fillOval(player.x + player.width / 4, player.y + player.height / 4, player.width / 2, player.height / 2);
		// Draw trackers
		g.setColor(Color.RED);
		Rectangle tracker;
		for (int i = 0; i < this.trackers.size(); i++) {
			tracker = this.trackers.get(i);
			g.fillOval(tracker.x + tracker.width / 4, tracker.y + tracker.height / 4, tracker.width / 2, tracker.height / 2);
		}

		// Draw GUI
		g.setColor(Color.BLACK);
		g.setFont(arial_24);
		g.drawString("Dimensions: " + this.WIDTH + "x" + this.HEIGHT, 10, this.HEIGHT * this.fieldDimension.height + 35);
		g.setFont(arial_18);
		g.drawString("Press \"R\" to restart.", 300, this.HEIGHT * this.fieldDimension.height + 20);
		g.drawString("Press \"E\" or \"Q\" to increase/decrease the difficulty.", 300, this.HEIGHT * this.fieldDimension.height + 40);
	}

	@Override
	public void update(World world) {
		
		// Update players size and location
		player.setSize(fieldDimension);
		player.setLocation((int)
				(world.getPlayerX() * fieldDimension.width),
				(int) (world.getPlayerY() * fieldDimension.height));
		// Update trackers
		for (int i = 0; i < this.trackers.size(); i++) {
			this.trackers.get(i).setSize(fieldDimension.width, fieldDimension.height);
			this.trackers.get(i).setLocation(world.getTracker(i).getX() * fieldDimension.width, world.getTracker(i).getY() * fieldDimension.height);
		}
		repaint();
	}
}
