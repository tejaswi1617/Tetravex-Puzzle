

import java.io.BufferedReader;


import java.io.IOException;
import  java.util.*;


public class Tetravex {

	private LinkedHashMap<Integer, Edges> puzzleData; //To store puzzle Pieces
	private ArrayList<Integer> piecesNames; //To store pieces name from the puzzleData
	private ArrayList<Integer> placedPieces; // keep track of pieces that are already placed in puzzle grid
	private static int badChoices; //count number of bad choices has been made
	private int[][] puzzleGrid; // puzzle grid of n X m
	private int col,row; //veriable for grid height and width
	
	//constructor to initialize objects and variables
	public Tetravex() {
		puzzleData=new LinkedHashMap<Integer, Edges>();
		piecesNames=new ArrayList<Integer>();
		placedPieces=new ArrayList<Integer>();
		badChoices=0;
		col=0;
		row=0;
		
	}
	
	public boolean loadPuzzle(BufferedReader stream) throws Exception {
		//already pieces for puzzle is loaded (create new instance for loading different file)
		if(puzzleData.size()!=0) {
			return false;
		}
		String line="";
		//read first data from the stream 
		line=stream.readLine();
		
		//will not accept empty or null value of stream
		if(line==null || line.isEmpty()) {
			throw new Exception();
		}
		
		//split first line data that contains height and width of the puzzle grid by space 
		String[] gridColRow=line.split("\\s+");
		
		//if user has provided more data more than height nd width will stop loading puzzle
		if(gridColRow.length!=2) {
			return false;
		}
		else {
			//convert hight and width in form of string  to integer
			col=Integer.parseInt(gridColRow[0]);
			row=Integer.parseInt(gridColRow[1]);
			
			//program only excepts grid of size at least 2 X 2
			if(col<2 && row <2) {
				return false;
			}
			//initialize grid 
			puzzleGrid=new int[row][col];
			
			//read data line-by-line untill get null value to read 
			while((line=stream.readLine())!=null) {
				
				//split puzzle piece name and its edges by space
				String[] edge=line.split("\\s+");
				
				//validate pieces's parameters(name, 4-edges)
				if(edge.length!=5) {
					return false;
				}
				
				//get 1st value as piece-name 
				int key=Integer.parseInt(edge[0]);
				if(!puzzleData.containsKey(key)) {
					//store 1st value of line as piece-name to likedHashMap
					puzzleData.put(key, null);
					
					//create edges of current pieces and store as value to the given piece name 
					int topEdge=Integer.parseInt(edge[1]);
					int leftEdge=Integer.parseInt(edge[2]);
					int bottomEdge=Integer.parseInt(edge[3]);
					int rightEdge=Integer.parseInt(edge[4]);
					Edges values=new Edges(topEdge,leftEdge, bottomEdge, rightEdge);
					
					//store object contains current piece(key) edges(value) to puzzleData
					puzzleData.replace(key, values);
				}else {
					//stop accepting further pieces if duplicate piece name encounters
					puzzleData.clear();
					return false;
				}
			}
		}
		
		//number of pieces must be equals to number of cells in puzzle grid
		if(puzzleData.size()==(col*row)) {
			//call method to retrive all the piece-name stores as key in puzzleData for furthure use in program
			getPuzzlePieces();
			
			return true;
		}else {
			puzzleData.clear();
			return false;
		}
		
	}
	
	private void getPuzzlePieces() {
		for(Map.Entry<Integer, Edges> key:puzzleData.entrySet()) 
		{
			//store all the keys stored in puzzleData
			piecesNames.add(key.getKey());
		}
	}
	
	
	public boolean solve() {
		//return back to main if called before loadPuzzle
		if(puzzleData.size()==0) {
			return false;
		}
		boolean result=false;
		
		//Chceck grid is filled by user or not
		if(placedPieces.isEmpty()) {
			
			//grid is empty
			//take one-by-one piece from the puzzleData
			
			for(int i=0;i<piecesNames.size();i++) {
					//store piece at positon 0,0 in the grid as grid is empty will surely set 
					puzzleGrid[0][0]=piecesNames.get(i);
					
					//mark piece stored in grid
					placedPieces.add(piecesNames.get(i));
					
					//call method to solve rest of the puzzle by filling unfilled cells
					result=supportSolve(0,1);
					
					
					if(result==true) {
						//puzzle is solved 
						return true;
					}
					
					//puzzle is not solved 
					//count 0,0 as wrong piece for 0,0 position
					badChoices++;
					//remove current piece from the puzzleGrid and placedPieces
					removeElement(piecesNames.get(i),0,0);
			}
			//puzzle can not be solved with given pieces
			return false;
		}else {
			
			//grid is filled by user
			result=supportSolve(0,0);
			if(result==true) {
				//puzzle is solved		
				return true;
			}else {
				//puzzle is not solved
				return false;
			}
		}
		
	}
	
