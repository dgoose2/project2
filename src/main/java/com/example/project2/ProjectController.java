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


    @RequestMapping(value = "/getVehicle/{id}", method = RequestMethod.GET)
    public Vehicle getVehicle(@PathVariable("id") int id)throws IOException {
        return vehiclesDao.getById(id);
    }

    @RequestMapping(value = "/updateVehicle/{id}", method = RequestMethod.PUT)
    public Vehicle updateVehicle(@PathVariable("id") int id, @RequestBody Vehicle newVehicle)throws IOException{
        return vehiclesDao.insertById(newVehicle, id);
    }

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

//    @RequestMapping(value = "/getLatestVehicles", method = RequestMethod.GET)
//    public List<Vehicle> getLatestVehicles() throws IOException{
//        List<Vehicle> latestVehicles = new ArrayList<>();
//        File file = new File("./latestVehicleReport.txt");
//        FileWriter outputFile = new FileWriter(file, false);
//
//        for(int i = vehicleStringList.size()-1, latestVehicleCounter = 0; i > 0 && latestVehicleCounter < 10; i--, latestVehicleCounter++){
//            if (!vehicleStringList.get(i).equals("")) {
//                latestVehicles.add(constructVehicle(vehicleStringList.get(i)));
//                outputFile.write(vehicleStringList.get(i) + "\n");
//                System.out.println(vehicleStringList.get(i));
//            }
//        }
//        outputFile.close();
//        return latestVehicles;
//    }
}