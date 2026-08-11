public class Node {

    public final byte[] hp;
    public final int parentIndex;
    public final int attackedShip;

    /**
     * Creates a state in the search tree.
     *
     * @param hp health points of every ship in this state
     * @param parentIndex index of the previous state in the nodes list
     * @param attackedShip ship attacked to create this state
     */
    public Node(
            byte[] hp,
            int parentIndex,
            int attackedShip) {

        this.hp = hp;
        this.parentIndex = parentIndex;
        this.attackedShip = attackedShip;
    }
}
