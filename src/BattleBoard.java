
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class BattleBoard {

    private static final int[][] DIRECTIONS = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    private final int rows;
    private final int cols;

    private final int shipCount;

    private final int[] shipRows;
    private final int[] shipCols;
    private final int[][] shipAt;

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

        if (board == null || board.length == 0 || board.length > 6
                || board[0] == null || board[0].length == 0
                || board[0].length > 6) {
            throw new IllegalArgumentException("Board size must be between 1 and 6");
        }

        this.rows = board.length;
        this.cols = board[0].length;

        int count = 0;

        for (int r = 0; r < rows; r++) {
            if (board[r] == null || board[r].length != cols) {
                throw new IllegalArgumentException("Board must be rectangular");
            }
            for (int c = 0; c < cols; c++) {
                if (board[r][c] < 0 || board[r][c] > 4) {
                    throw new IllegalArgumentException("Health must be between 0 and 4");
                }
                if (board[r][c] > 0) {
                    count++;
                }
            }
        }

        this.shipCount = count;

        this.shipRows = new int[count];
        this.shipCols = new int[count];
        this.shipAt = new int[rows][cols];

        for (int[] row : shipAt) {
            Arrays.fill(row, -1);
        }

        int index = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (board[r][c] > 0) {

                    shipRows[index] = r;
                    shipCols[index] = c;
                    shipAt[r][c] = index;

                    index++;
                }
            }
        }
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

            int ship = shipAt[r][c];

            if (ship != -1 && hp[ship] > 0) {
                return ship;
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

        while (!queue.isEmpty()) {

            int explodingShip = queue.poll();

            int row = shipRows[explodingShip];
            int col = shipCols[explodingShip];

            for (int[] dir : DIRECTIONS) {

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
