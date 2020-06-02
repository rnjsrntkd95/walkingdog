const createError = require("http-errors");
const express = require("express");
const path = require("path");
const cookieParser = require("cookie-parser");
const logger = require("morgan");
const session = require("express-session");
const bodyParser = require("body-parser");
const config = require("./config");

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
    extended: true,
  })
);

// secret
app.set("jwt-secret", config.secret);

// User Token Decoding
app.use((req, res, next) => {
  console.log(req.path);
  if (req.path === "/login") {
    next();
  } else {
    const Jwt = require("jsonwebtoken");
    const token = req.body.userToken;
    // if (!token) res.json({ error: -1 })
    const secret = req.app.get("jwt-secret");
    const decodedToken = Jwt.decode(token, secret);
    req.userToken = decodedToken;
    next();
  }
});

// Use Router
app.use("/testPost", loginRouter);
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
