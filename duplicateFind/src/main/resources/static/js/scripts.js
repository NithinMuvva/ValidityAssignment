function backFunction() {
	$("#chartContainer").hide();
	$("#duplicateTable").hide();
	$("#nonSimilarTable").hide();
	$("#selection").show();
	$("#back").hide();
	$("#myFile").val("");
}
function myFunction(){
    var file = document.getElementById('myFile').files[0];
    var txt = "";
    if (file) {
        txt += "name: " + file.name + "<br>";
        getTableData(file);
        $("#selection").hide();
        $("#back").show();
        $("#chartContainer").show();
    } 
    else {
    	txt += "Select a file.";
    }
    $("#demo").val(txt);
}

function getTableData(){
  $('#duplicateRecords > tbody > tr:nth-child(n+2)').remove();
  $('#nonSimilarRecords > tbody > tr:nth-child(n+2)').remove();
  var form = document.forms[0];
  var formData = new FormData(form);
  $.ajax({
   type: "POST",
   url: "/getSimilarRecords",
   contentType: "application/json",
   dataType : "json",
   data : formData,
   cache : false,
   contentType : false,
   processData : false,
   success: function (data) {
   var similarData = data['similarData'];
   var uniqueData = data['uniqueData'];
   $.each(similarData,function(i, item) {
        var rowdata = '<tr> '
            +'<td>'
            + similarData[i].first_name
            +'</td>'
            + '<td>'
            + similarData[i].last_name
            + '</td>'
            + '<td>'
            + similarData[i].company
            + '</td>'
            + '<td>'
            + similarData[i].email
            + '</td>'
            + '<td>'
            + similarData[i].address
            + '</td>'
            + '<td>'
            + similarData[i].zip
            + '</td>'
            + '<td>'
            +similarData[i].city
            + '</td>'
            + '<td>'
            +similarData[i].state
            + '</td>'
            + '<td>'
            +similarData[i].phone
            + '</td>'
            + '</tr>';
            $("#duplicateRecords").append(rowdata);
   });
   $.each(uniqueData,function(i, item) {
        var rowdata = '<tr> '
            +'<td>'
            + uniqueData[i].first_name
            +'</td>'
            + '<td>'
            + uniqueData[i].last_name
            + '</td>'
            + '<td>'
            + uniqueData[i].company
            + '</td>'
            + '<td>'
            + uniqueData[i].email
            + '</td>'
            + '<td>'
            + uniqueData[i].address
            + '</td>'
            + '<td>'
            + uniqueData[i].zip
            + '</td>'
            + '<td>'
            +uniqueData[i].city
            + '</td>'
            + '<td>'
            +uniqueData[i].state
            + '</td>'
            + '<td>'
            +uniqueData[i].phone
            + '</td>'
            + '</tr>';
            $("#nonSimilarRecords").append(rowdata);
   	});
	   $("#duplicateTable").show();
	   $("#nonSimilarTable").show();
	   var similarLength = (similarData.length/(similarData.length+uniqueData.length))*100;
	   var uniqueLength = (uniqueData.length/(similarData.length+uniqueData.length))*100;
	   chartFunction(similarLength,uniqueLength);
   },
   error : function(data,errorThrown){
        console.log(errorThrown);
   }
   });   
}

function chartFunction(x,z){
console.log('in chartFunction');
    var chart = new CanvasJS.Chart("chartContainer",
    {
        title:{
            text: "Data Analysis"
        },
        legend: {
            maxWidth: 350,
            itemWidth: 120
        },
        data: [
        {
            type: "pie",
            showInLegend: true,
            toolTipContent: "#percent %",
            legendText: "{indexLabel}",
            dataPoints: [
                { y: x, indexLabel: "Similar Records" },
                { y: z, indexLabel: "Unique Records" }
            ]
        }
        ]
    });
    chart.render();
}