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
    </head>
  <body>
  
  <p/>
  <p/>

	<ul class="tabs">
		<li><a href="#welcome">Welcome</a></li>
		<li><a href="#spm">SPM</a></li>
		<li><a href="#searchanalytics">Search Analytics</a></li>
		<li><a href="#solrmeter">SolrMeter</a></li>
		<li><a href="#vifun">Vifun</a></li>
		<a href="#welcome"><img class="logo" src="OpenESP-Logo.gif" alt="OpenESP logo" height="25"></a>
	</ul>
  
	<div class="tabcontents">
		<div id="welcome">
		  <h1>Welcome to OpenESP</h1>
		  This is the OpenESP administration panel. This is your starting point for managing your search engine.
		  <p><a href="/solr/"><b>Solr</b></a></p>
		  <p><a href="/mcf/"><b>ManifoldCF</b> (see Admin Guide for how to enable)</a></p>
			For help or more info, please see <a href="http://openesp.org/" target="_blank">openesp.org</a> and <a href="https://github.com/openesp/openesp/" target="_blank">GitHub</a>
		</div>
	
		<div id="spm">
			<h1>Scalabale Performance Monitoring from Sematext</h1>
			<p>General informations about SPM and signup instructions can be found <a href="http://sematext.com/spm/index.html" target="_blank">here</a>.</p>
			<p>To start monitoring your SPM application, on each monitored machine you should install SPM client package. </p>
			<p>SPM client install instructions can be found <a href="https://apps.sematext.com/spm-reports/client.do" target="_blank">here</a>.</p>
			<p>Note for Windows users: the <a href="https://sematext.atlassian.net/wiki/display/PUBSPM/SPM+FAQ#SPMFAQ-DoesSPMworkonWindows?" target="_blank">faq</a> from SPM recommends to install SPM client under Cygwin.</p>
			<p>After the SPM client has been installed you need to run:</p>
			<p><b>1) openespctl spm install &lt;location/of/agent.jar&gt;</b> (will update tomcat options) </p>
			<p> and if on Windows: <b>2) openespctl service restart</b> </p>
			<p> and if on Linux/Unix/Mac: <b>2) openespctl daemon restart</b> <p>
		</div>
		
		<div id="searchanalytics">
			<h1>Sematext Search Analytics</h1>
			<p>In order to use Sematext Search Analytics you need to <a href="http://sematext.com/search-analytics/index.html" target="_blank">signup</a> and follow instructions from <a href="http://sematext.com/search-analytics/index.html">here</a>.</p>
			<p>After you sign up, you can <a href="http://apps.sematext.com/users-web/login.do" target="_blank">login</a> and add a search analytics applications under the Search Analytics tab.</p>
			<p>FAQ for search analytics can be found <a href="http://sematext.com/search-analytics/faq.html" target="_blank">here</a>.</p>
			<p>Reports for your search analytics applications can be found <a href="https://apps.sematext.com/sa-reports/mainPage.do" target="_blank">here</a>. This link works only if you have active search analytics applications.</p>
		</div>
		
		<div id="solrmeter">
			<h1>SolrMeter</h1>
			<p>SolrMeter is bundled with OpenESP.</p>
			<p>It can be started from command line using: <b>openespctl solrmeter start</b></p>
			<p>More information on SolrMeter can be found <a href="https://code.google.com/p/solrmeter/" target="_blank">here</a>.</p>
		</div>
		
		<div id="vifun">
			<h1>Vifun</h1>
			<p>Vifun, a GUI to help visually tweak Solr scoring, is bundled with OpenESP.</p>
			<p>It can be started from command line using: <b>openespctl vifun start</b></p>
			<p>More information on Vifun can be found <a href="https://github.com/jmlucjav/vifun" target="_blank">here</a>.</p>			
		</div>
  </div>		
	
  </body>
</html>