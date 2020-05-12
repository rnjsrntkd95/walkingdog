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

    if (!userObj) {
      // User를 DB에 저장
      const newUser = await new User({
        email,
        password,
      });
      const resultReg = await User.create(newUser);
      userObj = await User.findOne({ email, password });
    }
    // 토큰 발행
    const token = Jwt.sign(
      {
        email: userObj.email,
        created_date: userObj.created_date,
      },
      secret,
      {
        expiresIn: "1d",
        issuer: "walkingDog",
        subject: "userInfo",
      },
      function (err, token) {
        if (err) console.log(err);
        console.log(token);
        res.json({ token: token, error: 0 });
      }
    );
  } catch (err) {
    console.log(err);
    res.json({ loginToken: "0000000", error: 1 });
  }
};

exports.signUp = (req, res, next) => {
  // const email = req.body.email;
  // const password = req.body.password;
  // const salt = Math.round((new Date().valueOf() * Math.random())) + "";
  // const hashPassword = crypto.createHash("sha512").update(password + salt)
};
