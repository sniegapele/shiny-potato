// Kotryna Slepetyte
// Informatika 3k. 2gr
// Linear Erathostenes sieve algorithm implementation

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <time.h>

void initArray(bool *array, long numberOfElements) {
    for (long i = 0; i < numberOfElements; i++) {
        array[i] = true;
    }
}

void analysePrimes(bool *array, long upperBound) {
    for (long i = 2; i < sqrt(upperBound); i++) {
        if (array[i - 2]) {
            for (long j = i * i; j <= upperBound; j += i) {
                array[j - 2] = false;
            }
        }
    }
}

long countPrimeNumbers(bool *array, long numberOfElements) {
    long count = 0;
    for (long i = 0; i < numberOfElements; i++) {
        if (array[i]) {
            count++;
        }
    }
    return count;
}

void printResults(long primeNumbers, double time) {
    printf("Prime numbers found: %ld\n", primeNumbers);
    printf("Time elapsed: %.5f\n", time);
}

int main(int argc, char **argv) {
    if (argc != 2) {
        printf("Invalid number of parameters!");
        return -1;
    }
    if (atol(argv[1]) <= 2) {
        printf("Invalid upper bound!");
        return -1;
    }

    double startTime = (double) clock();

    long upperBound = atol(argv[1]);

    bool primeNumbers[upperBound - 1];

    initArray(primeNumbers, upperBound - 1);

    analysePrimes(primeNumbers, upperBound);

    long count = countPrimeNumbers(primeNumbers, upperBound - 1);

    double endTime = (double) clock();

    printResults(count, (endTime - startTime) / CLOCKS_PER_SEC);

    return 0;
}