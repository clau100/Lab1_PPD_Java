import java.util.Random;

public class RandomArrayGenerator {
    public static int[][] generateRandomArray(int rows, int cols) {
        int[][] array = new int[rows][cols];
        Random random = new Random();

        // Fill the array with random numbers between 1 and 10
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j] = random.nextInt(10) + 1; // Random number from 1 to 10
            }
        }

        return array;
    }
}
