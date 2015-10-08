/*
 * Decompiled with CFR 0_102.
 */
package Participants.AntogniniPerez;

import java.util.Arrays;

public class Board {
    public static final int Blue = 1;
    public static final int Red = -1;
    public static final int Empty = 0;
    public static final int DUMMY_VALUE = -1;
    private static final int BOARD_SIZE = 8;
    public static final int END_BEGIN_GAME = 13;
    private int[][] board = new int[8][8];
    private int[][] positionMatrixScore;
    private boolean hasAPlayerPassed;
    private int oldPlayer;
    private int ithMove;
    private int tempMine;
    private int tempHis;

    public Board() {
        int[][] arrn = new int[8][];
        arrn[0] = new int[]{500, -150, 30, 10, 10, 30, -150, 500};
        int[] arrn2 = new int[8];
        arrn2[0] = -150;
        arrn2[1] = -250;
        arrn2[6] = -250;
        arrn2[7] = -150;
        arrn[1] = arrn2;
        int[] arrn3 = new int[8];
        arrn3[0] = 30;
        arrn3[2] = 1;
        arrn3[3] = 2;
        arrn3[4] = 2;
        arrn3[5] = 1;
        arrn3[7] = 30;
        arrn[2] = arrn3;
        int[] arrn4 = new int[8];
        arrn4[0] = 10;
        arrn4[2] = 2;
        arrn4[3] = 16;
        arrn4[4] = 16;
        arrn4[5] = 2;
        arrn4[7] = 10;
        arrn[3] = arrn4;
        int[] arrn5 = new int[8];
        arrn5[0] = 10;
        arrn5[2] = 2;
        arrn5[3] = 16;
        arrn5[4] = 16;
        arrn5[5] = 2;
        arrn5[7] = 10;
        arrn[4] = arrn5;
        int[] arrn6 = new int[8];
        arrn6[0] = 30;
        arrn6[2] = 1;
        arrn6[3] = 2;
        arrn6[4] = 2;
        arrn6[5] = 1;
        arrn6[7] = 30;
        arrn[5] = arrn6;
        int[] arrn7 = new int[8];
        arrn7[0] = -150;
        arrn7[1] = -250;
        arrn7[6] = -250;
        arrn7[7] = -150;
        arrn[6] = arrn7;
        arrn[7] = new int[]{500, -150, 30, 10, 10, 30, -150, 500};
        this.positionMatrixScore = arrn;
        this.init();
    }

    private void init() {
        this.oldPlayer = 1;
        this.hasAPlayerPassed = false;
        this.ithMove = 4;
        this.tempMine = 0;
        this.tempHis = 0;
        for (int i = 0; i < 8; ++i) {
            Arrays.fill(this.board[i], 0);
        }
        this.board[3][3] = 1;
        this.board[4][4] = 1;
        this.board[3][4] = -1;
        this.board[4][3] = -1;
    }

    public Board(Board board) {
        this.positionMatrixScore = new int[8][8];
        this.ithMove = board.ithMove;
        for (int i = 0; i < 8; ++i) {
            this.board[i] = Arrays.copyOf(board.board[i], 8);
            this.positionMatrixScore[i] = Arrays.copyOf(board.positionMatrixScore[i], 8);
        }
    }

    public void addPiece(int col, int row, int currentPlayer) {
        ++this.ithMove;
        if (this.oldPlayer == currentPlayer) {
            this.hasAPlayerPassed = !this.hasAPlayerPassed;
        }
        this.oldPlayer = currentPlayer;
        this.board[col][row] = currentPlayer;
        this.updateCornerCloseness(col, row);
        if (this.checkHorizontallyLeft2Right(col, row, currentPlayer)) {
            this.actionHorizontallyLeft2Right(col, row, currentPlayer);
        }
        if (this.checkHorizontallyRight2Left(col, row, currentPlayer)) {
            this.actionHorizontallyRight2Left(col, row, currentPlayer);
        }
        if (this.checkVerticallyTop2Bottom(col, row, currentPlayer)) {
            this.actionVerticallyTop2Bottom(col, row, currentPlayer);
        }
        if (this.checkVerticallyBottom2Top(col, row, currentPlayer)) {
            this.actionVerticallyBottom2Top(col, row, currentPlayer);
        }
        if (this.checkDiagonallyBottomLeft2TopRight(col, row, currentPlayer)) {
            this.actionDiagonallyBottomLeft2TopRight(col, row, currentPlayer);
        }
        if (this.checkDiagonallyBottomRight2TopLeft(col, row, currentPlayer)) {
            this.actionDiagonallyBottomRight2TopLeft(col, row, currentPlayer);
        }
        if (this.checkDiagonallyTopRight2BottomLeft(col, row, currentPlayer)) {
            this.actionDiagonallyTopRight2BottomLeft(col, row, currentPlayer);
        }
        if (this.checkDiagonallyTopLeft2BottomRight(col, row, currentPlayer)) {
            this.actionDiagonallyTopLeft2BottomRight(col, row, currentPlayer);
        }
    }

