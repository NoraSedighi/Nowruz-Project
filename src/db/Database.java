package db;

import db.db.exceptions.EntityNotFoundException;

import java.util.ArrayList;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static int nextId = 1;

    public static void add(Entity e){
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

    public static void update(Entity e) throws EntityNotFoundException{
        for (int i = 0; i < entities.size(); i++){
            if (entities.get(i).id == e.id) {
                entities.set(i, e.copy());
                return;
            }
        }
        throw new EntityNotFoundException(e.id);
    }
}
