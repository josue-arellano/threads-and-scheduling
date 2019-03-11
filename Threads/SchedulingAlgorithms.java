/*************************************************************** 
*   file: SchedulingAlgorithms.java 
*   author: Josue Arellano 
*   class: CS 4310 - Operating Systems
* 
*   assignment: program 1 
*   date last modified: 3/9/2019
* 
*   purpose: This program sorts a list in two threads and then 
*            merges the list into a single list in another
*            thread. This program also implements 
* 
****************************************************************/
import java.io.File;
import java.util.Scanner;
import java.lang.Exception;
import java.util.List;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;

public class SchedulingAlgorithms {
    private Scanner scan;
    List<Process> processes;
    private static String OPTIONS_1 = " 1) First Come First Serve\n 2) Shortest Job First\n";
    private static String OPTIONS_2 = " 3) Preemtive Priority Scheduling\n 4) Round Robin\n";
    private static String MENU= "-------Scheduling Algorithms-------\n" + OPTIONS_1 + OPTIONS_2;

    public SchedulingAlgorithms() {
        processes = new ArrayList<>();
        prepareFile();
    }

    // This method asks the user for the file and parses the file to get all the processes.
    private void prepareFile() {
        Scanner userInput = new Scanner(System.in);
        boolean fileNotFound = false;
        do {
            System.out.print("Enter the name of the file that includes the processes: ");
            String filename = userInput.nextLine();
            try {
                scan = new Scanner(new File(filename));
                fileNotFound = false;
                try {
                    while(scan.hasNextLine()) {
                        Scanner lineScan = new Scanner(scan.nextLine());
                        int pid = lineScan.nextInt();
                        double arrivalTime = lineScan.nextDouble();
                        int burstTime = lineScan.nextInt();
                        int priority = lineScan.nextInt();
                        processes.add(new Process(pid, arrivalTime, burstTime, priority));
                        lineScan.close();
                    }
                } catch(InputMismatchException e) {
                    //this catch statement handles if there is an incorrect token in the file.
                    System.out.println("The file provided is not in the correct format.");
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
                fileNotFound = true;
            }
        } while(fileNotFound);
        System.out.println();
    }

    // This method is the user interface that requests for user input.
    // This method also starts all the appropriate scheduling algorithms.
    public void start() {
        char ans = 'y';
        Scanner userInput = new Scanner(System.in);
        do {
            System.out.println(MENU);
            System.out.print("Please input a number: ");
            try {
                int input = Integer.parseInt(userInput.nextLine());
                switch(input) {
                    case 1:
                        firstComeFirstServe();
                        break;
                    case 2:
                        shortestJobFirst();
                        break;
                    case 3:
                        preemtivePriorityScheduling();
                        break;
                    case 4:
                        roundRobin();
                        break;
                    default:
                        throw new NumberFormatException();
                }
            } catch(NumberFormatException e) {
                System.out.println("Please input a number between 1 and 4.");
            }
            System.out.print("Would you like to try again? (y/n): ");
            ans = userInput.nextLine().charAt(0);
        } while (ans == 'y' || ans == 'Y');
        System.out.println("Thank you for using the CPU Scheduling Simulator.");
        userInput.close();
    }

    // This method implements the first come first serve Scheduling Algorithm.
    // It does so by sorting the processes by arrival time and looping through
    // all the processes.
    public void firstComeFirstServe() {
        reset();
        int time = 0;
        int waitTimeSum = 0;
        int turnArndTimeSum = 0;
        int procssTimeSum = 0;
        for(int j = 0; j < processes.size(); j++) {
            Process pn = processes.get(j);
            waitTimeSum += time;
            turnArndTimeSum += time + pn.getBurstTime();
            procssTimeSum += pn.getBurstTime();
            for(int i = 0; i < pn.getBurstTime(); i++) {
                // Call method to process.
                pn.process(time);
                time++;
            }
        }
        // Calling method that prints all the times.
        printTimes(waitTimeSum, waitTimeSum, turnArndTimeSum, procssTimeSum);
    }

    // This method implements the Shorted Job First Scheduling Algorithm. It does so
    // by sorting the processes by arrival time. It adds all the processes as the
    // arrive into a priority queue that sorts by burst time. This allows for easy
    // getting of the shortest process. I implemented a preemtive algorithm.
    public void shortestJobFirst() {
        reset();
        PriorityQueue<Process> queue = new PriorityQueue<>(10, Process.SortByRemaining);
        int time = 0;
        int index = 1;
        int waitTimeSum = 0;
        int turnArndTimeSum = 0;
        int responseTimeSum = 0;
        int prcssTime = 0;
        queue.add(processes.get(0));
        // Loop until there are no more processes in the queue.
        while(!queue.isEmpty()) {
            Process currentProcess = queue.poll();
            // This while loop checks if new processes have arrived and if they have
            // they are added to the priority queue and a new processes is selected
            // if there is a shorter process.
            while(index < processes.size() && time == processes.get(index).getArrivalTime()) {
                queue.add(processes.get(index));
                queue.add(currentProcess);
                currentProcess = queue.poll();
                index++;
            }
            boolean isNew = !currentProcess.process(time);
            // This checks if it is a new process or an already started one.
            if(isNew) {
                responseTimeSum += time - currentProcess.getArrivalTime();
                currentProcess.start();
            }
            // If the process is not complete then add it to the queue again.
            if(!currentProcess.isComplete()) queue.add(currentProcess);
            // If it is complete then calculate all the times.
            else {
                waitTimeSum += time - currentProcess.getBurstTime();
                turnArndTimeSum += time;
                prcssTime += currentProcess.getBurstTime();
            }
            time++;
        }
        printTimes(waitTimeSum, responseTimeSum, turnArndTimeSum, prcssTime);
    }

    // This method implements the Preemptive Priority Algorithm. It does so
    // by sorting the the processes by getting the processes as the arrive
    // and placing them in a priority queue that sorts by priority. This 
    // implementation is preemtive so it will pause a process if a process
    // with higher priority is placed in the queue.
    public void preemtivePriorityScheduling() {
        reset();
        PriorityQueue<Process> queue = new PriorityQueue<>(10, Process.SortByPriority);
        int time = 0;
        int index = 1;
        int waitTimeSum = 0;
        int turnArndTimeSum = 0;
        int responseTimeSum = 0;
        int prcssTime = 0;
        queue.add(processes.get(0));
        // This loops untill there are no more processes in the queue.
        while(!queue.isEmpty()) {
            Process currentProcess = queue.poll();
            // This loop adds new processes as they arrive to the queue.
            while(index < processes.size() && time == processes.get(index).getArrivalTime()) {
                queue.add(processes.get(index));
                queue.add(currentProcess);
                currentProcess = queue.poll();
                index++;
            }
            boolean isNew = !currentProcess.process(time);
            if(isNew) {
                responseTimeSum += time - currentProcess.getArrivalTime();
                currentProcess.start();
            }
            if(!currentProcess.isComplete()) queue.add(currentProcess);
            else {
                waitTimeSum += time - currentProcess.getBurstTime();
                turnArndTimeSum += time;
                prcssTime += currentProcess.getBurstTime();
            }
            time++;
        }
        printTimes(waitTimeSum, responseTimeSum, turnArndTimeSum, prcssTime);
    }

    // This method implements the Round Robin Scheduling Algorithm. It does so
    // by adding processes as the arrive into a queue. If the time quantum is
    // reached then the process is stopped and placed at the end of the queue.
    public void roundRobin() {
        reset();
        int time = 0;
        Queue<Process> processQueue = new LinkedList<>();
        Scanner userInput = new Scanner(System.in);
        int timeQuantum = 1;
        boolean incorrectTimeQuantum = true;
        // This loop gets the time quantum from the user.
        do {
            System.out.print("Enter the time quantum: ");
            try {
                timeQuantum = Integer.parseInt(userInput.nextLine());
                incorrectTimeQuantum = false;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Please enter an integer.");
            }
        } while(incorrectTimeQuantum);
        int index = 1;
        int waitTimeSum = 0;
        int turnArndTimeSum = 0;
        int prcssTime = 0;
        int responseTime = 0;
        processQueue.add(processes.get(0));
        // This loops until all processes are complete.
        while(!processQueue.isEmpty()) {
            Process currentProcess = processQueue.poll();
            // This loops until the time quantum is reached or until the process is complete.
            for(int i = 0; !currentProcess.isComplete() && i < timeQuantum; i++) {
                boolean isNew = currentProcess.process(time);
                // This loop adds all arrived processes to the queue.
                while(index < processes.size() && time == processes.get(index).getArrivalTime()) {
                    processQueue.add(processes.get(index));
                    index++;
                }
                if(isNew) {
                    responseTime += time - currentProcess.getArrivalTime();
                    currentProcess.start();
                }
                if(!currentProcess.isComplete()) {
                    processQueue.add(currentProcess);
                } else {
                    waitTimeSum += time - currentProcess.getBurstTime();
                    turnArndTimeSum += time;
                    prcssTime += currentProcess.getBurstTime();
                }
                time++;
            }
        }
        printTimes(waitTimeSum, responseTime, turnArndTimeSum, prcssTime);
    }

    // This method outputs the Average times.
    public void printTimes(int waitTime, int responseTime, int turnArndTime, int prcssTime) {
        int n = processes.size();
        System.out.println(" - Average Wait Time: " + (waitTime / n));
        System.out.println(" - Average Response Time: " + (responseTime / n));
        System.out.println(" - Average Turn Around Time: " + (turnArndTime / n));
        System.out.println(" - Average CPU Utilization: " + (prcssTime / n));
    }

    // This method resets the processes to be reused by a method.
    private void reset() {
        for(Process a: processes) {
            a.reset();
        }
        Collections.sort(processes, Process.SortByArrivalTime);
    }
}