    public int getIthMove() {
        return this.ithMove;
    }

    public void getAllPossibleMove(int[] outputArray, int currentPlayer) {
        int indexOutputArray = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (this.board[i][j] != 0 || !this.isLegit(i, j, currentPlayer)) continue;
                outputArray[indexOutputArray++] = i;
                outputArray[indexOutputArray++] = j;
            }
        }
        outputArray[indexOutputArray] = -1;
    }

    public boolean isTheGameEnded() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (this.board[i][j] != 0 || !this.isLegit(i, j, 1) && !this.isLegit(i, j, -1)) continue;
                return false;
            }
        }
        return true;
    }

    private boolean isLegit(int col, int row, int currentPlayer) {
        if (!(this.checkHorizontallyLeft2Right(col, row, currentPlayer) || this.checkHorizontallyRight2Left(col, row, currentPlayer) || this.checkVerticallyTop2Bottom(col, row, currentPlayer) || this.checkVerticallyBottom2Top(col, row, currentPlayer) || this.checkDiagonallyBottomLeft2TopRight(col, row, currentPlayer) || this.checkDiagonallyBottomRight2TopLeft(col, row, currentPlayer) || this.checkDiagonallyTopRight2BottomLeft(col, row, currentPlayer) || this.checkDiagonallyTopLeft2BottomRight(col, row, currentPlayer))) {
            return false;
        }
        return true;
    }

    private void updateCornerCloseness(int col, int row) {
        if (col == 0 && row == 0) {
            int[] arrn = this.positionMatrixScore[0];
            arrn[1] = arrn[1] * -1;
            int[] arrn2 = this.positionMatrixScore[1];
            arrn2[0] = arrn2[0] * -1;
            int[] arrn3 = this.positionMatrixScore[1];
            arrn3[1] = arrn3[1] * -1;
        } else if (col == 0 && row == 7) {
            int[] arrn = this.positionMatrixScore[0];
            arrn[6] = arrn[6] * -1;
            int[] arrn4 = this.positionMatrixScore[1];
            arrn4[7] = arrn4[7] * -1;
            int[] arrn5 = this.positionMatrixScore[1];
            arrn5[6] = arrn5[6] * -1;
        } else if (col == 7 && row == 0) {
            int[] arrn = this.positionMatrixScore[7];
            arrn[1] = arrn[1] * -1;
            int[] arrn6 = this.positionMatrixScore[6];
            arrn6[0] = arrn6[0] * -1;
            int[] arrn7 = this.positionMatrixScore[6];
            arrn7[1] = arrn7[1] * -1;
        } else if (col == 7 && row == 7) {
            int[] arrn = this.positionMatrixScore[7];
            arrn[6] = arrn[6] * -1;
            int[] arrn8 = this.positionMatrixScore[6];
            arrn8[7] = arrn8[7] * -1;
            int[] arrn9 = this.positionMatrixScore[6];
            arrn9[6] = arrn9[6] * -1;
        }
    }

    private boolean isIrreversiblePiece(int row, int col, int currentPlayer) {
        boolean isStateOneValid = true;
        isStateOneValid = this.checkIrreversiblePieceHorizontallyLeft(row, col, currentPlayer);
        if (!isStateOneValid) {
            isStateOneValid = this.checkIrreversiblePieceHorizontallyRight(row, col, currentPlayer);
        }
        if (!isStateOneValid) {
            return false;
        }
        boolean isStateTwoValid = true;
        isStateTwoValid = this.checkIrreversibleVerticallyTop(row, col, currentPlayer);
        if (!isStateTwoValid) {
            isStateTwoValid = this.checkIrreversiblePieceVerticallyBottom(row, col, currentPlayer);
        }
        if (!isStateTwoValid) {
            return false;
        }
        boolean isStateThreeValid = true;
        if (this.checkIrreversiblePieceDiagonallyTopLeft(row, col, currentPlayer)) {
            isStateThreeValid = this.checkIrreversiblePieceDiagonallyBottomLeft(row, col, currentPlayer);
        }
        if (isStateThreeValid) {
            return true;
        }
        isStateThreeValid = true;
        if (this.checkIrreversiblePieceDiagonallyTopLeft(row, col, currentPlayer)) {
            isStateThreeValid = this.checkIrreversiblePieceDiagonallyTopRight(row, col, currentPlayer);
        }
        if (isStateThreeValid) {
            return true;
        }
        isStateThreeValid = true;
        if (this.checkIrreversiblePieceDiagonallyTopRight(row, col, currentPlayer)) {
            isStateThreeValid = this.checkIrreversiblePieceDiagonallyBottomRight(row, col, currentPlayer);
        }
        if (isStateThreeValid) {
            return true;
        }
        isStateThreeValid = true;
        if (this.checkIrreversiblePieceDiagonallyBottomRight(row, col, currentPlayer)) {
            isStateThreeValid = this.checkIrreversiblePieceDiagonallyBottomLeft(row, col, currentPlayer);
        }
        return false;
    }

    public double getParityScore(int currentPlayer) {
        return this.hasAPlayerPassed && currentPlayer == 1 || !this.hasAPlayerPassed && currentPlayer == -1 ? 1.0 : 0.0;
    }

    public double getStabilityScore(int currentPlayer) {
        this.getCornerOccupacy(currentPlayer);
        double mineCorner = this.tempMine;
        double hisCorner = this.tempHis;
        this.getCornerCloseness(currentPlayer);
        double mineCornerCloseness = this.tempMine;
        double hisCornerCloseness = this.tempHis;
        this.getBordPiece(currentPlayer);
        double mineBord = this.tempMine;
        double hisBord = this.tempHis;
        this.getNbIrreversiblePiece(currentPlayer);
        double mineIrreversiblePiece = (double)this.tempMine - mineCorner;
        double hisIrreversiblePiece = (double)this.tempHis - hisCorner;
        this.getPieceDifference(currentPlayer);
        double minePiece = this.tempMine;
        double hisPiece = this.tempHis;
        if (hisPiece == 0.0) {
            hisPiece+=1.0;
        }
        if (hisCorner == 0.0) {
            hisCorner+=1.0;
        }
        if (hisBord == 0.0) {
            hisBord+=1.0;
        }
        if (hisCornerCloseness == 0.0) {
            hisCornerCloseness+=1.0;
        }
        if (hisIrreversiblePiece == 0.0) {
            hisIrreversiblePiece+=1.0;
        }
        return 3.0 * minePiece / hisPiece + 10.0 * mineCorner / hisCorner + 10.0 * mineIrreversiblePiece / hisIrreversiblePiece + 8.0 * mineBord / hisBord + 5.0 * mineCornerCloseness / hisCornerCloseness;
    }

    public double getMobilityScore(int currentPlayer) {
        this.getMobility(currentPlayer);
        double mineMobility = this.tempMine;
        double hisMobility = this.tempHis;
        this.getFrontierDiscs(currentPlayer);
        double mineFrontierDiscs = this.tempMine;
        double hisFrontierDiscs = this.tempHis;
        if (mineFrontierDiscs == 0.0) {
            mineFrontierDiscs+=1.0;
        }
        if (hisMobility == 0.0) {
            hisMobility+=1.0;
            mineMobility*=3.0;
        }
        return 3.0 * hisFrontierDiscs / mineFrontierDiscs + 8.0 * mineMobility / hisMobility;
    }

    public double getPlaceScore(int currentPlayer) {
        int out = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (this.board[i][j] == 0 || this.board[i][j] != currentPlayer) continue;
                out+=this.positionMatrixScore[i][j];
            }
        }
        return out;
    }

    public double getPieceDifference(int currentPlayer) {
        int his = 0;
        int mine = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (this.board[i][j] == currentPlayer) {
                    ++mine;
                    continue;
                }
                if (this.board[i][j] != - currentPlayer) continue;
                ++his;
            }
        }
        this.tempMine = mine;
        this.tempHis = his;
        return mine - his;
    }

    private void getCornerOccupacy(int currentPlayer) {
        int mine = 0;
        int his = 0;
        for (int i = 0; i <= 1; ++i) {
            for (int j = 0; j <= 1; ++j) {
                if (this.board[i * 7][j * 7] == currentPlayer) {
                    ++mine;
                    continue;
                }
                if (this.board[i * 7][j * 7] != - currentPlayer) continue;
                ++his;
            }
        }
        this.tempMine = mine;
        this.tempHis = his;
    }

    private void getCornerCloseness(int currentPlayer) {
        int mine = 0;
        int his = 0;
        for (int i = 0; i <= 1; ++i) {
            for (int j = 0; j <= 1; ++j) {
                if (this.board[i * 7][j * 7] != 0) continue;
                int i1 = i * 7;
                int j1 = j * 7;
                int i2 = -2 * i + 1;
                int j2 = -2 * j + 1;
                if (this.board[i1 + i2][j1] == currentPlayer) {
                    ++mine;
                } else if (this.board[i1 + i2][j1] == - currentPlayer) {
                    ++his;
                }
                if (this.board[i1][j1 + j2] == currentPlayer) {
                    ++mine;
                } else if (this.board[i1][j1 + j2] == - currentPlayer) {
                    ++his;
                }
                if (this.board[i1 + i2][j1 + j2] == currentPlayer) {
                    ++mine;
                    continue;
                }
                if (this.board[i1 + i2][j1 + j2] != - currentPlayer) continue;
                ++his;
            }
        }
        this.tempMine = mine;
        this.tempHis = his;
    }

    private void getBordPiece(int currentPlayer) {
        int mine = 0;
        int his = 0;
        for (int i = 0; i <= 1; ++i) {
            for (int j = 2; j <= 5; ++j) {
                if (this.board[i * 7][j] == currentPlayer) {
                    ++mine;
                } else if (this.board[i * 7][j] == - currentPlayer) {
                    // empty if block
                }
                ++his;
                if (this.board[j][i * 7] == currentPlayer) {
                    ++mine;
                } else if (this.board[j][i * 7] == - currentPlayer) {
                    // empty if block
                }
                ++his;
            }
        }
        this.tempMine = mine;
        this.tempHis = his;
    }

    private void getMobility(int currentPlayer) {
        int[] possibleMove = new int[121];
        this.getAllPossibleMove(possibleMove, currentPlayer);
        int i = 0;
        while (possibleMove[i] != -1) {
            i+=2;
        }
        int mine = i / 2;
        this.getAllPossibleMove(possibleMove, - currentPlayer);
        i = 0;
        while (possibleMove[i] != -1) {
            i+=2;
        }
        int his = i / 2;
        this.tempMine = mine;
        this.tempHis = his;
    }

    private void getFrontierDiscs(int currentPlayer) {
        int mine = 0;
        int his = 0;
        for (int i = 1; i < 7; ++i) {
            for (int j = 1; j < 7; ++j) {
                if (this.board[i][j] == 0 || !this.isFrontierDisc(i, j)) continue;
                if (this.board[i][j] == currentPlayer) {
                    ++mine;
                    continue;
                }
                if (this.board[i][j] != - currentPlayer) continue;
                ++his;
            }
        }
        this.tempMine = mine;
        this.tempHis = his;
    }

    private boolean isFrontierDisc(int i, int j) {
        boolean out = false;
        for (int ii = i - 1; !(out || ii > i + 1); ++ii) {
            for (int jj = j - 1; !(out || jj > j + 1); out|=this.board[ii][jj] == 0, ++jj) {
            }
        }
        return out;
    }

    private void getNbIrreversiblePiece(int currentPlayer) {
        int mine = 0;
        int his = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (this.board[i][j] == 0) continue;
                if (this.board[i][j] == currentPlayer && this.isIrreversiblePiece(i, j, currentPlayer)) {
                    ++mine;
                    continue;
                }
                if (this.board[i][j] != - currentPlayer || !this.isIrreversiblePiece(i, j, - currentPlayer)) continue;
                ++his;
            }
        }
        this.tempMine = mine;
        this.tempHis = his;
    }

    private boolean checkHorizontallyLeft2Right(int col, int row, int currentPlayer) {
        int oppositePlayer = - currentPlayer;
        for (int i = col + 2; i < 8; ++i) {
            if (this.board[i - 1][row] == oppositePlayer && this.board[i][row] == currentPlayer) {
                return true;
            }
            if (this.board[i - 1][row] == oppositePlayer && this.board[i][row] == oppositePlayer) continue;
            return false;
        }
        return false;
    }

    private boolean checkHorizontallyRight2Left(int col, int row, int currentPlayer) {
        int oppositePlayer = - currentPlayer;
        for (int i = col - 2; i >= 0; --i) {
            if (this.board[i + 1][row] == oppositePlayer && this.board[i][row] == currentPlayer) {
                return true;
            }
            if (this.board[i + 1][row] == oppositePlayer && this.board[i][row] == oppositePlayer) continue;
            return false;
        }
        return false;
    }

    private boolean checkVerticallyTop2Bottom(int col, int row, int currentPlayer) {
        int oppositePlayer = - currentPlayer;
        for (int j = row + 2; j < 8; ++j) {
            if (this.board[col][j - 1] == oppositePlayer && this.board[col][j] == currentPlayer) {
                return true;
            }
            if (this.board[col][j - 1] == oppositePlayer && this.board[col][j] == oppositePlayer) continue;
            return false;
        }
        return false;
    }

    private boolean checkVerticallyBottom2Top(int col, int row, int currentPlayer) {
        int oppositePlayer = - currentPlayer;
        for (int j = row - 2; j >= 0; --j) {
            if (this.board[col][j + 1] == oppositePlayer && this.board[col][j] == currentPlayer) {
                return true;
            }
            if (this.board[col][j + 1] == oppositePlayer && this.board[col][j] == oppositePlayer) continue;
            return false;
        }
        return false;
    }

    private boolean checkDiagonallyTopLeft2BottomRight(int col, int row, int currentPlayer) {
        int oppositePlayer = - currentPlayer;
        int j = row + 2;
        for (int i = col + 2; j < 8 && i < 8; ++j, ++i) {
            if (this.board[i - 1][j - 1] == oppositePlayer && this.board[i][j] == currentPlayer) {
                return true;
            }
            if (this.board[i - 1][j - 1] == oppositePlayer && this.board[i][j] == oppositePlayer) continue;
            return false;
        }
        return false;
    }

    private boolean checkDiagonallyBottomRight2TopLeft(int col, int row, int currentPlayer) {
        int oppositePlayer = - currentPlayer;
        int j = row - 2;
        for (int i = col - 2; j >= 0 && i >= 0; --j, --i) {
            if (this.board[i + 1][j + 1] == oppositePlayer && this.board[i][j] == currentPlayer) {
                return true;
            }
            if (this.board[i + 1][j + 1] == oppositePlayer && this.board[i][j] == oppositePlayer) continue;
            return false;
        }
        return false;
    }

    private boolean checkDiagonallyTopRight2BottomLeft(int col, int row, int currentPlayer) {
        int oppositePlayer = - currentPlayer;
        int j = row + 2;
        for (int i = col - 2; j < 8 && i >= 0; ++j, --i) {
            if (this.board[i + 1][j - 1] == oppositePlayer && this.board[i][j] == currentPlayer) {
                return true;
            }
            if (this.board[i + 1][j - 1] == oppositePlayer && this.board[i][j] == oppositePlayer) continue;
            return false;
        }
        return false;
    }

    private boolean checkDiagonallyBottomLeft2TopRight(int col, int row, int currentPlayer) {
        int oppositePlayer = - currentPlayer;
        int j = row - 2;
        for (int i = col + 2; j >= 0 && i < 8; --j, ++i) {
            if (this.board[i - 1][j + 1] == oppositePlayer && this.board[i][j] == currentPlayer) {
                return true;
            }
            if (this.board[i - 1][j + 1] == oppositePlayer && this.board[i][j] == oppositePlayer) continue;
            return false;
        }
        return false;
    }

    private boolean checkIrreversiblePieceHorizontallyRight(int col, int row, int currentPlayer) {
        for (int i = col + 1; i < 8; ++i) {
            if (this.board[i][row] == currentPlayer) continue;
            return false;
        }
        return true;
    }

    private boolean checkIrreversiblePieceHorizontallyLeft(int col, int row, int currentPlayer) {
        for (int i = col - 1; i >= 0; --i) {
            if (this.board[i][row] == currentPlayer) continue;
            return false;
        }
        return true;
    }

    private boolean checkIrreversiblePieceVerticallyBottom(int col, int row, int currentPlayer) {
        for (int j = row + 1; j < 8; ++j) {
            if (this.board[col][j] == currentPlayer) continue;
            return false;
        }
        return true;
    }

    private boolean checkIrreversibleVerticallyTop(int col, int row, int currentPlayer) {
        for (int j = row - 1; j >= 0; --j) {
            if (this.board[col][j] == currentPlayer) continue;
            return false;
        }
        return true;
    }

    private boolean checkIrreversiblePieceDiagonallyBottomRight(int col, int row, int currentPlayer) {
        int j = row + 1;
        for (int i = col + 1; j < 8 && i < 8; ++j, ++i) {
            if (this.board[i][j] == currentPlayer) continue;
            return false;
        }
        return true;
    }

    private boolean checkIrreversiblePieceDiagonallyTopLeft(int col, int row, int currentPlayer) {
        int j = row - 1;
        for (int i = col - 1; j >= 0 && i >= 0; --j, --i) {
            if (this.board[i][j] == currentPlayer) continue;
            return false;
        }
        return true;
    }

    private boolean checkIrreversiblePieceDiagonallyBottomLeft(int col, int row, int currentPlayer) {
        int j = row + 1;
        for (int i = col - 1; j < 8 && i >= 0; ++j, --i) {
            if (this.board[i][j] == currentPlayer) continue;
            return false;
        }
        return true;
    }

    private boolean checkIrreversiblePieceDiagonallyTopRight(int col, int row, int currentPlayer) {
        int j = row - 1;
        for (int i = col + 1; j >= 0 && i < 8; --j, ++i) {
            if (this.board[i][j] == currentPlayer) continue;
            return false;
        }
        return true;
    }

    private void actionHorizontallyLeft2Right(int col, int row, int currentPlayer) {
        for (int i = col + 1; i < 8 && this.board[i][row] == - currentPlayer; ++i) {
            this.board[i][row] = currentPlayer;
        }
    }

    private void actionHorizontallyRight2Left(int col, int row, int currentPlayer) {
        for (int i = col - 1; i >= 0 && this.board[i][row] == - currentPlayer; --i) {
            this.board[i][row] = currentPlayer;
        }
    }

    private void actionVerticallyTop2Bottom(int col, int row, int currentPlayer) {
        for (int j = row + 1; j < 8 && this.board[col][j] == - currentPlayer; ++j) {
            this.board[col][j] = currentPlayer;
        }
    }

    private void actionVerticallyBottom2Top(int col, int row, int currentPlayer) {
        for (int j = row - 1; j >= 0 && this.board[col][j] == - currentPlayer; --j) {
            this.board[col][j] = currentPlayer;
        }
    }

    private void actionDiagonallyTopLeft2BottomRight(int col, int row, int currentPlayer) {
        int j = row + 1;
        for (int i = col + 1; j < 8 && i < 8 && this.board[i][j] == - currentPlayer; ++j, ++i) {
            this.board[i][j] = currentPlayer;
        }
    }

    private void actionDiagonallyBottomRight2TopLeft(int col, int row, int currentPlayer) {
        int j = row - 1;
        for (int i = col - 1; j >= 0 && i >= 0 && this.board[i][j] == - currentPlayer; --j, --i) {
            this.board[i][j] = currentPlayer;
        }
    }

    private void actionDiagonallyTopRight2BottomLeft(int col, int row, int currentPlayer) {
        int j = row + 1;
        for (int i = col - 1; j < 8 && i >= 0 && this.board[i][j] == - currentPlayer; ++j, --i) {
            this.board[i][j] = currentPlayer;
        }
    }

    private void actionDiagonallyBottomLeft2TopRight(int col, int row, int currentPlayer) {
        int j = row - 1;
        for (int i = col + 1; j >= 0 && i < 8 && this.board[i][j] == - currentPlayer; --j, ++i) {
            this.board[i][j] = currentPlayer;
        }
    }
}
