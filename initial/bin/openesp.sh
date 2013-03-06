#!/bin/bash

#
# This shell script takes care of starting and stopping OpenESP
#
# chkconfig: - 80 20
#

### BEGIN INIT INFO
# Required-Start: $network $syslog
# Required-Stop: $network $syslog
# Default-Start:
# Default-Stop:
# Description: Release implementation for Servlet 2.5 and JSP 2.1
# Short-Description: start and stop OpenESP
### END INIT INFO



## Source function library.
#. /etc/rc.d/init.d/functions

#set OPENESP_HOME
#OPENESP_HOME=

export CATALINA_HOME=$OPENESP_HOME/tomcat
export CATALINA_BASE=$CATALINA_HOME
export JRE_HOME=$JAVA_HOME
export JAVA_HOME=$JAVA_HOME
export CATALINA_PID=$CATALINA_BASE/logs/openesp.pid

#set JAVA_OPTS
#export JAVA_OPTS=

export PATH=$JAVA_HOME/bin:$PATH
TOMCAT_HOME=$CATALINA_HOME
SHUTDOWN_WAIT=20


tomcat_pid() {
  echo `ps aux | grep org.apache.catalina.startup.Bootstrap | grep -v grep | awk '{ print $2 }'`
}


start() {
  mkdir -p $CATALINA_BASE/logs
  pid=$(tomcat_pid)

  if [ -n "$pid" ] 
  then
    echo "OpenESP is already running (pid: $pid)"
  else
    # Start tomcat

    echo "Starting OpenESP"

    cd $TOMCAT_HOME/bin
    $TOMCAT_HOME/bin/startup.sh
  fi
  return 0
}


stop() {
  pid=$(tomcat_pid)

  if [ -n "$pid" ]
    then

    echo "Stopping OpenESP"
    cd $TOMCAT_HOME/bin
    $TOMCAT_HOME/bin/shutdown.sh

    let kwait=$SHUTDOWN_WAIT

    count=0;

    until [ `ps -p $pid | grep -c $pid` = '0' ] || [ $count -gt $kwait ]
    do
      echo -n -e "\nwaiting for processes to exit";
      sleep 1
      let count=$count+1;
    done

    if [ $count -gt $kwait ]; then
      echo -n -e "\nkilling processes which didn't stop after $SHUTDOWN_WAIT seconds"
      kill -9 $pid
    fi
  else
    echo "OpenESP is not running"
  fi

  return 0
}


case $1 in
  start)
    start
  ;;

  stop)
    stop
  ;;

  restart)
    stop
    start
  ;;

  status)
    pid=$(tomcat_pid)
    if [ -n "$pid" ]
    then
      echo "OpenESP is running with pid: $pid"
    else
      echo "OpenESP is not running"
    fi
  ;;
esac

exit 0
