cd ..
mvn clean:clean
mvn -Dmaven.test.skip=true package 
#mvn -Dmaven.test.skip=true assembly:assembly
~