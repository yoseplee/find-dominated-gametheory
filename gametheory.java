import java.util.ArrayList;

public class gametheory {
	
	public static void main(String[] args) {
		
		//create a gameMatrix
//		int[][] exampleData = {{2,0,4},{1,2,3},{4,1,2}};		
		int[][] p1Data = {{16,3,2,13},{5,10,11,8},{9,6,7,12},{4,15,14,1}};
		int[][] p2Data = {{13,29,8,12,16,23}, {18,22,21,22,29,31}, {18,22,31,31,27,37}, {11,22,12,21,21,26}, {18,16,19,14,19,28}, {23,22,19,23,30,34}};
		
//		GameMatrix example = new GameMatrix(exampleData);

		GameMatrix p1 = new GameMatrix(p1Data);
		GameMatrix p2 = new GameMatrix(p2Data);
		
		p1.isDominant();
		System.out.println(p1);
		
		p2.isDominant();
		System.out.println(p2);
		
	}
}

class GameMatrix {
	int nRows = 5;
	int nCols = 5;
	int[][] gameMatrix;
	int[][] dominationProcessedMatrix;
	int dominatedRowIndex;
	int dominatedColIndex;
	
	public GameMatrix() {
		//instantiate with default number of nRows, nCols
		this.gameMatrix = createGameMatrix(this.nRows, this.nCols);
	}
	
	public GameMatrix(int[][] data) {
		setnRows(data.length);
		setnCols(data[0].length);
		this.gameMatrix = createGameMatrix(nRows, nCols);
		setGameMatrix(data.clone());
		
		dominationProcessedMatrix = createGameMatrix(nRows, nCols);
		setDominantProcessedMatrix(data);
		dominatedRowIndex = nRows;
		dominatedColIndex = nCols;
		
	}
	
	public int[][] createGameMatrix(int nRows, int nCols) {
		return new int[nRows][nCols];
	}
	
	public void isDominant() {
		int iteration = nRows*50;
		for(int i=0; i<iteration; i++) {
			findDominamtCol();
			findDominantRow();
		}
	}
	
	public String judgeDominant() {
		
		//check if it has different schema of matrix
		int nDRows = dominationProcessedMatrix.length;
		int nDCols = dominationProcessedMatrix[0].length;
		
		String result = "cannot judge";
		
		if(nRows == 2 && nCols == 2) {
			//2x2 matrix
			result = "there is dominated row or column and adjusted to 2x2 matrix";
		}
		
		if((nRows==nDRows) && (nCols==nDCols)) {
			result = "there's no dominated row or column";
		} else {
			result = "there is dominated row or column but it's not enough to adjust to 2x2 matrix";
		}
		
		return result;
	}
	
	public void adjust(ArrayList<Integer> index, int isCol) {
		//0 is row
		//1 is col
		int nDRows = dominationProcessedMatrix.length;
		int nDCols = dominationProcessedMatrix[0].length;
		int deleteCount = index.size();
		int[][] adjusted;
		int adjustedRowCount = 0, adjustedColumnCount = 0;
		if(isCol == 0) {
			//adjust row
			int adjustedRows = nDRows-deleteCount;
			int adjustedCols = nDCols;
			
			adjusted = new int[adjustedRows][nDCols];
			//hard copy
			for(int row=0; row<nDRows; row++) {
				if(index.contains(row)) continue; //skip dominated row
				for(int col=0; col<nDCols; col++) {
					adjusted[adjustedRowCount][adjustedColumnCount++] = dominationProcessedMatrix[row][col];
				}
				adjustedRowCount++;
				adjustedColumnCount = 0;
			}
		} else {
			//adjust col
			int adjustedRows = nDRows;
			int adjustedCols = nDCols-deleteCount;
			
			adjusted = new int[adjustedRows][adjustedCols];
			//hard copy
			for(int col=0; col<nDCols; col++) {
				if(index.contains(col)) continue; //skip dominated row
				for(int row=0; row<nDRows; row++) {
					adjusted[adjustedRowCount++][adjustedColumnCount] = dominationProcessedMatrix[row][col];
				}
				adjustedColumnCount++;
				adjustedRowCount=0;
			}
		}
		
		//assign
		this.dominationProcessedMatrix = adjusted;
	}
	
