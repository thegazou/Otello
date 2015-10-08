/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  Othello.Joueur
 *  Othello.Move
 */
package Participants.AntogniniPerez;

import Othello.Move;
import Participants.AntogniniPerez.Board;
import Participants.AntogniniPerez.Compute;

public class Joueur
extends Othello.Joueur {
    private Board board;
    private int player;
    private int oppositePlayer;
    private Compute compute;

    public Joueur(int depth, int playerID) {
        super(depth, playerID);
        this.player = playerID == 1 ? 1 : -1;
        this.oppositePlayer = - this.player;
        this.board = new Board();
        this.compute = new Compute(this.player, depth);
    }

    public Move nextPlay(Move move) {
        if (move != null) {
            this.board.addPiece(move.i, move.j, this.oppositePlayer);
        }
        this.compute.initialize();
        this.compute.alphaBeta(this.board, this.depth, 1, 1.7976931348623157E308, this.player, false);
        int i = this.compute.getI();
        int j = this.compute.getJ();
        if (i != -1) {
            this.board.addPiece(i, j, this.player);
        }
        return i != -1 ? new Move(i, j) : null;
    }
}
