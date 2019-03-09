import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.lang.Thread;

public class Main{
    public static void main(String[] args) {
        int[] arr = {7, 12, 19, 3, 18, 4, 2, 6, 15, 8};
        List<Integer> list = new ArrayList<>();
        int half = arr.length / 2;
        List<Integer> mergedList = new ArrayList<>();
        for(int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        Thread thread2 = new Thread(new SortingThread(list, list.size() / 2, list.size()));
        Thread thread1 = new Thread(new SortingThread(list, 0, list.size() / 2));
        thread1.start();
        thread2.start();
        
        try {
            // must waith for threads to finish before merging;
            thread1.join();
            thread2.join();
            Thread mergeThread = new Thread(new MergingThread(list, mergedList));
            mergeThread.start();
            mergeThread.join();
            System.out.println(mergedList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}