var express = require('express');
var router = express.Router();
var controller = require('./postController');
const multer = require('multer');

var storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, 'public/uploads/posts') // cb 콜백함수를 통해 전송된 파일 저장 디렉토리 설정
    },
    filename: function (req, file, cb) {
      cb(null, Date.now() + '_' + file.originalname) // cb 콜백함수를 통해 전송된 파일 이름 설정
    }
  })

const upload = multer({
    storage: storage,
    limits: { filesize: 5 *  1024 * 1024 }
});


// Create:새로운 게시글 등록
router.post('/create', upload.array('img'), controller.createPost);
// 타임라인 조회
router.get('/timeline', controller.timeline);


module.exports = router;
