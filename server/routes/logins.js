var express = require('express');
var router = express.Router();
var controller = require('./loginsController');




// Login
router.post('/', controller.logIn);

// SignUp
router.post('/signup', controller.signUp);

module.exports = router;
