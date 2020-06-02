var express = require('express');
var router = express.Router();
var controller = require('./loginsController');
const multer = require('multer');

var storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, 'public/uploads/users') // cb 콜백함수를 통해 전송된 파일 저장 디렉토리 설정
    },
    filename: function (req, file, cb) {
      cb(null, Date.now() + '_' + file.originalname) // cb 콜백함수를 통해 전송된 파일 이름 설정
    }
  })

const upload = multer({
    storage: storage,
    limits: { filesize: 5 *  1024 * 1024 }
});




// Login
router.post('/', controller.logIn);
// Set Nickname and Profile Image
router.put('/nickname',upload.single('img') , controller.setNickname);

module.exports = router;
