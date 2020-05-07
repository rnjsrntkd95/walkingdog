const User = require("../models/user");
const jwt = require("jsonwebtoken");

exports.register = function (req, res) {
  console.log(req.body);
  const { username, password, nickname, email, phone } = req.body;
  var newUser = null;

  const create = function (user) {
    if (user) throw new Error("username exists");
    else return User.create(username, password, nickname, email, phone);
  };

  const count = function (user) {
    newUser = user;
    return User.count({}).exec();
  };

  const respond = function () {
    res.json({
      message: "successfully registered",
    });
  };

  const onError = function (error) {
    res.status(409).json({
      message: error.message,
    });
  };

  // check username duplication
  User.findOneByUsername(username)
    .then(create)
    .then(count)
    .then(respond)
    .catch(onError);
};

exports.login = function (req, res) {
  const { username, password, nickname } = req.body;
  const secret = req.app.get("jwt-secret"); // secret 방식

  const check = function (user) {
    if (!user) {
      throw new Error("login failed");
    } else {
      console.log(user);
      if (true) {
        //user.verify(password)
        const p = new Promise(function (resolve, reject) {
          jwt.sign(
            {
              _id: user._id, //id, email, nickname will be in walking dog
              username: user.username,
              nickname: user.nickname,
            },
            secret,
            {
              expiresIn: "7d",
              issuer: "walkingdog",
              subject: "userInfo",
            },
            function (err, token) {
              if (err) reject(err);
              resolve(token);
            }
          );
        });
        return p;
      } else {
        throw new Error("login failed");
      }
    }
  };

  const respond = function (token) {
    res.json({
      message: "logged in successfully",
      token,
    });
  };

  const onError = function (error) {
    res.status(403).json({
      message: error.message,
    });
  };

  User.findOneByUsername(username).then(check).then(respond).catch(onError);
};

exports.check = function (req, res) {
  const token = req.headers["x-access-token"] || req.query.token;

  if (!token) {
    return res.status(403).json({
      success: false,
      message: "not logged in",
    });
  }

  const p = new Promise(function (resolve, reject) {
    jwt.verify(token, req.app.get("jwt-secret"), function (err, decoded) {
      if (err) reject(err);
      resolve(decoded);
    });
  });

  const respond = function (token) {
    res.json({
      success: true,
      info: token,
    });
  };

  const onError = function (error) {
    res.status(403).json({
      success: false,
      message: error.message,
    });
  };
  p.then(respond).catch(onError);
};
