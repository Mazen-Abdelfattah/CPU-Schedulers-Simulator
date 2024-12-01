public interface SchedulingAlgorithm {
    public  void getExecutionOrder();
    public  void getWaitingTime();
    public void getTurnAroundTime();
    public  double getAverageWaitingTime();
    public  double getAverageTurnAroundTime();

}
