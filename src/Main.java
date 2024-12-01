import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Process> processes  = new ArrayList<>();
        int contextSwitchTime = 1;
        int maxTime = 0;

        System.out.println("Enter the number of processes :");
        int n = sc.nextInt();
        System.out.println("Enter Round Robin Time Quantum :");
        int quantum = sc.nextInt();
        System.out.println("Enter the context switch time :");
        contextSwitchTime = sc.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.println("Enter the name of the process :");
            String name = sc.next();
            System.out.println("Enter the color of the process :");
            String color = sc.next();
            color = color.toUpperCase();
            System.out.println("Enter the arrival time of the process :");
            int arrivalTime = sc.nextInt();
            System.out.println("Enter the burst time of the process :");
            int burstTime = sc.nextInt();
            System.out.println("Enter the priority of the process :");
            int priority = sc.nextInt();
            processes.add(new Process(name, color, arrivalTime, burstTime, priority));
        }
        SchedulingAlgorithm sjf = new ShortestJobFirst(processes, contextSwitchTime);
        System.out.println("Shortest Job First Scheduling Algorithm");
        sjf.getExecutionOrder();
        sjf.getWaitingTime();
        sjf.getTurnAroundTime();
        System.out.println("Average Waiting Time : " + sjf.getAverageWaitingTime() + " ms");
        System.out.println("Average Turn Around Time : " + sjf.getAverageTurnAroundTime() + " ms");
        System.out.println("\n_________________________________________________________________\n");



    }
}