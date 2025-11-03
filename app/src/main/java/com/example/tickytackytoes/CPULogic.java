package com.example.tickytackytoes;

import java.util.Arrays;

public class CPULogic {

    static int gameDifficulty;
    static boolean validMove;

    static int[][] gameBoardDeepCopy;
    static int[][] cpuPastMoves;

    static int[] cpuLastMove;
    static int cpuMoveCount = 0;

    CPULogic () {
        // set cpu logic defaults onCreate
        gameDifficulty = PlayerSetup.getDifficulty();
        validMove = false;

        // may change to switch case and only do initial copy of board if on hard mode
        if(gameDifficulty == 1 || gameDifficulty == 2) {
            cpuPastMoves = new int[3][3]; // length only needs to be 4 or 5 but 9 feels better :)
            cpuLastMove = new int[2]; // only ever needs to hold 2 numbers - the last chosen row and col
            // deep copy of board on initialize CPULogic
            gameBoardDeepCopy = deepCopyBoard(GameLogic.getGameboard());
        }
    }

/// HELPERS
    private static int pickRow() {
        return (int)(Math.random() * 3) + 1;
    };
    private static int pickCol() {
        return (int)(Math.random() * 3) + 1;
    };

    private static int[][] deepCopyBoard(int[][] original) {
        if (original == null) return null;

        int rows = original.length;
        int cols = original[0].length;

        int[][] copy = new int[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                copy[r][c] = original[r][c]; // copy each element individually
            }
        }

        return copy;
    }

    private static boolean wouldWin(int row, int col) {
        // check move against deepCopy of board (updated every turn) using stripped down GameLogic.winnerCheck() logic
        int[][] boardCopyTemp = deepCopyBoard(gameBoardDeepCopy);

        if (boardCopyTemp[row - 1][col - 1] == 0) boardCopyTemp[row - 1][col - 1] = GameLogic.getPlayer();
        else return false;


        //horizontal check
        for (int r=row; r<3; r++) {
            if (boardCopyTemp[r][0] == boardCopyTemp[r][1] && boardCopyTemp[r][0] == boardCopyTemp[r][2] && boardCopyTemp[r][0] != 0) {
                return true;
            }
        }
        //vertical check
        for (int c=col; c<3; c++) {
            if (boardCopyTemp[0][c] == boardCopyTemp[1][c] && boardCopyTemp[0][c] == boardCopyTemp[2][c] && boardCopyTemp[0][c] != 0) {
                return true;
            }
        }
        //pos slope check
        if (boardCopyTemp[2][0] == boardCopyTemp[1][1] && boardCopyTemp[2][0] == boardCopyTemp[0][2] && boardCopyTemp[2][0] != 0) {
            return true;
        }
        //neg slope check
        if (boardCopyTemp[0][0] == boardCopyTemp[1][1] && boardCopyTemp[0][0] == boardCopyTemp[2][2] && boardCopyTemp[0][0] != 0) {
            return true;
        }
        return false;
    }

/// DIFFICULTY LOGIC FUNCTIONS

    private static int[] easyMove() { return new int[] {pickRow(), pickCol()}; }

    private static int[] mediumMove() {
        // if pastMoves.length != 0
        if (cpuMoveCount != 0) {
            // check around own moves for potential win
            int tempR = 0;
            if(cpuLastMove[0] > 1) tempR = cpuLastMove[0] - 1;
            else tempR = cpuLastMove[0];

            int tempC;
            if(cpuLastMove[1] > 1) tempC = cpuLastMove[1] - 1;
            else tempC = cpuLastMove[1];

            if(cpuMoveCount > 1) {

                // iterate through deep copy checking for win scenario
                for (int r = tempR; r < gameBoardDeepCopy.length && r < cpuLastMove[0] + 2; r++) {
                    for (int c = tempC; c < gameBoardDeepCopy[r].length && c < cpuLastMove[1] + 2; c++) {
                        if(wouldWin(r,c)) return new int[] {r,c};
                    }
                }
            }

            for (int r = tempR; r < gameBoardDeepCopy.length && r < cpuLastMove[0] + 2; r++) {
                for (int c = tempC; c < gameBoardDeepCopy[r].length && c < cpuLastMove[1] + 2; c++) {
                    // iterate through deep copy checking for lastMove openNeighbors
                    int[] tmp = new int[] {r,c};

                    if (GameLogic.getGameboard()[r - 1][c - 1] == 0 && !Arrays.equals(cpuLastMove, tmp)) return new int[] {r,c};
                }
            }

        }

        // else == cpu first move || default option all other ifs failed
        cpuMoveCount++;
        return easyMove(); // else => call easyMove() for random placement
    }

    private static int[] hardMove () {
        int[] move = new int[2];

        // if pastMoves().length != 0

            // if board has taken squares
                // find player markers && block potential playerWin

            // else if .length >= 2
                // check around own moves for potential win

            // else
                // place marker near last move

        // else == cpu first move

            // check board for any moves played
                // place marker near player last move

            // else => call easyMove() for random placement

        return move;
    }

/// MAIN CPU-MOVE FUNCTION
    public static int[] cpuMove(GameLogic game, boolean winningLine) {
        int row = 4;
        int col = 4;
        boolean validMove = false;
        gameBoardDeepCopy = deepCopyBoard(GameLogic.getGameboard());

        // cpuWin == false
        int cpuWin = 0;
        int[] moveChosen = new int[2];

        if(!winningLine) {
            winningLine = game.winnerCheck();

            while(!validMove) {

                switch (gameDifficulty){
                    case 1:
                        //medium
                        moveChosen = mediumMove();
                        break;
                    case 2:
                        //hard
                        moveChosen = hardMove();
                        break;
                    case 0://easy
                    default:
                        moveChosen = easyMove();
                        break;
                }

                row = moveChosen[0];
                col = moveChosen[1];

                validMove = game.updateGameBoard(row, col);
                winningLine = game.winnerCheck();

                if(winningLine) {
                    // sets cpuWin to 1 to indicate TRUE
                    cpuWin = 1;
                }
            }

            // alternate between players
            if (GameLogic.getPlayer() % 2 == 0) {
                game.setPlayer(GameLogic.getPlayer()-1);
            } else {
                game.setPlayer(GameLogic.getPlayer()+1);
            }
        }

        cpuLastMove = moveChosen;
        cpuPastMoves[cpuPastMoves.length - 1] = moveChosen;

        // pass back to touchEvent in TicTacToeBoard where the touch is happening [row, col] and whether or not it triggers a win condition (0/false or 1/true)
        return new int[]{row, col, cpuWin};
    }
}