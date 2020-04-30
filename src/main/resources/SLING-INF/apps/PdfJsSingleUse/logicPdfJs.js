$(function() {
	
	var passUrl='/portal/service/secure/securedSU?token='+document.getElementById('tk').value+'&documentname='+document.getElementById('dn').value;
	
	console.log("passUrlsingleuse: "+passUrl);
	gfg_Run(document.getElementById('dn').value);
	hitUrlMove(passUrl);
	
	
	$(document).bind("contextmenu",function(e) {
		 e.preventDefault();
		});
	
	$(document).keydown(function(e){
	    if(e.which === 123){
	       return false;
	    }
	});
	
	
	
});


function gfg_Run(filename) { 
	
	var document_name = document.getElementById("document-name");
//	    document_name.innerHTML = filename.split('_').join(' ');
	document_name.innerHTML = filename;
}

function clearconsole()
{ 
  console.log(window.console);  
  if(window.console )
  {    
    console.clear();  
  }
}


function hitUrlMove(urlPass){
	 
	 $.ajax({
		
		   type: "GET",
	        url: urlPass,
	        async: false,
	        success: function (dataRes) {
	        	console.log("dataUrlPass: "+dataRes);
	        	
	        	var isJsonValid=IsJsonString(dataRes);
				if(isJsonValid){
					
					var jsonStr = dataRes;
					jsonStr = JSON.parse(jsonStr);
					
					if(jsonStr.hasOwnProperty("status")){ 
						var status=jsonStr.status;
						
						if( status=="success" ){
							
							if(jsonStr.hasOwnProperty("fileUrl")){ 
								var fileUrl=jsonStr.fileUrl;
								
								if(fileUrl.match('^http://')){
									fileUrl = fileUrl.replace("http://","https://")
			 	    		    	 fileUrl = fileUrl.replace("8082","8083")
			 	    		    	}
								
								showPDF(fileUrl);
								
								// create dynamic anchor link.
								
								$(".downlodFile").click(function(){
								    
									var link = document.createElement('a');
									link.href = fileUrl;
									link.download = fileUrl.substring(fileUrl.lastIndexOf("/")+1, fileUrl.length);
									document.body.appendChild(link);
									link.click();
									
								  });
								
//								clearconsole();
							}
							
						}else{
							if( status=="error" ){
								if(jsonStr.hasOwnProperty("message")){ 
									var message=jsonStr.message;
									
//									document.getElementById("errorMsg").innerHTML=message;
									window.open("https://bluealgo.com:8083/portal/service/secure/expired","_self")
									
								}
							}
						}
						
					}
					
					
					
				} // jsonValid
	        	
	        }
		 
	 });
	 
 }

function IsJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}

function getAllUrlParams(url) {

	  // get query string from url (optional) or window
	  var queryString = url ? url.split('?')[1] : window.location.search.slice(1);

	  // we'll store the parameters here
	  var obj = {};

	  // if query string exists
	  if (queryString) {

	    // stuff after # is not part of query string, so get rid of it
	    queryString = queryString.split('#')[0];

	    // split our query string into its component parts
	    var arr = queryString.split('&');

	    for (var i = 0; i < arr.length; i++) {
	      // separate the keys and the values
	      var a = arr[i].split('=');

	      // set parameter name and value (use 'true' if empty)
	      var paramName = a[0];
	      var paramValue = typeof (a[1]) === 'undefined' ? true : a[1];

	      // (optional) keep case consistent
	      //paramName = paramName.toLowerCase();
	      paramName = paramName;
	      //if (typeof paramValue === 'string') paramValue = paramValue.toLowerCase();
	      if (typeof paramValue === 'string') paramValue = paramValue;

	      // if the paramName ends with square brackets, e.g. colors[] or colors[2]
	      if (paramName.match(/\[(\d+)?\]$/)) {

	        // create key if it doesn't exist
	        var key = paramName.replace(/\[(\d+)?\]/, '');
	        if (!obj[key]) obj[key] = [];

	        // if it's an indexed array e.g. colors[2]
	        if (paramName.match(/\[\d+\]$/)) {
	          // get the index value and add the entry at the appropriate position
	          var index = /\[(\d+)\]/.exec(paramName)[1];
	          obj[key][index] = paramValue;
	        } else {
	          // otherwise add the value to the end of the array
	          obj[key].push(paramValue);
	        }
	      } else {
	        // we're dealing with a string
	        if (!obj[paramName]) {
	          // if it doesn't exist, create property
	          obj[paramName] = paramValue;
	        } else if (obj[paramName] && typeof obj[paramName] === 'string'){
	          // if property does exist and it's a string, convert it to an array
	          obj[paramName] = [obj[paramName]];
	          obj[paramName].push(paramValue);
	        } else {
	          // otherwise add the property
	          obj[paramName].push(paramValue);
	        }
	      }
	    }
	  }

	  return obj;
	}


