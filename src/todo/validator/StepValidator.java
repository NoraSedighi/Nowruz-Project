package todo.validator;

import db.Database;
import db.Entity;
import db.Validator;
import db.db.exceptions.*;
import todo.entity.*;

public class StepValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if(!(entity instanceof Step))
            throw new IllegalArgumentException("Input must be of type step");

        Step step = (Step) entity;

        if(step.getTitle() == null || step.getTitle().isEmpty())
            throw new InvalidEntityException("Step title cannot be empty or null");

        try{
            Entity referencedEntity = Database.get(step.getTaskRef());
            if(!(referencedEntity instanceof Task))
                throw new InvalidEntityException("Referenced entity is not a task");
        }
        catch(EntityNotFoundException e){
            throw new InvalidEntityException("Referenced task with ID " + step.getTaskRef() + "does not exist");
        }
    }
}
