import java.util.ArrayList;
import java.util.List;

public class Process {
    private String name;
    private String color;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int remainingBurstTime;  // Track the remaining burst time for the process
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
        this.remainingBurstTime = burstTime;  // Initialize remaining burst time
        this.priority = priority;
        this.quantum = quantum;
    }

    // Copy constructor
    public Process(Process other) {
        this.name = other.name;
        this.color = other.color;
        this.arrivalTime = other.arrivalTime;
        this.burstTime = other.burstTime;
        this.remainingBurstTime = other.remainingBurstTime;
        this.priority = other.priority;
        this.quantum = other.quantum;
        this.quantumHistory = new ArrayList<>(other.quantumHistory);
    }

    // Getter and Setter methods
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

    public int getQuantum() {
        return quantum;
    }

    public int getRemainingBurstTime() {
        return remainingBurstTime;
    }

    public void setRemainingBurstTime(int remainingBurstTime) {
        this.remainingBurstTime = remainingBurstTime;
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

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    // Add quantum time to the history
    public void addQuantumToHistory(int quantum) {
        this.quantumHistory.add(quantum);
    }

    public List<Integer> getQuantumHistory() {
        return quantumHistory;
    }

    public boolean hasRemainingBurstTime() {
        return getRemainingBurstTime() > 0;
    }

    public int getFcaiFactor() {
        return fcaiFactor;
    }

    public void setFcaiFactor(int fcaiFactor) {
        this.fcaiFactor = fcaiFactor;
    }
}
