package com.example.tickytackytoes;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

public class CPULogic {

    int[][] pastCPUMoves;

    TicTacToeBoard ticTacToeBoard;

    CPULogic () {
        // just here to be here...fucks everything up if i take it out and im not smart enough yet to know why
    }

    private static int pickRow() {
        return (int)(Math.random() * 3) + 1;
    };
    private static int pickCol() {
        return (int)(Math.random() * 3) + 1;
    };

    public static int[] cpuMove(GameLogic game, boolean winningLine) {
        int row = 4;
        int col = 4;
        boolean validMove = false;

        // cpuWin == false
        int cpuWin = 0;

        if(!winningLine) {
                winningLine = game.winnerCheck();

                while(!validMove) {
                    row = pickRow();
                    col = pickCol();
                    validMove = game.updateGameBoard(row, col);
                    winningLine = game.winnerCheck();

                    if(winningLine) {
                        // sets cpuWin to 1 to indicate TRUE
                        cpuWin = 1;
                    }
                }

                // alternate between players
                if (game.getPlayer() % 2 == 0) {
                    game.setPlayer(game.getPlayer()-1);
                } else {
                    game.setPlayer(game.getPlayer()+1);
                }
        }

        // pass back to touchEvent in TicTacToeBoard where the touch is happening [row, col] and whether or not it triggers a win condition (0/false or 1/true)
        return new int[]{row, col, cpuWin};
    }
}
