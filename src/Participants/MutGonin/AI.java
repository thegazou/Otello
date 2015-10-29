// Copyright Horia Mut & Nicolas Gonin
// horiamut@msn.com | Doesn't want to give the email, that ahole (DO NOT ATTEMPT FACEBOOK)
package Participants.MutGonin;

import Othello.Move;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class containing the AI functionality.
 * @author Nicolas Gonin, Horia Mut
 */
public class AI
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
     * @param playerID  The player's ID.
     * @param depth     The depth of the algorithm future move lookup.
     * @param size      GameBoard size
     */
    public AI(int playerID, int depth, int size)
    {
        this.player = playerID;
        this.depth = depth;
        this.gameBoardSize = size;
    }

    /**
     * The core of the algorithm, the evaluation function gives an evaluation
     * for a specific move for a player.
     *
     * @param board
     * @param playerID
     * @param skippedMove       If the move that is being evaluated must be skipped.
     * @return
     */
    public int evaluationFunction(GameBoard board, int playerID, boolean skippedMove)
    {
        // The enemy player's ID.
        int enemyID = 1 - playerID;
        
        // Get how many coins we have and they have.
        int coins = board.getCoinCount(playerID); // Normal value
        int coinsEnemy = board.getCoinCount(enemyID);

        // Get the number of available blank cases.
        int availableBoardSpaces = gameBoardSize - (coins + coinsEnemy);
        int movesPlayed = availableBoardSpaces - BEGINING_NUMBER_OF_BOARD_COINS;

        // The corners of the gameboard.
        int cornerCoins = board.getCornerCoinCount(playerID);       // Best kind of coins, cannot be taken.
        int cornerCoinsEnemy = board.getCornerCoinCount(enemyID);   // Worst kind of coins, cannot be taken.

        // Edge coins are coins on the edge of the board.
        int edgeCoins = board.getEdgeCoinCount(playerID) - cornerCoins * 2;           // Worst kind of coins to have, they can easily be taken.
        int edgeCoinsEnemy = board.getEdgeCoinCount(enemyID) - cornerCoinsEnemy * 2;  // Best to have in order to capture them.

        // Retrieve the number of moves possible for both players.
        int nrMovesStillPossible = board.getPossibleMoves(playerID).size();
        int nrMovesStillPossibleEnemy = board.getPossibleMoves(enemyID).size();
        int deltaMoves = nrMovesStillPossible - nrMovesStillPossibleEnemy;

        int total = 0;
        int valueDeltaMoves = 5;

        // Values of our coins.
        int valueCoins = 1;
        int valueCornerCoins = 65;
        int valueEdgeCoins = 5;

        // Values of enemy's coins.
        int valueEnemyCornerCoins = -valueCornerCoins*2;
        int valueEnemyEdgeCoins = -valueEdgeCoins;

        // If we have to skip this move.
        if (skippedMove && nrMovesStillPossibleEnemy > 0)
            {
            total -= 500;
            }

        if (movesPlayed <= OPTIMAL_MOVES_TO_WIN)
            {
            total  += coins * valueCoins
                    + coinsEnemy * -valueCoins
                    + cornerCoins * valueCornerCoins
                    + cornerCoinsEnemy * valueEnemyCornerCoins
                    + edgeCoins * valueEdgeCoins
                    + edgeCoinsEnemy * valueEnemyEdgeCoins
                    + deltaMoves * valueDeltaMoves;
            } 
        else
            {
            valueDeltaMoves = 10;
            valueCoins = 0;
            
            total  += coins * valueCoins
                    + coinsEnemy * -valueCoins
                    + cornerCoins * valueCornerCoins
                    + cornerCoinsEnemy * valueEnemyCornerCoins
                    + edgeCoins * valueEdgeCoins
                    + edgeCoinsEnemy * valueEnemyEdgeCoins
                    + deltaMoves * valueDeltaMoves;
            }

        return total;
    }

    /**
     * Builds a Node tree containing all the moves that are possible to be done by the player.
     * @param gameBoard The Gameboard.
     * @param root      The root of the tree that will be built.
     * @param playerID  The player's ID.
     */
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

    /**
     * Retrieve the best move possible for the player with the game board in a
     * specific state.
     *
     * @param gameBoard The current game board.
     * @param root A Tree root containing all possible moves of the player.
     * @param depth Depth of the algorithm. The number of moves it will analyze
     * after each possible move has been evaluated.
     * @param playerID The player's ID.
     * @return
     */
    public Move getBestMove(GameBoard gameBoard, Node root, int depth, int playerID)
    {
        return alphaBetaN(gameBoard, root, depth, MAX, root.getEvaluation(), playerID, false).getMove();
    }

    /**
     * Alpha-Beta recursive algorithm for the AI.
     *
     * @param board
     * @param root
     * @param depth
     * @param minOrMax Minimize the evaluation or maximize?
     * @param parentValue
     * @param currentPlayer
     * @param skippedMove
     * @return
     */
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
            optVal = alphaBetaN(board, root, depth - 1, -minOrMax, optVal, -currentPlayer, true).getEvaluation();
            } 
        else
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
                        }
                    }
                }
            }

        optMove.setEvaluation(optVal);
        return optMove;
    }
}
