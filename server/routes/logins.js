var express = require('express');
var router = express.Router();

const User = require('../models/user');
const Animal = require('../models/animal');
const Breed = require('../models/breed');


// Login
router.get('/', async function(req, res, next) {
    try {
        const breed_id = await Breed.findOne({breed: '푸들'})
        const create_result = await Animal.create({
            animalName: '까까',
            breed: breed_id,
            birth: '2015-01-10',
            weight: 30,
        })
        console.log(breed_id)
        console.log(create_result)
        res.json(create_result)
    } catch (err) {
        console.log('error')
        console.log(err.code)
        res.json(err.code)
    }
});

// Google Login Page
router.get('/google', function(req, res, next) {

});

//Google Login이 성공적으로 완료되면 돌아오는 페이지
router.get('/google/callback', function(req, res, next) {

});

module.exports = router;
