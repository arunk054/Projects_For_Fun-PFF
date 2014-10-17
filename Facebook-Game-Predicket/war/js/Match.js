var Match={};
var MAX_POSSIBLE_SCORE = 450;
var MAX_T20_SCORE = 300;

Match.getScoreHTML=function(resp){
	var o1 = resp.op1Overs;
	var o2 = resp.op2Overs;
	var s = "";
	var classes = "ui-widget ui-widget-header contentScoreDetail";
	if (o1 > 0) {
		s="<div class='"+classes+"'><strong>"+resp.op1+"</strong>: "+resp.op1Score+"/"+resp.op1Wickets+" "+InitialUI.getOvers(o1,resp.op1MaxOv)+"</div>";
	} else {
		s="<div class='"+classes+"'><strong>"+resp.op1+"</strong>: "+" Yet to Bat</div>";
	}
	if (o2 > 0) {
		s+="<div class='"+classes+"'><strong>"+resp.op2+"</strong>: "+resp.op2Score+"/"+resp.op2Wickets+" "+InitialUI.getOvers(o2,resp.op2MaxOv)+"</div>";
	}else {
		s+="<div class='"+classes+"'><strong>"+resp.op2+"</strong>: "+" Yet to Bat</div>";
	}
	return s;
}


Match.getTeamNames = function(resp){
	return "<div class = 'ui-widget ui-widget-content contentScore'><strong>"+resp.op1+"</strong> vs <strong>"+resp.op2+"</strong> - "+resp.location+"</div>";	
}
Match.getPlayersHTML = function(resp){
	if (!resp.players)
		return;
	var arr = resp.players.split("|");
	//var max = 2;//Use this instead of hardcoding 2 bowls, batsmen
	var bowlers = ["",""];
	var batsmen = ["",""];
	var bowlIndex=0, batsIndex=0;
	for ( var i = 0; i < arr.length; i++) {
		val = arr[i];
		if (val!= "") {
			//Check if it is a bowler
			if (val.indexOf("-") != -1) {
				bowlers[bowlIndex]=val;
				bowlIndex= (bowlIndex+1)%2;
			} else {
				batsmen[batsIndex]=val;
				batsIndex = (batsIndex+1)%2;
			}
		}
	}
	var outerDiv = document.createElement('div');
	outerDiv.className='playersDiv';
	
	//Create the table
	table = document.createElement('table');
	row = document.createElement('tr');
	data = document.createElement('td');
	data.className = 'playersData';
	data.innerHTML = batsmen[0];
	row.appendChild(data);
	
	data = document.createElement('td');
	data.className = 'playersData';
	data.innerHTML = bowlers[0];
	row.appendChild(data);

	table.appendChild(row);

	row = document.createElement('tr');
	data = document.createElement('td');
	data.className = 'playersData';
	data.innerHTML = batsmen[1];
	row.appendChild(data);
	
	data = document.createElement('td');
	data.className = 'playersData';
	data.innerHTML = bowlers[1];
	row.appendChild(data);

	table.appendChild(row);
	
	outerDiv.appendChild(table);
	//outerDiv.innerHTML="<div>"+batsmen[0]+" "+bowlers[0]+"</div><div>"+batsmen[1]+" "+bowlers[1]+"</div>";
	return outerDiv;
	
}

