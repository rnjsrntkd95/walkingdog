// const crpyto = require('crpyto');

const User = require("../models/user");
const Animal = require("../models/animal");
const Breed = require("../models/breed");
const Jwt = require("jsonwebtoken");

exports.logIn = async (req, res, next) => {
  const email = req.body.email;
  const password = req.body.password;
  const secret = req.app.get("jwt-secret");

  try {
    let userObj = await User.findOne({ email, password });
    let nickname = null

    if (!userObj) {
      // User를 DB에 저장
      const newUser = await new User({
        email,
        password,
      });
      const resultReg = await User.create(newUser);
      userObj = await User.findOne({ email, password });
    } else {
        nickname = userObj.nickname
    }
    // 토큰 발행
    Jwt.sign(
      {
        id: userObj._id,
        email: userObj.email,
        created_date: userObj.created_date,
      },
      secret,
      {
        expiresIn: "1d",
        issuer: "walkingDog",
        subject: "userInfo",
      },function(err, loginToken) {
        if (err) res.json({})
        else res.json({ loginToken, nickname });
      }
    );
} catch (err) {
    console.log(err);
    res.json({});
  }
};

exports.setNickname = async (req, res, next) => {
    const userData = req.userToken;
    const nickname = req.body.nickname;

    try {
        const user = await User.findOneAndUpdate({_id: "5eba8b5ca76e3e20f4b0659e"}, {nickname});
        res.json({});
    } catch (err) {
        console.log(err);
        if (err.code === 11000) res.json({error: 11000})
        else res.json({error: 10000});
    }
};
