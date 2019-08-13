package za.co.wethinkcode.model.characters;

public abstract class VillainFactory {
    public static Villain createVillain(int n, int heroLevel) {
        if (n % 8 == 0) {
            return new Villain("Dragon", heroLevel);
        } else if (n % 3 == 0) {
            return new Villain("Witch", heroLevel);

        } else {
            return new Villain("Troll", heroLevel);
        }
    }
}
