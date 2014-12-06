DIR="$(cd "$(dirname "$0")" && pwd)"
MAVEN_HOME=/opt/softwares/maven/maven-3.0.2
JAVA_HOME=/usr/lib/jvm/jdk1.6.0_20
ANT_HOME=/opt/softwares/apache-ant-1.8.2
export PATH=$PATH:$MAVEN_HOME/bin:$ANT_HOME/bin

#cd ..
#cd jtbdivelogbook-application-archiver
#mvn -o clean
#mvn eclipse:eclipse
#mvn -o install

#cd ..
#cd jtbdivelogbook-website
#mvn -o clean
#mvn eclipse:eclipse
#mvn -o install

cd ..
cd jtbdivelogbook-global-archiver
cd ant
ant
