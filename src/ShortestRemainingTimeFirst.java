import java.util.*;
public class ShortestRemainingTimeFirst implements SchedulingAlgorithm{
    private final List<Process> processes;
    private final Map<Process, Integer> waitingTime = new HashMap<>();
    private final Map<Process, Integer> completionTime = new HashMap<>();
    private int totalWaitingTime = 0;
    private int totalTurnAroundTime = 0;
    private final int contextSwitchTime;
    private int time ;

    PriorityQueue<Process> readyQueue = new PriorityQueue<>((p1, p2) -> {
        int effectiveTimeThis = p1.getRemainingTime() - p1.getAge();
        int effectiveTimeOther = p2.getRemainingTime() - p2.getAge();

        if (effectiveTimeThis != effectiveTimeOther) {
            return Integer.compare(effectiveTimeThis, effectiveTimeOther);
        } else {
            return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
        }
    });


    ShortestRemainingTimeFirst(List<Process> processes, int contextSwitchTime){
        this.processes = processes;
        this.contextSwitchTime = contextSwitchTime;
        this.readyQueue = new PriorityQueue<>();
    }
    public void getExecutionOrder() {
        System.out.println("Execution Order:");
        int counter = 0;
        time = 0; // Current time
        int completedProcesses = 0; // Track number of completed processes
        final int n = processes.size(); // Total processes
        Process past=null;

        while (completedProcesses < n) {
            // Add processes that have arrived by the current time
            for (Process process : processes) {
                if (process.getArrivalTime() <= time && process.getRemainingTime() > 0 && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                    waitingTime.put(process,0);
                    counter++;
                }
            }

            // Process the next job in the readyQueue
            if (!readyQueue.isEmpty()) {
                Process current = readyQueue.poll();
                if(past!=current && counter > 1)time+=contextSwitchTime;
                int nextArrivalTime = Integer.MAX_VALUE;

                // Find the next arrival time for a process not yet in the queue
                for (Process process : processes) {
                    if (!readyQueue.contains(process) && process.getArrivalTime() > time && process.getRemainingTime() > 0) {
                        nextArrivalTime = Math.min(nextArrivalTime, process.getArrivalTime());
                    }
                }

                // Calculate the time to run the current process
                int timeToRun = Math.min(current.getRemainingTime(), nextArrivalTime - time);

                // Simulate running the process
                System.out.println(current.getName() + " executed from time " + time + " to " + (time + timeToRun));
                current.setRemainingTime(current.getRemainingTime()-timeToRun);
                time += timeToRun;
//                System.out.println("current time: "+time+"\n");
//                for(Process p : processes)
//                {
//                    System.out.println(p.getName()+ " has been waiting for : "+waitingTime.get(p)+"\n");
//                }
                // Age all processes in the ready queue
                for (Process p : readyQueue) {
                    p.setAge(p.getAge()+timeToRun); // Increment age by 1 (or more if needed)
                }

                // If the process is complete, update metrics
                if (current.getRemainingTime() == 0) {
                    completionTime.put(current, time);
                    waitingTime.put(current, time - current.getBurstTime() - current.getArrivalTime());
                    totalWaitingTime += waitingTime.get(current);
                    totalTurnAroundTime += time - current.getArrivalTime();
                    completedProcesses++;
                } else {
                    // If not complete, re-add it to the queue
                    readyQueue.add(current);

                }

                past=current;
            } else {
                // If no process is ready, jump to the next arrival time
                int nextArrivalTime = processes.stream()
                        .filter(p -> p.getArrivalTime() > time && p.getRemainingTime() > 0)
                        .mapToInt(Process::getArrivalTime)
                        .min()
                        .orElse(time + 1); // If no future arrivals, increment by 1
                time = nextArrivalTime;
            }

        }
    }


    public void getWaitingTime() {
        System.out.println("\nWaiting Times:");
        totalWaitingTime = 0;
        for (Process p : processes) {
            System.out.println(p.getName() + ": " + waitingTime.getOrDefault(p, 0));
            totalWaitingTime += waitingTime.getOrDefault(p, 0);
        }
        System.out.println("\nTotal Waiting Time: " + totalWaitingTime);
    }

    public void getTurnAroundTime() {
        System.out.println("\nTurnaround Times:");
        totalTurnAroundTime = 0;
        for (Process p : processes) {
            int tat = waitingTime.getOrDefault(p, 0) + p.getBurstTime();
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