
import java.util.ArrayDeque;
import java.util.Queue;

public class BattleBoard {

    private final int rows;
    private final int cols;

    private final int shipCount;

    private final int[] shipRows;
    private final int[] shipCols;

    public int getShipCount() {
        return shipCount;
    }

    public Attack getAttack(int shipIndex) {

        return new Attack(
                shipRows[shipIndex],
                shipCols[shipIndex]
        );
    }

    public BattleBoard(int[][] board) {

        this.rows = board.length;
        this.cols = board[0].length;

        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] > 0) {
                    count++;
                }
            }
        }

        this.shipCount = count;

        this.shipRows = new int[count];
        this.shipCols = new int[count];

        int index = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (board[r][c] > 0) {

                    shipRows[index] = r;
                    shipCols[index] = c;

                    index++;
                }
            }
        }
    }

    public long createInitialState(int[][] board) {

        long state = 0;

        for (int i = 0; i < shipCount; i++) {

            int row = shipRows[i];
            int col = shipCols[i];

            int hp = board[row][col];

            state = setHp(state, i, hp);
        }

        return state;
    }

    private long setHp(long state, int shipIndex, int hp) {

        int shift = shipIndex * 3;

        long mask = 7L << shift;

        state &= ~mask;

        state |= ((long) hp << shift);

        return state;
    }

    public int getHp(long state, int shipIndex) {

        int shift = shipIndex * 3;

        return (int) ((state >> shift) & 7L);
    }



    private int findFirstAliveShip(
            byte[] hp,
            int row,
            int col,
            int dr,
            int dc) {

        int r = row + dr;
        int c = col + dc;

        while (r >= 0 && r < rows
                && c >= 0 && c < cols) {

            for (int i = 0; i < shipCount; i++) {

                if (shipRows[i] == r
                        && shipCols[i] == c
                        && hp[i] > 0) {

                    return i;
                }
            }

            r += dr;
            c += dc;
        }

        return -1;
    }

    public byte[] attack(byte[] current, int shipIndex) {

        byte[] next = current.clone();

        if (next[shipIndex] <= 0) {
            return next;
        }

        next[shipIndex]--;

        if (next[shipIndex] == 0) {
            explode(next, shipIndex);
        }

        return next;
    }

    private void explode(byte[] hp, int startShip) {

        Queue<Integer> queue = new ArrayDeque<>();

        queue.offer(startShip);

        int[][] directions = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
        };

        while (!queue.isEmpty()) {

            int explodingShip = queue.poll();

            int row = shipRows[explodingShip];
            int col = shipCols[explodingShip];

            for (int[] dir : directions) {

                int target = findFirstAliveShip(
                        hp,
                        row,
                        col,
                        dir[0],
                        dir[1]
                );

                if (target == -1) {
                    continue;
                }

                hp[target]--;

                if (hp[target] == 0) {
                    queue.offer(target);
                }
            }
        }
    }

    public boolean isDestroyed(byte[] hp) {

        for (byte value : hp) {

            if (value > 0) {
                return false;
            }
        }

        return true;
    }

    public byte[] createInitialHp(int[][] board) {

        byte[] hp = new byte[shipCount];

        for (int i = 0; i < shipCount; i++) {

            hp[i] = (byte) board[
                    shipRows[i]
            ][
                    shipCols[i]
            ];
        }

        return hp;
    }

    

}
