#!/bin/bash

#javac J2V.java && 
#java J2V < ./test/inputminijavafiles/Factorial.java && 
#echo "----------------" && 
#java J2V < ./test/inputminijavafiles/Factorial01.java

echo ///////////////////////
javac J2V.java &&
java J2V < ./utest/Factorial$1.java > ./utest/.temp &&
java -jar ./test/vapor.jar run ./utest/.temp

echo ////////////////////////

cat ./utest/.temp

rm ./utest/.temp
