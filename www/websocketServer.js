var exec = require('cordova/exec');
exports.startServer = function(host,port, success, error) {
    exec(success, error, "websocketServer", "startServer", [host,port]);
};

exports.stopServer = function(success, error) {
    exec(success, error, "websocketServer", "stopServer", []);
};