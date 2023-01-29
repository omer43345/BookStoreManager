package bookstoremanager.dal;

import bookstoremanager.entity.Entity;

import java.util.List;

public interface BookStoreManagerDao {

    //returns list of the entities in the file
    public List<Entity> getAll(int id) throws Exception;

    //saves an entity to the file
    public void save(Entity t) throws Exception;

    //updates an entity in the file
    public void update(Entity t) throws Exception;

    //Deletes an entity with the given id and class from the file
    public void delete(int id, Class c) throws Exception;

    //returns an entity with the given id and class that in the file
    public Entity get(int id, Class c) throws Exception;

    //initializes the dao
    public void Initiate() throws Exception;

}
