import java.io.*;

public class Main {
    static int N, M, n, m;
    static int[][] mat, c, res;
    static final String filename_input = "input_matrix.txt", filename_input_c = "input_c.txt",
            filename_output = "output1.txt", iterative_output = "output.txt";
    static int p;
    public static void main(String[] args) throws IOException {

        p = Integer.parseInt(args[0]);

        N = M = 1000;
        n = m = 5;
        mat = RandomArrayGenerator.generateRandomArray(N, M);
        printOutput(filename_input, mat);

        c = RandomArrayGenerator.generateRandomArray(n, m);
        printOutput(filename_input_c, c);

        //do the iterative convolution
//        long startTime = System.currentTimeMillis();
        computeConvolution();
//        long endTime = System.currentTimeMillis();
        printOutput(iterative_output, res);

        long startTime = System.currentTimeMillis();
        convolutionMultiThreadRows();
        long endTime = System.currentTimeMillis();
        try{
            printOutput(filename_output, res);
        }catch(IOException e){
            e.printStackTrace();
        }

        if(!FileComparator.areFilesIdentical("output.txt", "output1.txt")){
            throw new RuntimeException("Output is not the same as the iterative one");
        }

        System.out.println((double)(endTime - startTime)/1E6);

    }

    static int getValue(int i, int j){
        if (i < 0) i = 0;
        if (i >= N) i = N - 1;

        if (j < 0) j = 0;
        if (j >= M) j = M - 1;

        return mat[i][j];
    }

    public static void computeConvolution(){
        res = new int[N][M];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                int sum = 0;
                // Iterate over the kernel
                for (int ki = 0; ki < n; ki++) {
                    for (int kj = 0; kj < m; kj++) {
                        // Calculate the corresponding element in the main matrix
                        int row = i + ki - n/2;
                        int col = j + kj - m/2;
                        sum += getValue(row, col) * c[ki][kj];
                    }
                }
                // Store the convolution result
                res[i][j] = sum;
            }
        }
    }

    public static void convolutionMultiThreadRows(){
        res = new int[N][M];
        Thread[] threads = new Thread[p];
        int rowsPerThread = N / p;
        int remainingRows = N % p;
        int startRow = 0;
        for (int i = 0; i < p; i++) {
            int endRow = startRow + rowsPerThread + (i < remainingRows ? 1 : 0);
            threads[i] = new Thread(new ConvolutionTaskRows(mat, c, res, startRow, endRow));
            threads[i].start();
            startRow = endRow;
        }
        for (int i = 0; i < p; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void convolutionMultiThreadColumns(){
        res = new int[N][M];
        Thread[] threads = new Thread[p];
        int colsPerThread = M / p;
        int remainingCols = M % p;
        int startCol = 0;
        for (int i = 0; i < p; i++) {
            int endCol = startCol + colsPerThread + (i < remainingCols ? 1 : 0);
            threads[i] = new Thread(new ConvolutionTaskColumns(mat, c, res, startCol, endCol));
            threads[i].start();
            startCol = endCol;
        }
        for (int i = 0; i < p; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printOutput(String filename, int[][] matrix) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (int[] row : matrix){
            for (int elem : row){
                writer.write(elem + " ");
            }
            writer.newLine();
        }
        writer.close();
    }
}