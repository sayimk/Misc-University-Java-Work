<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/
bootstrap/3.3.7/css/bootstrap.min.css">
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/
jquery.min.js"></script>


<!-- Google API Key AIzaSyBqCxxZtmhb8BTg5fNgThJXABgAi40L-Dk-->
<!-- Google Charts  -->
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

    
<!-- Bootstrap Javascript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/
bootstrap.min.js"></script>

<!-- Tree view JS and CSS from https://github.com/jonmiles/bootstrap-treeview-->
<script src="/resources/js/bootstrap-treeview.js"></script>
<link rel="stylesheet" href="/resources/CSS/bootstrap-treeview.css">

<!--  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">-->


<title>Bookmark Manager</title>
</head>
<body>

<div class="jumbotron text-center">
 <h2>MyBookmark Manager</h2>
 <p>Home</p>
</div>
<div class="container">
	<div class="row">

		<div class="col-sm-6">

			<div id="tree">
			</div>

		</div>
		
		<div class="col-sm-6">
			<div class="jumbotron text-left">
				<div class="btn-toolbar">
					<button style="float: right;" type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#addFolderModal">Add a New Folder</button>
					<button style="float: right;" type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deleteFolderModal">Delete this Folder</button>
		 			<button style="float: right;" type="button" class="btn btn-default btn-sm" onClick="toggleReadOnly()">Toggle ReadOnly</button>
		 			<h3><strong>Info:</strong></h3>
		 		
		 		</div>		 	
		 		<div style="clear: both">
		 			<h4 style = "float: left">Folder Name: </h4>
		 			<h4 style = "float: right" id = "folderHeader"></h4>
		 			<hr/>
		 			<br/>
		 			<h6 style = "float: left">Path: </h6>
		 			<h6 style = "float: right" id = "folderPath"></h6>
		 			<hr/>
		 			<h6 style = "float: left">ReadOnly: </h6>
		 			<h6 style = "float: right" id = "folderReadOnly"></h6>
		 	
		 		</div>		 		
			</div>
		</div>
		
		<div class="col-sm-7">
		
			<h3>Folders Map: </h3>
			
			<div id="chart_div" style="width: 475px; height: 300px;"></div>
		</div>
	</div>	
			<div class="row">
		
		<div class="col-sm-6">
			<div class="jumbotron text-left">
				<h4><strong>Links:</strong></h4>
		
				<div id="linksTable">
				</div>
			
				<div class="btn-toolbar">
					<button  type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#linkModal">Add Link</button>
					<button  type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#linkEditModal">Edit Link URL</button>
					<button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#linkDeleteModal">Delete Link</button>
				</div>
		
			</div>
		</div>

		<div class="col-sm-6">
			<div class="jumbotron text-left">

				<h4><strong>Text Files:</strong></h4>
		
				<div id="textTable">
				</div>
			
				<div class="btn-toolbar">
					<button  type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#newTextFileModal">New File</button>
					<button  type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#TextEditModal">Edit File</button>
					<button  type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#TextDeleteModal">Delete File</button>
				</div>
			</div>
		</div>		
		
				<div class="col-sm-6">
			<div class="jumbotron text-left">

				<h4>Locations:</h4>
		
				<div id="locationTable">
				</div>
			
				<div class="btn-toolbar">
					<button  type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#addLocationModal">Add New Location (Lat, Lng)</button>
					<button  type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#editLocationModal">Edit Location Details</button>
					<button  type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deleteLocationModal">Delete Location</button>
				</div>
			</div>
		</div>	
</div>

<!-- Modals -->

<!-- Link Modal -->
<div class="modal fade" id="linkModal" role="dialog">
	<div class="modal-dialog">
		<!-- Modal Content -->
		<div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Create New Link</h4>
        </div>
        <div class="modal-body">
                
            <label for ="linkName">Link Name:</label>
         	<input type="text" class="form-control" id="linkName" placeholder="My link">
              
            <label for ="linkURL">Link URL:</label>
         	<input type="text" class="form-control" id="linkURL" placeholder="www.Mylink.co.uk">
      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-success" data-dismiss="modal" onClick="addNewLink()">Add Link to Folder</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
	</div>
</div>
<!-- Link Modal End -->


<!-- Link edit Modal -->
<div class="modal fade" id="linkEditModal" role="dialog">
	<div class="modal-dialog">
		<!-- Modal Content -->
		<div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Edit Link URL</h4>
        </div>
        <div class="modal-body">
                
            <label for ="existinglinkName">Existing Link Name:</label>
         	<input type="text" class="form-control" id="existinglinkName" onKeyup="fetchURLContent()" placeholder="my Existing Name">
              
            <label for ="newlinkURL">New Link URL:</label>
         	<input type="text" class="form-control" id="newlinkURL" placeholder="www.newlink.co.uk">
      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-info" data-dismiss="modal" onClick="editLink()">Change URL</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
	</div>
