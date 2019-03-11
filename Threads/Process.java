/*************************************************************** 
*   file: Process.java 
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
import java.util.Comparator;

public class Process {
    private int pid;
    private double arrivalTime;
    private int burstTime;
    private int priority;
    private int timeRunning;
    private boolean complete;
    private boolean started;

    public Process(int pid, double arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        timeRunning = 0;
        complete = false;
        started = false;
    }

    // The following methods return values of the Process.
    public int getPid() {
        return pid;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }

    // Comparator to easily insert into Priority Queues and sorting methods.
    public static Comparator<Process> SortByArrivalTime = new Comparator<Process>() {
        public int compare(Process p1, Process p2) {
            return Double.compare(p1.arrivalTime, p2.arrivalTime);
        }
    };

    public static Comparator<Process> SortByPriority = new Comparator<Process>() {
        public int compare(Process p1, Process p2) {
            return Integer.compare(p1.priority, p2.priority);
        }
    };

    public static Comparator<Process> SortByRemaining = new Comparator<Process>() {
        public int compare(Process p1, Process p2) {
            return Integer.compare(p1.burstTime - p1.timeRunning, p2.burstTime - p2.timeRunning);
        }
    };

    public boolean process(int time) {
        timeRunning++;
        if(timeRunning == burstTime) complete = true;
        String printString = String.format("[%3dms: Process %-2d]", time, pid);
        printString += complete ? String.format(" - Process %d has finished running.", pid) : "";
        System.out.println(printString);
        return started;
    }

    public boolean isComplete() {
        return complete;
    }

    @Override
    public String toString() {
        return "[" + pid + ", " + arrivalTime + ", " + burstTime + ", " + priority + "]";
    }

    // Resets the values needed for processing.
    public void reset() {
        timeRunning = 0;
        complete = false;
        started = false;
    }

    // Sets a process to started state.
    public void start() {
        started = true;
    }
}