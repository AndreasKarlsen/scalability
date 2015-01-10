Performance test for "Investigation of trending concurrency models by comparison of performance and characteristics: Software Transactional Memory, Actor Model, and Threads & Locks.

There is bash scripts for running the performance tests used in the report.
There are two folders: Manhatten and Pearson. Each folder entails the files needed for running the performance test.

In Pearson the files:
- preliminary-test.sh
- test-a.sh
- test-b.sh

In Manhatten the files:
- test-c.sh
- test-d.sh

Each of the files will output the result in seperate files, in a folder named after the implementation. The files is named after the parameters used, e.g. "V2000M1C5I100.td" is a test ran with a vector size of 2000, 1 mapper and 100 iterations.

The jars can only be invoked manually, by using the command:
"java -jar j v m i" where j is the jar name, v is the vector size, m is the amount of mappers and i is the number if iterations. 
