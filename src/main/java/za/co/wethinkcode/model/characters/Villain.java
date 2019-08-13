package za.co.wethinkcode.model.characters;

import lombok.Data;

@Data
public class Villain {
    protected String villainClass;
    protected int attack;
    protected int defense;
    protected int hp;

    public Villain(String villainClass, int heroLevel) {

        this.villainClass = villainClass;
        switch (villainClass) {
            case "Troll":
                hp = 200;
                attack = 4 * heroLevel;
                defense = 6 * heroLevel;
                break;
            case "Witch":
                hp = 300 ;
                attack = 6 * heroLevel;
                defense = 4 * heroLevel;
                break;
            case "Dragon":
                hp = 500;
                attack = 10 * heroLevel;
                defense = 10 * heroLevel;
        }
    }
}
