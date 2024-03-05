# Hospital-ER
The HospitalER program is a tick-based simulation of patients arriving at the emergency room of a hospital. Patients arrive, wait for treatment, get treated, then are discharged.
Advance the time by one "tick"
Find all patients in the treatment room who have finished their current treatment and discharge them (remove them from the treatment room). For debugging, it is helpful to print a message for each patient when they are discharged: UI.println(time+ ": Discharge: " + p);
Process one time tick for each patient currently being treated, or waiting in the waiting room.
Move patients from the waiting room to the treatment room if there is space.
Get any new patient that has arrived and adds them to the waiting room [complete].
Display the state of the waiting room and treatment room [complete].
extend the run() method to update the statistics when a patient is discharged, and complete the reportStatistics() method to report:
The total number of patients treated
The average waiting time of the patients
Make the waiting room act as a priority queue taking into account the priority of the patients.
Extend the reset method to use a priority queue if the argument to the method is true, and a regular queue if the argument is false.
Complete the compareTo method in the Patient class so that a Patient with a higher priority is ordered before a Patient with lower priority. Patients with equal priority should be ordered by their time of arrival.
Add fields, and modify the run() and reportStatistics methods to also report
The total number of priority 1 patients treated,
The average waiting time of the priority 1 patients

Part 2 

Extending the program to handle the more realistic simulation.
When a patient finishes one treatment, they then need to move on to their next treatment, until they finally finish all the required treatments and can be discharged. When a Patient object is created, the sequence of treatments (and how long they will take) is worked out by PatientGenerator.getNextPatient.
Each department will have limited facilities so each department will have its own waiting room and treatment room. 

