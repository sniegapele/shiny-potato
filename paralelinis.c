#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <time.h>
#include <mpi.h>

void initArray(bool *array, int numberOfElements) {
    for (int i = 0; i < numberOfElements; i++) {
        array[i] = true;
    }
}

int countPrimeNumbers(bool *array, int numberOfElements) {
    int count = 0;
    for (int i = 0; i < numberOfElements; i++) {
        if (array[i]) {
            count++;
        }
    }
    return count;
}

void printResults(int primeNumbers, double time) {
    printf("Prime numbers found: %d\n", primeNumbers);
    printf("Time elapsed: %.5f\n", time);
}

int main(int argc, char **argv) {
    if (argc != 2) {
        printf("Invalid number of parameters!");
        return -1;
    }
    if (atoi(argv[1]) <= 2) {
        printf("Invalid upper bound!");
        return -1;
    }

    // program init
    MPI_Init(&argc, &argv);
//    int busy = atoi(argv[2]);
//    printf("%d",busy);

    // caller rank: master = 0
    int rank = 0;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    // number of participants in communication
    int participants;
    MPI_Comm_size(MPI_COMM_WORLD, &participants);

    MPI_Status status;

    if (rank == 0) {
        printf("Total number of procs: %d\n", participants);
    }

    double startTime = (double) clock();

    int upperBound = atoi(argv[1]);

    bool primeNumbers[upperBound - 1];
    initArray(primeNumbers, upperBound - 1);

    // seeder/master caller
    if (rank == 0) {
        int number = 2;
        int response;

//        for (int i = 1; i < participants; i++) {
//            MPI_Send(&number, 1, MPI_INT, i, 1, MPI_COMM_WORLD);
//            do {
//                number++;
//            } while (!primeNumbers[number - 2]);
//        }

        // darbu siuntimas
        while (number <= sqrt(upperBound)) {
            printf("Cia\n");
            MPI_Recv(&response, 1, MPI_INT, MPI_ANY_SOURCE, 1, MPI_COMM_WORLD, &status);
            printf("Ir dar cia\n");
            MPI_Send(&number, 1, MPI_INT, status.MPI_SOURCE, 1, MPI_COMM_WORLD);
            do {
                number++;
            } while (!primeNumbers[number - 2]);
            number++;
        }

        int endFlag = -1;
        // siunciamos darbo pabaigos zymes procesams
        for (int i = 1; i < participants; i++) {
            MPI_Recv(&response, 1, MPI_INT, MPI_ANY_SOURCE, 1, MPI_COMM_WORLD, &status);
            MPI_Send(&endFlag, 1, MPI_INT, status.MPI_SOURCE, 1, MPI_COMM_WORLD);
        }

    } else {
        int result = 0;
        int received = 0;

        while (received >= 0) {
            printf("dar");
            MPI_Recv(&received, 1, MPI_INT, 0, 1, MPI_COMM_WORLD, &status);
            MPI_Send(&result, 1, MPI_INT, 0, 1, MPI_COMM_WORLD);

//            MPI_Recv(&received, 1, MPI_INT, 0, 1, MPI_COMM_WORLD, &status);

            for (int i = received * received; i <= upperBound; i += received) {
                if (primeNumbers[i - 2]) {
                    primeNumbers[i - 2] = false;
                }
            }
        }
    }

    int result = countPrimeNumbers(primeNumbers, upperBound - 1);

    double endTime = (double) clock();

    MPI_Finalize();

    printResults(result, (endTime - startTime) / CLOCKS_PER_SEC);

    return 0;
}

