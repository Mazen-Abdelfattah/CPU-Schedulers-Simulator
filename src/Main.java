import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Process> processes  = new ArrayList<>();
        int contextSwitchTime = 1;
        int maxTime = 0;

//        System.out.println("Enter the number of processes :");
//        int n = sc.nextInt();
//        System.out.println("Enter the context switch time :");
//        contextSwitchTime = sc.nextInt();

//        for (int i = 0; i < n; i++) {
//            System.out.println("Enter the name of the process :");
//            String name = sc.next();
//            System.out.println("Enter the color of the process :");
//            String color = sc.next();
//            color = color.toUpperCase();
//            System.out.println("Enter the arrival time of the process :");
//            int arrivalTime = sc.nextInt();
//            System.out.println("Enter the burst time of the process :");
//            int burstTime = sc.nextInt();
//            System.out.println("Enter the priority of the process :");
//            int priority = sc.nextInt();
//            System.out.println("Enter Round Robin Time Quantum :");
//            int quantum = sc.nextInt();
//            processes.add(new Process(name, color, arrivalTime, burstTime, priority, quantum));
//        }

//============================================================================
        Process process1 = new Process("P1", "RED", 0, 17, 4, 4);
        processes.add(process1);
        Process process2 = new Process("P2", "GREEN", 3, 6, 9, 3);
        processes.add(process2);
        Process process3 = new Process("P3", "BLUE", 4, 10, 3, 5);
        processes.add(process3);
        Process process4 = new Process("P4", "BLACK", 29, 4, 10, 2);
        processes.add(process4);
//============================================================================
//        Process process1 = new Process("P1", "RED", 0, 99, 4, 4);
//        processes.add(process1);
//        Process process2 = new Process("P2", "GREEN", 0, 60, 9, 3);
//        processes.add(process2);
//        Process process3 = new Process("P3", "BLUE", 4, 10, 3, 5);
//        processes.add(process3);
//        Process process4 = new Process("P4", "BLACK", 29, 4, 10, 2);
//        processes.add(process4);
// ============================================================================

//        Process process1 = new Process("P1", "RED", 0, 10, 4, 4);
//        processes.add(process1);
//        Process process2 = new Process("P2", "GREEN", 0, 6, 9, 3);
//        processes.add(process2);
//        Process process3 = new Process("P3", "BLUE", 0, 5, 3, 5);
//        processes.add(process3);
//        Process process4 = new Process("P4", "BLACK", 4, 2, 10, 2);
//        processes.add(process4);
// ============================================================================



        FcaiFactorSchedulingAlgorithm fcaiScheduler = new FcaiFactorSchedulingAlgorithm(processes);
        System.out.println(">>>>>>>>>>>>FCAI Factor Scheduling Algorithm<<<<<<<<<<<<<<");
        fcaiScheduler.getExecutionOrder();
        fcaiScheduler.getWaitingTime();
        fcaiScheduler.getTurnAroundTime();
        System.out.println("Average Waiting Time : " + fcaiScheduler.getAverageWaitingTime() + " ms");
        System.out.println("Average Turn Around Time : " + fcaiScheduler.getAverageTurnAroundTime() + " ms");
        fcaiScheduler.printQuantumHistory();
        System.out.println("\n_________________________________________________________________\n");

        SchedulingAlgorithm sjf = new ShortestJobFirst(processes, contextSwitchTime);
        System.out.println("Shortest Job First Scheduling Algorithm");
        sjf.getExecutionOrder();
        sjf.getWaitingTime();
        sjf.getTurnAroundTime();
        System.out.println("Average Waiting Time : " + sjf.getAverageWaitingTime() + " ms");
        System.out.println("Average Turn Around Time : " + sjf.getAverageTurnAroundTime() + " ms");
        System.out.println("\n_________________________________________________________________\n");

        SchedulingAlgorithm priorityScheduling = new PrioritySchedulingAlgorithm(processes, contextSwitchTime);
        System.out.println("\nPriority Scheduling Algorithm");
        priorityScheduling.getExecutionOrder();
        priorityScheduling.getWaitingTime();
        priorityScheduling.getTurnAroundTime();
        System.out.println("Average Waiting Time : " + priorityScheduling.getAverageWaitingTime() + " ms");
        System.out.println("Average Turn Around Time : " + priorityScheduling.getAverageTurnAroundTime() + " ms");
        System.out.println("\n_________________________________________________________________\n");

        SchedulingAlgorithm srtf = new ShortestRemainingTimeFirst(processes, contextSwitchTime);
        System.out.println("\nShortest Remaining Time First Scheduling Algorithm");
        srtf.getExecutionOrder();
        srtf.getWaitingTime();
        srtf.getTurnAroundTime();
        System.out.println("Average Waiting Time : " + srtf.getAverageWaitingTime() + " ms");
        System.out.println("Average Turn Around Time : " + srtf.getAverageTurnAroundTime() + " ms");
        System.out.println("\n_________________________________________________________________\n");

    }
}