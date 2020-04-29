var express = require('express');
var router = express.Router();

const User = require('../models/user');

// Login
router.get('/', function(req, res, next) {
    User.create({
        email: 'rnjsrntkasdfad@gmail.com',
        password: 'admindasdfadddd',
        username: '권구상ddddddafadd',
        nickname: '구상이dddddasdfasdd',
        phone: '010950825260000111',
    }, function (err, user) {
        
        if (err) return console.log(err.errmsg);
        res.status(200).send(user);
    });
});

// Google Login Page
router.get('/google', function(req, res, next) {

});

//Google Login이 성공적으로 완료되면 돌아오는 페이지
router.get('/google/callback', function(req, res, next) {

});

module.exports = router;
