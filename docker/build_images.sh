export JAVA_HOME=$JAVA_17_HOME # assuming everybody is not on java 17 by default

pushd ..
mvn clean package jib:dockerBuild
popd