# sbt clean package
#!/bin/bash

# determine the script directory as suggested at
#  http://stackoverflow.com/questions/59895/can-a-bash-script-tell-what-directory-its-stored-in

SCRIPT_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

/home/dloshkarev/distrib/spark-2.4.0-bin-hadoop2.7/bin/spark-submit \
  --class "ru.Main" \
  /home/dloshkarev/workspace/TestSpark/target/scala-2.11/testspark_2.11-0.1.jar \
