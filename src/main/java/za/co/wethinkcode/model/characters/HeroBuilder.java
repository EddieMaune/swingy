package za.co.wethinkcode.model.characters;

import lombok.Data;
import za.co.wethinkcode.model.Artefacts.ArtefactFactory;
import za.co.wethinkcode.model.Artefacts.Weapon;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class HeroBuilder {
   private String name;
    private String heroClass;

    public Hero build() {
        return new Hero(name, heroClass);
    }

    public Hero build(ResultSet rs) throws SQLException {
        Hero hero = new Hero(rs.getString("name"), rs.getString("class"));

        hero.setHp(rs.getInt("hp"));
        hero.setAttack(rs.getInt("attack"));
        hero.setDefense(rs.getInt("defense"));
        hero.setXp(rs.getInt("xp"));
        hero.setLevel(rs.getInt("level"));
        hero.setId(rs.getInt("id"));
        hero.setArtefact((rs.getString("artefact").equals("none") ? null : ArtefactFactory.getArtefact(rs.getString("artefact"))));
        return hero;
    }
}
