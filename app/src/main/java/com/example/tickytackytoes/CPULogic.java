package com.example.tickytackytoes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CPULogic {

    static int gameDifficulty;
    static boolean validMove;

    static int[][] gameBoardDeepCopy;
    static List<int[]> cpuPastMoves;
    static Set<int[]> playerPastMoves;

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
        if (gameDifficulty == 2) playerPastMoves = new HashSet<>();
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

    private static boolean wouldWin(int row, int col, int player) {
        // check move against deepCopy of board (updated every turn) using stripped down GameLogic.winnerCheck() logic
        int[][] boardCopyTemp = deepCopyBoard(gameBoardDeepCopy);

        if (boardCopyTemp[row - 1][col - 1] != 0)  return false;
        int temp = ((row * 100) + col);

        switch (temp) {
            case 101: // == {1, 1} = [0][0]
                // ROW Check
                if (boardCopyTemp[0][1] == player && boardCopyTemp[0][2] == player) return true;
                // NEG Slope Check
                if (boardCopyTemp[1][1] == player && boardCopyTemp[2][2] == player) return true;
                // COL Check
                if (boardCopyTemp[1][0] == player && boardCopyTemp[2][0] == player) return true;
                break;
            case 102: // == {1, 2} = [0][1]
                // ROW Check
                if (boardCopyTemp[0][0] == player && boardCopyTemp[0][2] == player) return true;
                // COL Check
                if (boardCopyTemp[1][1] == player && boardCopyTemp[2][1] == player) return true;
                break;
            case 103: // == {1, 3} = [0][2]
                // ROW Check
                if (boardCopyTemp[0][0] == player && boardCopyTemp[0][1] == player) return true;
                // POS Slope Check
                if (boardCopyTemp[2][0] == player && boardCopyTemp[1][1] == player) return true;
                // COL Check
                if (boardCopyTemp[1][2] == player && boardCopyTemp[2][2] == player) return true;
                break;
            case 201:  // == {2, 1} = [1][0]
                // ROW Check
                if (boardCopyTemp[1][1] == player && boardCopyTemp[1][2] == player) return true;
                // COL Check
                if (boardCopyTemp[0][0] == player && boardCopyTemp[2][0] == player) return true;
                break;
            case 202: // == {2, 2} == canter square = [1][1]
                // ROW Check
                if (boardCopyTemp[1][0] == player && boardCopyTemp[1][2] == player) return true;
                //POS Slope Check
                if (boardCopyTemp[2][0] == player && boardCopyTemp[0][2] == player) return true;
                // NEG Slope Check
                if (boardCopyTemp[0][0] == player && boardCopyTemp[2][2] == player) return true;
                // COL Check
                if (boardCopyTemp[0][1] == player && boardCopyTemp[2][1] == player) return true;
                break;
            case 203:  // == {2, 3} = [1][2]
                // ROW Check
                if (boardCopyTemp[1][0] == player && boardCopyTemp[1][1] == player) return true;
                // COL Check
                if (boardCopyTemp[0][2] == player && boardCopyTemp[2][2] == player) return true;
                break;
            case 301: // == {3, 1} = [2][0]
                // ROW Check
                if (boardCopyTemp[2][1] == player && boardCopyTemp[2][2] == player) return true;
                // POS Slope Check
                if (boardCopyTemp[1][1] == player && boardCopyTemp[0][2] == player) return true;
                // COL Check
                if (boardCopyTemp[0][0] == player && boardCopyTemp[1][0] == player) return true;
                break;
            case 302:  // == {3, 2} = [2][1]
                // ROW Check
                if (boardCopyTemp[2][0] == player && boardCopyTemp[2][2] == player) return true;
                // COL Check
                if (boardCopyTemp[0][1] == player && boardCopyTemp[1][1] == player) return true;
                break;
            case 303: // == {3, 3} = [2][2]
                // ROW Check
                if (boardCopyTemp[2][0] == player && boardCopyTemp[2][1] == player) return true;
                // NEG Slope Check
                if (boardCopyTemp[0][0] == player && boardCopyTemp[1][1] == player) return true;
                // COL Check
                if (boardCopyTemp[0][2] == player && boardCopyTemp[1][2] == player) return true;
                break;
        }

        return false;
    }

    private static int[] findPotWin(List<int[]> pastMoves, int player) {
        /*
            This function will iterate through either the cpuPastMoves List or the playerPastMoves List to find a potential win.
            If a potential win is found it will be return, else null will be returned.
            If called with playerPastMoves as it's arg the move chosen (if one found) will be the cpu's Blocking Move
        */

        int tempR;
        int tempC;
        int[] focusMove;

        // iterates in reverse (last in list -> first)
        for (int x = pastMoves.size() - 1; x >= 0; x--) {
            // grab relevant values
            focusMove = pastMoves.get(x);

            if (focusMove[0] > 1) tempR = focusMove[0] - 1;
            else tempR = focusMove[0];
            if (focusMove[1] > 1) tempC = focusMove[1] - 1;
            else tempC = focusMove[1];

            // iterate to find potential wins
            for (int r = tempR; r < 4 && r < focusMove[0] + 2; r++) {
                for (int c = tempC; c < 4 && c < focusMove[1] + 2; c++) {
                    // if win condition found return move selection
                    if (GameLogic.getGameboard()[r - 1][c - 1] == 0) {
                        if(wouldWin(r, c, player)) return new int[] {r,c};
                    }
                }
            }
        }
        // no potential win or blocking move found will return null;
        return null;
    }

    private static int[] findOpenNeighbor() {
        int tempR;
        int tempC;
        int[] focusMove;

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

        return null;
    }

/// DIFFICULTY LOGIC FUNCTIONS

    private static int[] easyMove() { return new int[] {pickRow(), pickCol()}; }

    private static int[] mediumMove() {
        // if pastMoves.length != 0
        if (!cpuPastMoves.isEmpty()) {
            if (cpuPastMoves.size() > 1) {
                int[] cpuWin = findPotWin(cpuPastMoves, 2);
                if (cpuWin != null) return cpuWin;
            }
            // if no open neighbor was found null will be returned
            return findOpenNeighbor();
        }
        // if all above fails to find valid move, null returned
        return null;
    }

    private static int[] hardMove () {
        // update playerPastMoves List
        List<int[]> playerMovesList = Collections.emptyList();
        if(playerPastMoves != null) {
            playerMovesList = playerPastMoves.stream().toList();
        }

        if(!cpuPastMoves.isEmpty()) {
            
            if (playerMovesList.size() > 1) {
                // block potential player win
                int[] blockMove = findPotWin(playerMovesList, 1);
                if(blockMove != null) return blockMove;
            }

            if (cpuPastMoves.size() > 1) {
                // find a move for CPU win
                int[] cpuWin = findPotWin(cpuPastMoves, 2);
                if (cpuWin != null) return cpuWin;

            }

            // all else failed => find open neighbor
            return findOpenNeighbor();
        }

        // all other conditions failed == return null == will call easyMove() func for random move
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