


public class Process implements Comparable<Process>{
    private String name;
    private String color;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int id;
    private int age = 0;
    private int remainingTime ;

    public Process(String name, String color, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
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
}