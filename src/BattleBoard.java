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
    private final byte[] initialHp;

    /**
     * Returns the number of ships on the board.
     *
     * @return the number of ships
     */
    public int getShipCount() {
        // Solver uses this value to iterate over every ship.
        return shipCount;
    }

    /**
     * Converts an internal ship index into its board position.
     *
     * @param shipIndex the internal index of the ship
     * @return the board position of the ship
     * @throws IllegalArgumentException if the ship index is invalid
     */
    public Attack getAttack(int shipIndex) {
        if (shipIndex < 0 || shipIndex >= shipCount) {
            throw new IllegalArgumentException("Invalid ship index: " + shipIndex);
        }

        return new Attack(
                shipRows[shipIndex],
                shipCols[shipIndex]);
    }

    /**
     * Validates the board and builds mappings between positions and ships.
     * Every cell with health greater than zero represents one ship.
     *
     * @param board the initial board; values must be between 0 and 4
     * @throws IllegalArgumentException if the board dimensions or health value
     *         is invalid
     */
    public BattleBoard(int[][] board) {
        // Validate the general dimensions before accessing board[0].
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
                // Each cell value represents a ship's initial health.
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
        this.initialHp = new byte[count];

        // Mark every cell as empty by default.
        for (int[] row : shipAt) {
            Arrays.fill(row, -1);
        }

        int index = 0;

        // Assign an internal index and position to every ship.
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] > 0) {
                    shipRows[index] = r;
                    shipCols[index] = c;
                    shipAt[r][c] = index;
                    initialHp[index] = (byte) board[r][c];
                    index++;
                }
            }
        }
    }

    /**
     * Finds the first living ship in a direction from the given position.
     * Empty cells and destroyed ships are skipped.
     *
     * @param hp the current health of every ship
     * @param row the starting row
     * @param col the starting column
     * @param dr the row direction increment
     * @param dc the column direction increment
     * @return the index of the first living ship, or -1 if none is found
     */
    private int findFirstAliveShip(
            byte[] hp,
            int row,
            int col,
            int dr,
            int dc) {
        // Start at the adjacent cell in the selected direction.
        int r = row + dr;
        int c = col + dc;

        while (r >= 0 && r < rows
                && c >= 0 && c < cols) {
            int ship = shipAt[r][c];

            if (ship != -1 && hp[ship] > 0) {
                // Return the first living ship encountered.
                return ship;
            }

            // Skip empty cells and destroyed ships.
            r += dr;
            c += dc;
        }

        return -1;
    }

    /**
     * Attacks one ship once and returns the resulting health state.
     * Destroying a ship triggers the chain-reaction explosion.
     *
     * @param current the current health state
     * @param shipIndex the internal index of the ship to attack
     * @return a new health state after the attack
     * @throws IllegalArgumentException if the health state or ship index is invalid
     */
    public byte[] attack(byte[] current, int shipIndex) {
        // Do not modify the current state because BFS still needs it.
        validateHp(current);

        if (shipIndex < 0 || shipIndex >= shipCount) {
            throw new IllegalArgumentException("Invalid ship index: " + shipIndex);
        }

        byte[] next = current.clone();

        if (next[shipIndex] <= 0) {
            // A destroyed ship cannot lose more health.
            return next;
        }

        // Each attack causes exactly one point of damage.
        next[shipIndex]--;

        if (next[shipIndex] == 0) {
            // The destroyed ship triggers the explosion chain.
            explode(next, shipIndex);
        }

        return next;
    }

    /**
     * Processes explosions using BFS. Each explosion damages the nearest
     * living ship in all four directions; newly destroyed ships explode too.
     *
     * @param hp the health state to update during the chain reaction
     * @param startShip the index of the ship that started the explosion
     */
    private void explode(byte[] hp, int startShip) {
        // Queue contains ships waiting to process their explosions.
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(startShip);

        while (!queue.isEmpty()) {
            // Process explosions in BFS order.
            int explodingShip = queue.poll();
            int row = shipRows[explodingShip];
            int col = shipCols[explodingShip];

            for (int[] dir : DIRECTIONS) {
                // Find only the first living ship in this direction.
                int target = findFirstAliveShip(
                        hp,
                        row,
                        col,
                        dir[0],
                        dir[1]);

                if (target == -1) {
                    // There is no living target in this direction.
                    continue;
                }

                // The explosion causes one point of damage.
                hp[target]--;

                if (hp[target] == 0) {
                    // A newly destroyed ship creates another explosion.
                    queue.offer(target);
                }
            }
        }
    }

    /**
     * Returns true when every ship has zero health.
     *
     * @param hp the current health of every ship
     * @return true if all ships are destroyed; otherwise false
     * @throws IllegalArgumentException if the health state is invalid
     */
    public boolean isDestroyed(byte[] hp) {
        validateHp(hp);

        // One living ship means the board is not finished.
        for (byte value : hp) {
            if (value > 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns a copy of the initial health values in ship-index order.
     *
     * @return a copy of the initial health values
     */
    public byte[] createInitialHp() {
        // Return a copy so callers cannot modify the board's internal data.
        return initialHp.clone();
    }

    /**
     * Validates that an HP array matches this board.
     *
     * @param hp the health array to validate
     * @throws IllegalArgumentException if the array length or health value is invalid
     */
    private void validateHp(byte[] hp) {
        if (hp == null || hp.length != shipCount) {
            throw new IllegalArgumentException(
                    "HP array length must be " + shipCount);
        }

        for (byte value : hp) {
            if (value < 0 || value > 4) {
                throw new IllegalArgumentException(
                        "Health must be between 0 and 4");
            }
        }
    }
}
