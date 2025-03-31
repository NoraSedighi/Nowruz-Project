package db;

import db.db.exceptions.EntityNotFoundException;
import db.db.exceptions.InvalidEntityException;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static int nextId = 1;
    public static HashMap<Integer, Validator> validators = new HashMap<>();

    public static void add(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        if(validator != null)
            validator.validate(e);

        e.id = nextId++;
        entities.add(e.copy());
    }

    public static Entity get(int id) throws EntityNotFoundException {
        for (Entity entity : entities){
            if (entity.id == id)
                return entity.copy();
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException{
        Entity entityToRemove = null;
        for (Entity entity : entities){
            if (entity.id == id) {
                entityToRemove = entity;
                break;
            }
        }
        if (entityToRemove != null)
            entities.remove(entityToRemove);
        else
            throw new EntityNotFoundException(id);
    }

    public static void update(Entity e) throws EntityNotFoundException, InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        if(validator != null)
            validator.validate(e);

        for (int i = 0; i < entities.size(); i++){
            if (entities.get(i).id == e.id) {
                entities.set(i, e.copy());
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }

    public static void registerValidator(int entityCode, Validator validator){
        if(validators.containsKey(entityCode)){
            throw new IllegalArgumentException("Validator already exists for entity code: " + entityCode);
        }
        validators.put(entityCode, validator);
    }
}
