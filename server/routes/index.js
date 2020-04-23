var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.get('/message', function(req, res, next) {
  console.log('진입');
  res.json({ appname: 'walkingdog'});
})

router.post('/message', function(req, res, next) {
  console.log('POST 진입')
  res.json({
    appname: 'kusang'
  })
})

module.exports = router;
