package za.co.wethinkcode.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

//Linking the validator I had shown above.
@Constraint(validatedBy = {HeroClassValidator.class})
//This constraint annotation can be used only on fields and method parameters.
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface HeroClass {
        //The message to return when the instance of MyAddress fails the validation.
        String message() default "Hero can be either a Knight, Elf or Dwarf";

        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}
