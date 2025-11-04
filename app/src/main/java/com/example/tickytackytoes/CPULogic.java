package com.example.tickytackytoes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CPULogic {

    static int gameDifficulty;
    static boolean validMove;

    static int[][] gameBoardDeepCopy;
    static List<int[]> cpuPastMoves;

    static int[] cpuLastMove;

    CPULogic () {
        // set cpu logic defaults onCreate
        gameDifficulty = PlayerSetup.getDifficulty();
        validMove = false;

        // may change to switch case and only do initial copy of board if on hard mode
        if(gameDifficulty == 1 || gameDifficulty == 2) {
            cpuPastMoves = new ArrayList<>();
            cpuLastMove = new int[2]; // only ever needs to hold 2 numbers - the last chosen row and col
            // deep copy of board on initialize CPULogic
            gameBoardDeepCopy = deepCopyBoard(GameLogic.getGameboard());
        }
    }

/// HELPERS
    private static int pickRow() {
        return (int)(Math.random() * 3) + 1;
    }
    private static int pickCol() {
        return (int)(Math.random() * 3) + 1;
    }

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
            if (boardCopyTemp[r][0] == boardCopyTemp[r][1] && boardCopyTemp[r][0] == boardCopyTemp[r][2] && boardCopyTemp[r][0] != 0) return true;
        }

        //vertical check
        for (int c=col; c<3; c++) {
            if (boardCopyTemp[0][c] == boardCopyTemp[1][c] && boardCopyTemp[0][c] == boardCopyTemp[2][c] && boardCopyTemp[0][c] != 0) return true;
        }

        //pos slope check
        if (boardCopyTemp[2][0] == boardCopyTemp[1][1] && boardCopyTemp[2][0] == boardCopyTemp[0][2] && boardCopyTemp[2][0] != 0) return true;

        //neg slope check - if this does not resolve to true false is returned
        return boardCopyTemp[0][0] == boardCopyTemp[1][1] && boardCopyTemp[0][0] == boardCopyTemp[2][2] && boardCopyTemp[0][0] != 0;
    }

/// DIFFICULTY LOGIC FUNCTIONS

    private static int[] easyMove() { return new int[] {pickRow(), pickCol()}; }

    private static int[] mediumMove() {
        // if pastMoves.length != 0
        if (!cpuPastMoves.isEmpty()) {
            // check around own moves for potential win - defaulted to [0, 0]
            int tempR;
            int tempC;
            int[] focusMove;

            // iterate through deep copy checking for win scenario around all lastMoves
            if (cpuPastMoves.size() > 1) {
                // --- starting with most recently added move -> first added
                for (int pm = cpuPastMoves.size() - 1; pm >= 0; pm--) {
                    // grab relevant values
                    focusMove = cpuPastMoves.get(pm);

                    if (focusMove[0] > 1) tempR = focusMove[0] - 1;
                    else tempR = focusMove[0];
                    if (focusMove[1] > 1) tempC = focusMove[1] - 1;
                    else tempC = focusMove[1];

                    // iterate to find potential wins
                    for (int r = tempR; r < 4 && r < focusMove[0] + 2; r++) {
                        for (int c = tempC; c < 4 && c < focusMove[1] + 2; c++) {
                            // if win condition found return winning move
                            if (GameLogic.getGameboard()[r - 1][c - 1] == 0) {
                                if(wouldWin(r,c)) return new int[] {r,c};
                            }
                            // else continue iterating
                        }
                    }
                }
            }

            // iterate through deep copy checking all lastMoves for openNeighbors
            // first open space found is selected --- starting with most recently added move -> first added
            for (int pm = cpuPastMoves.size() - 1; pm >= 0; pm--) {
                // grab relevant values
                focusMove = cpuPastMoves.get(pm);

                if (focusMove[0] > 1) tempR = focusMove[0] - 1;
                else tempR = focusMove[0];
                if (focusMove[1] > 1) tempC = focusMove[1] - 1;
                else tempC = focusMove[1];

                // iterate to find openNeighbor
                for (int r = tempR; r < 4 && r < focusMove[0] + 2; r++) {
                    for (int c = tempC; c < 4 && c < focusMove[1] + 2; c++) {
                        int[] tmp = new int[] {r,c};
                        // if open space found return valid neighbor move
                        if (GameLogic.getGameboard()[r - 1][c - 1] == 0 && !Arrays.equals(focusMove, tmp)) return new int[] {r,c};
                        // else continue iterating
                    }
                }
            }
        }

        // if all above fails to find valid move, null returned
        return null;
    }

    private static int[] hardMove () {
        //int[] move = new int[2];

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

        return null;
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
            while(!validMove) {

                switch (gameDifficulty){
                    case 1:
                        //medium
                        moveChosen = mediumMove();
                        if (moveChosen == null) moveChosen = easyMove();
                        break;
                    case 2:
                        //hard
                        moveChosen = hardMove();
                        if(moveChosen == null) moveChosen = mediumMove();
                        if(moveChosen == null) moveChosen = easyMove();
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
                GameLogic.setPlayer(GameLogic.getPlayer()-1);
            } else {
                GameLogic.setPlayer(GameLogic.getPlayer()+1);
            }
        }

        switch (gameDifficulty) {
            case 1: //medium
            case 2: //hard
                cpuLastMove = moveChosen;
                cpuPastMoves.add(new int[] {moveChosen[0], moveChosen[1]});
                break;
        }

        // pass back to touchEvent in TicTacToeBoard where the touch is happening [row, col] and whether or not it triggers a win condition (0/false or 1/true)
        return new int[]{row, col, cpuWin};
    }
}