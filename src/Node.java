public class Node {

    public final byte[] hp;
    public final int parentIndex;
    public final int attackedShip;

    /**
     * Creates one compact search state.
     *
     * @param hp the health of every ship in this state
     * @param parentIndex the parent state index, or -1 for the root
     * @param attackedShip the ship attacked to create this state
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
