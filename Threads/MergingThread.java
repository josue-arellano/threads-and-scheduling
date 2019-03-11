/*************************************************************** 
*   file: MergingThread.java 
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

// This class implements the merging of two sides of a list.
public class MergingThread implements Runnable {
    private List<Integer> list;
    private List<Integer> mergedList;
    private int threadID;

    public MergingThread(List<Integer> list, List<Integer> mergedList, int threadID) {
        this.list = list;
        this.mergedList = mergedList;
        this.threadID = threadID;
    }

    // Method needed to create thread. This method implements the merging algorithm.
    public void run() {
        int i = 0;
        int j = list.size() / 2;

        // Inserts values into a new ArrayList in a sorted order.
        while(i < list.size() / 2 && j < list.size()) {
            int min = list.get(i);
            if(list.get(j) < min) {
                min = list.get(j);
                j++;
            } else i++;
            mergedList.add(min);
        }

        // These loops insert the remaining values into the arraylist
        // from either side of the list.
        if(i == list.size() / 2) {
            while(j < list.size()) {
                mergedList.add(list.get(j));
                j++;
            }
        } else {
            while(i < list.size() / 2) {
                mergedList.add(list.get(i));
                i++;
            }
        }
        System.out.println("Thread " + threadID + " is done merging.");
    }
}