	public void findDominantRow() {
		
		int nDRows = dominationProcessedMatrix.length;
		ArrayList<Integer> dominantedIndecies = new ArrayList<Integer>();
		
		//let this matrix be processed
		for(int row=0; row<nDRows; row++) {
			for(int start=row+1; start<nDRows; start++) {
				boolean dominantFlag = rowsCompare(dominationProcessedMatrix[row], dominationProcessedMatrix[start]);
//				System.out.println("ROW:: "+row+"|"+start+"|"+dominantFlag);
				if(dominantFlag) {
					//set all 0 to the row that have little value
					int aValueOfThisRow = dominationProcessedMatrix[row][0];
					int aValueOfStartRow = dominationProcessedMatrix[start][0];
					if(aValueOfThisRow > aValueOfStartRow) {
						if(dominantedIndecies.contains(start)) continue;
						dominantedIndecies.add(start);
					}
					else {
						if(dominantedIndecies.contains(row)) continue;
						dominantedIndecies.add(row);
					}
				}
			}
		}
		adjust(dominantedIndecies, 0);
	}
	
	public void findDominamtCol() {
		
		int nDCols = dominationProcessedMatrix[0].length;
		ArrayList<Integer> dominantedIndecies = new ArrayList<Integer>();
		
		//let this matrix be processed
		for(int col=0; col<nDCols; col++) {
			for(int start=col+1; start<nDCols; start++) {
				int[] thisCol = transpose(dominationProcessedMatrix, col);
				int[] startCol = transpose(dominationProcessedMatrix, start);
				boolean dominantFlag = rowsCompare(thisCol, startCol);
//				System.out.println("COL:: "+col+"|"+start+"|"+dominantFlag);
				if(dominantFlag) {
					//set all 0 to the column that have bigger value
					if(thisCol[0] > startCol[0]) {
						if(dominantedIndecies.contains(col)) continue;
						dominantedIndecies.add(col);
					}
					else {
						if(dominantedIndecies.contains(start)) continue;
						dominantedIndecies.add(start);
					}
				}
			}
		}
		adjust(dominantedIndecies, 1);
	}
	
	public int[] transpose(int[][] data, int col) {
		//transpose vertical to horizon, preventing waste of computing power
		int nRows = data.length;
		int nCols = data[0].length;
		int[] transposed = new int[nRows];
		
		for(int i=0; i<nRows; i++) {
			transposed[i] = data[i][col];
		}
		return transposed;
	}
	
	public boolean rowsCompare(int[] from, int[] to) {
		//assume that those two parameter has the same length
		int length = from.length;
		if(length != to.length) return false;
	
		//do compare
		boolean consistentFlag = true;
		for(int i=0; i<length; i++) {
			boolean thisRound = (from[i] <= to[i]);
			if(i==0) consistentFlag = thisRound;
			if(consistentFlag != thisRound) return false;
		}
		
		//either from or to is a dominant row
		return true;
	}
	
	public String toString() {
		String retString = "";
		
		retString += "\nthe number of rows: " + nRows;
		retString += "\nthe number of columns: " + nCols;
		retString += "\n\n=======GAME MATRIX=======\n";
		for(int i=0; i<nRows; i++) {
			retString += String.format("\n%dth | ", i);
			for(int j=0; j<nCols; j++) {
				retString += String.format("%3d ", gameMatrix[i][j]); 
			}
		}
		if(dominationProcessedMatrix != null) {
			int nDRows = dominationProcessedMatrix.length;
			int nDCols = dominationProcessedMatrix[0].length;
			retString += "\n\n=======DOMINATION MATRIX=======\n";
			retString += judgeDominant();
			for(int i=0; i<nDRows; i++) {
				retString += String.format("\n%dth | ", i);
				for(int j=0; j<nDCols; j++) {
					retString += String.format("%3d ", dominationProcessedMatrix[i][j]); 
				}
			}
		}
		return retString;
	}
	
	public int[][] getGameMatrix() {
		return this.gameMatrix;
	}
	
	public void setGameMatrix(int[][] data) {
		this.gameMatrix = data;
	}
	
	public void setDominantProcessedMatrix(int[][] data) {
		//hard copy
		for(int row=0; row<nRows; row++) {
			for(int col=0; col<nCols; col++) {
				this.dominationProcessedMatrix[row][col] = data[row][col];
			}
		}
	}
	
	public void setnRows(int nRows) {
		this.nRows = nRows;
	}
	
	public void setnCols(int nCols) {
		this.nCols = nCols;
	}
}
