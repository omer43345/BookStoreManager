package bookstoremanager.dal;

import bookstoremanager.Exceptions.InvalidEntityException;
import bookstoremanager.entity.Book;
import bookstoremanager.entity.BookCustomer;
import bookstoremanager.entity.Entity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class BookStoreManagerFileDao implements BookStoreManagerDao {
    private static String BOOKS_FILE_NAME = "BookStoreDataBooks";
    private static String BOOKCUSTOMERS_FILE_NAME = "BookStoreDataBookCustomers";

    public static final int bookClassId = 0;
    public static final int bookCustomerClassId = 1;

    public static Map<String, Integer> idMap;

    static {
        idMap = new HashMap<String, Integer>();
        idMap.put(Book.class.toString(), bookClassId);
        idMap.put(BookCustomer.class.toString(), bookCustomerClassId);
    }

    public static int classId = 0;

    private String GetFileName(int classId) {
        // get the file name according to the given class id
        if (classId == 0)
            return BOOKS_FILE_NAME;
        if (classId == 1)
            return BOOKCUSTOMERS_FILE_NAME;
        return null;
    }

    private ArrayList<Entity> read(int classId) throws Exception {
        // get the list of the entities from the file with the given class id

        ArrayList<Entity> entityList = new ArrayList<Entity>();
        String file_name = GetFileName(classId);
        if (file_name != null) {
            File file = new File(file_name);
            file.createNewFile(); // if file already exists will do nothing
            try {
                FileInputStream Input = new FileInputStream(file_name);
                ObjectInputStream objectInput = new ObjectInputStream(Input);
                entityList = (ArrayList<Entity>) objectInput.readObject();
                objectInput.close();
                Input.close();

            } catch (IOException ioe) {
                return null;
            } catch (ClassNotFoundException ex) {
                System.out.println("Invalid class");
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return entityList;
    }

    public void write(ArrayList<Entity> entityList) throws Exception {
        // write the list of the entities to the file it belongs
        if (entityList.isEmpty()) {
            String file_name = GetFileName(classId);
            FileWriter file = new FileWriter(file_name);
            file.write("");
            return;
        }
        // get the file name according to the type of the entities in the list
        String file_name = GetFileName(idMap.get(entityList.get(0).getClass().toString()));
        try {
            FileOutputStream output = new FileOutputStream(file_name);
            ObjectOutputStream objectOutput = new ObjectOutputStream(output);
            objectOutput.writeObject(entityList);
            objectOutput.close();
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public List<Entity> getAll(int classId) throws Exception {
        return read(classId);
    }

    @Override
    public void save(Entity entity) throws Exception {
        // save the given entity in the file it belong to
        ArrayList<Entity> entityList = (ArrayList<Entity>) getAll(idMap.get(entity.getClass().toString()));
        // adding the new entity to the list of the entities of its class
        if (entityList == null) {
            entityList = new ArrayList<>();
        }
        entityList.add(entity);
        Collections.sort(entityList);
        // writing to the correct file the updated entity list
        write(entityList);

    }

    @Override
    public void update(Entity entity) throws Exception {
        // update an entity in the file it belongs to

        ArrayList<Entity> entityList = (ArrayList<Entity>) getAll(idMap.get(entity.getClass().toString()));
        if (entityList.contains((Object) entity)) {
            // iterate all the entities and update the correct one
            int index = 0;
            for (Entity e : entityList) {
                if (e.getId() == entity.getId()) {
                    entityList.remove(index);
                    entityList.add(index, entity);
                    break;
                }
                index++;
            }
            // writing to the correct file the updated entity list
            write(entityList);
        } else {
            throw new InvalidEntityException("Entity does not exist in the file");
        }
    }

    @Override
    public void delete(int entityId, Class c) throws Exception {
        // deleting entity with the given id and class from the file it belongs to

        classId = idMap.get(c.toString());
        ArrayList<Entity> entityList = (ArrayList<Entity>) getAll(classId);

        // iterate all the entities and delete the correct one
        int index = 0;
        for (Entity entity : entityList) {
            if (entity.getId() == entityId) {
                entityList.remove(index);
                break;
            }
            index++;
        }
        // writing to the correct file the updated entity list
        write(entityList);

    }

    @Override
    public Entity get(int entityId, Class c) throws Exception {
        // get a entity with the given id and class from the file it belongs to

        ArrayList<Entity> entityList = (ArrayList<Entity>) getAll(idMap.get(c.toString()));
        if (entityList == null)
            throw new InvalidEntityException("there is no entities");

        // iterate all the entities and return the correct one
        int index = 0;
        for (Entity entity : entityList) {
            if (entity.getId() == entityId) {
                return (Entity) entity;
            }
            index++;
        }
        // if nothing matched
        throw new InvalidEntityException("the given id not exist in the file");
    }

    @Override
    public void Initiate() {
        // initialize the static id counter for each class
        try {
            Book.initializeCounter(getAll(BookStoreManagerFileDao.bookClassId).size());
        } catch (Exception e) {
            Book.initializeCounter(0);
        }
        try {
            BookCustomer.initializeCounter(getAll(BookStoreManagerFileDao.bookCustomerClassId).size());
        } catch (Exception e) {
            BookCustomer.initializeCounter(0);
        }
    }
}
