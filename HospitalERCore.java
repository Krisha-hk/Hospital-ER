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
import java.io.*;

/**
 * Simple Simulation of a Hospital ER
 * 
 * The Emergency room has a waiting room and a treatment room that has a fixed
 *  set of beds for examining and treating patients.
 * 
 * When a patient arrives at the emergency room, they are immediately assessed by the
 *  triage team who determines the priority of the patient.
 *
 * They then wait in the waiting room until a bed becomes free, at which point
 * they go from the waiting room to the treatment room.
 *
 * When a patient has finished their treatment, they leave the treatment room and are discharged,
 *  at which point information about the patient is added to the statistics. 
 *
 *  READ THE ASSIGNMENT PAGE!
 */

public class HospitalERCore{

    // Fields for recording the patients waiting in the waiting room and being treated in the treatment room
    private Queue<Patient> waitingRoom = new ArrayDeque<Patient>();
    private static final int MAX_PATIENTS = 5;   // max number of patients currently being treated
    private Set<Patient> treatmentRoom = new HashSet<Patient>();
    private List <Integer> pri = new ArrayList<>();

    // fields for the statistics
    /*# YOUR CODE HERE */
    private int waitTime = 0;
    private int totalPatient = 0;
    private int priority1Time = 0;
    private int pri1waitTime = 0;
    private int treatedPatient = 0;

    // Fields for the simulation
    private boolean running = false;
    private int time = 0; // The simulated time - the current "tick"
    private int delay = 300;  // milliseconds of real time for each tick


    /**
     * Reset the simulation:
     *  stop any running simulation,
     *  reset the waiting and treatment rooms
     *  reset the statistics.
     */
    public void reset(boolean usePriorityQueue){
        running=false;
        UI.sleep(2*delay);  // to make sure that any running simulation has stopped

        time = 0;           // set the "tick" to zero.
        // reset the waiting room, the treatment room, and the statistics.
        /*# YOUR CODE HERE */
        waitingRoom.clear();
        treatmentRoom.clear();
        
        waitTime = 0;
        totalPatient = 0;
        priority1Time = 0;
        pri1waitTime = 0;
        treatedPatient = 0;
        
        pri.clear();
        UI.clearGraphics();
        UI.clearText();
    }

    /**
     * Main loop of the simulation
     */
    public void run(){
        if (running) { return; } // don't start simulation if already running one!
        running = true;
        while (running){         // each time step, check whether the simulation should pause.

            /*# YOUR CODE HERE */
            // Process patients in the treatment and waiting rooms
            List <Patient> treatedPat = new ArrayList <>();
            
            // Check finished treatments and collect treated patients
            for(Patient T : treatmentRoom){
                if(T.currentTreatmentFinished())
                {
                    treatedPat.add(T);
                    UI.println("Patient done" +T);
        
                }
            }
            
            // Update statistics and remove treated patients
            for(Patient T: treatedPat){
                if (T.getPriority() == 1)
                {
                    pri1waitTime += T.getTotalWaitingTime();
                    priority1Time ++;
                }
                treatedPatient ++;
                totalPatient += T.getTotalWaitingTime();
                treatmentRoom.remove(T);
            }
            
            // Advance treatments and waiting times for remaining patients
            for(Patient T : treatmentRoom){T.advanceCurrentTreatmentByTick();}
            for(Patient waitingPatient : waitingRoom){waitingPatient.waitForATick();}
            
             // Move waiting patients to treatment room if there's space
            if(treatmentRoom.size() < MAX_PATIENTS && waitingRoom.size() > 0)
             {
                 treatmentRoom.add(waitingRoom.poll());
             }
            
            // Gets any new patient that has arrived and adds them to the waiting room
            Patient newPatient = PatientGenerator.getNextPatient(time);
            if (newPatient != null){
                UI.println(time+ ": Arrived: "+newPatient);
                waitingRoom.offer(newPatient);
            }
            
            // Redraw the simulation and sleep
            redraw();
            UI.sleep(delay);
        }
        // paused, so report current statistics
        reportStatistics();
    }

    // Additional methods used by run() (You can define more of your own)
   
    /**
     * Report summary statistics about all the patients that have been discharged.
     */
    public void reportStatistics(){
        /*# YOUR CODE HERE */
        // Calculate and print average treatment time for treated patients
        if(totalPatient == 0)
        {
            UI.println(treatedPatient + " Patients had an average treatment time of " + 0);
        
        } else
        {
            int avgPatientTime = (totalPatient/treatedPatient);
            UI.println(treatedPatient + " Patients had an average treatment time of " + avgPatientTime);
        }
        
        // Calculate and print average wait time for priority 1 patients
         if(pri1waitTime == 0)
        {
            UI.println(Collections.frequency(pri, 1) + " Patients had an average wait time of " + 0);
        
        } else
        {
            int avgPri1Patient = (pri1waitTime/ priority1Time);
            UI.println(treatedPatient + " Patients had an average treatment time of " + avgPri1Patient);
        }


    }




    // METHODS FOR THE GUI AND VISUALISATION

    /**
     * Set up the GUI: buttons to control simulation and sliders for setting parameters
     */
    public void setupGUI(){
        UI.addButton("Reset (Queue)", () -> {this.reset(false); });
        UI.addButton("Reset (Pri Queue)", () -> {this.reset(true);});
        UI.addButton("Start", ()->{if (!running){ run(); }});   //don't start if already running!!
        UI.addButton("Pause & Report", ()->{running=false;});
        UI.addSlider("Speed", 1, 400, (401-delay), (double val)-> {delay = (int)(401-val);});
        UI.addSlider("Av arrival interval", 1, 50, PatientGenerator.getArrivalInterval(),
                     PatientGenerator::setArrivalInterval);
        UI.addSlider("Prob of Pri 1", 1, 100, PatientGenerator.getProbPri1(),
                     PatientGenerator::setProbPri1);
        UI.addSlider("Prob of Pri 2", 1, 100, PatientGenerator.getProbPri2(),
                     PatientGenerator::setProbPri2);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1000,600);
        UI.setDivider(0.5);
    }

    /**
     * Redraws all the patients and the state of the simulation
     */
    public void redraw(){
        UI.clearGraphics();
        UI.setFontSize(14);
        UI.drawString("Treating Patients", 5, 15);
        UI.drawString("Waiting Queues", 200, 15);
        UI.drawLine(0,32,400, 32);

        // Draw the treatment room and the waiting room:
        double y = 80;
        UI.setFontSize(14);
        UI.drawString("ER", 0, y-35);
        double x = 10;
        UI.drawRect(x-5, y-30, MAX_PATIENTS*10, 30);  // box to show max number of patients
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


    /**
     * main:  Construct a new HospitalERCore object, setting up the GUI, and resetting
     */
    public static void main(String[] arguments){
        HospitalERCore er = new HospitalERCore();
        er.setupGUI();
        er.reset(false);   // initialise with an ordinary queue.
    }        


}
