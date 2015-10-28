/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Participants.UnrealTeam;

import Othello.Move;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Nicolas Gonin, Horia Mut
 */
public class AI_V0
{

    // Attributes
    public static final int INF = Integer.MAX_VALUE;
    private static final int BEGINING_NUMBER_OF_BOARD_COINS = 4;
    private static final int OPTIMAL_MOVES_TO_WIN = 14;

    private int player;
    private int depth;

    public static final int MIN = -1;
    public static final int MAX = 1;
    private final int gameBoardSize;

    /**
     * AI constructor.
     *
     * @param playerID
     * @param depth
     */
    public AI_V0(int playerID, int depth, int size)
    {
        this.player = playerID;
        this.depth = depth;
        this.gameBoardSize = size;
    }

    public int evaluationFunction(GameBoard board, int playerID, boolean skippedMove)
    {
        // Get how many coins we have and they have.
        int coins = board.getCoinCount(playerID); // Normal value
        int coinsEnemy = board.getCoinCount(1 - playerID);

        // Get the number of available blank cases.
        int availableBoardSpaces = gameBoardSize - (coins + coinsEnemy);
        int movesPlayed = availableBoardSpaces - BEGINING_NUMBER_OF_BOARD_COINS;

        int edgeCoins = board.getEdgeCoinCount(playerID);               // Worst kind of coins to have, they can easily be taken.
        int edgeCoinsEnemy = board.getEdgeCoinCount(1 - playerID);      // Best to have in order to capture them.
        int cornerCoins = board.getCornerCoinCount(playerID);           // Best kind of coins, cannot be taken.
        int cornerCoinsEnemy = board.getCornerCoinCount(1 - playerID);  // Worst kind of coins, cannot be taken.

        // Retrieve the number of moves possible for both players.
        int nrMovesStillPossible = board.getPossibleMoves(playerID).size();
        int nrMovesStillPossibleEnemy = board.getPossibleMoves(1 - playerID).size();
        int deltaMoves = nrMovesStillPossible - nrMovesStillPossibleEnemy;

        int total = 0;
        int ratioOurMoves = 10;
        int ratioEnemyMoves = -10;
        if (deltaMoves < 0)
            {
            // The enemy is winning.
            total -= nrMovesStillPossibleEnemy * 500;
            ratioEnemyMoves *= 10;
            }

        // If we have to skip this move.
        if (skippedMove && nrMovesStillPossibleEnemy > 0)
            {
            total -= 5000;
            }

        if (movesPlayed <= OPTIMAL_MOVES_TO_WIN)
            {
            total += coins * 1 + coinsEnemy * -1 + cornerCoins * 5000 + cornerCoinsEnemy * -1000 + nrMovesStillPossible * ratioOurMoves + nrMovesStillPossibleEnemy * ratioEnemyMoves + edgeCoins * -200 + edgeCoinsEnemy * 1000;
            } else
            {
            total += coins * -1 + coinsEnemy * -1 + cornerCoins * 50000 + cornerCoinsEnemy * -10000 + nrMovesStillPossible * 10 + nrMovesStillPossibleEnemy * -100 + edgeCoins * -200 + edgeCoinsEnemy * 1000;
            }

        return total;
    }

    public void buildTree(GameBoard gameBoard, Node root, int playerID)
    {
        // Fill the tree with our possible moves.
        ArrayList<Move> allOurPossibleMoves = gameBoard.getPossibleMoves(playerID);
        for (Iterator<Move> iMove = allOurPossibleMoves.iterator(); iMove.hasNext();)
            {
            Move next = iMove.next();
            // Build the node.
            Node nextMoveNode = new Node(next);
            // Set it's data.
            nextMoveNode.setEvaluation(evaluationFunction(gameBoard, playerID, false));
            root.addChildNode(nextMoveNode);
            }
    }

    public Move getBestMove(GameBoard gameBoard, Node root, int depth, int playerID)
    {
        return alphaBetaN(gameBoard, root, depth, MAX, root.getEvaluation(), playerID, false).getMove();
    }

    private Node alphaBetaN(GameBoard board, Node root, int depth, int minOrMax, int parentValue, int currentPlayer, boolean skippedMove)
    {
        int nrMovesStillPossible = board.getPossibleMoves(currentPlayer).size();
        int nrMovesStillPossibleEnemy = board.getPossibleMoves(1 - currentPlayer).size();
        boolean endGame = false;

        if (nrMovesStillPossible == 0 && nrMovesStillPossibleEnemy == 0)
            {
            endGame = true;
            }

        // Check if we have reached the maximum depth of if the root is at the end.
        if (depth == 0 || endGame || root.isLeaf())
            {
            // Since we have reached the end, evaluate the actual node (root).
            root.setEvaluation(evaluationFunction(board, player, skippedMove));
            return root;
            }

        int optVal = minOrMax * -INF;
        // No optimal operation (move) yet.
        Node optMove = new Node(null);

        boolean stopClause = false;
        ArrayList<Node> allPossibleMoves = root.getChildNodeList();

        // Check if we can do any moves.
        if (allPossibleMoves.isEmpty())
            {
            // If we can't, we skip our move.
            //optMove.setEvaluation(evaluationFunction(board, player));
            optVal = alphaBetaN(board, root, depth - 1, -minOrMax, optVal, -currentPlayer, true).getEvaluation();
            } else
            {
            while (!allPossibleMoves.isEmpty() && !stopClause)
                {
                // Get the node containing the first possible move.
                Node actualMoveNode = allPossibleMoves.remove(0);

                // Now rebuild the game state, add the piece, then re-evaluate while substracting the depth.
                GameBoard gameBoardClone = board.clone();
                gameBoardClone.addCoin(actualMoveNode.getMove(), currentPlayer);

                // Once the coin has been added, do alpha beta on the new Board and minimize this time.
                actualMoveNode.setEvaluation(alphaBetaN(gameBoardClone, actualMoveNode, depth - 1, -minOrMax, optVal, currentPlayer, false).getEvaluation());
                int val = actualMoveNode.getEvaluation();

                // If the value of the minimized move
                if (val * minOrMax > optVal * minOrMax)
                    {
                    optVal = val;
                    optMove.setMove(actualMoveNode.getMove());
                    optMove.setEvaluation(optVal);

                    // Stop looking.
                    if (optVal * minOrMax > parentValue * minOrMax)
                        {
                        stopClause = true;
                        //break;
                        }
                    }
                }
            }

        optMove.setEvaluation(optVal);
        return optMove;
    }

}
