import javax.swing.*;

/// Entry point to program.
/**
 * This class acts as the entry point to the program and is responsible for parsing command line arguments
 * and setting up the GameController.  Run the program with the --help command-line parameter for more
 * information.
 * 
 */
public class Main
{
	/// Prints the commandline instructions.
	public static void helpPrinter()
	{
		System.out.println("  Command Line Parameters are as follows:");
		System.out.println("    \"--help\" : You're looking at it");
		System.out.println("    \"-p1 [AI Class Name]\" : Set player 1 to the appropriate AI");
		System.out.println("      Example: -p1 minimax");
		System.out.println("    \"-p2 [AI Class Name]\" : Set player 2 to the appropriate AI");
		System.out.println("      Example: -p2 minimax");
	}

	/// Program startup function.
	public static void main(String[] args)
	{
		final AIModule[] players = new AIModule[2];

		// Default max ai time is 500 ms
		int AI_time = 500;

		// Parse through the command line arguements
		try
		{
			int i = 0;
			while(i < args.length)
			{
				if(args[i].equalsIgnoreCase("-p1"))
					players[0] = (AIModule) Class.forName(args[i + 1]).getDeclaredConstructor().newInstance();
				else if(args[i].equalsIgnoreCase("-p2"))
					players[1] = (AIModule) Class.forName(args[i + 1]).getDeclaredConstructor().newInstance();
				else if(args[i].equalsIgnoreCase("--help"))
				{
					helpPrinter();
					System.exit(0);
				}
				else
					throw new IllegalArgumentException();
				i += 2;
			}
		}
		catch(ClassNotFoundException cnf)
		{
			System.err.println("Player Not Found: " + cnf.getMessage());
			System.exit(1);
		}
		catch(IndexOutOfBoundsException ioob)
		{
			System.err.println("Invalid Arguments");
			System.exit(2);
		}
		catch(NumberFormatException e)
		{
			System.err.println("Invalid Integer: " + e.getMessage());
			System.exit(3);
		}
		catch(IllegalArgumentException ia)
		{
			System.err.println("Invalid Arguments: " + ia.getMessage());
			System.exit(4);
		}
		catch(Exception e)
		{
			System.err.println("Unknown Error");
			System.exit(5);
		}

		// Create a new game
		GameStateModule game = new GameState_General();

		IOModule io;
		final Display display = new Display();
		io = display;
		final JFrame frame = new JFrame("Connect Four");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(display);
		frame.pack();
		frame.setVisible(true);
		
		// Turn on the turn based system
		GameController controller = new GameController(game, io, players, AI_time);
		controller.play();
		// Print out the results of the match
		if(game.getWinner() == 0)
			System.out.println("Draw Game");
		else
			System.out.println("Player " + game.getWinner() + " won");
	}
}