<%--
Root admin application of OpenESP
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>OpenESP</title>
    <style type="text/css" media="screen">
    body {
      font-family: Arial, Helvetica, sans-serif;
    }
    </style>
	<link href="tabcontent/tabcontent.css" rel="stylesheet" type="text/css" />
	<script src="tabcontent/tabcontent.js" type="text/javascript"></script>
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<script>
  	$.ajax({
      url: '/mcf/',
      type: 'HEAD',
      success: function(transport) {
          document.getElementById("mcfLink").href='/mcf/'; 
          document.getElementById("mcfText").innerHTML=''; 
          return false;
      }
    });
	</script>
    </head>
  <body>
  
  <p/>
  <p/>

	<ul class="tabs">
		<li><a href="#welcome">OpenESP</a></li>
		<li><a href="#spm">Monitoring</a></li>
		<li><a href="#searchanalytics">Search Analytics</a></li>
		<li><a href="#solrmeter">SolrMeter</a></li>
		<li><a href="#vifun">Vifun</a></li>
		<li><a href="#zk">ZooKeeper</a></li>
		<a href="#welcome"><img class="logo" src="OpenESP-Logo.gif" alt="OpenESP logo" height="25"></a>
	</ul>
  
	<div class="tabcontents">
		<div id="welcome">
		  <h1>Welcome to OpenESP</h1>
		  This is the OpenESP administration page, the starting point for managing your search platform.
		  <p><a href="/solr/#/"><b>Solr</b></a></p>
		  <p><a id="mcfLink" href='javascript: void(0)'><b>ManifoldCF</b></a> <span id="mcfText">(disabled, run <span class="inlinecode">bin/openespctl enable mcf</span> to enable)</span></p>
			For help or more info, please see the <a href="OpenESP admin guide.pdf" target="_blank">Administration Guide</a>, <a href="http://openesp.org/" target="_blank">openesp.org</a> and <a href="https://github.com/openesp/openesp/" target="_blank">GitHub page</a>
		</div>
	
		<div id="spm">
			<img src="sematext-spm.png" align="right" width="400">
			<h1>Sematext SPM monitoring</h1>
			<p>General informations about SPM and signup instructions can be found <a href="http://sematext.com/spm/index.html" target="_blank">here</a>.</p>
			<p>To start monitoring your SPM application, on each monitored machine you should install SPM client package.</p>
			<p>SPM client install instructions can be found <a href="https://apps.sematext.com/spm-reports/client.do" target="_blank">here</a>.</p>
			<p>Note for Windows users: the <a href="https://sematext.atlassian.net/wiki/display/PUBSPM/SPM+FAQ#SPMFAQ-DoesSPMworkonWindows?" target="_blank">faq</a> from SPM recommends to install SPM client under Cygwin.</p>
			<p>After the SPM client has been installed you need to run:</p>
			<pre>openespctl spm --spmjarfile=&lt;location/of/agent.jar&gt; --spmappid=&lt;spmApplicationID&gt; --servicename=&lt;serviceOrDaemonName&gt; install</pre>
			<p>Next, restart the OpenESP service or daemon for logging to start</p>
			<br clear="all"/>
		</div>
		
		<div id="searchanalytics">
			<img src="sematext-sa.png" align="right" width="400">
			<h1>Sematext Search Analytics</h1>
			<p>Sematext Search Analytics is a cloud-based Search Analytics service. In order to use it you need to <a href="http://sematext.com/search-analytics/index.html" target="_blank">signup</a> and follow <a href="http://sematext.com/search-analytics/index.html">these instructions</a>.</p>
			<p>After you sign up, you can <a href="http://apps.sematext.com/users-web/login.do" target="_blank">login</a> and add a search analytics applications under the Search Analytics tab.</p>
			<p>Also review the <a href="http://sematext.com/search-analytics/faq.html" target="_blank">FAQ</a>.</p>
			<p>Reports for your search analytics applications can be found on the <a href="https://apps.sematext.com/sa-reports/mainPage.do" target="_blank">dashboard page</a>. This link works only if you have active search analytics applications.</p>
			<br clear="all"/>
		</div>
		
		<div id="solrmeter">
			<img src="solrmeter.png" align="right" width="400">
			<h1>SolrMeter</h1>
			<p>SolrMeter is a generic tool to interact with Solr, firing queries and adding documents to make sure that your Solr implementation will support the real use. With SolrMeter you can simulate your work load over solr index and retrieve statistics graphically.</p>
			<p>It can be started from OpenESP command line using: <span class="inlinecode">openespctl solrmeter start</span></p>
			<p>More information on SolrMeter can be found on <a href="https://code.google.com/p/solrmeter/" target="_blank">code.google.com/p/solrmeter/</a>.</p>
			<br clear="all"/>
		</div>
		
		<div id="vifun">
			<img src="vifun.png" align="right" width="400">
			<h1>Vifun</h1>
			<p>Vifun, a GUI to help visually tweak Solr scoring, is bundled with OpenESP.</p>
			<p>It can be started from command line using: <span class="inlinecode">openespctl vifun start</span></p>
			<p>More information on Vifun can be found <a href="https://github.com/jmlucjav/vifun" target="_blank">here</a>.</p>			
			<br clear="all"/>
		</div>

		<div id="zk">
			<img src="zookeeper.png" align="right" width="200">
			<h1>ZooKeeper</h1>
			<p>ZooKeeper is a requirement for running Solr in SolrCloud mode.</p>
			<p>It is recommended to run a standalone ZooKeeper instead of an in-process together with Solr.
			  You'll find ZooKeeper bundled in <span class="inlinecode">openesp/zookeeper</span></p>
			<p>More information on ZooKeeper, see <a href="http://zookeeper.apache.org/" target="_blank">zookeeper.apache.org</a>.</p>			
			<br clear="all"/>
		</div>
  </div>		
	
  </body>
</html>