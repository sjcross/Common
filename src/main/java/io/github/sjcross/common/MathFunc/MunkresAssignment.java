package io.github.sjcross.common.MathFunc;

import java.text.DecimalFormat;

public class MunkresAssignment {
    private float[][] costs;
    private int l;

    public MunkresAssignment(float[][] costs) {
        this.costs = costs;
        this.l = costs.length;
    }

    public static void main(String[] args) {
        float[][] costs = new float[][]{{10f,19,8f,15f,19f},{10f,18f,7f,17f,19f},{13f,16f,9f,14f,19f},{12f,19f,8f,18f,19f},{14,17,10,19,19f}};

        new MunkresAssignment(costs).calculate(costs);

    }

    public float[] calculate(float[][] costs) {
        System.out.println("Initial costs");
        showCosts();
        System.out.println(" ");

        // Removing the minimum value from each row
        removeRowMin();
        System.out.println("Removed row mins");
        showCosts();
        System.out.println(" ");

        // Ensuring all rows contain a zero
        ensureColumnZeroes();
        System.out.println("Remove column mins");
        showCosts();
        System.out.println(" ");

        // Resolving unassigned regions
        resolveUnassigned();

        return null;

    }

    private void removeRowMin() {
        for (int i=0;i<l;i++) {
            float min = Float.MAX_VALUE;

            for (int j=0;j<l;j++) {
                min = Math.min(costs[i][j],min);
            }

            for (int j=0;j<l;j++) {
                costs[i][j] = costs[i][j] - min;
            }
        }
    }

    private void ensureColumnZeroes() {
        for (int j=0;j<l;j++) {
            boolean hasZero = false;
            // Getting the column minimum value
            float min = Float.MAX_VALUE;
            for (int i=0;i<l;i++) {
                // If this column contains a zero skip to the next column
                if (costs[i][j] == 0) {
                    hasZero = true;
                    break;
                }

                min = Math.min(costs[i][j],min);
            }

            if (hasZero) continue;

            // Subtracting the column minimum
            for (int i=0;i<l;i++) {
                costs[i][j] = costs[i][j] - min;
            }
        }
    }

    /**
     * Covers all rows and oolumns containing a zero, then adds the minimum uncovered value to all covered values.
     * @return true when the number of covering lines equals the number of rows
     */
    private void resolveUnassigned() {
        int lineCount = 0;

        while (lineCount < l) {
            calculateAssignment();
            System.out.println("Costs after the first assignment");
            showCosts();
            System.out.println(" ");

            boolean[][] coverage = calculateCoverage();
            System.out.println("Calculated coverage");
            showCoverage(coverage);
            System.out.println(" ");

            lineCount = oountCoverageLines(coverage);
            System.out.println(lineCount + " lines");

            if (lineCount < l) {
                removeUncoveredMin(coverage);
                System.out.println("Costs after uncovered min removed");
                showCosts();
                System.out.println(" ");
            }
        }

        System.out.println("Final costs");
        showCosts();
        System.out.println(" ");

    }

    /* Performs the first assignment.  This sets a maximum of one Float.NaN per row.
     */
    private void calculateAssignment() {
        boolean[][] assignment = new boolean[l][2];

        // Covering zeroes from rows with a single assignment
        for (int i=0;i<l;i++) {
            int count = 0;
            int col = 0;
            for (int j=0;j<l;j++) {
                // Skipping considering any columns that have previously been covered
                if (assignment[i][0] | assignment[j][1]) continue;

                // If this is a zero-cost position, add one to the counter and make a note of the column
                if (costs[i][j] == 0) {
                    count++;
                    col = j;
                }
            }

            // If there is only one zero in this row, set it as an assignment
            if (count == 1) {
                costs[i][col] = Float.NaN;
                assignment[i][0] = true;
                assignment[col][1] = true;
            }
        }

        // Repeating for the remaining unassigned rows/columns, but here we can take any that have a zero
        // Covering zeroes from rows with a single assignment
        for (int i=0;i<l;i++) {
            for (int j=0;j<l;j++) {
                // Skipping considering any columns that have previously been covered
                if (assignment[i][0] | assignment[j][1]) continue;

                // If this is a zero-cost position, add one to the counter and make a note of the column
                if (costs[i][j] == 0) {
                    costs[i][j] = Float.NaN;
                    assignment[i][0] = true;
                    assignment[j][1] = true;
                }
            }
        }
    }

