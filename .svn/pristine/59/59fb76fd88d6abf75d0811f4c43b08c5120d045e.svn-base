
var CricFB = {};


CricFB.isAuthenticated = false;

 
CricFB.facebookLogin = function()
{
  FB.login(CricFB.onFacebookLoginStatus, {});
}

CricFB.onUserAuthenticated = function(session) {
	$(document).ready(function(){
		if (CricFB.isAuthenticated == true)
			return;
		CricFB.isAuthenticated = true;
		document.getElementById("fb-login-button-div").style.display="none";
	    
	    //Store the User Id here, but later we need to use the FB.getSession Method
	    CricFB.uid=session.userID;
	    
	    //Get the User Id and send it to our server
	    CricFB.matchTabs = $('#leftPane').tabs({
	    	tabTemplate: "<li><a class='tabHeadLeft' href='#{href}'>#{label}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>",
	    	add: InitialUI.addTabHandler
		});
	    $( "#leftPane span.ui-icon-close" ).live( "click", function() {
			var index = $( "li", CricFB.matchTabs ).index( $( this ).parent() );
			InitialUI.removeListener($(this).parent()[0].childNodes[0].listenerFunc);
			CricFB.matchTabs.tabs( "remove", index );
		});
	    $('a.links').button();
	    $('#gameModes').buttonset();
	    $('#rightPane').tabs();
	    $('#left-header').css('width','32%');
	    $('#header-links').show();
	    $('#global-container').show();
	    
	    //Add listeners
	   	InitialUI.listenerArr=[InitialUI.displayMatches];
	   	
		//$('#matches').empty();
		$('#matches').append(InitialUI.getLoadingImage());
		
		//$('#myList').empty();
		$('#myList').append(Predicket.getLoadingImage());
		
	    //This is wher we also retrieve the predicts
	    InitialUI.userLogin(session.userID);
	    

	   	
	});
}
CricFB.loadingImagePath = "../css/bg-images/loading.gif";

/*
 * Callback function for FB.getLoginStatus
 */
CricFB.onFacebookInitialLoginStatus  = function(response)
{
  if (response.status=="connected" && response.authResponse)
  {
	  CricFB.onUserAuthenticated(response.authResponse);
  }
  else
  {
	  CricFB.facebookLogin();
  }
}

/*
 * Callback function for 'auth.statusChange' event.
 */
CricFB.onFacebookStatusChange  = function(response)
{
	if (response.status=="connected" && response.authResponse)
		CricFB.onUserAuthenticated(response.authResponse);
}

/*
 * Callback function for FB.login
 */
CricFB.onFacebookLoginStatus  = function(response)
{
   if (response.status=="connected" && response.authResponse)
  {
	   CricFB.onUserAuthenticated(response.authResponse);
  }
  else
  {
    //Could be the user denied access... Just leave the loser alone for now.
  }
}

CricFB.loadFacebookSDK = function(elementName) {
	CricFB.isAuthenticated = false;
	  window.fbAsyncInit = function()
	  {
		//TODO: in future auto detect if user logged out
		  //And call location.reload(); to refresh the page
	    //FB.Event.subscribe('auth.statusChange', CricFB.onFacebookStatusChange);
		  FB.Event.subscribe('auth.login', CricFB.onFacebookLoginStatus);
	    
	    //Appid predicket: 199335020094883
	    //Appid predicket-test: 146799498715662
	    FB.init({
	      appId  : '199335020094883',
	      status : true, // check login status
	      cookie : true, // enable cookies to allow the server to access the session
	      xfbml  : true  // parse XFBML
	    });

	   FB.getLoginStatus(CricFB.onFacebookInitialLoginStatus);
	  };
	  
  var e = document.createElement('script');
  e.src = document.location.protocol + '//connect.facebook.net/en_US/all.js';
  e.async = true;
  document.getElementById(elementName).appendChild(e);
}
