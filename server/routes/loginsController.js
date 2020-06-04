// const crpyto = require('crpyto');

const User = require("../models/user");
const Animal = require("../models/animal");
const Breed = require("../models/breed");
const Jwt = require("jsonwebtoken");

exports.logIn = async (req, res, next) => {
  console.log(req.body);
  const email = req.body.email;
  const password = req.body.password;
  const secret = req.app.get("jwt-secret");

  try {
    let userObj = await User.findOne({ email, password });
    let nickname = null;

    if (!userObj) {
      // User를 DB에 저장
      const newUser = await new User({
        email,
        password,
      });
      const resultReg = await User.create(newUser);
      userObj = await User.findOne({ email, password });
    } else {
      //..?
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
      },
      function (err, loginToken) {
        if (err) res.json({ err });
        else res.json({ loginToken, nickname });
      }
    );
  } catch (err) {
    if (err.errors.password.message != undefined) {
      res.json({
        error: "2",
        message: err.errors.password.message,
      });
    } else if (err.errors.password.email.message != undefined) {
      res.json({
        error: "1",
        message: err.errors.email.message,
      });
    } else {
      res.json(err);
    }
  }
};

exports.setNickname = async (req, res, next) => {
  const userData = req.userToken;
  const nickname = req.body.nickname;
  const requestFiles = req.file;
  console.log("토큰값: " + req.userToken);
  console.log(req.body);
  //   if (!requestFiles) {
  //     image.push('uploads\\default.jpg')
  // } else {
  //     for (file of requestFiles) {
  //         image.push(file.path.replace('public\\', ''))
  //     }
  // }
  try {
    await User.findOneAndUpdate(
      { _id: "5ebac6bd59e3d32080d6d941" },
      { $set: { nickname } },
      {
        new: true,
        // findOneAndUpdate에서 validation을 하기 위해 추가.
        upsert: true,
        setDefaultsOnInsert: true,
        runValidators: true,
        context: "query",
      }
    );
    if (requestFiles) {
      await User.findOneAndUpdate(
        { _id: "5ebac6bd59e3d32080d6d941" },
        { $set: { profileImage: requestFiles.path.replace("public\\", "") } },
        { new: true }
      );
    }

    res.json({});
  } catch (err) {
    console.log(err);
    if (err.errors.nickname.message != undefined) {
      res.json({
        error: "3",
        message: err.errors.nickname.message,
      });
    }
    //res 중복으로 오류 뜰텐데 ㄱㅊ
    if (err.code === 11000) res.json({ error: 11000 });
    else res.json({ error: 10000 });
  }
};
