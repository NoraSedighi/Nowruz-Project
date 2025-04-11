package todo.entity;

import db.Entity;
import db.Trackable;

import java.util.Date;

public class Step extends Entity implements Trackable {
    public enum Status{
        NotStarted,
        Completed
    }

    private String title;
    private Status status;
    private int taskRef;
    private Date creationDate;
    private Date lastModificationDate;
    public static final int STEP_ENTITY_CODE = 17;


    public Step(int id, String title, Status status, int taskRef){
        this.id = id;
        setTitle(title);
        setStatus(status);
        setTaskRef(taskRef);
        this.creationDate = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if(this.status == Status.Completed && status != Status.Completed)
            throw new IllegalArgumentException("Cannot change status from Completed");

        this.status = status;
    }

    public int getTaskRef() {
        return taskRef;
    }

    public void setTaskRef(int taskRef) {
        if(taskRef < 0)
            throw new IllegalArgumentException("Task reference must be positive");
        this.taskRef = taskRef;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        if (creationDate == null)
            throw new IllegalArgumentException("Creation date cannot be null");
        this.creationDate = creationDate;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    @Override
    public Entity copy() {
        Step copy = new Step(this.id , this.title , this.status , this.getTaskRef());
        copy.setCreationDate(this.getCreationDate());
        copy.setLastModificationDate(this.getLastModificationDate());

        return copy;
    }

    @Override
    public int getEntityCode() {
        return STEP_ENTITY_CODE;
    }

}