</div>
<!-- Link edit  Modal End -->

 <!-- Link delete Modal -->
<div class="modal fade" id="linkDeleteModal" role="dialog">
	<div class="modal-dialog">
		<!-- Modal Content -->
		<div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Confirm Link Delete</h4>
        </div>
        <div class="modal-body">
                
            <label for ="toDeletelinkName">Existing Link Name:</label>
         	<input type="text" class="form-control" id="toDeletelinkName" placeholder="My Link Name">
      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal" onClick="DeleteLink()">Delete</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
	</div>
</div>
<!-- Link delete Modal End -->

 <!-- folder add Modal -->
<div class="modal fade" id="addFolderModal" role="dialog">
	<div class="modal-dialog">
		<!-- Modal Content -->
		<div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Create New Folder</h4>
        </div>
        <div class="modal-body">
                
            <label for ="newFolderName">New Folder Name:</label>
         	<input type="text" class="form-control" id="newFolderName" placeholder="MyNewFolder">
      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-success" data-dismiss="modal" onClick="addFolder()">Create Folder</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
	</div>
</div>
<!-- folder add Modal End -->
  
 
 <!-- folder add Modal -->
<div class="modal fade" id="deleteFolderModal" role="dialog">
	<div class="modal-dialog">
		<!-- Modal Content -->
		<div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Delete Folder</h4>
        </div>
        <div class="modal-body">
          <h3>Warning: Deleting a Folder will remove everything within that folder, subFolders, Links, Files and Map locations<br/>
          Are You Sure?</h3>
      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal" onClick="deleteFolder()">Yes</button>
          <button type="button" class="btn btn-success" data-dismiss="modal">No</button>
        </div>
      </div>
	</div>
</div>
<!-- folder add Modal End -->
  
 <!-- Add Text Modal -->
<div class="modal fade" id="newTextFileModal" role="dialog">
	<div class="modal-dialog">
		<!-- Modal Content -->
		<div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Create New File</h4>
        </div>
        <div class="modal-body">
                
            <label for ="NewFileName">File Name:</label>
         	<input type="text" class="form-control" id="NewFileName" placeholder="My Text File">
              
            <label for ="FileContents">Text Contents:</label>
         	<textarea  class="form-control" rows="6" id="FileContents" placeholder="blah blah"></textarea>
      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-success" data-dismiss="modal" onClick="addNewTextFile()">Add File to Folder</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
	</div>
</div>
<!-- add text Modal End -->

 <!-- text delete Modal -->
<div class="modal fade" id="TextDeleteModal" role="dialog">
	<div class="modal-dialog">
		<!-- Modal Content -->
		<div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Confirm File Delete</h4>
        </div>
        <div class="modal-body">
                
            <label for ="toDeleteTextFileName">Existing File Name:</label>
         	<input type="text" class="form-control" id="toDeleteTextFileName" placeholder="My Text File">
      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal" onClick="DeleteTextFile()">Delete</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
	</div>
</div>
<!-- text delete Modal End -->

 <!-- text Edit Modal -->
<div class="modal fade" id="TextEditModal" role="dialog">
	<div class="modal-dialog">
		<!-- Modal Content -->
		<div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Confirm File Edit</h4>
        </div>
        <div class="modal-body">
                
			<label for ="ExistingFileName">Existing File Name:</label>
         	<input type="text" class="form-control" id="ExistingFileName" onKeyup="fetchFileContent()" placeholder="My Existing Text File">
              
            <label for ="NewFileContents">Text Contents:</label>
         	<textarea  class="form-control" rows="6" id="NewFileContents" placeholder="NEW blah blah"></textarea>
      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-info" data-dismiss="modal" onClick="editTextFile()">Edit File</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
	</div>
</div>
<!-- text delete Modal End -->

<!-- location add model -->
<div class="modal fade" id="addLocationModal" role="dialog">
	<div class="modal-dialog">
		<!-- Modal Content -->
		<div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Add a New Location</h4>
        </div>
        <div class="modal-body">
                
            <label for ="locationName">Location Name:</label>
         	<input type="text" class="form-control" id="locationName"  placeholder="My Location">
              
            <label for ="Addlat">Location Latitude:</label>
         	<input type="text" class="form-control" id="AddLat" placeholder="-324.000">
         	
         	<label for ="Addlng">Location Longitude:</label>
         	<input type="text" class="form-control" id="AddLng" placeholder="755.000">
      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-success" data-dismiss="modal" onClick="addNewlocation()">Save Location</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
	</div>
