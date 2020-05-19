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
        mongoose.set('useFindAndModify', false);
    }
    connect();
    mongoose.connection.on('disconnected', connect);

    require('./models/user');
    require('./models/breed');
    require('./models/animal');
    require('./models/walking');
    require('./models/post');
    require('./models/comment');
    require('./models/challenge');
    require('./models/record');
    require('./models/userChallenge');

}

module.exports = db;