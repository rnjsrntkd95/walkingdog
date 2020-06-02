var express = require("express");
var router = express.Router();
var controller = require("./commentController");

// 댓글 조회
router.get("/list", controller.getCommentList);

// 새 댓글 등록
router.post("/new", controller.createComment);

module.exports = router;
