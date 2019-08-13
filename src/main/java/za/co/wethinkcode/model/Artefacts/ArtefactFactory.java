package za.co.wethinkcode.model.Artefacts;

public abstract class ArtefactFactory {
    public static Artefact getArtefact(int luck, int level) {
        if (luck % 5 == 0) {
            return new Weapon((int) (Math.random() * 10), level);
        } else if (luck % 3 == 0) {
            return new Armor((int) (Math.random() * 10), level);
        } else {
            return new Helm((int) (Math.random() * 10), level);
        }
    }

    public static Artefact getArtefact(String name) {
        if (name.equals("Dragon Slayer") || name.equals("Mjolnir") || name.equals("Katana"))
            return new Weapon(name);
        else if (name.equals("Vibranium Suit") || name.equals("Shield") || name.equals("Leather Suit"))
            return new Armor(name);
        return null;
    }
}
