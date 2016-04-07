package components;

// This class stores the fixed values of ranks of pieces
// It also stores the amount of pieces a player sets and 
// how many of their pieces are removed from the board.

public class Pieces
{
	// rank
	public static final int FLAG = 0;
	public static final int SPY = 1;
	public static final int SCOUT = 2;
	public static final int MINER = 3;
	public static final int SERGEANT = 4;
	public static final int LIEUTENANT = 5;
	public static final int CAPTAIN = 6;
	public static final int MAJOR = 7;
	public static final int COLONEL = 8;
	public static final int GENERAL = 9;
	public static final int MARSHALL = 10;
	public static final int BOMB = 11;


	// max allowed on board for 1 player
	public static final int MAX_FLAG = 1;
	public static final int MAX_SPY = 1;
	public static final int MAX_SCOUT = 8;
	public static final int MAX_MINER = 5;
	public static final int MAX_SERGEANT = 4;
	public static final int MAX_LIEUTENANT = 4;
	public static final int MAX_CAPTAIN = 4;
	public static final int MAX_MAJOR = 3;
	public static final int MAX_COLONEL = 2;
	public static final int MAX_GENERAL = 1;
	public static final int MAX_MARSHALL = 1;
	public static final int MAX_BOMB = 6;


	// stores array of set and removed pieces
	public int[] set;
	public int[] removed;

	public Pieces()
	{
		int i; 

		set = new int[12];
		removed = new int[12];

		for(i = 0; i < 12; i++)
		{
			set[i] = 0;
			removed[i] = 0; 
		}
	}

	//subtracts an available piece for a play to set on the board
	// since it is being set now
	public void Sub_Piece_Set(int slot)
	{
		switch (slot)
		{
			case FLAG:	set[FLAG]++;
						break;
			case SPY:	set[SPY]++;
						break;
			case SCOUT:	set[SCOUT]++;
						break;
			case MINER:	set[MINER]++;
						break;
			case SERGEANT:	set[SERGEANT]++;
							break;
			case LIEUTENANT:	set[LIEUTENANT]++;
								break;
			case CAPTAIN:	set[CAPTAIN]++;
							break;
			case MAJOR:	set[MAJOR]++;
						break;
			case COLONEL:	set[COLONEL]++;
							break;
			case GENERAL:	set[GENERAL]++;
							break;
			case MARSHALL:	set[MARSHALL]++;
						break;
			case BOMB:	set[BOMB]++;
						break;
		}
	}


	// increment player's piece removed
	public void Add_Piece_Removed(int slot)
	{
		switch (slot)
		{
			case FLAG:	removed[FLAG]++;
						break;
			case SPY:	removed[SPY]++;
						break;
			case SCOUT:	removed[SCOUT]++;
						break;
			case MINER:	removed[MINER]++;
						break;
			case SERGEANT:	removed[SERGEANT]++;
							break;
			case LIEUTENANT:	removed[LIEUTENANT]++;
								break;
			case CAPTAIN:	removed[CAPTAIN]++;
							break;
			case MAJOR:	removed[MAJOR]++;
						break;
			case COLONEL:	removed[COLONEL]++;
							break;
			case GENERAL:	removed[GENERAL]++;
							break;
			case MARSHALL:	removed[MARSHALL]++;
						break;
			case BOMB:	removed[BOMB]++;
						break;
		}
	}

	// reset player pieces upon restart of game
	public void Reset_Pieces()
	{
		int i;

		for(i = 0; i < 12; i++)
		{
			set[i] = 0;
			removed[i] = 0; 
		}
	}

}