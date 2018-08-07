import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HumanPlayer extends Player
{
    public HumanPlayer(String name)
    {
        super(name);
    }

    /**
     * Attack the specified Location loc.  Marks
     *   the attacked Location on the guess board
     *   with a positive number if the enemy Player
     *   controls a ship at the Location attacked;
     *   otherwise, if the enemy Player does not
     *   control a ship at the attacked Location,
     *   guess board is marked with a negative number.
     *
     * If the enemy Player controls a ship at the attacked
     *   Location, the ship must add the Location to its
     *   hits taken.  Then, if the ship has been sunk, it
     *   is removed from the enemy Player's list of ships.
     *
     * Return true if the attack resulted in a ship sinking;
     *   false otherwise.
     *
     * @param enemy
     * @param loc
     * @return
     */
    @Override
    public boolean attack(Player enemy, Location loc)
    {
        Ship EShip = enemy.getShip(loc);//gets ship
        if(EShip != null)
        {
            EShip.takeHit(loc);
            

            getGuessBoard()[loc.getRow()][loc.getCol()] = 1;
           
            if(EShip.isSunk()){
                enemy.removeShip(EShip);
                return true;//returns true if ship is sunk
            }
           
        }
        else
            getGuessBoard()[loc.getRow()][loc.getCol()] = -1;
        
        return false;
    }

    /**
     * Construct an instance of
     *
     *   AircraftCarrier,
     *   Destroyer,
     *   Submarine,
     *   Cruiser, and
     *   PatrolBoat
     *
     * and add them to this Player's list of ships.
     */
    @Override
    public void populateShips()//randomly populates ship
    {
        String[] directions = {"L", "R", "D", "U"};
        int[] lengths = {5, 4, 3, 3, 2};//ship lengths
        ArrayList<Location[]> locs = new ArrayList<Location[]>();
        
        for(int i = 0; i < lengths.length; i++){
            int j = locs.size();
            while(j == locs.size())
            {
            	Location[] addin = new Location[lengths[i]];
                String direct = directions[(int) (Math.random()*directions.length)];
                int vert = (int) (Math.random()*getGuessBoard().length);
                int hor = (int) (Math.random()*getGuessBoard()[0].length);
                
                addin = help(direct, lengths[i], vert, hor);
                if(addin != null)
                	locs.add(addin);
            }
        }
        
        addShip(new AircraftCarrier(locs.get(0)));
        addShip(new Destroyer(locs.get(1)));
        addShip(new Submarine(locs.get(2)));
        addShip(new Cruiser(locs.get(3)));
        addShip(new PatrolBoat(locs.get(4)));


        int[][] board = getGuessBoard() ;//resets guessBoard
        for(int i = 0; i < board.length; i++)
        	Arrays.fill(board[i], 0);
        
    }
    
    private Location[] help(String direct, int length, int vert,int hor){
        int Vadd = 0;
        int Hadd = 0;
        Location[] locs = new Location[length];
        int[][] board = getGuessBoard();

        if(direct.equals("L"))
            Hadd--;
        else if(direct.equals("R"))
            Hadd++;
        else if (direct.equals("U"))
            Vadd++;
        else
            Vadd--;
        
        for(int i = 0; i < locs.length; i++)
            if(vert > 0 && hor > 0 && vert < board.length && hor < board[vert].length && board[vert][hor] != 1){
                locs[i] = new Location(vert, hor);
                board[vert][hor] = 1;
                vert += Vadd;
                hor += Hadd;
            }
            else
                return null;
        
        return locs;
    }
}