</div>

<!-- location add model end -->


<!-- location edit model -->
<div class="modal fade" id="editLocationModal" role="dialog">
	<div class="modal-dialog">
		<!-- Modal Content -->
		<div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Edit Existing Location</h4>
        </div>
        <div class="modal-body">
                
            <label for ="editlocationName">Existing Location Name:</label>
         	<input type="text" class="form-control" id="editlocationName" onKeyup="getLatLng()" placeholder="My Location">
              
            <label for ="Addlat">New Location Latitude:</label>
         	<input type="text" class="form-control" id="editedLat" placeholder="-324.000">
         	
         	<label for ="Addlng">New Location Longitude:</label>
         	<input type="text" class="form-control" id="editedLng" placeholder="755.000">
      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-success" data-dismiss="modal" onClick="editlocation()">Save Location</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
	</div>
</div>
<!-- location edit model end -->


<!-- location delete model -->
<div class="modal fade" id="deleteLocationModal" role="dialog">
	<div class="modal-dialog">
		<!-- Modal Content -->
		<div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Edit Existing Location</h4>
        </div>
        <div class="modal-body">
                
            <label for ="deletelocationName">Existing Location Name:</label>
         	<input type="text" class="form-control" id="deletelocationName" placeholder="My Location">
   
      
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal" onClick="deletelocation()">Delete Location</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
	</div>
</div>
<!-- location delete model end -->





</div> 
<script>

var contents;
var googleChartsJSON;


$('document').ready(function(){
	
	var ajaxResponse = ${model.text};
	
	$('#tree').treeview({multiSelect: false, data: ajaxResponse, 
		onNodeSelected: function(event, data){
			getSelected();
			}
	})
	
	
	$('#tree').treeview('selectNode', [ 0, { silent: true } ]);
	getSelected();

});

function refreshLinkTable(){
	$.ajax({type: "GET",data: { folderName: $('#folderHeader').text(), path: $('#folderPath').text()},  url: "/service/getLinksTable", datatype: 'html', success: function(result){
		$('#linksTable').html(result);
	}})
}

function refreshTextTable(){
	$.ajax({type: "GET", data: { folderName: $('#folderHeader').text(), path: $('#folderPath').text()}, url: "/service/getTextTable", datatype: 'html', success: function(Textresult){
		$('#textTable').html(Textresult);}})
}

function refreshLocationTable(){
	$.ajax({type: "GET", data: { folderName: $('#folderHeader').text(), path: $('#folderPath').text()}, url: "/service/getLocationTable", datatype: 'html', success: function(Locationresult){
		$('#locationTable').html(Locationresult);}})
}


function getSelected(){
	var object = $('#tree').treeview(true).getSelected()[0];
	$('#folderHeader').html(object["text"]);
	$('#folderPath').html(object["path"]);
	
	refreshLinkTable();
	refreshTextTable();
	refreshLocationTable();
	getReadOnly();
	generateCharts();

}



function addNewLink(){
	var LinkURL = $('#linkURL').val();
	var LinkName = $('#linkName').val();
	
	$.get('/service/addNewLink',{folderName: $('#folderHeader').text(), linkUrl: LinkURL, linkName:LinkName, path: $('#folderPath').text(),},
			function(returnedData){
		refreshLinkTable();
		generateCharts();
	});
}

function addFolder(){
	
	if($('#folderHeader').text()=="/")
		var parentFolder = $('#folderPath').text()+$('#folderHeader').text();
	else var parentFolder = $('#folderPath').text();

	
	$.get('/service/create',{parent: parentFolder, folder: $('#newFolderName').val()},
			function(returnedData){
		window.location.reload(true);
	});
}

function deleteFolder(){
	$.get('/service/delete',{folder: $('#folderPath').text()},
			function(returnedData){
		window.location.reload(true);
	});
}

function editLink(){
	var newLinkURL = $('#newlinkURL').val();
	var existingLinkName = $('#existinglinkName').val();
	
	$.get('/service/editLink',{folderName: $('#folderHeader').text(), linkUrl: newLinkURL, path: $('#folderPath').text(), linkName:existingLinkName},
			function(returnedData){
		refreshLinkTable();
	});
}


function DeleteLink(){
	
	var existingLinkName = $('#toDeletelinkName').val();
	
	$.get('/service/deleteLink',{folderName: $('#folderHeader').text(), path: $('#folderPath').text(),linkName:existingLinkName},
			function(returnedData){
		refreshLinkTable();
		generateCharts();
	});
}

