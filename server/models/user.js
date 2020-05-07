var mongoose = require("mongoose");
var Schema = mongoose.Schema;

const userSchema = new Schema({
  email: {
    type: String,
    required: true,
    unique: true,
    lowercase: true,
  },
  password: {
    type: String,
    required: true,
  },
  username: {
    type: String,
    required: true,
    lowercase: true,
  },
  nickname: {
    type: String,
    required: true,
    unique: true,
  },
  phone: {
    type: Number,
    required: true,
    unique: true,
  },
  created_date: {
    type: Date,
    default: Date.now,
  },
});

userSchema.statics.create = function (
  username,
  password,
  nickname,
  email,
  phone
) {
  const user = new this({
    username,
    password,
    nickname,
    email,
    phone,
  });
  // save user in database and return
  return user.save();
};

userSchema.statics.findOneByUsername = function (username) {
  return this.findOne({
    username,
  }).exec();
};

userSchema.methods.verify = function (password) {
  if (this.password === password) return true;
  else return false;
};

module.exports = mongoose.model("User", userSchema);
