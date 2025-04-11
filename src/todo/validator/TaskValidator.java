package todo.validator;

import db.Entity;
import db.Validator;
import db.db.exceptions.InvalidEntityException;
import todo.entity.Task;

public class TaskValidator implements Validator {

    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if(!(entity instanceof Task))
            throw new IllegalArgumentException("Input must be of type task");

        Task task = (Task) entity;

        if(task.getTitle() == null || task.getTitle().isEmpty())
            throw new InvalidEntityException("Task title cannot be empty or null");
    }
}
