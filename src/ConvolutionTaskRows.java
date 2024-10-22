public class ConvolutionTaskRows implements Runnable{
    private final int[][] mat;
    private final int[][] c;
    private final int[][] output;
    private final int startRow;
    private final int endRow;
    private final int cRows;
    private final int cCols;
    private final int N;
    private final int M;

    public ConvolutionTaskRows(int[][] mat, int[][] c, int[][] output, int startRow, int endRow) {
        this.mat = mat;
        this.c = c;
        this.output = output;
        this.startRow = startRow;
        this.endRow = endRow;
        this.cRows = c.length;
        this.cCols = c[0].length;
        this.N = mat.length;
        this.M = mat[0].length;
    }

    int getValue(int i, int j){
        if (i < 0) i = 0;
        if (i >= N) i = N - 1;

        if (j < 0) j = 0;
        if (j >= M) j = M - 1;

        return mat[i][j];
    }

    @Override
    public void run() {
        int matRows = mat.length;
        int matCols = mat[0].length;

        // Apply convolution for the assigned rows
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < matCols; j++) {
                // Convolve at (i, j) in the matrix
                int sum = 0;
                for (int m = 0; m < cRows; m++) {
                    for (int n = 0; n < cCols; n++) {
                        int rowIndex = i + m - cRows / 2;
                        int colIndex = j + n - cCols / 2;
                        sum += getValue(rowIndex, colIndex) * c[m][n];
                    }
                }
                output[i][j] = sum;
            }
        }
    }
}
