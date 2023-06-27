# Labyrinth-Person
`Note that the key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT", "SHOULD”, "SHOULD NOT", "RECOMMENDED", "MAY", and "OPTIONAL" are to be interpreted as described in RFC 2119.`

## About this project
A game that requires the player to reach a destination without getting caught by one of the trackers.

## Getting started
To run the application simply compile the project and run the `Labyrinth` file (source `./controller/Labyrinth.java`).

### Important
`./assets/assets_2.jar` SHOULD be added to the referenced libraries before attempting to compile the code as it contains the textures referenced in `GrahicView.java`.

## Controls:
### Movement:
Arrow keys

### Restart level:
`R`

### Increase difficulty:
`E`

### Lower difficulty:
`Q`

## Tile:
Tiles are used to store information about each of the map tiles and make the pathfinding a whole lot easier. For more info see ./model/Tile.java

## Tracker:
A tracker is an adversery that tracks the player and tries to eliminate them. They are essentially a computer controlled player.

## Pathfinding:
A slightly tweaked A* algorithm is used to find the "shortest" path from tracker to player.
