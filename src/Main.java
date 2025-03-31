import db.db.exceptions.EntityNotFoundException;
import db.db.exceptions.InvalidEntityException;
import example.Human;
import db.*;
import example.HumanValidator;

public class Main {
    public static void main(String[] args) throws InvalidEntityException {
        Database.registerValidator(Human.HUMAN_ENTITY_CODE, new HumanValidator());

        Human ali = new Human("Ali", -10);
        Database.add(ali);
    }
}
