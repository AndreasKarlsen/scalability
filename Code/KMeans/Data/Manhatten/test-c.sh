# vector-size nr-mappers nr-iterations

stm="STM.jar"
tl="TL.jar"
actor="ACTOR.jar"

function test {
  for i in {1..5}
  do
    for j in {1..10}
    do
      java -jar $1.jar 10000 $j 100000
    done
  done
}
