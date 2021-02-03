package dk.dtu.compute.se.pisd.roborally.model;


/**
 * This class is where the creation of game model objects can be found.
 * Furthermore, most of the game logic and actions is taking place in this package.
 *
 * @class Board: Creates the board object (size of the board) and keeps track of numbers of players and player turn.
 * @class player: Player instantiation method, which creates a player with a specific name and color
 *   as well as direction and placing on the board.
 * @class space: Space indicates the field on the board which a specific player occupies
 * @Heading: Keeps track of which way the peice is heading. You can't turn 180 degrees (north to south) which means you can only turn 90 degrees at a time.
 * @Command: Command is how you want to move the peice.
 * @since 1.0
 */
