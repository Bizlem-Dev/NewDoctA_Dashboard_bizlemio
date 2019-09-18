//var Email="nilesh@gmail.com";

if(document.getElementById("email").value=="anonymous"){
//	var Email= "viki@gmail.com";
//	var Email= "nilesh@gmail.com";
}else{
	var Email= document.getElementById("email").value;
}


//var group="G1";
var group;
var role;
var roleid;
var serviceid;

var sslCheck=window.location.href;

var clauseapproveljson={};

$(document).ready(function() {
	
//	$('#list').prop('disabled', 'disabled'); // extra
	
	var selection =$('#stepUlSelect').val();
	 $('.right-step-part ul').css('display','none');
     if(selection == 1) {
       $('.right-step-part ul.stepUl1').css('display','block');
     }
	
	
	displayLoginUsername();
	
	    var option = $('.options1:checked').val();
	    if (option == 0) {
	        $('.radio-option-select').hide();
//	        $('.right-step-part ul li').css('margin-bottom','5px');
	        $('.right-step-part').css('max-height','200px');
	    }else{
	      $('.radio-option-select').show();
//	      $('.right-step-part ul li').css('margin-bottom','31px');
	      $('.right-step-part').css('max-height','307px');
	    }

	   /* $(this).find('.plus-minus-toggle').toggleClass('collapsed');
	    $(this).parent().toggleClass('active');*/

	 /* $('.card-deck a').fancybox({
	    caption : function( instance, item ) {
	      return $(this).parent().find('.card-text').html();
	    }
	  });*/
	
	
	selectworkingroupfun("working-group-DropdownClass");
//	getFileNameList();
	setSelectDocumentData();
	
//	folderCode();
//	checktemplateAndSourceForPreview();
	
	grtserviceid();
 //displaysettingtabsbyrolefun();

});

