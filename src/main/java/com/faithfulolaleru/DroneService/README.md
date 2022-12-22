
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


**Endpoints & Sample Payload:**

    1.  POST 



**More Information:**

    - For file to save successfuly, change the filePath in MedicationServiceImpl to a location on your
        local PC

    - In production, instead of saving the file to hardcoded file path on local machine, save the file 
        to a 3rd party serivce, and then save the response to the database.

    - The cron job would keep saving the same value for battery percentage because there's no mechanism
        in place to reduce the battery percentage
