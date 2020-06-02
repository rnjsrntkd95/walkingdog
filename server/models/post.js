var mongoose = require("mongoose");
var Schema = mongoose.Schema;

const postSchema = new Schema({
  content: {
    type: String,
  },
  like: {
    type: Number,
    default: 0,
  },
  image: [
    {
      type: String,
      // type: Buffer,
      // contentsType: String,
    },
  ],
  created_date: {
    type: Date,
    default: Date.now,
  },
  calorie: {
    type: Number,
    required: true,
    default: 0,
  },
  distance: {
    type: Number,
    required: true,
    default: 0,
  },
  routeImage: {
    type: String,
    // type: Buffer,
    // contentsType: String,
  },
  walkingTime: {
    type: Number,
    required: true,
  },
  addressAdmin: {
    type: String,
  },
  addressLocality: {
    type: String,
  },
  addressThoroughfare: {
    type: String,
  },
  animal: [
    {
      type: String,
    },
  ],
  user: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "User",
    required: true,
  },
  nickname: {
    type: String,
    required: true,
  },
});

module.exports = mongoose.model("Post", postSchema);
