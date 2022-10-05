import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Driver {

    private static int numOfEquations = 4;  //make sure to change
    private static int numOfVariables = 4 + 1;
    private static double[][] testMatrix2 = {
        {2,3,0},
        {-1,2,-1},
        {3,0,2}
    }; 
    private static double [] testb2 = {8,0,9};
    private static double[][] testMatrix = {
        {3,-13,9,3,},
        {-6,4,1,-18},
        {6,-2,2,4},
        {12,-8,6,10}
    };
    /*2 3 0 8
    -1 2 -1 0
    3 0 2 9 */
    private static double [] testb = {-19,-34,16,26};
    private static double[][] userMatrixA;
    private static double[] userMatrixB;
    private static int pivotCount = 0;
    private static List<Integer> pivotRows = new ArrayList<>();  // index vector
    private static List<Integer> indexVector = new ArrayList<>();
    private static List<Double> maxCoefficients = new ArrayList<>();

    public static void main(String[] args) {
        //Scanner input = new Scanner(System.in);
        //System.out.print("Enter number of equations: ");
        //numOfEquations = Integer.parseInt(input.nextLine());        // row
        //System.out.print("Enter the number variables to solve for: ");
        //numOfVariables = Integer.parseInt(input.nextLine());         // col
        //input.close();
        /*for (int i = 0; i < numOfEquations; i++) {
            int pivotRow = getScaledRatio(pivotCount++);
            for (int j = 0; j < numOfVariables; i++) {

            }
        } */
        List<double[]> listA = new ArrayList<>(); 
        List<Double> listB = new ArrayList<>(); 
        try {
            File myObj = new File("InputFile.txt");
            Scanner myReader = new Scanner(myObj);
            
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.replaceAll("\\s", "") == "") break;
                String arr[] = data.split(" ");
                //System.out.println(Arrays.toString(arr));
                listB.add(Double.parseDouble(arr[arr.length-1]));
                arr = Arrays.copyOfRange(arr, 0, arr.length-1);
                double [] arrA = Arrays.stream(arr).mapToDouble(Double::valueOf).toArray();
                listA.add(arrA);
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        userMatrixA = listA.toArray(new double[0][]);
        userMatrixB = listB.stream().mapToDouble(Double::intValue).toArray();
        for (int i =0; i < userMatrixA.length; i++) {
            //System.out.println(Arrays.toString(userMatrixA[i]));
        }
        numOfEquations = 4;
        //numOfVariables = 5;
        //double[] x = Gauss(testMatrix, testb);
        for (int i = 1; i<= testMatrix.length; i++) {
            indexVector.add(i);
        }
        double[] x = Gauss(testMatrix, testb);
        System.out.println("\nAnswer:");
        for (int i = 0; i < 4; i++) {
            System.out.println("x"+(i+1) + " = " +x[i]);
        } 
        
    }

    private static int getScaledRatio(int pivotCol) {
        List<Double> scaledRatios = new ArrayList<>();
        if (pivotCol == 0) {
            for (int i = 0; i < numOfEquations; i++) {
                double greatest = 0;
                for (int j = 0; j < numOfVariables-1; j++) {
                    if (j == 0) {
                        greatest = Math.abs(testMatrix[i][j]);
                    }
                    else {
                        if (greatest < Math.abs(testMatrix[i][j])) greatest = Math.abs(testMatrix[i][j]);
                    }
                }
                maxCoefficients.add(greatest);
            }
        }
        //System.out.println(maxCoefficients);
        for (int i = 0; i < maxCoefficients.size(); i++) {
            //if (pivotRows.contains(i)) continue;
            scaledRatios.add(Math.abs(((double) testMatrix[i][pivotCol] / (double) maxCoefficients.get(i))));
        }
        double pivotValue =0;
        boolean flag = false;
        for (int i = 0; i < scaledRatios.size(); i++) {
            if (!pivotRows.contains(i) && flag) {
                pivotValue = scaledRatios.get(0);
            }
            else if ((pivotRows.contains(i)) ) continue;
            else if (pivotValue < scaledRatios.get(i)) pivotValue = scaledRatios.get(i);
        }
        
        int pivotRow = scaledRatios.indexOf(pivotValue);
        //if (pivotRows.size() != 0) {
        //    Collections.swap(scaledRatios, pivotRows.size(), indexVector.indexOf(pivotRow+1));
        //}
        
        //Collections.swap(indexVector, pivotRows.size(), indexVector.indexOf(pivotRow+1));
        //System.out.println(indexVector);
        for (int i = 0; i < pivotRows.size(); i++) {
            System.out.println(scaledRatios);
            if (i == pivotRows.size()-1){
                Collections.swap(scaledRatios, i, indexVector.indexOf(pivotRow+1));
            }
            else {
                //System.out.println(scaledRatios);
                int x = indexVector.get(i) -1;
                //x = pivotRows.indexOf(x);
                //System.out.println(i);
                Collections.swap(scaledRatios, i, x);
            }
        }
        //if (pivotRows.size() > 0) {
        //    Collections.swap(scaledRatios, pivotRows.size(), indexVector.indexOf(pivotRow+1));
        //}
        System.out.println(scaledRatios);
        for (int i = 0; i < pivotRows.size(); i++) {
            scaledRatios.remove(0);
        }
        if (pivotRows.size() != testb.length -1) {
            System.out.println("Scaled Ratio: " + scaledRatios);
            System.out.println("Pivot row: " + (pivotRow + 1));
        }
        System.out.println("Index vector: " + indexVector);
        
        Collections.swap(indexVector, pivotRows.size(), indexVector.indexOf(pivotRow+1));
        //System.out.println("Index vector: " + indexVector);
        return pivotRow; // index base 1
    }

    public static double[] Gauss(double[][] A, double[] b) {
        int n = b.length;
        for (int p = 0; p < n-1; p++) {
            
            for (int i = 0; i< A.length; i++) {
                for (int j=0; j < A[i].length; j++) {
                    System.out.print(" " + A[i][j]);
                }
                System.out.print(" " + b[i]);
                System.out.println();
            }
            System.out.println();

           
            //System.out.println("Index vector: " + indexVector);

            int max = p;
            max = getScaledRatio(p);
            pivotRows.add(max);

            

            // pivot within A and b
            for (int i = 0; i < n; i++) {
                if (pivotRows.contains(i)) continue;
                double multiplier = A[i][p] / A[max][p];
                b[i] -= multiplier * b[max];
                for (int j = p; j < n; j++) {
                    A[i][j] -= multiplier * A[max][j];
                }
            }
        }

        for (int i = 0; i< A.length; i++) {
            for (int j=0; j < A[i].length; j++) {
                System.out.print(" " + A[i][j]);
            }
            System.out.print(" " + b[i]);
            System.out.println();
        }
        System.out.println();
        //System.out.println("test");
        // back substitution
        List<Integer> pivotRows_indexBase1 = pivotRows.stream().map(x -> x+1).collect(Collectors.toList());
        //int nsum = n*(n+1)/2;
        //int remainingPivotRow = nsum - pivotRows_indexBase1.stream().collect(Collectors.summingInt(Integer::intValue));
        //pivotRows_indexBase1.add(remainingPivotRow);
        //pivotRows.add(remainingPivotRow-1);
        //System.out.println(pivotRows_indexBase1);
        System.out.println("Index vector: " + indexVector);
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            int c = indexVector.get(i) - 1; 
            for (int j = i + 1; j < n; j++) {
                sum += A[c][j] * x[j];
            }
            x[i] = (b[c] - sum) / A[c][i];
        }
        return x;
    }
}

