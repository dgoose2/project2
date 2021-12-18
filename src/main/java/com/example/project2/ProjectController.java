package com.example.project2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
public class ProjectController {
    // local txt file
    String fileName;

    List<String> vehicleStringList;

    @Autowired
    private VehiclesDao vehiclesDao;


    public List<String> getVehicleStringList() {
        return vehicleStringList;
    }

    @PostConstruct
    public void initializeData() throws FileNotFoundException {
        fileName = "./vehicles.txt";
        vehicleStringList = new ArrayList<>();
        Scanner scanner = new Scanner(new File(fileName));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            vehicleStringList.add(line);
        }
    }



    /**
     * POST method that creates a new vehicle with the provided details in the body of the API request.
     * Sample API request:
     *
     *{
     * 	"make": "Ford",
     * 	"model": "Focus",
     * 	"modelYear": 1850,
     * 	"fwd": false,
     * 	"price": 100,
     * 	"mpg": 1
     *}
     *
     * @param newVehicle
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/addVehicle", method = RequestMethod.POST)
    public Vehicle addVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        vehiclesDao.create(newVehicle);
        return newVehicle;
    }

    /**
     *
     * @param id
     * Utilizing the parameter for id we are getting the vehicle by the specified id
     * We also utilize the vehiclesDao method that we created called getByID
     * @return
     * We are then returning the vehicle by the requested id.
     * @throws IOException
     */
    @RequestMapping(value = "/getVehicle/{id}", method = RequestMethod.GET)
    public Vehicle getVehicle(@PathVariable("id") int id)throws IOException {
        return vehiclesDao.getById(id);
    }

    /**
     *
     * @param id
     * We are utilising our helper method in vehiclesDao called insertById that we created in order to add into the database
     * We are given a parementer of id and this parameter is used in order to update a vehicle by a specific id
     * @param newVehicle
     * We use the newVehicle parameter in order to update the given behicle at a specified id
     * @return
     * We then return the new vehicle into the database that we created
     * @throws IOException
     */
    @RequestMapping(value = "/updateVehicle/{id}", method = RequestMethod.PUT)
    public Vehicle updateVehicle(@PathVariable("id") int id, @RequestBody Vehicle newVehicle)throws IOException{
        return vehiclesDao.insertById(newVehicle, id);
    }

    /**
     *
     * @param id
     * We use this specified id in order to deleted a vehicle at a specified id
     * We created a helper method called deleteByID in order to help with this
     * @return
     * We then return our responseEntity
     * @throws IOException
     */
    @RequestMapping(value = "/deleteVehicle/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteVehicle(@PathVariable("id") int id) throws IOException{
        ResponseEntity responseEntity;
        Scanner scanner = new Scanner(new File(fileName));

        if (vehiclesDao.getById(id) == null) {
            responseEntity = new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        } else {
            vehiclesDao.deleteById(id);
            responseEntity = new ResponseEntity(HttpStatus.OK);
        }
        return responseEntity;
    }

    /**
     *
     * @return
     * In this method we create a list and then loop through the database in order to get the ten most recent vehicles
     * We used a method in order to get it by the id if it was a valid vehicle
     * @throws IOException
     */
    @RequestMapping(value = "/getLatestVehicles", method = RequestMethod.GET)
    public List<Vehicle> getLatestVehicles() throws IOException{
        List<Vehicle> vehicleList = new ArrayList<>();
        for(int i = vehicleStringList.size()-1, latestVehicleCounter = 0; i > 0 && latestVehicleCounter < 10; i--, latestVehicleCounter++){
            if (!vehicleStringList.get(i).equals("")) {
                Vehicle validVehicle = vehiclesDao.getById(i);
                vehicleList.add(validVehicle);
            }
        }
        return vehicleList;
    }
}