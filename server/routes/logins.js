var express = require('express');
var router = express.Router();
var controller = require('./loginsController');




// Login
router.get('/', controller.login);

// Google Login Page
router.get('/google', function(req, res, next) {

});

//Google Login이 성공적으로 완료되면 돌아오는 페이지
router.get('/google/callback', function(req, res, next) {

});

module.exports = router;
