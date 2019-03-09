import java.util.List;
import java.lang.Runnable;
import java.lang.Exception;

public class SortingThread2 implements Runnable {
    List<Integer> list;
    int begin;
    int end;

    public SortingThread2(List<Integer> list, int begin, int end) {
        this.list = list;
        this.begin = begin;
        this.end = end;
    }

    public void run() {
        try {
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
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}