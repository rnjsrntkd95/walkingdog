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
  },
  nickname: {
    type: String,
    default: "주인님",
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
    default: "uploads\\default_profile.png",
  },
});

module.exports = mongoose.model("User", userSchema);