	private boolean supportSolve(int rows,int cols) {
		
		//all the pieces are set in the puzzle grid and puzzle is solved
		if(placedPieces.size()==puzzleData.size()) {
			return true;
		}
		
		
		int result=0;
		
		//Take one-by-one pieces of puzzle to set in the puzzle grid at current location of the grid
		for(int k=0;k<piecesNames.size();k++) {
			
			//current pieces can not be set as its already present in the puzzle grid at any location 
			if(!placedPieces.contains(piecesNames.get(k))) {
				
				//current position is filled by user
				if(puzzleGrid[rows][cols]!=0) {
					
					if(cols<col-1) {
						//fill current row by incrementing column
						//recursively call method to fill next position
						boolean resul=supportSolve(rows,cols+1);
						//backtracking
						if(resul==true) {
							//puzzle is solved 
							return true;
						}else {
							//puzzle is not solved
							return false;
						}							
					}else {	
						
						//current row in puzzle grid is filled, now fill next row
						boolean resul=supportSolve(rows+1,0);
						
						//backtracking
						if(resul==true) {
							//puzzle is solved
							return true;
						}else {
							//puzzle is not solved
							return false;
						}					
					}
				}
				//current position is empty need to fill by solve
				else {
					
					//check whether current piece can be set at current location
					result=placePieceSupport(piecesNames.get(k),cols,rows);
					
					//piece can be set at current location
					if(result==0) {
						
						//set current piece at given locaton
						puzzleGrid[rows][cols]=piecesNames.get(k);
						
						//mark piece as already present in puzzle grid
						placedPieces.add(piecesNames.get(k));
						
						//fill current row by incrementing column
						if(cols<col-1) {
							
							//recursively call method to fill next position
							boolean resul=supportSolve(rows,cols+1);
							if(resul==true) {
								//puzzle is solved
								return true;
							}else {
								//puzzle is not solved mark current piece is bad choice
								badChoices++;
								//remove current piece from puzzleGrid and placedPieces
								removeElement(puzzleGrid[rows][cols],cols,rows);
							}							
						}else {
							//current row in puzzle grid is filled, now fill next row
							boolean resul=supportSolve(rows+1,0);
							if(resul==true) {
								//puzzle is solved
								return true;
							}else {
								//puzzle is not solved mark current piece is bad choice
								badChoices++;
								//remove current piece from puzzleGrid and placedPieces
								removeElement(puzzleGrid[rows][cols],cols,rows);
							}					
						}
					}
				}
				
			}	
		}
		//no single piece can be set at current position in grid
		return false;
	}
	