//resp is the json object of a match
Match.make = function(resp){
	var a = {};
	
	
	//TODO: Set the deltaOvers depending on the MaxOv ie for T20
	var deltaOvers = 3;
	
	
	var isOp1Batting = function(){
		//Either all out or overs ended
		if (resp.op1Wickets == "10")
			return false;

		if (resp.op1Overs+deltaOvers > resp.op1MaxOv)
			return false;
		
		return true;
	}
	
	var isOp2Batting = function(){
		//Either all out or overs ended
		//OR they scored more
		var retVal = true;
		if (resp.op2Wickets == "10") {
			retVal = false;
		} else if (resp.op2Overs >= resp.op2MaxOv) {
	 		//assuming deltaOvers is atleast 1
			retVal = false;
		}else if (resp.op2Score > resp.op1Score) {
			retVal = false;
		}
		if (retVal == false) {
			isMatchEnded = true;
		}
		if (resp.op2Overs+deltaOvers > resp.op2MaxOv) {
			retVal = false;
		}
		return retVal;
	}
	
	//Are initialized in getPredictionHTML
	var currentIndex;
	var predObjs;
	var container;
	var isMatchEnded;
	
	var setMatchEnded = function(val){
		isMatchEnded = val;
	}
	var getSliderTable = function(str,min,max, pObj, isRuns, curOvers){
		var table = document.createElement('table');
		var td = document.createElement('td');
		var val = min;
		if (isRuns) {
			//Set the default position to a projected score
			if (curOvers != 0)
			{
				val += Math.floor((min*(deltaOvers+1))/curOvers);
				if (val > max)
				{
					val = max;
				}
			} else {
				val += 6*(deltaOvers+1);
			}
		} else {
			var temp = document.createElement('span');
			temp.innerHTML="in ";
			td.appendChild(temp);
		}
		
		var spanDisp = document.createElement('span');
		spanDisp.className='sliderText';
		var spanCont = document.createElement('span');
		spanCont.innerHTML=str;
		
		td.appendChild(spanDisp);
		td.appendChild(spanCont);
		var tr = document.createElement('tr');
		tr.appendChild(td);
		table.appendChild(tr);
		var actualVal;
		if (isRuns) {
			actualVal='sc';
		} else {
			actualVal='ov';
		}
		//create the run slider
		spanCont = document.createElement('div');
		$(spanCont).slider({
	        value: val,
	        min: min,
	        max: max,
			step: 1,
	        slide: function(event, ui){
	            $(spanDisp).html(ui.value);
	            pObj[actualVal]=ui.value;
	            
	        },
			create: function(event, ui) {
				$(spanDisp).html(val);
				pObj[actualVal]=val;
			}

	    }).addClass('ui-state-focus');
		
		td = document.createElement('td');
		td.appendChild(spanCont);
		tr = document.createElement('tr');
		tr.appendChild(td);
		table.style['width']='100%'	;
		table.style['padding']='.5em 0.5em 0 .8em'	;
		table.appendChild(tr);

		spanCont = document.createElement('span');
		spanCont.appendChild(table);
		return spanCont;
	}
	var createOp1Object = function(){
		
		if(!isOp1Batting()) {
			return;
		}
		
		var op1Obj = {};
		
		var max = Math.floor(resp.op1MaxOv);
		var maxScore = MAX_POSSIBLE_SCORE;
		if (max < 25)
			maxScore = MAX_T20_SCORE;
		
		op1Obj.runs = getSliderTable(" Runs", parseInt(resp.op1Score), maxScore,op1Obj,true,Math.floor(resp.op1Overs));
		


		var overs = Math.ceil(resp.op1Overs)+deltaOvers;

		op1Obj.overs = getSliderTable(" Overs", overs, max,op1Obj,false,0 );
		
		op1Obj.teamName=resp.op1;
		op1Obj.minOv=overs;
		op1Obj.maxOv=max;
		
		predObjs.push(op1Obj);
		
	}
	
	var createOp2Object = function(){
		if(!isOp2Batting()) {
			return;
		}
		var op2Obj = {};
		
		var maxScore = MAX_POSSIBLE_SCORE;
		var max = Math.floor(resp.op2MaxOv);
		
		//The op1 has completed their innings
		if(predObjs.length == 0)
		{
			maxScore = resp.op1Score+6;
		} else if(max < 25) {
			maxScore = MAX_T20_SCORE;
		}
		op2Obj.runs = getSliderTable(" Runs", parseInt(resp.op2Score), maxScore,op2Obj,true,Math.floor(resp.op2Overs));
		


		var overs = Math.ceil(resp.op2Overs)+deltaOvers;

		op2Obj.overs = getSliderTable(" Overs", overs, max,op2Obj,false,0);
		
		op2Obj.teamName=resp.op2;
		op2Obj.minOv=overs;
		op2Obj.maxOv=max;
		
		predObjs.push(op2Obj);
		
	}
	var gameEndStr = function()	{
		return "Sorry! No predictions after the end of the match.";
	}
	
	var inningsEndStr = function(){
		return "Sorry! No predictions "+deltaOvers+" overs before the end of the innings.";
	}
	
	var getMatchEndStr = function(isEndMatch){
		var s = "";
		if (isEndMatch == false) {
			s = inningsEndStr();
		} else {
			s = gameEndStr(); 
		}
		return "<div style='margin:4em .5em 0 .5em;padding:.5em 0 .5em 0; border-style:none' class='ui-widget ui-widget-content ui-state-highlight' >"+s+"</div>";
	}
	var animSpan;
	var DISPLAY_NAME_LEN = 17;

	var getPredictionHTML = function (){
		var mainDiv = document.createElement('div');
		mainDiv.className='predictMainDiv';
		predObjs=[];
		currentIndex=0;
		//Get if op1 is still batting
		createOp1Object();
		createOp2Object();
		
		if (predObjs.length > 0) {
			var teamDiv = document.createElement('span');
			teamDiv.innerHTML ="";
			var i = 0;
			for ( i = 0; i < predObjs.length; i++) {
				var val = predObjs[i].teamName.replace(/ /g,"_");
				var ckeckedStr="";
				if (i==0)
					ckeckedStr="checked='checked'";
				teamDiv.innerHTML += "<input class='teamSelect' type='radio' name='teams' "+ckeckedStr+" value='"+val+"' id='rad_"+val+"'/><label for='rad_"+val+"'>"+predObjs[i].teamName.substring(0,DISPLAY_NAME_LEN)+"</label>";
			}
			
			var headerDiv = document.createElement('div');
			headerDiv.className='ui-widget ui-widget-content ui-state-highlight';
			$(headerDiv).addClass('predictHeader');
			headerDiv.innerHTML="<span class='predHeaderText'>Select Team: </span>";
			
			$(teamDiv).buttonset();
			$(teamDiv).css('font-size','0.7em');
			headerDiv.appendChild(teamDiv);
			var runsDiv = document.createElement('div');
			var oversDiv = document.createElement('div');
			currentIndex = 0;
			$(teamDiv).change(function(){
				var prevIndex = currentIndex;
				currentIndex = (currentIndex+1)%2;
				$(predObjs[prevIndex].runs).detach();
				runsDiv.appendChild(predObjs[currentIndex].runs);
				$(predObjs[prevIndex].overs).detach();
				oversDiv.appendChild(predObjs[currentIndex].overs);
			});

			mainDiv.appendChild(headerDiv);
			runsDiv.appendChild(predObjs[0].runs);
			oversDiv.appendChild(predObjs[0].overs);
			mainDiv.appendChild(runsDiv);
			mainDiv.appendChild(oversDiv);
			
			/*var table= document.createElement('table');
			table.style['width']='100%';
			var tr = document.createElement('tr');

			runsTd.appendChild(predObjs[0].runs);
			tr.appendChild(runsTd);
			table.appendChild(tr);
			var runsOversDiv = document.createElement('div');
			runsOversDiv.className='runsOversSliders';
	
			runsOversDiv.appendChild(table);
			
			//Second is the overs div
			table= document.createElement('table');
			table.style['width']='100%';
			tr = document.createElement('tr');

			oversTd.appendChild(predObjs[0].overs);
			tr.appendChild(oversTd);
			table.appendChild(tr);
			runsOversDiv.appendChild(table);
			
			mainDiv.appendChild(runsOversDiv);*/
			
			var predictButtDiv = document.createElement('div');
			$(predictButtDiv).css('text-align','center');
			
			animSpan = document.createElement('span');
			animSpan.className='countDown ui-state-highlight'; 
			predictButtDiv.appendChild(animSpan);
			$(animSpan).hide();
			
			var statusTextDiv = document.createElement('div');
			statusTextDiv.className='pendingStatus ui-widget ui-state-highlight ui-widget-content';
			statusTextDiv.innerHTML="Your request is being processed.<div>Please check the <span>My Predickets</span> tab for status.</div>";
			
			var predictButt = document.createElement('a');
			predictButt.href='javascript:&quot;&quot;';
			predictButt.innerHTML="Add Predicket";
			predictButt.id='predictButt';
			$(predictButt).button().click(function(obj){
				var curObj = predObjs[currentIndex];
				//set the msg below
				$(statusTextDiv).fadeIn(1000,function(){
					setTimeout(function(){
						$(statusTextDiv).fadeOut(1000);
					},2000);
				});

				//Extract the info and create the resp predicket object
				var pResp = {
						team:curObj.teamName,
						runs:curObj.sc,
						overs:curObj.ov,
						state: -1,
						timeStamp:0,
						riskOvers:curObj.ov-curObj.minOv,
						points:0,
						maxOv:curObj.maxOv
				};
				
				//Call the ajax method
				Predicket.addPredicket(pResp);

			});
			predictButtDiv.appendChild(predictButt);
			
			mainDiv.appendChild(predictButtDiv);
			

			mainDiv.appendChild(statusTextDiv);
			
			/*var footerDiv = document.createElement('div');
			$(footerDiv).css('margin','0 0 8em 0');
			mainDiv.appendChild(footerDiv);*/
			
		} else {
			mainDiv.innerHTML=getMatchEndStr(isMatchEnded);
		}
		return mainDiv;
	}
	
	var localListenerFunc;
	
	a.addContentHTML = function(panel){
		container = panel;
		localListenerFunc = this.listenerFunction;
		
		
		container.empty();
		//First the team names		
		container.addClass('gamePanel');
		container.addClass('tabContent');
		var scoreContainer = $('<div />');
		scoreContainer.addClass('scoreContainer');
		
		scoreContainer.append(Match.getTeamNames(resp));
		scoreContainer.append(Match.getScoreHTML(resp));
		scoreContainer.append("<div class='matStatusIns'>"+resp.status+"</div>");

		//Create the Table for players
		scoreContainer.append(Match.getPlayersHTML(resp));		
		container.append(scoreContainer);
		//Add the seperater Div
		container.append("<div class='ui-widget ui-widget-header gameSeparator'>"+"</div>");
		
		//Add the prediction div
		container.append(getPredictionHTML());
	}

	loadingImage = null;
	var createLoadingImage =  function(){
		if (loadingImage != null)
			return;
		loadingImage =document.createElement('div');
		loadingImage.className='loading-matches';
		loadingImage.style['text-align']='center';
		var img =document.createElement('img');
		img.src=CricFB.loadingImagePath;
		loadingImage.appendChild(img);
		return loadingImage;	
	}
	
	a.listenerFunction = function(){
		var newResp = null;
		var len = InitialUI.allMatchesKeyArr.length;
		for(var i=0;i<len;++i) {
			 if (InitialUI.allMatchesKeyArr[i]==resp.op1) {
				 newResp=InitialUI.allMatchesArr[i];
				 break;
			 }
		 }
		if (newResp==null) {
			for(var i=0;i<len;++i) {
				 if (InitialUI.allMatchesKeyArr[i]==resp.op2) {
					 newResp=InitialUI.allMatchesArr[i];
					 break;
				 }
			 }
		}
		if (newResp == null) {
			InitialUI.removeListener(localListenerFunc);
			resp=null;
			//TODO Remove the tab completely
			container.empty();
			//For now just show a message
			container.append(getMatchEndStr(true));
			return;
		}
		resp=newResp;

		//Empty all contents including slider 
		if(isMatchEnded == true) {
			InitialUI.removeListener(localListenerFunc);
		}
		if (predObjs.length == 0) {
			a.addContentHTML(container);
		} else {
			var count = 3;
			var mObj = this;
			var doAddContent = function(){
				a.addContentHTML(container);
			}
			
			var animateCountDown = function(){
				if (count == 0) {
					container.empty();
					container.append(loadingImage);
					setTimeout(doAddContent,1000);
				} else {
					animSpan.innerHTML = count;
					count--;
					setTimeout(animateCountDown,1000);
				}
				
			}
			$(animSpan).show();
			animateCountDown();
		}
	}

	createLoadingImage();
	setMatchEnded(false);
	return a;
	
}