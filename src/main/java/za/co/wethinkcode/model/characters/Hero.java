package za.co.wethinkcode.model.characters;

import lombok.Data;
import za.co.wethinkcode.constraints.HeroClass;
import za.co.wethinkcode.model.Artefacts.Artefact;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Hero {
    protected String name;
    @HeroClass
    protected String heroClass;
    @NotNull
    protected int level;
    protected int xp;
    protected int attack;
    protected int defense;
    protected int hp;
    private Coordinates position;
    private int id;
    private Artefact artefact;

    public Hero(String name, String heroClass) {
        this.name = name;
        this.heroClass = heroClass;
        level = 0;
        hp = 100;
        xp = 0;
        position = new Coordinates(0,0);
        switch (heroClass) {
            case "Knight":
                attack = 6;
                defense = 4;
                break;
            case "Elf":
                attack = 8;
                defense = 2;
                break;
            case "Dwarf":
                attack = 3;
                defense = 7;
                break;
        }
    }


}
