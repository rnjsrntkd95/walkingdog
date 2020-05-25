var mongoose = require("mongoose");
var Schema = mongoose.Schema;

const userSchema = new Schema({
  email: {
    type: String,
    required: true,
    lowercase: true,
  },
  password: {
    type: String,
    required: true,
  },
  nickname: {
    type: String,
    unique: true,
    sparse: true,
  },
  created_date: {
    type: Date,
    default: Date.now,
  },
  profileImage: {
    type: String,
    default: 'uploads\\default_profile.jpg'
  }
});

module.exports = mongoose.model("User", userSchema);
