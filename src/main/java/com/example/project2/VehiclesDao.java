package com.example.project2;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class VehiclesDao {
    @PersistenceContext
    private EntityManager entityManager;

    public void create(Vehicle vehicle){
        entityManager.persist(vehicle);
    }

    /**
     *
     * @param id
     * We use the parameter of id in order to find it utilizing the entity manager
     * @return
     * We then return our entity manager
     */
    public Vehicle getById(int id){
        return entityManager.find(Vehicle.class, id);
    }

    /**
     *
     * @param vehicle
     * We use the parameter of vehicle in order to add it by a specific vehicle
     * @param id
     * We also use a value of id in order to insert a vehicle by a specific id
     * @return
     * We then return the new vehicle that we created
     */
    public Vehicle insertById(Vehicle vehicle, int id){
        Vehicle originalVehicle = entityManager.find(Vehicle.class, id);
        originalVehicle.setMake(vehicle.getMake());
        originalVehicle.setModel(vehicle.getModel());
        originalVehicle.setModelYear(vehicle.getModelYear());
        originalVehicle.setPrice(vehicle.getPrice());
        originalVehicle.setMpg(vehicle.getMpg());
        originalVehicle.setFwd(vehicle.isFwd());

        return vehicle;
    }

    /**
     *
     * @param id
     * We use a value for an id in order to delete a specific vehicle by a specified id
     * @return
     * We then return the vehicle that we deleted
     */
    public Vehicle deleteById(int id){
        entityManager.getTransaction().begin();
        Vehicle vehicle = entityManager.find(Vehicle.class, id);
        entityManager.remove(vehicle);
        entityManager.getTransaction().commit();
        return vehicle;
    }

}
