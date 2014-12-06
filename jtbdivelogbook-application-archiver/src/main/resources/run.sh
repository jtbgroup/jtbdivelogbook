@echo off
DIR="$(cd "$(dirname "$0")" && pwd)"
LIB_CLASSPATH=
LIB_PATH=
LIB_DIR=$DIR/lib
BIN_DIR=$DIR/bin
MACHINE_TYPE=`uname -m`

echo "==============================================================================="
echo ""
echo "	Jt'B Dive Logbook"
echo "	Developped by Jt'B team"
echo ""
echo "  Running under system $MACHINE_TYPE"
echo "	LIB_DIR: $LIB_DIR"
echo "	BIN_DIR: $BIN_DIR"
echo ""
echo "==============================================================================="
echo ""

java -version

#set the path
export PATH=$PATH:$BIN_DIR
echo "PATH is : $PATH"
echo ""

#set the classpath
for file in $LIB_DIR/*.jar
 do
  LIB_CLASSPATH=$LIB_CLASSPATH:$file
 done
export CLASSPATH=$CLASSPATH:$LIB_CLASSPATH
echo "CLASSPATH is : $CLASSPATH"
 
BIN_DIR=$BIN_DIR/linux/$MACHINE_TYPE

java -Djava.library.path=$BIN_DIR ${mainClass} &