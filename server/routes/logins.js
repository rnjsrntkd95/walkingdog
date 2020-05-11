var express = require('express');
var router = express.Router();
var controller = require('./loginsController');




// Login
router.post('/', controller.logIn);

module.exports = router;
