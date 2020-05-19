var express = require('express');
var router = express.Router();
var controller = require('./walkingController');

// Create:새로운 산책 정보 등록
router.post('/create', controller.createWalking);

module.exports = router;
