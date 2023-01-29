package bookstoremanager.entity;

import java.io.Serializable;

public abstract class Entity implements Comparable<Entity>, Serializable {

    public abstract int getId();

    public abstract double getValue();

    @Override
    public int compareTo(Entity entity) {
        double difference = this.getValue() - entity.getValue();
        if (difference > 0) {
            return 1;
        } else if (difference == 0) {
            return 0;
        }
        return -1;
    }


}
