package com.example.tictactoe;

import java.io.Serializable;
import java.util.Random;

public class Game implements Serializable {
    private boolean versusAI;
    private int playerNum;
    private int turnsCount;
    private int lastRow,lastCol;
    private Map map;
    private char curDot;
    private int markForWin;
    private boolean isEnd;
    private int winnerPlayerNum;
    final Random random = new Random();

    public int getWinnerPlayerNum() {
        return winnerPlayerNum;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public Game(boolean versusAI, Map map, int markForWin) {
        this.versusAI = versusAI;
        this.playerNum=1;
        this.turnsCount=0;
        this.lastRow=0;
        this.lastCol=0;
        this.map=map;
        this.curDot=Map.DOT_X;
        this.markForWin=markForWin;
        this.winnerPlayerNum=-1;
    }

    private void changePlayer(){
        if (playerNum==1) {
            playerNum=2;
            curDot=Map.DOT_0;
        } else{
            playerNum=1;
            curDot=Map.DOT_X;
        }
    }

    public char getCurDot(){
        return curDot;
    }

    public boolean playerTurn(int row, int col){
        if (map.setDot(row, col, curDot)) {
            endTurn(row, col);

            return true;
        } else{
            return false;
        }
    }

    private void endTurn(int row, int col) {
        turnsCount++;
        lastRow=row;
        lastCol=col;

        isEnd=checkEnd(curDot);
        if (!isEnd) {
            changePlayer();
        }
    }

    private boolean checkEnd(char symbol) {
        if (checkWin(symbol)) {
            winnerPlayerNum=playerNum;
            isEnd=true;
        }
        if (isMapFull()) {
            isEnd=true;
            winnerPlayerNum=-1;
        }

        return isEnd;
    }

    private boolean checkWin(char symbol) {
        if (checkHorizontal(symbol)) return true;
        if (checkVertical(symbol)) return true;
        if (checkMainDiagonal(symbol)) return true;
        if (checkSecondDiagonal(symbol)) return true;

        return false;
    }

    private boolean checkSecondDiagonal(char symbol) {
        int startRow=lastRow;
        int startCol=lastCol;
        while ((startRow<map.getSize()-1)&&(startCol>0)) {
            startRow++;
            startCol--;
        }

        int num=0;
        int curRow=startRow;
        int curCol=startCol;
        do {
            if (map.getDot(curRow,curCol)==symbol) {
                num++;
                if (num == markForWin) return true;
            } else
                num=0;

            curRow--;
            curCol++;
        } while (curCol<map.getSize() && curRow>=0);

        return false;
    }

    private boolean checkMainDiagonal(char symbol) {
        int startRow=lastRow;
        int startCol=lastCol;
        while (startCol>0 && startRow>0) {
            startRow=startRow-1;
            startCol=startCol-1;
        }

        int num=0;
        int curRow=startRow;
        int curCol=startCol;
        do {
            if (map.getDot(curRow,curCol)==symbol) {
                num++;
                if (num == markForWin) return true;
            } else
                num=0;

            curRow++;
            curCol++;
        } while (curRow<map.getSize() && curCol<map.getSize());

        return false;
    }

    private boolean checkVertical(char symbol) {
        int num=0;
        for (int i = 0; i < map.getSize(); i++) {
            if (map.getDot(i,lastCol)==symbol) {
                num++;
                if (num == markForWin) return true;
            } else
                num=0;
        }

        return false;
    }

    private boolean checkHorizontal(char symbol) {
        int num=0;
        for (int i = 0; i < map.getSize(); i++) {
            if (map.getDot(lastRow,i)==symbol) {
                num++;
                if (num == markForWin) return true;
            } else
                num=0;
        }

        return false;
    }

    private boolean isMapFull() {
        return turnsCount == map.getSize() * map.getSize();
    }

    private void initArrCell(int[][] arrCell) {
        for (int i = 0; i < arrCell.length; i++) {
            for (int j = 0; j < arrCell[i].length; j++) {
                arrCell[i][j]=-1;
            }
        }
    }

    private int getCellHorizontal(int[][] arrCell) {
        int[][] arrTmp = new int[map.getSize()][3];
        initArrCell(arrTmp);

        int prevCol=-1;
        int curWeight=0;
        int tmpRow=0;
        for (int i = 0; i < map.getSize(); i++) {
            if (map.getDot(lastRow,i)==Map.DOT_X) {
                curWeight++;

                if (prevCol!=-1 && i==map.getSize()-1) {
                    arrTmp[tmpRow][0]=curWeight;
                    arrTmp[tmpRow][1]=lastRow;
                    arrTmp[tmpRow][2]=prevCol;
                    tmpRow++;
                }
            }

            if (map.getDot(lastRow,i)==Map.DOT_EMPTY) {
                if (curWeight>0) {
                    arrTmp[tmpRow][0]=curWeight;
                    arrTmp[tmpRow][1]=lastRow;
                    arrTmp[tmpRow][2]=i;
                    tmpRow++;

                    if (prevCol!=-1) {
                        arrTmp[tmpRow][0]=curWeight;
                        arrTmp[tmpRow][1]=lastRow;
                        arrTmp[tmpRow][2]=prevCol;
                        tmpRow++;
                    }
                }

                prevCol=i;
                curWeight=0;
            }

            if (map.getDot(lastRow,i)== Map.DOT_0) {
                if (prevCol!=-1 && curWeight>0){
                    arrTmp[tmpRow][0]=curWeight;
                    arrTmp[tmpRow][1]=lastRow;
                    arrTmp[tmpRow][2]=prevCol;
                    tmpRow++;
                }

                prevCol=-1;
                curWeight=0;
            }
        }

        int maxWeight = getMaxWeight(arrCell, arrTmp);

        return maxWeight;
    }

    private int getCellMainDiagonal(int[][] arrCell) {
        int startRow=lastRow;
        int startCol=lastCol;
        while (startCol>0 && startRow>0) {
            startRow=startRow-1;
            startCol=startCol-1;
        }

        int[][] arrTmp = new int[map.getSize()][3];
        initArrCell(arrTmp);

        int prevCol=-1;
        int prevRow=-1;
        int curWeight=0;
        int tmpRow=0;

        int curRow=startRow;
        int curCol=startCol;
        do {
            if (map.getDot(curRow,curCol)==Map.DOT_X) {
                curWeight++;

                if (prevRow!=-1 && prevCol!=-1 && curRow==map.getSize()-1 && curCol==map.getSize()-1) {
                    arrTmp[tmpRow][0]=curWeight;
                    arrTmp[tmpRow][1]=prevRow;
                    arrTmp[tmpRow][2]=prevCol;
                    tmpRow++;
                }
            }

            if (map.getDot(curRow,curCol)==Map.DOT_EMPTY) {
                if (curWeight>0) {
                    arrTmp[tmpRow][0]=curWeight;
                    arrTmp[tmpRow][1]=curRow;
                    arrTmp[tmpRow][2]=curCol;
                    tmpRow++;

                    if (prevRow!=-1 && prevCol!=-1) {
                        arrTmp[tmpRow][0]=curWeight;
                        arrTmp[tmpRow][1]=prevRow;
                        arrTmp[tmpRow][2]=prevCol;
                        tmpRow++;
                    }
                }

                prevRow=curRow;
                prevCol=curCol;
                curWeight=0;
            }

            if (map.getDot(curRow,curCol)==Map.DOT_0) {
                if (prevRow!=-1 && prevCol!=-1 && curWeight>0){
                    arrTmp[tmpRow][0]=curWeight;
                    arrTmp[tmpRow][1]=prevRow;
                    arrTmp[tmpRow][2]=prevCol;
                    tmpRow++;
                }

                prevRow=-1;
                prevCol=-1;
                curWeight=0;
            }

            curRow++;
            curCol++;
        } while (curRow<map.getSize() && curCol<map.getSize());

        int maxWeight = getMaxWeight(arrCell, arrTmp);
        return maxWeight;
    }

    private int getCellVertical(int[][] arrCell) {
        int[][] arrTmp = new int[map.getSize()][3];
        initArrCell(arrTmp);

        int prevRow=-1;
        int curWeight=0;
        int tmpRow=0;

        for (int i = 0; i < map.getSize(); i++) {
            if (map.getDot(i,lastCol)==Map.DOT_X) {
                curWeight++;

                if (prevRow!=-1 && i==map.getSize()-1) {
                    arrTmp[tmpRow][0]=curWeight;
                    arrTmp[tmpRow][1]=prevRow;
                    arrTmp[tmpRow][2]=lastCol;
                    tmpRow++;
                }
            }

            if (map.getDot(i,lastCol)==Map.DOT_EMPTY) {
                if (curWeight>0) {
                    arrTmp[tmpRow][0]=curWeight;
                    arrTmp[tmpRow][1]=i;
                    arrTmp[tmpRow][2]=lastCol;
                    tmpRow++;

                    if (prevRow!=-1) {
                        arrTmp[tmpRow][0]=curWeight;
                        arrTmp[tmpRow][1]=prevRow;
                        arrTmp[tmpRow][2]=lastCol;
                        tmpRow++;
                    }
                }

                prevRow=i;
                curWeight=0;
            }

            if (map.getDot(i,lastCol)== Map.DOT_0) {
                if (prevRow!=-1 && curWeight>0){
                    arrTmp[tmpRow][0]=curWeight;
                    arrTmp[tmpRow][1]=prevRow;
                    arrTmp[tmpRow][2]=lastCol;
                    tmpRow++;
                }

                prevRow=-1;
                curWeight=0;
            }
        }

        int maxWeight = getMaxWeight(arrCell, arrTmp);

        return maxWeight;
    }


    private int getMaxWeight(int[][] arrCell, int[][] arrTmp) {
        int tmpRow;
        int maxWeight=0;
        for (int i = 0; i < arrTmp.length; i++) {
            if (arrTmp[i][0]>maxWeight) maxWeight= arrTmp[i][0];
        }

        tmpRow=0;
        for (int i = 0; i < arrTmp.length; i++) {
            if (arrTmp[i][0]==maxWeight){
                arrCell[tmpRow][0]= arrTmp[i][1];
                arrCell[tmpRow][1]= arrTmp[i][2];
                tmpRow++;
            }

            if (tmpRow==2) break;
        }

        return maxWeight;
    }

    private int getCellSecondDiagonal(int[][] arrCell) {
        int startRow=lastRow;
        int startCol=lastCol;
        while ((startRow<map.getSize()-1)&&(startCol>0)) {
            startRow++;
            startCol--;
        }

        int[][] arrTmp = new int[map.getSize()][3];
        initArrCell(arrTmp);

        int prevCol=-1;
        int prevRow=-1;
        int curWeight=0;
        int tmpRow=0;

        int curRow=startRow;
        int curCol=startCol;
        do {
            if (map.getDot(curRow,curCol)==Map.DOT_X) {
                curWeight++;

                if (prevRow!=-1 && prevCol!=-1 && curRow==0 && curCol==map.getSize()-1) {
                    arrTmp[tmpRow][0]=curWeight;
                    arrTmp[tmpRow][1]=prevRow;
                    arrTmp[tmpRow][2]=prevCol;
                    tmpRow++;
                }
            }

            if (map.getDot(curRow,curCol)==Map.DOT_EMPTY) {
                if (curWeight>0) {
                    arrTmp[tmpRow][0]=curWeight;
                    arrTmp[tmpRow][1]=curRow;
                    arrTmp[tmpRow][2]=curCol;
                    tmpRow++;

                    if (prevRow!=-1 && prevCol!=-1) {
                        arrTmp[tmpRow][0]=curWeight;
                        arrTmp[tmpRow][1]=prevRow;
                        arrTmp[tmpRow][2]=prevCol;
                        tmpRow++;
                    }
                }

                prevRow=curRow;
                prevCol=curCol;
                curWeight=0;
            }

            if (map.getDot(curRow,curCol)==Map.DOT_0) {
                if (prevRow!=-1 && prevCol!=-1 && curWeight>0){
                    arrTmp[tmpRow][0]=curWeight;
                    arrTmp[tmpRow][1]=prevRow;
                    arrTmp[tmpRow][2]=prevCol;
                    tmpRow++;
                }

                prevRow=-1;
                prevCol=-1;
                curWeight=0;
            }

            curRow--;
            curCol++;
        } while (curCol<map.getSize() && curRow>=0);

        int maxWeight = getMaxWeight(arrCell, arrTmp);
        return maxWeight;
    }

    public void aiTurn() {
        int rowNumber=-1;
        int colNumber=-1;

        int NUM_CELLS_VARIANT=8;

        int[][] arrAllCell = new int[NUM_CELLS_VARIANT][2];

        for (int i = 0; i <arrAllCell.length ; i++) {
            for (int j = 0; j < arrAllCell[i].length; j++) {
                arrAllCell[i][j]=-1;
            }
        }

        int[] arrWeight = new int[4];
        for (int i = 0; i < arrWeight.length; i++) {
            int[][] arrCell = new int[2][2];
            initArrCell(arrCell);

            if (i==0) arrWeight[i]=getCellHorizontal(arrCell);
            if (i==1) arrWeight[i]=getCellVertical(arrCell);
            if (i==2) arrWeight[i]=getCellMainDiagonal(arrCell);
            if (i==3) arrWeight[i]=getCellSecondDiagonal(arrCell);

            for (int j = 0; j < arrCell.length; j++) {
                for (int k = 0; k < arrCell[j].length; k++) {
                    arrAllCell[j+(i*2)][k]=arrCell[j][k];
                }
            }
        }

        int maxWeight=arrWeight[0];
        for (int i = 0; i < arrWeight.length; i++) {
            if (arrWeight[i]>maxWeight) maxWeight=arrWeight[i];
        }

        for (int i = 0; i < arrWeight.length; i++) {
            if (arrWeight[i]<maxWeight){
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 2; k++) {
                        arrAllCell[j+(i*2)][k]=-1;
                    }
                }
            }
        }

        boolean flIsCell=false;
        for (int i = 0; i < arrAllCell.length; i++) {
            if (arrAllCell[i][0]!=-1 && arrAllCell[i][1]!=-1) flIsCell=true;
        }

        do {
            if (!flIsCell) {
                rowNumber=random.nextInt(map.getSize());
                colNumber=random.nextInt(map.getSize());
            } else {
                do {
                    int variant=random.nextInt(NUM_CELLS_VARIANT);

                    rowNumber = arrAllCell[variant][0];
                    colNumber = arrAllCell[variant][1];
                } while (rowNumber==-1 && colNumber ==-1);
            }
        } while (!isCellFree(rowNumber, colNumber));

        map.setDot(rowNumber,colNumber,curDot);
        endTurn(rowNumber, colNumber);
    }

    private boolean isCellFree(int rowNumber, int colNumber) {
        return map.getDot(rowNumber,colNumber) == Map.DOT_EMPTY;
    }
}
