// const crpyto = require('crpyto');

const User = require('../models/user');
const Animal = require('../models/animal');
const Breed = require('../models/breed');

exports.logIn = (req, res, next) => {
    // const token = req.token;
    const email = req.email;
    console.log(email)
    const password = req.password;
    res.json({loginToken: "1111111"});
    if (!token) {
        const context ={
            email,
            password,
        }
        res.json(context);
    }
};

exports.signUp = (req, res, next) => {
    // const email = req.body.email;
    // const password = req.body.password;

    // const salt = Math.round((new Date().valueOf() * Math.random())) + "";
    // const hashPassword = crypto.createHash("sha512").update(password + salt)
    

    
}