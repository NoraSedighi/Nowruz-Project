package todo.entity;

import db.Entity;
import db.Trackable;

import java.util.Date;

public class Task extends Entity implements Trackable {
    public enum Status{
        NotStarted,
        InProgress,
        Completed
    }

    private String title;
    private String description;
    private Date dueDate;
    private Status status;
    private Date creationDate;
    private Date lastModificationDate;
    public static final int TASK_ENTITY_CODE  = 16;

    public Task(String title, String description, Date dueDate){
        setTitle(title);
        setDescription(description);
        setDueDate(dueDate);
        this.status = Status.NotStarted;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    @Override
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status){
        if(this.status == Status.Completed && status != Status.Completed)
            throw new IllegalArgumentException("Cannot change status from Completed");

        this.status = status;
    }

    @Override
    public Task copy(){
        Task copy = new Task(this.title, this.description, this.dueDate);
        copy.status = this.status;

        copy.id = this.id;

        copy.creationDate = new Date(this.creationDate.getTime());
        copy.lastModificationDate = new Date(this.lastModificationDate.getTime());

        return copy;
    }

    @Override
    public int getEntityCode() {
        return TASK_ENTITY_CODE;
    }

}
