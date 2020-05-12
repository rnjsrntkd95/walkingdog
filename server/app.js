const createError = require("http-errors");
const express = require("express");
const path = require("path");
const cookieParser = require("cookie-parser");
const logger = require("morgan");
const session = require("express-session");
const bodyParser = require("body-parser");
const config = require("./config");

// Router
var loginRouter = require("./routes/logins");
var animalRouter = require("./routes/animals");
var indexRouter = require("./routes/index");

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

// Use Router
app.use("/", indexRouter);
app.use("/login", loginRouter);
app.use("/animal", animalRouter);

// Session Setup
// app.use(session({
//   key: 'sid'
// }))

// view engine setup
app.set("views", path.join(__dirname, "views"));
app.set("view engine", "ejs");

app.use(logger("dev"));
app.use(express.json());
// app.use(express.urlencoded({ extended: true }));
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
