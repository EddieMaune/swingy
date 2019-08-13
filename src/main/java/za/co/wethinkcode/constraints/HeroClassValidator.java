package za.co.wethinkcode.constraints;

import za.co.wethinkcode.model.characters.Hero;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HeroClassValidator implements ConstraintValidator<HeroClass, String> {

    @Override
    public void initialize(HeroClass heroClass) {

    }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
           if (value.equals("Knight") || value.equals("Elf") || value.equals("Dwarf")) {
               return true;
           }
            return false;
        }
}
