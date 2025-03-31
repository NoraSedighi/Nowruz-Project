package db;

import db.db.exceptions.InvalidEntityException;

public interface Validator {
    void validate(Entity entity) throws InvalidEntityException;
}
