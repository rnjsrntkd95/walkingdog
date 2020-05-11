var mongoose = require('mongoose');
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
    nickname: {
        type: String,
        unique: true,
        default: null
    },
    created_date: {
        type: Date,
        default: Date.now,
    },
    // username: {
    //     type: String,
    //     required: true,
    //     lowercase: true,
    // },
})

module.exports = mongoose.model('User', userSchema);