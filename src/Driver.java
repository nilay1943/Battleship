
public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] board = new int[10][10];
		
		for(int vert = 0; vert < board.length; vert++)
			for(int hor = 0; hor < board[vert].length/2; hor++)
			{
				board[vert][hor] = hor+1;
				board[vert][board[0].length - hor-1] =  board[vert][hor];
			}
		
		for(int vert = 0; vert < board.length/2; vert++)
			for(int hor = 0; hor < board[vert].length; hor++)
			{
				board[vert][hor] += vert+1;
				board[board.length-1 - vert][hor] +=  vert +1;
			}
		
		for(int vert = 0; vert < board.length; vert++){
			for(int hor = 0; hor < board[vert].length; hor++)
				System.out.print(board[vert][hor] + " ");
			System.out.println();
		}
	}

}
