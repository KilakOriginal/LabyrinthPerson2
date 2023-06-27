package controller;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFrame;

import model.World;
import view.ConsoleView;
import view.GraphicView;

/**
 * This is our main program. It is responsible for creating all of the objects
 * that are part of the MVC pattern and connecting them with each other.
 */
public class Labyrinth {

	public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	// Dimension of the game board (25x25).
            	int width = 25;
    			int height = 25;
                int start = 55;
                int destination = 91;
                int difficulty = 1;
    			// Create a new game world.
            	World world = null;
                try {
                    world = new World(width, height,
                        "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
                        "0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0," +
                        "0,1,0,0,1,1,1,0,1,1,1,1,1,1,0,1,1,0,0,0,0,1,1,1,0," +
                        "0,1,1,0,1,1,1,0,1,1,1,1,1,0,0,1,1,1,1,1,0,0,1,1,0," +
                        "0,1,1,1,1,1,1,0,1,1,0,1,0,0,1,1,1,1,1,1,1,0,1,1,0," +
                        "0,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,1,0,1,1,0," +
                        "0,1,1,1,1,0,1,1,1,1,0,1,0,1,1,1,1,1,0,1,1,0,1,1,0," +
                        "0,1,0,1,1,0,1,0,1,1,0,1,1,1,0,0,0,1,0,1,1,0,1,1,0," +
                        "0,1,0,1,1,0,0,0,1,1,0,1,1,1,0,0,0,1,0,0,0,0,1,1,0," +
                        "0,1,0,0,1,1,1,1,1,1,0,0,1,1,0,0,0,1,1,1,1,1,1,1,0," +
                        "0,1,1,0,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,0,0," +
                        "0,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,1,1,1,1,1,0," +
                        "0,1,0,1,0,0,1,1,1,1,0,1,1,1,1,0,0,0,0,0,0,1,1,1,0," +
                        "0,1,0,1,1,0,1,0,0,1,0,0,1,1,1,0,1,1,1,1,1,1,1,1,0," +
                        "0,1,0,0,0,0,1,0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0," +
                        "0,1,1,1,1,0,1,0,0,1,1,0,1,1,1,1,1,0,0,0,0,0,0,1,0," +
                        "0,1,1,0,1,0,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0," +
                        "0,1,1,0,1,0,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,0,1,1,0," +
                        "0,1,1,0,1,1,1,0,1,0,0,0,0,0,1,1,1,0,1,1,1,0,1,1,0," +
                        "0,1,1,0,0,0,0,0,1,0,1,1,1,0,1,1,1,0,0,1,1,0,1,1,0," +
                        "0,1,0,0,1,1,1,1,1,0,1,0,0,0,1,1,1,1,0,0,1,0,0,1,0," +
                        "0,1,1,0,1,0,0,1,1,0,1,1,1,1,1,1,0,1,1,1,1,0,1,1,0," +
                        "0,1,1,0,1,1,0,0,1,0,0,0,0,0,0,1,0,1,0,1,1,0,1,1,0," +
                        "0,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,0," +
                        "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 
                        start, destination, difficulty);
                }
                catch (Exception e) {
                    System.out.println("Unable to load map (" + e + "). Exiting...");
                    System.exit(-1);
                }
            	
            	// Size of a field in the graphical view.
            	Dimension fieldDimensions = new Dimension(50, 50);
            	// Create and register graphical view.
            	GraphicView gview = new GraphicView(width, height, fieldDimensions, world.getMap(), destination, difficulty);
            	world.registerView(gview);
                gview.setVisible(true);
            	
            	// Create and register console view.
        		ConsoleView cview = new ConsoleView();
            	world.registerView(cview);
            	
            	// Create controller and initialize JFrame.
                Controller controller = new Controller(world);
                controller.setTitle("Labyrinth-Person");
                controller.setResizable(false);                
                controller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                controller.getContentPane().add(gview);
                // pack() is needed before JFrame size can be calculated.
                controller.pack();

                // Calculate size of window by size of insets (titlebar + border) and size of graphical view.
                Insets insets = controller.getInsets();
                
                int windowX = width * fieldDimensions.width + insets.left + insets.right;
                int windowY = height * fieldDimensions.height + insets.bottom + insets.top;
                Dimension size = new Dimension(windowX, windowY+50);
                controller.setSize(size);
                controller.setMinimumSize(size);
                controller.setVisible(true);
            }
        });
    }
}
