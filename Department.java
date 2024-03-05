// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T2, Assignment 3
 * Name: krisha
 * Username: patelkris3
 * ID: 300651925
 */

import ecs100.*;
import java.util.*;

/**
 * A treatment Department (ER, X-Ray, MRI, ER, UltraSound, Surgery)
 * Each department will need
 * - A name,
 * - A maximum number of patients that can be treated at the same time
 * - A Set of Patients that are currently being treated
 * - A Queue of Patients waiting to be treated.
 *    (ordinary queue, or priority queue, depending on argument to constructor)
 */

public class Department{

    private String name;
    private int maxPatients;   // maximum number of patients receiving treatment at one time. 
    private double y;
    private Set<Patient> treatmentRoom = new HashSet<Patient>();    // the patients receiving treatment
    private Queue<Patient> waitingRoom;    // the patients waiting for treatment
    private List <Patient> treatedPatients = new ArrayList<Patient>();
    /**
     * Construct a new Department object
     * Initialise the waiting queue and the current Set.
     */
    public Department(String name, int maxP, boolean usePriQueue, double y){
        /*# YOUR CODE HERE */
        this.name = name;
        this.maxPatients = maxP;
        this.y = y;
        if (usePriQueue) {
            this.waitingRoom = new PriorityQueue<>();
        }
        else
        {
            this.waitingRoom = new ArrayDeque<Patient>();
        }

    }

    // Methods 

    /*# YOUR CODE HERE */
    public void treat(){
        treatedPatients.clear();
        for (Patient T : treatmentRoom) {
            if (T.currentTreatmentFinished()){
                treatedPatients.add(T);
            }
        }
        for(Patient T : treatedPatients) {
            treatmentRoom.remove(T);
            
        }
        for (Patient T : treatmentRoom) {
            T.advanceCurrentTreatmentByTick();
            
        }
        for (Patient T : waitingRoom) {
            T.waitForATick();
        }
        if (treatmentRoom.size() < maxPatients && waitingRoom.size() > 0) {
            treatmentRoom.add(waitingRoom.poll());
        }
    }
    
    public void redraw(){
        
        // Draw the treatment room and the waiting room:
        //double y = 80;
        UI.setFontSize(14);
        UI.drawString(name, 0, y-35);
        double x = 10;
        UI.drawRect(x-5, y-30, maxPatients*10, 30);  // box to show max number of patients
        
        for(Patient p : treatmentRoom){
            p.redraw(x, y);
            x += 10;
        }
        x = 200;
        for(Patient p : waitingRoom){
            p.redraw(x, y);
            x += 10;
        }
        UI.drawLine(0,y+2,400, y+2);
        
    }
    
    public void addPatient(Patient p){
        waitingRoom.offer(p);
        
    }
    
    public String getName() {return name;}
    
    public List treated() {
        return treatedPatients;
    
    }
    
    public Queue<Patient> getWaitingRoom(){
        return waitingRoom;
    }

    /**
     * Draw the department: the patients being treated and the patients waiting
     * You may need to change the names if your fields had different names
     */
    public void redraw(double y){
        UI.setFontSize(14);
        UI.drawString(name, 0, y-35);
        double x = 10;
        UI.drawRect(x-5, y-30, maxPatients*10, 30);  // box to show max number of patients
        for(Patient p : treatmentRoom){
            p.redraw(x, y);
            x += 10;
        }
        x = 200;
        for(Patient p : waitingRoom){
            p.redraw(x, y);
            x += 10;
        }
    }

}
