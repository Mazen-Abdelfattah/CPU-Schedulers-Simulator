import java.util.*;

/*
The FCAI scheduler dynamically adjusts process priority based on a composite factor that takes into account priority, arrival time, and remaining burst time.
Processes have adaptive quantum durations and can be preempted after 40% of execution, with preempted processes re-added to the queue.
The approach aims to balance fairness and efficiency while addressing issues like starvation.
 */
public class FcaiFactorSchedulingAlgorithm implements SchedulingAlgorithm {
    private final List<Process> processes;
    private final List<Process> completedProcesses = new ArrayList<>();
    private LinkedList<Process> readyQueue;
    private final int lastArrivalTime;
    private final int maxBurstTime;
    private int totalWaitingTime = 0;
    private int totalTurnAroundTime = 0;


    public FcaiFactorSchedulingAlgorithm(List<Process> processes) {
        this.processes = processes;
        this.lastArrivalTime = processes.getLast().getArrivalTime();
        this.maxBurstTime = processes.stream()
                .mapToInt(Process::getBurstTime)
                .max()
                .orElse(0);
        this.readyQueue = new LinkedList<>();

        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        for (Process p : processes) {
            int fcaiFactor = calculateFcaiFactor(p, lastArrivalTime, maxBurstTime);
            p.setFcaiFactor(fcaiFactor);
        }

    }

    // Calculate the FCAI factor dynamically for each process based on priority, arrival time, and remaining burst time
    public int calculateFcaiFactor(Process p, int lastArrivalTime, int maxBurstTime) {
        double V1 = lastArrivalTime / 10.0;
        double V2 = maxBurstTime / 10.0;
        return (int) Math.ceil((10 - p.getPriority()) + Math.ceil(p.getArrivalTime() / V1) + Math.ceil(p.getRemainingBurstTime() / V2));
    }

    // Update quantum based on whether the process was preempted
    public void updateQuantum(Process p, boolean preempted, int unusedQuantum) {
        if (preempted) {
            p.setQuantum(p.getQuantum() + unusedQuantum);
            p.addQuantumToHistory(p.getQuantum());
        } else {
            p.setQuantum(p.getQuantum() + 2);
            p.addQuantumToHistory(p.getQuantum());
        }
    }


    @Override
    public void getExecutionOrder() {
        int time = 0;
        boolean flagForFirstProcess = true;
        boolean flagForLastProcess = false;

        // Print table headers (if needed, you could print these once outside the loop)
        System.out.printf("%-10s%-10s%-15s%-20s%-20s%-10s%-10s%n",
                "Time", "Process", "ExecutedTime", "RemainingBurstTime",
                "UpdatedQuantum", "Priority", "FCAI Factor");


        while (completedProcesses.size() != processes.size()) {
            if (completedProcesses.size() == processes.size() - 1)
                flagForLastProcess = true;


            checkNewProcesses(time);
            Process currentProcess = readyQueue.pollFirst();
            int executedTime = 0;
            int startTime = time;
            int initialQuantum = currentProcess.getQuantum();
            int nonPreemptiveQuantum = (int) Math.ceil(currentProcess.getQuantum() * 0.4);
            if (nonPreemptiveQuantum > currentProcess.getRemainingBurstTime()) {
                //Edge testcase when the 40% of the Quantum > the remaining burst time (when the process is almost done)
                nonPreemptiveQuantum = currentProcess.getRemainingBurstTime();
            }
            currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - nonPreemptiveQuantum);
            time += nonPreemptiveQuantum;
            executedTime += nonPreemptiveQuantum;

            // Check and update FCAI factor after the non-preemptive execution phase
            int fcaiFactor = calculateFcaiFactor(currentProcess, lastArrivalTime, maxBurstTime);
            currentProcess.setFcaiFactor(fcaiFactor);


            //I have added the less than or equal instead of (==) to not miss the 1 second
            while (currentProcess.getFcaiFactor() <= getMinimumFcaiFactor(currentProcess) && executedTime < currentProcess.getQuantum()
                    && currentProcess.hasRemainingBurstTime()
            ) {
                if (currentProcess.getFcaiFactor() == getMinimumFcaiFactor(currentProcess) && !readyQueue.isEmpty())
                    break;//To handle the interrupt after the loop

                time++;
                //To check whether a new process arrived or not. If so break because it is an interrupt
                for (int j = 0; j < processes.size(); j++) {
                    Process p = processes.get(j);
                    if (p.getArrivalTime() <= time && !p.isAddedToQueue) {
                        readyQueue.add(p);
                        p.isAddedToQueue = true;
                        p.addQuantumToHistory(p.getQuantum());
                        break;
                    }
                }
                executedTime++;
                currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - 1);
                fcaiFactor = calculateFcaiFactor(currentProcess, lastArrivalTime, maxBurstTime);
                currentProcess.setFcaiFactor(fcaiFactor);

            }


