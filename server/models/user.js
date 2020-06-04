var mongoose = require("mongoose");
var Schema = mongoose.Schema;

const userSchema = new Schema({
  email: {
    type: String,
    required: true,
    lowercase: true,
    validate: {
      validator: function (val) {
        var emailRegex = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
        console.log(emailRegex.test(val));
        return emailRegex.test(val);
      },
      message: "Email do not fit to regex",
    },
  },
  password: {
    type: String,
    required: true,
    validate: {
      validator: function (val) {
        return val.length <= 10 && val.length >= 5;
      },
      message: "password should be less than 5, longer than 15",
    },
  },
  nickname: {
    type: String,
    unique: true,
    sparse: true,
    validate: {
      validator: function (val) {
        var specialChar = /[!@#$%^&*(),.?":{}|<>]/;
        console.log(val);
        console.log(specialChar.test(val));
        return !(specialChar.test(val) || val.length > 10);
      },
      message:
        "There should be no special char and should be less than 10 char",
    },
  },
  created_date: {
    type: Date,
    default: Date.now,
  },
  profileImage: {
    type: String,
    default: "uploads\\default_profile.jpg",
  },
});

module.exports = mongoose.model("User", userSchema);
