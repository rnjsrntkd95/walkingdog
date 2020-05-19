var mongoose = require('mongoose');
var Schema = mongoose.Schema;

const userChallengeSchema = new Schema({
    userId: {
        type: String,
        required: true,
    },
    challengeId: {
        type: String,
        required: true,
    }
})

module.exports = mongoose.model('userChallege', userChallengeSchema);