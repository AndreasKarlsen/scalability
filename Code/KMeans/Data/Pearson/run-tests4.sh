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
      java -Xmx8g -Xms8g -jar $1.jar 2000000 $j 100
    done
  done
}

test stm
test tl
test actor
