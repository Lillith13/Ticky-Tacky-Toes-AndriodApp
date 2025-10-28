package com.example.tickytackytoes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TicTacToeBoard extends View {

    private int boardColor;
    private int XColor;
    private int OColor;
    private final int winningLineColor;
    private boolean winningLine = false;

    private final Paint paint = new Paint();

    private int cellSize = getWidth()/3;

    GameLogic game;
    CPULogic cpu = new CPULogic();


    public TicTacToeBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        game = new GameLogic();
        if(MainActivity.playerCount == 2) cpu = null;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TicTacToeBoard, 0,0);

        try {
            boardColor = a.getInteger(R.styleable.TicTacToeBoard_boardColor, 0);
            XColor = a.getInteger(R.styleable.TicTacToeBoard_XColor, 0);
            OColor = a.getInteger(R.styleable.TicTacToeBoard_OColor, 0);
            winningLineColor = a.getInteger(R.styleable.TicTacToeBoard_winningLineColor, 0);
        } finally {
            a.recycle();
        }
    }

    public void setBoardColor(int boardColor) {
        this.boardColor = boardColor;
    }

    public void setXColor(int color) {
        this.XColor = color;
    }

    public void setOColor(int color) {
        this.OColor = color;
    }

    //sets size for board based on screen constraints
    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);

        int dimension = Math.min(getMeasuredWidth(), getMeasuredHeight());
        cellSize = dimension/3;

        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        drawGameBoard(canvas);
        drawMarkers(canvas);

        if (winningLine) {
            paint.setColor(winningLineColor);
            drawWinningLine(canvas);
        }

        invalidate();
    }

    public void simulateTouch(float x, float y) {
        long downTime = SystemClock.uptimeMillis();

        MotionEvent downEvent = MotionEvent.obtain(
                downTime,
                downTime,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                0
        );

        this.dispatchTouchEvent(downEvent);
        downEvent.recycle();

        // small delay before ACTION_UP — keeps behavior consistent with real taps
        long upTime = SystemClock.uptimeMillis();
        MotionEvent upEvent = MotionEvent.obtain(
                downTime,
                upTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                0
        );

        this.dispatchTouchEvent(upEvent);
        upEvent.recycle();
    }

    public boolean isEnabled = true;
    // make board interactable
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isEnabled = game.isInputEnabled();

        // inputEnabled only changes if playerCount == 1
        if(MainActivity.playerCount == 1) {
            game.setInputEnabled(game.getPlayer() == 1);
        }

        if (!isEnabled) {
            return false; // ignore touches if disabled
        }

        // get location of user tap
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            int row = (int) Math.ceil(y/cellSize);
            int col = (int) Math.ceil(x/cellSize);

            if(!winningLine) {
                if (game.updateGameBoard(row, col)) {
                    invalidate();

                    if (game.winnerCheck()) {
                        winningLine = true;
                        invalidate();
                    }
                    // alternate between players
                    if (game.getPlayer() % 2 == 0) {
                        game.setPlayer(game.getPlayer()-1);
                    } else {
                        game.setPlayer(game.getPlayer()+1);
                    }
                }
            }

            // below IF should be false unless CPU turn && gamemode == Single Player
            if (!winningLine && game.getPlayer() % 2 == 0 && MainActivity.playerCount == 1) {
                game.setInputEnabled(!(game.getPlayer() % 2 == 0));

                // wait a few seconds for CPU to make it's move
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    int[] cpuChosenMove = CPULogic.cpuMove(game, winningLine);
                    float cpuX = (cpuChosenMove[1] - 0.5f) * cellSize;
                    float cpuY = (cpuChosenMove[0] - 0.5f) * cellSize;
                    simulateTouch(cpuX, cpuY);

                    // triggers onDraw for winningLine for CPU win
                    if (cpuChosenMove[2] == 1)  winningLine = true;

                    invalidate();
                }, 1000);

                invalidate();
            }

            //refreshes gameboard
            invalidate();
            return true;
        }
        return false;
    }

    // tells app how to draw board
    private void drawGameBoard(Canvas canvas) {
        paint.setColor(boardColor);
        paint.setStrokeWidth(16);

        for (int c=1; c<3; c++) {
            canvas.drawLine(cellSize * c, 0, cellSize * c, canvas.getWidth(), paint);
        }

        for (int r=1; r<3; r++) {
            canvas.drawLine(0, cellSize * r, canvas.getWidth(), cellSize * r, paint);
        }
    }

    private void drawMarkers(Canvas canvas) {
        //decides which marker to put on the board
        for (int r=0; r<3; r++) {
            for (int c=0; c<3; c++) {
                if (game.getGameboard()[r][c] != 0) {
                    if (game.getGameboard()[r][c] == 1) {
                        drawX(canvas, r, c);
                    } else {
                        drawO(canvas, r, c);
                    }
                }
            }
        }
        invalidate();
    }

    // tells app how to draw X's and O's
    private void drawX(Canvas canvas, int row, int col) {
        paint.setColor(XColor);

        canvas.drawLine(
                (float) ((col + 1) * cellSize - cellSize * 0.15),
                (float) (row * cellSize + cellSize * 0.15),
                (float) (col * cellSize + cellSize * 0.15),
                (float) ((row + 1) * cellSize - cellSize * 0.15),
                paint
        );
        canvas.drawLine(
                (float) (col * cellSize  + cellSize * 0.15),
                (float) (row * cellSize + cellSize * 0.15),
                (float) ((col + 1) * cellSize - cellSize * 0.15),
                (float) ((row + 1) * cellSize - cellSize * 0.15),
                paint
        );
    }

    private void drawO(Canvas canvas, int row, int col) {
        paint.setColor(OColor);

        canvas.drawOval(
                (float) (col * cellSize + cellSize * 0.15),
                (float) (row * cellSize + cellSize * 0.15),
                (float) ((col * cellSize + cellSize) - cellSize * 0.15),
                (float) ((row * cellSize + cellSize) - cellSize * 0.15),
                paint
        );
    }

    private void drawHorizontalLine(Canvas canvas, int row, int col) {
        canvas.drawLine(col, row*cellSize + (float)cellSize/2, cellSize*3, row*cellSize + (float)cellSize/2, paint);
        invalidate();
    }
    private void drawVerticalLine(Canvas canvas, int row, int col) {
        canvas.drawLine(col*cellSize + (float)cellSize/2, row, col*cellSize + (float)cellSize/2, (float)cellSize*3, paint);
        invalidate();
    }
    private void drawDiagonalLinePos(Canvas canvas) {
        canvas.drawLine(0, cellSize*3, cellSize*3, 0, paint);
        invalidate();
    }
    private void drawDiagonalLineNeg(Canvas canvas) {
        canvas.drawLine(0, 0, cellSize*3, cellSize*3, paint);
        invalidate();
    }

    private void drawWinningLine(Canvas canvas) {
        int row = game.getWinType()[0];
        int col = game.getWinType()[1];

        switch (game.getWinType()[2]) {
            case 1:
                drawHorizontalLine(canvas, row, col);
                break;
            case 2:
                drawVerticalLine(canvas, row, col);
                break;
            case 3:
                drawDiagonalLinePos(canvas);
                break;
            case 4:
                drawDiagonalLineNeg(canvas);
                break;
            case 5:
                break;
        }
        invalidate();
    }

    public void setUpGame(Button playAgainBTN, Button homeBTN, TextView playerDisplay) {
        game.setPlayAgainBTN(playAgainBTN);
        game.setHomeBTN(homeBTN);
        game.setPlayerTurn(playerDisplay);
    }

    public void resetGame() {
        game.resetGame();
        if (MainActivity.playerCount == 1) {
           game.setPlayer(1);
        }
        winningLine = false;
    }

}
