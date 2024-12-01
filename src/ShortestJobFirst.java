import java.util.*;

public class ShortestJobFirst implements SchedulingAlgorithm{
    private PriorityQueue<Process> readyQueue;
    public int totalWaitingTime = 0;
    private final List<Process> processes;
    public int contextSwitchTime;
    public int totalTurnAroundTime = 0;
    private final HashMap<Process, Integer> waitingTime = new HashMap<>();
    private final HashMap<Process, Integer> completionTime = new HashMap<>();





    public ShortestJobFirst(List<Process> processes, int contextSwitchTime) {
        this.contextSwitchTime = contextSwitchTime;
        readyQueue = new PriorityQueue<>(processes.size(), Comparator.comparingInt(Process::getBurstTime));
        this.processes = new ArrayList<>();
        for (Process p : processes)
            this.processes.add(new Process(p)); // Create a deep copy
    }


    @Override
    public void getExecutionOrder() {
        System.out.println("Execution Order:");
        readyQueue.clear();
        int time = 0;
        HashMap<String, Boolean> added = new HashMap<>();

        while (added.size() < processes.size()) {
            for (Process process : processes) {
                if (process.getArrivalTime() <= time && !added.containsKey(process.getName())) {
                    readyQueue.add(process);
                    added.put(process.getName(), true);
                }
            }
            if (!readyQueue.isEmpty()) {
                Process current = readyQueue.poll();
                time += contextSwitchTime;
                System.out.println(current.getName() + " entered CPU at: " + time);

                int start = time;
                time += current.getBurstTime();
                int end = time;

                waitingTime.put(current, start - current.getArrivalTime());
                totalWaitingTime += waitingTime.get(current);

                completionTime.put(current, end);
                totalTurnAroundTime += (end - current.getArrivalTime());

            } else {
                time++;
            }
        }


    }
    @Override
    public void getWaitingTime() {
        System.out.println("\n_________________________________________________________________\n");

        totalWaitingTime = 0;
        System.out.println("Waiting Time : ");
        for(Process p : processes){
            System.out.println(p.getName() + " : " + waitingTime.get(p));
            totalWaitingTime += waitingTime.get(p);
        }
        System.out.println("\n_________________________________________________________________\n");

    }

    @Override
    public void getTurnAroundTime() {
        System.out.println("Turn Around Time : ");
        for(Process p : processes){
            System.out.println(p.getName() + " : " +( waitingTime.get(p) + p.getBurstTime()));
            totalTurnAroundTime += waitingTime.get(p) + p.getBurstTime();
        }
        System.out.println("\n_________________________________________________________________\n");


    }

    @Override
    public double getAverageWaitingTime() {
        return (double)totalWaitingTime / processes.size();

    }

    @Override
    public double getAverageTurnAroundTime() {
        return (double)totalTurnAroundTime / processes.size();
    }
}
