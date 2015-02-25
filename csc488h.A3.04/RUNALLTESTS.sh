#! /bin/sh
#  Location of directory containing  dist/compiler488.jar
WHERE=.
#  Compiler reads one source file from command line argument
#  Output to standard output 
python3 ./testrunner.py $WHERE/dist/compiler488.jar
exit 0
