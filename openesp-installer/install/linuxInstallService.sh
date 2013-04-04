cp -f $INSTALL_PATH/.temp/install/java_opts.properties $INSTALL_PATH/bin
$INSTALL_PATH/bin/openespctl daemon -n ${openesp.service.name} -o $INSTALL_PATH -j $JAVA_HOME install
start=${solr.start.service}
if $start ; then
	$INSTALL_PATH/bin/openespctl daemon -n ${openesp.service.name} start
fi


