<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/resources/js/scripts.js"></script>
</head>
<body>
<div>
	<form action="fileUpload" method="post" enctype="multipart/form-data">
		<div  class="form-group" id="selection">
		    <h3>Upload csv file for duplicate records</h3>
		    <div>
		        <input type="file" id="myFile" size="50" name="file" accept = ".csv" onchange= "myFunction()">
		        <p id="demo"></p>
		    </div>
		</div> 
	</form>
</div>
<button id ="back" type="button" onclick ="backFunction()" style="display:none;"> back</button>
<div id="chartContainer" style="height: 300px; width: 100%;"></div><br>
<div id="duplicateTable" style="display:none;">
    <h4>Duplicate Records</h4>
    <table id="duplicateRecords">
        <tr>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Company</th>
            <th>Email</th>
            <th>Address</th>
            <th>Zip</th>
            <th>City</th>
            <th>State</th>
            <th>Phone</th>
        </tr>
    </table>
</div>
<div id="nonSimilarTable" style="display:none;">
        <h4>Unique Records</h4>
        <table id="nonSimilarRecords">
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Company</th>
                <th>Email</th>
                <th>Address</th>
                <th>Zip</th>
                <th>City</th>
                <th>State</th>
                <th>Phone</th>
            </tr>
        </table>
    </div>

</body>
</html>