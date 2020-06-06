var express = require("express");
var router = express.Router();
var controller = require("./challengeController.js");

// 새로운 챌린지 등록
router.post("/new", controller.createChallenge);
// 챌린지 참가
router.post("/join", controller.joinChallenge);
// 챌린지 리스트 검색
router.get("/search", controller.searchChallenge);
// 챌린지 탈퇴: 유저의 중도 포기
router.delete("/drop", controller.dropChallenge);

module.exports = router;