function grtserviceid(){
	
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/getserviceid?email='+Email,
			
			async:false,

		success:function(dataa){
			//console.log(JSON.stringify(dataa));
       // var datajson =JSON.parse(dataa);
			//console.log("json  "+JSON.stringify(datajson));
			console.log("json  "+dataa);
			serviceid=dataa;
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


//....group dashboard.........


/*function groupAllData(){
	
	$.ajax({ 
		
		type: 'GET',
		url: 'http://bizlem.io:8082/portal/process/shoppingcart/service_sampletests?email='+Email,
		async:false,
		success: function (dataa) {
			var json = JSON.parse(dataa);
			console.log("group selct: "+JSON.stringify(json));
			
			var getAllGroup="";
			
			for(var i=0;i<json.length;i++){
				var singleJson=json[i];
				
				getAllGroup=getAllGroup+'<option>'+singleJson+'</option>';
			}
			
			document.getElementById("working-group-Dropdown-idDrop").innerHTML =getAllGroup;
			
			group =document.getElementById("working-group-Dropdown-idDrop").value;
			console.log("group inside "+group);
			
			selectworkingrole();
			
		}
		
	});
}*/

function selectworkingroupfun(selectclass){
	//alert("in selectworkingroupfun");
	// if there is no event it is giving error messahe handle tha and show only create new event option
	$.ajax({ 
	type: 'GET',
	url: '/portal/process/shoppingcart/service_sampletests?email='+Email,
	async:false,
	success: function (dataa) {
	console.log(dataa);
	var json = JSON.parse(dataa);
	console.log("group selct: "+JSON.stringify(json));
	
	var x = document.getElementsByClassName(selectclass);
	//alert("x.length" +x.length);

	for(var i=0; i<x.length; i++){
	//alert("in for "+i);
	x[i].innerHTML="";
	//x[i].options[x[i].options.length] = new Option("--Select Event--", "--Select Event--");
	//x[i].options[x[i].options.length] = new Option("Create New Event", "eventnew");
	if(json.length>0){
	for(var j=0; j<json.length; j++){
	var key =json[j];
	x[i].options[x[i].options.length] = new Option(key, key);
	//x[i].options.add( new Option(key,value) );
	}}else{
	x[i].options[x[i].options.length] = new Option("NoGroup", "NoGroup");
	}

	}
	group =document.getElementById("working-group-Dropdown-idDrop").value;
	console.log("group inside "+group);
	selectworkingrole();
	
	}
	});
	}


function selectworkingrole(){
	//alert("in selectworkingroupfun");
	// if there is no event it is giving error messahe handle tha and show only create new event option
	$.ajax({ 
	type: 'GET',
	url: '/portal/servlet/service/getrole_id?Email='+Email+"&group="+group,
	async:false,
	success: function (dataa) {
	console.log(dataa);
	var json = JSON.parse(dataa);
	if(json.hasOwnProperty("role")){
	document.getElementById("Display-role").innerHTML=" "+json.role;
//	console.log(" # "+document.getElementById("Display-role").innerHTML);
	//alert("role inside ");
	}
	if(json.hasOwnProperty("roleid")){
	document.getElementById("Display-roleid").innerHTML=json.roleid;
//	console.log("# "+document.getElementById("Display-roleid").innerHTML);

	console.log("json.roleid "+json.roleid+" json.role "+json.role);
	
	//alert("roleid inside ");
	}
	
	 if(json.role=="user" || ""){
		 console.log("hide new document "+json.role);
		 $('#newDocumentIdShow').hide();
		    displayNewDocumentPage();
	}else{
		
		 $('#newDocumentIdShow').show();
		    displayNewDocumentPage();
	}
	 
	
	}
	});
	}

$("body").on("change","#working-group-Dropdown-idDrop",function(){
	group=$(this).val();
	
	
	//selectworkingroupfun("working-group-DropdownClass");
	selectworkingrole();
    getFileNameList();
	setSelectDocumentData();
	
	folderCode();
//	checktemplateAndSourceForPreview();
	
	role=document.getElementById("Display-role").innerHTML;
	roleid=document.getElementById("Display-roleid").innerHTML;
	//alert("role "+role+" roleid "+roleid);
	console.log("body_role: "+role );
	console.log("body_roleid: "+roleid );
	});


// end group dashboard.......


//....  start contact page code from here.............

function getFieldData(){	
	
//	var valueNameDrop = document.getElementsByClassName("fileListDrop").value;
	var valueNameDrop = document.getElementById("fileListDrop").value;
	
	//console.log("valueNameDrop: "+valueNameDrop);

	$.ajax({

		url : '/portal/getFieldData?nodePath='+valueNameDrop,
		type : 'GET',
		success : function(data) {
			//console.log("Data getFieldData " +data);
			var jsonStr = data;
			
			var isJsonValid=IsJsonString(jsonStr);
			if(isJsonValid){
			
			jsonStr = JSON.parse(jsonStr);
			
			var getFieldAllData="";
			
//			for (var i = 0; i < jsonStr.finalFieldGet.length; i++) {
				
//				var data= jsonStr.finalFieldGet[i];
			
			
				
				 // EXTRACT VALUE FOR HTML HEADER. 
			for (var i = 0; i < jsonStr.finalFieldGet.length; i++) {	
				 var col = [];
			            for (var key in jsonStr.finalFieldGet[i]) {
			                if (col.indexOf(key) === -1) {
			                	
			                	if(key==="fieldId"){
			                		
			                	}else{
			                      col.push(key);
			                	}
			                }
			            }
			}
			//console.log("col: "+col);
			         // get  TABLE.
//			            var table =document.getElementById("getAllFieldDataTable");
//			       var table =document.getElementById("getAllFieldData");
			 var table = document.createElement('table');
			 table.setAttribute('id', 'getAllFieldDataTable');
			 table.setAttribute('class', 'table table-hover');
			            
			         // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.

			            var tr = table.insertRow(-1);                   // TABLE ROW.
			            
			            for (var i = 0; i < col.length; i++) {
			                var th = document.createElement("th");      // TABLE HEADER.
			                th.innerHTML = col[i];
			                tr.appendChild(th);
			            }
			            
			            
			            // ADD JSON DATA TO THE TABLE AS ROWS.
			            for (var i = 0; i < jsonStr.finalFieldGet.length; i++) {

			                tr = table.insertRow(-1);

			                for (var j = 0; j < col.length; j++) {
			                    var tabCell = tr.insertCell(-1);
			                    tabCell.innerHTML = jsonStr.finalFieldGet[i][col[j]];
			                }
			            }
			            
			            
				/*Object.keys(data).forEach(function(key){
				    var value = data[key];
				  
				    
				    if(i==0){
				    	  
				    	  getFieldAllData=getFieldAllData+'<tr><th>'+key+'</th></tr>';
				    	  
				    }
				    	getFieldAllData=getFieldAllData+'<tr><a href="#"><td>'+value+'</td></a></tr>';
				    	
				    	
				    
				}); // close dynamic keys
*/				
				
				
//			} // for close 
			
			/*document.getElementById("getAllFieldData").innerHTML = getFieldAllData;*/
//			            document.getElementById("getAllFieldData").innerHTML = table;
			            
			         // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
			            var divContainer = document.getElementById("getAllFieldData");
			            divContainer.innerHTML = "";
			            divContainer.appendChild(table);
			            
		}// check length
	else{
		 console.log("no data filename uploaded data");
	}
			            
			
		} // success 
//		} // jsonvalid check

	}); // function on click
	
}



var getFileNameExcel={};
function getFileNameList(){	
	
	getFileNameExcel.Email=Email;
	getFileNameExcel.group=group;
	
	console.log("Email: "+Email);
	console.log("group: "+group);
	
	var json = JSON.stringify(getFileNameExcel);

	$.ajax({

		url : "/portal/getUploadedFileName",
		type : 'POST',
		 beforeSend: function(){
	        	$('#load').css("visibility", "visible");
	          },
		
		data : {
			'getFileList' : json
		},
//		async:false,
		success : function(data) {
			console.log("Data getFileList " +data);
			var jsonStr = data;
			
			var isJsonValid=IsJsonString(jsonStr);
			if(isJsonValid){
			
			jsonStr = JSON.parse(jsonStr);
			var getDropList="";
			for (var i = 0; i < jsonStr.allFileName.length; i++) {
				
				var filename = "";
				var filenode = "";
				
				if (jsonStr.allFileName[i].hasOwnProperty("filename")) {
					filename=jsonStr.allFileName[i].filename;
					filenode=jsonStr.allFileName[i].filenode;
			        // console.log("filename: "+filename);
//			         if(i == 0){
//			         getDropList=getDropList+'<option selected="selected" value='+filenode+'>'+filename+'</option>';
//			         }else{
//			        	 getDropList=getDropList+'<option value='+filenode+'>'+filename+'</option>';	 
//			         }
			         getDropList=getDropList+'<option value='+filenode+'>'+filename+'</option>';
			         document.getElementById("fileListDrop").innerHTML = getDropList;
			         
		      }
				getFieldData();
				
			} // for close 
			
			}// json valid check
			else{
				console.log("no filelist there");
			}
			
		} ,// sucees 

		complete: function(){
      	  $('#load').css("visibility", "hidden");
      	  }
		
	}); // function on click
	
}



/*document.getElementById('buttonExcelUpload').addEventListener('click', function() {
	  var files = document.getElementById('fileUploadExcel').files;
	  console.log("files: "+files);
	  if (files.length > 0) {
		  console.log("inside: ");
	      getBase64(files[0]);
	  }
	});


function getBase64(file) {
	   var reader = new FileReader();
	   reader.readAsDataURL(file);
	   reader.onload = function () {
	     console.log(reader.result);
	   };
	   reader.onerror = function (error) {
	     console.log('Error: ', error);
	   };
	}*/

/*var uploadExcel={};
var Moduletype="";
$("body").on("change",".test-xls",function(){
	
	if($(this).attr('id')=="file"){
		Moduletype= "clause";
	}
	else if($(this).attr('id')=="upload-xls-temp-lib"){
		Moduletype= "template";
	}else{
		console.log("write here for mailtemp");
	}
	var file_id= $(this).attr('id');
	var fileName=document.getElementById(file_id).value;
	console.log(fileName);
	console.log($(this).attr('id'));
	excelFileUpload(file_id, Moduletype, fileName);
	
	});	*/	

var uploadExcel={};
function excelFileUpload(){	
	var fileName=document.getElementById("fileUploadExcel").value;
	var cleanName=fileName.split('\\').pop();
//	$(this).parent("div").find("#fileuloadImagename").text(fileName);
	console.log("filename: "+fileName);
	console.log("cleanName: "+cleanName);
	var reader=new FileReader();
	
	var f=document.getElementById("fileUploadExcel").files;
	reader.readAsDataURL(f[0]);
	console.log("f: "+f);
	reader.onloadend=function(){
		console.log(reader.result);
		var filename=cleanName;
		var filedata=reader.result;
		
		var fd= filedata.substr(0, filedata.indexOf(",")+1);
		var fdata= filedata.replace(fd, "");
			uploadExcel.Email=encodeURI(Email);
			uploadExcel.filename=filename;
			uploadExcel.filedata=encodeURI(fdata);
			uploadExcel.group=group;
			
			var json = JSON.stringify(uploadExcel);

			$.ajax({

				url : "/portal/ContactData",
				type : 'POST',
				data : {
					'contactUploadFile' : json
				},

				success : function(dataRes) {
					console.log("Data Saved! " +dataRes);
					
					var jsonStr = dataRes;
					jsonStr = JSON.parse(jsonStr);
					
					var status=jsonStr.status;
					var message=jsonStr.message;
					if(status=="success"){
						console.log("if uploaded successfully file in contact upload.");
//					   var errorMsg = "<p>Example error message</p>"
					    var successMsg = "File SuccessFully Uploaded!"
						document.getElementById("successUploadContact").innerHTML = successMsg;
					    
					    document.getElementById("fileuloadImagename").innerHTML="File Name : "+cleanName;
					
						getFileNameList();
						getFieldData();
//						window.location.reload();
					    
					} // json status success
					
					else{
						console.log("else not uploaded successfully file in contact upload.");
						document.getElementById("successUploadContact").innerHTML = message;
						
						document.getElementById("fileuloadImagename").innerHTML="File Name : "+cleanName;
					}
					
					
				} // success function close
				
				

			}); // function on click
			
			
		}
}

// ............ end contact page code here...........................


//............dashboard code here.............................

var templateName1;
function setSelectDocumentData(){	

	$.ajax({

		url : '/portal/servlet/service/GetDTATemplateListSKU1?email='+Email+'&group='+group,
		type : 'GET',
		async:false,
		success : function(data) {
			console.log("setSelectDocumentData " +data);
			var jsonStr = data;
			jsonStr = JSON.parse(jsonStr);
			var setSelectDocumentData="";
			
			if(jsonStr.Templates.length>0){
				
			for (var i = 0; i < jsonStr.Templates.length; i++) {
				
				var templatename="";
				var templatetype="";
				var datasource="";
				var object="";


				if (jsonStr.Templates[i].hasOwnProperty("templatename")) {
					
					templatename=jsonStr.Templates[i].templatename;
					templatetype=jsonStr.Templates[i].templatetype;
					datasource=jsonStr.Templates[i].datasource;
					object=jsonStr.Templates[i].object;


					
					if(i==0){
						setSelectDocumentData=setSelectDocumentData+'<option>Select Document</option><option datasource="'+datasource+'" object="'+object+'" value="'+templatetype+'">'+templatename+'</option>';
					}else{
						setSelectDocumentData=setSelectDocumentData+'<option datasource="'+datasource+'" object="'+object+'" value="'+templatetype+'">'+templatename+'</option>';
					}
						
			            document.getElementById("setSelectDocumentData").innerHTML = setSelectDocumentData;
			         
			            var textSelectDocumentName = document.getElementById("setSelectDocumentData");
			       	         templateName1=textSelectDocumentName.options[textSelectDocumentName.selectedIndex].text;
			       	        console.log("templateNameOnload: "+templateName1);
			       	     
		      }

				
			} // for close 
			
			}
			
		} // sucees 

	}); // function on click
	
}


// take selected mailTemplateName.....................
/*var mailTemplateValue;
$("#multipleGetDropdowngetValue") .change(function () { 
	
	var textSelectDocumentName = document.getElementById("multipleGetDropdowngetValue");
	    mailTemplateValue=textSelectDocumentName.options[textSelectDocumentName.selectedIndex].text;
	    console.log("mailTemplateValue: "+mailTemplateValue);
	
	
	
});*/

var mailTemplateValue;
$("body").on("change", "#multipleGetDropdowngetValue", function(){
	 mailTemplateValue = $("option:selected", this).text();
	 console.log("mailTemplateValue: "+mailTemplateValue);

	});

function setDynamicMultipleDropdownList(){	

	$.ajax({

		url : '/portal/servlet/service/GetMailDTATemplateListSKU1?email='+Email+'&group='+group,
		type : 'GET',
		async:false,
		success : function(data) {
			console.log("setDynamicMultipleDropdownList " +data);
			var jsonStr = data;
			jsonStr = JSON.parse(jsonStr);
			var multipleGetDropdownList='';
			
			if(jsonStr.MailTemplates.length>0){
				
			for (var i = 0; i < jsonStr.MailTemplates.length; i++) {
				
					multipleGetDropdownList=multipleGetDropdownList+'<option value='+jsonStr.MailTemplates[i]+'>'+jsonStr.MailTemplates[i]+'</option>';
				
			} // for close
			document.getElementById("multipleGetDropdownList").innerHTML = '<select class="form-control" id="multipleGetDropdowngetValue">' + multipleGetDropdownList + '</select>';
			
			 mailTemplateValue = $('#multipleGetDropdowngetValue').val();
//			 mailTemplateValue = $("#multipleGetDropdowngetValue", this).text();
			 console.log("mailTemplateValue inside: "+mailTemplateValue);
			
			}
			
		} // sucees 

	}); // function on click
	
}



//........end mailtemplate here.........................

var col;
$( "#example2" ) .change(function () { 
	
	var values = $('#example2').val();
	console.log("deliveryOption: "+values);
	
	console.log("length: "+values.length);

	/*var data = $('#example2 option:selected').toArray().map(item => item.text).join();
	console.log("data: "+data);
	
	var nameArr = data.split(',');
	console.log("nameArr: "+nameArr.length);
	
	col=[];
	
	for(var i=0;i<nameArr.length;i++){
		var a=nameArr[i];
		col.push(a);
	}
	
	console.log("colArray: "+col);*/
	
	col=[];
	if(values.length>0){
		
	for(var i=0;i<values.length;i++){
		var a=values[i];
		console.log("a Array: "+a);
		
            col.push(a);
            
	} // for close 
	console.log("colArray: "+col);
	
	}
	
});


var templateName;
var typeDataSource;
var templateLibrary;
var datasource;
var object;
var dynamicDataSourceMainJson = {};

function getTemplateName(){
	
	var textSelectDocumentName = document.getElementById("setSelectDocumentData");
	 templateName=textSelectDocumentName.options[textSelectDocumentName.selectedIndex].text;
	 console.log("templateNameinside: "+templateName);
	 
	 templateLibrary=textSelectDocumentName.options[textSelectDocumentName.selectedIndex].value;
	 console.log("templateLibrary: "+templateLibrary);
	 
	 datasource=textSelectDocumentName.options[textSelectDocumentName.selectedIndex].getAttribute("datasource");
	 console.log("datasource: "+datasource);
	 
	 object=textSelectDocumentName.options[textSelectDocumentName.selectedIndex].getAttribute("object");
	 console.log("object: "+object);
	 
	 checktemplateAndSourceForPreview();
	 
  if(datasource=="ws"){
	  document.getElementById("pkObject").innerHTML = object;
	 $('.displaydatasource').css('display','none');
	 $('.displaydatasourcews').css('display','block');

  }else{
	  $('.displaydatasource').css('display','block');
		 $('.displaydatasourcews').css('display','none');
  }
  
  document.getElementById("documentUrlGenerateId").innerHTML = "";
  
  ResetClickTemplateName(); 

}



function showDataSource(){
	
	var contactsUrl='/portal/servlet/service/getContactOrFields.contacts?email='+Email+'&group='+group+'&templatename='+templateName+'&templatetype='+templateLibrary;
	var uploadExcelUrl='/portal/servlet/service/getContactOrFields.uploadexcel?email='+Email+'&group='+group+'&templatename='+templateName+'&templatetype='+templateLibrary;
	var enterManuallyUrl='/portal/servlet/service/getContactOrFields.entermanually?email='+Email+'&group='+group+'&templatename='+templateName+'&templatetype='+templateLibrary;
	
	 typeDataSource = document.getElementById("list");
	typeDataSource=typeDataSource.options[typeDataSource.selectedIndex].text;
	
	
	 if(typeDataSource=="Contacts"){
		 console.log("templateNameFunction: "+templateName);
		 console.log("typeDataSource: "+typeDataSource);
		 
		 callFunctionDataSourceFetchData(contactsUrl);
//		 $("#list").selectpicker("refresh");
		 
	 }else if(typeDataSource=="Upload Excel"){
		 console.log("templateNameFunction: "+templateName);
		 console.log("typeDataSource: "+typeDataSource);
		 
		 callUploadExcelDataSource(uploadExcelUrl);
//		 $("#list").selectpicker("refresh");
		 
	 }else if(typeDataSource=="Enter Manually"){
		 console.log("templateNameFunction: "+templateName);
		 console.log("typeDataSource: "+typeDataSource);
		 console.log("enterManuallyUrl: "+enterManuallyUrl);
		 callFunctionEnterManuallyDataSourceFetchData(enterManuallyUrl);
		 
	 }else{
		 console.log("no data source found from list ");
	 }
	 
	 
	
}

var uploadExcel={};
function excelDataSourceFileUpload(){	
	var fileName=document.getElementById("uploadExcelDataSource").value;
	var cleanName=fileName.split('\\').pop();
	$(this).parent("div").find(".testdatasource-xls-filename").text(fileName);
	console.log("fileName: "+fileName);
	console.log("cleanName: "+cleanName);
	var reader=new FileReader();
	
	var f=document.getElementById("uploadExcelDataSource").files;
	reader.readAsDataURL(f[0]);
	console.log(f);
	reader.onloadend=function(){
		console.log(reader.result);
		var filename=cleanName;
		var filedata=reader.result;
		
		var fd= filedata.substr(0, filedata.indexOf(",")+1);
		var fdata= filedata.replace(fd, "");
			uploadExcel.Email=encodeURI(Email);
			uploadExcel.filename=filename;
			uploadExcel.filedata=encodeURI(fdata);
			uploadExcel.group=group;
			
			var json = JSON.stringify(uploadExcel);

			$.ajax({

				url : "/portal/dataSourceExcelUploadFile",
				type : 'POST',
				data : {
					'dataSourceExcelUploadFile' : json
				},

				success : function(data) {
					console.log("DataSourceUploadFile " +data);
					
					dynamicDataSourceMainJson = JSON.parse(data);
//					jsonStr = JSON.parse(jsonStr);
//					dynamicDataSourceMainJson=JSON.parse(jsonStr);
					
					    var successMsg = "File SuccessFully Uploaded!"
						document.getElementById("successDataSourceUploadContact").innerHTML = successMsg
					
				}

			}); // function on click
			
			
		}
}


function  callUploadExcelDataSource(UrlLink){	

	$.ajax({

		url : UrlLink,
		type : 'GET',
		async:false,
		success : function(data) {
			console.log("callUploadExcelDataSource " +data);
			var jsonStr = data;
			jsonStr = JSON.parse(jsonStr);
			
			var downlodFileInsert="";
			var fieldExcel_url=jsonStr.fieldExcel_url;
			
			downlodFileInsert=' <a href="'+fieldExcel_url+'" download class="btn btn-primary btn-block">Download File</a>';
			document.getElementById("uploadDownloadFile").innerHTML =  downlodFileInsert;
			
//			checktemplateAndSourceForPreview();
			
		} // sucees 

	}); // function on click
	
}

function  callFunctionEnterManuallyDataSourceFetchData(UrlLink){	

	$.ajax({

		url : UrlLink,
		type : 'GET',
		async:false,
		success : function(data) {
			console.log("selectedfields " +data);
			var jsonStr = data;
			jsonStr = JSON.parse(jsonStr);
			var multipleGetDropdownList='';
			
			if(jsonStr.selectedfields.length>0){
				
			for (var i = 0; i < jsonStr.selectedfields.length; i++) {
				
					multipleGetDropdownList=multipleGetDropdownList+'<div class="form-group"><label class="keyname">'+jsonStr.selectedfields[i]+'</label><input type="text" name="" class="form-control keyvalue"></div>';
				
			} // for close
			/*document.getElementById("enterManuallyId").innerHTML = '<div class="form-group"><input type="text" name="" class="form-control" placeholder="Enter value" disabled="disabled"></div>' + multipleGetDropdownList;*/
			document.getElementById("enterManuallyId").innerHTML =  multipleGetDropdownList;			
			}
			
//			checktemplateAndSourceForPreview();
		} // sucees 

	}); // function on click
	
}


function getDataSourceFormDataDynamic(){	
	
	var keyname = document.getElementsByClassName("keyname");
	var keyvalue = document.getElementsByClassName("keyvalue");
	
	
	var data = [];
	var dynamicDataSourceFormData = {};
	
	
	for (var i = 0; i < keyname.length; i++) {
		var keynameData=keyname[i].innerHTML;
		console.log("keynameData: "+keynameData);
		var keyvalueData=keyvalue[i].value;
		console.log("keyvalueData: "+keyvalueData);
		
		/*dynamicDataSourceFormData.data.push({
			
			[keynameData] : keyvalueData,
			
		});*/
		
		dynamicDataSourceFormData[keynameData]=keyvalueData;
		
	} // for close
	
	data.push(dynamicDataSourceFormData);
	dynamicDataSourceMainJson.data = data;
	
	var json = JSON.stringify(dynamicDataSourceMainJson);
	console.log("jsonInside: "+json);
	
	
	
}


var dataTableCount = 0;
function callFunctionDataSourceFetchData(urlLink){	

	
	/*$.ajax({

	url : urlLink,
	type : 'GET',
	async:false,
	success : function(data) {
		console.log("DataSource: " +data);
		var jsonStr = data;
		jsonStr = JSON.parse(jsonStr);
		
		var allContactsJson=JSON.parse(jsonStr.Allcontacts);
		//console.log("jsonData: "+allContactsJson);
		var finalField = allContactsJson.finalFieldGet;
		//console.log("finalField: "+finalField);
		console.log("finalField lngth: "+jsonStr.data.length);
		
		var tabledata="";
		var col = [];
		
		for (var i = 0; i < jsonStr.data.length; i++) {
			
            for (var key in jsonStr.data[i]) {
                if (col.indexOf(key) === -1) {
                    col.push(key);
                    
                }
            }
            
		}
		console.log("col: "+col);
		
        
        for (var k = 0; k < col.length; k++) {
        	
        	if(k==0){
        		tabledata=tabledata+'<th width="50%">'+col[k]+'</th>';
        	}else{
        		tabledata=tabledata+'<th width="50%">'+col[k]+'</th>';
        	}
        	
        	if (k === 2) { break; }
        	
        }
        document.getElementById("tabledataHeader").innerHTML = '<tr><th style="width:10px;"></th>'+tabledata+'</tr>';
        var trtablebody = "";
        for (var i = 0; i < finalField.length; i++) {
        	var tablebody="";
			
        	 for (var j = 0; j < col.length; j++) {
        		var data= finalField[i][col[j]];
        		console.log("data: "+data);
        		
        		tablebody=tablebody+'<td>'+data+'</td>';
        		
        		if(j===2){
        			break;
        		}
        		
        	 }
        	 trtablebody = trtablebody + ' <tr><td><input type="checkbox" name="" value="0"></td>'+tablebody+'</tr>';
        }
        
        document.getElementById("tablebody").innerHTML = trtablebody;
        
        
		
	} 

});*/
	
	
	
		$.ajax({
		url : urlLink,
		type : 'GET',
		success : function(data) {
			console.log("data: " + data);
			
			var isJsonValid=IsJsonString(data);
			if(isJsonValid){
			
				 var dataJsonObj = JSON.parse(data);
				if (dataJsonObj.hasOwnProperty("data")) {
					var dataArr = dataJsonObj.data;
			   
			    var cols = [];

				var exampleRecord = dataArr[0];
				 console.log("exampleRecord: " + JSON.stringify(exampleRecord));
				 

				//get keys in object. This will only work if your statement remains true that all objects have identical keys
				var keys = Object.keys(exampleRecord);

				//for each key, add a column definition
//				keys.forEach(function(k) {
//					
//					cols.push({
//						title : k,
//						data : k
//					//optionally do some type detection here for render function
//					});
//					
//				});
				cols.push({
					title : "",
					data : ""
				});
				for(var k=0;k<keys.length;k++){
					if(k == 3){
						break;
					}
					cols.push({
						title : keys[k],
						data : keys[k]
					//optionally do some type detection here for render function
					});
				}

				console.log("cols: "+cols);
				//initialize DataTables
				if(dataTableCount == 0){
				var table = $('#exampleA').DataTable({
					"columnDefs": [{
						"targets": 0,
						"render": function(data, type, full, meta) {
						return '<input type="checkbox" ' + (data == 'True' ? 'checked' : '') + ' />';
						}
						}],
						data : dataArr,
					columns : cols
				});
				dataTableCount++;
				}else{
					var table = $('#exampleA').DataTable({
						"columnDefs": [{
							"targets": 0,
							"render": function(data, type, full, meta) {
							return '<input type="checkbox" ' + (data == 'True' ? 'checked' : '') + ' />';
							}
							}],
							"bDestroy" : true,
							data : dataArr,
						columns : cols
					});	
				}

				//add data and draw
				//table.rows.add(dataArr).draw();
				
				
				
			}// data check here
			
				else{
					 var dataArr = [];
					    var cols = [];

						//initialize DataTables
						if(dataTableCount == 0){
							console.log("inside count");
						var table = $('#exampleA').DataTable({
							"columnDefs": [{
								"targets": 0,
								"render": function(data, type, full, meta) {
								return '<input type="checkbox" ' + (data == 'True' ? 'checked' : '') + ' />';
								}
								}],
								data : dataArr,
							columns : cols
						});
						dataTableCount++;
						}else{
							var table = $('#exampleA').DataTable({
								"columnDefs": [{
									"targets": 0,
									"render": function(data, type, full, meta) {
									return '<input type="checkbox" ' + (data == 'True' ? 'checked' : '') + ' />';
									}
									}],
									"bDestroy" : true,
									data : dataArr,
								columns : cols
							});	
						}
				}
				
				
			}// json valid check
			else{
				console.log("no data there");
			}
			
//			checktemplateAndSourceForPreview();
		}
	});
	
		
}



function getDataTableSelectedData(){
	
//	var dynamicDataSourceMainJson = {};
	var data = [];
	var oTable = $('#exampleA').dataTable();
	var rowcollection =  oTable.$("input[type='checkbox']:checked", {"page": "all"});
	rowcollection.each(function(index,elem){
	    var checkbox_value = $(elem).val();
	    console.log("checkbox_value: "+checkbox_value);
	    var tr = $(this).closest("tr");
	    var rowdata = $("#exampleA").DataTable().row(tr).data();
	    console.log("row_value rowdata: " + rowdata);
	    console.log("row_value: " + JSON.stringify(rowdata));
	    data.push(rowdata);
	});
	dynamicDataSourceMainJson.data = data;
	console.log("getDataTableSelectedData dynamicDataSourceMainJson : " + JSON.stringify(dynamicDataSourceMainJson));
	
	
}

var resetDocumentData=0;

function DocumentGeneration(){
	
	
	
 if(datasource!="ws"){
		 
	if( !isEmpty(templateName) && !isEmpty(Email) ){
	
//if(resetDocumentData==0){
		
		if(col.length>0){
			
			for(var i=0;i<col.length;i++){
				var mailSendCheckTemplate=col[i];
				if(mailSendCheckTemplate=="2"){
					dynamicDataSourceMainJson.MailTempName=mailTemplateValue;
				}else if(mailSendCheckTemplate=="3"){
					dynamicDataSourceMainJson.MailTempName=mailTemplateValue;
				}
			}
		}
		
	dynamicDataSourceMainJson.templateName=templateName; 
	dynamicDataSourceMainJson.typeDataSource=typeDataSource;
	dynamicDataSourceMainJson.AttachtempalteType=templateLibrary;
	dynamicDataSourceMainJson.Email=Email;
	dynamicDataSourceMainJson.group=group;
	dynamicDataSourceMainJson.multipeDropDown=col;
	dynamicDataSourceMainJson.Type="Generation";
	
	var json = JSON.stringify(dynamicDataSourceMainJson);
	console.log("jsonDocumentGeneration: "+json);
	
	
	$.ajax({
        type: "POST",
        url: "/portal/servlet/service/dDependencySKU1",
        async: false,
        data:JSON.stringify(dynamicDataSourceMainJson),
        contentType: "application/json",
        success: function (data) {
        console.log("urlGenerationdata: "+JSON.stringify(data));
        
        var dataArr = JSON.parse(data);
        
        if(dataArr.length>0){
        	var allMultipleLink="";
        	
            for(var i=0;i<dataArr.length;i++){
            	var documentlink="";
            	var pdfFileName="";
            	
            	if ( dataArr[i].hasOwnProperty("filename") ) {
           		  pdfFileName=dataArr[i].filename;
           	 }
            	
 	    	   if ( dataArr[i].hasOwnProperty("documentlink") ) {
 	    		     documentlink=dataArr[i].documentlink;
 	    		   
 	    		   /* var s=documentlink.lastIndexOf("/");
 	    		     pdfFileName = documentlink.substring(s+1);*/
 	    		   
 	    		    allMultipleLink= allMultipleLink+'<a href="'+documentlink+'" target="_blank">'+pdfFileName+'</a><br>';
 	    		     
 	    	   } // close here check documentlink
 	    	   
 	    	   
 	    	   
 	       } // for close
            
            if( !isEmpty(col) && !isEmpty(templateName) && templateName!="Select Document" ){
            	 document.getElementById("documentUrlGenerateId").innerHTML = allMultipleLink;
            }
            
           
            
            }// len check
        
    }
}); // ajax close
	

	Reset();
//	resetDocumentData++;
/*}else{
	console.log("else more than zero; ");
}*/
	}// 
	else{
		document.getElementById("documentUrlGenerateId").innerHTML = '<h6 style="color:#FF001B;">Please Select TemplateName</h6>';
	}
	
}// ws check

	 else{
		 
		 if(datasource=="ws"){
			 
			 var wsJson={};
			 
//			 if(resetDocumentData==0){
			 
				 wsJson.templatename=templateName;
				 wsJson.email=Email;
				 wsJson.group=group;
				 wsJson.datasource=datasource;
				 wsJson.object=object;
				 wsJson.pkvalue=document.getElementById("pkId").value; ;
				 console.log(JSON.stringify(wsJson));
			$.ajax({
			        type: "POST",
			        url: "/portal/servlet/service/callSFQuery.data",
			        async: false,
			        data:JSON.stringify(wsJson),
			        contentType: "application/json",
			        success: function (data) {
			        console.log("urlGenerationdata: "+JSON.stringify(data));
			        
			        var data1 = [];
			        data1.push(JSON.parse(data));
			        
			        dynamicDataSourceMainJson.templateName=templateName;  
			    	dynamicDataSourceMainJson.typeDataSource=typeDataSource;
			    	dynamicDataSourceMainJson.AttachtempalteType=templateLibrary;
			    	dynamicDataSourceMainJson.Email=Email;
			    	dynamicDataSourceMainJson.group=group;
			    	dynamicDataSourceMainJson.multipeDropDown=[];
			    	dynamicDataSourceMainJson.MailTempName=mailTemplateValue;
			    	dynamicDataSourceMainJson.Type="Generation";
			    	
			    	dynamicDataSourceMainJson.data=data1;
			    	
			    	
			    	
			    	var json = JSON.stringify(dynamicDataSourceMainJson);
			    	console.log("jsonDocumentGeneration: "+json);
			    	
			    	$.ajax({
			            type: "POST",
			            url: "/portal/servlet/service/dDependencySKU1",
			            async: false,
			            data:JSON.stringify(dynamicDataSourceMainJson),
			            contentType: "application/json",
			            success: function (data) {
			            console.log("urlGenerationdata: "+JSON.stringify(data));
			            
			            var dataArr = JSON.parse(data);
			            if(dataArr.length>0){
			            	var allMultipleLink="";
			            	
			                for(var i=0;i<dataArr.length;i++){
			                	var documentlink="";
			                	var pdfFileName="";
			                	
			                	if ( dataArr[i].hasOwnProperty("filename") ) {
				             		  pdfFileName=dataArr[i].filename;
				             	 }
			                	
			     	    	   if ( dataArr[i].hasOwnProperty("documentlink") ) {
			     	    		     documentlink=dataArr[i].documentlink;
			     	    		   
			     	    		   /* var s=documentlink.lastIndexOf("/");
			     	    		     pdfFileName = documentlink.substring(s+1);*/
			     	    		   
			     	    		    allMultipleLink= allMultipleLink+'<a href="'+documentlink+'" target="_blank">'+pdfFileName+'</a><br>';
			     	    		     
			     	    	   } // close here check documentlink
			     	    	   
			     	    	   
			     	    	   
			     	       } // for close
			                
			                if( !isEmpty(col) && !isEmpty(templateName) && templateName!="Select Document" ){
			               	     document.getElementById("documentUrlGenerateId").innerHTML = allMultipleLink;
			               }
			                
			                }// len check
			            
			        }
			    	}); // ajax close
			        }
			}); // ajax close
			
			Reset();
		/*		resetDocumentData++;
			 
		 }else{
			 console.log("else more than zero ws call; ");
		 }*/
			 
		 } // ws close
		 
		 
	 }// elsee close 
 
	 
}

function Reset() {
    var selectDocument = document.getElementById("setSelectDocumentData");
    selectDocument.selectedIndex = 0;
    
    var listDataSource = document.getElementById("list");
    listDataSource.selectedIndex = 0;
    
    var deliveryOption = document.getElementById("example2");
    deliveryOption.selectedIndex = 0;
    
    $('#multipleGetDropdownList').hide();
    
    $('option', $('#example2')).each(function(element) {
        $(this).removeAttr('selected').prop('selected', false);
      });
    $("#example2").multiselect('refresh'); 
    
    $('#pkId').val(''); // blank pkid  blank
    $('input[type="text"],textarea').val('');  
    document.getElementById("successDataSourceUploadContact").innerHTML="";
    
    /*console.log("datatable check refresh");
    $('#exampleA').DataTable();
   $('#exampleA').dataTable({
	    "bPaginate": false,
	});*/
    
//    location.reload(true);
    
    
    
    /*$('#documentUrlGenerateId').append('<br><br><p>This text has been appended to the end.</p> ');*/
    
}

function ResetClickTemplateName() {
    
    var listDataSource = document.getElementById("list");
    listDataSource.selectedIndex = 0;
    
    var deliveryOption = document.getElementById("example2");
    deliveryOption.selectedIndex = 0;
    
    $('#multipleGetDropdownList').hide();
    
    $('option', $('#example2')).each(function(element) {
        $(this).removeAttr('selected').prop('selected', false);
      });
    $("#example2").multiselect('refresh'); 
    
    $('#pkId').val(''); // blank pkid  blank
    $('input[type="text"],textarea').val('');  
    document.getElementById("successDataSourceUploadContact").innerHTML="";
    
    
//    location.reload(true);
    
    
    
    /*$('#documentUrlGenerateId').append('<br><br><p>This text has been appended to the end.</p> ');*/
    
}


//.............document previeww code..................
var previewDocumentData=0;
function DocumentPreviewpdf(){
	
	 if(datasource!="ws"){
	if( !isEmpty(templateName) && !isEmpty(Email) ){
	
	
//	if(previewDocumentData==0){
		dynamicDataSourceMainJson.templateName=templateName;  
		dynamicDataSourceMainJson.typeDataSource=typeDataSource;
		dynamicDataSourceMainJson.AttachtempalteType=templateLibrary;
		dynamicDataSourceMainJson.Email=Email;
		dynamicDataSourceMainJson.group=group;
		dynamicDataSourceMainJson.multipeDropDown=col;
		dynamicDataSourceMainJson.MailTempName=mailTemplateValue;
		dynamicDataSourceMainJson.Type="Preview";
		
		var json = JSON.stringify(dynamicDataSourceMainJson);
		console.log("jsonPdfDocumentPreview: "+json);
		
		$.ajax({
	        type: "POST",
	        url: "/portal/servlet/service/dDependencySKU1",
	        async: false,
	        data:JSON.stringify(dynamicDataSourceMainJson),
	        contentType: "application/json",
	        success: function (data) {
	        console.log("pdfurlgeneration: "+JSON.stringify(data));
	        
	        /*var dataArr = JSON.parse('[{"documentlink":"http://bizlem.io:8085/Attachment/Temp1_05-Jul-2019_17-13-10-857.pdf","creation_date":"Fri Jul 05 17:13:22 IST 2019","created_by":"viki@gmail.com"}]');*/
	        var dataArr = JSON.parse(data);
	        console.log("dataArr: "+dataArr);
	        
	       var len= dataArr.length;
	       console.log("len: "+len);
	       
	       for(var i=0;i<dataArr.length;i++){
	    	   
	    	   var pdfFileName="";
           	
           	if ( dataArr[i].hasOwnProperty("filename") ) {
            		  pdfFileName=dataArr[i].filename;
            	 }
	    	   
	    	   if ( dataArr[i].hasOwnProperty("documentlink") ) {
	    		    var documentlink=dataArr[i].documentlink;
	    		   
	    		   /* var s=documentlink.lastIndexOf("/");
	    		    var pdfFileName = documentlink.substring(s+1);*/
	    		    
	    		    if( !isEmpty(col) && !isEmpty(templateName) && templateName!="Select Document" ){
	    		    document.getElementById("pdfFileName").innerHTML = pdfFileName;
	    		    
	    		   var el = document.getElementById('iframeId');
	   	               el.src = documentlink; // assign url to src property
	   	            if(i==0){
	 	    		    break;
	 	    		    }
	    		    }// col check
	    	   } // close here check documentlink
	    	   
	       } // for close
	       
	       
	        /*var pdfData='<embed src="http://bizlem.io:8085/Attachment/Temp1_05-Jul-2019_17-13-10-857.pdf" frameborder="0" width="100%" height="400px" ><div class="modal-footer"><button type="button" class="btn btn-default" data-dismiss="modal">Close</button></div>';
	        document.getElementById("iframeId").innerHTML = pdfData;*/
	        
//	        $("#dialog").dialog();
	    }
	}); // ajax close
		
		
//		Reset();
	/*	previewDocumentData++;
	}else{
		console.log("else more than zero; ");
		
	}*/
	
	}// check null 
	else{
//		document.getElementById("documentUrlGenerateId").innerHTML = "<h6>Please Select TemplateName And Data Source</h6>";
	}
	
	 }// ws check
		 else{
			 
			 if(datasource=="ws"){
				 
				 var wsJson={};
				 
//				 if(previewDocumentData==0){
				 
					 wsJson.templatename=templateName;
					 wsJson.email=Email;
					 wsJson.group=group;
					 wsJson.datasource=datasource;
					 wsJson.object=object;
					 wsJson.pkvalue=document.getElementById("pkId").value; ;
					 console.log(JSON.stringify(wsJson));
				$.ajax({
				        type: "POST",
				        url: "/portal/servlet/service/callSFQuery.data",
				        async: false,
				        data:JSON.stringify(wsJson),
				        contentType: "application/json",
				        success: function (data) {
				        console.log("urlGenerationdata: "+JSON.stringify(data));
				        
				        var data1 = [];
				        data1.push(JSON.parse(data));
				        
				        dynamicDataSourceMainJson.templateName=templateName;  
				    	dynamicDataSourceMainJson.typeDataSource=typeDataSource;
				    	dynamicDataSourceMainJson.AttachtempalteType=templateLibrary;
				    	dynamicDataSourceMainJson.Email=Email;
				    	dynamicDataSourceMainJson.group=group;
				    	dynamicDataSourceMainJson.multipeDropDown=[];
				    	dynamicDataSourceMainJson.MailTempName=mailTemplateValue;
				    	dynamicDataSourceMainJson.Type="Preview";
				    	dynamicDataSourceMainJson.data=data1;
				    	
				    	var json = JSON.stringify(dynamicDataSourceMainJson);
				    	console.log("jsonDocumentGeneration: "+json);
				    	
				    	$.ajax({
				            type: "POST",
				            url: "/portal/servlet/service/dDependencySKU1",
				            async: false,
				            data:JSON.stringify(dynamicDataSourceMainJson),
				            contentType: "application/json",
				            success: function (data) {
				            console.log("urlGenerationdata: "+JSON.stringify(data));
				            
				            var dataArr = JSON.parse(data);
					        console.log("dataArr: "+dataArr);
				            
				            if(dataArr.length>0){
				            for(var i=0;i<dataArr.length;i++){
				 	    	   console.log("dataArr: "+dataArr[i]);
				 	    	  console.log("dataArr[i]: "+JSON.stringify(dataArr[i]));
				 	    	  
				 	    	 var pdfFileName="";
			                	
			                	if ( dataArr[i].hasOwnProperty("filename") ) {
				             		  pdfFileName=dataArr[i].filename;
				             	 }
				 	    	  
				 	    	   if ( dataArr[i].hasOwnProperty("documentlink") ) {
				 	    		    var documentlink=dataArr[i].documentlink;
				 	    		   console.log("documentlink: "+documentlink);
				 	    		   /* var s=documentlink.lastIndexOf("/");
				 	    		    var pdfFileName = documentlink.substring(s+1);*/
				 	    		    
				 	    		   if( !isEmpty(col) && !isEmpty(templateName) && templateName!="Select Document" ){
				 	             
				 	    		   document.getElementById("pdfFileName").innerHTML = pdfFileName;
					    		    
					    		   var el = document.getElementById('iframeId');
					   	               el.src = documentlink; // assign url to src property
				 	    		    if(i==0){
				 	    		    break;
				 	    		    }
				 	    		    
				 	    		   }// col check
				 	    		    
				 	    	   } // close here check documentlink
				 	    	   
				 	       } // for close
				            
				            }// len check
				        }
				    	}); // ajax close
				        
				        }
				}); // ajax close
				 
//				    Reset();
				/*    previewDocumentData++;
				 
			 }else{
				 console.log("else more than zero ws call; ");
			 }*/
				 
			 } // ws close
			 
	 } // else close

	}

function isEmpty(str) {
    return (!str || 0 === str.length);
}

function checktemplateAndSourceForPreview(){
	
	console.log("variablecheck: "+"templateName: "+templateName+"templateName1: "+templateName1);
	if( !isEmpty(templateName) & templateName!="Select Document"){
		
		console.log("if true");
		// show modalClass
//		 $('.modalClass').show();
//		$('.modalClass').attr('disabled',true);
		//alert("true");
		document.getElementById("previewBtn").disabled = false;
		document.getElementById("list").disabled = false;
		
	}else{
		console.log("else true");
		// not show
//		 $('.modalClass').hide();
//		$('.modalClass').attr('disabled',false);
		//alert("false");
		document.getElementById("previewBtn").disabled = true;
		document.getElementById("list").disabled = true;
	}
	
}

/*$("#trigger").click(function(){
    $("#dialog").dialog();
  });*/ 

//............ end document preview code here..........




// end ........dashboard code here..............................


//start ........Delivery Options code here..............................



function getIdSelectdeliveryOption(){	

	console.log("inside_getIdSelectdeliveryOption : ");
	
	var values = $('#example2').val();
	console.log("deliveryOption_getIdSelectdeliveryOption: "+values);
	
	console.log("lengthgetIdSelectdeliveryOption: "+values.length);
	
	
	if(values.length>0){
	for(var i=0;i<values.length;i++){
		var a=values[i];
		console.log("a_getIdSelectdeliveryOption: "+a);
		
		if(a=="2"){
			console.log("download_getIdSelectdeliveryOption");
			
			setDynamicMultipleDropdownList();
			$('#multipleGetDropdownList').show();
			/*var status = this.checked;
			console.log("statusDropdown: "+status);*/
			
		}else{
			if(a=="3"){
				console.log("email_getIdSelectdeliveryOption");
				
				setDynamicMultipleDropdownList();
				$('#multipleGetDropdownList').show();
			}
		}
		
	}  // for close 
	
	}else{
		 $('#multipleGetDropdownList').hide();
	}
	
}




/*function getdeliveryOptionForEachDataSource(){	

	var values = $('#example2').val();
	console.log("deliveryOption: "+values);
	
	console.log("length: "+values.length);
	
	var col = [];
	
	if(values.length>0){
		
	for(var i=0;i<values.length;i++){
		var a=values[i];
		console.log("a: "+a);
		
		if (col.indexOf(a) === -1) {
            col.push(a);
            
        }
		
	} // for close 
	console.log("col: "+col);
	
	}
	
	return col;
	
}
*/


//end ........Delivery Options code here..............................

/*$("#newDocumentIdShow").click(function(){
	 displayNewDocumentPage();
	
	});*/



function BookingApplication(){
		
	// we can pass two parameter in ajax uisng comma.
	
	      var bookingId=document.getElementById("boolinTextId").value;
	      console.log(bookingId);
	//var json = {"email":"viki@gmail.com","group":"G1","EventId":"3","EventName":"Welcomeevent","Primery_key":"start_date","SFObject":"","Primery_key_value":""};
		$.ajax({
	        type: "POST",
	        url: "/portal/bookingServlet",
	        async: false,
			data : {
				'bookingId':bookingId
			},
	        success: function (data) {
	        	console.log("bookingservletAjax: "+data);
	        	
	        	location.reload();
	        	
	    }
	}); // ajax close

	}


//..... folder code here.......................................................

var jsonFolder={};
function folderCode() {
	
	jsonFolder.group=group;
	jsonFolder.Email=Email;
	
	var json = JSON.stringify(jsonFolder);
	
	$.ajax({
        type: "POST",
        
        beforeSend: function(){
        	$('#load').css("visibility", "visible");
          },
        
//        url: "/portal/getFolderData",
        url: "/portal/getFolderDataNewCode",
//        async: false,
		data : {
			'getFolderList':json
		},
        success: function (data) {

        	var jsonStr = data;
        	
        	console.log("getFolderDataNewCode: "+jsonStr);
        	
        	var isJsonValid=IsJsonString(jsonStr);
			if(isJsonValid){
			jsonStr = JSON.parse(jsonStr);
        	
			var tableheader="";

			Object
			.keys(jsonStr)
			.forEach(
					function(key) {
						
						console.log("key: " + key);
						tableheader=tableheader+'<div class="col-md-2 folder-box-sec"><a href="#newFileData" data-toggle="tab"><div class="folder-box"><i class="fa fa-folder" aria-hidden="true"></i>&nbsp;&nbsp;<span>'+key+'</span></div></a></div>';
						/*var dynamicKey = jsonStr[key];
						console.log("dynamicKey: "+ JSON.stringify(dynamicKey));*/
						
					});
			
			document.getElementById("tableHeaderFolder").innerHTML = tableheader;
			
			
			
			Object
			.keys(jsonStr)
			.forEach(
					function(key) {
						
						console.log("key: " + key);
						var dynamicKey = jsonStr[key];
						console.log("dynamicKey: "+ JSON.stringify(dynamicKey));
						
						var col = [];
						for(var i=0;i<dynamicKey.length;i++){
							
							for (var key in dynamicKey[i]) {
				                if (col.indexOf(key) === -1) {
				                    col.push(key);
				                    
				                }
				            }// check key here 
							
						} // for inside dynamic keys
						console.log("insidecol: "+col);
						
						// take another loop for body 
						var headerInsideData="";
						
						for (var k = 0; k < col.length; k++) {
							headerInsideData=headerInsideData+'<th>'+col[k]+'</th>';
							
				        }// headerInsideData close
						    document.getElementById("headerInsideData").innerHTML = '<tr>'+headerInsideData+'</tr>';
						
						    var trtablebody="";
						    for (var i = 0; i < dynamicKey.length; i++) {
					        	var tablebody="";
								
					        	 for (var j = 0; j < col.length; j++) {
					        		var data= dynamicKey[i][col[j]];
//					        		console.log("data_j: "+data);
					        		
					        		tablebody=tablebody+'<td>'+data+'</td>';
					        		
					        	 }
					        	 trtablebody = trtablebody + '<tr>'+tablebody+' <td><div class="dropdown"><button class="btn btn-defualt dropdown-toggle" type="button" data-toggle="dropdown"><span><i class="fa fa-ellipsis-h" aria-hidden="true"></i></span></button><ul class="dropdown-menu"><li><a href="#">Rename</a></li><li><a href="#">Delete</a></li><li><a href="#">Download</a></li><li><a href="#">Share URL</a></li></ul></div></td></tr>';
					        }
						    
						    document.getElementById("bodyInsideData").innerHTML = trtablebody;
						    
						
						
					});
			
    }// folder json check
        },
        complete: function(){
      	  $('#load').css("visibility", "hidden");
      	  }
			
}); // ajax close
	

	
}


//.............. folder code end here..........................................



function displaysettingtabsbyrolefun(){
	var admin;
	$.ajax({
        type: "GET",
        url: "/portal/servlet/service/getserviceid?email="+Email,
        async: false,
        success: function (data) {
        	console.log("data "+data)
        	var datajson=JSON.parse(data);
        	if(datajson.hasOwnProperty("admin")){
        		admin=datajson.admin;
        		var optionsli="";
        		if(admin=="1"){
        			// optionsli='<li class="active"><a href="#password" data-toggle="tab" aria-expanded="true">Change Password</a></li><li class=""><a href="#biling" data-toggle="tab" aria-expanded="false">Biling</a></li><li class=""><a href="#profilenew" data-toggle="tab" aria-expanded="false">Profile</a></li><li class=""><a href="#provisional" data-toggle="tab" aria-expanded="false">Provisional</a></li>';
        			 optionsli='<li class="active"><a href="#provisional" data-toggle="tab" aria-expanded="false">Provisional</a></li>';

        			 var setiframe=  '<iframe frameborder="0"  src="http://bizlem.io:8082/portal/servlet/service/config.serviceUrl?name='+datajson.serviceid+'&display=1" style="width: 100%; height: 400px;"></iframe>';              
        		document.getElementById("provisional").innerHTML=setiframe;
        		}else{
        			//optionsli='<li class="active"><a href="#password" data-toggle="tab" aria-expanded="true">Change Password</a></li><li class=""><a href="#profilenew" data-toggle="tab" aria-expanded="false">Profile</a></li>';
        		}
        		document.getElementById("displaysettingtabsbyrole").innerHTML=optionsli;
        	}
        	
        }
	});
}

function displayNewDocumentPage(){
	
	var getNewDashboardDocument='<a target="_blank" class="btn btn-success dropdown-toggle" href="http://bizlem.io:8082/portal/servlet/service/DoctigerCreation?group='+group+'#data-source">New Create Template</a>';
	
	document.getElementById("newDocumentIdShow").innerHTML = getNewDashboardDocument;

}

function datasourceResetCloseModal(){
	
	$('option', $('#list')).each(function(element) {
        $(this).removeAttr('selected').prop('selected', false);
      });
	
}

// report js start here........................

function documenttrackingMonthDropdown(){
	
	for(y = 2019; y <= 2500; y++) {
        var optn = document.createElement("OPTION");
        optn.text = y;
        optn.value = y;
        
        // if year is 2015 selected
        /*if (y == 2015) {
            optn.selected = true;
        }*/
        
        document.getElementById('year').options.add(optn);
}
	
}

function documenttrackingYearDropdown(){
	
	var d = new Date();
	var monthArray = new Array();
	monthArray[1] = "January";
	monthArray[2] = "February";
	monthArray[3] = "March";
	monthArray[4] = "April";
	monthArray[5] = "May";
	monthArray[6] = "June";
	monthArray[7] = "July";
	monthArray[8] = "August";
	monthArray[9] = "September";
	monthArray[10] = "October";
	monthArray[11] = "November";
	monthArray[12] = "December";
	for(m = 1; m <= 12; m++) {
	    var optn = document.createElement("OPTION");
	    optn.text = monthArray[m];
	    // server side month start from one
	    optn.value = (m);
	   /* optn.value = (m+1);*/
	 
	    // if june selected
	    /*if ( m == 5) {
	        optn.selected = true;
	    }*/
	 
	    document.getElementById('month').options.add(optn);
	}
	
	
}


$(document).ready(function() {
	documenttrackingMonthDropdown();
	documenttrackingYearDropdown();
	
	var yearDefaultValue=	document.getElementById('year').value;
	  console.log("yearDefaultValue: "+yearDefaultValue);
	  
	  var monthDefaultValue=	document.getElementById('month').value;
	  console.log("monthDefaultValue: "+monthDefaultValue);
	  
	  if(yearDefaultValue=="Select Year" && monthDefaultValue=="Select Month"){
	  
	  
	  var today = new Date();
	  var month = today.getMonth()+1; 
	   console.log("month: "+month);
	  var year = today.getFullYear(); 
	    console.log("year: "+year); //monthyearId
	    
	    document.getElementById('monthyearId').innerHTML="Month / Year : "+month+" / "+year;
	    
	    
	    
	  }
	  
//	  getDocumentTrackingDataOnLoad();
	  
		   
	  

  });


/*function getDocumentTrackingData(){
	
	$.ajax({
        type: "GET",
        url: "/portal/servlet/service/getDocumentTrackingData?email="+Email,
        async: false,
        success: function (data) {
        	console.log("data "+data)
        	
        	var isJsonValid=IsJsonString(data);
			if(isJsonValid){
				
				var datajson=JSON.parse(data);
				
				var allDataBody="";
				var j=0;
				
				Object.keys(datajson).forEach(function(key) {
							
							console.log("datajson_key: " + key);
							
							var dynamicKey = datajson[key];
							console.log("dynamicKey: "+ JSON.stringify(dynamicKey));
							
							
							
							for(var i=0;i<dynamicKey.length;i++){
								var insidejSonObj=dynamicKey[i];
								
								var document_url="";
								var GenerationDate="";
								var documentName="";
								
								if(insidejSonObj.hasOwnProperty("document_url")){
									document_url=insidejSonObj.document_url;
									
									 var s=document_url.lastIndexOf("/");
				 	    		     documentName = document_url.substring(s+1);
									
								}if(insidejSonObj.hasOwnProperty("GenerationDate")){
									GenerationDate=insidejSonObj.GenerationDate;
								}
								j++;
								console.log("j: "+j);
								allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td><img src="/portal/apps/DoctigerNewUICSSAndJS/img/doublecheck1.png" width="20" height="20"></td><td><img src="/portal/apps/DoctigerNewUICSSAndJS/img/doublecheck1.png" width="20" height="20"></td><td></td><td></td><td></td><td></td><td></td><td></td><td>docx</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
								
								
							} // for close
							
							
							
						});
				 document.getElementById('documenttackingBodyId').innerHTML='<tr style="font-weight: bold; height: 50px;"><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td></tr>'+allDataBody;
				
			} // jsonValid
        	
        }
	});
	
}
*/

function daysInMonth (month, year) { 
    return new Date(year, month, 0).getDate(); 
}
function custom_sort(a, b) {
    return new Date(a.GenerationDate).getTime() - new Date(b.GenerationDate).getTime();
}

var documentTrackingData;
function getDocumentTrackingDataOnLoad(){
	
//	$('#load').show();
	
	$.ajax({
        type: "GET",
        beforeSend: function(){
//            $('.ajax-loader').css("visibility", "visible");
        	$('#load').css("visibility", "visible");
          },
//        url: "/portal/getDocumentTrackingNew?email="+Email,
        url: "/portal/getDocumentScriptData?email="+Email,
//        async: false,
        success: function (data) {
        	console.log("getDocumentTrackingDataOnLoad: "+data)
        	
        	var isJsonValid=IsJsonString(data);
			if(isJsonValid){
				
				var datajson=JSON.parse(data);
				
				if(datajson.hasOwnProperty("documentTrackingData")){
					 documentTrackingData=datajson.documentTrackingData;
					documentTrackingData.sort(custom_sort);
					
					//console.log("Sorted_array  :: "+JSON.stringify(documentTrackingData));
					
					var allDataBody="";
					var j=0;

					if(documentTrackingData.length>0){
					
					for(var i=documentTrackingData.length-1;i>=0;i--){
						var insidejSonObj=documentTrackingData[i];
						//console.log("insidejSonObj: "+insidejSonObj);
						var document_url="";
						var GenerationDate="";
						var documentName="";
						var doctype="";
						var mailStatus="";
						var noOfViewsMail="";
						var noOfViewsDocument="";
						var documentStatus="";
						var lastViewsByDocument="";
						var uniqueView="";
						var uniqueViewMail="";
					
						if(insidejSonObj.hasOwnProperty("documentname")){
							documentName=insidejSonObj.documentname;
						}if(insidejSonObj.hasOwnProperty("documentExtension")){
							doctype=insidejSonObj.documentExtension;
						}if(insidejSonObj.hasOwnProperty("mailStatus")){
							mailStatus=insidejSonObj.mailStatus;
						}if(insidejSonObj.hasOwnProperty("noOfViewsMail")){
							noOfViewsMail=insidejSonObj.noOfViewsMail;
						}if(insidejSonObj.hasOwnProperty("noOfViewsDocument")){
							noOfViewsDocument=insidejSonObj.noOfViewsDocument;
						}if(insidejSonObj.hasOwnProperty("documentStatus")){
							documentStatus=insidejSonObj.documentStatus;
						}if(insidejSonObj.hasOwnProperty("lastViewsByDocument")){
							lastViewsByDocument=insidejSonObj.lastViewsByDocument;
						}if(insidejSonObj.hasOwnProperty("uniqueView")){
							uniqueView=insidejSonObj.uniqueView;
						}if(insidejSonObj.hasOwnProperty("uniqueViewMail")){
							uniqueViewMail=insidejSonObj.uniqueViewMail;
						}
						
						if(insidejSonObj.hasOwnProperty("document_url")){
							document_url=insidejSonObj.document_url;
							
							/* var s=document_url.lastIndexOf("/");
		 	    		     documentName = document_url.substring(s+1);
		 	    		     
		 	    		    if(documentName.includes(".docx")){
		 	    		    	documentName=documentName.substring(0,documentName.indexOf(".docx"));
		 	    		    	doctype="docx";
		 	    			}else if(documentName.includes(".pdf")){
		 	    				      documentName=documentName.substring(0,documentName.indexOf(".pdf"));
		 	    				     doctype="pdf";
		 	    			}*/
							
						}
						
						if(insidejSonObj.hasOwnProperty("GenerationDate")){
							GenerationDate=insidejSonObj.GenerationDate;
							
							if(GenerationDate.lastIndexOf(".")!=-1){
								 GenerationDate=GenerationDate.substring(0, GenerationDate.lastIndexOf("."));
								
								if(GenerationDate.includes("T1")){
									var beforeData=GenerationDate.substring(0,GenerationDate.indexOf("T1"));
									var afterData=GenerationDate.substring(GenerationDate.indexOf("T1")+1);
									
									 GenerationDate=beforeData+" "+afterData;
								}
								
							}
							
						}
						
						j++;
						
						var mailImageShow="";
						var documentImageShow="";
						
						if( mailStatus=="open" ){
							mailImageShow='<img src="/portal/apps/DoctigerNewUICSSAndJS/img/doublecheck1.png" width="20" height="20">';
							
							//allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td><img src="/portal/apps/DoctigerNewUICSSAndJS/img/doublecheck1.png" width="20" height="20"></td><td><img src=""></td><td>'+noOfViewsMail+'</td><td></td><td></td><td></td><td></td><td></td><td>'+doctype+'</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
						}
						else{
							//allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>'+doctype+'</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
						}
						
                        if(documentStatus=="open"){
                        	documentImageShow='<img src="/portal/apps/DoctigerNewUICSSAndJS/img/doublecheck1.png" width="20" height="20">';
                        	//allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td><img src=""></td><td><img src="/portal/apps/DoctigerNewUICSSAndJS/img/doublecheck1.png" width="20" height="20"></td><td></td><td>'+noOfViewsDocument+'</td><td></td><td></td><td></td><td></td><td>'+doctype+'</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
						}else{
							//allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>'+doctype+'</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
						}
                        
//                        allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td>'+mailImageShow+'</td><td>'+documentImageShow+'</td><td>'+noOfViewsMail+'</td><td>'+noOfViewsDocument+'</td><td></td><td></td><td></td><td></td><td>'+doctype+'</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
                        allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td>'+mailImageShow+'</td><td>'+documentImageShow+'</td><td>'+noOfViewsMail+'</td><td>'+noOfViewsDocument+'</td><td></td><td>'+lastViewsByDocument+'</td><td>'+uniqueViewMail+'</td><td>'+uniqueView+'</td><td>'+doctype+'</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
						
					} // for close
					
//					document.getElementById('documenttackingBodyId').innerHTML='<tr style="font-weight: bold; height: 50px;"><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td></tr>'+allDataBody;
					document.getElementById('documenttackingBodyId').innerHTML='<tr style="font-weight: bold; height: 50px;"><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td></tr>'+allDataBody;
					
//					$('#load').hide();
					} // length check
					
					/*else{
						
						if(datajson.hasOwnProperty("status")){
							var status=datajson.status;
							
							allDataBody=allDataBody+status;
							document.getElementById('documenttackingBodyId').innerHTML='<tr style="font-weight: bold; height: 50px;"><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td></tr>'+allDataBody;
							
						}
						
					}*/
				} // documentTrackingData close check
				
				
			} // jsonValid
        	
        },
          complete: function(){
//        	    $('.ajax-loader').css("visibility", "hidden");
        	  $('#load').css("visibility", "hidden");
        	  }
	});
	
}

function getDocumentTrackingDataOnClick(month,year){
	
  console.log("getDocumentTrackingDataOnClick: "+documentTrackingData);	
					
					var allDataBody="";
					var j=0;

					var fromTime = new Date(year+"-"+month+"-"+"1"+" "+"00:00:00.000").getTime();
					console.log("fromTime: "+fromTime);
					var toTime = new Date(year+"-"+month+"-"+daysInMonth(month, year)+" "+"23:59:59.999").getTime();
					console.log("toTime: "+toTime);
					var filteredDates = [];
					
					for (i in documentTrackingData) {

						  var row = documentTrackingData[i];
						 var date = new Date(row["GenerationDate"]);

						  if (date.getTime() >= fromTime && date.getTime() <= toTime) {
						    filteredDates.push(row);
						  }
						}
					
					console.log("filteredDates: "+JSON.stringify(filteredDates));
					
					if(filteredDates.length>0){
					
					for(var i=filteredDates.length-1;i>=0;i--){
						var insidejSonObj=filteredDates[i];
						
						var document_url="";
						var GenerationDate="";
						var documentName="";
						var doctype="";
						var mailStatus="";
						var noOfViewsMail="";
						var noOfViewsDocument="";
						var documentStatus="";
						
						var lastViewsByDocument="";
						var uniqueView="";
						var uniqueViewMail="";
					
						if(insidejSonObj.hasOwnProperty("documentname")){
							documentName=insidejSonObj.documentname;
						}if(insidejSonObj.hasOwnProperty("documentExtension")){
							doctype=insidejSonObj.documentExtension;
						}if(insidejSonObj.hasOwnProperty("mailStatus")){
							mailStatus=insidejSonObj.mailStatus;
						}if(insidejSonObj.hasOwnProperty("noOfViewsMail")){
							noOfViewsMail=insidejSonObj.noOfViewsMail;
						}if(insidejSonObj.hasOwnProperty("noOfViewsDocument")){
							noOfViewsDocument=insidejSonObj.noOfViewsDocument;
						}if(insidejSonObj.hasOwnProperty("documentStatus")){
							documentStatus=insidejSonObj.documentStatus;
						}
						if(insidejSonObj.hasOwnProperty("lastViewsByDocument")){
							lastViewsByDocument=insidejSonObj.lastViewsByDocument;
						}if(insidejSonObj.hasOwnProperty("uniqueView")){
							uniqueView=insidejSonObj.uniqueView;
						}if(insidejSonObj.hasOwnProperty("uniqueViewMail")){
							uniqueViewMail=insidejSonObj.uniqueViewMail;
						}
						
						if(insidejSonObj.hasOwnProperty("document_url")){
							document_url=insidejSonObj.document_url;
							
							/* var s=document_url.lastIndexOf("/");
		 	    		     documentName = document_url.substring(s+1);
		 	    		     
		 	    		    if(documentName.includes(".docx")){
		 	    		    	documentName=documentName.substring(0,documentName.indexOf(".docx"));
		 	    		    	doctype="docx";
		 	    			}else if(documentName.includes(".pdf")){
		 	    				      documentName=documentName.substring(0,documentName.indexOf(".pdf"));
		 	    				     doctype="pdf";
		 	    			}*/
							
						}if(insidejSonObj.hasOwnProperty("GenerationDate")){
							GenerationDate=insidejSonObj.GenerationDate;
							
							if(GenerationDate.lastIndexOf(".")!=-1){
								 GenerationDate=GenerationDate.substring(0, GenerationDate.lastIndexOf("."));
								
								if(GenerationDate.includes("T1")){
									var beforeData=GenerationDate.substring(0,GenerationDate.indexOf("T1"));
									var afterData=GenerationDate.substring(GenerationDate.indexOf("T1")+1);
									
									 GenerationDate=beforeData+" "+afterData;
								}
								
							}
							
						}
						
						j++;
						
						var mailImageShow="";
						var documentImageShow="";
						
						if( mailStatus=="open" ){
							mailImageShow='<img src="/portal/apps/DoctigerNewUICSSAndJS/img/doublecheck1.png" width="20" height="20">';
							
							//allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td><img src="/portal/apps/DoctigerNewUICSSAndJS/img/doublecheck1.png" width="20" height="20"></td><td><img src=""></td><td>'+noOfViewsMail+'</td><td></td><td></td><td></td><td></td><td></td><td>'+doctype+'</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
						}
						else{
							//allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>'+doctype+'</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
						}
						
                        if(documentStatus=="open"){
                        	documentImageShow='<img src="/portal/apps/DoctigerNewUICSSAndJS/img/doublecheck1.png" width="20" height="20">';
                        	//allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td><img src=""></td><td><img src="/portal/apps/DoctigerNewUICSSAndJS/img/doublecheck1.png" width="20" height="20"></td><td></td><td>'+noOfViewsDocument+'</td><td></td><td></td><td></td><td></td><td>'+doctype+'</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
						}else{
							//allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td>'+doctype+'</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
						}
                        
                       // allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td>'+mailImageShow+'</td><td>'+documentImageShow+'</td><td>'+noOfViewsMail+'</td><td>'+noOfViewsDocument+'</td><td></td><td></td><td></td><td></td><td>'+doctype+'</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
                        allDataBody=allDataBody+'<tr style="height: 50px;"><td>'+j+'</td><td>'+documentName+'</td><td></td><td>'+GenerationDate+'</td><td>'+mailImageShow+'</td><td>'+documentImageShow+'</td><td>'+noOfViewsMail+'</td><td>'+noOfViewsDocument+'</td><td></td><td>'+lastViewsByDocument+'</td><td>'+uniqueViewMail+'</td><td>'+uniqueView+'</td><td>'+doctype+'</td><td></td><td><a href="'+document_url+'" class="btn btn-primary btn-sm">View Document</a></td></tr>';
					} // for close
					
					//document.getElementById('documenttackingBodyId').innerHTML='<tr style="font-weight: bold; height: 50px;"><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td></tr>'+allDataBody;
					document.getElementById('documenttackingBodyId').innerHTML='<tr style="font-weight: bold; height: 50px;"><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td></tr>'+allDataBody;
					} // length check
					
					else{
							document.getElementById('documenttackingBodyId').innerHTML='<tr style="font-weight: bold; height: 50px;"><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col"><i class="fa fa-envelope" style="color: #ff0000;"></i>&nbsp;Mail</td><td scope="col"><i class="fa fa-file" style="color: #169169;"></i>&nbsp;Document</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td><td scope="col">&nbsp;</td></tr><tr style="height: 50px;" align="center"><p align="center">No Data Available</p></tr>';
					}
}


$('#month').click(function () {
	
	
	
	var getMonthTexhName = document.getElementById("month");
	var monthTextData=getMonthTexhName.options[getMonthTexhName.selectedIndex].text;
	 console.log("monthTextData: "+monthTextData);
	 
	 var monthValueData=getMonthTexhName.options[getMonthTexhName.selectedIndex].value;
	 console.log("monthValueData: "+monthValueData);
	 
	  var getYearTexhName = document.getElementById("year");
		var yearData=getYearTexhName.options[getYearTexhName.selectedIndex].text;
	 
		 console.log("yearTextData: "+yearData);
		
	 if(monthValueData!="Select Month" & yearData!="Select Year"){
		 getDocumentTrackingDataOnClick(monthValueData,yearData);
	 }else{
		 
		 if(monthValueData=="Select Month"){
//			 alert("please select month");
		 }
	 }
	 
	});


$('#year').click(function () {
	 
	var getMonthTexhName = document.getElementById("month");
	var monthTextData=getMonthTexhName.options[getMonthTexhName.selectedIndex].text;
	 console.log("monthTextData: "+monthTextData);
	 
	 var monthValueData=getMonthTexhName.options[getMonthTexhName.selectedIndex].value;
	 console.log("monthValueData: "+monthValueData);
		
	 var getYearTexhName = document.getElementById("year");
		var yearData=getYearTexhName.options[getYearTexhName.selectedIndex].text;
		 console.log("yearTextData: "+yearData);
		 
		 if(monthValueData!="Select Month" & yearData!="Select Year"){
			 getDocumentTrackingDataOnClick(monthValueData,yearData);
		 }else{
			 if(yearData=="Select Year"){
//				 alert("please select Year");
			 }
		 }
		 
});


// report js end here..............................
//approvel js start here..............................

function Templateapprovallist(){
	console.log("Email: "+Email+"group "+group)

	$.ajax({
        type: "GET",
        url: "/portal/servlet/service/getpendinApprovalltemplate?email="+Email+"&group="+group,
        async: false,
        success: function (data) {
        	console.log("data "+data)
        	var json =JSON.parse(data);
        	var TemplateList=[];
        	console.log(json.hasOwnProperty("TemplateList"));
        	if(json.hasOwnProperty("TemplateList")){
            	console.log("data "+data)

        		TemplateList=json.TemplateList;
        		var tbodydata='<tr> <th>Sr No.</th><th class="clause_name">Template Name</th><th>View</th><th colspan="3" align="center" class="text-center action-column">Approval</th></tr>';
        		for(var i=0; i<TemplateList.length; i++){
        			var sub=TemplateList[i];
        			tbodydata=tbodydata+'<tr><td>'+i+'</td> <td>'+sub.Template+'</td><td><a href ="'+sub.TemplateUrl+'">Download</a></td><td class="text-center"><button class="btn btn-success btn-sm" path="'+sub.TemplatePath+'" type="template" onclick="ApproveAll(this)"><i class="fa fa-check-circle"></i>&nbsp;Approved</button></td><td class="text-center"><button class="btn btn-danger btn-sm" path="'+sub.TemplatePath+'" type="template" onclick="RejectAll(this)"><i class="fa fa-times"></i>&nbsp;Not Approved</button></td><td class="text-center"><button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#template-request-change"><i class="fa fa-edit"></i>&nbsp;Request Change</button></td></tr>';
        		}
            	console.log("tbodydata "+tbodydata)
        		document.getElementById("templateapprovaltbody").innerHTML=tbodydata;
        	}
        	
        }
	});
}


function MailTemplateapprovallist(){
	console.log("Email: "+Email+"group "+group)
	$.ajax({
        type: "GET",
        url: "/portal/servlet/service/getpendinApprovallmailtemplate?email="+Email+"&group="+group,
        async: false,
        success: function (data) {
        	console.log("data: "+data)
        	var json =JSON.parse(data);
        	var TemplateList=[];
        	console.log(json.hasOwnProperty("TemplateList"));
        	if(json.hasOwnProperty("TemplateList")){
        		TemplateList=json.TemplateList;
        		var tbodydata='<tr> <th>Sr No.</th><th class="clause_name">MailTemplate Name</th><th>View</th><th colspan="3" align="center" class="text-center action-column">Approval</th></tr>';
        		for(var i=0; i<TemplateList.length; i++){
        			var sub=TemplateList[i];
        			tbodydata=tbodydata+'<tr> <td>'+i+'</td> <td>'+sub.MailTemplate+'</td><td class="text-center"><button type="button" class="btn btn-info btn-sm" to="'+sub.To+'" cc="'+sub.Cc+'" bcc="'+sub.Bcc+'" subject="'+sub.Subject+'"  body="'+sub.Body+'" onclick="displaymaildata(this)"  data-toggle="modal" data-target="#mail-view-change"><i class="fa fa-edit"></i>&nbsp;View</button></td><td class="text-center"><button class="btn btn-success btn-sm" path="'+sub.MailTemplatePath+'" type="mailtemplate" onclick="ApproveAll(this)><i class="fa fa-check-circle"></i>&nbsp;Approved</button></td><td class="text-center"><button class="btn btn-danger btn-sm" path="'+sub.MailTemplatePath+'" type="mailtemplate"  onclick="RejectAll(this)"><i class="fa fa-times"></i>&nbsp;Not Approved</button></td><td class="text-center"><button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#mail-request-change"><i class="fa fa-edit"></i>&nbsp;Request Change</button></td></tr>';
        		}
        		
            	console.log("tbodydata "+tbodydata)
        		document.getElementById("mailtemplateapprovaltbody").innerHTML=tbodydata;
        	}
        	
        }
	});
}




function clauseapprovallist(){
	console.log("Email: "+Email+"group "+group)

	$.ajax({
        type: "GET",
        url: "/portal/servlet/service/getpendinApprovallclause?email="+Email+"&group="+group,
        async: false,
        success: function (data) {
        	console.log("data: "+data)

        	var json =JSON.parse(data);
        	clauseapproveljson=JSON.parse(data);
        	var allcls=[];
        	console.log(json.hasOwnProperty("allcls"))

        	if(json.hasOwnProperty("allcls")){
        		allcls=json.allcls;
        		var tbodydata='<tr> <th>Sr No.</th><th class="clause_name">Clause Name</th><th>View</th><th colspan="3" align="center" class="text-center action-column">Approval</th></tr>';
        		for(var i=0; i<allcls.length; i++){
        			var sub=allcls[i];
        			tbodydata=tbodydata+'<tr> <td>'+i+'</td> <td>'+sub.clausename+'</td><td class="text-center"><button type="button" class="btn btn-info btn-sm"  path="'+sub.clausenodepath+'" type="clause"  clausename="'+sub.clausename+'" onclick="displayclauseapproval(this)" data-toggle="modal" data-target="#clause-view-change"><i class="fa fa-edit"></i>&nbsp;View</button></td><td class="text-center"><button class="btn btn-success btn-sm" path="'+sub.clausenodepath+'" type="clause" onclick="ApproveAll(this)"><i class="fa fa-check-circle"></i>&nbsp;Approved</button></td><td class="text-center"><button class="btn btn-danger btn-sm" path="'+sub.clausenodepath+'" type="clause" onclick="RejectAll(this)"><i class="fa fa-times"></i>&nbsp;Not Approved</button></td><td class="text-center"><button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#clause-request-change"><i class="fa fa-edit"></i>&nbsp;Request Change</button></td></tr>';
        		}
        		document.getElementById("clasueapprovaltbody").innerHTML=tbodydata;
        	}
        	
        }
	});
}

function ApproveAll(param){
	var $this = $(param);
	console.log("thisValue: "+$this);
	console.log("stringify: "+JSON.stringify($this));

	var path = $this.attr("path");
	var type = $this.attr("type");
	console.log("path: "+path+" type  "+type);

	var sendjson={};
	sendjson["nodepath"]=path;
	sendjson["email"]=Email;
	
	$.ajax({
        type: "POST",
        url: "/portal/servlet/service/approvealltype",
        async: false,
        data:JSON.stringify(sendjson),
		contentType:"application/json",
        success: function (data) {
        	console.log("data: "+data)
        	var datajson=JSON.parse(data);
		    			if ( datajson.hasOwnProperty("status")) {
		    			var status=datajson.status;
	                    if(status=="success"){
	                    	if(type=="clause"){
	                    		clauseapprovallist();
	                    	}else if(type=="template"){
	                    		Templateapprovallist();
	                    	} else if(type=="mailtemplate"){
	                    		MailTemplateapprovallist();
	                    	}
	                    	
	                    }	
	                    }
        }
	});
}


function RejectAll(param){
	var $this = $(param);
	console.log("thisValue: "+$this);
	console.log("stringify: "+JSON.stringify($this));

	var path = $this.attr("path");
	var type = $this.attr("type");
	var sendjson={};
	sendjson["nodepath"]=path;
	sendjson["email"]=Email;


	console.log("path: "+path+" type  "+type);
	$.ajax({
        type: "POST",
        url: "/portal/servlet/service/rejectalltype",
        async: false,
        data:JSON.stringify(sendjson),
		contentType:"application/json",
        success: function (data) {
        	console.log("data: "+data)
        	var datajson=JSON.parse(data);
		    			if ( datajson.hasOwnProperty("status")) {
		    			var status=datajson.status;
	                    if(status=="success"){
	                    	if(type=="clause"){
	                    		clauseapprovallist();
	                    	}else if(type=="template"){
	                    		Templateapprovallist();
	                    	} else if(type=="mailtemplate"){
	                    		MailTemplateapprovallist();
	                    	}
	                    	
	                    }	
	                    }
        }
	});
}


function displaymaildata(param){
	console.log("in displaymaildata")	;

	var $this = $(param);
	console.log("thisValue: "+$this);
	console.log("stringify: "+JSON.stringify($this));
	
	var to = "";
	var cc = "";
	var bcc ="";
	var subject = "";
	var body = "";
	
	 to = $this.attr("to");
	 cc = $this.attr("cc");
	 bcc = $this.attr("bcc");
	 subject = $this.attr("subject");
	 body = $this.attr("body");
	 
		console.log("to: "+to+" cc  "+cc);

		console.log("bcc: "+bcc+" subject  "+subject);
		console.log("body : "+body);


		document.getElementById("mail__to").value="";
		document.getElementById("mail__cc").value="";
		document.getElementById("mail__bcc").value="";
		document.getElementById("mail__subject").value="";
		document.getElementById("mail__body").value="";
		
document.getElementById("mail__to").value=to;
document.getElementById("mail__cc").value=cc;
document.getElementById("mail__bcc").value=bcc;
document.getElementById("mail__subject").value=subject;
document.getElementById("mail__body").value=body;
console.log("Done")	;
	
}



function displayclauseapproval(param){
	var $this = $(param);
	console.log("thisValue: "+$this);
	console.log("stringify: "+JSON.stringify($this));
	
	var path = "";
	var clausenameclicked = "";
	var bcc ="";
	var subject = "";
	var body = "";
	
	path = $this.attr("path");
	clausenameclicked = $this.attr("clausename");
	var content="";
	var clsjson= clauseapproveljson;
	console.log("clsjson "+JSON.stringify(clsjson))	;
	var allcls=[];
	if ( clsjson.hasOwnProperty("allcls")) {
		allcls=clsjson.allcls;
		for(var i=0; i<allcls.length; i++){
			var subjson=allcls[i];
			if( subjson.hasOwnProperty("clausename")){
				var clausename=subjson.clausename;
				
				if(clausenameclicked==clausename ){
					//alert("parse for content");
					if(subjson.hasOwnProperty("languages")){
						var languages=subjson.languages;
					for(var j=0; j<languages.length; j++){
						var sublan=languages[j];
						console.log(j+ " sublan: "+JSON.stringify(sublan));
						console.log(j+ " content: "+sublan.content);
						 content=content +'<div class="form-group"> <label>'+sublan.languagename+'</label>  </div><div class="form-group"><label>'+clausename+'</label><textarea class="form-control" name="" value="'+sublan.content+'" rows="3"> '+sublan.content+'</textarea> </div>';
					}
						
					}
					break;
				}
			}
		}
	}	

	document.getElementById("clausecontentview").innerHTML=content;
}
//approvel js end here..............................

$('.options1').click(function(){
    var option = $('.options1:checked').val();
    if (option == 0) {
        $('.radio-option-select').hide();
        $('.right-step-part ul li').css('margin-bottom','5px');
    }else{
    	
    	if (option == 1) {
    	
    		GetDomainNameWelcomeData();
    		
      $('.radio-option-select').show();
      $('.right-step-part ul li').css('margin-bottom','31px');
    	}
    } // else close
    
  }); // on click

  $('.faq li .question').click(function () {
    $(this).find('.plus-minus-toggle').toggleClass('collapsed');
    $(this).parent().toggleClass('active');
  }); // on click

  /*$('.card-deck a').fancybox({
    caption : function( instance, item ) {
      return $(this).parent().find('.card-text').html();
    }
  });*/

  
  $("#list-form-data").change(function () {
      selection = $(this).val();
      if(selection == 2) {
          $('#enter-manually-form-data').modal('show');
      }
      /*if(selection == 1) {
          $('#enter-manually2').modal('show');
      }*/
  });
  

  function GetDomainNameWelcomeData(){

		$.ajax({
		type:'GET',
		url:'/portal/servlet/service/GetDomainNameWelcome',
		async:false,
		success:function(data){
		console.log("getDomainData: "+data);

		var isJsonValid=IsJsonString(data);
		if(isJsonValid){

		var json=JSON.parse(data);

		if ( json.hasOwnProperty("domain_name")) {
		var domain_name= json.domain_name;

		if( domain_name.length>0 ){
		var dataBody="";

		for( var i=0;i<domain_name.length;i++ ){
		var insideObj= domain_name[i];

		var displayText="";
		var NodeText="";

		if ( insideObj.hasOwnProperty("displayText")) {
		displayText= insideObj.displayText;
		}if ( insideObj.hasOwnProperty("NodeText")) {
		NodeText= insideObj.NodeText;
		}
		
		dataBody=dataBody+'<option value="'+NodeText+'">'+displayText+'</option>';

		}// for close

		document.getElementById("domainselectcls").innerHTML = '<option value="Select Domain">Select Domain</option>'+dataBody;


		} // length check close

		} // datasorceList check close

		} // jsonValidCheck

		}
		});


		}
  

  function getsubDomainWelcomeData(){

		$.ajax({
		type:'GET',
		url:'/portal/servlet/service/GetSubDomainNameWelcome?subdomainname='+document.getElementById("domainselectcls").value,
		async:false,
		success:function(data){
		console.log("getsubDomainData: "+data);

		var isJsonValid=IsJsonString(data);
		if(isJsonValid){

		var json=JSON.parse(data);

		if ( json.hasOwnProperty("domainList")) {
		var domain_name= json.domainList;

		if( domain_name.length>0 ){
		var dataBody="";

		for( var i=0;i<domain_name.length;i++ ){
		var insideObj= domain_name[i];

		var nameArr = insideObj.split(',');
		console.log("nameArr: "+nameArr.length);

		for(var i=0;i<nameArr.length;i++){
		var a=nameArr[i];
		dataBody=dataBody+'<option value="'+a+'">'+a+'</option>';
		}

		}// for close

		document.getElementById("domainsubselectcls").innerHTML = '<option value="Select Domain">Select Sub Domain</option>'+dataBody;

		$('#domainsubId').css('display','block');
		
		} // length check close

		} // datasorceList check close

		} // jsonValidCheck

		}
		});
		}
  
  $('#domainselectcls').click(function () {
		 
		var getdomainselectclsTexhName = document.getElementById("domainselectcls");
		var getdomainTextData=getdomainselectclsTexhName.options[getdomainselectclsTexhName.selectedIndex].text;
		 console.log("getdomainTextData: "+getdomainTextData);
		 
		 var getdomainValueData=getdomainselectclsTexhName.options[getdomainselectclsTexhName.selectedIndex].value;
		 console.log("getdomainValueData: "+getdomainValueData);
			 
			 if( getdomainValueData!="Select Domain" ){
				  getsubDomainWelcomeData(); // write inside please select domainname
			 }else{
				 
			 }
			 
	});

  
  $('#domainsubselectcls').click(function () {
		 
		var getdomainselectclsTexhName = document.getElementById("domainsubselectcls");
		var getdomainTextData=getdomainselectclsTexhName.options[getdomainselectclsTexhName.selectedIndex].text;
		 console.log("domainsubselectcls: "+getdomainTextData);
		 
		 var getdomainValueData=getdomainselectclsTexhName.options[getdomainselectclsTexhName.selectedIndex].value;
		 console.log("domainsubselectcls: "+getdomainValueData);
			 
			 if( getdomainValueData!="Select Sub Domain" ){
				 getWelcomeRadioTempData();
			 }else{
				 
			 }
			 
	});
  
  function getWelcomeRadioTempData(){

		$.ajax({
		type:'GET',
		url:'/portal/servlet/service/getTempWelcomeRadio?subDomainname='+document.getElementById("domainsubselectcls").value+'&email=templateadmin@gmail.com',
		async:false,
		success:function(data){
		console.log("getWelcomeRadioTempData: "+data);

		var isJsonValid=IsJsonString(data);
		if(isJsonValid){

		var json=JSON.parse(data);

		if ( json.hasOwnProperty("welcomeData")) {
		var welcomeData= json.welcomeData;

		if( welcomeData.length>0 ){
		var dataBody="";

		for( var i=0;i<welcomeData.length;i++ ){
		     var insideObj= welcomeData[i];
		     
		     if ( insideObj.hasOwnProperty("tempName")) {
		    	 var insideObjInside= insideObj.tempName;
		    	 
		    	 if(i==0){
		    		 dataBody=dataBody+'<label class="mr-2" style="font-size: 16px;"><input type="radio" name="options2" id="radiowiseId'+i+'" value="'+insideObjInside+'" checked onclick="removePdfLInk();">'+insideObjInside+'</label>';
		    	 }else{
		    		 dataBody=dataBody+'<label class="mr-2" style="font-size: 16px;"><input type="radio" name="options2" id="radiowiseId'+i+'" value="'+insideObjInside+'" onclick="removePdfLInk();">'+insideObjInside+'</label>';
		    	 }
		    	
		    	 
		     }
		     
		}// for close

		document.getElementById("radioSingleId").innerHTML = dataBody;

		$('#mainRadioDiv').css('display','block');
		
		} // length check close

		} // datasorceList check close

		} // jsonValidCheck

		}
		});
		}
  
  var typeDataSourceWelcomePage;
  function showDataSourceWelcomePage(){
		
	  var checkedRadioTempValue= $('input[name=options2]:checked').val();
	  
	  var lableValue= $("input[name='options1']:checked").parent('label').text();
	  console.log("lableValue: "+lableValue);
	  
	  lableValue=lableValue.trim();
	  console.log("lableValuetrim: "+lableValue);
	  
	  if(lableValue=="Start with Non Disclosure Agreement"){
      	checkedRadioTempValue="Non Disclosure Agreement";
      	console.log("checkedRadioTempValue_label check: "+checkedRadioTempValue);
      }
	  var groupAdmin="no group";
		var enterManuallyUrl='/portal/servlet/service/getContactOrFields.entermanually?email=templateadmin@gmail.com&group='+groupAdmin+'&templatename='+checkedRadioTempValue+'&templatetype=TemplateLibrary';
		
		typeDataSourceWelcomePage = document.getElementById("list-form-data");
		typeDataSourceWelcomePage=typeDataSourceWelcomePage.options[typeDataSourceWelcomePage.selectedIndex].text;
		
		
		 if(typeDataSourceWelcomePage=="Merge Using Test Data Set"){
			 console.log("typeDataSourceWelcomePage: "+typeDataSourceWelcomePage);
			 
			 typeDataSourceWelcomePage="Enter Manually";
			 
			 clickWelcomePageMergeTest(enterManuallyUrl);
			 
//			 document.getElementById("previewBtnWelcome").disabled = false;
			 $('#previewBtnWelcome').removeAttr("disabled");
			 $('#welcomeGenerateDocId').removeAttr("disabled");
			 
		 }else if(typeDataSourceWelcomePage=="Enter Data in Form"){
			 typeDataSourceWelcomePage="Enter Manually";
			 console.log("checkedRadioTempValue: "+checkedRadioTempValue);
			 console.log("typeDataSourceWelcomePage: "+typeDataSourceWelcomePage);
			 
			 clickOnDatasourceWelcomePage(enterManuallyUrl);
			 
//			 document.getElementById("previewBtnWelcome").disabled = false;
			 $('#previewBtnWelcome').removeAttr("disabled");
			 $('#welcomeGenerateDocId').removeAttr("disabled");
			 
		 }
		
	}
  
  
  function clickOnDatasourceWelcomePage(urlLink){	
	  
		$.ajax({

			url : urlLink,
			type : 'GET',
			async:false,
			success : function(data) {
				console.log("clickOnDatasourceWelcomePage: " +data);
				var jsonStr = data;
				jsonStr = JSON.parse(jsonStr);
				var multipleGetDropdownList='';
				
				if(jsonStr.selectedfields.length>0){
					
				for (var i = 0; i < jsonStr.selectedfields.length; i++) {
					
						multipleGetDropdownList=multipleGetDropdownList+'<div class="form-group"><label class="keynamewelcome">'+jsonStr.selectedfields[i]+'</label><input type="text" name="" class="form-control keyvaluewelcome"></div>';
						
				} // for close
				document.getElementById("enterManuallyWelcomeId").innerHTML =  multipleGetDropdownList;			
				}
				
			} // sucees 

		}); // function on click
		
	}
  var dynamicDataSourceMainJsonWelcome = {};
  
  function clickWelcomePageMergeTest(urlLink){	
	  
		$.ajax({

			url : urlLink,
			type : 'GET',
			async:false,
			success : function(data) {
				console.log("clickOnDatasourceWelcomePage: " +data);
				var jsonStr = data;
				jsonStr = JSON.parse(jsonStr);
				var multipleGetDropdownList='';
				
				if(jsonStr.selectedfields.length>0){
					
					var data = [];
					var dynamicDataSourceFormData = {};
					
				for (var i = 0; i < jsonStr.selectedfields.length; i++) {
					
				    var singleArrayValue=jsonStr.selectedfields[i];
				    
				    dynamicDataSourceFormData[singleArrayValue]=singleArrayValue
				    
				} // for close
				data.push(dynamicDataSourceFormData);
				dynamicDataSourceMainJsonWelcome.data = data;
				
				var json = JSON.stringify(dynamicDataSourceMainJsonWelcome);
				console.log("dynamicDataSourceMainJsonWelcome_mergetest: "+json);
				}
				
			} // sucees 

		}); // function on click
		
	}
  
  
  
  function getDataSourceFormDataDynamicwelcome(){	
		
		var keyname = document.getElementsByClassName("keynamewelcome");
		var keyvalue = document.getElementsByClassName("keyvaluewelcome");
		
		
		var data = [];
		var dynamicDataSourceFormData = {};
		
		
		for (var i = 0; i < keyname.length; i++) {
			var keynameData=keyname[i].innerHTML;
			console.log("keynamewelcomeData: "+keynameData);
			var keyvalueData=keyvalue[i].value;
			console.log("keyvaluewelcomeData: "+keyvalueData);
			
			dynamicDataSourceFormData[keynameData]=keyvalueData;
			
		} // for close
		
		data.push(dynamicDataSourceFormData);
		dynamicDataSourceMainJsonWelcome.data = data;
		
		var json = JSON.stringify(dynamicDataSourceMainJsonWelcome);
		console.log("dynamicDataSourceMainJsonWelcome: "+json);
		
	}
  
  function DocumentGenerationWelcomePage(){
		
	  var checkedRadioTempValue= $('input[name=options2]:checked').val();
	  
    var lableValue= $("input[name='options1']:checked").parent('label').text();
	
    lableValue=lableValue.trim();
	  console.log("lableValuetrim: "+lableValue);
    
	  if(lableValue=="Start with Non Disclosure Agreement"){
      	checkedRadioTempValue="Non Disclosure Agreement";
      	console.log("checkedRadioTempValue_label check: "+checkedRadioTempValue);
      }
	  
	  var col=[];
	  col.push("2");
		
	  if(datasource!="ws"){
	 		 
	 	if( !isEmpty(checkedRadioTempValue) && !isEmpty(Email) ){
	 	
	 		dynamicDataSourceMainJsonWelcome.templateName=checkedRadioTempValue;  
	 		dynamicDataSourceMainJsonWelcome.typeDataSource=typeDataSourceWelcomePage;
	 		dynamicDataSourceMainJsonWelcome.AttachtempalteType="TemplateLibrary";
	 		dynamicDataSourceMainJsonWelcome.Email="templateadmin@gmail.com";
	 		dynamicDataSourceMainJsonWelcome.group="no group";
	 		dynamicDataSourceMainJsonWelcome.multipeDropDown=col;
//	 	dynamicDataSourceMainJson.MailTempName=mailTemplateValue;
	 		dynamicDataSourceMainJsonWelcome.Type="Generation";
	 	
	 	var json = JSON.stringify(dynamicDataSourceMainJsonWelcome);
	 	console.log("jsonDocumentGeneration: "+json);
	 	
	 	$.ajax({
	         type: "POST",
	         url: "/portal/servlet/service/dDependencySKU1",
	         async: false,
	         data:JSON.stringify(dynamicDataSourceMainJsonWelcome),
	         contentType: "application/json",
	         success: function (data) {
	         console.log("urlGenerationdata: "+JSON.stringify(data));
	         
	         var dataArr = JSON.parse(data);
	         
	         if(dataArr.length>0){
	         	var allMultipleLink="";
	         	
	             for(var i=0;i<dataArr.length;i++){
	             	var documentlink="";
	             	var pdfFileName="";
	             	
	             	 if ( dataArr[i].hasOwnProperty("filename") ) {
	             		  pdfFileName=dataArr[i].filename;
	             	 }
	             	
	  	    	   if ( dataArr[i].hasOwnProperty("documentlink") ) {
	  	    		     documentlink=dataArr[i].documentlink;
	  	    		   
	  	    		    /*var s=documentlink.lastIndexOf("/");
	  	    		     pdfFileName = documentlink.substring(s+1);*/
	  	    		   
	  	    		    allMultipleLink= allMultipleLink+'<a href="'+documentlink+'" target="_blank">'+pdfFileName+'</a><br>';
	  	    		     
	  	    	   } // close here check documentlink
	  	    	   
	  	       } // for close
	             
	             if( !isEmpty(col) && !isEmpty(checkedRadioTempValue) ){
	             	 document.getElementById("documentUrlGenerateIdWelcomePage").innerHTML = allMultipleLink;
	             }
	             
	            
	             
	             }// len check
	         
	     }
	 }); // ajax close
	 	

	 	ResetWelcomepageGenerationClick();

	 	}// 
	 	else{
	 		document.getElementById("documentUrlGenerateIdWelcomePage").innerHTML = '<h6 style="color:#FF001B;">Please Select TemplateName</h6>';
	 	}
	 	
	 }// ws check

	 	 else{
	 		 
	 		 if(datasource=="ws"){
	 			 
	 			 var wsJson={};
	 			 
	 				 wsJson.templatename=checkedRadioTempValue;
	 				 wsJson.email="templateadmin@gmail.com";
	 				 wsJson.group="no group";
	 				 wsJson.datasource=datasource;
	 				 wsJson.object=object;
	 				 wsJson.pkvalue=document.getElementById("pkId").value; ;
	 				 console.log(JSON.stringify(wsJson));
	 			$.ajax({
	 			        type: "POST",
	 			        url: "/portal/servlet/service/callSFQuery.data",
	 			        async: false,
	 			        data:JSON.stringify(wsJson),
	 			        contentType: "application/json",
	 			        success: function (data) {
	 			        console.log("urlGenerationdata: "+JSON.stringify(data));
	 			        
	 			        var data1 = [];
	 			        data1.push(JSON.parse(data));
	 			        
	 			       dynamicDataSourceMainJsonWelcome.templateName=checkedRadioTempValue;  
	 			       dynamicDataSourceMainJsonWelcome.typeDataSource=typeDataSourceWelcomePage;
	 			       dynamicDataSourceMainJsonWelcome.AttachtempalteType="TemplateLibrary";
	 			       dynamicDataSourceMainJsonWelcome.Email="templateadmin@gmail.com";
	 			       dynamicDataSourceMainJsonWelcome.group="no group";
	 			       dynamicDataSourceMainJsonWelcome.multipeDropDown=[];
//	 			    	dynamicDataSourceMainJson.MailTempName=mailTemplateValue;
	 			       dynamicDataSourceMainJsonWelcome.Type="Generation";
	 			    	
	 			        dynamicDataSourceMainJsonWelcome.data=data1;
	 			    	
	 			    	var json = JSON.stringify(dynamicDataSourceMainJsonWelcome);
	 			    	console.log("jsonDocumentGeneration: "+json);
	 			    	
	 			    	$.ajax({
	 			            type: "POST",
	 			            url: "/portal/servlet/service/dDependencySKU1",
	 			            async: false,
	 			            data:JSON.stringify(dynamicDataSourceMainJsonWelcome),
	 			            contentType: "application/json",
	 			            success: function (data) {
	 			            console.log("urlGenerationdata: "+JSON.stringify(data));
	 			            
	 			            var dataArr = JSON.parse(data);
	 			            if(dataArr.length>0){
	 			            	var allMultipleLink="";
	 			            	
	 			                for(var i=0;i<dataArr.length;i++){
	 			                	var documentlink="";
	 			                	var pdfFileName="";
	 			                	
	 			                	 if ( dataArr[i].hasOwnProperty("filename") ) {
	 				             		  pdfFileName=dataArr[i].filename;
	 				             	 }
	 			                	
	 			     	    	   if ( dataArr[i].hasOwnProperty("documentlink") ) {
	 			     	    		     documentlink=dataArr[i].documentlink;
	 			     	    		   
	 			     	    		  /*  var s=documentlink.lastIndexOf("/");
	 			     	    		     pdfFileName = documentlink.substring(s+1);*/
	 			     	    		   
	 			     	    		    allMultipleLink= allMultipleLink+'<a href="'+documentlink+'" target="_blank">'+pdfFileName+'</a><br>';
	 			     	    		     
	 			     	    	   } // close here check documentlink
	 			     	    	   
	 			     	    	   
	 			     	    	   
	 			     	       } // for close
	 			                
	 			                if( !isEmpty(col) && !isEmpty(checkedRadioTempValue) ){
	 			               	     document.getElementById("documentUrlGenerateIdWelcomePage").innerHTML = allMultipleLink;
	 			               }
	 			                
	 			                }// len check
	 			            
	 			        }
	 			    	}); // ajax close
	 			        }
	 			}); // ajax close
	 			
	 			ResetWelcomepageGenerationClick();
	 	
	 		 } // ws close
	 		 
	 		 
	 	 }// elsee close 
	  
	 	 
	 }
  
  function ResetWelcomepageGenerationClick() {
	    
	    var domainselectclsDataSource = document.getElementById("domainselectcls");
	    domainselectclsDataSource.selectedIndex = 0;
	    
	    var domainsubselectcls = document.getElementById("domainsubselectcls");
	    domainsubselectcls.selectedIndex = 0;
	    
	    $('option', $('#list-form-data')).each(function(element) {
	        $(this).removeAttr('selected').prop('selected', false);
	      });
	    
	    $('input[type="text"],textarea').val('');  
	    
	   /* $("input:radio[name='options2']").each(function(i) {
	        this.checked = false;
	 });*/
	    
	    $('input[name="options2"]').attr('checked', false);
	    
	    document.getElementById("previewBtnWelcome").disabled = true;
	    document.getElementById("welcomeGenerateDocId").disabled = true;
	    
	}
  
  
  function DocumentPreviewpdfWelcomePage(){
		
       var checkedRadioTempValue= $('input[name=options2]:checked').val();
       
       var lableValue= $("input[name='options1']:checked").parent('label').text();
 	  
       lableValue=lableValue.trim();
 	  console.log("lableValuetrim: "+lableValue);
       
 	  if(lableValue=="Start with Non Disclosure Agreement"){
       	checkedRadioTempValue="Non Disclosure Agreement";
       	console.log("checkedRadioTempValue_label check: "+checkedRadioTempValue);
       }
	  
	  var col=[];
	  col.push("2");
		
	  
		 if(datasource!="ws"){
		if( !isEmpty(checkedRadioTempValue) && !isEmpty(Email) ){
		
			dynamicDataSourceMainJsonWelcome.templateName=checkedRadioTempValue;  
			dynamicDataSourceMainJsonWelcome.typeDataSource=typeDataSourceWelcomePage;
			dynamicDataSourceMainJsonWelcome.AttachtempalteType="TemplateLibrary";
			dynamicDataSourceMainJsonWelcome.Email="templateadmin@gmail.com";
			dynamicDataSourceMainJsonWelcome.group="no group";
			dynamicDataSourceMainJsonWelcome.multipeDropDown=col;
//			dynamicDataSourceMainJson.MailTempName=mailTemplateValue;
			dynamicDataSourceMainJsonWelcome.Type="Preview";
			
			var json = JSON.stringify(dynamicDataSourceMainJsonWelcome);
			console.log("jsonPdfDocumentPreview: "+json);
			
			$.ajax({
		        type: "POST",
		        url: "/portal/servlet/service/dDependencySKU1",
		        async: false,
		        data:JSON.stringify(dynamicDataSourceMainJsonWelcome),
		        contentType: "application/json",
		        success: function (data) {
		        console.log("pdfurlgeneration: "+JSON.stringify(data));
		        
		        /*var dataArr = JSON.parse('[{"documentlink":"http://bizlem.io:8085/Attachment/Temp1_05-Jul-2019_17-13-10-857.pdf","creation_date":"Fri Jul 05 17:13:22 IST 2019","created_by":"viki@gmail.com"}]');*/
		        var dataArr = JSON.parse(data);
		        console.log("dataArr: "+dataArr);
		        
		       var len= dataArr.length;
		       console.log("len: "+len);
		       
		       for(var i=0;i<dataArr.length;i++){
		    	   
		    	   var pdfFileName="";
		    	   if ( dataArr[i].hasOwnProperty("filename") ) {
	             		  pdfFileName=dataArr[i].filename;
	             	 }
		    	   
		    	   if ( dataArr[i].hasOwnProperty("documentlink") ) {
		    		    var documentlink=dataArr[i].documentlink;
		    		   
		    		    /*var s=documentlink.lastIndexOf("/");
		    		    var pdfFileName = documentlink.substring(s+1);*/
		    		    
		    		    if( !isEmpty(col) && !isEmpty(checkedRadioTempValue) ){
		    		    document.getElementById("pdfFileName").innerHTML = pdfFileName;
		    		    
		    		   var el = document.getElementById('iframeId');
		   	               el.src = documentlink; // assign url to src property
		   	            if(i==0){
		 	    		    break;
		 	    		    }
		    		    }// col check
		    	   } // close here check documentlink
		    	   
		       } // for close
		       
		    }
		}); // ajax close
	
		}// check null 
		else{
//			document.getElementById("documentUrlGenerateId").innerHTML = "<h6>Please Select TemplateName And Data Source</h6>";
		}
		
		 }// ws check
			 else{
				 
				 if(datasource=="ws"){
					 
					 var wsJson={};
					 
						 wsJson.templatename=checkedRadioTempValue;
						 wsJson.email="templateadmin@gmail.com";
						 wsJson.group="no group";
						 wsJson.datasource=datasource;
						 wsJson.object=object;
						 wsJson.pkvalue=document.getElementById("pkId").value; ;
						 console.log(JSON.stringify(wsJson));
					$.ajax({
					        type: "POST",
					        url: "/portal/servlet/service/callSFQuery.data",
					        async: false,
					        data:JSON.stringify(wsJson),
					        contentType: "application/json",
					        success: function (data) {
					        console.log("urlGenerationdata: "+JSON.stringify(data));
					        
					        var data1 = [];
					        data1.push(JSON.parse(data));
					        
					        dynamicDataSourceMainJsonWelcome.templateName=checkedRadioTempValue;  
					        dynamicDataSourceMainJsonWelcome.typeDataSource=typeDataSourceWelcomePage;
					        dynamicDataSourceMainJsonWelcome.AttachtempalteType="TemplateLibrary";
					        dynamicDataSourceMainJsonWelcome.Email="templateadmin@gmail.com";
					        dynamicDataSourceMainJsonWelcome.group="no group";
					        dynamicDataSourceMainJsonWelcome.multipeDropDown=[];
//					        dynamicDataSourceMainJsonWelcome.MailTempName=mailTemplateValue;
					        dynamicDataSourceMainJsonWelcome.Type="Preview";
					        dynamicDataSourceMainJsonWelcome.data=data1;
					    	
					    	var json = JSON.stringify(dynamicDataSourceMainJsonWelcome);
					    	console.log("jsonDocumentGeneration: "+json);
					    	
					    	$.ajax({
					            type: "POST",
					            url: "/portal/servlet/service/dDependencySKU1",
					            async: false,
					            data:JSON.stringify(dynamicDataSourceMainJsonWelcome),
					            contentType: "application/json",
					            success: function (data) {
					            console.log("urlGenerationdata: "+JSON.stringify(data));
					            
					            var dataArr = JSON.parse(data);
						        console.log("dataArr: "+dataArr);
					            
					            if(dataArr.length>0){
					            for(var i=0;i<dataArr.length;i++){
					 	    	   console.log("dataArr: "+dataArr[i]);
					 	    	  console.log("dataArr[i]: "+JSON.stringify(dataArr[i]));
					 	    	  
					 	    	 var pdfFileName="";
					 	    	  
					 	    	 if ( dataArr[i].hasOwnProperty("filename") ) {
				             		  pdfFileName=dataArr[i].filename;
				             	 }
					 	    	  
					 	    	   if ( dataArr[i].hasOwnProperty("documentlink") ) {
					 	    		    var documentlink=dataArr[i].documentlink;
					 	    		   console.log("documentlink: "+documentlink);
					 	    		    /*var s=documentlink.lastIndexOf("/");
					 	    		    var pdfFileName = documentlink.substring(s+1);*/
					 	    		    
					 	    		   if( !isEmpty(col) && !isEmpty(checkedRadioTempValue) ){
					 	             
					 	    		   document.getElementById("pdfFileName").innerHTML = pdfFileName;
						    		    
						    		   var el = document.getElementById('iframeId');
						   	               el.src = documentlink; // assign url to src property
					 	    		    if(i==0){
					 	    		    break;
					 	    		    }
					 	    		    
					 	    		   }// col check
					 	    		    
					 	    	   } // close here check documentlink
					 	    	   
					 	       } // for close
					            
					            }// len check
					        }
					    	}); // ajax close
					        
					        }
					}); // ajax close

				 } // ws close
				 
		 } // else close

		}
  
  function removePdfLInk(){
	    
	  console.log("radio change click inside");
	  document.getElementById("documentUrlGenerateIdWelcomePage").innerHTML = "";
	  
  }
  
  function displayLoginUsername(){
	    
	  console.log("login username");
	 
	 
	  if(Email.lastIndexOf("@")!=-1){
	    var userNameDispaly=Email.substring(0, Email.lastIndexOf("@"));
	    document.getElementById("userNameId").innerHTML = userNameDispaly;
	  }
	 
	  
  }
  
  
  $("#stepUlSelect").change(function () {
      selection = $(this).val();
      $('.right-step-part ul').css('display','none');
      if(selection == 1) {
        $('.right-step-part ul.stepUl1').css('display','block');
      }
      if(selection == 2) {
        $('.right-step-part ul.stepUl2').css('display','block');
      }
      if(selection == 3) {
        $('.right-step-part ul.stepUl3').css('display','block');
      }
      if(selection == 4) {
        $('.right-step-part ul.stepUl4').css('display','block');
      }
  });
  
 


$(document).on('click', '.sidebar-menu > li > a', function(){
	  var href = 'UIDoctiger'+$(this).attr('href');
	  console.log("link: "+href);
	  var hrefFull = location.href;
	  var hrefFull = hrefFull.substring(0, hrefFull.lastIndexOf('/'));
	  var hrefFull = hrefFull+'/'+href;
	  location.replace(hrefFull);
	});
  
  
/*document.onreadystatechange = function () {
	  var state = document.readyState
	  if (state == 'interactive') {
	       document.getElementById('welcomeHideId').style.visibility="hidden";
	  } else if (state == 'complete') {
	      setTimeout(function(){
	         document.getElementById('interactive');
	         document.getElementById('load').style.visibility="hidden";
	         document.getElementById('welcomeHideId').style.visibility="visible";
	      },1000);
	  }
	}*/

document.onreadystatechange = function () {
	  var state = document.readyState
	  
	  if (hrefFull1 == 'UIDoctiger#dashboard') {
		  getRecentDocumentData();
		}else if (hrefFull1 == 'UIDoctiger#contactus') {
			selectworkingroupfun("working-group-DropdownClass");
			getFileNameList();
		}else if (hrefFull1 == 'UIDoctiger#reports') {
			getDocumentTrackingDataOnLoad();
			 
		}else{
			
			if (state == 'interactive') {
			       document.getElementById('welcomeHideId').style.visibility="hidden";
			  } else if (state == 'complete') {
			      setTimeout(function(){
			         document.getElementById('interactive');
			         document.getElementById('load').style.visibility="hidden";
			         document.getElementById('welcomeHideId').style.visibility="visible";
			      },1000);
			  }
		}
	 
	  
	  
	}


function getRecentDocumentData(){
	
	$.ajax({
        type: "GET",
        beforeSend: function(){
        	$('#load').css("visibility", "visible");
          },
        url: "/portal/servlet/service/getRecentDocumentsData?email="+Email,
//        async: false,
        success: function (data) {
        	console.log("getRecentDocumentData: "+data)
        	
        	var isJsonValid=IsJsonString(data);
			if(isJsonValid){
				
				var datajson=JSON.parse(data);
				
				if(datajson.hasOwnProperty("recentDocument")){
					var documentTrackingData=datajson.recentDocument;
					documentTrackingData.sort(custom_sort);
					
					var allDataBody="";
					var j=0;

					if(documentTrackingData.length>0){
					
					for(var i=documentTrackingData.length-1;i>=0;i--){
						var insidejSonObj=documentTrackingData[i];
						
						var documentName="";
					
						if(insidejSonObj.hasOwnProperty("documentname")){
							documentName=insidejSonObj.documentname;
						}
						
						j++;
                        
//                        allDataBody=allDataBody+'<tr><td>'+documentName+'</td><td></td></tr>';
						allDataBody=allDataBody+'<tr><td>'+documentName+'</td></tr>';
                        
                        if(j==10){
                        	break;
                        }
						
					} // for close
					
//					document.getElementById('recentDocumentTenId').innerHTML='<tr><th width="70%">Name</th><th width="30%">Created By</th></tr>'+allDataBody;
					document.getElementById('recentDocumentTenId').innerHTML='<tr><th width="70%">Name</th></tr>'+allDataBody;
					} // length check
					else{
//						document.getElementById('recentDocumentTenId').innerHTML='<tr><th width="70%">Name</th><th width="30%">Created By</th></tr>'+'<tr><p align="center">No Documents Available</p></tr>';
						document.getElementById('recentDocumentTenId').innerHTML='<tr><th width="70%">Name</th></tr>'+'<tr><p align="center">No Documents Available</p></tr>';
					}
					
				} // documentTrackingData close check
				
				
			} // jsonValid
			else{
//					document.getElementById('recentDocumentTenId').innerHTML='<tr><th width="70%">Name</th><th width="30%">Created By</th></tr>'+'<tr><p align="center">No Documents Available</p></tr>';
				document.getElementById('recentDocumentTenId').innerHTML='<tr><th width="70%">Name</th></tr>'+'<tr><p align="center">No Documents Available</p></tr>';
			}
        	
        },
          complete: function(){
        	  $('#load').css("visibility", "hidden");
        	  }
	});
	
}



var ii = 1;
$('body').on('click', '.add-more-btn-workflow', function() { 
  var copy = $(this).parents('.addMore').find('.addMore-copy').html();
  copy = copy.replace("**1**",ii);
  copy = copy.replace("**1**",ii);
  ii++;
  $(this).parents('.addMore').find('.addMore-main').append(copy);
  $('.selectpicker').selectpicker('refresh');
  $(this).parents('.addMore').find('.addMore-sub:last-child').find(".dropdown-toggle:first").css("display","none");
  $(this).parents('.addMore').find('.addMore-sub:last-child').find('.form-group').find(".dropdown-toggle:first").css("display","none");
});

$('body').on('click', '.copy-remove-btn', function() { 
  $(this).parents('.addMore-sub').remove();
});


function ontabapproverconfig(){
	 getapproverlist ("approverlistdiv", "approverlistid1","approverlistcls");
	 getapproverlist ("approverlistdiv2","approverlistid2","approverlistcls");

}


$('body').on('click', '.displaysecondapprover', function() { 
    $('.secondapprover').css('display','block');

});

$('body').on('click', '.removesecondapprover', function() { 
    $('.secondapprover').css('display','none');

});

$('body').on('click', '.saveapprover', function() { 
	saveapproverfun();
});



function saveapproverfun(){
	var tempmainJson={};
      console.log("in save");
      console.log(document.getElementById("approvertype").value);
      console.log(document.getElementById("approverlistid1").value);
      	      console.log(document.getElementById("approverlistid2").value);


	
	var type = $('#approvertype').val();
  	var level1 = $('#approverlistid1').children("option:selected").val();
  	var level2 = $('#approverlistid2').children("option:selected").val();

      var priority = $("input[name='approverpriority']:checked").val();

      console.log("type "+type+" level1 "+level1+" level2 "+level2+" priority "+priority);
	if(type == '0'){
		alert('Please select Type');
		return false;
		}else if(level1 == '0'){
			alert('Please select Approver');
			return false;

		}
	
	tempmainJson["email"]=Email;
	tempmainJson["group"]=group;
	tempmainJson["type"]=type;
	tempmainJson["level1"]=level1;
	if(level1 !== '0'){
		tempmainJson["level2"]=level2;
	}
	tempmainJson["priority"]=priority;


	$.ajax({		
		type:'POST',
		url:'/portal/servlet/service/saveApprover',
		async:true,	
		data:JSON.stringify(tempmainJson),
		contentType:"application/json",	
		success:function(data){
			// alert(tempdata);
			console.log(data);
			var json=JSON.parse(data);
			if ( json.hasOwnProperty("status")) {
				var status=json.status;
                if(status=="success"){
                	
                	alert("Approver Saved Successfully");
                	//window.location.reload(); 
                	//document.getElementById("approversavesuccess").innerHTML="Approver Saved Successfully";
               	 getapproverlist ("approverlistdiv", "approverlistid1","approverlistcls");
            	 getapproverlist ("approverlistdiv2","approverlistid2","approverlistcls");

                }}
			
		}
	});
}
//function configurenewapproer(){
//	
//	document.getElementById("approversavesuccess").innerHTML="";
//
//}

function getapproverlist (divid,idforselec,  classtoselect){
	// getContactFilelist("Sel_ConactFileTemp","temp_selContact")
	// this post method -accepts json in param.
	try{
	document.getElementById(divid).innerHTML="";
	
	$.ajax({
		type:'GET',
		url:'/portal/servlet/service/getvalidapprovallist?email='+Email+'&group='+group,
		async: false,
		success:function(dataa){
			// alert(dataa);
			console.log("Approvers  "+dataa);
			var Approversjson = JSON.parse(dataa);
			var Approversarr=Approversjson.Approvers;
			
			var option = '<option value="0">Select Approver</option>';
			for(var i=0;i<Approversarr.length;i++){
				option = option + '<option value="'+Approversarr[i]+'">'+Approversarr[i]+'</option>';
			}
			console.log("option :: "+option);
			// $("#"+idforselec).html(option);
			document.getElementById(divid).innerHTML = '<select class="form-control '+classtoselect+'" id="'+idforselec+'">'+option+'</select>';

	}
	});
	}catch(err){
		console.log(err);
	}
}




//.........................................................settings tab code
//here......................

$("#normalsetupsave").click(function() {
	
	GetSelectedItem('allmails');

});

function GetSelectedItem(el)
{
 var e = document.getElementById(el);
// var strSel = "The Value is: " + e.options[e.selectedIndex].value + " and
	// text is: " + e.options[e.selectedIndex].text;
 var mailType = e.options[e.selectedIndex].text;
 // console.log(mailType);
 
 saveNormalSetUpResponse(mailType);
}


function saveNormalSetUpResponse(mailType) {

	var usernameNormal = document.getElementById("usernamemail");
	var passwordNormal = document.getElementById("passwordmails");
	/*
	 * var hostNormal = document.getElementById("hostNormal"); var portNormal =
	 * document.getElementById("portNormal"); var protocolNormal =
	 * document.getElementById("protocolNormal");
	 */

	
	
	var normalSetUpResponseObj = {
		normalSetUpResponseArray : []
	};

	normalSetUpResponseObj.normalSetUpResponseArray.push({
		"usernameNormal" : usernameNormal.value,
		"passwordNormal" : passwordNormal.value,
		/*
		 * "hostNormal" : hostNormal.value, "portNormal" : portNormal.value,
		 * "protocolNormal" : protocolNormal.value,
		 */
		"Email" : Email,
		"group":group,
		"mailType":mailType,
	});
	
	var json = JSON.stringify(normalSetUpResponseObj);
	console.log("normalSetUpResponseObj: "+json);

	$.ajax({

		url : "/portal/normalSetUpRes",
		type : 'POST',
		data : {
			'normalSetUpRequest' : json
		},

		success : function(data) {
			alert("Data Saved! " +data);
			
			// clear fields
         $('input[type="text"],textarea').val('');
		}

	}); // function on click

	
} // saveNormalSetUpResponse function close here

//..................................for userinfo to get usermail start
//here...................................
var OAUTHURLUSERINFO    =   'https://accounts.google.com/o/oauth2/auth?';
var VALIDURLUSERINFO    =   'https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=';
var SCOPEUSERINFO       =   'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/gmail.send';
var CLIENTIDUSERINFO    =   '350043906139-infkug7o8lu33pvo0ak3l4accic1guqo.apps.googleusercontent.com';
var REDIRECTUSERINFO    =   'https://bizlem.io:8083/portal/servlet/service/DoctigerCreation'
var LOGOUTUSERINFO      =   'http://accounts.google.com/Logout';
//var TYPEUSERINFO = 'token';


//............................extra
var UserInfo_access_type =    'offline';
var approval_prompt='force';
var TYPEUSERINFO        =   'code';
//............................extra

//var _urlUSERINFO = OAUTHURLUSERINFO + 'scope=' + SCOPEUSERINFO +
//'&client_id=' + CLIENTIDUSERINFO + '&redirect_uri=' + REDIRECTUSERINFO +
//'&response_type=' + TYPEUSERINFO;
var _urlUSERINFO        =   OAUTHURLUSERINFO + 'scope=' + SCOPEUSERINFO + '&client_id=' + CLIENTIDUSERINFO + '&redirect_uri=' + REDIRECTUSERINFO + '&response_type=' + TYPEUSERINFO + '&approval_prompt=' + approval_prompt + '&access_type=offline';

//..................................for userinfo to get usermail end
//here...................................

/*
* var OAUTHURL = 'https://accounts.google.com/o/oauth2/v2/auth?'; var SCOPE =
* 'https://www.googleapis.com/auth/gmail.send'; var CLIENTID =
* '350043906139-infkug7o8lu33pvo0ak3l4accic1guqo.apps.googleusercontent.com';
* var REDIRECT = 'http://bizlem.io:8082/portal/servlet/service/DocTiger' var
* TYPE= 'code'; var access_type = 'offline'; var _url = OAUTHURL + 'scope=' +
* SCOPE + '&client_id=' + CLIENTID + '&redirect_uri=' + REDIRECT +
* '&response_type=' + TYPE + '&access_type=' +access_type;
*/ 

//var acToken;
//var tokenType;
//var expiresIn;
//var user;
var loggedIn    =   false;
//var tokenJson;
//var userAuthMail;
//var newcode;


/*
* function login() //http://localhost:8080/Autoreply/GetCodeGmail.html
* http://www.mailtangy.com/ { var win = window.open(_urlUSERINFO);
* 
* console.log("win: "+win); if (win.document.URL.indexOf(REDIRECTUSERINFO) !=
* -1) { var url = win.document.URL; console.log("url: "+url); }
* 
* SaveCode(); //changeVisibility(); } function changeVisibility() {
* console.log("in hide");
* 
* 
* 
* document.getElementById("loginText").style.visibility = "hidden"; }
* 
* function SaveCode() {
* 
* 
* var queryString = decodeURIComponent(window.location.search);
* console.log("queryString "); queryString = queryString.substring(1);
* //alert("queryString = "+queryString); var queries = queryString.split('?');
* console.log("queries = "+queries); //alert("queries = "+queries); var
* code=queries[0].split('=')[1]; console.log("code = "+code); window.close(); }
*/


function login() {
 var win         =   window.open(_urlUSERINFO, "windowname1", 'width=800, height=600'); 
 var pollTimer   =   window.setInterval(function() { 
     try {
         if (win.document.URL.indexOf(REDIRECTUSERINFO) != -1) {
             window.clearInterval(pollTimer);
             var url =   win.document.URL;
             console.log("urlWindow: "+url);
            // acToken = gup(url, 'access_token');
             // tokenType = gup(url, 'token_type');
             // expiresIn = gup(url, 'expires_in');
             
             var newcode= getcode(url, 'code');
             console.log("newcode:: "+newcode);
            
            
             $.ajax({
	           		type:'GET',
	           		url:'/portal/getAuthCode?code='+newcode + '&email='+Email+'&group='+group,
	           		async:true,
	           		success:function(data){
	           			try{
	           				console.log("successServlet: "+data);
	           				
	           			}catch(err){
	           				console.log("err: "+err);
	           			}
	           		}
	           	});
             
           // validateToken(acToken);
           
            // console.log("acToken: "+acToken);
            
         /*
			 * $.post('https://www.googleapis.com/oauth2/v4/token', { 'code':
			 * newcode, 'client_id' : CLIENTID,'client_secret' :
			 * 'rWSaS6LZA_VZSTGe7UzSoS9l','grant_type' :
			 * 'authorization_code','redirect_uri' : REDIRECT},
			 * function(returnedData){ console.log(returnedData); var jsontoken =
			 * JSON.stringify(returnedData); console.log("jsontoken:
			 * "+jsontoken); tokenJson= returnedData.access_token;
			 * console.log("tokenpassJsoninside: "+tokenJson); //
			 * validateToken(tokenJson); getUserInfo(tokenJson);
			 * 
			 * $.ajax({ type:'GET', url:'/portal/authSend?token='+tokenJson,
			 * async:true, success:function(data){ try{ console.log("success:
			 * "+data);
			 * 
			 * }catch(err){ console.log("err: "+err); } } }); });
			 */
            
             win.close();
//SaveCode();
             
            

               // validateToken(acToken);
              
              // .............................................save in
					// sling.......................
            /*
				 * var authSetUpResponseObj = { authSetUpResponseArray : [] };
				 * 
				 * authSetUpResponseObj.authSetUpResponseArray.push({
				 * "access_token" : acToken, "Email" : Email,
				 * 
				 * });
				 * 
				 * var json = JSON.stringify(authSetUpResponseObj);
				 * console.log("authSetUpResponseObj: "+json);
				 * 
				 * $.ajax({
				 * 
				 * url : "http://bizlem.io:8082/portal/auth", type : 'POST',
				 * data : { 'authSetUpRequest' : json },
				 * 
				 * success : function(data) { alert("token saved"); }
				 * 
				 * }); // function on click
				 */
              
              // .....................................end save
					// sling....................................
              
         }
     } catch(e) {
     }
 }, 500);
}


function gup(url, name) {
 name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
 var regexS = "[\\#&]"+name+"=([^&#]*)";
 var regex = new RegExp( regexS );
 var results = regex.exec( url );
 if( results == null )
     return "";
 else
     return results[1];
}

function getcode(url, name) {
 name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
 var regexS = "[\\?&]"+name+"=([^&?]*)";
 var regex = new RegExp( regexS );
 var results = regex.exec( url );
 if( results == null )
     return "";
 else
     return results[1];
}


/*
* function validateToken(token) { $.ajax({ url: VALIDURLUSERINFO + token, data:
* null, success: function(responseText){ getUserInfo(); loggedIn = true;
* 
* //console.log("responseText: "+token); $('#loginText').hide();
* $('#logoutText').show(); // return(loggedIn); }, dataType: "jsonp" }); }
*/

/*
* function getUserInfo() { $.ajax({ url:
* 'https://www.googleapis.com/oauth2/v1/userinfo?access_token=' + acToken,
* data: null, success: function(resp) { user = resp; // console.log("user:
* "+user);
* 
* var json1 = JSON.stringify(user); console.log("json1: "+json1);
* 
* console.log("tokentrue: "+acToken); console.log("usertrue: "+user.name);
* 
* var authSetUpResponseObj = { authSetUpResponseArray : [] };
* 
* authSetUpResponseObj.authSetUpResponseArray.push({ "access_token" : acToken,
* "userName" : user.name, "picture" : user.picture, "userEmail" : user.email,
* "Email" : Email, });
* 
* var json = JSON.stringify(authSetUpResponseObj);
* console.log("authSetUpResponseObj: "+json);
* 
* $.ajax({
* 
* url : "http://bizlem.io:8082/portal/auth", type : 'POST', data : {
* 'authSetUpRequest' : json },
* 
* success : function(data) { alert("token saved"); }
* 
* }); // function on click
* 
* 
* $('#uName').text('Welcome ' + user.name); $('#imgHolder').attr('src',
* user.picture); }, dataType: "jsonp" }); }
*/

function startLogoutPolling() {
 $('#loginText').show();
 $('#logoutText').hide();
 loggedIn = false;
 $('#uName').text('Welcome ');
 $('#imgHolder').attr('src', 'none.jpg');
}


//.......................outlook start
//here..................................................



//........................outlook end
//here...................................................

var Outlook_OAUTHURL    =    'https://login.microsoftonline.com/common/oauth2/v2.0/authorize?';
var Outlook_SCOPE       =    'openid+Mail.Read+Mail.Send+email';
var Outlook_CLIENTID    =    '4106b9c3-33cf-4886-b19a-e4ba5e7f9e17';
//var Outlook_REDIRECT_URI = 'http://localhost:8080/Autoreply/'
	// var Outlook_REDIRECT_URI =
	// 'http://localhost:8085/ExcelCheckApi/index.jsp'
var Outlook_REDIRECT_URI ='https://bizlem.io:8083/portal/servlet/service/DoctigerCreation'
var Outlook_response_type=            'code';

var Outlook_url   =   Outlook_OAUTHURL + 'scope=' + Outlook_SCOPE + '&client_id=' + Outlook_CLIENTID + '&redirect_uri=' + Outlook_REDIRECT_URI + '&response_type=' + Outlook_response_type;

function Outlook_url_login() {
 var win         =   window.open(Outlook_url, "win", 'width=800, height=600'); 
 var pollTimer   =   window.setInterval(function() { 
     try {
         if (win.document.URL.indexOf(Outlook_REDIRECT_URI) != -1) {
             window.clearInterval(pollTimer);
             var url =   win.document.URL;
             console.log("urlWindow: "+url);
             
            var newcode= getcode(url, 'code');
            console.log("newcode:: "+newcode);
            
            $.ajax({
	           		type:'GET',
	           		url:'/portal/getOutlookAuthCode?code='+newcode + '&email='+Email+'&group='+group,
	           		async:true,
	           		success:function(data){
	           			try{
	           				console.log("outlook_successServlet: "+data);
	           				
	           			}catch(err){
	           				console.log("outlook_err: "+err);
	           			}
	           		}
	           	});
            
            
             win.close();

         }
     } catch(e) {
     }
 }, 500);
}


//.........................................................settings tab close