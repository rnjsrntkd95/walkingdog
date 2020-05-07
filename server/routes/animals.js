var express = require('express');
var router = express.Router();
var controller = require('./animalsController');


// 등록된 애견 확인
router.get('/mypet', controller.searchPet);


// 새로운 애견 등록
router.get('/register', controller.register);

module.exports = router;
