/**
 * @author Subramanyan Murali subram@yahoo-inc.com
 * @class remoteData
 * @Usage
 *
 * YAHOO.remoteData.fetch(url, callBack, scopeObj, expectigJsonCallback);
 *
 * expectingJsonCallBack is a boolean when set to true, will add &format=json&callback=YAHOO.remoteData.handle
 * if set to false, then you will need to set the call back to YAHOO.remoteData.handle yourself in the url passed
 * default is true
 *
 * If the scope object is not specified, it will be set to window
 */


   // Remote Data name space
   YAHOO.namespace("remoteData");
   // Singleton to handle all the cross domain request
   YAHOO.remoteData = function()
   {
     // the callback object that will be given by the user
     var callBackObj = null;
     // Scope with which the user wants the callback to be called
     var scopeObj = null;

     var _id = "data_fetch_script_"+Math.ceil(Math.random()*1000);

     // Fetch function using the dynamic script node attach
     this.fetch = function(url, callBack, scope, useJson)
     {
      if(typeof(useJson) == "undefined")
         useJson = true;
      if(typeof(url) != "string" || url == "")
         alert("Url is needed for fetching data");

      // Assign the global callback and scope objects
      callBackObj = callBack;
      scopeObj    = (scope)?scope:window;
      var scr = null;
      if(document.getElementById(_id) == null)
      {
         scr = document.createElement("script");
         scr.type="text/javascript";
      }
      else
         scr = document.getElementById(_id);

      if(useJson)
         url = url + "&format=json&callback=YAHOO.remoteData.handle";
      // Cross domain request that returns the data wrapped in the callback function
      //YAHOO.remoteData.handle({'........'})
      scr.src= url;

      scr.id = _id;
      document.body.appendChild(scr);
     }
     // Handle function that acts as the standard call back
     this.handle = function(o)
     {
      if(!callBackObj)
         return;
      callBackObj.call(scopeObj, o);
      var _src = document.getElementById(_id);
      _src.parentNode.removeChild(_src);
     }
     return this;
   }();