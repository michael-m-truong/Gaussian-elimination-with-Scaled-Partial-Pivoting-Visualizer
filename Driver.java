import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Driver {

    private static int numOfEquations;
    private static int numOfVariables;
    private static double[][] testMatrixA = {
        {2,3,0},
        {-1,2,-1},
        {3,0,2}
    }; 
    private static double [] testMatrixB = {8,0,9};
    private static double[][] userMatrixA;
    private static double[] userMatrixB;
    private static List<Integer> pivotRows = new ArrayList<>();
    private static List<Integer> indexVector = new ArrayList<>();
    private static List<Double> maxCoefficients = new ArrayList<>();
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to Gaussian elimination w/ partial scaling visualizer!");
        System.out.println("1. Select from file");
        System.out.println("2. Manually enter matrix");
        System.out.println("3. Use test case matrix");
        System.out.print("Enter number choice: ");
        int numberChoice = Integer.parseInt(input.nextLine());

        if (numberChoice == 1) {                         // user input file
            List<double[]> listA = new ArrayList<>(); 
            List<Double> listB = new ArrayList<>();
            System.out.print("Enter text file name with .txt : ");
            String userInputFile = input.nextLine();
            try {
                File myObj = new File(userInputFile);
                Scanner myReader = new Scanner(myObj);
                
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    if (data.replaceAll("\\s", "") == "") break;
                    String arr[] = data.split(" ");
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
            numOfEquations = userMatrixA.length;
            numOfVariables = numOfEquations;
        }
        if (numberChoice == 2) {                                     // user input
            System.out.print("Enter number of equations: ");
            numOfEquations = Integer.parseInt(input.nextLine());
            numOfVariables = numOfEquations; // num of variables to solve for equals num of equations 
            userMatrixA = new double[numOfEquations][numOfEquations];
            userMatrixB = new double [numOfEquations];
            for (int i = 0; i < numOfEquations; i++) {
                for (int j = 0; j < numOfEquations; j++) {
                    System.out.print("Enter value for a" + (i+1) + (j+1) + ": ");
                    int val = Integer.parseInt(input.nextLine());
                    userMatrixA[i][j] = val;
                }
                System.out.print("Enter value for b" + (i+1) + ": ");
                int val = Integer.parseInt(input.nextLine());
                userMatrixB[i] = val;
            }
        }
        if (numberChoice == 3) {                // test case
            userMatrixA = testMatrixA;
            userMatrixB = testMatrixB;
            numOfEquations = userMatrixA.length;
            numOfVariables = numOfEquations;
        }
        for (int i = 1; i<= userMatrixA.length; i++) {
            indexVector.add(i);
        }
        double[] x = Gauss(userMatrixA, userMatrixB);
        System.out.println("\nAnswer:");
        for (int i = 0; i < numOfVariables; i++) {
            System.out.print("x"+(i+1) + " = ");
            System.out.printf(" %.5f \n", x[i]);
        } 
        input.close();
    }

    private static int getScaledRatio(int pivotCol, double[][] matrix) {
        List<Double> scaledRatios = new ArrayList<>();
        if (pivotCol == 0) {
            for (int i = 0; i < numOfEquations; i++) {
                double greatest = 0;
                for (int j = 0; j < numOfVariables; j++) {
                    if (j == 0) {
                        greatest = Math.abs(matrix[i][j]);
                    }
                    else {
                        if (greatest < Math.abs(matrix[i][j])) greatest = Math.abs(matrix[i][j]);
                    }
                }
                maxCoefficients.add(greatest);
            }
        }
        for (int i = 0; i < maxCoefficients.size(); i++) {
            scaledRatios.add(Math.abs(((double) matrix[i][pivotCol] / (double) maxCoefficients.get(i))));
        }
        double pivotValue =0;
        for (int i = 0; i < scaledRatios.size(); i++) {
            if ((pivotRows.contains(i)) ) continue;
            else if (pivotValue < scaledRatios.get(i)) pivotValue = scaledRatios.get(i);
        }
        
        int pivotRow = scaledRatios.indexOf(pivotValue);

        for (int i = 0; i < pivotRows.size(); i++) {
            if (i == pivotRows.size()-1){
                Collections.swap(scaledRatios, i, indexVector.indexOf(pivotRow+1));
            }
            else {
                int x = indexVector.get(i) -1;
                Collections.swap(scaledRatios, i, x);
            }
        }

        for (int i = 0; i < pivotRows.size(); i++) {
            scaledRatios.remove(0);
        }
        if (pivotRows.size() != matrix.length -1) {
            System.out.println("Scaled Ratio: " + scaledRatios);
            System.out.println("Pivot row: " + (pivotRow + 1));
        }
        System.out.println("Previous index vector: " +  indexVector);
        
        Collections.swap(indexVector, pivotRows.size(), indexVector.indexOf(pivotRow+1));
        System.out.println("Updated index vector:  " + indexVector);
        return pivotRow;
    }

    public static double[] Gauss(double[][] A, double[] b) {
        int n = b.length;
        for (int p = 0; p < n-1; p++) {
            
            for (int i = 0; i< A.length; i++) {
                for (int j=0; j < A[i].length; j++) {
                    if (A[i][j] >= 0)
                        System.out.printf("   %-9.9s", A[i][j]);
                    else
                        System.out.printf("  -%-9.9s", -A[i][j]);
                }
                if (b[i] >= 0)
                    System.out.printf(" |  %-9.9s", b[i]);
                else 
                    System.out.printf(" | -%-9.9s", -b[i]); 
                System.out.println();
            }
            System.out.println();

           

            int max = p;
            max = getScaledRatio(p, A);
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

        // print resulting matrix
        for (int i = 0; i< A.length; i++) {
            for (int j=0; j < A[i].length; j++) {
                if (A[i][j] >= 0)
                    System.out.printf("   %-9.9s", A[i][j]);
                else
                    System.out.printf("  -%-9.9s", -A[i][j]);
            }
            if (b[i] >= 0)
                System.out.printf(" |  %-9.9s", b[i]);
            else 
                System.out.printf(" | -%-9.9s", -b[i]); 
            System.out.println();
        }
        System.out.println();

        // back substitution
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

