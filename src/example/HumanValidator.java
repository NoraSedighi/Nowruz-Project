package example;

import db.Entity;
import db.Validator;
import db.db.exceptions.InvalidEntityException;

public class HumanValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException{
        if(!(entity instanceof Human)){
            throw new IllegalArgumentException("Input must be of type human");
        }

        Human human = (Human) entity;

        if(human.name == null)
            throw new InvalidEntityException("Name cannot be empty or null");
        if(human.age < 0)
            throw new InvalidEntityException("Age must be a positive integer");
    }
}
