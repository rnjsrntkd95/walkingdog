const mongoose = require('mongoose');

var db = () => {
    function connect() {
        mongoose.connect('localhost:27017', err => {
            if(err) {
                console.error('mongodb connection error', err);
            }
            console.log('mongodb connected');
        });
    }
    connect();
    mongoose.connection.on('disconnected', connect);
};

module.exports = db;