// forEachProperty
Object.prototype.forEachProperty = function(action) {
  for (var property in this) {
      action(property, this[property]);
  }
};

Object.prototype.forEachOwnProperty = function(action) {
  for (var property in this) {
    if (this.hasOwnProperty(property))
      action(property, this[property]);
  }
};

Object.prototype.forEachIn = Object.prototype.forEachOwnProperty;


// hasProperty
Object.prototype.hasProperty = function(x) {
    for (var p in this) {
        if (p === x) {
            return true;
        };
    };
    return false;
};

// hasMethod
Object.prototype.hasMethod = function(name) {
    return this.hasProperty(name) && (typeof this[name] === "function");
};

Object.prototype.hasOwnMethod = function(name) {
    return this.hasOwnProperty(name) && (typeof this[name] === "function");

};

// getProperties
Object.prototype.getProperties = function() {
  var a = [];
  for (var p in this) {
      a.push(p);
  };
  return a;
};

Object.prototype.getOwnProperties = function() {
  var a = [];
  for (var p in this) {
      if (this.hasOwnProperty(p)) a.push(p);
  };
  return a;
}



//   return this.getAllProperties().
//      filter(function(x) {
//               return that.hasOwnProperty(x);
//             });

// Object.prototype.run = function(m) {
//     var  method =  this[m];
//     return m.apply(this, arguments);
// };




