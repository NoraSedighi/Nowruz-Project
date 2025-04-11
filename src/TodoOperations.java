import db.Database;
import db.Entity;
import db.db.exceptions.*;
import todo.entity.*;
import todo.service.TaskService;
import todo.validator.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

public class TodoOperations {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public TodoOperations() {
        Database.registerValidator(Task.TASK_ENTITY_CODE, new TaskValidator());
        Database.registerValidator(Step.STEP_ENTITY_CODE, new StepValidator());
    }

    public static void addTask(Scanner scn){
        try {
            System.out.print("Title: ");
            String title = scn.nextLine().trim();
            if (title.isEmpty())
                throw new InvalidEntityException("Task title cannot be empty");

            System.out.print("Description: ");
            String description = scn.nextLine().trim();
            if (description.isEmpty())
                throw new InvalidEntityException("Task description cannot be empty");

            System.out.print("Due date (yyyy-mm-dd): ");
            String dateInput = scn.nextLine().trim();
            Date dueDate;

            try {
                dateFormat.setLenient(false);
                dueDate = dateFormat.parse(dateInput);
            } catch (ParseException e) {
                throw new InvalidEntityException("Invalid date format. Please use yyyy-mm-dd format");
            }

            Task task = TaskService.createTask(title, description, dueDate);
            task.setStatus(Task.Status.NotStarted);
            TaskService.saveTask(task);

            System.out.println("Task saved successfully.");
            System.out.println("ID: " + task.id);
        } catch (Exception e) {
            System.out.println("Cannot save task.");
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addStep(Scanner scn){
        try{
            System.out.print("TaskID: ");
            int taskId;
            try {
                taskId = Integer.parseInt(scn.nextLine().trim());
            } catch (NumberFormatException e) {
                throw new InvalidEntityException("Task ID must be a valid number");
            }

            try {
                Task task = (Task) Database.get(taskId);
                if (task == null) {
                    throw new EntityNotFoundException("Cannot find task with ID=" + taskId);
                }
            } catch (EntityNotFoundException e) {
                throw new InvalidEntityException("Cannot find task with ID=" + taskId);
            }

            System.out.print("Title: ");
            String title = scn.nextLine().trim();
            if (title.isEmpty())
                throw new InvalidEntityException("Step title cannot be empty");

            Step step = new Step(0, title, Step.Status.NotStarted, taskId);
            Database.add(step);

            System.out.println("Task saved successfully.");
            System.out.println("ID: " + step.id);
            System.out.println("Creation Date: " + step.getCreationDate());
        } catch (Exception e){
            System.out.println("Cannot save step.");
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void handleDelete(Scanner scn) {
        System.out.println("Please enter the ID of the entity you want to delete");
        System.out.println("ID: ");
        try{
            int id = Integer.parseInt(scn.nextLine().trim());

            Entity entity;
            try{
                entity = Database.get(id);
            } catch (EntityNotFoundException e){
                throw new EntityNotFoundException("Entity with ID=" + id + " not found");
            }

            if(entity instanceof Task)
                deleteStepsForTask(id);

            Database.delete(id);
            System.out.println("Entity with ID=" + id + " successfully deleted.");

        }catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number for ID");
        } catch (EntityNotFoundException e) {
            System.out.println("Cannot delete entity. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Cannot delete entity. Error: " + e.getMessage());
        }
    }

    public static void deleteStepsForTask(int taskId) {
        ArrayList<Entity> allSteps = Database.getAll(Step.STEP_ENTITY_CODE);

        for(Entity entity : allSteps){
            Step step = (Step) entity;
            if(step.getTaskRef() == taskId){
                Database.delete(step.id);
                System.out.println("Deleted associated step with ID=" + step.id);
            }
        }
    }

    public static void updateTask(Scanner scn) {
        try {
            System.out.println("ID: ");
            int taskId = Integer.parseInt(scn.nextLine().trim());

            System.out.println("Field: ");
            String field = scn.nextLine().trim().toLowerCase();

            System.out.println("New Value: ");
            String value = scn.nextLine().trim();

            Task task = (Task) Database.get(taskId);

            switch (field){
                case "title":
                    String oldTitle = task.getTitle();
                    task.setTitle(field);
                    Database.update(task);
                    printTaskUpdateSuccess("title", oldTitle, value, task);
                    break;

                case "description":
                    String oldDescription = task.getDescription();
                    task.setDescription(field);
                    Database.update(task);
                    printTaskUpdateSuccess("description", oldDescription, value, task);
                    break;

                case "duedate":
                    try{
                        Date oldDate = task.getDueDate();
                        Date newDate = dateFormat.parse(value);
                        task.setDueDate(newDate);
                        Database.update(task);
                        printTaskUpdateSuccess("due date", dateFormat.format(oldDate), value, task);
                    } catch (Exception e) {
                        throw new InvalidEntityException("Invalid date format.Please use yyyy-mm-dd format");
                    }
                    break;

                case "status":
                    try{
                        Task.Status oldStatus = task.getStatus();
                        Task.Status newStatus = Task.Status.valueOf(value);
                        task.setStatus(newStatus);
                        Database.update(task);
                        printTaskUpdateSuccess("status", oldStatus.toString(), value, task);

                        if(newStatus == Task.Status.Completed) {
                            updateStepStatus(taskId, Step.Status.Completed);
                        }
                    } catch (IllegalArgumentException e){
                        throw new InvalidEntityException("Invalid status value. Use : NotStarted, InProgress, Completed");
                    }
                    break;

                default:
                    throw new InvalidEntityException("Invalid field. Available fields: title, description, duedate, status");
            }
        } catch(Exception e){
            System.out.println("Cannot update task with ID=");
            System.out.println("Error: Cannot find entity with ID=");
        }
    }

    public static void printTaskUpdateSuccess(String field, String oldValue, String newValue, Task task) {
        System.out.println("Successfully updated the task.");
        System.out.println("Field: " + field);
        System.out.println("Old Value: " + oldValue);
        System.out.println("New Value: " + newValue);
        System.out.println("Modification Date: " + task.getLastModificationDate());
    }

    public static void updateStepStatus(int taskId, Step.Status newStatus) {
        ArrayList<Entity> allSteps = Database.getAll(Step.STEP_ENTITY_CODE);

        for(Entity entity : allSteps){
            Step step = (Step) entity;
            if(step.getTaskRef() == taskId && step.getStatus() != newStatus){
                step.setStatus(newStatus);
                try{
                    Database.update(step);
                    System.out.println("Updated step ID=" + step.id + " status to " + newStatus);
                } catch (Exception e) {
                    System.out.println("Failed to update step ID=" + step.id);
                }
            }
        }
    }

    public static void updateStep(Scanner scn){
        try{
            System.out.println("ID: ");
            int stepId = Integer.parseInt(scn.nextLine().trim());

            System.out.println("Field: ");
            String field = scn.nextLine().trim().toLowerCase();

            System.out.println("New Value: ");
            String value = scn.nextLine().trim();

            Step step = (Step) Database.get(stepId);
            Step oldStep = (Step) step.copy();

            switch (field){
                case "title":
                    String oldTitle = oldStep.getTitle();
                    step.setTitle(value);
                    Database.update(step);
                    printStepUpdateSuccess("title", oldTitle, value, step);
                    break;

                case "status":
                    try{
                        Step.Status oldStatus = oldStep.getStatus();
                        Step.Status newStatus = Step.Status.valueOf(value);

                        if(oldStatus == newStatus){
                            System.out.println("Status is already: " + newStatus);
                            return;
                        }
                        step.setStatus(newStatus);
                        Database.update(step);
                        printStepUpdateSuccess("status", oldStatus.toString(), value, step);

                        updateTaskStatusBasedOnSteps(step.getTaskRef());
                    } catch (IllegalArgumentException e){
                        throw new InvalidEntityException("Invalid status value. Use : NotStarted, InProgress, Completed");
                    }
                    break;

                default:
                    throw new InvalidEntityException("Invalid field. Available fields: title, description, duedate, status");
            }

        }
        catch(InvalidEntityException e){
            System.out.println(e.getMessage());
        }
    }

    public static void updateTaskStatusBasedOnSteps(int taskId) {
        try{
            Task task = (Task) Database.get(taskId);
            ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);

            boolean allStepsCompleted = true;
            boolean anyStepCompleted = false;

            for(Entity entity : steps){
                Step step = (Step) entity;
                if(step.getTaskRef() == taskId){
                    if(step.getStatus() != Step.Status.Completed)
                        allStepsCompleted = false;
                    else
                        anyStepCompleted = true;
                }
            }

            Task.Status newStatus = task.getStatus();

            if(allStepsCompleted)
                newStatus = Task.Status.Completed;
            else if(anyStepCompleted && task.getStatus() == Task.Status.NotStarted)
                newStatus = Task.Status.InProgress;

            if(newStatus != task.getStatus()){
                task.setStatus(newStatus);
                Database.update(task);
                System.out.println("Task ID=" + taskId + " status automatically updated to " + newStatus);
            }

        } catch (Exception e) {
            System.out.println("Failed to update task status automatically: " + e.getMessage());
        }
    }

    public static void printStepUpdateSuccess(String field, String oldValue, String newValue, Step step) {
        System.out.println("Successfully updated the step.");
        System.out.println("Field: " + field);
        System.out.println("Old Value: " + oldValue);
        System.out.println("New Value: " + newValue);
        System.out.println("Modification Date: " + step.getLastModificationDate());
    }

    public static void getTaskById(Scanner scn) {
        try{
            System.out.println("ID: ");
            int taskId = Integer.parseInt(scn.nextLine().trim());

            try {
                Task task = (Task) Database.get(taskId);

                System.out.println("Title: " + task.getTitle());
                System.out.println("Due Date: " + dateFormat.format(task.getDueDate()));
                System.out.println("Status: " + task.getStatus());

                ArrayList<Entity> allSteps = Database.getAll(Step.STEP_ENTITY_CODE);
                boolean hasStep = false;

                for(Entity entity : allSteps){
                    Step step = (Step) entity;
                    if(step.getTaskRef() == taskId){
                        if(!hasStep){
                            System.out.println("Steps: ");
                            hasStep = true;
                        }
                        System.out.println("    + " + step.getTitle() + ":");
                        System.out.println("        ID: " + step.id);
                        System.out.println("        Status: " + step.getStatus());
                    }
                }

                if(!hasStep) {
                    System.out.println("Steps: None");
                }
            } catch (EntityNotFoundException e){
                System.out.println("Cannot find task with ID=" + taskId);
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a number.");
        }
    }

    public static void getAllTasks(Scanner scn){
        try{
            ArrayList<Task> allTasks = new ArrayList<>();
            for(Entity entity : Database.getAll(Task.TASK_ENTITY_CODE))
                allTasks.add((Task)entity);

            allTasks.sort(Comparator.comparing(Task::getDueDate));

            for (Task task : allTasks) {
                System.out.println("ID: " + task.id);
                System.out.println("Title: " + task.getTitle());
                System.out.println("Due Date: " + dateFormat.format(task.getDueDate()));
                System.out.println("Status: " + task.getStatus());

                ArrayList<Entity> allSteps = Database.getAll(Step.STEP_ENTITY_CODE);
                boolean hasStep = false;

                for (Entity entity : allSteps) {
                    Step step = (Step) entity;
                    if (step.getTaskRef() == task.id) {
                        if (!hasStep) {
                            System.out.println("Steps: ");
                            hasStep = true;
                        }
                        System.out.println("    + " + step.getTitle() + ":");
                        System.out.println("        ID: " + step.id);
                        System.out.println("        Status: " + step.getStatus());
                    }
                }

                if(!hasStep) {
                    System.out.println("Steps : None");
                }
                System.out.println();
            }
        } catch (EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    public static void getIncompleteTasks(Scanner scn) {
        try{
            ArrayList<Task> incompleteTasks = new ArrayList<>();
            for (Entity entity : Database.getAll(Task.TASK_ENTITY_CODE)){
                Task task = (Task) entity;
                if(task.getStatus() == Task.Status.InProgress)
                    incompleteTasks.add(task);
            }

            incompleteTasks.sort(Comparator.comparing(Task::getDueDate));

            for(Task task : incompleteTasks){
                System.out.println("ID: " + task.id);
                System.out.println("Title: " + task.getTitle());
                System.out.println("Due Date: " + dateFormat.format(task.getDueDate()));
                System.out.println("Status: " + task.getStatus());

                ArrayList<Entity> allSteps = Database.getAll(Step.STEP_ENTITY_CODE);
                boolean hasStep = false;

                for (Entity entity : allSteps) {
                    Step step = (Step) entity;
                    if (step.getTaskRef() == task.id) {
                        if (!hasStep) {
                            System.out.println("Steps: ");
                            hasStep = true;
                        }
                        System.out.println("    + " + step.getTitle() + ":");
                        System.out.println("        ID: " + step.id);
                        System.out.println("        Status: " + step.getStatus());
                    }
                }

                if(!hasStep) {
                    System.out.println("Steps : None");
                }
                System.out.println();
            }

        } catch (EntityNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
}
