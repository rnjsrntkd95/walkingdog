var mongoose = require("mongoose");
var Schema = mongoose.Schema;

const userChallengeSchema = new Schema({
  userId: {
    type: Schema.Types.ObjectId,
    ref: "User",
    required: true,
  },
  challengeId: {
    type: String,
    required: true,
  },
});

module.exports = mongoose.model("userChallenge", userChallengeSchema);
