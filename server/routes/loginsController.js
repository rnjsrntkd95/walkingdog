// const crpyto = require('crpyto');

const User = require('../models/user');
const Animal = require('../models/animal');
const Breed = require('../models/breed');

exports.logIn = async (req, res, next) => {
    const email = req.body.email;
    const password = req.body.password;

    try {
        const userObj = await User.findOne({ email, password });

        if (!userObj) {
            // User를 DB에 저장
            const newUser = await new User({
                email,
                password,
            });
            const resultReg = await User.create(newUser);
        }
        // 토큰 발행
        res.json({ loginToken: "1111111", error: 0 })

    } catch(err) {
        console.log(err)
        res.json({ loginToken: "0000000", error: 1 })
    }
};

exports.signUp = (req, res, next) => {
    // const email = req.body.email;
    // const password = req.body.password;

    // const salt = Math.round((new Date().valueOf() * Math.random())) + "";
    // const hashPassword = crypto.createHash("sha512").update(password + salt)
    

    
}