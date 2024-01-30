package tests.evalfunc;

public class TestMove {

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public TestMove(int sX, int sY, int eX, int eY) {
        startX = sX;
        startY = sY;
        endX = eX;
        endY = eY;
    }

    public TestMove(int startSlot, int endSlot) {
        startX = startSlot % 8;
        startY = Math.floorDiv(startSlot, 8);
        endX = endSlot % 8;
        endY = Math.floorDiv(endSlot, 8);
    }

    public String toString() {
        return String.format(
            "[%d, %d] -> [%d, %d]", startX, startY, endX, endY
        );
    }
}
