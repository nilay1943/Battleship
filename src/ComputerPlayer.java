

	import com.sun.org.apache.xpath.internal.SourceTree;
	import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.Collections;
	import java.util.Random;
import java.util.concurrent.SynchronousQueue;
public class ComputerPlayer extends Player{
		private int[][] Fboard, ACboard, Sboard, Dboard, Cboard, Pboard;
		private boolean hunt;
		private boolean second;
		private Location lastHit;
		private Location hitB4;
		private Location last;
	    private Ship target;
	    private ArrayList<Location> next;
		
		public ComputerPlayer(String name)
	    {
	        super(name);
	        hunt = true;
	        second = false;
	        next = new ArrayList<Location>();
	        lastHit = new Location(0, 0);
	        hitB4 = new Location(0,0);
	        Fboard = new int[10][10];
			ACboard = new int[10][10];
			Sboard = new int[10][10];
			Dboard = new int[10][10];
			Cboard = new int[10][10];
			Pboard = new int[10][10];
			
			//ACBoard
			setBoards(ACboard, 5);
			
			//Dboard
			setBoards(Dboard, 4);
			
			//Sboard
			setBoards(Sboard, 3);
			
			//Cboard
			setBoards(Cboard, 3);

			//PBoard
			setBoards(Pboard, 2);

			//Fboard
			setF();


	    }
		
		private void setF()
		{
			for(int vert = 0; vert < Fboard.length; vert++)
				for(int hor = 0; hor < Fboard[vert].length; hor++)
					Fboard[vert][hor] = Sboard[vert][hor] + Dboard[vert][hor] + Sboard[vert][hor] + Cboard[vert][hor] + Pboard[vert][hor];
		}
		private void setBoards(int[][] board, int len)
		{
			for(int[] bo: board)
				Arrays.fill(bo, 0);
			for(int vert = 0; vert < board.length; vert++)
				for(int hor = 0; hor < board[vert].length; hor++)
				{
					ArrayList<Location> spots = new ArrayList<Location>();
					if(hor <= board[vert].length-len)
					{
						for (int temp = hor; temp < hor + len; temp++)
							if(getGuessBoard()[vert][temp] == 0)
								spots.add(new Location(vert, temp));
							else {
								spots = new ArrayList<Location>();
								break;
							}
						for(Location loc : spots)
							board[loc.getRow()][loc.getCol()]++;
					}

					spots = new ArrayList<Location>();
					if(vert <= board.length-len)
					{
						for (int temp = vert; temp < vert + len; temp++)
							if(getGuessBoard()[temp][hor] == 0)
								spots.add(new Location(temp, hor));
							else {
								spots = new ArrayList<Location>();
								break;
							}
						for(Location loc : spots)
							board[loc.getRow()][loc.getCol()]++;
					}
				}
			for(int vert = 0; vert < board.length; vert++)
				for(int hor = 0; hor < board[vert].length; hor++)
					board[vert][hor] = (int) Math.pow(board[vert][hor], 6);
			System.out.print("Boat: " + len);
			for(int vert = 0; vert < board.length; vert++)
				System.out.println(Arrays.toString(board[vert]));
			System.out.println();

		}
	    /**
	     * Randomly chooses a Location that has not been
	     *   attacked (Location loc is ignored).  Marks
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
	     * @param enemy The Player to attack.
	     * @param loc The Location to attack.
	     * @return
	     */
	    @Override
	    public boolean attack(Player enemy, Location loc)
	    {
	        Location pick = hunt();
	        
	        if(!hunt)
	        	pick = target();
			else
				while(pick == null)
					pick = hunt();

	        
	        Ship EShip = enemy.getShip(pick);
	        
	        
	        if(EShip != null)
	        {
	            if(hunt){
	        	    hitB4 = new Location(pick.getRow(), pick.getCol());
	        	    hunt = false;
	        	    target = EShip;
	            }
	            else
	            	second = true;
	            
	            if(EShip.getClass().toString().equals(target.getClass().toString())){
	            	last = new Location(lastHit.getRow(), lastHit.getCol());
	            	lastHit = new Location(pick.getRow(), pick.getCol());
	            }
	            else
	            	next.add(pick);
	            
	            		
	            EShip.takeHit(pick);
	            
	            Fboard[pick.getRow()][pick.getCol()] = 0;
	            getGuessBoard()[pick.getRow()][pick.getCol()] = 1;

	            if(EShip.isSunk()){
	                enemy.removeShip(EShip);

	                hunt = true;
	                second = false;
	                last = new Location(0,0);
	                lastHit = new Location(0,0);


	                if(!next.isEmpty()){
	                	last = new Location(0,0);
		            	lastHit = next.get(0);
		            	next.remove(0);
		            	target = enemy.getShip(lastHit);
		            	hitB4 = new Location(lastHit.getRow(), lastHit.getCol());
		            	hunt = false;
		            }

					sunk(EShip);
					reset();

					return true;
	            }
	        }
	        else
			{
	            getGuessBoard()[pick.getRow()][pick.getCol()] = -1;
	            Fboard[pick.getRow()][pick.getCol()] = 0;
	        }
			reset();
			return false;
	    }

		private void reset()
		{
			//ACBoard
			if(!Arrays.deepEquals(ACboard, new int[10][10]))
			setBoards(ACboard, 5);

			//Dboard
			if(!Arrays.deepEquals(Dboard, new int[10][10]))
			setBoards(Dboard, 4);

			//Sboard
			if(!Arrays.deepEquals(Sboard, new int[10][10]))
			setBoards(Sboard, 3);

			//Cboard
			if(!Arrays.deepEquals(Cboard, new int[10][10]))
			setBoards(Cboard, 3);

			//Pboard
			if(!Arrays.deepEquals(Pboard, new int[10][10]))
				setBoards(Pboard, 2);

			setF();
		}

