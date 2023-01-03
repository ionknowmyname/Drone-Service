
**DRONE SERVICE - JAVA 11 - APACHE MAVEN - H2 DATABASE**


**Summary of Service:**
    Drone Service for transporting Medical supplies. Service can:
    -   create drones 
    -   create medication with picture file
    -   load medications to drone in bulk
    -   offload medications from drone in bulk 
    -   check all medications on a given drone
    -   get battery capacity for any given drone
    -   get all drones by given state
    -   store battery percentage log using cronjob


**Setup:**
    No setup required, clone project and open in IntelliJ IDE and run. Runs on port 8080.
    Checkout application.properties file for more info.
    
    OR 

    Run "mvn spring-boot:run" in terminal

    OR 

    Build & Run with Dockerfile


**Endpoints & Sample Payload:**

    1.  POST --> localhost:8080/api/v1/drone -- Creates a drone with sample payload below
        {
            "model": "LIGHTWEIGHT",
            "weight": 30,
            "batteryCapacity": 85,
            "state": "IDLE"
        }

    2.  GET --> localhost:8080/api/v1/drone/id/{serial} -- Get drone by serial

    3.  GET --> localhost:8080/api/v1/drone/id/{serial}/batteryPercentage  -- Get Battery percentage of a drone

    4.  GET --> localhost:8080/api/v1/drone/id/{serial}/medications  -- Get all medications loaded on a drone

    5.  GET --> localhost:8080/api/v1/drone/all?state=IDLE -- Get all drones by state

    6.  PUT --> localhost:8080/api/v1/drone/id/{serial}/medications/add -- Load medications to drone using list
            of medication codes, like sample payload below:
        {
            "medicationCodes": [
                "RFcBRUs0O7"
            ]
        }

    7.  PUT --> localhost:8080/api/v1/drone/id/{serial}/medications/remove -- Unload medications to drone using list
            of medication codes, like sample payload below:
        {
            "medicationCodes": [
                "RFcBRUs0O7"
            ]
        }

    8.  POST --> localhost:8080/api/v1/medication/?name=Testing14&weight=20 -- Create new medication with name & weight 
            params, then add file to upload in request body as form-data

    9.  GET --> localhost:8080/api/v1/droneBatteryLog/id/{serial} -- Get all battery logs by drone serial


**More Information:**

    - For file to save successfuly, change the filePath in MedicationServiceImpl to a location on your
        local PC

    - In production, instead of saving the file to hardcoded file path on local machine, save the file 
        to a 3rd party serivce, and then save the response to the database.

    - The cron job would keep saving the same value for battery percentage because there's no mechanism
        in place to reduce the battery percentage