function addNewTextFile(){
	
	var textContent = $('#FileContents').val();
	var textName = $('#NewFileName').val();
	
	$.get('/service/addNewTextFile',{folderName: $('#folderHeader').text(), path: $('#folderPath').text() ,fileName: textName, fileContents:textContent},
			function(returnedData){
		refreshTextTable();
		generateCharts();
	});
}

function DeleteTextFile(){
	
	var existingTextFileName = $('#toDeleteTextFileName').val();
	
	$.get('/service/deleteTextFile',{folderName: $('#folderHeader').text(), path: $('#folderPath').text() ,FileName:existingTextFileName},
			function(returnedData){
		refreshTextTable();
		generateCharts();
	});
}

function editTextFile(){
	var newContents = $('#NewFileContents').val();
	var existingFileName = $('#ExistingFileName').val();
	
	$.get('/service/editTextFile',{folderName: $('#folderHeader').text(),path: $('#folderPath').text() ,fileContents: newContents, fileName:existingFileName},
			function(returnedData){
		refreshTextTable();
	});
}


function fetchFileContent(){
	$.ajax({type:"GET", url:'/service/ajaxGetFileContent', data:{folderName:$('#folderHeader').text(),path: $('#folderPath').text() ,FileName:$('#ExistingFileName').val()},
		success: function(res){
			$('#NewFileContents').val(res);
		}
	});
}

function fetchURLContent(){
	$.ajax({type:"GET", url:'/service/ajaxGetURL', data:{folderName:$('#folderHeader').text(),path: $('#folderPath').text() ,LinkName:$('#existinglinkName').val()},
		success: function(res){
			$('#newlinkURL').val(res);
		}
	});
}

function addNewlocation(){
	
	var locationLng = $('#AddLng').val();
	var locationLat = $('#AddLat').val();
	var location = $('#locationName').val();

	
	$.get('/service/addNewLocation',{folderName: $('#folderHeader').text(), path: $('#folderPath').text() ,locationName: location, lat:locationLat, lng:locationLng},
			function(returnedData){
		refreshLocationTable();
		generateCharts();
	});
	
}

function deletelocation(){
	
	var location = $('#deletelocationName').val();

	$.get('/service/deleteLocation',{folderName: $('#folderHeader').text(), path: $('#folderPath').text() ,locationName: location},
			function(){
		refreshLocationTable();
		generateCharts();
	});
	
}


function toggleReadOnly(){
	
	$.get('/service/ajaxToggle',{folderName: $('#folderHeader').text(), path: $('#folderPath').text()},
			function(){
		getReadOnly();
	});
	
}

function getReadOnly(){
	
	$.ajax({type:"GET", url:'/service/ajaxGetReadOnly', data:{folderName:$('#folderHeader').text(),path: $('#folderPath').text()},
		success: function(res){
			$('#folderReadOnly').html(res);
		}
	});
	
}


function editlocation(){
	
	var locationLng = $('#editedLng').val();
	var locationLat = $('#editedLat').val();
	var location = $('#editlocationName').val();

	
	$.get('/service/editLocation',{folderName: $('#folderHeader').text(), path: $('#folderPath').text() ,locationName: location, lat:locationLat, lng:locationLng},
			function(returnedData){
		refreshLocationTable();
	});
	
}

function getLatLng(){
	
	var location = $('#editlocationName').val();
	$.get('/service/getLocationLat',{folderName: $('#folderHeader').text(), path: $('#folderPath').text() ,locationName: location},
			function(returnedLat){
		$('#editedLat').val(returnedLat);
	});
	
	$.get('/service/getLocationLng',{folderName: $('#folderHeader').text(), path: $('#folderPath').text() ,locationName: location},
			function(returnedLng){
		$('#editedLng').val(returnedLng);
	});
	
}

//Google Charts Stuff
      // Load the Visualization API and the corechart package.
		google.charts.load('current', {'packages':['treemap']});

function generateCharts(){   
	
	$.ajax({type:"GET", url:'/service/ajaxCountFolderStructure',
		success: function(res){
			googleChartsJSON =JSON.parse(res);
		    google.charts.setOnLoadCallback(drawChart);
		}
	});
}

function drawChart() {

 // Create the data table.

    var data = google.visualization.arrayToDataTable(googleChartsJSON);

    var tree = new google.visualization.TreeMap(document.getElementById('chart_div'));

    tree.draw(data, {
      minColor: '#f00',
      midColor: '#ddd',
      maxColor: '#0d0',
      headerHeight: 15,
      fontColor: 'black',
      showScale: true
    });
 
  }
  
  
  
  
      
      

</script>


</body>
</html>