		private Ship sunk(Ship e)
		{

			int[][] remove = new int[10][10];
			if(e.getClass().toString().contains("A"))
				remove = ACboard;
			else if(e.getClass().toString().contains("D"))
				remove = Dboard;
			else if(e.getClass().toString().contains("C"))
				remove = Cboard;
			else if(e.getClass().toString().contains("S"))
				remove = Sboard;
			else if(e.getClass().toString().contains("P"))
				remove = Pboard;

			for(int vert = 0; vert < remove.length; vert++)
				Arrays.fill(remove[vert], 0);
			setF();
			return e;
		}


		private Location target(){
	    	Location up = null;
	    	Location down = null;
	    	Location left = null;
	    	Location right = null;
	    	int[][] board = this.getGuessBoard();

	    		up = new Location(lastHit.getRow()-1, lastHit.getCol());
	    		down = new Location(lastHit.getRow()+1, lastHit.getCol());
	    		left = new Location(lastHit.getRow(), lastHit.getCol()-1);
	    		right = new Location(lastHit.getRow(), lastHit.getCol()+1);
	    	
	    		int row = lastHit.getRow()-last.getRow();
	    		int col = lastHit.getCol()-last.getCol();
	    	
	    	if(second){
	    		if(up.getRow() == lastHit.getRow()+row && up.getCol() == lastHit.getCol()+col)
	    		{
	    			if(up.getRow() >= 0 && board[up.getRow()][up.getCol()] == 0)
	    				return up;
	    			lastHit = new Location(hitB4.getRow(), hitB4.getCol());
	    			last = new Location(hitB4.getRow()+row, hitB4.getCol()+col);
	    			return target();
	    		}
	    		else if(down.getRow() == lastHit.getRow()+row && down.getCol() == lastHit.getCol()+col)
    			{
    				if(down.getRow() < board.length && board[down.getRow()][down.getCol()] == 0)
    					return down;
    				lastHit = new Location(hitB4.getRow(), hitB4.getCol());
    				last = new Location(hitB4.getRow()+row, hitB4.getCol()+col);
    				return target();
    			}
	    		else if(left.getCol() == lastHit.getCol()+col && left.getRow() == lastHit.getRow()+row)
    			{
    				if(left.getCol() >= 0 && board[left.getRow()][left.getCol()] == 0)
    					return left;
    				lastHit = new Location(hitB4.getRow(), hitB4.getCol());
    				last = new Location(hitB4.getRow()+row, hitB4.getCol()+col);
    				return target();
    			}
	    		else if(right.getCol() == lastHit.getCol()+col && right.getRow() == lastHit.getRow()+row)
    			{
    				if(right.getCol() < board.length && board[right.getRow()][right.getCol()] == 0)
    					return right;
    				lastHit = new Location(hitB4.getRow(), hitB4.getCol());
    				last = new Location(hitB4.getRow()+row, hitB4.getCol()+col);
    				return target();
    			}
	    	}
	    	Location[] outputs = {up, down, left, right};
	    	Location output = outputs[(int)(Math.random()*outputs.length)];
	    	
	    	while(output.getRow() < 0 || output.getRow() >= board.length ||
	    			output.getCol() < 0 || output.getCol() >= board.length
	    				|| board[output.getRow()][output.getCol()] != 0)
	    		output = outputs[(int)(Math.random()*outputs.length)];

	        return output;
	    	
	    	
	    }
	    
	    private Location hunt(){
	    	
	    	ArrayList<Integer> numbears = new ArrayList<Integer>();
	    	
	    	for(int vert = 0; vert < Fboard.length; vert++)
	    		for(int hor = 0; hor < Fboard[vert].length; hor++)
	    			if((vert + hor)%2 != 0 && !numbears.contains(Fboard[vert][hor]) && getGuessBoard()[vert][hor] == 0)
	    				numbears.add(Fboard[vert][hor]);

	    	
	    	ArrayList<Integer> chance = new ArrayList<Integer>();
	    	 
	    	for(int i = 0; i < numbears.size(); i++)
	    		for(int rep = 0; rep <= numbears.get(i); rep++)
	    			chance.add(numbears.get(i));
	    	
	    	
	    	Collections.shuffle(chance);
	    	ArrayList<Location> rand = new ArrayList<Location>();
	    	do{
	            int number = chance.get((int) (Math.random()*chance.size()));
	    	
	    	
	    	
	    		for(int vert = 0; vert < Fboard.length; vert++)
	    			for(int hor = 0; hor < Fboard[vert].length; hor++)
	    				if((vert + hor)%2 != 0 && Fboard[vert][hor] == number && getGuessBoard()[vert][hor] == 0)
	    					rand.add(new Location(vert, hor));
	    	}
	    	while(rand.size() == 0);
	    	
	    	Location output = rand.get((int)(Math.random()*rand.size()));
	    	return output;
	    	
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
    public void populateShips()
    {
        String[] directions = {"L", "R", "D", "U"};
        int[] lengths = {5, 4, 3, 3, 2};//lengths of boats
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

        int[][] board = getGuessBoard() ;//resets guess board
        for(int i = 0; i < board.length; i++)
        	Arrays.fill(board[i], 0);
        
    }
    
    private Location[] help(String direct, int length, int vert,int hor){//used to randomly get available locations in a row(or col)
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
