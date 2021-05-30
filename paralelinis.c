// Kotryna Slepetyte
// Informatika 3k. 2gr
// Parallel Erathostenes sieve algorithm implementation using MPI library

#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <time.h>

#define WORKTAG    1
#define DIETAG     2

void master(int upperBound);

void slave();

void generatePrimeNumbers(int *numberOfNumbers, int upperBound, int *primes);

int main(int argc, char *argv[]) {
    int upperBound = atoi(argv[1]);

    if (upperBound <= 2) {
        printf("Invalid arguments!");
        return 0;
    }

    int myRank;

    MPI_Init(&argc, &argv);         /* initialize MPI */
    MPI_Comm_rank(MPI_COMM_WORLD,   /* always use this */
                  &myRank);         /* process rank, 0 thru N-1 */

    if (myRank == 0) {
        master(upperBound);
    } else {
        slave();
    }
    MPI_Finalize();                 /* cleanup MPI */
}

void master(int upperBound) {
    double startTime = (double) clock();

    int numberOfPrimes;
    int primesToSqrt[(int) sqrt(upperBound)];
    generatePrimeNumbers(&numberOfPrimes, upperBound, primesToSqrt);

    int ntasks, rank;
    int result = numberOfPrimes;
    MPI_Status status;

    MPI_Comm_size(
            MPI_COMM_WORLD,         /* always use this */
            &ntasks);               /* #processes in application */

    // send info about already calculated primes
    for (rank = 1; rank < ntasks; ++rank) {
        MPI_Send(&numberOfPrimes, 1, MPI_INT, rank, WORKTAG, MPI_COMM_WORLD);
        MPI_Send(primesToSqrt, numberOfPrimes, MPI_INT, rank, WORKTAG, MPI_COMM_WORLD);
    }

    int numberOfTasks = ntasks - 1;

    int numberOfNumbers = (upperBound - (int) sqrt(upperBound)) / numberOfTasks - 1;
    int leftovers = (upperBound - (int) sqrt(upperBound)) % numberOfTasks;

    int startNumber = (int) sqrt(upperBound) + 1;

    int message[2];

    for (rank = 1; rank <= numberOfTasks; rank++) {
        // bounds calculation
        message[0] = startNumber;

        if (leftovers > 0) {
            message[1] = startNumber + numberOfNumbers + 1;
            startNumber = startNumber + numberOfNumbers + 2;
            leftovers--;
        } else {
            message[1] = startNumber + numberOfNumbers;
            startNumber = startNumber + numberOfNumbers + 1;
        }

        if (message[1] > upperBound) {
            message[1] = upperBound;
        }

        // send bounds
        MPI_Send(message, 2, MPI_INT, rank, WORKTAG, MPI_COMM_WORLD);
    }

    // receive results
    for (int i = 0; i < numberOfTasks; i++) {
        int receivedResult;
        MPI_Recv(&receivedResult, 1, MPI_INT, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
        result += receivedResult;
    }

    for (rank = 1; rank < ntasks; ++rank) {
        MPI_Send(0, 0, MPI_INT, rank, DIETAG, MPI_COMM_WORLD);
    }

    printf("Found %d primes in range [2, %d]\n", result, upperBound);
    double endTime = (double) clock();
    printf("Time: %.5f\r\n", (endTime - startTime) / CLOCKS_PER_SEC);
}

void slave() {
    MPI_Status status;

    // receive minimum primes
    int numberOfItems;
    MPI_Recv(&numberOfItems, 1, MPI_INT, 0, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
    int primes[numberOfItems];
    MPI_Recv(&primes, numberOfItems, MPI_INT, 0, MPI_ANY_TAG, MPI_COMM_WORLD, &status);

    int bounds[2];
    MPI_Recv(&bounds, 2, MPI_INT, 0, MPI_ANY_TAG, MPI_COMM_WORLD, &status);

    int numberOfNumberInInterval = bounds[1] - bounds[0] + 1;

    // init current interval
    int myInterval[numberOfNumberInInterval];
    for (int i = 0; i < numberOfNumberInInterval; i++) {
        myInterval[i] = true;
    }

    // calculate primes in interval
    for (int i = 0; i < numberOfItems; i++) {
        int start;
        int coef = (bounds[0] - primes[i] * primes[i]) / primes[i];

        if (coef < 0) {
            start = primes[i] * primes[i];
        } else if (primes[i] * primes[i] + coef * primes[i] < bounds[0]) {
            start = primes[i] * primes[i] + (coef + 1) * primes[i];
        } else {
            start = primes[i] * primes[i] + coef * primes[i];
        }

        for (int j = start; j <= bounds[1]; j += primes[i]) {
            myInterval[j - bounds[0]] = false;
        }
    }

    // count calculated prime numbers in given interval
    int count = 0;
    for (int i = 0; i < numberOfNumberInInterval; i++) {
        if (myInterval[i]) {
            count++;
        }
    }

    MPI_Send(&count, 1, MPI_INT, 0, WORKTAG, MPI_COMM_WORLD);

    // receive end tag
    int randomPlace;
    MPI_Recv(&randomPlace, 1, MPI_INT, 0, DIETAG, MPI_COMM_WORLD, &status);
}

void generatePrimeNumbers(int *numberOfNumbers, int upperBound, int *primes) {
    int sqrtFromUpperBound = (int) sqrt(upperBound);

    // init array
    bool interval[sqrtFromUpperBound - 1];
    for (int i = 0; i < sqrtFromUpperBound - 1; i++) {
        interval[i] = true;
    }

    // calculate primes until sqrt(upperBound) reached
    int sqrtOfSqrt = (int) sqrt(sqrtFromUpperBound);
    for (int i = 2; i <= sqrtOfSqrt; i++) {
        if (interval[i - 2]) {
            for (int j = i * i; j <= sqrtFromUpperBound; j += i) {
                interval[j - 2] = false;
            }
        }
    }

    // count prime numbers
    int number = 0;
    for (int i = 0; i < sqrtFromUpperBound - 1; i++) {
        if (interval[i]) {
            number++;
        }
    }

    // pass number of prime numbers in that interval for master
    *numberOfNumbers = number;

    int index = 0;
    for (int i = 0; i < sqrtFromUpperBound - 1; i++) {
        if (interval[i]) {
            primes[index] = i + 2;
            index++;
        }
    }
}
