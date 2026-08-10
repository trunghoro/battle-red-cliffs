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

    public Solver(BattleBoard board) {
        this.board = board;
    }

    private String encode(byte[] hp) {
        return Arrays.toString(hp);
    }

    private List<Attack> reconstruct(
        List<Node> nodes,
        int goalIndex) {

        List<Attack> result =
                new ArrayList<>();

        int index = goalIndex;

        while (true) {

            Node node = nodes.get(index);

            if (node.parentIndex == -1) {
                break;
            }

            result.add(
                    board.getAttack(
                            node.attackedShip
                    )
            );

            index = node.parentIndex;
        }

        Collections.reverse(result);

        return result;
    }

    public List<Attack> solve(byte[] initialHp) {

        Queue<Integer> queue = new ArrayDeque<>();

        List<Node> nodes = new ArrayList<>();

        Set<String> visited = new HashSet<>();

        Node root = new Node(
                initialHp.clone(),
                -1,
                -1
        );

        nodes.add(root);

        queue.offer(0);

        visited.add(encode(initialHp));

        while (!queue.isEmpty()) {

            int currentIndex = queue.poll();

            Node current = nodes.get(currentIndex);

            if (board.isDestroyed(current.hp)) {

                return reconstruct(
                        nodes,
                        currentIndex
                );
            }

            for (int ship = 0;
                    ship < board.getShipCount();
                    ship++) {

                if (current.hp[ship] <= 0) {
                    continue;
                }

                byte[] nextHp
                        = board.attack(
                                current.hp,
                                ship
                        );

                String key = encode(nextHp);

                if (!visited.add(key)) {
                    continue;
                }

                Node nextNode
                        = new Node(
                                nextHp,
                                currentIndex,
                                ship
                        );

                nodes.add(nextNode);

                int nextIndex
                        = nodes.size() - 1;

                queue.offer(nextIndex);
            }
        }

        return List.of();
    }

    
}
