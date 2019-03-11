/*************************************************************** 
*   file: Main.java 
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
import java.util.List;
import java.util.ArrayList;
import java.lang.Thread;

public class Main{
    public static void main(String[] args) {
        System.out.println("---------Thread Program---------");

        int[] arr = {7, 12, 19, 3, 18, 4, 2, 6, 15, 8};
        List<Integer> list = new ArrayList<>();
        List<Integer> mergedList = new ArrayList<>();
        // inserting all values in array into arraylist
        for(int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        System.out.println("Original Array: " + list);
        // creating threads that will sort the halves of the array
        Thread thread2 = new Thread(new SortingThread(list, list.size() / 2, list.size(), 1));
        Thread thread1 = new Thread(new SortingThread(list, 0, list.size() / 2, 2));
        // starting both threads
        thread1.start();
        thread2.start();
        
        try {
            // must wait for threads to finish before merging;
            thread1.join();
            thread2.join();
            // creating thread that will merge the two sides into one list
            Thread mergeThread = new Thread(new MergingThread(list, mergedList, 3));
            mergeThread.start();
            // wait for the merge to finish before printing the array.
            mergeThread.join();
            System.out.println("Sorted Array: " + mergedList);
            // handle the exceptions thrown by .join().
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("\n------CPU Scheduling Simulator------");
        // create object that will handle all the scheduling portion of the project.
        SchedulingAlgorithms schedule = new SchedulingAlgorithms();
        // this method requests for user input and starts the requested algorithm.
        schedule.start();
    }
}