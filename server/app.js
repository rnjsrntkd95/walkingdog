const createError = require("http-errors");
const express = require("express");
const path = require("path");
const cookieParser = require("cookie-parser");
const logger = require("morgan");
const session = require("express-session");
const bodyParser = require("body-parser");
const config = require("./config");
const User = require('./models/user');

// Router
const loginRouter = require("./routes/logins");
const animalRouter = require("./routes/animals");
const walkingRouter = require("./routes/walkings");
const postRouter = require("./routes/posts");
const challengeRouter = require("./routes/challenges");
const commentRouter = require("./routes/comment");

// MongoDB Connect
const mongodb = require("./db.js");
mongodb();

var app = express();

// Body Parser
app.use(bodyParser.json());
app.use(
  bodyParser.urlencoded({
    extended: false,
  })
);

// secret
app.set("jwt-secret", config.secret);

// User Token Decoding
app.use(async (req, res, next) => {
  console.log(req.path);
  if (req.path === "/login" || req.path === "/post/timeline"
    || req.path === "/walking/route" || req.path === "/animal/breed-list"
    || req.path === "/challenge/search" || req.path.includes("/uploads")) {
    next();
  } else {
    const Jwt = require("jsonwebtoken");
    let token = req.body.userToken;
    console.log(req.headers);
    console.log("토큰확인1" + token)
    if (!token) {
      token = req.query.userToken;
      console.log("토큰"+token)
    }
    console.log("토큰확인2" + token)

    if (!token) {
      token = req.headers.usertoken;
    }

    if (!token) {
      res.json({ error: 1004 });
      return
    }

    // 토큰 해석
    const secret = req.app.get("jwt-secret");
    const decodedToken = Jwt.decode(token, secret);
    try {
      let user = await User.findOne({ _id: decodedToken.id });
      console.log("////////////////////////////////////")
      console.log(`유저접속: ${decodedToken}, ${user.email}`);
      req.userToken = decodedToken;
      next();
    } catch (err) {
      res.json({ error: 1004 });
    }
  }
});

// Use Router
app.use("/login", loginRouter);
app.use("/animal", animalRouter);
app.use("/walking", walkingRouter);
app.use("/post", postRouter);
app.use("/challenge", challengeRouter);
app.use("/comment", commentRouter);

// view engine setup
app.set("views", path.join(__dirname, "views"));
app.set("view engine", "ejs");

app.use(logger("dev"));
app.use(express.json());
app.use(cookieParser());
app.use(express.static(path.join(__dirname, "public")));

// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get("env") === "development" ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render("error");
});

module.exports = app;
