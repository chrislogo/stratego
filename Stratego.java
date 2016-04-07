import java.awt.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import java.util.*;

import components.Player;
import components.Pieces;

// instructions used from: http://www.hasbro.com/common/instruct/Stratego_Star_Wars.PDF

public class Stratego
{
	private static Vector<JButton> board;			// 100 buttons
	private static Vector<JButton> light_side;		// 12 buttons
	private static Vector<JButton> dark_side;		// 12 buttons

	private static int[] board_values;		// contains the values of pieces on the board
	private static String[] board_sides;	// contains the side of pieces on the board

	public static Player player1 = new Player("Light Side");		//init player1
	public static Player player2 = new Player("Dark Side");		//init player2


	// All icons
	private static Image ds0;
	private static Image ds1;
	private static Image ds2;
	private static Image ds3;
	private static Image ds4;
	private static Image ds5;
	private static Image ds6;
	private static Image ds7;
	private static Image ds8;
	private static Image ds9;
	private static Image ds10;
	
	private static Image ls0;
	private static Image ls1;
	private static Image ls2;
	private static Image ls3;
	private static Image ls4;
	private static Image ls5;
	private static Image ls6;
	private static Image ls7;
	private static Image ls8;
	private static Image ls9;
	private static Image ls10;

	private static Image bomb;

	private static Image hide;

	private static int turn;
	private static boolean been_set;

	public static void main(String args[]) throws IOException, FileNotFoundException
    {
      	// create frame 
      	board = new Vector<JButton>();
      	light_side = new Vector<JButton>();
      	dark_side = new Vector<JButton> ();

      	board_values = new int[100];
      	board_sides = new String[100];

      	been_set = false;

      	for(int i = 0; i < 100; i++)
      	{
      		// an inital value for a "Free space"
      		board_values[i] = -1;

      		board_sides[i] = "";

      	}

      	StrategoFrame stratFrame = new StrategoFrame(); 
      	stratFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		stratFrame.setSize(1400, 850); // set frame size
      	stratFrame.setVisible(true); // display frame
    } 

	private static class StrategoFrame extends JFrame 
	{
		//private GridBagLayout layout; // layout of this frame
   		private GridBagConstraints gbc; // constraints of this layout

   		private JLabel p1_turn;
	    private JLabel p2_turn;
	    private JLabel dis_win;	//display winner
	    private JLabel idle;	
	    private JLabel set1;
	    private JLabel set2;

	    private JButton but_start;
	    private JButton but_restart;

		public StrategoFrame() throws IOException, FileNotFoundException
		{
			// frame title
			super("Star Wars Stratego");

			// prompt for player1 to choose which side they want
			Object[] options = {"Light Side", "Dark Side"};
			int n = JOptionPane.showOptionDialog(this,
				"\tPlayer 1, will you fight for the Light or Dark Side?",
			    "\tChoose Your Side!",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.PLAIN_MESSAGE,
			    null,
			    options,     
			    options[0]); 

			//set player sides from player 1's input
			if(n == 0)
			{
				player1 = new Player("Light Side");
				player2 = new Player("Dark Side");
			}
			else
			{
				player1 = new Player("Dark Side");
				player2 = new Player("Light Side");
			}

			// event controllers for buttons
			LastClicked lc = new LastClicked();
			BoardListener bl = new BoardListener();

			//start with player 1 setting pieces
			turn = 1;
   			
			setLayout(new BorderLayout());
	        gbc = new GridBagConstraints();
	        gbc.insets = new Insets(10, 0, 5, 0);

	        // establish all icon pictures
	        Set_Icons();

	        // set title and top bottons (north)
	        Set_Top();

	        // set dark side (west)
	        Set_Dark(lc);
	       

	       // set light side (east)
	        Set_Light(lc);

	        // set bottom (south)
	        Set_Bottom();

	        // set center
	        Set_Center(bl);
		}


		// establish all icons
		public void Set_Icons() throws IOException, FileNotFoundException
		{
			ds0 = ImageIO.read(getClass().getResource("/side_pics/dssaber.jpg"));
			ds1 = ImageIO.read(getClass().getResource("/side_pics/ds.jpg"));
			ds2 = ImageIO.read(getClass().getResource("/side_pics/ds2.jpg"));
			ds3 = ImageIO.read(getClass().getResource("/side_pics/ds3.jpg"));
			ds4 = ImageIO.read(getClass().getResource("/side_pics/ds4.jpg"));
			ds5 = ImageIO.read(getClass().getResource("/side_pics/ds5.jpg"));
			ds6 = ImageIO.read(getClass().getResource("/side_pics/ds6.jpg"));
			ds7 = ImageIO.read(getClass().getResource("/side_pics/ds7.jpg"));
			ds8 = ImageIO.read(getClass().getResource("/side_pics/ds8.jpg"));
			ds9 = ImageIO.read(getClass().getResource("/side_pics/ds9.jpg"));
			ds10 = ImageIO.read(getClass().getResource("/side_pics/ds10.jpg"));

			ls0 = ImageIO.read(getClass().getResource("/side_pics/lssaber.jpg"));
			ls1 = ImageIO.read(getClass().getResource("/side_pics/ls.jpg"));
			ls2 = ImageIO.read(getClass().getResource("/side_pics/ls2.jpg"));
			ls3 = ImageIO.read(getClass().getResource("/side_pics/ls3.jpg"));
			ls4 = ImageIO.read(getClass().getResource("/side_pics/ls4.jpg"));
			ls5 = ImageIO.read(getClass().getResource("/side_pics/ls5.jpg"));
			ls6 = ImageIO.read(getClass().getResource("/side_pics/ls6.jpg"));
			ls7 = ImageIO.read(getClass().getResource("/side_pics/ls7.jpg"));
			ls8 = ImageIO.read(getClass().getResource("/side_pics/ls8.jpg"));
			ls9 = ImageIO.read(getClass().getResource("/side_pics/ls9.jpg"));
			ls10 = ImageIO.read(getClass().getResource("/side_pics/ls10.jpg"));

			bomb = ImageIO.read(getClass().getResource("/side_pics/bomb.jpg"));

			hide = ImageIO.read(getClass().getResource("/side_pics/sw.jpg"));
		}

