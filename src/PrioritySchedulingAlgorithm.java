import java.util.*;

public class PrioritySchedulingAlgorithm implements SchedulingAlgorithm {
    private final List<Process> processes;
    private final int contextSwitchTime;  // Time taken to switch from one process to another.
    private final List<Process> executionOrder;
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
        int currentTime = 0;
        List<Process> readyQueue = new ArrayList<>();

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            for (Iterator<Process> it = processes.iterator(); it.hasNext(); ) {
                Process p = it.next();
                // it's arrived.
                if (p.getArrivalTime() <= currentTime) {
                    readyQueue.add(p);
                    it.remove();
                }
            }

            readyQueue.sort(Comparator.comparingInt(Process::getPriority)
                    .thenComparingInt(Process::getArrivalTime));

            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.removeFirst();
                if (!executionOrder.isEmpty())
                    currentTime += contextSwitchTime;
                int startTime = currentTime, endTime = startTime + currentProcess.getBurstTime();
                int turnaroundTime = endTime - currentProcess.getArrivalTime(), waitingTime = startTime - currentProcess.getArrivalTime();
                currentProcess.setWaitingTime(waitingTime);  // Time spent waiting in the ready queue before execution.
                totalWaitingTime += waitingTime;
                currentProcess.setTurnaroundTime(turnaroundTime);  // Time from process arrival to completion.
                totalTurnAroundTime += turnaroundTime;
                currentTime = endTime;
                executionOrder.add(currentProcess);
            } else {  // idle time, CPU's free.
                if (!processes.isEmpty()) {
                    currentTime = processes.getFirst().getArrivalTime();
                }
            }
        }
    }

    @Override
    public void getWaitingTime() {
        System.out.println("Processes waiting time:");
        for (Process p : executionOrder) {
            System.out.println("Process " + p.getName() + " waiting time: " + p.getWaitingTime() + " ms");
        }
    }

    @Override
    public void getTurnAroundTime() {
        System.out.println("Processes turnaround time:");
        for (Process p : executionOrder) {
            System.out.println("Process " + p.getName() + " turnaround time: " + p.getTurnaroundTime() + " ms");
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

    public void printExecutionOrder() {
        System.out.println("Execution order of processes:");
        for (Process p : executionOrder) {
            System.out.println("Process " + p.getName() + " executed.");
        }
    }
}
