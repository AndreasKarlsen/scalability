#!/usr/bin/env bash

# vector-size nr-mappers nr-iterations

stm="STM.jar"
tl="TL.jar"
actor="ACTOR.jar"

function test {
  for i in {1..5}
  do
    for j in {1000000..10000000}
    do
    if (( $j % 1000000 == 0 ))
    then
      java -Xmx8g -Xms8g -jar $1.jar $j 7 100
    fi
    done
  done
}

test stm
test tl
test actor
	
10.000.000
