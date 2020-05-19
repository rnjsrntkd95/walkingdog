var express = require('express');
var router = express.Router();
var controller = require('./loginsController');




// Login
router.post('/', controller.logIn);
// Set Nickname
router.put('/nickname', controller.setNickname);

module.exports = router;
