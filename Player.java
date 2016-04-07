package components;

//import pieces.components;

public class Player
{
	public String side;
	public Pieces pieces;

	public Player(String s) 
	{
		side = new String(s);
		pieces = new Pieces();
	}


	// checks if player's pieces are all set
	public boolean Check_All_Set()
	{

		if(pieces.set[Pieces.FLAG] == Pieces.MAX_FLAG)
		{
			if(pieces.set[Pieces.SPY] == Pieces.MAX_SPY)
			{
				if(pieces.set[Pieces.SCOUT] == Pieces.MAX_SCOUT)
				{
					if(pieces.set[Pieces.MINER] == Pieces.MAX_MINER)
					{
						if(pieces.set[Pieces.SERGEANT] == Pieces.MAX_SERGEANT)
						{
							if(pieces.set[Pieces.LIEUTENANT] == Pieces.MAX_LIEUTENANT)
							{
								if(pieces.set[Pieces.CAPTAIN] == Pieces.MAX_CAPTAIN)
								{
									if(pieces.set[Pieces.MAJOR] == Pieces.MAX_MAJOR)
									{
										if(pieces.set[Pieces.COLONEL] == Pieces.MAX_COLONEL)
										{
											if(pieces.set[Pieces.GENERAL] == Pieces.MAX_GENERAL)
											{
												if(pieces.set[Pieces.MARSHALL] == Pieces.MAX_MARSHALL)
												{
													if(pieces.set[Pieces.BOMB] == Pieces.MAX_BOMB)
													{
														return true;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}
}