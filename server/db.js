const mongoose = require('mongoose');

var db = function () {
    function connect() {
        const url = 'mongodb://localhost:27017/walkingdog';
        mongoose.connect(url, {useNewUrlParser: true, useUnifiedTopology : true,
        }, function(err) {
            if (err) {
                console.error('mongodb connection error', err);
            }
            console.log('mongodb connected')
        });
        mongoose.set('useCreateIndex', true);
    }
    connect();
    mongoose.connection.on('disconnected', connect);
    
    require('./models/user.js');
}

module.exports = db;