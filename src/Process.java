import java.util.ArrayList;
import java.util.List;

public class Process implements Comparable<Process>{
    private String name;
    private String color;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int id;
    private int age = 0;
    private int remainingTime ;
    private int quantum;  // Track the quantum assigned to the process
    private int completionTime;
    private int turnaroundTime;
    private int waitingTime;
    private int fcaiFactor;
    boolean isAddedToQueue = false;

    private List<Integer> quantumHistory = new ArrayList<>();  // Track quantum history

    public Process(String name, String color, int arrivalTime, int burstTime, int priority, int quantum) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.quantum = quantum;
    }

    public Process(Process other) { // Copy constructor
        this.name = other.name;
        this.color = other.color;
        this.arrivalTime = other.arrivalTime;
        this.burstTime = other.burstTime;
        this.priority = other.priority;
        this.remainingTime = other.remainingTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge(){return age;}

    public void setAge(int age){this.age=age;}

    public void setRemainingTime(int time){remainingTime=time;}

    public int getRemainingTime(){return remainingTime;}

    @Override
    public int compareTo(Process other) {
        int remainingTimeComparison = Integer.compare(this.remainingTime, other.remainingTime);

        if (remainingTimeComparison != 0) {
            return remainingTimeComparison;
        }

        // If remaining time is the same, break the tie by arrival time
        return Integer.compare(this.arrivalTime, other.arrivalTime);
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getFcaiFactor() {
        return fcaiFactor;
    }

    public void setFcaiFactor(int fcaiFactor) {
        this.fcaiFactor = fcaiFactor;
    }

    public List<Integer> getQuantumHistory() {
        return quantumHistory;
    }

    public void setQuantumHistory(List<Integer> quantumHistory) {
        this.quantumHistory = quantumHistory;
    }

    public boolean hasRemainingTime() {
        return getRemainingTime() > 0;
    }


    public void addQuantumToHistory(int quantum) {
        this.quantumHistory.add(quantum);
    }

}