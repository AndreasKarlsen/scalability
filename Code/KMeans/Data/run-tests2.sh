#!/usr/bin/env bash

# vector-size nr-mappers nr-iterations

stm="STM.jar"
tl="TL.jar"
actor="ACTOR.jar"

#Scale iterations
function test {
  for i in {1..5}
  do
    for j in {1..10}
    do
      java -jar $1.jar 10000 $j 100000
    done
    for j in {1..100000}
    do
    if (( $j % 10000 == 0 )) || (( $j == 1 ))
    then
      java -jar $1.jar 10000 7 $j
    fi
    done
  done
}

test stm
test tl
test actor
	
