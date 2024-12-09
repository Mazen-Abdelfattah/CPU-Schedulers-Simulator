import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PrioritySchedulingAlgorithm implements SchedulingAlgorithm {
    private List<Process> processes;
    private int contextSwitchTime;
    private List<Process> executionOrder;
    private int totalWaitingTime;
    private int totalTurnAroundTime;

    public PrioritySchedulingAlgorithm(List<Process> processes, int contextSwitchTime) {
        this.processes = new ArrayList<>(processes);
        this.contextSwitchTime = contextSwitchTime;
        this.executionOrder = new ArrayList<>();
        this.totalWaitingTime = 0;
        this.totalTurnAroundTime = 0;
    }

    @Override
    public void getExecutionOrder() {
        // Sort processes based on priority (lower priority number means higher priority)
        processes.sort(Comparator.comparingInt(Process::getPriority)
                .thenComparingInt(Process::getArrivalTime));

        int currentTime = 0;
        List<Process> readyQueue = new ArrayList<>();

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Add all processes that have arrived by current time to the ready queue
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime) {
                    readyQueue.add(p);
                }
            }

            // Remove the processes that have been added to the ready queue from the original list
            int finalCurrentTime = currentTime;
            processes.removeIf(p -> p.getArrivalTime() <= finalCurrentTime);

            // Sort the ready queue by priority and arrival time
            readyQueue.sort(Comparator.comparingInt(Process::getPriority)
                    .thenComparingInt(Process::getArrivalTime));

            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.get(0);
                readyQueue.remove(0);

                // Add the process to the execution order and calculate times
                executionOrder.add(currentProcess);
                currentTime += currentProcess.getBurstTime();
                totalTurnAroundTime += currentTime - currentProcess.getArrivalTime();
                totalWaitingTime += (currentTime - currentProcess.getArrivalTime()) - currentProcess.getBurstTime();

                // Include context switching time if there is a next process
                if (!readyQueue.isEmpty()) {
                    currentTime += contextSwitchTime;
                }
            } else {
                // If no process is ready, increment time to the arrival time of the next process
                if (!processes.isEmpty()) {
                    currentTime = processes.get(0).getArrivalTime();
                }
            }
        }
    }

    @Override
    public void getWaitingTime() {
        System.out.println("Processes waiting time:");
        for (Process p : executionOrder) {
            int waitingTime = (totalWaitingTime - p.getBurstTime());
            System.out.println("Process " + p.getName() + " waiting time: " + waitingTime + " ms");
        }
    }

    @Override
    public void getTurnAroundTime() {
        System.out.println("Processes turnaround time:");
        for (Process p : executionOrder) {
            int turnAroundTime = (totalTurnAroundTime - p.getArrivalTime());
            System.out.println("Process " + p.getName() + " turnaround time: " + turnAroundTime + " ms");
        }
    }

    @Override
    public double getAverageWaitingTime() {
        return (double) totalWaitingTime / executionOrder.size();
    }

    @Override
    public double getAverageTurnAroundTime() {
        return (double) totalTurnAroundTime / executionOrder.size();
    }

    // Method to print the execution order
    public void printExecutionOrder() {
        System.out.println("Execution order of processes:");
        for (Process p : executionOrder) {
            System.out.println("Process " + p.getName() + " executed.");
        }
    }
}
//