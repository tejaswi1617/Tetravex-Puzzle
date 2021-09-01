

import java.io.*;

import java.util.*;


public class Main {
			 
			//This method is taken from Asssignment-1 Main.java file
			private static String getEndingString(Scanner userInput ) {
				String userArgument = null;

				userArgument = userInput.nextLine();
				userArgument = userArgument.trim();


				if (userArgument.equalsIgnoreCase("empty")) {
					userArgument = "";
				} else if (userArgument.equalsIgnoreCase("null")) {
					userArgument = null;
				}

				return userArgument;
			}
			
			public static void main(String args[]) throws Exception {
				String PuzzleReadCommand = "readPuzzle";
				String placeCommand = "placePiece";
				String solveCommand = "solve";
				String printCommand="print";
				String choiceCommand="choices";
				String newCommand="newPuzzle";
				String quitCommand="quit";
				System.out.println("Commands available:");
				System.out.println("  " + PuzzleReadCommand + " <File name>" );
				System.out.println("  " + placeCommand);
				System.out.println("  " + solveCommand );
				System.out.println("  " + printCommand);
				System.out.println("  " + choiceCommand);
				System.out.println("  " + newCommand);
				System.out.println("  " + quitCommand);
				String UserCommand="";
				Scanner userInput=new Scanner(System.in);
				String Command;
				Tetravex itemToHandle=new Tetravex();
				do {
					UserCommand=userInput.next();
					
					//load puzzle pieces 
					if(UserCommand.equalsIgnoreCase(PuzzleReadCommand)) {
						Command= getEndingString( userInput );
						
						try {
							boolean result=itemToHandle.loadPuzzle(new BufferedReader(new FileReader(Command)));
							System.out.println(result);
						}
						catch(Exception e) {
							System.out.println("provide valid file");
						}
					}
					
					//place the piece at given x,y in the puzzle grid
					else if(UserCommand.equalsIgnoreCase(placeCommand))
					{
						Scanner sc=new Scanner(System.in);
						int pieceword=0,col=0,row=0;
						System.out.println("enter pieceword:");
						pieceword=sc.nextInt();
						System.out.println("enter coloum:");
						col=sc.nextInt();
						System.out.println("enter row:");
						row=sc.nextInt();
						try {
							boolean result=itemToHandle.placePiece(pieceword,col,row);
							System.out.println(result);
						}catch(Exception ex) {
							System.out.println(ex);
						}
						
					}
					//print puzzle grid
					else if(UserCommand.equalsIgnoreCase(printCommand)) {
						String result=itemToHandle.print();
						System.out.println(result);
						
					}
					//call solve to solve puzzle 
					else if(UserCommand.equalsIgnoreCase(solveCommand)) {
						boolean result=itemToHandle.solve();
						System.out.println(result);
						
					}
					//create new insrance for new puzzle data
					else if(UserCommand.equalsIgnoreCase(newCommand)){
						itemToHandle=new Tetravex();
						System.out.println("new puzzle is created");
					}
					//ask bad choices has been made while solving problem
					else if(UserCommand.equalsIgnoreCase(choiceCommand)){
						int result=itemToHandle.Choices();
						System.out.println("Number of bad choices:"+result);
					}
					//quit the game
					else if(UserCommand.equalsIgnoreCase(quitCommand)) {
						System.out.println("exit from the Systsem");
					}
					else {
						System.out.println("Bad Command");
					}
			
				}while(!UserCommand.equalsIgnoreCase(quitCommand));
			}
			
}
