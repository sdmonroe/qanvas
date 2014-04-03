
<!DOCTYPE html "-//W3C//DTD XHTML 1.0 Strict//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 

<html>
<title>qanvas (prototype)</title>
<head>
	<link type="text/css" href="css/main.css" rel="stylesheet"/> 
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/main.js"></script>
	<script type="text/javascript" src="js/view.js"></script>
	<script type="text/javascript" src="js/me.js"></script>
	<script type="text/javascript" src="js/date.format.js"></script>

	<!-- load calendar -->
	<link rel='stylesheet' type='text/css' href='fullcalendar/fullcalendar.css' />
	<script type='text/javascript' src='fullcalendar/fullcalendar.js'></script>

	<!-- load pretty date -->
	<script type="text/javascript" src="js/humandates.js"></script>
	
	<!-- load photo gallery -->
	<script src="galleria/galleria-1.2.4.js"></script>
	<script src="galleria/galleria-1.2.4.min.js"></script>
<!---->	<script src="galleria/themes/classic/galleria.classic.min.js"></script>
	<link rel="stylesheet" href="galleria/themes/galleria.classic.css">
	
	<!-- load div slide to close animator -->
		<script type='text/javascript' src='js/jquery.maps.js'></script>	</head> 
<!-- 	<script src="https://maps.google.com/maps?file=api&v=2&key=ABQIAAAAvbpvFpgiK-X0_ZSkXIb_fhSoRcdydWEi6q5FOFHuyLS2Q5O86RRL2I7-WT0GcgY5e1SpCgsWurPF4w" type="text/javascript"></script>  -->


	<!-- load google books -->
    <script type="text/javascript" src="https://www.google.com/jsapi?key=ABQIAAAArud3wgC2HyiVBE7ZoZa2gRT2yXp_ZAY8_ufC3CFXhHIE1NvwkxQyFk3HStKn4xF3rXUR9ROQ4aBwaA"></script>

	<!--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>-->
	<script type="text/javascript" src="js/animatedcollapse.js">
	
		/***********************************************
		* Animated Collapsible DIV v2.4- (c) Dynamic Drive DHTML code library (www.dynamicdrive.com)
		* This notice MUST stay intact for legal use
		* Visit Dynamic Drive at http://www.dynamicdrive.com/ for this script and 100s more
		***********************************************/
	
	</script>
	
	<script type="text/javascript">
    		google.load("books", "0");
		var featureList = ["dock","panelbar", "ghostdrag", "quickedit"];
		function init(){
			jQuery(document).ready(function(jq){
   				jq(); // this is the jQuery reference!
				document.getElementById('locationBarPane').style.display = 'none';
				document.getElementById('tripleBarPane').style.display = 'none';
			});
			var text = document.getElementById("vLabel");
			//text.innerHTML = "hello";
			OAT.QuickEdit.assign(text, OAT.QuickEdit.STRING);
			
			//	var q = "limit=25&offset=0&viewType=list";
			//	document.getElementById('path').value = "me";
			//	var params = getFormParams();
				// remove text from input box after pulling it into params, but before making the ajax call
				// this allows user to continue typing without having their text erased after the ajax call is complete
			//	document.getElementById('path').value = "";
			//	jQuery.get(baseUrl + "?" + q, params, loadView, 'xml');
		}
	</script>
	<script type="text/javascript" src="js/oat/loader.js"></script>
	
	
	
	<script type="text/javascript">
	
		animatedcollapse.addDiv('locationBarPane', 'fade=0,speed=550');
		animatedcollapse.addDiv('tripleBarPane', 'fade=0,speed=550');
		
		//------------------------ EXAMPLES -----------------------//
		//animatedcollapse.addDiv('kelly', 'fade=1,height=100px');
		//animatedcollapse.addDiv('michael', 'fade=1,height=120px');
		
		//animatedcollapse.addDiv('cat', 'fade=0,speed=400,group=pets');
		//animatedcollapse.addDiv('dog', 'fade=0,speed=400,group=pets,persist=1,hide=1');
		//animatedcollapse.addDiv('rabbit', 'fade=0,speed=400,group=pets,hide=1');
		
		animatedcollapse.ontoggle=function($, divobj, state){ //fires each time a DIV is expanded/contracted
			//$: Access to jQuery
			//divobj: DOM reference to DIV being expanded/ collapsed. Use "divobj.id" to get its ID
			//state: "block" or "none", depending on state
		}
		
		animatedcollapse.init();
		//document.getElementById('tripleBarPane').style.visability = 'hidden';
	
	</script>

