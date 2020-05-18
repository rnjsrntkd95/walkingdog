var express = require('express');
var router = express.Router();
var controller = require('./postController');

// Create:새로운 게시글 등록
router.post('/create', controller.createPost);
// 타임라인 조회
router.get('/timeline', controller.timeline);

module.exports = router;
