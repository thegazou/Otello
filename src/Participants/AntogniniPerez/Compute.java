/*
 * Decompiled with CFR 0_102.
 */
package Participants.AntogniniPerez;

import Participants.AntogniniPerez.Board;

public class Compute {
    public static final double INF = 1.7976931348623157E308;
    private int player;
    private int depth;
    private double f1;
    private double f2;
    private double f3;
    private double f4;
    private static final double[][] factors = new double[][]{{3.0, 1.0, 8.0, 3.0}, {4.0, 1.0, 6.0, 6.0}, {3.0, 1.0, 3.0, 8.0}, {5.0, 1.0, 8.0, 10.0}, {4.0, 17.0, 49.0, 30.0}, {4.0, 26.0, 20.0, 6.0}, {8.0, 3.0, 6.0, 9.0}, {9.0, 10.0, 6.0, 4.0}, {9.0, 10.0, 7.0, 3.0}, {6.0, 1.0, 2.0, 8.0}, {3.0, 2.0, 2.0, 9.0}, {6.0, 5.0, 6.0, 9.0}, {3.0, 9.0, 9.0, 3.0}, {0.2, 31.0, 10.0, 8.0}, {0.6, 38.0, 20.0, 50.0}, {9.0, 8.0, 1.0, 7.0}, {9.0, 10.0, 6.0, 4.0}, {9.0, 10.0, 7.0, 3.0}};
    private int I;
    private int J;

    public Compute(int player, int depth) {
        this.player = player;
        this.depth = depth;
        this.initialize();
        this.chooseFactors();
    }

    public void initialize() {
        this.J = -1;
        this.I = -1;
    }

    public int getI() {
        return this.I;
    }

    public int getJ() {
        return this.J;
    }

    public double alphaBeta(Board root, int depth, int minOrMax, double parentValue, int currentPlayer, boolean hasToPass) {
        boolean isEndOfGame = root.isTheGameEnded();
        if (depth == 0 || isEndOfGame) {
            return this.eval(root, isEndOfGame, hasToPass);
        }
        double optVal = (double)minOrMax * -1.7976931348623157E308;
        int optOpi = -1;
        int optOpj = -1;
        int[] allPossibleMoves = new int[121];
        root.getAllPossibleMove(allPossibleMoves, currentPlayer);
        if (allPossibleMoves[0] == -1) {
            optVal = this.alphaBeta(root, depth - 1, - minOrMax, optVal, - currentPlayer, true);
        } else {
            int i = 0;
            while (allPossibleMoves[i] > -1) {
                Board newNode = new Board(root);
                newNode.addPiece(allPossibleMoves[i], allPossibleMoves[i + 1], currentPlayer);
                double val = this.alphaBeta(newNode, depth - 1, - minOrMax, optVal, - currentPlayer, false);
                if (val * (double)minOrMax > optVal * (double)minOrMax) {
                    optVal = val;
                    optOpi = allPossibleMoves[i];
                    optOpj = allPossibleMoves[i + 1];
                    if (optVal * (double)minOrMax > parentValue * (double)minOrMax) break;
                }
                i+=2;
            }
        }
        this.I = optOpi;
        this.J = optOpj;
        return optVal;
    }

    private double eval(Board root, boolean isEndOfGame, boolean hasToPass) {
        if (isEndOfGame) {
            return root.getPieceDifference(this.player) > 0.0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        double score = 0.0;
        double scoreParity = root.getParityScore(this.player);
        double scoreMobility = root.getMobilityScore(this.player);
        double scorePlace = root.getPlaceScore(this.player);
        double scoreStability = root.getStabilityScore(this.player);
        int ithMove = root.getIthMove();
        if (ithMove < 13) {
            score+=this.f1 * 5.0 * scoreParity + this.f2 * 3.0 * scoreMobility + this.f3 * (scorePlace / 10.0) + this.f4 * scoreStability;
        } else {
            double ratio = ithMove / 64;
            score+=this.f1 * 5.0 * scoreParity * (1.0 - ratio) + this.f2 * 2.0 * scoreMobility * (1.0 - ratio) + this.f3 * (scorePlace / 10.0) * (1.0 - ratio) + this.f4 * scoreStability * ratio;
        }
        if (hasToPass) {
            score-=500.0;
        }
        return score;
    }

    private void chooseFactors() {
        int index = -1 + this.depth + (this.player > 0 ? 9 : 0);
        this.f1 = factors[index][0];
        this.f2 = factors[index][1];
        this.f3 = factors[index][2];
        this.f4 = factors[index][3];
    }
}
