package view;

import model.World;

/**
 * A view that prints the current state of the world to the console upon every
 * update.
 */
public class ConsoleView implements View {

	@Override
	public void update(World world) {
		// The player's position
		int playerX = world.getPlayerX();
		int playerY = world.getPlayerY();
		int width = world.getWidth();

		// Clear screen then print labyrinth
		System.out.print("\033[H\033[2J");  
    	System.out.flush();
		for (int row = 0; row < world.getHeight(); row++) {
			for (int col = 0; col < world.getWidth(); col++) {
				// If the player is here, print #
				int tile = col + row * world.getWidth();
				if (row == playerY && col == playerX) {
					System.out.print("#");
				}
				else if (tile == world.getDestination()) {
					System.out.print("x");
				}
				else if (tile == world.getTracker(0).getX() + world.getTracker(0).getY() * width) {
					System.out.print("°");
				}
				else {
					switch(world.getMap().get(tile).getType()) {
						case 0:
							System.out.print("█");
							break;
						case 1:
							System.out.print(".");
							break;
						default:
							System.out.print("?");
							break;
					}
				}
			}

			// A newline after every row
			System.out.println();
		}

		// A newline between every update
		System.out.println();
	}

}
