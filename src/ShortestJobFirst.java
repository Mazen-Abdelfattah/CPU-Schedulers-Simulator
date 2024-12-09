import java.util.*;

public class ShortestJobFirst implements SchedulingAlgorithm {
    private final PriorityQueue<Process> readyQueue;
    private final List<Process> processes;
    private final Map<Process, Integer> waitingTime = new HashMap<>();
    private final Map<Process, Integer> completionTime = new HashMap<>();
    private int totalWaitingTime = 0;
    private int totalTurnAroundTime = 0;
    private final int contextSwitchTime;

    public ShortestJobFirst(List<Process> processes, int contextSwitchTime) {
        this.contextSwitchTime = contextSwitchTime;
        this.readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getBurstTime));
        this.processes = new ArrayList<>();
        for (Process p : processes) {
            this.processes.add(new Process(p)); // Create a deep copy
        }
    }

    @Override
    public void getExecutionOrder() {
        System.out.println("Execution Order:");
        readyQueue.clear();
        int time = 0;
        Set<Process> added = new HashSet<>();

        while (added.size() < processes.size() || !readyQueue.isEmpty()) {
            for (Process process : processes) {
                if (process.getArrivalTime() <= time && !added.contains(process)) {
                    readyQueue.add(process);
                    added.add(process);
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
        System.out.println("\nWaiting Times:");
        totalWaitingTime = 0;
        for (Process p : processes) {
            System.out.println(p.getName() + ": " + waitingTime.get(p));
            totalWaitingTime += waitingTime.get(p);
        }
        System.out.println("\nTotal Waiting Time: " + totalWaitingTime);
    }

    @Override
    public void getTurnAroundTime() {
        System.out.println("\nTurnaround Times:");
        totalTurnAroundTime = 0;
        for (Process p : processes) {
            int tat = waitingTime.get(p) + p.getBurstTime();
            System.out.println(p.getName() + ": " + tat);
            totalTurnAroundTime += tat;
        }
        System.out.println("\nTotal Turnaround Time: " + totalTurnAroundTime);
    }

    @Override
    public double getAverageWaitingTime() {
        return (double) totalWaitingTime / processes.size();
    }

    @Override
    public double getAverageTurnAroundTime() {
        return (double) totalTurnAroundTime / processes.size();
    }
}

//