import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Solver {

    private final BattleBoard board;

    private record SearchPath(
            byte[] hp,
            List<Attack> attacks) {
    }

    /**
     * Creates a solver for the specified battle board.
     *
     * @param board the board that the solver will search
     */
    public Solver(BattleBoard board) {
        this.board = board;
    }

    /**
     * Finds every shortest attack sequence.
     *
     * @param initialHp the initial health of every ship
     * @return all attack sequences having the minimum length
     * @throws IllegalArgumentException if the initial health array is invalid
     */
    public List<List<Attack>> solveAll(byte[] initialHp) {
        if (initialHp == null
                || initialHp.length != board.getShipCount()) {
            throw new IllegalArgumentException(
                    "Initial HP array length must be "
                            + board.getShipCount());
        }

        Queue<SearchPath> queue = new ArrayDeque<>();
        List<List<Attack>> solutions = new ArrayList<>();
        int shortestLength = -1;

        queue.offer(new SearchPath(
                initialHp.clone(),
                List.of()));

        while (!queue.isEmpty()) {
            SearchPath current = queue.poll();

            if (shortestLength != -1
                    && current.attacks().size() > shortestLength) {
                break;
            }

            if (board.isDestroyed(current.hp())) {
                if (shortestLength == -1) {
                    shortestLength = current.attacks().size();
                }

                solutions.add(current.attacks());
                continue;
            }

            for (int ship = 0; ship < board.getShipCount(); ship++) {
                if (current.hp()[ship] <= 0) {
                    continue;
                }

                byte[] nextHp = board.attack(current.hp(), ship);
                List<Attack> nextAttacks = new ArrayList<>(current.attacks());
                nextAttacks.add(board.getAttack(ship));

                queue.offer(new SearchPath(
                        nextHp,
                        List.copyOf(nextAttacks)));
            }
        }

        return solutions;
    }

}
