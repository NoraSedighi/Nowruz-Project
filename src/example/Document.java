package example;

import db.Entity;
import db.Trackable;

import java.util.Date;

public class Document extends Entity implements Trackable {
    private String content;
    private Date creationDate;
    private Date lastModificationDate;
    public static final int DOCUMENT_ENTITY_CODE = 15;

    public Document(String content){
        if(content == null)
            throw new IllegalArgumentException("Content cannot be null");

        this.content = content;
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

    public void setContent(String content) {
        if(content == null)
            throw new IllegalArgumentException("Content cannot be null");

        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public Entity copy() {
        Document copy = new Document(content);
        copy.id = id;
        copy.creationDate = creationDate;
        copy.lastModificationDate= lastModificationDate;
        return copy;
    }

    @Override
    public int getEntityCode() {
        return DOCUMENT_ENTITY_CODE;
    }
}
