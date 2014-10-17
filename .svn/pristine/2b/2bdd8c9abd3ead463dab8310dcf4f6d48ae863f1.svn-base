var InitialUI = {};
InitialUI.points;
InitialUI.available;

InitialUI.getPointsStr = function(points,availablePreds){
	var s = "You have <span id='pointsText' class='availableInfo'>"+points+"</span> points and ";
	s+= "<span id='availableText' class='availableInfo'>"+availablePreds+"</span> predickets left!";
	return s;
}

InitialUI.allMatchesArr;
InitialUI.allMatchesKeyArr;
InitialUI.loadMatchError;
InitialUI.listenerArr;

//Checklist before uploading
//Check app ID in CricFB.js
//Check getReaderFromURL in LoaderServlet
//Check logging.properties
//Remove console.log and sysout
//check cron.xml and appengine.xml


InitialUI.removeListener = function(func) {
	for ( var i = 0; i < InitialUI.listenerArr.length; i++) {
		if(func == InitialUI.listenerArr[i]){
			InitialUI.listenerArr.splice(i,1);
			break;
		}
	}
}

InitialUI.getOvers = function(overs, maxOv){
	var s="("+overs;
	if (maxOv != 50 && maxOv != 20) {
		s+=" / "+maxOv;
	}
	s+=" ov)";
	return s;
}

InitialUI.getTeamNames = function(resp){
	return "<div class='matScore'><strong>"+resp.op1+"</strong> vs <strong>"+resp.op2+"</strong> - "+resp.location+"</div>";
}

InitialUI.getScoreHTML=function(resp){
	var o1 = resp.op1Overs;
	var o2 = resp.op2Overs;
	var s = "";
	if (o1 > 0) {
		s="<div class='matScore'><strong>"+resp.op1+"</strong>: "+resp.op1Score+"/"+resp.op1Wickets+" "+InitialUI.getOvers(o1,resp.op1MaxOv)+"</div>";
		if (o2 > 0) {
			s+="<div class='matScore'><strong>"+resp.op2+"</strong>: "+resp.op2Score+"/"+resp.op2Wickets+" "+InitialUI.getOvers(o2,resp.op2MaxOv)+"</div>";
		}else {
			s+="<div class='matScore'><strong>"+resp.op2+"</strong>: "+" Yet to Bat</div>";
		}
	}
	return s;
}
InitialUI.addMatches = function(){
	var resp = InitialUI.allMatchesArr;
	//Check if there are any items in resp
	if (!resp || resp.length == 0) {
		$('#matches').empty();
		$('#matches').append("<div class='loading-matches ui-widget ui-widget-content ui-state-highlight ui-corner-all'>No Cricket Match available now. Please come back later.</div>");
		return;
	}
	var i=0;
	var ul = document.createElement('ul');
	for (i=0;i<resp.length;++i) {
		var li = document.createElement('li');
		var outerDiv = document.createElement('div');
		var content = document.createElement('a');
		content.op1=resp[i].op1;
		content.op2=resp[i].op2;
		content.href="javascript:&quot;&quot;";
		content.title='Select this match to play!';
		$(content).click(function(obj) {
			var op1 = obj.currentTarget.op1;
			var op1Id = op1.replace(/ /g,"_");
			//First check if a tab is already present then Show the tab
			var matchElem = document.getElementById(op1Id);
			if (matchElem != null) {
				///Just show the tab
				CricFB.matchTabs.tabs('select', '#' + op1Id);
			} else {
				var op2 = obj.currentTarget.op2;
				var op2Id = op2.replace(/ /g,"_");
				//First check if a tab is already present then Show the tab
				matchElem = document.getElementById(op2Id);
				if (matchElem != null) {				
					CricFB.matchTabs.tabs('select', '#' + op2Id);
				} else {
					//The id of the content, Title
					CricFB.matchTabs.tabs( "add", "#"+op1Id,op1);
				}
			}
		});
		//if op1Ov greater than 0 then show score else show status
		var s = InitialUI.getScoreHTML(resp[i]);
		if (s=="") {
			outerDiv.innerHTML = InitialUI.getTeamNames(resp[i]);
		}else {
			outerDiv.innerHTML = s;
		}
		outerDiv.innerHTML+="<div class='matStatus'>"+resp[i].status+"</div>";		
		outerDiv.className="matchTextDiv";
		
		content.appendChild(outerDiv);
		li.appendChild(content);
		
		ul.appendChild(li);
	}
	$('#matches').empty();
	$(ul).menu().css('border-style','none');
	$('#matches').append(ul);
	$('#matches .matchTextDiv').mouseover(function(){
		$(this).css('border-style','none');
	}).mouseout(function(){
		$(this).css('border-style','dotted');
	});
	//Add any animation if required
	$('#matches').fadeIn(1000);
}
InitialUI.addTabHandler =  function( event, ui ) {
	 //create the match object
	 op1 = ui.panel.id.replace(/_/g," ");
	 var match;
	 for(var i=0;i<InitialUI.allMatchesKeyArr.length;++i) {
		 if (InitialUI.allMatchesKeyArr[i]==op1) {
			 match = Match.make(InitialUI.allMatchesArr[i]);
			 $(ui.panel).css('padding',0);
			 $(ui.panel).css('margin',0);
			 match.addContentHTML($(ui.panel));
			InitialUI.listenerArr.push(match.listenerFunction);
		 	ui.tab.listenerFunc=match.listenerFunction;
		 	CricFB.matchTabs.tabs('select', '#' + ui.panel.id);
			 break;
		 }
	 }	 
}