</head>

<!-- TODO: this needs to go in a .js file -->
<!-- 'just type' feature requires the cursor to be hidden; only way is to disable to text field -->
<!-- browsers still keyboard stroke events like backspace and space, unless you give a text field focus; so when a key is held down -->
<!-- give the locationbar the focus, then append the letter, then disable it again -->
<!-- also, give the body back the focus, can't set focus on a div, so you have to scroll to the body element to give it focus -->
		
	<body id="x" onkeydown="javascript:
	if(document.getElementById('tripleBarPane').style.display == 'none'){
		if(document.getElementById('path').disabled == true){
			document.getElementById('path').disabled = false;
			document.getElementById('path').focus();
		}
		addChar(event);
	}
	else{
		if (event.keyCode == 38) {
			animatedcollapse.hide('tripleBarPane');
		}
	}" 
	
	onkeyup="javascript:
	if(document.getElementById('tripleBarPane').style.display == 'none'){
		self.location = '#x';
		document.getElementById('path').disabled = true;
	}	
	if(document.getElementById('tripleBarPane').getElementsByTagName('input').length == 8){
		document.getElementById('v').value = jQuery('#tripleBarPane').children('input')[0].value;
	}">
	
	
	
<!--<script type="text/javascript"> 
 
jQuery(document).ready(function() {
	jQuery("#map1").googleMap(37, -122, 5, {
		controls: ["GSmallMapControl", "GMapTypeControl"],
		markers: jQuery(".geo")
	});
	// Example to access the map object is below:
	// jQuery.googleMap.maps["map1"].setMapType(G_SATELLITE_TYPE);
});
 
</script> <div id="map1" style="width: 300px; height: 300px;"></div> 
 
<div class="geo" title="Foo"> 
<abbr class="latitude" title="37.408183">N 37° 24.491</abbr> 
<abbr class="longitude" title="-122.13855">W 122° 08.313</abbr> 
</div> 
 
<div class="geo" title="Bar"> 
<abbr class="latitude" title="36.408183">N 36° 24.491</abbr> 
<abbr class="longitude" title="-121.13855">W 121° 08.313</abbr> 
</div></div>
-->

		<!-- events are attached to the locationBar input field in the addChar() method -->
		<div class="searchIn" id="searchIn" style="display:none;"></div>
		<div id="locationBarPane"  style="display:none;"><input name="path"  onmouseover="javascript:cursor='auto';" id="path" type="text" class="locationBar" disabled="true" autocomplete="off"/></div>
		<div id="tripleBarPane" style="display:none;">
			<div class="searchIn" id="sLabel" onmouseover="javascript:this.style.cursor='pointer';" onclick="javascript:swapSV();">me</div>
			<div class="searchIn" id="pLabel" onmouseover="javascript:this.style.cursor='pointer';" onclick="javascript:swapSV()"></div>
			<div class="tripleBarValue" id="vLabel" onmouseover="javascript:this.style.cursor='pointer';"></div>
			<span title="save statement (ctrl+s)" class="tripleBarAction" onmouseover="javascript:this.style.cursor='pointer';" onclick="javascript:saveStatement()">save</span>
<!--			<span title="swap subject and value" class="tripleBarAction" onmouseover="javascript:this.style.cursor='pointer';" onclick="javascript:swapSV()">swap</span> -->
			<span title="create something" class="tripleBarAction" onmouseover="javascript:this.style.cursor='pointer';" onclick="javascript:doCreate();">create</span>
			<span title="create something" class="tripleBarAction" onmouseover="javascript:this.style.cursor='pointer';" onclick="javascript:var v = DELIMIT_IRI_START+document.getElementById('v').value+DELIMIT_IRI_END;doBookmark(v);">bookmark</span>
			<span title="create something" class="tripleBarAction" onmouseover="javascript:this.style.cursor='pointer';" onclick="javascript:var v = document.getElementById('v').value;doCreateNote(v);">write</span>
			<input type="hidden" name="s" id="s" value="me"/>
			<input type="hidden" name="p" id="p"/>
			<input type="hidden" name="v" id="v"/>
			<input type="hidden" name="r" id="r"/>
			<input type="hidden" name="searchSuffix" id="searchSuffix"/>
			<input type="hidden" name="searchTarget" id="searchTarget"/>
			<input type="hidden" name="webid" id="webid" value="http://semanticweb.org/id/Sherman_Monroe"/>
		</div>
		<div id = "canvas">
		</div>
	</body>
</html>