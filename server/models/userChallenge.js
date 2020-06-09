var mongoose = require("mongoose");
var Schema = mongoose.Schema;

const userChallengeSchema = new Schema({
  userId: {
    type: Schema.Types.ObjectId,
    ref: "User",
    required: true,
    index: true,
  },
  challengeId: {
    type: String,
    required: true,
  },
  score: {
    type: Number,
    required: true,
    default: 0,
  },
  walkingCount: {
    type: Number,
    required: true,
    default: 0,
  },
  walkingAvg: {
    type: Number,
    required: true,
    default: 0,
  },
});

module.exports = mongoose.model("userChallege", userChallengeSchema);