InitialUI.LoadingImage = null;
InitialUI.getLoadingImage =  function(){
	if (InitialUI.LoadingImage != null) {
		return InitialUI.LoadingImage;
	}
	InitialUI.LoadingImage =document.createElement('div');
	InitialUI.LoadingImage.className='loading-matches';
	InitialUI.LoadingImage.style['text-align']='center';
	var img =document.createElement('img');
	img.src=CricFB.loadingImagePath;
	InitialUI.LoadingImage.appendChild(img);
	return InitialUI.LoadingImage;	
}

InitialUI.displayMatches = function() {
	var args = "";
	//First display the loading icon for a few seconds and then the match appears
	$('#matches').empty();
	$('#matches').append(InitialUI.getLoadingImage());
	//Show loading for few secs
	setTimeout(InitialUI.addMatches,1000);
}
InitialUI.loadMatches = function(isFirst) {
	
	$.ajax("getMatches",{
	        type: "GET",  
	        data: "",  
	        dataType: 'text',		                 
	        success: function(data, textStatus, jqXHR){ 
			var resp = JSON.parse(data);
			 //Populate the array of keys first
			var i = 0;
			var keyArr=[];
			for (i=0;i<resp.length;++i) {
				keyArr.push(resp[i].op1);
				resp[i].op1Overs = parseFloat(resp[i].op1Overs);
				resp[i].op2Overs = parseFloat(resp[i].op2Overs);
				resp[i].op1MaxOv = parseFloat(resp[i].op1MaxOv);
				resp[i].op2MaxOv = parseFloat(resp[i].op2MaxOv);
			}
			
			InitialUI.allMatchesArr = resp;
			InitialUI.allMatchesKeyArr = keyArr;
			InitialUI.loadMatchError = "";
			if (isFirst != null && isFirst == true) {
			   	 $('#myList').empty();
			}
			//Inform the listeners
			if (InitialUI.listenerArr) {
				for (i=0;i<InitialUI.listenerArr.length;++i) {
					InitialUI.listenerArr[i]();
				}
			}
        },
		 error: function(jqXHR, textStatus, errorThrown){ 
        	//For now we can just ignore this, we will see later
			InitialUI.loadMatchError = textStatus;
        },  
        complete: function(jqXHR, textStatus){
        	//Its better if this is 4-5 secs plus the cronJob interval
        	//But need to analyze further to find the optimum value
        	//For now keeping it a at almost half the cronjob interval
        	setTimeout(InitialUI.loadMatches,60000);
        }
        });  
}

InitialUI.userLogin = function(uid) {
    $('#points').hide();
	var args = "fbId="+uid;
 	$.ajax("login",{  
	                 type: "GET",  
	                 data: args,  
	                 dataType: 'text',		                 
	                 success: function(data, textStatus, jqXHR){ 
	                	 var resp = JSON.parse(data);
	                	 InitialUI.points = resp.points;
	                	 InitialUI.available = resp.available;
	                	 $('#points').html(InitialUI.getPointsStr(resp.points, resp.available));
	                	 //without points no point loading matches
	                	 
	                	 //Add getPred as a listener
	                	 Predicket.lastTimeStamp = 0;
	                	 InitialUI.listenerArr.push(Predicket.getPredsListener);
	                	 
	                	 InitialUI.loadMatches(true);
	                 },
 					 error: function(jqXHR, textStatus, errorThrown){ 
	 					 var resp = JSON.parse(jqXHR.responseText);
	 					$('#points').text("Error Occured retrieving points: "+resp.error);
	 					//Could initiate a page refresh here. location.reload();
   
	                 },  
	                 complete: function(jqXHR, textStatus){
	  				    $('#points').fadeIn(1000);
	                 }               
	                 });  
 	
    //Get the user Name and photo
    //Display it in the header
    FB.api('/me', function(response) {
    	document.getElementById('right-header').innerHTML="<img src='http://graph.facebook.com/"+uid+"/picture'/>"
    		+"<span style='vertical-align:middle;margin: 0 0 0 2px'>"+response.name+"</div>";
    });
}

InitialUI.dispTimeout = 2000;
var invFriendsDialog;
InitialUI.getInvFriendsDialog = function(){
	if (invFriendsDialog != null){
		invFriendsDialog.dialog();
		return;
	}
	invFriendsDialog = $( "#invFriends" ).dialog({
		height: 150,
		modal: true
	});
	setTimeout(InitialUI.hideInvFriendsDialog,InitialUI.dispTimeout);
}
InitialUI.hideInvFriendsDialog = function(){
	invFriendsDialog.dialog("close");
}

var successPostDialog;
InitialUI.getSuccessPostDialog = function(){
	if (successPostDialog != null){
		successPostDialog.dialog();
		return;
	}
	successPostDialog = $( "#successPost" ).dialog({
		height: 150,
		modal: true
	});
	setTimeout(InitialUI.hideSuccessDialog,InitialUI.dispTimeout);
}

InitialUI.hideSuccessDialog = function(){
	successPostDialog.dialog("close");
}

var failedPostDialog;
InitialUI.getFailedPostDialog = function(){
	if (failedPostDialog != null){
		failedPostDialog.dialog();
		return;
	}
	failedPostDialog = $( "#failedPost" ).dialog({
		height: 150,
		modal: true
	});
	setTimeout(InitialUI.hideFailedDialog,InitialUI.dispTimeout);
}
InitialUI.hideFailedDialog = function(){
	failedPostDialog.dialog("close");
}
