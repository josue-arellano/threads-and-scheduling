/*************************************************************** 
*   file: SortingThread.java 
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
import java.lang.Runnable;
import java.lang.Exception;

// This class implements the sorting algorithm for a portion of a list.
public class SortingThread implements Runnable {
    private List<Integer> list;
    private int begin;
    private int end;
    private int threadID;

    public SortingThread(List<Integer> list, int begin, int end, int threadID) {
        this.list = list;
        this.begin = begin;
        this.end = end;
        this.threadID = threadID;
    }

    // This method is needed by thread and it implements the sorting algorithm.
    public void run() {
        try {
            // This implements bubble sort on a portion of an array.
            for(int i = begin; i < end - 1; i++) {
                    int tempI = 0;
                    if(i >= list.size() / 2) tempI = i - list.size() / 2;
                    for(int j = begin; j < end - tempI - 1; j++) {
                    if(list.get(j) > list.get(j + 1)) {
                        int temp = list.get(j);
                        list.set(j, list.get(j + 1));
                        list.set(j + 1, temp);
                    }
                }
            }
            System.out.println("Thread " + threadID + " is finished sorting.");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}