		// set the north part of the grid layout
		public void Set_Top()
		{
			// holds everything in the north grid
			JPanel top = new JPanel(new GridBagLayout());

			//title
			JLabel board_title = new JLabel("Star Wars Stratego");
			board_title.setFont(new Font("Serif", Font.BOLD, 60));
			board_title.setForeground(Color.YELLOW);

			// holds title
			JPanel title = new JPanel();
			title.setBackground(Color.BLACK);
	        title.add(board_title);
	     
	     	// holds buttons
			final JPanel start = new JPanel();
			
			//establish buttons
			but_start = new JButton("Start Game");
			but_restart = new JButton("Restart Game");
			JButton but_defeated = new JButton("Defeated Pieces");
			JButton but_view = new JButton("View Rules");	
			JButton but_how = new JButton("How To Get Started");

			but_start.setBackground(Color.GREEN);
			but_restart.setBackground(Color.ORANGE);
			but_defeated.setBackground(Color.CYAN);
			but_view.setBackground(Color.WHITE);
			but_how.setBackground(Color.PINK);


			// listener for start button
			// locks the outside buttons 
			but_start.addActionListener(new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e)
    			{
    				int i;
    				JButton temp;

    				been_set = true;
    				turn = 1;

    				// lock side buttons, can no longer set pieces
    				for(i = 0; i < 12; i++)
    				{
    					temp = light_side.elementAt(i);
    					temp.setEnabled(false);
    					light_side.set(i, temp);
    					temp = dark_side.elementAt(i);
    					temp.setEnabled(false);
    					dark_side.set(i, temp);

    				}
    				
    				//game started, lock start button, allow for resetitng
    				// prompt player1 it is their turn
    				but_start.setEnabled(false);
    				but_restart.setEnabled(true);
    				idle.setVisible(false);	
    				p1_turn.setVisible(true);
    			}
    		});


			
			// clears board and defeated pieces
			// unlocks outside buttons
			but_restart.addActionListener(new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e)
    			{
    				int i;
    				JButton temp = new JButton();

    				been_set = false;


    				for(i = 0; i < 12; i++)
    				{
    					temp = light_side.elementAt(i);
    					temp.setEnabled(true);
    					light_side.set(i, temp);
    					temp = dark_side.elementAt(i);
    					temp.setEnabled(true);
    					dark_side.set(i, temp);
    				}

    				for(i = 0; i < 100; i++)
    				{
       					temp = board.elementAt(i);

    					temp.setIcon(null);
    					temp.setSelected(false);
    					if(i != 42 || i != 43 || i != 52 || i != 53 || i != 46 || i != 47
	        					|| i != 56 || i != 57)
	        			{
    						temp.setEnabled(true);
    					}
    					else
    						temp.setEnabled(false);

    					board.set(i, temp);

    					board_values[i] = -1;
    					board_sides[i] = "";

    				}

    				for(i = 0; i < 12; i++)
    				{
    					player1.pieces.set[i] = 0;
    					player2.pieces.set[i] = 0;

    					player1.pieces.removed[i] = 0;
    					player2.pieces.removed[i] = 0;
    				}

    				//reset to beginning state
    				dis_win.setVisible(false);
    				set1.setVisible(true);
    				p1_turn.setVisible(false);
    				p2_turn.setVisible(false);

    				but_start.setEnabled(false);
    				but_restart.setEnabled(false);

    				turn = 1;
    			}
    		});

			
			// displays defeated pieces for both teams
			but_defeated.addActionListener(new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e)
    			{
    				String def = "            " + "Player 1" + "             " 
    						+ "Player 2\n\n" 
    						+ "F   :        " + player1.pieces.removed[Pieces.FLAG] + " of " + Pieces.MAX_FLAG 
    						+ "                   " + player2.pieces.removed[Pieces.FLAG] + " of " + Pieces.MAX_FLAG + "\n"
    						+ "B   :        " + player1.pieces.removed[Pieces.BOMB] + " of " + Pieces.MAX_BOMB  
    						+ "                   " + player2.pieces.removed[Pieces.BOMB] + " of " + Pieces.MAX_BOMB + "\n"
    						+ "S   :        " + player1.pieces.removed[Pieces.SPY] + " of " + Pieces.MAX_SPY   
    						+ "                   " + player2.pieces.removed[Pieces.SPY] + " of " + Pieces.MAX_SPY + "\n"
    						+ "2   :        " + player1.pieces.removed[Pieces.SCOUT] + " of " + Pieces.MAX_SCOUT  
    						+ "                   " + player2.pieces.removed[Pieces.SCOUT] + " of " + Pieces.MAX_SCOUT + "\n"
    						+ "3   :        " + player1.pieces.removed[Pieces.MINER] + " of " + Pieces.MAX_MINER 
    						+ "                   " + player2.pieces.removed[Pieces.MINER] + " of " + Pieces.MAX_MINER + "\n"
    						+ "4   :        " + player1.pieces.removed[Pieces.SERGEANT] + " of " + Pieces.MAX_SERGEANT 
    						+ "                   " + player2.pieces.removed[Pieces.SERGEANT] + " of " + Pieces.MAX_SERGEANT + "\n"
    						+ "5   :        " + player1.pieces.removed[Pieces.LIEUTENANT] + " of " + Pieces.MAX_LIEUTENANT 
    						+ "                   " + player2.pieces.removed[Pieces.LIEUTENANT] + " of " + Pieces.MAX_LIEUTENANT + "\n"
    						+ "6   :        " + player1.pieces.removed[Pieces.CAPTAIN] + " of " + Pieces.MAX_CAPTAIN 
    						+ "                   " + player2.pieces.removed[Pieces.CAPTAIN] + " of " + Pieces.MAX_CAPTAIN + "\n"
    						+ "7   :        " + player1.pieces.removed[Pieces.MAJOR] + " of " + Pieces.MAX_MAJOR 
    						+ "                   " + player2.pieces.removed[Pieces.MAJOR] + " of " + Pieces.MAX_MAJOR + "\n"
    						+ "8   :        " + player1.pieces.removed[Pieces.COLONEL] + " of " + Pieces.MAX_COLONEL  
    						+ "                   " + player2.pieces.removed[Pieces.COLONEL] + " of " + Pieces.MAX_COLONEL + "\n"
    						+ "9   :        " + player1.pieces.removed[Pieces.GENERAL] + " of " + Pieces.MAX_GENERAL 
    						+ "                   " + player2.pieces.removed[Pieces.GENERAL] + " of " + Pieces.MAX_GENERAL + "\n"
    						+ "10 :        " + player1.pieces.removed[Pieces.MARSHALL] + " of " + Pieces.MAX_MARSHALL 
    						+ "                   " + player2.pieces.removed[Pieces.MARSHALL] + " of " + Pieces.MAX_MARSHALL + "\n";


			    	Object[] opt = {"Ok"};
					int n = JOptionPane.showOptionDialog(start,
						def,
					    "Defeated Pieces",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.PLAIN_MESSAGE,
					    null,
					    opt,     
					    opt[0]); 
			    }
    		});

			
			
			// display how to play the game
			but_view.addActionListener(new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e)
    			{
    				Object[] opt = {"OK"};

			      	String rules_txt = "How To Play:\n" + "Each game is between two armies (the light side and dark side). Pick a side!\n"
			      	+ "Each army has 40 total pieces, 33 of which are army pieces ranked 1-10 (1(S) is the lowest rank and 10 is the highest rank).\n"
			      	+ "Each army also gets 6 Bombs and 1 Light Saber(the flag) but these pieces do not move once the game has started.\n"
			      	+ "Only army pieces may move or attack. The army includes:\n"
			      	+ "                                               Light Side"
			      	+ "                                 Dark Side\n"
			      	+ "10 :  1 Marshall                        Yoda                                   The Emperor\n" 
			      	+ "9  :  1 General                	 Luke Sykwalker                          Darth Vader\n"
			      	+ "8  :  2 Colonels                Obi-Wan Kenobi                         Darth Maul\n"
			      	+ "7  :  3 Majors                      Chewbacca                                Boba Fett\n"
			      	+ "6  :  4 Captains                  Qui-Gon Jinn                              Jango Fett\n"
			      	+ "5  :  4 Lieutenants           Padme Amidala                      Jabba the Hutt\n" 
			      	+ "4  :  4 Sergeants                     Pilot                                      Droideka\n" 
			      	+ "3  :  5 Miners                          R2-D2                                         Droid\n"
			      	+ "2  :  8 Scouts                          Rebel                                  Storm Trooper\n" 
			      	+ "S  :  1 Spy                         The Light Side                          The Dark Side\n"
			      	+ "B  :  6 Bombs\n\n"
			      	+ "-You may only either move or attack an opponent’s piece and you may not do both.\n"
			      	+ "-If you find yourself in a position where you cannot move or attack, then you have lost the game.\n"
			      	+ "-In the game you take turns moving pieces in vertical or horizontal directions. Pieces may not move in a diagonal direction.\n"
			      	+ "-Pieces may only move one space, except for the Scouts(2) which can move an unlimited distance.\n"
			      	+ "-Moving a Scout(2) more than one space will reveal its identity to your opponent.\n"
					+ "-Pieces cannot jump over the blacked out spaces or over other pieces. They also may not occupy the same space as another piece.\n"
					+ "-If you land on a space where an enemy piece is already located then the higher ranking piece will remain in that space and the other will be removed from the board.\n"
					+ "-If they are the same rank, then both pieces are removed.\n\n"
					+ "The Objective:\n"
					+ "Attack your opponent’s pieces to reduce his or her numbers and capture your opponent’s Flag.\n"
					+ "You may only attack pieces that are directly next to your pieces. They may not be a space away or diagonal to one of your pieces.\n\n"
					+ "Special Notes:\n"
					+ "- The Spy(S) can defeat the Marshall(10) if the Spy(S) attacks first.\n"
					+ "- If the Spy(S) is attacked by the Marshall(10), then the Spy(S) is defeated.\n"
					+ "- Scouts(2) can move and attack on the same turn. No other pieces can do both in the same turn.\n"
					+ "- Miners(3) can disarm Bomb(B) pieces. All other pieces must be removed from the board if they attack a Bomb(B) piece.\n"
					+ "- Bombs(B) and Light Sabers(F) cannot be moved once game has begun.\n"
			      	;


			      	int n = JOptionPane.showOptionDialog(start,
						rules_txt,
					    "Rules",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.INFORMATION_MESSAGE,
					    null,     //do not use a custom Icon
					    opt,  //the titles of buttons
					    opt[0]); //default button title
    			}
    		});

			
			
			// display how to use the GUI interface
			but_how.addActionListener(new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e)
    			{
    				Object[] opt = {"OK"};

			      	String rules_txt = "Directions:\n\n"
			      		+ "First, Player 1 sets their pieces and then Player 2 may set their pieces.\n"
			      		+ "A player that chooses the Dark side may only place their pieces on the red tiles to begin.\n"
			      		+ "A player that chooses the Light Side may only place their pieces on the blue tiles.\n"
			      		+ "To set a piece, click the icon of the side you are are on and then select the tile of the board you wish to set it on.\n"
			      		+ "When a side icon is selected, you may continuously set that piece until the max amount for that piece is set or\n"
			      		+ "you may choose another icon to begin setting that piece.\n"
			      		+ "When all of the pieces are set, the players may begin the game (Refer to View Rules if unsure how to play stratego).\n"
			      		+ "Once the game has begun, players alternate taking turns and may only make the valid moves described in the Rules.\n"
			      		+ "To move a piece, click the tile it is on and then click the destination tile (keep in mind it must be a valid movement).\n"
			      		+ "Players can view the defeated pieces by pressing the Defeated Pieces button.\n"
			      		+ "When a play cannot make another move or their flag is captured, the game is over and the opposing play wins.\n"
			      		+ "To play again just press Restart Game and begin setting pieces again."
			      	;


			      	int n = JOptionPane.showOptionDialog(start,
						rules_txt,
					    "How To Get Started",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.INFORMATION_MESSAGE,
					    null,     //do not use a custom Icon
					    opt,  //the titles of buttons
					    opt[0]); //default button title
    			}
    		});

	
			//so players can prematurely start the game
			but_start.setEnabled(false);
			but_restart.setEnabled(false);

			// add all the buttons to one panel
    		start.setLayout(new GridLayout(1, 4, 0, 0));
			start.add(but_start);
			start.add(but_restart);
			start.add(but_defeated);
			start.add(but_view);
			start.add(but_how);

			// set the background to black
			start.setBackground(Color.BLACK);
			start.setForeground(Color.YELLOW);


			// add title
			gbc.gridx = 0;
    		gbc.gridy = 0;
			top.add(title, gbc);
			
			// add buttons
			gbc.gridx = 0;
    		gbc.gridy = 1;
    		top.add(start, gbc);
    	
    		// set size and add to main frame
    		top.setPreferredSize(new Dimension(0, 150));
			top.setBackground(Color.BLACK);
	        add(top, BorderLayout.NORTH);
		}


		// set the west part of the grid layout
		public void Set_Dark(LastClicked lc)
		{
	        JPanel team = new JPanel();
	        JPanel left = new JPanel(new GridBagLayout());
	        JLabel board_title = new JLabel("Dark Side");
	        JPanel title = new JPanel();

	       	team.setLayout(new GridLayout(6, 2, 0, 0));
			
			for(int i = 0; i < 12; i++)
			{
				JButton button = new JButton();

				button.addActionListener(lc);

				if (i == 0)
				{
    				button.setIcon(new ImageIcon(ds0));
				}
				else if(i == 1)
				{
    				button.setIcon(new ImageIcon(ds1));
				}
				else if (i == 2)
				{
    				button.setIcon(new ImageIcon(ds2));
				}
				else if (i == 3)
				{
    				button.setIcon(new ImageIcon(ds3));
				}
				else if (i == 4)
				{
    				button.setIcon(new ImageIcon(ds4));
				}
				else if (i == 5)
				{
    				button.setIcon(new ImageIcon(ds5));
				}
				else if(i == 6)
				{
    				button.setIcon(new ImageIcon(ds6));
				}
				else if (i == 7)
				{
    				button.setIcon(new ImageIcon(ds7));
				}
				else if(i == 8)
				{
    				button.setIcon(new ImageIcon(ds8));
				}
				else if (i == 9)
				{
    				button.setIcon(new ImageIcon(ds9));
				}
				else if (i == 10)
				{
    				button.setIcon(new ImageIcon(ds10));
				}
				else if (i == 11)
				{
    				button.setIcon(new ImageIcon(bomb));
				}

				button.setBackground(Color.RED);
    			button.setPreferredSize(new Dimension(55, 55));
    			dark_side.add(button);

    			team.add(dark_side.elementAt(i));
			}

			//set the team view background
			team.setBackground(Color.RED);
			team.setForeground(Color.BLACK);
			
			// set and add title
			board_title.setFont(new Font("Serif", Font.BOLD, 30));
			board_title.setForeground(Color.BLACK);
			title.setBackground(Color.RED);
			title.add(board_title);

			// add title
			gbc.gridx = 0;
    		gbc.gridy = 0;
			left.add(title, gbc);

			// add team buttons
			gbc.gridx = 0;
    		gbc.gridy = 1;
			left.add(team, gbc);

			left.setPreferredSize(new Dimension(230, 0));

			left.setBackground(Color.RED);
	        add(left, BorderLayout.WEST);
		}

		// set the east part of the grid layout
		public void Set_Light(LastClicked lc)
		{
			gbc.insets = new Insets(0, 0, 0, 0);

	        JPanel team = new JPanel();
	        JPanel title = new JPanel();
	        JLabel board_title = new JLabel("Light Side");
	        JPanel right = new JPanel(new GridBagLayout());

	        team.setLayout(new GridLayout(6,2,0,0));
			
			
			//team buttons
			for(int i = 0; i < 12; i++)
			{
				final JButton button = new JButton();

				button.addActionListener(lc);

				if(i == 0)
				{
    				button.setIcon(new ImageIcon(ls0));
				}
				else if (i == 1)
				{
    				button.setIcon(new ImageIcon(ls1));
				}
				else if (i == 2)
				{
    				button.setIcon(new ImageIcon(ls2));
				}
				else if(i == 3)
				{
    				button.setIcon(new ImageIcon(ls3));
				}
				else if (i == 4)
				{
    				button.setIcon(new ImageIcon(ls4));
				}
				else if (i == 5)
				{
    				button.setIcon(new ImageIcon(ls5));
				}
				else if(i == 6)
				{
    				button.setIcon(new ImageIcon(ls6));
				}
				else if (i == 7)
				{
    				button.setIcon(new ImageIcon(ls7));
				}
				else if (i == 8)
				{
    				button.setIcon(new ImageIcon(ls8));
				}
				else if (i == 9)
				{
    				button.setIcon(new ImageIcon(ls9));
				}
				else if (i == 10)
				{
    				button.setIcon(new ImageIcon(ls10));
				}
				else if (i == 11)
				{
    				button.setIcon(new ImageIcon(bomb));
				}

				button.setBackground(Color.BLUE);
    			button.setPreferredSize(new Dimension(55, 55));
    			light_side.add(button);

    			team.add(light_side.elementAt(i));
			}

			team.setBackground(Color.BLUE);
			team.setForeground(Color.BLACK);


			// light side title
			board_title.setFont(new Font("Serif", Font.BOLD, 30));
			title.setBackground(Color.BLUE);
			board_title.setForeground(Color.BLACK);
			title.add(board_title);

			
			// add title
    		gbc.gridx = 0;
    		gbc.gridy = 0;
    		right.add(title, gbc);

    		// add team buttons
    		gbc.gridx = 0;
    		gbc.gridy = 1;
			right.add(team, gbc);


			right.setBackground(Color.BLUE);

			right.setPreferredSize(new Dimension(220, 0));

	        add(right, BorderLayout.EAST);
		}

		// set the south part of the grid layout
		public void Set_Bottom()
		{
	        gbc.insets = new Insets(0, 85, 0, 0);

	        JPanel bottom = new JPanel(new GridBagLayout());
	       
			// set panels to display in game state, default idle1 is displayed first						
	       	p1_turn = new JLabel("Player 1's Turn");
	       	p2_turn = new JLabel("Player 2's Turn");
	       	dis_win = new JLabel("Game Over! Press Restart Game to Play Again");
	       	idle = new JLabel("Press Start Game to Begin");
	       	set1 = new JLabel("Player 1 Set Your Pieces");
	       	set2 = new JLabel("Player 2 Set Your Pieces");

	       	// Player 1 is first to set their pieces
	       	p1_turn.setVisible(false);
	       	p2_turn.setVisible(false);
	       	dis_win.setVisible(false);
	       	idle.setVisible(false);
	       	set1.setVisible(true);
	       	set2.setVisible(false);

			p1_turn.setFont(new Font("Serif", Font.BOLD, 40));
			p2_turn.setFont(new Font("Serif", Font.BOLD, 40));
			dis_win.setFont(new Font("Serif", Font.BOLD, 40));
			idle.setFont(new Font("Serif", Font.BOLD, 40));
			set1.setFont(new Font("Serif", Font.BOLD, 40));
			set2.setFont(new Font("Serif", Font.BOLD, 40));

			p1_turn.setForeground(Color.YELLOW);
			p2_turn.setForeground(Color.YELLOW);
			dis_win.setForeground(Color.YELLOW);
			idle.setForeground(Color.YELLOW);
			set1.setForeground(Color.YELLOW);
			set2.setForeground(Color.YELLOW);
		
			bottom.setBackground(Color.BLACK);

			// add labels
			bottom.add(idle);
			bottom.add(p1_turn);
			bottom.add(p2_turn);
			bottom.add(dis_win);
			bottom.add(set1);
			bottom.add(set2);

	        add(bottom, BorderLayout.SOUTH);
		}

		// set the center part (game board) of the grid layout
		public void Set_Center(BoardListener bl)
		{
			int i;
			JPanel center = new JPanel();
	        center.setLayout(new GridLayout(10,10,2,2));

	        for(i = 0; i < 100; i++)
	        {
	        	JButton button = new JButton();

				button.addActionListener(bl);

	        	if(i == 42 || i == 43 || i == 52 || i == 53 || i == 46 || i == 47
	        		|| i == 56 || i == 57)
	        	{
	        		button.setBackground(Color.BLACK);
	        		button.setEnabled(false);
	        		
	        	}
	        	else if (i < 40)
	        	{
	        		button.setBackground(Color.RED);
	        		
	        	}
	        	else if (i > 59)
	        	{
	        		button.setBackground(Color.BLUE);
	        		
	        	}
	        	
	        	
	        	board.add(button);
	        	center.add(board.elementAt(i));
	        }

	        add(center, BorderLayout.CENTER);
		}

		// event class keeps track of the last clicked button
		// either light or dark side to add to the game board
		private class LastClicked implements ActionListener 
		{
			public void actionPerformed(ActionEvent e)
			{
				JButton t = new JButton();

				// dark side piece pressed
				if(e.getSource() == dark_side.elementAt(0))
				{
					t = dark_side.elementAt(0);
					t.setSelected(true);
					t.setEnabled(false);
					dark_side.set(0, t);
				
					reset_dark(0);
					reset_light(12); // to reset the whole other side
				}
				else if(e.getSource() == dark_side.elementAt(1))
				{
					t = dark_side.elementAt(1);
					t.setSelected(true);
					t.setEnabled(false);
					dark_side.set(1, t);
				
					reset_dark(1);
					reset_light(12); // to reset the whole other side
				}
				else if(e.getSource() == dark_side.elementAt(2))
				{
					t = dark_side.elementAt(2);
					t.setSelected(true);
					t.setEnabled(false);
					dark_side.set(2, t);
				
					reset_dark(2);
					reset_light(12); // to reset the whole other side
				}
				else if(e.getSource() == dark_side.elementAt(3))
				{
					t = dark_side.elementAt(3);
					t.setSelected(true);
					t.setEnabled(false);
					dark_side.set(3, t);
				
					reset_dark(3);
					reset_light(12); // to reset the whole other side
				}
				else if(e.getSource() == dark_side.elementAt(4))
				{
					t = dark_side.elementAt(4);
					t.setSelected(true);
					t.setEnabled(false);
					dark_side.set(4, t);
				
					reset_dark(4);
					reset_light(12); // to reset the whole other side
				}
				else if(e.getSource() == dark_side.elementAt(5))
				{
					t = dark_side.elementAt(5);
					t.setSelected(true);
					t.setEnabled(false);
					dark_side.set(5, t);
				
					reset_dark(5);
					reset_light(12); // to reset the whole other side
				}
				else if(e.getSource() == dark_side.elementAt(6))
				{
					t = dark_side.elementAt(6);
					t.setSelected(true);
					t.setEnabled(false);
					dark_side.set(6, t);
				
					reset_dark(6);
					reset_light(12); // to reset the whole other side
				}
				else if(e.getSource() == dark_side.elementAt(7))
				{
					t = dark_side.elementAt(7);
					t.setSelected(true);
					t.setEnabled(false);
					dark_side.set(7, t);

					reset_dark(7);
					reset_light(12); // to reset the whole other side
				}
				else if(e.getSource() == dark_side.elementAt(8))
				{
					t = dark_side.elementAt(8);
					t.setSelected(true);
					t.setEnabled(false);
					dark_side.set(8, t);

					reset_dark(8);
					reset_light(12); // to reset the whole other side
				}
				else if(e.getSource() == dark_side.elementAt(9))
				{
					t = dark_side.elementAt(9);
					t.setSelected(true);
					t.setEnabled(false);
					dark_side.set(9, t);

					reset_dark(9);
					reset_light(12); // to reset the whole other side
				}
				else if(e.getSource() == dark_side.elementAt(10))
				{
					t = dark_side.elementAt(10);
					t.setSelected(true);
					t.setEnabled(false);
					dark_side.set(10, t);

					reset_dark(10);
					reset_light(12); // to reset the whole other side
				}
				else if(e.getSource() == dark_side.elementAt(11))
				{
					t = dark_side.elementAt(11);
					t.setSelected(true);
					t.setEnabled(false);
					dark_side.set(11, t);

					reset_dark(11);
					reset_light(12); // to reset the whole other side
				}

				// light side piece pressed
				if(e.getSource() == light_side.elementAt(0))
				{
					t = light_side.elementAt(0);
					t.setSelected(true);
					t.setEnabled(false);
					light_side.set(0, t);
				
					reset_light(0);
					reset_dark(12);
				}
				else if(e.getSource() == light_side.elementAt(1))
				{
					t = light_side.elementAt(1);
					t.setSelected(true);
					t.setEnabled(false);
					light_side.set(1, t);
				
					reset_light(1);
					reset_dark(12);
				}
				else if(e.getSource() == light_side.elementAt(2))
				{
					t = light_side.elementAt(2);
					t.setSelected(true);
					t.setEnabled(false);
					light_side.set(2, t);
				
					reset_light(2);
					reset_dark(12);
				}
				else if(e.getSource() == light_side.elementAt(3))
				{
					t = light_side.elementAt(3);
					t.setSelected(true);
					t.setEnabled(false);
					light_side.set(3, t);
				
					reset_light(3);
					reset_dark(12);
				}
				else if(e.getSource() == light_side.elementAt(4))
				{
					t = light_side.elementAt(4);
					t.setSelected(true);
					t.setEnabled(false);
					light_side.set(4, t);
				
					reset_light(4);
					reset_dark(12);
				}
				else if(e.getSource() == light_side.elementAt(5))
				{
					t = light_side.elementAt(5);
					t.setSelected(true);
					t.setEnabled(false);
					light_side.set(5, t);
				
					reset_light(5);
					reset_dark(12);
				}
				else if(e.getSource() == light_side.elementAt(6))
				{
					t = light_side.elementAt(6);
					t.setSelected(true);
					t.setEnabled(false);
					light_side.set(6, t);
				
					reset_light(6);
					reset_dark(12);
				}
				else if(e.getSource() == light_side.elementAt(7))
				{
					t = light_side.elementAt(7);
					t.setSelected(true);
					t.setEnabled(false);
					light_side.set(7, t);

					reset_light(7);
					reset_dark(12);
				}
				else if(e.getSource() == light_side.elementAt(8))
				{
					t = light_side.elementAt(8);
					t.setSelected(true);
					t.setEnabled(false);
					light_side.set(8, t);

					reset_light(8);
					reset_dark(12);
				}
				else if(e.getSource() == light_side.elementAt(9))
				{
					t = light_side.elementAt(9);
					t.setSelected(true);
					t.setEnabled(false);
					light_side.set(9, t);

					reset_light(9);
					reset_dark(12);
				}
				else if(e.getSource() == light_side.elementAt(10))
				{
					t = light_side.elementAt(10);
					t.setSelected(true);
					t.setEnabled(false);
					light_side.set(10, t);

					reset_light(10);
					reset_dark(12);
				}
				else if(e.getSource() == light_side.elementAt(11))
				{
					t = light_side.elementAt(11);
					t.setSelected(true);
					t.setEnabled(false);
					light_side.set(11, t);

					reset_light(11);
					reset_dark(12);
				}				
			}


			public void reset_dark(int pos)
			{
				int j;
				JButton t;

				for(j = 0; j < 12; j++)
				{
					if(j != pos)
					{
						t = dark_side.elementAt(j);	
						t.setEnabled(true);
						t.setSelected(false);
						dark_side.set(j, t);		
					}				
				}
			}

			public void reset_light(int pos)
			{
				int j;
				JButton t;

				for(j = 0; j < 12; j++)
				{
					if(j != pos)
					{
						t = light_side.elementAt(j);	
						t.setEnabled(true);
						t.setSelected(false);
						light_side.set(j, t);		
					}				
				}
			}
		}


		// event class keeps track of the button clicked to add
		// the image when the game board is being intitalized
		// after that it is used to implement game movement
		// deal with moving spaces and battles of pieces
		private class BoardListener implements ActionListener 
		{
			JButton temp_sel;
			JButton icon_sel;
			public void actionPerformed(ActionEvent e) 
			{
				temp_sel = new JButton();
				icon_sel = new JButton();
				JButton p = new JButton();
				int i;
				int j;
				int k;
				int diff;
				int temp_j;		// used for Scout horiz. movement
				int temp_i;		// ""

				int q;	// special case for Scouts

				boolean blocked = false; // flag if any space is blocked in a scout's path

				int temp;
				String holder;

				if(turn == 0)
					return;
				

				// user is still setting pieces and hasn't started the game
				if(!been_set)
				{
					for(i = 0; i < 100; i++)
					{
						//find the button on the board
						if(e.getSource() == board.elementAt(i))
						{
							if(board_values[i] != -1)
								break;


							// selected button
							temp_sel = board.elementAt(i);

							//copy the selected button
							for(j = 0; j < 12; j++)
							{
								if(turn == 1)
								{
									//put the icon on the board and declare piece as set
									if(player1.side.equals("Dark Side"))
									{
										icon_sel = dark_side.elementAt(j);

										if(icon_sel.isSelected())
										{
											if(i < 40)
											{
												Valid_Placement(i, j, turn);
											}
										}
									}
									else
									{
										icon_sel = light_side.elementAt(j);

										if(icon_sel.isSelected())
										{
											if(i > 59)
											{
												Valid_Placement(i, j, turn);
											}
										}
									}

									// player 1 is done placing all of their pieces
									if(player1.Check_All_Set())
									{
										set1.setVisible(false);
    									set2.setVisible(true);
										turn = 2;

										//Hide_Player(player1.side);
									}
								}
								else
								{
									// change icon and declare piece as set
									if(player2.side.equals("Dark Side"))
									{
										icon_sel = dark_side.elementAt(j);

										if(icon_sel.isSelected())
										{
											if(i < 40)
											{
												Valid_Placement(i, j, turn);
											}
					    					
										}
									}
									else
									{
										icon_sel = light_side.elementAt(j);

										if(icon_sel.isSelected())
										{
											if(i > 59)
											{
												Valid_Placement(i, j, turn);
											}
										}
									}

									// player 1 is done placing all of their pieces
									if(player2.Check_All_Set())
									{
    									set2.setVisible(false);
    									idle.setVisible(true);
    									but_start.setEnabled(true);
										turn = 2;
										//Hide_Player(player2.side);
									}
								}
							}
						}
					}
				}
				else  // game has been started so now focus on moving the board pieces
				{
					// check to see if is this is the "movement click"
					// user must first click the button they want to move a pieces
					// and then click the button they wish to move to
					for(i = 0; i < 100; i++)
					{
						temp_sel = board.elementAt(i);

						// this is the second click
						if(temp_sel.isSelected())
						{
							break;
						}
					}

					// this is the first click (selecting the piece to move)
					if(i == 100)
					{
						for(j = 0; j < 100; j++)
						{
							// lock the button
							if(e.getSource() == board.elementAt(j))
							{
								temp_sel = board.elementAt(j);
								temp_sel.setEnabled(false);
								temp_sel.setSelected(true);
								board.set(j, temp_sel);
								break;
							}
						}
					}
					else // second click (selecting the destination) assuming the user will first 
							// select the piece they want to move and then selecting the destination button
					{
						//deselecting all buttons since move is known
						for(k = 0; k < 100; k++)
						{
							p = board.elementAt(k);
							
							// can deselect now
							if(k != 42 || k != 43 || k != 52 || k != 53 || k != 46 || k != 47
	        					|| k != 56 || k != 57)
	        				{
								p.setEnabled(true);
							}
							else 
							{
								p.setEnabled(false);
							}

							p.setSelected(false);
							board.set(k, p);
						}
							

						for(j = 0; j < 100; j++)
						{
							if(e.getSource() == board.elementAt(j))
							{
								// cant go in blacked out areas
								if(j == 42 || j == 43 || j == 52 || j == 53 || j == 46 || j == 47
	        						|| j == 56 || j == 57)
	        					{
									break;
								}

								icon_sel = board.elementAt(j);
								
								//space movement
								diff = j - i;

								// special case for scouts since they can jump spaces
								if(board_values[i] == 2 && icon_sel.isEnabled())
								{
									//handles verical case
									if(j % 10 != i % 10)
									{
										temp_j = j / 10;
										temp_i = i / 10;

										blocked = false;

										// handles horizontal
										if(temp_j % 10 != temp_i % 10)
										{
											break;
										}

							
										if(j > i)	// going right on board
										{
											
											q = i+1;

											for(; q < j; q++)
											{
												//someone is inbetween and can't jump pieces
												if(board_values[q] != -1)
												{	
													blocked = true;
													break;
												}
												
												// can't jump black area
												if(q == 42 || q == 43 || q == 52 || q == 53 || q == 46 || q == 47
					        						|| q == 56 || q == 57)
					        					{
					        						blocked = true;
													break;
												}

											}
										}
										else //going left on board
										{
											q = j+1;

											for(; q < i; q++)
											{
												if(board_values[q] != -1)
												{	
													blocked = true;
													break;
												}

												if(q == 42 || q == 43 || q == 52 || q == 53 || q == 46 || q == 47
					        						|| q == 56 || q == 57)
					        					{
					        						blocked = true;
													break;
												}

											}
										}

										if(blocked)
											break;
									}	
									else
									{ 
										blocked = false;
										
										q = i;

										if(j > i) // going down the board
										{
											q = i+10;

											for(; q < j; q += 10)
											{
												if(board_values[q] != -1)
												{	
													blocked = true;
													break;
												}

												if(q == 42 || q == 43 || q == 52 || q == 53 || q == 46 || q == 47
					        						|| q == 56 || q == 57)
					        					{
					        						blocked = true;
													break;
												}
											}
										}
										else
										{	
											// going up the board
											q = j+10;

											for(; q < i; q += 10)
											{
												if(board_values[q] != -1)
												{	
													blocked = true;
													break;
												}

												if(q == 42 || q == 43 || q == 52 || q == 53 || q == 46 || q == 47
					        						|| q == 56 || q == 57)
					        					{
					        						blocked = true;
													break;
												}
											}
										}
										
										if(blocked)
											break;
									}


									if(turn == 1)
									{
										if(player1.side.equals("Light Side"))
										{
											// can't move on same space as someone on team
											if(board_values[j] != -1 && (board_sides[j].equals("LS")))
											{
												return;
											}

											// can't select other player's pieces
											if(board_sides[i].equals("DS"))
											{
												return;
											}

											if(board_values[j] != -1)
											{
												// someone not on player's side was position here
												Battle(board_values[i], board_values[j], i, j);
												if(turn == 0)
													return;
									
												turn = 2;
												p1_turn.setVisible(false);
												p2_turn.setVisible(true);
											}
											else
											{
												// swap positions
												icon_sel.setIcon(new ImageIcon(ls2));
												temp_sel.setIcon(null);
												temp = board_values[j];
												board_values[j] = board_values[i];
												board_values[i] = temp;
												

												holder = new String(board_sides[j]);
												board_sides[j] = new String(board_sides[i]);
												board_sides[i] = new String(holder);
												board.set(i, temp_sel);
												board.set(j, icon_sel);
												
												turn = 2;
												p1_turn.setVisible(false);
												p2_turn.setVisible(true);
											}		
										}
										else
										{
											// can't move on same space as someone on team
											if(board_values[j] != -1 && (board_sides[j].equals("DS")))
											{
												return;
											}

											// can't select other player's pieces
											if(board_sides[i].equals("LS"))
											{
												return;
											}

											if(board_values[j] != -1)
											{
												// someone not on player's side was position here
												Battle(board_values[i], board_values[j], i, j);
												if(turn == 0)
													return;
									
												turn = 2;
												p1_turn.setVisible(false);
												p2_turn.setVisible(true);
											}
											else
											{
												// swap positions
												icon_sel.setIcon(new ImageIcon(ds2));
												temp_sel.setIcon(null);
												temp = board_values[j];
												board_values[j] = board_values[i];
												board_values[i] = temp;
												

												holder = new String(board_sides[j]);
												board_sides[j] = new String(board_sides[i]);
												board_sides[i] = new String(holder);
												board.set(i, temp_sel);
												board.set(j, icon_sel);
												
												turn = 2;
												p1_turn.setVisible(false);
												p2_turn.setVisible(true);
											}		
										}

										if(turn == 0)
											break;										
									}
									else
									{
										if(player2.side.equals("Light Side"))
										{
											// can't move on same space as someone on team
											if(board_values[j] != -1 && (board_sides[j].equals("LS")))
											{
												return;
											}

											// can't select other player's pieces
											if(board_sides[i].equals("DS"))
											{
												return;
											}

											if(board_values[j] != -1)
											{
												// someone not on player's side was position here
												Battle(board_values[i], board_values[j], i, j);
												if(turn == 0)
													return;
									
												turn = 1;
												p1_turn.setVisible(true);
												p2_turn.setVisible(false);
											}
											else
											{
												// swap positions
												icon_sel.setIcon(new ImageIcon(ls2));
												temp_sel.setIcon(null);
												temp = board_values[j];
												board_values[j] = board_values[i];
												board_values[i] = temp;
												

												holder = new String(board_sides[j]);
												board_sides[j] = new String(board_sides[i]);
												board_sides[i] = new String(holder);
												board.set(i, temp_sel);
												board.set(j, icon_sel);
												
												turn = 1;
												p1_turn.setVisible(true);
												p2_turn.setVisible(false);
											}		
										}
										else
										{
											// can't move on same space as someone on team
											if(board_values[j] != -1 && (board_sides[j].equals("DS")))
											{
												return;
											}

											// can't select other player's pieces
											if(board_sides[i].equals("LS"))
											{
												return;
											}

											if(board_values[j] != -1)
											{
												// someone not on player's side was position here
												Battle(board_values[i], board_values[j], i, j);
												if(turn == 0)
													return;
									
												turn = 1;
												p1_turn.setVisible(true);
												p2_turn.setVisible(false);
											}
											else
											{
												// swap positions
												icon_sel.setIcon(new ImageIcon(ds2));
												temp_sel.setIcon(null);
												temp = board_values[j];
												board_values[j] = board_values[i];
												board_values[i] = temp;
												

												holder = new String(board_sides[j]);
												board_sides[j] = new String(board_sides[i]);
												board_sides[i] = new String(holder);
												board.set(i, temp_sel);
												board.set(j, icon_sel);
												
												turn = 1;
												p1_turn.setVisible(true);
												p2_turn.setVisible(false);
											}		
										}

										if(turn == 0)
											break;
									}			
								} 
								// not a scout, check if a valid direction
								// left,right,down or up 1
								else if((diff == -1 || (diff == 1) || (diff == 10) || (diff == -10)) && icon_sel.isEnabled())	
								{
									// if first player's turn
									if(turn == 1)
									{
										// if they chose light side 
										if(player1.side.equals("Light Side"))
										{
											//can't move opposing player's pieces
											if(board_sides[i].equals("DS"))
											{
												break;
											}

											// can't move on same space as someone on team
											if(board_values[j] != -1 && (board_sides[j].equals("LS")))
											{

												break;
											}


											if(board_values[j] != -1 && (board_sides[j].equals("DS"))) // spot is populated by opposing piece
											{
												Battle(board_values[i], board_values[j], i, j);
												if(turn == 0)
													break;
												turn = 2;
												p1_turn.setVisible(false);
												p2_turn.setVisible(true);
												break;
											}	
											else if(board_values[i] == 0) // can't move flag
											{
												turn = 1;
												p1_turn.setVisible(true);
												p2_turn.setVisible(false);
												break;
											}
											else if (board_values[i] == 1)
											{	
					    						icon_sel.setIcon(new ImageIcon(ls1));
					    						temp_sel.setIcon(null);	
											}
											else if (board_values[i] == 2)
											{
					    						icon_sel.setIcon(new ImageIcon(ls2));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 3)
											{
					    						icon_sel.setIcon(new ImageIcon(ls3));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 4)
											{
					    						icon_sel.setIcon(new ImageIcon(ls4));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 5)
											{
					    						icon_sel.setIcon(new ImageIcon(ls5));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 6)
											{
					    						icon_sel.setIcon(new ImageIcon(ls6));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 7)
											{
					    						icon_sel.setIcon(new ImageIcon(ls7));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 8)
											{
					    						icon_sel.setIcon(new ImageIcon(ls8));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 9)
											{
					    						icon_sel.setIcon(new ImageIcon(ls9));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 10)
											{
					    						icon_sel.setIcon(new ImageIcon(ls10));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 11) // can't move bombs
											{
												turn = 1;
												p1_turn.setVisible(true);
												p2_turn.setVisible(false);
												break;
											}

											temp = board_values[j];
											board_values[j] = board_values[i];
											board_values[i] = temp;
											

											holder = new String(board_sides[j]);
											board_sides[j] = new String(board_sides[i]);
											board_sides[i] = new String(holder);
											board.set(i, temp_sel);
											board.set(j, icon_sel);
											turn = 2;
											p1_turn.setVisible(false);
											p2_turn.setVisible(true);
											break;
										}
										else
										{
											//can't move opposing player's pieces
											if(board_sides[i].equals("LS"))
											{
												break;
											}

											// cant move on same space as teammate
											if(board_values[j] != -1 && (board_sides[j].equals("DS")))
											{
												
												break;
											}

											// opposing piece owns this spot so higher value stays
											if(board_values[j] != -1 && board_sides[j].equals("LS"))
											{
												Battle(board_values[i], board_values[j], i, j);
												if(turn == 0)
													break;
												turn = 2;
												p1_turn.setVisible(false);
												p2_turn.setVisible(true);
												break;
											}
											if(board_values[i] == 0) // can't move flag
											{
												turn = 1;
												p1_turn.setVisible(true);
												p2_turn.setVisible(false);
												break;
											}
											else if(board_values[i] == 1)
											{	
					    						icon_sel.setIcon(new ImageIcon(ds1));
					    						temp_sel.setIcon(null);	
											}
											else if(board_values[i] == 2)
											{
					    						icon_sel.setIcon(new ImageIcon(ds2));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 3)
											{
					    						icon_sel.setIcon(new ImageIcon(ds3));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 4)
											{
					    						icon_sel.setIcon(new ImageIcon(ds4));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 5)
											{
					    						icon_sel.setIcon(new ImageIcon(ds5));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 6)
											{
					    						icon_sel.setIcon(new ImageIcon(ds6));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 7)
											{
					    						icon_sel.setIcon(new ImageIcon(ds7));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 8)
											{
					    						icon_sel.setIcon(new ImageIcon(ds8));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 9)
											{
					    						icon_sel.setIcon(new ImageIcon(ds9));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 10)
											{
					    						icon_sel.setIcon(new ImageIcon(ds10));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 11) // can't move bombs
											{
												turn = 1;
												p1_turn.setVisible(true);
												p2_turn.setVisible(false);
												break;
											}

											//swap spots
											temp = board_values[j];
											board_values[j] = board_values[i];
											board_values[i] = temp;
											

											holder = new String(board_sides[j]);
											board_sides[j] = new String(board_sides[i]);
											board_sides[i] = new String(holder);
											board.set(i, temp_sel);
											board.set(j, icon_sel);
											turn = 2;
											p1_turn.setVisible(false);
											p2_turn.setVisible(true);
											break;
										}	
									}
									else
									{
										// if they chose light side 
										if(player2.side.equals("Light Side"))
										{
											//can't move opposing player's pieces
											if(board_sides[i].equals("DS"))
											{
												break;
											}

											// can't move on same space as someone on team
											if(board_values[j] != -1 && (board_sides[j].equals("LS")))
											{
												break;
											}
												
											if(board_values[j] != -1 && board_sides[j].equals("DS"))
											{
												Battle(board_values[i], board_values[j], i, j);
												if(turn == 0)
													break;
												turn = 1;
												p1_turn.setVisible(true);
												p2_turn.setVisible(false);
												break;
											}
											else if(board_values[i] == 0) // can't move flag
											{
												turn = 2;
												p1_turn.setVisible(false);
												p2_turn.setVisible(true);
												break;
											}
											else if (board_values[i] == 1)
											{	
					    						icon_sel.setIcon(new ImageIcon(ls1));
					    						temp_sel.setIcon(null);	
											}
											else if (board_values[i] == 2)
											{
					    						icon_sel.setIcon(new ImageIcon(ls2));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 3)
											{
					    						icon_sel.setIcon(new ImageIcon(ls3));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 4)
											{
					    						icon_sel.setIcon(new ImageIcon(ls4));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 5)
											{
					    						icon_sel.setIcon(new ImageIcon(ls5));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 6)
											{
					    						icon_sel.setIcon(new ImageIcon(ls6));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 7)
											{
					    						icon_sel.setIcon(new ImageIcon(ls7));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 8)
											{
					    						icon_sel.setIcon(new ImageIcon(ls8));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 9)
											{
					    						icon_sel.setIcon(new ImageIcon(ls9));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 10)
											{
					    						icon_sel.setIcon(new ImageIcon(ls10));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 11) // can't move bombs
											{
												turn = 2;
												p1_turn.setVisible(false);
												p2_turn.setVisible(true);
												break;
											}

											temp = board_values[j];
											board_values[j] = board_values[i];
											board_values[i] = temp;
											

											holder = new String(board_sides[j]);
											board_sides[j] = new String(board_sides[i]);
											board_sides[i] = new String(holder);
											board.set(i, temp_sel);
											board.set(j, icon_sel);
											turn = 1;
											p1_turn.setVisible(true);
											p2_turn.setVisible(false);
											break;
										}
										else
										{
											//can't move opposing player's pieces
											if(board_sides[i].equals("LS"))
											{
												break;
											}

											if(board_values[j] != -1 && (board_sides[j].equals("DS")))
											{
												break;
											}


											if(board_values[j] != -1 && board_sides[j].equals("LS"))
											{
												Battle(board_values[i], board_values[j], i, j);
												if(turn == 0)
													break;
												turn = 1;
												p1_turn.setVisible(true);
												p2_turn.setVisible(false);
												break;
											}
											else if(board_values[i] == 0) // can't move flag
											{
												turn = 2;
												p1_turn.setVisible(false);
												p2_turn.setVisible(true);
												break;
											}
											else if(board_values[i] == 1)
											{	
					    						icon_sel.setIcon(new ImageIcon(ds1));
					    						temp_sel.setIcon(null);	
											}
											else if(board_values[i] == 2)
											{
					    						icon_sel.setIcon(new ImageIcon(ds2));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 3)
											{
					    						icon_sel.setIcon(new ImageIcon(ds3));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 4)
											{
					    						icon_sel.setIcon(new ImageIcon(ds4));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 5)
											{
					    						icon_sel.setIcon(new ImageIcon(ds5));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 6)
											{
					    						icon_sel.setIcon(new ImageIcon(ds6));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 7)
											{
					    						icon_sel.setIcon(new ImageIcon(ds7));
					    						temp_sel.setIcon(null);
											}
											else if(board_values[i] == 8)
											{
					    						icon_sel.setIcon(new ImageIcon(ds8));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 9)
											{
					    						icon_sel.setIcon(new ImageIcon(ds9));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 10)
											{
					    						icon_sel.setIcon(new ImageIcon(ds10));
					    						temp_sel.setIcon(null);
											}
											else if (board_values[i] == 11) // can't move bombs
											{
												turn = 2;
												p1_turn.setVisible(false);
												p2_turn.setVisible(true);
												break;
											}

											temp = board_values[j];
											board_values[j] = board_values[i];
											board_values[i] = temp;
											

											holder = new String(board_sides[j]);
											board_sides[j] = new String(board_sides[i]);
											board_sides[i] = new String(holder);
											board.set(i, temp_sel);
											board.set(j, icon_sel);
											turn = 1;
											p1_turn.setVisible(true);
											p2_turn.setVisible(false);
											break;
										}	
									}	
								}
							}
						}

						// final reset of selected buttons
						for(k = 0; k < 100; k++)
						{
							p = board.elementAt(k);
							p.setSelected(false);
							board.set(k, p);
						}	
					}

					if(turn == 0)
					{
						p1_turn.setVisible(false);
						p2_turn.setVisible(false);
					}
				}
			}


			// valid set area for pieces so establish that spot as being taken
			// increment the users placed pieces
			public void Valid_Placement(int i, int j, int turn)
			{
				if(turn == 1)
				{
					if(j == 0)
					{
						if(player1.pieces.set[Pieces.FLAG] < Pieces.MAX_FLAG)
						{
							player1.pieces.set[Pieces.FLAG]++;
							
							if(player1.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds0));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls0));
							}	
						}
						else
							return;
					}
					else if (j == 1)
					{
						if(player1.pieces.set[Pieces.SPY] < Pieces.MAX_SPY)
						{
							player1.pieces.set[Pieces.SPY]++;
							
							if(player1.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds1));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls1));
							}
						}
						else
							return;
					}
					else if (j == 2)
					{
						if(player1.pieces.set[Pieces.SCOUT] < Pieces.MAX_SCOUT)
						{
							player1.pieces.set[Pieces.SCOUT]++;
							
							if(player1.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds2));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls2));
							}
						}
						else
							return;
					}
					else if (j == 3)
					{
						if(player1.pieces.set[Pieces.MINER] < Pieces.MAX_MINER)
						{
							player1.pieces.set[Pieces.MINER]++;
							
							if(player1.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds3));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls3));
							}
						}
						else
							return;
					}
					else if (j == 4)
					{
						if(player1.pieces.set[Pieces.SERGEANT] < Pieces.MAX_SERGEANT)
						{
							player1.pieces.set[Pieces.SERGEANT]++;
														
							if(player1.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds4));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls4));
							}
						}
						else
							return;
					}
					else if (j == 5)
					{
						if(player1.pieces.set[Pieces.LIEUTENANT] < Pieces.MAX_LIEUTENANT)
						{
							player1.pieces.set[Pieces.LIEUTENANT]++;
							
							if(player1.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds5));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls5));
							}
						}
						else
							return;
					}
					else if(j == 6)
					{
						if(player1.pieces.set[Pieces.CAPTAIN] < Pieces.MAX_CAPTAIN)
						{
							player1.pieces.set[Pieces.CAPTAIN]++;
							
							if(player1.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds6));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls6));
							}
						}
						else
							return;
					}
					else if(j == 7)
					{
						if(player1.pieces.set[Pieces.MAJOR] < Pieces.MAX_MAJOR)
						{
							player1.pieces.set[Pieces.MAJOR]++;
							
							if(player1.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds7));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls7));
							}
						}
						else
							return;
					}
					else if(j == 8)
					{
						if(player1.pieces.set[Pieces.COLONEL] < Pieces.MAX_COLONEL)
						{
							player1.pieces.set[Pieces.COLONEL]++;
							
							if(player1.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds8));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls8));
							}
						}
						else
							return;
					}
					else if (j == 9)
					{
						if(player1.pieces.set[Pieces.GENERAL] < Pieces.MAX_GENERAL)
						{
							player1.pieces.set[Pieces.GENERAL]++;
								
							if(player1.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds9));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls9));
							}
						}
						else
							return;
					}
					else if (j == 10)
					{
						if(player1.pieces.set[Pieces.MARSHALL] < Pieces.MAX_MARSHALL)
						{
							player1.pieces.set[Pieces.MARSHALL]++;

							if(player1.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds10));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls10));
							}
						}
						else
							return;
					}
					else if (j == 11)
					{
						if(player1.pieces.set[Pieces.BOMB] < Pieces.MAX_BOMB)
						{
							player1.pieces.set[Pieces.BOMB]++;
							temp_sel.setIcon(new ImageIcon(bomb));
						}
						else
							return;

					}

					if(player1.side.equals("Dark Side"))
					{
						Set_Board_Pos(i, j, "DS", temp_sel);
					}
					else
					{
						Set_Board_Pos(i, j, "LS", temp_sel);
					}
				}
				else
				{
					if(j == 0)
					{
						if(player2.pieces.set[Pieces.FLAG] < Pieces.MAX_FLAG)
						{
							player2.pieces.set[Pieces.FLAG]++;
							
							if(player2.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds0));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls0));
							}	
						}
						else
							return;
					}
					else if (j == 1)
					{
						if(player2.pieces.set[Pieces.SPY] < Pieces.MAX_SPY)
						{
							player2.pieces.set[Pieces.SPY]++;
							
							if(player2.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds1));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls1));
							}
						}	
						else
							return;
					}
					else if (j == 2)
					{
						if(player2.pieces.set[Pieces.SCOUT] < Pieces.MAX_SCOUT)
						{
							player2.pieces.set[Pieces.SCOUT]++;
							
							if(player2.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds2));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls2));
							}
						}
						else
							return;
					}
					else if (j == 3)
					{
						if(player2.pieces.set[Pieces.MINER] < Pieces.MAX_MINER)
						{
							player2.pieces.set[Pieces.MINER]++;
							
							if(player2.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds3));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls3));
							}
						}
						else
							return;
					}
					else if (j == 4)
					{
						if(player2.pieces.set[Pieces.SERGEANT] < Pieces.MAX_SERGEANT)
						{
							player2.pieces.set[Pieces.SERGEANT]++;
														
							if(player2.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds4));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls4));
							}
						}
						else
							return;
					}
					else if (j == 5)
					{
						if(player2.pieces.set[Pieces.LIEUTENANT] < Pieces.MAX_LIEUTENANT)
						{
							player2.pieces.set[Pieces.LIEUTENANT]++;
							
							if(player2.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds5));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls5));
							}
						}
						else
							return;
					}
					else if(j == 6)
					{
						if(player2.pieces.set[Pieces.CAPTAIN] < Pieces.MAX_CAPTAIN)
						{
							player2.pieces.set[Pieces.CAPTAIN]++;
							
							if(player2.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds6));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls6));
							}
						}
						else
							return;
					}
					else if(j == 7)
					{
						if(player2.pieces.set[Pieces.MAJOR] < Pieces.MAX_MAJOR)
						{
							player2.pieces.set[Pieces.MAJOR]++;
							
							if(player2.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds7));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls7));
							}
						}
						else
							return;
					}
					else if(j == 8)
					{
						if(player2.pieces.set[Pieces.COLONEL] < Pieces.MAX_COLONEL)
						{
							player2.pieces.set[Pieces.COLONEL]++;
							
							if(player2.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds8));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls8));
							}
						}
						else
							return;
					}
					else if (j == 9)
					{
						if(player2.pieces.set[Pieces.GENERAL] < Pieces.MAX_GENERAL)
						{
							player2.pieces.set[Pieces.GENERAL]++;
								
							if(player2.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds9));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls9));
							}
						}
						else
							return;
					}
					else if (j == 10)
					{
						if(player2.pieces.set[Pieces.MARSHALL] < Pieces.MAX_MARSHALL)
						{
							player2.pieces.set[Pieces.MARSHALL]++;

							if(player2.side.equals("Dark Side"))
							{
								temp_sel.setIcon(new ImageIcon(ds10));
							}
							else
							{
								temp_sel.setIcon(new ImageIcon(ls10));
							}
						}
						else
							return;
					}
					else if (j == 11)
					{
						if(player2.pieces.set[Pieces.BOMB] < Pieces.MAX_BOMB)
						{
							player2.pieces.set[Pieces.BOMB]++;
							temp_sel.setIcon(new ImageIcon(bomb));
						}
						else
							return;
					}

					if(player2.side.equals("Dark Side"))
					{
						Set_Board_Pos(i, j, "DS", temp_sel);
					}
					else
					{
						Set_Board_Pos(i, j, "LS", temp_sel);
					}
				}
			}

			
			// intial set of pieces on the baord
			public void Set_Board_Pos(int pos, int piece, String side, JButton temp)
			{
				board_values[pos] = piece;
				board_sides[pos] = new String(side);
				board.set(pos, temp);
			}


			// handle the interaction of pieces when they are competing for same tile
			public void Battle(int attacker, int defender, int i, int j)
			{
				// means game over
				if(defender == Pieces.FLAG)
				{
					String winner;

					// make the final move and alert the game is over
					if(turn == 1)
					{
						player2.pieces.Add_Piece_Removed(Pieces.FLAG);
						winner = new String("Player 1 Wins!");
						p1_turn.setVisible(false);

						P1_Move(board_values[i]);
					}
					else
					{
						player1.pieces.Add_Piece_Removed(Pieces.FLAG);
						winner = new String("Player 2 Wins!");
						p2_turn.setVisible(false);

						P2_Move(board_values[i]);
					}

					temp_sel.setIcon(null);
					board_values[j] = attacker;
					board_values[i] = -1;

					board_sides[j] = new String(board_sides[i]);
					board_sides[i] = "";
					board.set(i, temp_sel);
					board.set(j, icon_sel);

					JFrame temp = new JFrame();

					//displays game over
					dis_win.setVisible(true);

					// alert of a winner
			    	Object[] opt = {"Ok"};
					int n = JOptionPane.showOptionDialog(temp,
						winner,
					    "Game Over",
					    JOptionPane.YES_NO_OPTION,
					    JOptionPane.PLAIN_MESSAGE,
					    null,
					    opt,     
					    opt[0]); 

					turn = 0;	//helps signal game is over
					return;
				}

				// special case attacking a bomb
				if(defender == Pieces.BOMB)
				{
					// only way to beat a bomb
					if(attacker == Pieces.MINER)
					{
						if(turn == 1)
						{
							player2.pieces.Add_Piece_Removed(Pieces.BOMB);

							if(player1.side.equals("Light Side"))
							{
								temp_sel.setIcon(null);
								icon_sel.setIcon(new ImageIcon(ls3));
							}
							else
							{
								temp_sel.setIcon(null);
								icon_sel.setIcon(new ImageIcon(ds3));
							}
						}
						else
						{
							player1.pieces.Add_Piece_Removed(Pieces.BOMB);

							if(player2.side.equals("Light Side"))
							{
								temp_sel.setIcon(null);
								icon_sel.setIcon(new ImageIcon(ls3));
							}
							else
							{
								temp_sel.setIcon(null);
								icon_sel.setIcon(new ImageIcon(ds3));
							}
						}

						
						board_values[j] = Pieces.MINER;
						board_values[i] = -1;

						board_sides[j] = new String(board_sides[i]);
						board_sides[i] = "";
						board.set(i, temp_sel);
						board.set(j, icon_sel);
					}
					else
					{

						// all other pieces lose
						if(turn == 1)
						{
							player1.pieces.Add_Piece_Removed(attacker);
						}
						else
						{
							player2.pieces.Add_Piece_Removed(attacker);
						}

						temp_sel.setIcon(null);
						board_values[i] = -1;

						board_sides[i] = "";
						board.set(i, temp_sel);
					}
					return;
				}

				// special case spy attacks the marshall
				if(attacker == Pieces.SPY && defender == Pieces.MARSHALL)
				{
					if(turn == 1)
					{
						player2.pieces.Add_Piece_Removed(Pieces.MARSHALL);

						if(player1.side.equals("Light Side"))
						{
							icon_sel.setIcon(new ImageIcon(ls1));
						}
						else
						{
							icon_sel.setIcon(new ImageIcon(ds1));
						}
					}
					else
					{
						player1.pieces.Add_Piece_Removed(Pieces.MARSHALL);

						if(player2.side.equals("Light Side"))
						{
							icon_sel.setIcon(new ImageIcon(ls1));
						}
						else
						{
							icon_sel.setIcon(new ImageIcon(ds1));
						}
					}

					board_values[j] = Pieces.SPY;
					board_values[i] = -1;

					board_sides[j] = new String(board_sides[i]);
					board_sides[i] = "";
					temp_sel.setIcon(null);
					board.set(i, temp_sel);
					board.set(j, icon_sel);
					return;
				}

				// attacking a piece of equal value, both pieces are taken
				if(attacker == defender)
				{
					player1.pieces.Add_Piece_Removed(attacker);
					player2.pieces.Add_Piece_Removed(defender);

					temp_sel.setIcon(null);
					icon_sel.setIcon(null);

					board_values[i] = -1;
					board_values[j] = -1;

					board_sides[i] = "";
					board_sides[j] = "";

					board.set(i, temp_sel);
					board.set(j, icon_sel);
				}
				else if(attacker < defender)
				{
					//defender won so remove attacker
					if(turn == 1)
					{
						player1.pieces.Add_Piece_Removed(attacker);
					}
					else
					{
						player2.pieces.Add_Piece_Removed(attacker);
					}

					board_values[i] = -1;
					board_sides[i] = "";
					temp_sel.setIcon(null);
					board.set(i, temp_sel);
				}
				else if(attacker > defender)
				{
					//attacker won so take defender's spot
					if(turn == 1)
					{
						player2.pieces.Add_Piece_Removed(defender);
						temp_sel.setIcon(null);

						P1_Move(board_values[i]);
					}
					else
					{
						player1.pieces.Add_Piece_Removed(defender);
						temp_sel.setIcon(null);

						P2_Move(board_values[i]);
					}

					board_values[j] = attacker;
					board_values[i] = -1;

					board_sides[j] = new String(board_sides[i]);
					board_sides[i] = "";
					board.set(i, temp_sel);
					board.set(j, icon_sel);
				}
			}

			// set icon of moving piece for player1 in battle
			public void P1_Move(int icon)
			{
				if(player1.side.equals("Light Side"))
				{
					if(icon == 1)
						icon_sel.setIcon(new ImageIcon(ls1));
					else if(icon == 2)
						icon_sel.setIcon(new ImageIcon(ls2));
					else if(icon == 3)
						icon_sel.setIcon(new ImageIcon(ls3));
					else if(icon == 4)
						icon_sel.setIcon(new ImageIcon(ls4));
					else if(icon == 5)
						icon_sel.setIcon(new ImageIcon(ls5));
					else if(icon == 6)
						icon_sel.setIcon(new ImageIcon(ls6));
					else if(icon == 7)
						icon_sel.setIcon(new ImageIcon(ls7));
					else if(icon == 8)
						icon_sel.setIcon(new ImageIcon(ls8));
					else if(icon == 9)
						icon_sel.setIcon(new ImageIcon(ls9));
					else if(icon == 10)
						icon_sel.setIcon(new ImageIcon(ls10));
				}
				else
				{
					if(icon == 1)
						icon_sel.setIcon(new ImageIcon(ds1));
					else if(icon == 2)
						icon_sel.setIcon(new ImageIcon(ds2));
					else if(icon == 3)
						icon_sel.setIcon(new ImageIcon(ds3));
					else if(icon == 4)
						icon_sel.setIcon(new ImageIcon(ds4));
					else if(icon == 5)
						icon_sel.setIcon(new ImageIcon(ds5));
					else if(icon == 6)
						icon_sel.setIcon(new ImageIcon(ds6));
					else if(icon == 7)
						icon_sel.setIcon(new ImageIcon(ds7));
					else if(icon == 8)
						icon_sel.setIcon(new ImageIcon(ds8));
					else if(icon == 9)
						icon_sel.setIcon(new ImageIcon(ds9));
					else if(icon == 10)
						icon_sel.setIcon(new ImageIcon(ds10));
				}
			}

			// set icon of moving piece for player2 in battle
			public void P2_Move(int icon)
			{
				if(player2.side.equals("Light Side"))
				{
					if(icon == 1)
						icon_sel.setIcon(new ImageIcon(ls1));
					else if(icon == 2)
						icon_sel.setIcon(new ImageIcon(ls2));
					else if(icon == 3)
						icon_sel.setIcon(new ImageIcon(ls3));
					else if(icon == 4)
						icon_sel.setIcon(new ImageIcon(ls4));
					else if(icon == 5)
						icon_sel.setIcon(new ImageIcon(ls5));
					else if(icon == 6)
						icon_sel.setIcon(new ImageIcon(ls6));
					else if(icon == 7)
						icon_sel.setIcon(new ImageIcon(ls7));
					else if(icon == 8)
						icon_sel.setIcon(new ImageIcon(ls8));
					else if(icon == 9)
						icon_sel.setIcon(new ImageIcon(ls9));
					else if(icon == 10)
						icon_sel.setIcon(new ImageIcon(ls10));
				}
				else
				{
					if(icon == 1)
						icon_sel.setIcon(new ImageIcon(ds1));
					else if(icon == 2)
						icon_sel.setIcon(new ImageIcon(ds2));
					else if(icon == 3)
						icon_sel.setIcon(new ImageIcon(ds3));
					else if(icon == 4)
						icon_sel.setIcon(new ImageIcon(ds4));
					else if(icon == 5)
						icon_sel.setIcon(new ImageIcon(ds5));
					else if(icon == 6)
						icon_sel.setIcon(new ImageIcon(ds6));
					else if(icon == 7)
						icon_sel.setIcon(new ImageIcon(ds7));
					else if(icon == 8)
						icon_sel.setIcon(new ImageIcon(ds8));
					else if(icon == 9)
						icon_sel.setIcon(new ImageIcon(ds9));
					else if(icon == 10)
						icon_sel.setIcon(new ImageIcon(ds10));
				}
			}
		}		
	}
}