# vector-size nr-mappers nr-iterations

stm="STM.jar"
tl="TL.jar"
actor="ACTOR.jar"

function test {
  for i in {1..5}
  do
    for j in {1..100000}
    do
    if (( $j % 10000 == 0 )) || (( $j == 1 ))
    then
      java -jar $1.jar 10000 7 $j
    fi
    done
  done
}
