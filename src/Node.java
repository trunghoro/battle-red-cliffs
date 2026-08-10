public class Node {

    public final byte[] hp;
    public final int parentIndex;
    public final int attackedShip;

    public Node(
            byte[] hp,
            int parentIndex,
            int attackedShip) {

        this.hp = hp;
        this.parentIndex = parentIndex;
        this.attackedShip = attackedShip;
    }
}