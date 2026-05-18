package Model;

/**
 *
 * @author admin
 */
public class Statistic implements Comparable<Statistic>{
     private String peakCode;
     private int numberOfStudent;
     private double totalCost;

    public Statistic(String peakCode) {
         this.peakCode = peakCode;
         this.numberOfStudent=0;
         this.totalCost= 0;
    }

    public String getPeakCode() {
        return peakCode;
    }

    public int getNumberOfStudent() {
        return numberOfStudent;
    }

    public double getTotalCost() {
        return totalCost;
    }
     
     public Statistic increaseStudent(double studentCost){
         this.numberOfStudent++;
         this.totalCost+= studentCost;
         return this;
     }

    @Override
    public int compareTo(Statistic o) {
        return this.peakCode.compareTo(o.peakCode);
    }
     
     
     
     
}