    /**
     * Determines the indices of rows and columns containing Float.Nan (previously-assigned positions).
     * @return First column contains indices of rows and second column contains indices of columns.  True when row/
     * column contains a Float.NaN.
     */
    private boolean[][] calculateCoverage() {
        int[][] coverageCount = new int[l][2];

        // Marking all rows without an assignment
        for (int i = 0; i < l; i++) {
            boolean hasAssignment = false;
            for (int j = 0; j < l; j++) {
                if (Float.isNaN(costs[i][j])) {
                    hasAssignment = true;
                }
            }

            if (!hasAssignment) {
                coverageCount[i][0] = 1;
            }
        }

        int iteration = 1;
        boolean coverageAdded = true;
        while (coverageAdded) {
            coverageAdded = false;

            // Covering all unmarked columns containing a Float.Nan in a newly-marked row
            for (int j = 0; j < l; j++) {
                // If the column has previously been covered, skip it
                if (coverageCount[j][1] != 0) continue;

                // Iterating over all rows in this column.  Give it coverage if it has a Float.NaN and the row was assigned
                // in the current iteration.
                for (int i = 0; i < l; i++) {
                    if ((Float.isNaN(costs[i][j]) || costs[i][j] ==0) && coverageCount[i][0] == iteration) {
                        coverageCount[j][1] = iteration;
                        coverageAdded = true;
                    }
                }
            }

            // Covering all unmarked rows containing a Float.NaN in a newly-marked column
            for (int i = 0; i < l; i++) {
                // If the row has previously been covered, skip it
                if (coverageCount[i][0] != 0) continue;

                // Iterating over all columns in this row.  Give it coverage if it has a Float.NaN and the column was
                // assigned in the current iteration.
                for (int j = 0; j < l; j++) {
                    if (Float.isNaN(costs[i][j]) && coverageCount[j][1] == iteration) {
                        coverageCount[i][0] = iteration;
                        coverageAdded = true;
                    }
                }
            }

            // Incrementing the iteration counter
            iteration++;

        }

        // Creating coverage array, where elements are true if that row/column has a line over it
        boolean[][] coverage = new boolean[l][2];
        for (int i=0;i<l;i++) {
            coverage[i][0] = (coverageCount[i][0] == 0);
            coverage[i][1] = (coverageCount[i][1] != 0);
        }

        return coverage;

    }

    private int oountCoverageLines(boolean[][] coverage) {
        int count = 0;
        for (int i=0;i<l;i++) {
            if (coverage[i][0]) count++;
            if (coverage[i][1]) count++;
        }

        return count;
    }

    private void removeUncoveredMin(boolean[][] coverage) {
        // Calculating the minimum value in the uncovered elements
        float min = Float.MAX_VALUE;
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                if (!coverage[i][0] &! coverage[j][1]) min = Math.min(costs[i][j],min);
            }
        }

        // Converting all NaNs back to zero
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                if (Float.isNaN(costs[i][j])) costs[i][j] = 0;
            }
        }

        // Subtracting the minimum value from the uncovered elements
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                if (!coverage[i][0] &! coverage[j][1]) {
                    costs[i][j] = costs[i][j] - min;
                } else if (coverage[i][0] && coverage[j][1]){
                    costs[i][j] = costs[i][j] + min;
                }
            }
        }
    }

    private void showCosts() {
        DecimalFormat df = new DecimalFormat("0");
        for (int i=0;i<l;i++) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int j=0;j<l;j++) {
                if (j == l - 1) {
                    stringBuilder.append(df.format(costs[i][j]));
                } else {
                    stringBuilder.append(df.format(costs[i][j])+", ");
                }
            }
            System.out.println(stringBuilder.toString());
        }
    }

    private void showCoverage(boolean[][] coverage) {
        for (int i=0;i<l;i++) {
            System.out.println(coverage[i][0]+", "+coverage[i][1]);
        }
    }
}
