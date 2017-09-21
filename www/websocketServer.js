var exec = require('cordova/exec');

exports.startServer = function(arg0, arg1, success, error) {
    exec(success, error, "websocketServer", "startServer", [arg0,arg1]);
};