            // If the process has finished execution, set its completion time
            if (!currentProcess.hasRemainingBurstTime()) {
                currentProcess.setCompletionTime(time);
                completedProcesses.add(currentProcess);
            }

            // If there is still burst time left, the process will be re-added to the queue
            if (currentProcess.hasRemainingBurstTime()) {
                if (flagForFirstProcess) {
                    checkNewProcesses(time);
                    flagForFirstProcess = false;
                }
                readyQueue.addLast(currentProcess);
                if (flagForLastProcess) {
                    time += currentProcess.getQuantum();
                    currentProcess.setCompletionTime(time);
                    currentProcess.setQuantum(initialQuantum);
                    executedTime += initialQuantum;
                    currentProcess.setRemainingBurstTime(0);
                    completedProcesses.add(currentProcess);
                } else if (currentProcess.getQuantum() == executedTime) {
                    updateQuantum(currentProcess, false, 0);
                } else {
                    int unusedQuantum = currentProcess.getQuantum() - executedTime;
                    updateQuantum(currentProcess, true, unusedQuantum);
                    int minIndex = getMinimumFcaiFactorINDEX();
                    Process temp = readyQueue.get(minIndex);
                    readyQueue.remove(minIndex);
                    readyQueue.addFirst(temp);
                }

            }
            int finalQuantum = currentProcess.getQuantum();
            // Print each process' execution info in a structured manner
            System.out.printf("%-10s%-10s%-15d%-20d", //s/d-> the no. of chars that it will leave, %-> the beginning of a format specifier
                    startTime + "-" + time, currentProcess.getName(),
                    executedTime, currentProcess.getRemainingBurstTime());

            if (initialQuantum == finalQuantum) {
                System.out.printf("%-20s", "Completed");
            } else {
                System.out.printf("%-20s", initialQuantum + "->" + finalQuantum);
            }
            System.out.printf("%-10d%-10d%n", currentProcess.getPriority(), currentProcess.getFcaiFactor());
        }
        System.out.println("----------");
    }

    private void checkNewProcesses(int time) {
        for (int j = 0; j < processes.size(); j++) {
            Process p = processes.get(j);
            if (p.getArrivalTime() <= time && !p.isAddedToQueue) {
                readyQueue.add(p);
                p.isAddedToQueue = true;
                p.addQuantumToHistory(p.getQuantum());
            }
        }
    }

    private int getMinimumFcaiFactor(Process currentProcess) {
        if (readyQueue.isEmpty()) {
            return currentProcess.getFcaiFactor();
        }
        int minFactor = Integer.MAX_VALUE;
        for (Process p : readyQueue) {
            if (p.getFcaiFactor() < minFactor)
                minFactor = p.getFcaiFactor();
        }
        return minFactor;
    }

    private int getMinimumFcaiFactorINDEX() {
        int minFactor = Integer.MAX_VALUE;
        int index = 0;
        int currFCAI;
        for (int i = 0; i < readyQueue.size(); i++) {
            currFCAI = readyQueue.get(i).getFcaiFactor();
            if (currFCAI < minFactor) {
                minFactor = currFCAI;
                index = i;
            }
        }
        return index;
    }

    @Override
    public void getWaitingTime() {
        System.out.println("Processes waiting time:");
        for (Process p : processes) {
            int waitingTime = p.getCompletionTime() - p.getArrivalTime() - p.getBurstTime();
            p.setWaitingTime(waitingTime);
            System.out.println("Process " + p.getName() + " waiting time: " + waitingTime + " ms");
            totalWaitingTime += waitingTime;
        }
        System.out.println("----------");
    }

    @Override
    public void getTurnAroundTime() {
        System.out.println("Processes turnaround time:");
        for (Process p : processes) {
            int turnaroundTime = p.getCompletionTime() - p.getArrivalTime();
            p.setTurnaroundTime(turnaroundTime);
            System.out.println("Process " + p.getName() + " turnaround time: " + turnaroundTime + " ms");
            totalTurnAroundTime += turnaroundTime;
        }
        System.out.println("----------");
    }

    public void printQuantumHistory() {
        System.out.println("----------");
        for (Process p : processes) {
            System.out.println("Process " + p.getName() + " quantum history: " + p.getQuantumHistory());
        }
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