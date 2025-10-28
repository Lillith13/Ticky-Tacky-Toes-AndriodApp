package com.example.tickytackytoes;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameLogic {
    protected static int[][] gameboard;

    //winType Array => {row, col, winLine type}
    private int[] winType = {-1, -1, -1};

    private Button playAgainBTN;
    private Button homeBTN;
    private TextView playerTurn;
    private int player = 1; // player turn

    private boolean inputEnabled = true;


    GameLogic() {
        //setup empty gameboard
        gameboard = new int[3][3];
        for (int r=0; r<3; r++) {
            for (int c=0; c<3; c++) {
                gameboard[r][c] = 0;
            }
        }
    }

    public boolean updateGameBoard(int row, int col) {
        //check to make sure selection is available
        if (gameboard[row - 1][col - 1] == 0) {
            gameboard[row-1][col-1] = player;

            //update textView for playerTurn
            if(player == 1) {
                playerTurn.setText((String.format("%s's Turn", PlayerSetup.playerNames[1])));
            } else {
                playerTurn.setText((String.format("%s's Turn", PlayerSetup.playerNames[0])));
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean winnerCheck() {
        boolean isWinner = false;

        //horizontal check
        for (int r=0; r<3; r++) {
            if (gameboard[r][0] == gameboard[r][1] && gameboard[r][0] == gameboard[r][2] && gameboard[r][0] != 0) {
                winType = new int[] {r, 0, 1};
                isWinner = true;
            }
        }
        //vertical check
        for (int c=0; c<3; c++) {
            if (gameboard[0][c] == gameboard[1][c] && gameboard[0][c] == gameboard[2][c] && gameboard[0][c] != 0) {
                winType = new int[] {0, c, 2};
                isWinner = true;
            }
        }
        //pos slope check
        if (gameboard[2][0] == gameboard[1][1] && gameboard[2][0] == gameboard[0][2] && gameboard[2][0] != 0) {
            winType = new int[] {2, 2, 3};
            isWinner = true;
        }
        //neg slope check
        if (gameboard[0][0] == gameboard[1][1] && gameboard[0][0] == gameboard[2][2] && gameboard[0][0] != 0) {
            winType = new int[] {0, 2, 4};
            isWinner = true;
        }

        int boardFilled = 0;
        for (int r=0; r<3; r++) {
            for (int c=0; c<3; c++) {
                if(gameboard[r][c] != 0) {
                    boardFilled += 1;
                }
            }
        }

        if (isWinner) {
            playAgainBTN.setVisibility(View.VISIBLE);
            homeBTN.setVisibility((View.VISIBLE));
            playerTurn.setText((String.format("%s WON!!!", PlayerSetup.playerNames[player - 1])));
            return true;
        } else if(boardFilled == 9) {
            playAgainBTN.setVisibility(View.VISIBLE);
            homeBTN.setVisibility((View.VISIBLE));
            playerTurn.setText("Tied Game");
            winType = new int[] {0, 0, 5};
            return true;
        }

        return isWinner;
    }

    public void resetGame() {
        // reset gameboard
        for (int r=0; r<3; r++) {
            for (int c=0; c<3; c++) {
                gameboard[r][c] = 0;
            }
        }

        //only reset if OnePlayer mode
        if(MainActivity.getPlayerCount() == 1) {
            setPlayer(1);
        }

        playAgainBTN.setVisibility(View.GONE);
        homeBTN.setVisibility(View.GONE);
        playerTurn.setText(String.format("%s's Turn", PlayerSetup.playerNames[player - 1]));

    }

    public void setPlayAgainBTN(Button playAgainBTN) {
        this.playAgainBTN = playAgainBTN;
    }

    public void setHomeBTN(Button homeBTN) {
        this.homeBTN = homeBTN;
    }

    public void setPlayerTurn(TextView playerTurn) {
        this.playerTurn = playerTurn;
    }


    public int[][] getGameboard() {
        return gameboard;
    }
    public void setPlayer(int player) {
        this.player = player;
    }
    public int getPlayer() {
        return player;
    }

    public int[] getWinType() {
        return winType;
    }

    public boolean isInputEnabled() {
        return inputEnabled;
    }

    public void setInputEnabled(boolean inputEnabled) {
        this.inputEnabled = inputEnabled;
    }

}
