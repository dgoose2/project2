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

    public Vehicle getById(int id){
        return entityManager.find(Vehicle.class, id);
    }

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

    public Vehicle deleteById(int id){
        entityManager.getTransaction().begin();
        Vehicle vehicle = entityManager.find(Vehicle.class, id);
        entityManager.remove(vehicle);
        entityManager.getTransaction().commit();
        return vehicle;
    }

}
