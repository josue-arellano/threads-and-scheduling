import java.util.List;

public class MergingThread implements Runnable {
    private List<Integer> list;
    private List<Integer> mergedList;

    public MergingThread(List<Integer> list, List<Integer> mergedList) {
        this.list = list;
        this.mergedList = mergedList;
    }

    public void run() {
        int i = 0;
        int j = list.size() / 2;

            while(i < list.size() / 2 && j < list.size()) {
                int min = list.get(i);
                if(list.get(j) < min) {
                    min = list.get(j);
                    j++;
                } else i++;
                mergedList.add(min);
            }

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
    }
}