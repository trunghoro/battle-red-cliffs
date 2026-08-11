import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Solver {

    private final BattleBoard board;

    /**
     * Creates a solver for the specified battle board.
     *
     * @param board the board that the solver will search
     */
    public Solver(BattleBoard board) {
        this.board = board;
    }

    /**
     * Encodes an HP array so it can be stored in the visited set.
     *
     * @param hp the health state to encode
     * @return a string representation of the health state
     */
    private String encode(byte[] hp) {
        return Arrays.toString(hp);
    }

    /**
     * Walks from the goal state back to the root and restores the attack order.
     *
     * @param nodes all states generated during the search
     * @param goalIndex the index of the goal state
     * @return the attacks in the order they should be performed
     */
    private List<Attack> reconstruct(
            List<Node> nodes,
            int goalIndex) {

        List<Attack> result = new ArrayList<>();
        int index = goalIndex;

        while (true) {
            // Start at the goal and follow parent links toward the root.
            Node node = nodes.get(index);

            if (node.parentIndex == -1) {
                break;
            }

            result.add(
                    board.getAttack(
                            node.attackedShip));

            // Move to the state that created the current state.
            index = node.parentIndex;
        }

        // Backtracking produces reverse order, so restore execution order.
        Collections.reverse(result);
        return result;
    }

    /**
     * Finds the shortest attack sequence using breadth-first search.
     *
     * @param initialHp the initial health of every ship
     * @return the shortest attack sequence, or an empty list if no solution exists
     * @throws IllegalArgumentException if the initial health array is invalid
     */
    public List<Attack> solve(byte[] initialHp) {

        if (initialHp == null
                || initialHp.length != board.getShipCount()) {
            throw new IllegalArgumentException(
                    "Initial HP array length must be "
                            + board.getShipCount());
        }

        // Queue controls BFS order: the oldest state is processed first.
        Queue<Integer> queue = new ArrayDeque<>();

        // Nodes are kept so the final attack sequence can be reconstructed.
        List<Node> nodes = new ArrayList<>();

        // Each HP state should be processed only once.
        Set<String> visited = new HashSet<>();

        // The root has no parent and no previous attack.
        Node root = new Node(
                initialHp.clone(),
                -1,
                -1);

        nodes.add(root);
        queue.offer(0);
        visited.add(encode(initialHp));

        while (!queue.isEmpty()) {
            int currentIndex = queue.poll();
            Node current = nodes.get(currentIndex);

            // The first goal found by BFS uses the fewest attacks.
            if (board.isDestroyed(current.hp)) {
                return reconstruct(
                        nodes,
                        currentIndex);
            }

            // Try attacking every ship that is still alive.
            for (int ship = 0; ship < board.getShipCount(); ship++) {
                if (current.hp[ship] <= 0) {
                    continue;
                }

                // BattleBoard applies damage and any resulting explosion chain.
                byte[] nextHp = board.attack(
                        current.hp,
                        ship);

                String key = encode(nextHp);

                if (!visited.add(key)) {
                    // Ignore states that have already been reached.
                    continue;
                }

                // Store the parent and the attack used to create this state.
                Node nextNode = new Node(
                        nextHp,
                        currentIndex,
                        ship);

                nodes.add(nextNode);
                int nextIndex = nodes.size() - 1;
                queue.offer(nextIndex);
            }
        }

        // No solution exists for the supplied state.
        return List.of();
    }

}
