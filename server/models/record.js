var mongoose = require('mongoose');
var Schema = mongoose.Schema;

const recordSchema = new Schema({
    walkingCount: {
        type: Number,
        default: 0,
    },
    walkingAvg: {
        type: Number,
        default: 0,
    },
    score: {
        type: Number,
        default: 0,
    },
    user: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true,
    },
    challenge: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Challenge',
        required: true,
    },
    challengeTitle: {
        type: String,
    }
})

module.exports = mongoose.model('Record', recordSchema);