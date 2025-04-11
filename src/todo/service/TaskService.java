package todo.service;

import db.Database;
import db.db.exceptions.*;
import todo.entity.Task;

import java.util.Date;

public class TaskService {
    public static Task createTask(String title, String description, Date dueDate)throws InvalidEntityException{
        Task task =  new Task(title, description, dueDate);
        task.setCreationDate(new Date());
        return task;
    }

    public static Task saveTask(Task task) throws InvalidEntityException{
        Database.add(task);
        return task;
    }

    private static void updateTaskStatus(int taskId, Task.Status status){
        try{
            Task task = (Task) Database.get(taskId);
            task.setStatus(status);
            Database.update(task);
        } catch (EntityNotFoundException e) {
            System.out.println("Task not found with ID: " + taskId);
        } catch (InvalidEntityException e) {
            System.out.println("Invalid task entity with ID: " + taskId);
        }

    }
    public static void setAsCompleted(int taskId){
        updateTaskStatus(taskId, Task.Status.Completed);
    }

    public static void setAsInProgress(int taskId){
        updateTaskStatus(taskId, Task.Status.InProgress);
    }

    public static void setAsNotStarted(int taskId){
        updateTaskStatus(taskId, Task.Status.NotStarted);
    }

    public static Task getTask(int taskId) throws EntityNotFoundException{
        return (Task) Database.get(taskId);
    }

    public static void updateTaskTitle(int taskId, String newTitle) throws InvalidEntityException, EntityNotFoundException{
        Task task = getTask(taskId);
        task.setTitle(newTitle);
        Database.update(task);
    }

    public static void updateTaskDescription(int taskId, String newDescription) throws InvalidEntityException, EntityNotFoundException{
        Task task = getTask(taskId);
        task.setDescription(newDescription);
        Database.update(task);
    }

    public static void updateDueDate(int taskId, Date newDueDate) throws InvalidEntityException, EntityNotFoundException{
        Task task = getTask(taskId);
        task.setDueDate(newDueDate);
        Database.update(task);
    }

    public static void deleteTask(int taskId) throws InvalidEntityException{
        Database.delete(taskId);
    }
}