	//method to remove piece from the puzzleGrid and placedPieces
	private void removeElement(int piece,int cols,int rows) {
		int index=placedPieces.indexOf(piece);
		placedPieces.remove(index);
		puzzleGrid[rows][cols]=0;
	}
	
	
	private int placePieceSupport(int pieceName, int xPosition, int yPosition) {
		int flag=0;
		//piecesName is present in puzzleData or not
		if(puzzleData.containsKey(pieceName)) {
			//get edges of current piece from puzzleData
			Edges piecename=puzzleData.get(pieceName);
			
			//given positions must be valid and no piece is present at the given location in puzzle grid
			if((xPosition>=0 && xPosition<col) && (yPosition>=0 && yPosition<row) && (puzzleGrid[yPosition][xPosition]==0)){
				
				int newleft=xPosition-1; //to match  left piece's edge with current piece if possible
				int newright=xPosition+1; //to match  right piece's edge with current piece if possible
				int newTop=yPosition-1; //to match  top piece's edge with current piece if possible
				int newBottom=yPosition+1; //to match  bottom piece's edge with current piece if possible
				
				//puzzleWord1- store egdes of left piece
				//puzzleWord2- store egdes of right piece
				//puzzleWord3- store egdes of top piece
				//puzzleWord4- store egdes of bottom piece
				Edges puzzleWord1=null,puzzleWord2=null,puzzleWord3=null, puzzleWord4=null;
				
				if(newleft>=0) {
					if(puzzleGrid[yPosition][newleft]!=0) {
						//gives egdes of piece placed at left cell  of current piece
						puzzleWord1=puzzleData.get(puzzleGrid[yPosition][newleft]);
					}	
				}
				if(newright<col) {
					if(puzzleGrid[yPosition][newright]!=0) {
						//gives egdes of piece placed at right cell of current piece
						puzzleWord2=puzzleData.get(puzzleGrid[yPosition][newright]);					
					}
				}
				if(newTop>=0) {
					if(puzzleGrid[newTop][xPosition]!=0) {
						//gives egdes of piece placed at top cell of current piece
						puzzleWord3=puzzleData.get(puzzleGrid[newTop][xPosition]);
					}
				}
				if(newBottom<row){
					if(puzzleGrid[newBottom][xPosition]!=0) {
						//gives egdes of piece placed at bottom cell of current piece
						puzzleWord4=puzzleData.get(puzzleGrid[newBottom][xPosition]);
					}
				}
				
				//check if puzzleGrid has element for comparision
				
				if(puzzleWord1!=null) {
					//match left piece's right-edge value with current piece left-edge value
					if(puzzleWord1.rightEdge!=piecename.leftEdge) {
						flag=1;
					}
				}
				if(puzzleWord2!=null) {
					//match right piece's left-edge value with current piece right-edge value
					if(puzzleWord2.leftEdge!=piecename.rightEdge) {
						flag=1;
					}
				}
				if( puzzleWord3!=null) {
					//match top piece's bottom-edge value with current piece top-edge value
					if(puzzleWord3.bottomEdge!=piecename.topEdge)
					{
						flag=1;
					}
				}
				if(puzzleWord4!=null) {
					//match bottom piece's top-edge value with current piece bottom-edge value
					if(puzzleWord4.topEdge!=piecename.bottomEdge) {
						flag=1;
					}
				}
			}
			else {
				flag=1;
			}
		}else {
			flag=1;
		}
		
		//return 1 if piece can not me places at current position as it has not proper edge or edges matching
		//return 0 if piece can be placed at current position and has proper egde or edges matching 
		return flag;
	}
	
	
	public boolean placePiece( int pieceName, int xPosition, int yPosition ) {
		//call method to check the piece can be set at current location or not by matching its egdes with neighbour pieces if places
		int result=placePieceSupport(pieceName, xPosition,yPosition);
		
				if(result==0) {
					//the piece can be set at current position and placed in puzzle Grid
					puzzleGrid[yPosition][xPosition]=pieceName;
					//mark the piece as stored in puzzle grid 
					placedPieces.add(pieceName);
					return true;
				}else {
					//the piece can't bet set and placed in puzzle Grid
					return false;
				}
	}
	
	public String print() {
		
		String placed="";
		//class doesn't have data to print-called without loading puzzle pieces
		if(puzzleData.size()==0) {
			return null;
		}
		
		for(int i=0;i<row;i++) {
			for(int j=0;j<col;j++) {
				if(puzzleGrid[i][j]==0) {
					//current cell is empty
					placed+="xxx"+"\t";
				}else {
					//current cell has piece to print
					placed+=puzzleGrid[i][j]+"\t";
					
				}
			}
			placed+="\n";
		}
		//will separate used and unused pieces 
		placed+="--------------------"+"\n";
		
		//concatenate unused piece name and its edges as string 
		for(int i=0;i<piecesNames.size();i++) {
			if(!placedPieces.contains(piecesNames.get(i))) {
				int key=piecesNames.get(i);
				Edges obj=puzzleData.get(key);
				placed+=piecesNames.get(i)+"\t"+obj.topEdge+"\t"+obj.rightEdge+"\t"+obj.bottomEdge+"\t"+obj.leftEdge+"\n";
			}
		}
		return placed;
		
	}
	
	//method returns number of bad choices program has made to solve puzzle
	public int Choices() {
		return badChoices;
	}
}
