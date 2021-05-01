#!/bin/sh
#SBATCH -p short 
#SBATCH -N1 
#SBATCH -c12 
#SBATCH -C beta 
java Main 
