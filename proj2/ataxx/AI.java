/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.util.ArrayList;
import static ataxx.PieceColor.*;
import static java.lang.Math.min;
import static java.lang.Math.max;

/** A Player that computes its own moves.
 *  @author Darren Wang
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 4;
    /** A position magnitude indicating a win (for red if positive, blue
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI for GAME that will play MYCOLOR. SEED is used to initialize
     *  a random-number generator for use in move computations.  Identical
     *  seeds produce identical behaviour. */
    AI(Game game, PieceColor myColor, long seed) {
        super(game, myColor);
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getMove() {
        Main.startTiming();
        Move move = findMove();
        Main.endTiming();
        game().reportMove(move, myColor());
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(getBoard());
        _lastFoundMove = null;
        if (myColor() == RED) {
            minMax(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            minMax(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        if (_lastFoundMove == null) {
            _lastFoundMove = Move.pass();
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to the findMove method
     *  above. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove, int sense,
                       int alpha, int beta) {
        /* We use WINNING_VALUE + depth as the winning value so as to favor
         * wins that happen sooner rather than later (depth is larger the
         * fewer moves have been made. */
        if (depth == 0 || board.getWinner() != null) {
            return staticScore(board, WINNING_VALUE + depth);
        }
        int bestValue;
        if (sense == 1) {
            bestValue = -INFTY;
            ArrayList<Move> listOfMoves =
                    possibleMoves(board, board.whoseMove());
            for (Move move : listOfMoves) {
                Board copyBoard = new Board(board);
                copyBoard.makeMove(move);
                int possible
                        = minMax(copyBoard, depth - 1, false, -1, alpha, beta);
                if (saveMove && possible > bestValue) {
                    _lastFoundMove = move;
                }
                bestValue = max(bestValue, possible);
                alpha = max(alpha, bestValue);
                if (beta <= alpha) {
                    break;
                }
            }
            if (bestValue == -INFTY) {
                return 0;
            }
            return bestValue;
        } else {
            bestValue = INFTY;
            ArrayList<Move> listOfMoves =
                    possibleMoves(board, board.whoseMove());
            for (Move move : listOfMoves) {
                Board copyBoard = new Board(board);
                copyBoard.makeMove(move);
                int possible
                        = minMax(copyBoard, depth - 1, false, 1, alpha, beta);
                if (saveMove && possible < bestValue) {
                    _lastFoundMove = move;
                }
                bestValue = min(bestValue, possible);
                beta = min(beta, bestValue);
                if (beta <= alpha) {
                    break;
                }
            }
            if (bestValue == INFTY) {
                return 0;
            }
            return bestValue;
        }
    }

    /** Return a heuristic value for BOARD.  This value is +- WINNINGVALUE in
     *  won positions, and 0 for ties. */
    private int staticScore(Board board, int winningValue) {
        PieceColor winner = board.getWinner();
        if (winner != null) {
            return switch (winner) {
            case RED -> winningValue;
            case BLUE -> -winningValue;
            default -> 0;
            };
        }
        int myColor = board.numPieces(board.whoseMove());
        int oppColor = board.numPieces(board.whoseMove().opposite());
        return myColor - oppColor;
    }

    /** Return all possible moves for a color.
     * @param board the current board.
     * @param myColor the specified color.
     * @return an ArrayList of all possible moves for the specified color. */
    private ArrayList<Move> possibleMoves(Board board, PieceColor myColor) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (char row = '7'; row >= '1'; row--) {
            for (char col = 'a'; col <= 'g'; col++) {
                int index = Board.index(col, row);
                if (board.get(index) == myColor) {
                    ArrayList<Move> addMoves
                            = assistPossibleMoves(board, row, col);
                    possibleMoves.addAll(addMoves);
                }
            }
        }
        return possibleMoves;
    }

    /** Returns an Arraylist of legal moves.
     * @param board the board for testing
     * @param row the row coordinate of the center
     * @param col the col coordinate of the center */
    private ArrayList<Move>
        assistPossibleMoves(Board board, char row, char col) {
        ArrayList<Move> assistPossibleMoves = new ArrayList<>();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i != 0 || j != 0) {
                    char row2 = (char) (row + j);
                    char col2 = (char) (col + i);
                    Move currMove = Move.move(col, row, col2, row2);
                    if (board.legalMove(currMove)) {
                        assistPossibleMoves.add(currMove);
                    }
                }
            }
        }
        return assistPossibleMoves;
    }
}
