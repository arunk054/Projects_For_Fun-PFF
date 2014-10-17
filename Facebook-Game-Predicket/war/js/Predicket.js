	var Predicket = {}

Predicket.LoadingImage = null;
Predicket.getLoadingImage =  function(){
	if (Predicket.LoadingImage != null) {
		return Predicket.LoadingImage;
	}
	Predicket.LoadingImage =document.createElement('div');
	Predicket.LoadingImage.className='loading-preds';
	Predicket.LoadingImage.style['text-align']='center';
	var img =document.createElement('img');
	img.src=CricFB.loadingImagePath;
	Predicket.LoadingImage.appendChild(img);
	return Predicket.LoadingImage;	
}
Predicket.lastTimeStamp;

Predicket.addPredicket = function(pResp){
	var groupDiv = document.createElement('div');
	groupDiv.className = 'virtual';
	groupDiv.style['display'] = 'none';
	var elem = Predicket.make(pResp,null);
	
	groupDiv.appendChild(elem);
	
	var args="id="+CricFB.uid;
	args+="&team="+encodeURI(pResp.team);
	args+="&runs="+pResp.runs;
	args+="&overs="+pResp.overs;
	args+="&riskOvers="+pResp.riskOvers;
	args+="&maxOv="+pResp.maxOv;

	if (Predicket.lastTimeStamp == 0)
	{
		$('#myList').empty();
	}
	$('#myList').prepend(groupDiv);
	
	//Display the groupDiv in fadein
	$(groupDiv).slideDown(2000);
	
	$.ajax("addPredicket",{
        type: "GET",  
        data: args,  
        dataType: 'text',		                 
        success: function(data, textStatus, jqXHR){ 
			var resp = JSON.parse(data);
			var statusElem = elem.statusElem;
			//statusElem=$(statusElem,'.predStatus');
			if (resp.status == 'error') {
				statusElem.innerHTML=Predicket.getStatus(-2);
				elem.appendChild("<div>"+resp.error+"</div>");
				return;
			} else {
				//Change the header
				var availTextElem= document.getElementById('availableText');
				availTextElem.innerHTML=resp.available;
				//Change the status in this
				statusElem.innerHTML=Predicket.getStatus(1);
				//Set time stamp
				Predicket.lastTimeStamp=resp.ts;
				elem.timeStamp = resp.ts;
			}
        },
		 error: function(jqXHR, textStatus, errorThrown){ 
        	//TODO do something
        }
    });
}

Array.prototype.contains = function(obj) {
	  var i = this.length;
	  while (i-- ) {
	    if (this[i] === obj) {
	      return true;
	    }
	  }
	  return false;
}

Predicket.getPredsListener = function() {
	$.ajax("getPreds",{
	        type: "GET",  
	        data: "id="+CricFB.uid+"&ts="+Predicket.lastTimeStamp,  
	        dataType: 'text',		                 
	        success: function(data, textStatus, jqXHR){ 
				var resp = JSON.parse(data);
				var predArr = resp.predArr;
				
				if (predArr.length == 0) {
					if (Predicket.lastTimeStamp == 0) {
						$('#myList').empty();
						$('#myList').append("<div class='myPredInfo ui-widget ui-widget-content ui-state-highlight ui-corner-all'>Please select a match and use the <span style='font-weight:bold;'>&quot;Add Predicket&quot;</span> button to create your predickets.</div>");
					}
					return;
				}

	        	var groupDiv = document.createElement('div');
	        	groupDiv.className = 'virtual';
	        	groupDiv.style['display'] = 'none';
	        	
	        	var toRemove = [];
	        	var completed = 0;
	        	var curLastTimeStamp = Predicket.lastTimeStamp; 
	        	for(i = 0;i<predArr.length;++i) {
	        		if (i==0){
	        			Predicket.lastTimeStamp = predArr[i].timeStamp;
	        		}
	        		if(predArr[i].state != 1) {
	        			completed++;
	        		}
	        		//Create a new predicket object and add it to virtual
	        		groupDiv.appendChild(Predicket.make(predArr[i],toRemove));
	        	}
	        	//Update the available and points in points header
				var elem = document.getElementById('pointsText');
				elem.innerHTML=resp.points;
				elem = document.getElementById('availableText');
				if(curLastTimeStamp == 0)
					elem.innerHTML=resp.available;
				else {
					var oldVal = parseInt(elem.innerHTML);
					elem.innerHTML=oldVal+completed;
				}
				
	        	//remove the elements that are in pending and have been completed
	        	if (toRemove.length > 0) {
		        	var myListElem = document.getElementById('myList');
		        	var myListKids = myListElem.children;
		        	for(i=0; i<myListKids.length;++i){
		        		gdArr = myListKids[i].children;
		        		
		        		for (j=0;j<gdArr.length;++j){
		        			if (toRemove.contains(gdArr[j].timeStamp)) {
		        				gdArr[j].parentNode.removeChild(gdArr[j]);
		        				j--;
		        			}

		        		}
		        	}
	        	}
	        	
	        	//Display the groupDiv in fadein
	        	if(curLastTimeStamp == 0){
	        	  $('#myList').append(groupDiv);
	        	  $(groupDiv).fadeIn(3000);
	        	}else {
	        		$('#myList').prepend(groupDiv);
	        		$(groupDiv).slideDown(3000);	
	        	}
	        },
			 error: function(jqXHR, textStatus, errorThrown){ 
	        	//TODO do something
	        },  
	        complete: function(jqXHR, textStatus){
	        }
        });  
}

