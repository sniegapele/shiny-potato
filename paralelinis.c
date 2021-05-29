#include <mpi.h>
#include <stdio.h>

#define WORKTAG 1
#define DIETAG 2

void master();
void slave();

int main(int argc, char *argv[])
{
	int myrank;

	MPI_Init(&argc, &argv);   	/* initialize MPI */
	MPI_Comm_rank(MPI_COMM_WORLD,   /* always use this */
			&myrank);      	/* process rank, 0 thru N-1 */

	if (myrank == 0) {
		master();
	} else {
		slave();
	}
	MPI_Finalize();			/* cleanup MPI */
}

void master()
{
	int	ntasks, rank, work=0;
	double       result;
	MPI_Status     status;

	MPI_Comm_size(
		MPI_COMM_WORLD,   /* always use this */
		&ntasks);          /* #processes in application */
/*
* Seed the slaves.
*/
	for (rank = 1; rank < ntasks; ++rank) 
	{
		work ++; /* get_next_work_request: @pakeisti i realu darbo aprasa */
		
		MPI_Send(&work,         /* message buffer */
		1,              /* one data item */
		MPI_INT,        /* data item is an integer */
		rank,           /* destination process rank */
		WORKTAG,        /* user chosen message tag */
		MPI_COMM_WORLD);/* always use this */

		printf("Master(1): Procesui %d issiustas duomuo %d\n", rank, work);

	}

/*
* Receive a result from any slave and dispatch a new work
* request work requests have been exhausted.
*/
 	work ++; /* @pakeisti: get_next_work_request */;
	while (   work < 10 /* @pakeisti: valid new work request */) {
		MPI_Recv(&result,       /* message buffer */
		1,              /* one data item */
		MPI_DOUBLE,     /* of type double real */
		MPI_ANY_SOURCE, /* receive from any sender */
		MPI_ANY_TAG,    /* any type of message */
		MPI_COMM_WORLD, /* always use this */
		&status);       /* received message info */
		
		printf("Master(2): Is proceso %d gautas duomuo %f\n", status.MPI_SOURCE, result);

		MPI_Send(&work, 1, MPI_INT, status.MPI_SOURCE,
			WORKTAG, MPI_COMM_WORLD);

		printf("Master(3): Procesui %d issiustas duomuo %d\n", status.MPI_SOURCE, work);

		work++; /* @pakeisti: get_next_work_request */;
	}
/*
* Receive results for outstanding work requests.
*/
	for (rank = 1; rank < ntasks; ++rank) {
		MPI_Recv(&result, 1, MPI_DOUBLE, MPI_ANY_SOURCE,
		MPI_ANY_TAG, MPI_COMM_WORLD, &status);

		printf("Master(4): Is proceso %d gautas duomuo %f\n", status.MPI_SOURCE, result);
	}
/*
* Tell all the slaves to exit.
*/
	for (rank = 1; rank < ntasks; ++rank) {
		MPI_Send(0, 0, MPI_INT, rank, DIETAG, MPI_COMM_WORLD);
		printf("Master(5): sustabdyti procesa %d\n", rank);
	}
}

void slave()
{
	double              result;
	int                 work;
	MPI_Status          status;
	for (;;) 
	{
		MPI_Recv(&work, 1, MPI_INT, 0, MPI_ANY_TAG,
			MPI_COMM_WORLD, &status);

/*
* Check the tag of the received message.
*/
		if (status.MPI_TAG == DIETAG) {
			printf("Slave(1): atsiusta baigmes zyme\n");
			return;
		}
		else
		{
			printf("Slave(2): atsiustas duomuo %d\n", work);

			result = work * work; /* @pakeisti do the work */
			MPI_Send(&result, 1, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);

			printf("Slave(3): issiustas rezultatas %f\n", result);
		}
	}
}
