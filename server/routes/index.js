var express = require("express");
const controller = require("./authController");
var router = express.Router();

/* GET users listing. */
router.get("/", function (req, res, next) {
  res.render("index", { title: "Hello World!" });
});

router.post("/register", controller.register);
router.post("/login", controller.login);
router.get("/check", controller.check);

module.exports = router;