Predicket.getPostMessage=function(resp){
	var s = "";
	if (resp.state == 0) {
		if (resp.points > 0) {
			s="I made "+resp.points+" points in predicting "+resp.team+" scores "+resp.runs+" runs in "+resp.overs+" overs.";
		} else if (resp.points < 0){
			s="I lost "+Math.abs(resp.points)+" points in predicting "+resp.team+" scores "+resp.runs+" runs in "+resp.overs+" overs.";
		} else {
			s="Neither lost nor made any points in predicting "+resp.team+" scores "+resp.runs+" runs in "+resp.overs+" overs.";
		}
	} else if (resp.state == 1 || resp.state == -1){
		s="I predict that "+resp.team+" will score "+resp.runs+" runs in "+resp.overs+" overs.";
	} else if (resp.state == -2) {
		s="I just ran out of predickets while predicting "+resp.team+" scores "+resp.runs+" runs in "+resp.overs+" overs.";
	} else if (resp.state == 2){
		s="My predicket was cancelled - "+resp.team+" scores "+resp.runs+" runs in "+resp.overs+" overs.";;
	} else {
		s="I am unable to add predickets. Can someone help?";
	}
	return s;
}
Predicket.getStatus = function(st){
	if(st == 0) {
		return "<span style='color:#2EAA4B;font-weight:200;'>COMPLETED</span>";
	} else if (st ==1){
		return "<span style='color:#0000DD;font-weight:200;'>SCHEDULED</span>";
	} else if (st ==2){
		return "<span style='color:#AB1515;font-weight:200;'>CANCELLED</span>";
	} else if(st == -1){
		return "<span style='color:#8E9080;font-weight:200;'>PENDING</span>";
	} else if (st == -2){
		return  "<span style='color:#F51D1D;font-weight:200;'>ERROR</span>";
	} else {
		return "<span style='color:#D5EC29;font-weight:200;'>UNKNOWN</span>";
	}
	
}

Predicket.make = function(resp,toRemove){

	var a = document.createElement('div');
	a.className='myPredicket ui-widget ui-widget-content';
	a.timeStamp = resp.timeStamp;
	a.title='Publish on your Wall';
	$(a).mouseover(function(){
		$(this).addClass('ui-state-hover');
		$(this).css('cursor','pointer');


	}).mouseout(function(){
	    $(this).removeClass('ui-state-hover');
	}).click(function(){
		//TODO Check if user logged in otherwise just refresh
		//alert("Please login to use this app. Your page will refresh now.");
		//TODO if CricFB.uid and session.uid does not match, inform and refresh
		//alert("Another user has logged in. Your page will refresh now.");
		FB.ui(
				//picture: http://i54.tinypic.com/wuj8s6.jpg
				//picture: http://i54.tinypic.com/2mhvnm0.jpg
				//picture: http://i56.tinypic.com/35jf5mh.jpg
				{
					method: 'feed',
					name: 'Predicket',
					link: 'http://predicket.appspot.com/',
					picture: 'http://i56.tinypic.com/35jf5mh.jpg',
					caption: 'Have fun predicting live cricket scores!',
					description: 'Get ready to sharpen your cricketing skills now. Play exciting score prediction games while watching live cricket scores.',
					message: Predicket.getPostMessage(resp)
				},
				function(response) {
					if (response != null && response.post_id != null) {
						InitialUI.getSuccessPostDialog();
					} else {
						InitialUI.getFailedPostDialog();
					}
				}
		);

	});
	//Check for prev Timestamp and add it to a global arr 
	if (toRemove != null && resp.prevTimeStamp != 0) {
		toRemove.push(resp.prevTimeStamp);
	}
	
	//create predTeam div
	var c1 = document.createElement('div');
	c1.className = 'predTeam';
	c1.innerHTML = resp.team;
	a.appendChild(c1);
	
	//create predContent
	cPredCont = document.createElement('div');
	cPredCont.className = 'predContent';
	cPredCont.innerHTML = "<span style='font-weight:bold;'>"+resp.runs+"</span><span style='font-style:italic;'> Runs<span> in </span></span>"+"<span style='font-weight:bold;'>"+resp.overs+"</span><span style='font-style:italic;'> Overs</span>";
	a.appendChild(cPredCont);
	
	//create predStatus  predStatusText predPoints
	c1 = document.createElement('div');
	c1.className = 'predStatus';
	c2= document.createElement('span');
	c2.innerHTML='Status: ';
	c1.appendChild(c2);
	
	c2= document.createElement('span');
	c2.className='predStatusText';
	c2.innerHTML=Predicket.getStatus(resp.state);
	c1.appendChild(c2);
	a.statusElem=c2;
	
	c2= document.createElement('span');
	c2.innerHTML=' Points: ';
	c1.appendChild(c2);
	
	c2= document.createElement('span');
	c2.className='predPoints ui-state-highlight';
	c2.innerHTML=resp.points;
	c1.appendChild(c2);
	
	a.appendChild(c1);
	
	return a;
	
}
