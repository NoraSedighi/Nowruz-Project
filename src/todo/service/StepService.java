package todo.service;

import db.Database;
import db.db.exceptions.*;
import todo.entity.Step;

public class StepService {
    public static Step createStep(int taskRef, String title) throws InvalidEntityException{
        return new Step(0, title, Step.Status.NotStarted, taskRef);
    }

    public static Step saveStep(Step step) throws InvalidEntityException{
        Database.add(step);
        System.out.println("Step saved with title: " + step.getTitle());
        return step;
    }

    public static void updateStep(Step step) throws InvalidEntityException, EntityNotFoundException{
        Database.update(step);
        System.out.println("Step updated with ID: " + step.id);
    }

    public static void deleteStep(int id) {
        try {
            Database.delete(id);
            System.out.println("Step deleted with ID: " + id);
        } catch (EntityNotFoundException e) {
            System.out.println("Failed to delete step: " + e.getMessage());
        }
    }

    public static Step getStep(int id) throws EntityNotFoundException {
        try {
            return (Step) Database.get(id);
        } catch (EntityNotFoundException e) {
            System.out.println("Step not found with ID: " + id);
            throw e;
        }
    }
}
